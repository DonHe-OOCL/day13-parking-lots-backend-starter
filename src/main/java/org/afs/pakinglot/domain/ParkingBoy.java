package org.afs.pakinglot.domain;


import java.util.ArrayList;
import java.util.List;

import org.afs.pakinglot.domain.entity.Car;
import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.Ticket;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.springframework.stereotype.Component;

@Component
public class ParkingBoy {
    protected List<ParkingLot> parkingLots = new ArrayList<>();
    private ParkingStrategy parkingStrategy = new SequentiallyStrategy();

    public ParkingBoy() {
    }

    public ParkingBoy(List<ParkingLot> parkingLots) {
        this.parkingLots = parkingLots;
    }

    public ParkingBoy(ParkingLot parkingLot) {
        parkingLots.add(parkingLot);
    }

    public ParkingBoy(List<ParkingLot> parkingLots, ParkingStrategy parkingStrategy) {
        this.parkingLots = parkingLots;
        this.parkingStrategy = parkingStrategy;
    }

    public Ticket park(Car car) {
        return parkingStrategy.findParkingLot(parkingLots).park(car);
    }

    public Car fetch(Ticket ticket) {
        ParkingLot parkingLotOfTheTicket = parkingLots.stream()
            .filter(parkingLot -> parkingLot.contains(ticket))
            .findFirst()
            .orElseThrow(UnrecognizedTicketException::new);
        return parkingLotOfTheTicket.fetch(ticket);
    }

    public List<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public void setParkingStrategy(ParkingStrategy parkingStrategy) {
        this.parkingStrategy = parkingStrategy;
    }
}
