package org.afs.pakinglot.domain.controller;

import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.Ticket;
import org.afs.pakinglot.domain.entity.vo.ParkingLotVo;
import org.afs.pakinglot.domain.entity.vo.TicketVo;
import org.afs.pakinglot.domain.repository.ParkingLotRepository;
import org.afs.pakinglot.domain.repository.TicketRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ParkingControllerTest {
    @Autowired
    private MockMvc client;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private JacksonTester<ParkingLotVo> parkingLotVoJacksonTester;

    @Autowired
    private JacksonTester<TicketVo> ticketVoJacksonTester;

    @Autowired
    private JacksonTester<List<ParkingLotVo>> parkingLotVoListJacksonTester;

    @Autowired
    private JacksonTester<List<TicketVo>> ticketVoListJacksonTester;

    @Autowired
    private JacksonTester<ParkingLot> parkingLotJacksonTester;

    @Autowired
    private JacksonTester<Ticket> ticketJacksonTester;

    @BeforeEach
    void setUp() {
        givenDataParkingLotJpaRepository();
    }

    private void givenDataParkingLotJpaRepository() {
        parkingLotRepository.deleteAll();
        parkingLotRepository.save(new ParkingLot(null, "The Plaza Park", 9));
        parkingLotRepository.save(new ParkingLot(null, "City Mall Garage", 12));
        parkingLotRepository.save(new ParkingLot(null, "Office Tower Parking", 9));
    }

    @Test
    void should_return_parking_lots_when_get_all_given_parking_lots_exist() throws Exception {
        //given
        final List<ParkingLot> givenParkingLots = parkingLotRepository.findAll();

        //when
        //then
        client.perform(MockMvcRequestBuilders.get("/parking"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("The Plaza Park"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].capacity").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("City Mall Garage"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].capacity").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Office Tower Parking"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].capacity").value(9));
    }

    @Test
    void should_add_parking_lot_when_add_given_parking_lot() throws Exception {
        // Given
        String givenName = "The Park";
        Integer givenCapacity = 90;
        String givenParkingLot = String.format(
                "{\"name\": \"%s\", \"capacity\": \"%d\"}",
                givenName,
                givenCapacity
        );

        // When
        // Then
        String contentAsString = client.perform(MockMvcRequestBuilders.post("/parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(givenParkingLot)
        ).andReturn().getResponse().getContentAsString();
        ParkingLot parkingLot = parkingLotJacksonTester.parseObject(contentAsString);

        ParkingLot findParkingLot = parkingLotRepository.findById(parkingLot.getId()).orElse(null);
        assert findParkingLot != null;
        AssertionsForClassTypes.assertThat(findParkingLot.getName()).isEqualTo(givenName);
        AssertionsForClassTypes.assertThat(findParkingLot.getCapacity()).isEqualTo(givenCapacity);
    }
}
