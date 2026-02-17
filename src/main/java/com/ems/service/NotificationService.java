package com.ems.service;

import com.ems.dto.request.NotificationRequest;
import com.ems.dto.response.NotificationResponse;
import com.ems.exception.ResourceNotFoundException;
import com.ems.model.*;
import com.ems.repository.NotificationRepository;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private MailSender mailSender;

    @Transactional
    public NotificationResponse createNotification(NotificationRequest request){
        User user=userRepository.findById(request.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User not found with id:"+request.getUserId()));

        Notification notification=new Notification();
        notification.setUser(user);
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setIsRead(false);
        notification.setIsSent(false);

        Notification savedNotification=notificationRepository.save(notification);

        //send notification
        if (request.getType() == NotificationType.EMAIL){
            sendEmailAsync(user,request.getSubject(),request.getMessage(),savedNotification.getId());
        }

        return convertToResponse(savedNotification);

    }


    @Async
    public void sendEmailAsync(User user,String subject,String message,Long notificationId){
        try {
            sendEmail(user.getEmail(),subject,message);

            //Update notification as sent
            notificationRepository.findById(notificationId).ifPresent(notification -> {
                notification.setIsSent(true);
                notification.setSentAt(LocalDateTime.now());
                notificationRepository.save(notification);
            });
        } catch (Exception e) {
            System.err.println("Failed to send email: "+e.getMessage());
        }
    }

    public void sendEmail(String to,String subject,String text){
        if (mailSender==null){
            System.out.println("Email not configured. Would send: "+subject+" to "+to);
            return;
        }

        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("noreply@eventmanagement.com");

        mailSender.send(message);
    }

    @Async
    public void sendBookingConfirmation(Booking booking){
        String subject="Booking Confirmation - "+booking.getEvent().getTitle();
        String message=String.format(
                """
                        Dear %s,\s
                        
                        Your booking has been confirmed!
                        
                        Booking Reference: %s
                        Event: %s
                        Date: %s
                        Quantity: %d
                        Total Amount: ₹%.2f
                        
                        Thank you for your booking!
                        
                        Best regards,
                        Event Management Team""",

                booking.getUser().getUsername(),
                booking.getBookingReference(),
                booking.getEvent().getTitle(),
                booking.getEvent().getStartDateTime(),
                booking.getQuantity(),
                booking.getTotalAmount()
        );

        createAndSendNotification(booking.getUser(),subject,message,NotificationType.EMAIL);
    }


    @Async
    public void sendEventReminder(Event event, List<User> attendees){
        String subject="Event Reminder - "+ event.getTitle();
        String message=String.format(
                """
                        This is the reminder that the event '%s' is scheduled for %s.\s
                        
                        Venue: %s
                        Don't forget to bring your booking confirmation!
                        
                        See you there!""",
                event.getTitle(),
                event.getStartDateTime(),
                event.getVenue() != null ? event.getVenue().getName(): "TBD"
        );

        for (User user:attendees){
            createAndSendNotification(user,subject,message,NotificationType.EMAIL);
        }
    }


    @Async
    public void sendBookingCancellation(Booking booking){
        String subject="Booking Cancelled - "+booking.getEvent().getTitle();
        String message=String.format(
                """
                        Dear %s,\s
                        
                        Your booking has been cancelled.\s
                        
                        Booking Reference: %s
                        Event: %s
                        Refund Amount: ₹%.2f
                        
                        The refund will be processed within 5-7 business days.\s
                        
                        Best regards,\s
                        Event Management Team""",

                booking.getUser().getUsername(),
                booking.getBookingReference(),
                booking.getEvent().getTitle(),
                booking.getTotalAmount()
        );

        createAndSendNotification(booking.getUser(),subject,message,NotificationType.EMAIL);
    }


    private void createAndSendNotification(User user,String subject,String message,NotificationType type){
        Notification notification=new Notification();
        notification.setUser(user);
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        notification.setIsSent(false);

        Notification savedNotification=notificationRepository.save(notification);
        if (type==NotificationType.EMAIL){
            sendEmailAsync(user,subject,message, savedNotification.getId());
        }
    }


    public List<NotificationResponse> getUserNotification(User user){
        return notificationRepository.findByUser(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadNotification(User user){
        return notificationRepository.findByUserAndIsReadFalse(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public void markAsRead(Long notificationId,User user){
        Notification notification=notificationRepository.findById(notificationId)
                .orElseThrow(()->new ResourceNotFoundException("Notification not found with notification id: "+notificationId));

        if (!notification.getUser().getId().equals(user.getId())){
            throw new ResourceNotFoundException("Notification not found");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }


    @Transactional
    public void markAllAsRead(User user){
        List<Notification> notifications=notificationRepository.findByUserAndIsReadFalse(user);
        notifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }



    private NotificationResponse convertToResponse(Notification notification){
        NotificationResponse response=new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(notification.getUser().getId());
        response.setUsername(notification.getUser().getUsername());
        response.setSubject(notification.getSubject());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType());
        response.setIsRead(notification.getIsRead());
        response.setIsSent(notification.getIsSent());
        response.setSentAt(notification.getSentAt());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }

}
