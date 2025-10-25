package com.parking.smartparking.service;

import com.parking.smartparking.model.ParkingSlot;
import com.parking.smartparking.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParkingSlotService {

    private final ParkingSlotRepository slotRepository;

    @Autowired
    public ParkingSlotService(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public ParkingSlot createSlot(ParkingSlot slot) {
        return slotRepository.save(slot);
    }

    public List<ParkingSlot> getAllSlots() {
        return slotRepository.findAll();
    }

    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findByStatus(ParkingSlot.SlotStatus.AVAILABLE);
    }

    public ParkingSlot getSlotById(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
    }

    public ParkingSlot updateSlotStatus(Long id, ParkingSlot.SlotStatus status) {
        ParkingSlot slot = getSlotById(id);
        slot.setStatus(status);
        return slotRepository.save(slot);
    }

    public void deleteSlot(Long id) {
        slotRepository.deleteById(id);
    }
}
