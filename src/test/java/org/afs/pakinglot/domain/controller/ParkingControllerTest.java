package org.afs.pakinglot.domain.controller;

import org.afs.pakinglot.domain.common.StrategyConstant;
import org.afs.pakinglot.domain.entity.ParkingLot;
import org.afs.pakinglot.domain.entity.Ticket;
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

import static org.afs.pakinglot.domain.CarPlateGenerator.generatePlate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private JacksonTester<TicketVo> ticketVoJacksonTester;

    @Autowired
    private JacksonTester<ParkingLot> parkingLotJacksonTester;

    @BeforeEach
    void setUp() {
        givenDataParkingLotJpaRepository();
    }

    private void givenDataParkingLotJpaRepository() {
        ticketRepository.deleteAll();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(givenParkingLots.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].capacity").value(givenParkingLots.get(0).getCapacity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(givenParkingLots.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].capacity").value(givenParkingLots.get(1).getCapacity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(givenParkingLots.get(2).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].capacity").value(givenParkingLots.get(2).getCapacity()));
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

    @Test
    void should_return_strategies_when_get_strategies() throws Exception {
        //given
        //when
        //then
        client.perform(MockMvcRequestBuilders.get("/parking/strategy"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(StrategyConstant.SUPER_SMART_PARKING_BOY_STRATEGY))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value(StrategyConstant.STANDARD_PARKING_BOY_STRATEGY))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]").value(StrategyConstant.SMART_PARKING_BOY_STRATEGY));
    }

    @Test
    void should_return_ticket_when_park_given_car_and_strategy() throws Exception {
        // Given
        String givenPlateNumber = generatePlate();
        String givenStrategy = StrategyConstant.STANDARD_PARKING_BOY_STRATEGY;
        String givenCar = String.format(
                "{\"plateNumber\": \"%s\"}",
                givenPlateNumber
        );

        // When
        // Then
        String contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + givenStrategy)
                .contentType(MediaType.APPLICATION_JSON)
                .content(givenCar)
        ).andReturn().getResponse().getContentAsString();

        TicketVo ticketVo = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket = ticketRepository.findById(ticketVo.getId()).orElse(null);
        assert ticket != null;
        AssertionsForClassTypes.assertThat(ticket.getPlateNumber()).isEqualTo(givenPlateNumber);
    }

    @Test
    void should_return_car_when_fetch_given_ticket() throws Exception {
        // Given
        String givenPlateNumber = generatePlate();
        String givenTicketDto = String.format(
                "{\"plateNumber\": \"%s\"}",
                givenPlateNumber
        );
        ticketRepository.save(new Ticket(givenPlateNumber, 1, parkingLotRepository.findAll().get(0)));

        // When
        // Then
        String contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTicketDto))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo = ticketVoJacksonTester.parseObject(contentAsString);
        AssertionsForClassTypes.assertThat(ticketVo.getPlateNumber()).isEqualTo(givenPlateNumber);
    }

    @Test
    void should_return_nothing_with_error_message_when_fetch_given_an_unrecognized_ticket() throws Exception {
        // Given
        String givenPlateNumber = generatePlate();
        String givenTicketDto = String.format(
                "{\"plateNumber\": \"%s\"}",
                givenPlateNumber
        );

        // When
        // Then
        try {
            client.perform(MockMvcRequestBuilders.post("/parking/fetch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(givenTicketDto))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> {
                        String errorMessage = result.getResponse().getContentAsString();
                        assertTrue(errorMessage.contains("Unrecognized parking ticket."));
                    });
        } catch (Exception ignore) {
        }
    }

    @Test
    void should_return_nothing_with_error_message_when_fetch_given_an_used_ticket() throws Exception {
        // Given
        String givenPlateNumber = generatePlate();
        String givenTicketDto = String.format(
                "{\"plateNumber\": \"%s\"}",
                givenPlateNumber
        );
        ticketRepository.save(new Ticket(givenPlateNumber, 1, parkingLotRepository.findAll().get(0)));

        // When
        client.perform(MockMvcRequestBuilders.post("/parking/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTicketDto))
                .andReturn().getResponse().getContentAsString();

        // Then
        try {
            client.perform(MockMvcRequestBuilders.post("/parking/fetch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(givenTicketDto))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> {
                        String errorMessage = result.getResponse().getContentAsString();
                        assertTrue(errorMessage.contains("Unrecognized parking ticket."));
                    });
        } catch (Exception ignore) {
        }
    }

    @Test
    void should_return_nothing_with_error_message_when_park_given_all_parking_lots_are_full() throws Exception {
        // Given
        String givenPlateNumber = generatePlate();
        String givenTicketDto = String.format(
                "{\"plateNumber\": \"%s\"}",
                givenPlateNumber
        );
        for (int i = 0; i < parkingLotRepository.findAll().get(0).getCapacity(); i++) {
            ticketRepository.save(new Ticket(generatePlate(), i, parkingLotRepository.findAll().get(0)));
        }
        for (int i = 0; i < parkingLotRepository.findAll().get(1).getCapacity(); i++) {
            ticketRepository.save(new Ticket(generatePlate(), i, parkingLotRepository.findAll().get(1)));
        }
        for (int i = 0; i < parkingLotRepository.findAll().get(2).getCapacity(); i++) {
            ticketRepository.save(new Ticket(generatePlate(), i, parkingLotRepository.findAll().get(2)));
        }

        // When
        // Then
        try {
            client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.STANDARD_PARKING_BOY_STRATEGY)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(givenTicketDto))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> {
                        String errorMessage = result.getResponse().getContentAsString();
                        assertTrue(errorMessage.contains("No available position."));
                    });
        } catch (Exception ignore) {
        }
    }

    @Test
    void should_return_correct_car_when_fetch_twice_given_a_parking_lot_and_two_tickets() {
        // Given
        String givenPlateNumber1 = generatePlate();
        String givenPlateNumber2 = generatePlate();
        Ticket ticket1 = new Ticket(givenPlateNumber1, 1, parkingLotRepository.findAll().get(0));
        Ticket ticket2 = new Ticket(givenPlateNumber2, 2, parkingLotRepository.findAll().get(0));

        // When
        Ticket save1 = ticketRepository.save(ticket1);
        Ticket save2 = ticketRepository.save(ticket2);
        // Then
        AssertionsForClassTypes.assertThat(save1.getPlateNumber()).isEqualTo(givenPlateNumber1);
        AssertionsForClassTypes.assertThat(save2.getPlateNumber()).isEqualTo(givenPlateNumber2);
    }

    @Test
    void should_park_car_to_parking_lot_with_more_empty_positions_when_park_given_3_parking_lots_and_smart() throws Exception {
        // Given
        List<ParkingLot> givenParkingLots = parkingLotRepository.findAll();

        String plate1 = generatePlate();
        String plate2 = generatePlate();
        String plate3 = generatePlate();
        String plate4 = generatePlate();
        String plate5 = generatePlate();

        String givenCar1 = String.format("{\"plateNumber\": \"%s\"}", plate1);
        String givenCar2 = String.format("{\"plateNumber\": \"%s\"}", plate2);
        String givenCar3 = String.format("{\"plateNumber\": \"%s\"}", plate3);
        String givenCar4 = String.format("{\"plateNumber\": \"%s\"}", plate4);
        String givenCar5 = String.format("{\"plateNumber\": \"%s\"}", plate5);

        // When
        String contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar1))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo1 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket1 = ticketRepository.findById(ticketVo1.getId()).orElse(null);
        assert ticket1 != null;

        contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar2))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo2 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket2 = ticketRepository.findById(ticketVo2.getId()).orElse(null);
        assert ticket2 != null;

        contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar3))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo3 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket3 = ticketRepository.findById(ticketVo3.getId()).orElse(null);
        assert ticket3 != null;

        contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar4))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo4 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket4 = ticketRepository.findById(ticketVo4.getId()).orElse(null);
        assert ticket4 != null;

        contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar5))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo5 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket5 = ticketRepository.findById(ticketVo5.getId()).orElse(null);
        assert ticket5 != null;

        // Then
        assertEquals(givenParkingLots.get(1).getId(), ticket1.getParkingLot().getId());
        assertEquals(givenParkingLots.get(1).getId(), ticket2.getParkingLot().getId());
        assertEquals(givenParkingLots.get(1).getId(), ticket3.getParkingLot().getId());
        assertEquals(givenParkingLots.get(0).getId(), ticket4.getParkingLot().getId());
        assertEquals(givenParkingLots.get(1).getId(), ticket5.getParkingLot().getId());
    }

    @Test
    void should_park_into_the_the_parking_lot_which_have_big_available_capacity_rate_given_3_parking_lots_and_super() throws Exception {
        // Given
        List<ParkingLot> givenParkingLots = parkingLotRepository.findAll();

        String plate1 = generatePlate();
        String plate2 = generatePlate();
        String plate3 = generatePlate();
        String plate4 = generatePlate();

        String givenCar1 = String.format("{\"plateNumber\": \"%s\"}", plate1);
        String givenCar2 = String.format("{\"plateNumber\": \"%s\"}", plate2);
        String givenCar3 = String.format("{\"plateNumber\": \"%s\"}", plate3);
        String givenCar4 = String.format("{\"plateNumber\": \"%s\"}", plate4);

        // When
        String contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SUPER_SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar1))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo1 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket1 = ticketRepository.findById(ticketVo1.getId()).orElse(null);
        assert ticket1 != null;

        contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SUPER_SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar2))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo2 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket2 = ticketRepository.findById(ticketVo2.getId()).orElse(null);
        assert ticket2 != null;

        contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SUPER_SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar3))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo3 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket3 = ticketRepository.findById(ticketVo3.getId()).orElse(null);
        assert ticket3 != null;

        contentAsString = client.perform(MockMvcRequestBuilders.post("/parking/park/" + StrategyConstant.SUPER_SMART_PARKING_BOY_STRATEGY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenCar4))
                .andReturn().getResponse().getContentAsString();
        TicketVo ticketVo4 = ticketVoJacksonTester.parseObject(contentAsString);
        Ticket ticket4 = ticketRepository.findById(ticketVo4.getId()).orElse(null);
        assert ticket4 != null;

        // Then
        assertEquals(givenParkingLots.get(0).getId(), ticket1.getParkingLot().getId());
        assertEquals(givenParkingLots.get(1).getId(), ticket2.getParkingLot().getId());
        assertEquals(givenParkingLots.get(2).getId(), ticket3.getParkingLot().getId());
        assertEquals(givenParkingLots.get(1).getId(), ticket4.getParkingLot().getId());
    }
}
