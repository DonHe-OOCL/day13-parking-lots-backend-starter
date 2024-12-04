package org.afs.pakinglot.domain.service;

import org.afs.pakinglot.domain.entity.Car;
import org.afs.pakinglot.domain.ParkingBoy;
import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.Ticket;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ParkingService {

    private Map<String, ParkingStrategy> strategyMap;

    private ParkingBoy parkingBoy;

    public ParkingService(Map<String, ParkingStrategy> strategyMap, ParkingBoy parkingBoy) {
        this.strategyMap = strategyMap;
        this.parkingBoy = parkingBoy;
    }

    public List<String> getParkingStrategy() {
        return strategyMap.keySet().stream().toList();
    }

    public Ticket park(Car car, String strategy) {
        ParkingStrategy parkingStrategy = strategyMap.putIfAbsent(strategy, strategyMap.get("Sequentially"));
        parkingBoy.setParkingStrategy(parkingStrategy);
        return parkingBoy.park(car);
    }

    public List<ParkingLot> getParkingLots() {
        return parkingBoy.getParkingLots();
    }
}
