package com.ems.service;

import com.ems.dto.response.DashboardStatsResponse;
import com.ems.model.BookingStatus;
import com.ems.model.EventStatus;
import com.ems.model.Payment;
import com.ems.model.PaymentStatus;
import com.ems.repository.BookingRepository;
import com.ems.repository.EventRepository;
import com.ems.repository.PaymentRepository;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    public DashboardStatsResponse getDashboardStats(){
        DashboardStatsResponse stats=new DashboardStatsResponse();

        //Total counts
        stats.setTotalUsers(userRepository.count());
        stats.setTotalEvents(eventRepository.count());
        stats.setTotalBookings(bookingRepository.count());

        //Event status count
        stats.setActiveEvents((long) eventRepository.findByStatus(EventStatus.ONGOING).size());
        stats.setUpcomingEvents((long) eventRepository.findByStatus(EventStatus.UPCOMING).size());
        stats.setCompletedEvents((long) eventRepository.findByStatus(EventStatus.COMPLETED).size());

        //Booking status counts
        stats.setConfirmedBookings((long) bookingRepository.findByStatus(BookingStatus.CONFIRMED).size());
        stats.setCancelledBookings((long) bookingRepository.findByStatus(BookingStatus.CANCELLED).size());

        //Revenue Calculations
        stats.setTotalRevenue(calculateTotalRevenue());
        stats.setTodayRevenue(calculateTodayRevenue());

        //Today Booking
        stats.setTodayBookings(calculateTodayBookings());

        //Monthly Booking
        stats.setMonthlyBookings(calculateMonthlyBookings());

        return stats;
    }


    private Long calculateTotalRevenue(){
        return paymentRepository.findByStatus(PaymentStatus.SUCCESS).stream()
                .map(payment -> payment.getAmount().longValue())
                .reduce(0L,Long::sum);
    }


    private BigDecimal calculateTodayRevenue(){
        LocalDateTime startOfDay=LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay=LocalDateTime.of(LocalDate.now(),LocalTime.MAX);

        return paymentRepository.findByStatus(PaymentStatus.SUCCESS).stream()
                .filter(payment -> payment.getPaymentDate() != null &&
                        payment.getPaymentDate().isAfter(startOfDay) &&
                        payment.getPaymentDate().isBefore(endOfDay)
                )
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    private Long calculateTodayBookings(){
        LocalDateTime startOfDay=LocalDateTime.of(LocalDate.now(),LocalTime.MIN);
        LocalDateTime endOfDay=LocalDateTime.of(LocalDate.now(),LocalTime.MAX);

        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getCreatedAt().isAfter(startOfDay) &&
                        booking.getCreatedAt().isBefore(endOfDay))
                .count();
    }

    private Long calculateMonthlyBookings(){
        YearMonth currentMonth=YearMonth.now();
        LocalDateTime startOfMonth=currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth=currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getCreatedAt().isAfter(startOfMonth) &&
                        booking.getCreatedAt().isBefore(endOfMonth)).count();
    }

}
