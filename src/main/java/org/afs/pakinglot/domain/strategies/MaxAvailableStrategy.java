package org.afs.pakinglot.domain.strategies;

import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.repository.TicketRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class MaxAvailableStrategy implements ParkingStrategy{

    private TicketRepository ticketRepository;

    public MaxAvailableStrategy(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public ParkingLot findParkingLot(List<ParkingLot> parkingLots) {
        return parkingLots.stream()
                .max(Comparator.comparingInt(this::calculateAvailablePosition))
                .orElseThrow(NoAvailablePositionException::new);
    }

    private Integer calculateAvailablePosition(ParkingLot parkingLot) {
        return parkingLot.getCapacity() - ticketRepository.countByParkingLotId(parkingLot.getId());
    }
}
