package com.parking.smartparking.repository;

import com.parking.smartparking.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByStatus(ParkingSlot.SlotStatus status);
    List<ParkingSlot> findBySlotType(ParkingSlot.SlotType slotType);
}
