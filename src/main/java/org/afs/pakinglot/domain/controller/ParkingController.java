package org.afs.pakinglot.domain.controller;

import org.afs.pakinglot.domain.entity.Car;
import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.Ticket;
import org.afs.pakinglot.domain.service.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking")
public class ParkingController {

    private ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/strategy")
    public List<String> getParkingStrategy() {
        return parkingService.getParkingStrategy();
    }

    @GetMapping
    public List<ParkingLot> getParkingLots() {
        return parkingService.getParkingLots();
    }

    @PostMapping("/park/{strategy}")
    public Ticket park(@RequestBody Car car, @PathVariable String strategy) {
        return parkingService.park(car, strategy);
    }

    @PostMapping("/fetch")
    public Car fetch(@RequestBody Ticket ticket) {
        return parkingService.fetch(ticket);
    }
}
