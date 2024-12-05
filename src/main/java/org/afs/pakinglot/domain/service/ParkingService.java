package org.afs.pakinglot.domain.service;

import org.afs.pakinglot.domain.entity.Car;
import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.Ticket;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.repository.ParkingLotRepository;
import org.afs.pakinglot.domain.repository.TicketRepository;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ParkingService {

    private Map<String, ParkingStrategy> strategyMap;

    private ParkingLotRepository parkingLotRepository;

    private TicketRepository ticketRepository;

    public ParkingService(Map<String, ParkingStrategy> strategyMap,
                          ParkingLotRepository parkingLotRepository,
                          TicketRepository ticketRepository) {
        this.strategyMap = strategyMap;
        this.parkingLotRepository = parkingLotRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<String> getParkingStrategy() {
        return strategyMap.keySet().stream().toList();
    }

    public ParkingLot addParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    @Transactional
    public Ticket park(Car car, String strategy) {
        ParkingStrategy parkingStrategy = strategyMap.getOrDefault(strategy, strategyMap.get("Sequentially"));
        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        ParkingLot parkingLot = parkingStrategy.findParkingLot(parkingLots);
        Integer slots = ticketRepository.countByParkingLotId(parkingLot.getId());
        if (slots == parkingLot.getCapacity()) {
            throw new NoAvailablePositionException();
        }
        Ticket ticket = new Ticket(car.getPlateNumber(), slots + 1, parkingLot);
        ticketRepository.save(ticket);
        parkingLotRepository.save(parkingLot);
        return ticket;
    }
//
//    public Car fetch(Ticket ticket) {
//        ParkingLot parkingLotOfTheTicket = parkingLots.stream()
//                .filter(parkingLot -> parkingLot.contains(ticket))
//                .findFirst()
//                .orElseThrow(UnrecognizedTicketException::new);
//        return parkingLotOfTheTicket.fetch(ticket);
//    }

    public List<ParkingLot> getParkingLots() {
        return parkingLotRepository.findAll();
    }
}
