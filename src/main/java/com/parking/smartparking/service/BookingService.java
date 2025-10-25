package com.parking.smartparking.service;

import com.parking.smartparking.model.Booking;
import com.parking.smartparking.model.ParkingSlot;
import com.parking.smartparking.repository.BookingRepository;
import com.parking.smartparking.util.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParkingSlotService slotService;
    private final QRCodeService qrCodeService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          ParkingSlotService slotService,
                          QRCodeService qrCodeService) {
        this.bookingRepository = bookingRepository;
        this.slotService = slotService;
        this.qrCodeService = qrCodeService;
    }

    public Booking createBooking(Booking booking) {
        // Calculate total amount
        long hours = Duration.between(booking.getStartTime(), booking.getEndTime()).toHours();
        if (hours < 1) hours = 1;
        double amount = hours * booking.getParkingSlot().getPricePerHour();
        booking.setTotalAmount(amount);

        // Generate QR Code
        String qrData = "BOOKING-" + UUID.randomUUID().toString();
        booking.setQrCode(qrData);

        // Update slot status
        slotService.updateSlotStatus(booking.getParkingSlot().getId(),
                ParkingSlot.SlotStatus.RESERVED);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public Booking getBookingByQRCode(String qrCode) {
        return bookingRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new RuntimeException("Invalid QR Code"));
    }

    public Booking updateBookingStatus(Long id, Booking.BookingStatus status) {
        Booking booking = getBookingById(id);
        booking.setStatus(status);

        // Update slot status based on booking status
        if (status == Booking.BookingStatus.COMPLETED ||
                status == Booking.BookingStatus.CANCELLED) {
            slotService.updateSlotStatus(booking.getParkingSlot().getId(),
                    ParkingSlot.SlotStatus.AVAILABLE);
        }

        return bookingRepository.save(booking);
    }
}
