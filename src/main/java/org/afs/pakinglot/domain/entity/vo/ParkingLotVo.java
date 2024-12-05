package org.afs.pakinglot.domain.entity.vo;

import java.util.List;

public class ParkingLotVo {
    private Integer id;
    private String name;
    private int capacity;
    private int currentSlots;
    private List<TicketVo> tickets;

    public ParkingLotVo() {
    }

    public ParkingLotVo(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentSlots() {
        return currentSlots;
    }

    public void setCurrentSlots(int currentSlots) {
        this.currentSlots = currentSlots;
    }

    public List<TicketVo> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketVo> tickets) {
        this.tickets = tickets;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
