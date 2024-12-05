package org.afs.pakinglot.domain.strategies;


import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.repository.TicketRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class AvailableRateStrategy implements ParkingStrategy{

    private TicketRepository ticketRepository;

    public AvailableRateStrategy(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public ParkingLot findParkingLot(List<ParkingLot> parkingLots) {
        return parkingLots.stream()
                .max(Comparator.comparingDouble(this::calculateAvailablePositionRate))
                .orElseThrow(NoAvailablePositionException::new);
    }

    private Double calculateAvailablePositionRate(ParkingLot parkingLot) {
        return (double) (parkingLot.getCapacity() - ticketRepository.countByParkingLotId(parkingLot.getId())) / parkingLot.getCapacity();
    }
}
