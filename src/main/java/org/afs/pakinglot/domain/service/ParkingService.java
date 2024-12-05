package org.afs.pakinglot.domain.service;

import org.afs.pakinglot.domain.entity.Car;
import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.Ticket;
import org.afs.pakinglot.domain.entity.dto.TicketDto;
import org.afs.pakinglot.domain.entity.vo.ParkingLotVo;
import org.afs.pakinglot.domain.entity.vo.TicketVo;
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
    public TicketVo park(Car car, String strategy) {
        if (ticketRepository.findByPlateNumber(car.getPlateNumber()) != null) {
            throw new UnrecognizedTicketException();
        }
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
        return ticketToVo(ticket);
    }

    public TicketVo fetch(TicketDto ticket) {
        Ticket ticketEntity = ticketRepository.findByPlateNumber(ticket.getPlateNumber());
        if (ticketEntity == null) {
            throw new UnrecognizedTicketException();
        }
        ticketRepository.deleteById(ticketEntity.getId());
        TicketVo ticketVo = new TicketVo();
        ticketVo.setPlateNumber(ticketEntity.getPlateNumber());
        ticketVo.setPosition(ticketEntity.getPosition());
        ticketVo.setId(ticketEntity.getId());
        ticketVo.setParkingLotId(ticketEntity.getParkingLot().getId());
        return ticketVo;
    }

    public List<ParkingLotVo> getParkingLots() {
        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        return parkingLots.stream().map(parkingLot -> {
            Integer slots = ticketRepository.countByParkingLotId(parkingLot.getId());
            ParkingLotVo parkingLotVo = new ParkingLotVo();
            parkingLotVo.setId(parkingLot.getId());
            parkingLotVo.setName(parkingLot.getName());
            parkingLotVo.setCapacity(parkingLot.getCapacity());
            parkingLotVo.setCurrentSlots(slots);
            parkingLotVo.setTickets(ticketToVo(parkingLot));
            return parkingLotVo;
        }).toList();
    }

    private List<TicketVo> ticketToVo(ParkingLot parkingLot) {
        return ticketRepository.findByParkingLotId(parkingLot.getId()).stream().map(ticket -> {
            TicketVo ticketVo = new TicketVo();
            ticketVo.setPlateNumber(ticket.getPlateNumber());
            ticketVo.setPosition(ticket.getPosition());
            ticketVo.setId(ticket.getId());
            ticketVo.setParkingLotId(ticket.getParkingLot().getId());
            return ticketVo;
        }).toList();
    }

    private TicketVo ticketToVo(Ticket ticket) {
        TicketVo ticketVo = new TicketVo();
        ticketVo.setPlateNumber(ticket.getPlateNumber());
        ticketVo.setPosition(ticket.getPosition());
        ticketVo.setId(ticket.getId());
        ticketVo.setParkingLotId(ticket.getParkingLot().getId());
        return ticketVo;
    }
}
