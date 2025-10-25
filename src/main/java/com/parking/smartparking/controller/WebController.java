package com.parking.smartparking.controller;

import com.parking.smartparking.model.Booking;
import com.parking.smartparking.model.ParkingSlot;
import com.parking.smartparking.model.User;
import com.parking.smartparking.service.BookingService;
import com.parking.smartparking.service.ParkingSlotService;
import com.parking.smartparking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    private final UserService userService;
    private final ParkingSlotService slotService;
    private final BookingService bookingService;

    @Autowired
    public WebController(UserService userService, ParkingSlotService slotService, BookingService bookingService) {
        this.userService = userService;
        this.slotService = slotService;
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ParkingSlot> availableSlots = slotService.getAvailableSlots();
        model.addAttribute("availableSlots", availableSlots);
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<ParkingSlot> allSlots = slotService.getAllSlots();
        List<Booking> recentBookings = bookingService.getAllBookings();
        
        // Calculate statistics
        long availableCount = allSlots.stream()
            .filter(slot -> slot.getStatus() == ParkingSlot.SlotStatus.AVAILABLE)
            .count();
        long occupiedCount = allSlots.stream()
            .filter(slot -> slot.getStatus() == ParkingSlot.SlotStatus.OCCUPIED)
            .count();
        
        model.addAttribute("slots", allSlots);
        model.addAttribute("bookings", recentBookings);
        model.addAttribute("availableCount", availableCount);
        model.addAttribute("occupiedCount", occupiedCount);
        return "dashboard";
    }

    @GetMapping("/slots")
    public String slots(Model model) {
        List<ParkingSlot> allSlots = slotService.getAllSlots();
        model.addAttribute("slots", allSlots);
        return "slots";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        List<Booking> allBookings = bookingService.getAllBookings();
        model.addAttribute("bookings", allBookings);
        return "bookings";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        model.addAttribute("user", new User());
        return "users";
    }

    @GetMapping("/book-slot")
    public String bookSlot(Model model, javax.servlet.http.HttpSession session) {
        List<ParkingSlot> availableSlots = slotService.getAvailableSlots();
        List<User> users = userService.getAllUsers();
        model.addAttribute("availableSlots", availableSlots);
        model.addAttribute("users", users);
        
        // Pre-select logged-in user if available
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser != null) {
            model.addAttribute("selectedUserId", loggedInUser.getId());
        }
        
        return "book-slot";
    }

    @PostMapping("/book-slot")
    public String createBooking(@RequestParam Long parkingSlotId,
                               @RequestParam Long userId,
                               @RequestParam String startTime,
                               @RequestParam String endTime,
                               @RequestParam(required = false) Long vehicleId) {
        try {
            // Create booking object
            Booking booking = new Booking();
            
            // Set user
            User user = userService.getUserById(userId);
            booking.setUser(user);
            
            // Set parking slot
            ParkingSlot slot = slotService.getSlotById(parkingSlotId);
            booking.setParkingSlot(slot);
            
            // Set times
            booking.setStartTime(java.time.LocalDateTime.parse(startTime));
            booking.setEndTime(java.time.LocalDateTime.parse(endTime));
            
            // Set vehicle if provided
            if (vehicleId != null) {
                // For now, we'll skip vehicle validation since it's optional
                // In a real app, you'd validate the vehicle belongs to the user
            }
            
            // Set initial status
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            
            // Create the booking (this will generate QR code and calculate amount)
            Booking createdBooking = bookingService.createBooking(booking);
            
            return "redirect:/booking-confirmation/" + createdBooking.getId();
        } catch (Exception e) {
            return "redirect:/book-slot?error=true";
        }
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/users";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, 
                           @RequestParam String password,
                           javax.servlet.http.HttpSession session) {
        try {
            User user = userService.getUserByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                return "redirect:/dashboard?login=success";
            } else {
                return "redirect:/login?error=invalid";
            }
        } catch (Exception e) {
            return "redirect:/login?error=invalid";
        }
    }

    @GetMapping("/logout")
    public String logout(javax.servlet.http.HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/?logout=success";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login?success=true";
    }

    @GetMapping("/booking-confirmation/{id}")
    public String bookingConfirmation(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "booking-confirmation";
    }

    @GetMapping("/checkin")
    public String checkin() {
        return "checkin";
    }

    @GetMapping("/process-qr/{qrCode}")
    public String processQRCode(@PathVariable String qrCode, Model model) {
        try {
            Booking booking = bookingService.getBookingByQRCode(qrCode);
            model.addAttribute("booking", booking);
            return "booking-details";
        } catch (Exception e) {
            return "redirect:/checkin?error=invalid";
        }
    }
}
