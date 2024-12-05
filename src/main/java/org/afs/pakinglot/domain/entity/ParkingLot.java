package org.afs.pakinglot.domain.entity;

import jakarta.persistence.*;

@Entity
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int capacity;

    public ParkingLot() {
    }

    public ParkingLot(int capacity) {
        this.capacity = capacity;
    }

    public ParkingLot(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }


//    public Ticket park(Car car) {
//        if (isFull()) {
//            throw new NoAvailablePositionException();
//        }
//
//        Ticket ticket = new Ticket(car.getPlateNumber(), tickets.size() + 1, this);
//        tickets.put(ticket, car);
//        return ticket;
//    }


//    public Car fetch(Ticket ticket) {
//        if (!tickets.containsKey(ticket)) {
//            throw new UnrecognizedTicketException();
//        }
//
//        return tickets.remove(ticket);
//    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
