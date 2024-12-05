package org.afs.pakinglot.domain.strategies;


import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.repository.TicketRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SequentiallyStrategy implements ParkingStrategy {

    private TicketRepository ticketRepository;

    public SequentiallyStrategy(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public ParkingLot findParkingLot(List<ParkingLot> parkingLots) {
        return  parkingLots.stream()
                .filter(parkingLot -> !checkFull(parkingLot))
                .findFirst()
                .orElseThrow(NoAvailablePositionException::new);
    }

    private boolean checkFull(ParkingLot parkingLot) {
        return ticketRepository.countByParkingLotId(parkingLot.getId()) == parkingLot.getCapacity();
    }
}
