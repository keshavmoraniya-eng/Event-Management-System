package com.ems.service;

import com.ems.dto.request.PaymentRequest;
import com.ems.dto.response.PaymentResponse;
import com.ems.exception.ResourceNotFoundException;
import com.ems.model.Booking;
import com.ems.model.BookingStatus;
import com.ems.model.Payment;
import com.ems.model.PaymentStatus;
import com.ems.repository.BookingRepository;
import com.ems.repository.PaymentRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) throws BadRequestException {
        //validate booking
        Booking booking=bookingRepository.findById(request.getBookingId())
                .orElseThrow(()->new ResourceNotFoundException("Booking not found with id:"+request.getBookingId()));

        //Check if payment already exist or not
        if (paymentRepository.findByBooking(booking).isPresent()){
            throw new BadRequestException("Payment already exists for this booking");
        }

        //Create Payment
        Payment payment=new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentGateway(request.getPaymentGateway() != null ? request.getPaymentGateway(): "MANUAL");
        payment.setNotes(request.getNotes());
        payment.setTransactionId(generateTransactionId());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment=paymentRepository.save(payment);
        return convertToResponse(savedPayment);
    }

    public PaymentResponse verifyPayment(String transactionId){
        Payment payment=paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(()->new ResourceNotFoundException("Payment verification field with transaction id:"+transactionId));

        return convertToResponse(payment);
    }

    @Transactional
    public PaymentResponse refundPayment(Long paymentId) throws BadRequestException {
        Payment payment=paymentRepository.findById(paymentId)
                .orElseThrow(()->new ResourceNotFoundException("Payment refund field with payment id:"+paymentId));

        if (payment.getStatus()==PaymentStatus.REFUNDED){
            throw new BadRequestException("Payment already refunded");
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS){
            throw new BadRequestException("Can only refund successful payments");
        }

        //Payment status update
        payment.setStatus(PaymentStatus.REFUNDED);
        Payment refundedPayment=paymentRepository.save(payment);

        //Update booking status
        Booking booking=payment.getBooking();
        booking.setStatus(BookingStatus.REFUNDED);
        bookingRepository.save(booking);

        return convertToResponse(refundedPayment);
    }

    public PaymentResponse getPaymentById(Long id){
        Payment payment=paymentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found by id:"+id));
        return convertToResponse(payment);
    }

    public PaymentResponse getPaymentByBooking(Long bookingId){
        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFoundException("Booking Payment not found with id:"+bookingId));

        Payment payment=paymentRepository.findByBooking(booking)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found for booking:"+bookingId));

        return convertToResponse(payment);
    }

    public List<PaymentResponse> getAllPayments(){
        return paymentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentByStatus(PaymentStatus status){
        return paymentRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public String generateTransactionId(){
        return "TXN-"+ UUID.randomUUID().toString().replace("-","").substring(0,16);
    }


    private PaymentResponse convertToResponse(Payment payment){
        PaymentResponse response=new PaymentResponse();
        response.setId(payment.getId());
        response.setBookingId(payment.getBooking().getId());
        response.setBookingReference(payment.getBooking().getBookingReference());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setTransactionId(payment.getTransactionId());
        response.setPaymentGateway(payment.getPaymentGateway());
        response.setPaymentDate(payment.getPaymentDate());
        response.setNotes(payment.getNotes());
        response.setCreatedAt(payment.getCreatedAt());
        return response;

    }

}
