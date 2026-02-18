package com.ems.util;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplate {

    public String generateBookingConfirmationEmail(String username,String bookingReference,String eventTitle,String eventDate,Integer quantity,String totalAmount){
        return String.format(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head><style>body{font-family:Arial,sans-serif;}</style></head>" +
                        "<body>" +
                        "<h2>Booking Confirmation</h2>" +
                        "<p>Dear %s,</p>" +
                        "<p>Your booking has been successfully confirmed!</p>" +
                        "<table border='1' cellpadding='10' style='border-collapse:collapse;'>" +
                        "<tr><td><strong>Booking Reference</strong></td><td>%s</td></tr>" +
                        "<tr><td><strong>Event</strong></td><td>%s</td></tr>" +
                        "<tr><td><strong>Date</strong></td><td>%s</td></tr>" +
                        "<tr><td><strong>Tickets</strong></td><td>%d</td></tr>" +
                        "<tr><td><strong>Total Amount</strong></td><td>₹%s</td></tr>" +
                        "</table>" +
                        "<p>Please keep this booking reference for check-in.</p>" +
                        "<p>Thank you for choosing our service!</p>" +
                        "<p>Best regards,<br>Event Management Team</p>" +
                        "</body></html>",
                username, bookingReference, eventTitle, eventDate, quantity, totalAmount

        );

    }



    public String eventReminderEvent(String username,String eventTitle,String eventDate,String venueName){
        return String.format(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head><style>body{font-family:Arial,sans-serif;}</style></head>" +
                        "<body>" +
                        "<h2>Event Reminder</h2>" +
                        "<p>Dear %s,</p>" +
                        "<p>This is a friendly reminder about your upcoming event:</p>" +
                        "<table border='1' cellpadding='10' style='border-collapse:collapse;'>" +
                        "<tr><td><strong>Event</strong></td><td>%s</td></tr>" +
                        "<tr><td><strong>Date & Time</strong></td><td>%s</td></tr>" +
                        "<tr><td><strong>Venue</strong></td><td>%s</td></tr>" +
                        "</table>" +
                        "<p>Don't forget to bring your booking confirmation!</p>" +
                        "<p>We look forward to seeing you there!</p>" +
                        "<p>Best regards,<br>Event Management Team</p>" +
                        "</body></html>",
                username, eventTitle, eventDate, venueName
        );
    }

    public String generatePasswordResetEmail(String username, String resetLink) {
        return String.format(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head><style>body{font-family:Arial,sans-serif;}</style></head>" +
                        "<body>" +
                        "<h2>Password Reset Request</h2>" +
                        "<p>Dear %s,</p>" +
                        "<p>We received a request to reset your password.</p>" +
                        "<p>Click the link below to reset your password:</p>" +
                        "<p><a href='%s' style='background:#007bff;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;'>Reset Password</a></p>" +
                        "<p>If you didn't request this, please ignore this email.</p>" +
                        "<p>This link will expire in 24 hours.</p>" +
                        "<p>Best regards,<br>Event Management Team</p>" +
                        "</body></html>",
                username, resetLink
        );
    }

}
