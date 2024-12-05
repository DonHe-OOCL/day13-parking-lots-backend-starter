package org.afs.pakinglot.domain.service;

import org.afs.pakinglot.domain.entity.Car;
import org.afs.pakinglot.domain.ParkingBoy;
import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.Ticket;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.repository.ParkingLotRepository;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ParkingService {

    private Map<String, ParkingStrategy> strategyMap;

    private ParkingBoy parkingBoy;

    private ParkingLotRepository parkingLotRepository;

    public ParkingService(Map<String, ParkingStrategy> strategyMap, ParkingBoy parkingBoy, ParkingLotRepository parkingLotRepository) {
        this.strategyMap = strategyMap;
        this.parkingBoy = parkingBoy;
        this.parkingLotRepository = parkingLotRepository;
    }

    public List<String> getParkingStrategy() {
        return strategyMap.keySet().stream().toList();
    }

    public ParkingLot addParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }


//    public Ticket park(Car car, String strategy) {
//        ParkingStrategy parkingStrategy = strategyMap.getOrDefault(strategy, strategyMap.get("Sequentially"));
//        return parkingStrategy.findParkingLot(parkingLots).park(car);
//    }
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
