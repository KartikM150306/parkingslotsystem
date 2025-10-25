package com.parking.smartparking.service;

import com.parking.smartparking.model.*;
import com.parking.smartparking.repository.ParkingSlotRepository;
import com.parking.smartparking.repository.UserRepository;
import com.parking.smartparking.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private ParkingSlotRepository slotRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample data if database is empty
        try {
            if (slotRepository.count() == 0) {
                initializeSampleData();
            }
        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }

    private void initializeSampleData() {
        // Create sample parking slots
        ParkingSlot slot1 = new ParkingSlot();
        slot1.setSlotNumber("A-001");
        slot1.setSlotType(ParkingSlot.SlotType.FOUR_WHEELER);
        slot1.setFloor("Ground Floor");
        slot1.setLocation("Main Building - Ground Floor");
        slot1.setPricePerHour(5.0);
        slot1.setStatus(ParkingSlot.SlotStatus.AVAILABLE);
        slotRepository.save(slot1);

        ParkingSlot slot2 = new ParkingSlot();
        slot2.setSlotNumber("A-002");
        slot2.setSlotType(ParkingSlot.SlotType.FOUR_WHEELER);
        slot2.setFloor("Ground Floor");
        slot2.setLocation("Main Building - Ground Floor");
        slot2.setPricePerHour(5.0);
        slot2.setStatus(ParkingSlot.SlotStatus.AVAILABLE);
        slotRepository.save(slot2);

        ParkingSlot slot3 = new ParkingSlot();
        slot3.setSlotNumber("B-001");
        slot3.setSlotType(ParkingSlot.SlotType.TWO_WHEELER);
        slot3.setFloor("Ground Floor");
        slot3.setLocation("Main Building - Ground Floor");
        slot3.setPricePerHour(2.0);
        slot3.setStatus(ParkingSlot.SlotStatus.AVAILABLE);
        slotRepository.save(slot3);

        ParkingSlot slot4 = new ParkingSlot();
        slot4.setSlotNumber("B-002");
        slot4.setSlotType(ParkingSlot.SlotType.TWO_WHEELER);
        slot4.setFloor("Ground Floor");
        slot4.setLocation("Main Building - Ground Floor");
        slot4.setPricePerHour(2.0);
        slot4.setStatus(ParkingSlot.SlotStatus.OCCUPIED);
        slotRepository.save(slot4);

        ParkingSlot slot5 = new ParkingSlot();
        slot5.setSlotNumber("C-001");
        slot5.setSlotType(ParkingSlot.SlotType.HEAVY_VEHICLE);
        slot5.setFloor("Ground Floor");
        slot5.setLocation("Main Building - Ground Floor");
        slot5.setPricePerHour(10.0);
        slot5.setStatus(ParkingSlot.SlotStatus.AVAILABLE);
        slotRepository.save(slot5);

        // Create sample users
        User admin = new User();
        admin.setFullName("Admin User");
        admin.setEmail("admin@smartparking.com");
        admin.setPassword("admin123");
        admin.setPhone("+1234567890");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        User user1 = new User();
        user1.setFullName("John Doe");
        user1.setEmail("john@example.com");
        user1.setPassword("password123");
        user1.setPhone("+1234567891");
        user1.setRole(User.Role.USER);
        userRepository.save(user1);

        User user2 = new User();
        user2.setFullName("Jane Smith");
        user2.setEmail("jane@example.com");
        user2.setPassword("password123");
        user2.setPhone("+1234567892");
        user2.setRole(User.Role.USER);
        userRepository.save(user2);

        // Create sample vehicles (commented out for now to avoid dependency issues)
        /*
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setVehicleNumber("ABC-1234");
        vehicle1.setVehicleType(Vehicle.VehicleType.FOUR_WHEELER);
        vehicle1.setBrand("Toyota");
        vehicle1.setModel("Camry");
        vehicle1.setColor("Blue");
        vehicle1.setUser(user1);
        vehicleRepository.save(vehicle1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVehicleNumber("XYZ-5678");
        vehicle2.setVehicleType(Vehicle.VehicleType.TWO_WHEELER);
        vehicle2.setBrand("Honda");
        vehicle2.setModel("CBR");
        vehicle2.setColor("Red");
        vehicle2.setUser(user2);
        vehicleRepository.save(vehicle2);
        */

        System.out.println("Sample data initialized successfully!");
    }
}
