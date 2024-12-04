package org.afs.pakinglot.domain.strategies;


import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;

import java.util.List;

public class SequentiallyStrategy implements ParkingStrategy {

    @Override
    public ParkingLot findParkingLot(List<ParkingLot> parkingLots) {
        return  parkingLots.stream()
                .filter(parkingLot -> !parkingLot.isFull())
                .findFirst()
                .orElseThrow(NoAvailablePositionException::new);
    }
}
