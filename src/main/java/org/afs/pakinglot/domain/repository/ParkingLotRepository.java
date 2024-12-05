package org.afs.pakinglot.domain.repository;

import org.afs.pakinglot.domain.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {

}
