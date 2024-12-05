package org.afs.pakinglot.domain.entity;

import java.util.Set;

import jakarta.persistence.*;

@Entity
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int capacity;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets;

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

    public int calculateAvailableCapacity() {
        return capacity - tickets.size();
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

    public boolean checkFull() {
        return capacity == tickets.size();
    }

//    public Car fetch(Ticket ticket) {
//        if (!tickets.containsKey(ticket)) {
//            throw new UnrecognizedTicketException();
//        }
//
//        return tickets.remove(ticket);
//    }

    public boolean containsTicket(Ticket ticket) {
        return tickets.contains(ticket);
    }

    public double calculateAvailablePositionRate() {
        return calculateAvailableCapacity() / (double) capacity;
    }

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

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }
}
