package org.afs.pakinglot.domain.strategies;


import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;

import java.util.Comparator;
import java.util.List;

public class MaxAvailableStrategy implements ParkingStrategy{
    @Override
    public ParkingLot findParkingLot(List<ParkingLot> parkingLots) {
        return parkingLots.stream()
                .max(Comparator.comparingInt(ParkingLot::getAvailableCapacity))
                .orElseThrow(NoAvailablePositionException::new);
    }
}
