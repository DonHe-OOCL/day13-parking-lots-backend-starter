package org.afs.pakinglot.domain.controller;

import org.afs.pakinglot.domain.entity.Car;
import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.dto.TicketDto;
import org.afs.pakinglot.domain.entity.vo.ParkingLotVo;
import org.afs.pakinglot.domain.entity.vo.TicketVo;
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
    public List<ParkingLotVo> getParkingLots() {
        return parkingService.getParkingLots();
    }

    @PostMapping("/park/{strategy}")
    public TicketVo park(@RequestBody Car car, @PathVariable String strategy) {
        return parkingService.park(car, strategy);
    }

    @PostMapping("/fetch")
    public TicketVo fetch(@RequestBody TicketDto ticket) {
        return parkingService.fetch(ticket);
    }

    @PostMapping
    public ParkingLot addParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingService.addParkingLot(parkingLot);
    }
}
