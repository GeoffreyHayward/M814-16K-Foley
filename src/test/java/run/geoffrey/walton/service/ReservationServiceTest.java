package run.geoffrey.walton.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import run.geoffrey.walton.dto.ReservationDTO;
import run.geoffrey.walton.model.Reservation;
import run.geoffrey.walton.model.Workshop;
import run.geoffrey.walton.repository.ReservationRepository;
import run.geoffrey.walton.repository.WorkshopRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private WorkshopRepository workshopRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Workshop availableWorkshop;
    private Workshop fullyBookedWorkshop;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test workshops
        availableWorkshop = new Workshop();
        availableWorkshop.setId(1);
        availableWorkshop.setName("Available Workshop");
        availableWorkshop.setDescription("This is an available workshop");
        availableWorkshop.setDateTime(LocalDateTime.now().plusDays(7));
        availableWorkshop.setCapacity(10);
        availableWorkshop.setCurrentBookings(5);

        fullyBookedWorkshop = new Workshop();
        fullyBookedWorkshop.setId(2);
        fullyBookedWorkshop.setName("Fully Booked Workshop");
        fullyBookedWorkshop.setDescription("This is a fully booked workshop");
        fullyBookedWorkshop.setDateTime(LocalDateTime.now().plusDays(7));
        fullyBookedWorkshop.setCapacity(10);
        fullyBookedWorkshop.setCurrentBookings(10);

        // Create test reservation
        reservation = new Reservation();
        reservation.setId(1);
        reservation.setWorkshop(availableWorkshop);
        reservation.setAttendeeName("John Doe");
        reservation.setAttendeeEmail("john@example.com");
        reservation.setReservationDate(LocalDateTime.now());
    }

    @Test
    void testGetAllReservations() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        // Act
        List<ReservationDTO> result = reservationService.getAllReservations();

        // Assert
        assertEquals(1, result.size());
        assertEquals(reservation.getId(), result.get(0).getId());
        assertEquals(reservation.getWorkshop().getId(), result.get(0).getWorkshopId());
        assertEquals(reservation.getAttendeeName(), result.get(0).getAttendeeName());
        assertEquals(reservation.getAttendeeEmail(), result.get(0).getAttendeeEmail());
    }

    @Test
    void testGetReservationsByWorkshopId() {
        // Arrange
        when(reservationRepository.findByWorkshopId(availableWorkshop.getId())).thenReturn(Arrays.asList(reservation));

        // Act
        List<ReservationDTO> result = reservationService.getReservationsByWorkshopId(availableWorkshop.getId());

        // Assert
        assertEquals(1, result.size());
        assertEquals(reservation.getId(), result.get(0).getId());
        assertEquals(availableWorkshop.getId(), result.get(0).getWorkshopId());
    }

    @Test
    void testGetReservationsByAttendeeEmail() {
        // Arrange
        when(reservationRepository.findByAttendeeEmail(reservation.getAttendeeEmail())).thenReturn(Arrays.asList(reservation));

        // Act
        List<ReservationDTO> result = reservationService.getReservationsByAttendeeEmail(reservation.getAttendeeEmail());

        // Assert
        assertEquals(1, result.size());
        assertEquals(reservation.getId(), result.get(0).getId());
        assertEquals(reservation.getAttendeeEmail(), result.get(0).getAttendeeEmail());
    }

    @Test
    void testGetReservationById() {
        // Arrange
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        // Act
        Optional<ReservationDTO> result = reservationService.getReservationById(reservation.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(reservation.getId(), result.get().getId());
    }

    @Test
    void testCreateReservation_Available() {
        // Arrange
        when(workshopRepository.findById(availableWorkshop.getId())).thenReturn(Optional.of(availableWorkshop));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Optional<ReservationDTO> result = reservationService.createReservation(
                availableWorkshop.getId(),
                reservation.getAttendeeName(),
                reservation.getAttendeeEmail()
        );

        // Assert
        assertTrue(result.isPresent());
        assertEquals(availableWorkshop.getId(), result.get().getWorkshopId());
        assertEquals(reservation.getAttendeeName(), result.get().getAttendeeName());
        assertEquals(reservation.getAttendeeEmail(), result.get().getAttendeeEmail());

        // Verify that the workshop's current bookings were incremented
        assertEquals(6, availableWorkshop.getCurrentBookings());
        verify(workshopRepository, times(1)).save(availableWorkshop);
    }

    @Test
    void testCreateReservation_FullyBooked() {
        // Arrange
        when(workshopRepository.findById(fullyBookedWorkshop.getId())).thenReturn(Optional.of(fullyBookedWorkshop));

        // Act
        Optional<ReservationDTO> result = reservationService.createReservation(
                fullyBookedWorkshop.getId(),
                reservation.getAttendeeName(),
                reservation.getAttendeeEmail()
        );

        // Assert
        assertFalse(result.isPresent());
        
        // Verify that the workshop's current bookings were not changed
        assertEquals(10, fullyBookedWorkshop.getCurrentBookings());
        verify(workshopRepository, never()).save(fullyBookedWorkshop);
    }

    @Test
    void testCancelReservation() {
        // Arrange
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        // Act
        boolean result = reservationService.cancelReservation(reservation.getId());

        // Assert
        assertTrue(result);
        
        // Verify that the workshop's current bookings were decremented
        assertEquals(4, availableWorkshop.getCurrentBookings());
        verify(workshopRepository, times(1)).save(availableWorkshop);
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void testCancelReservation_NotFound() {
        // Arrange
        when(reservationRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        boolean result = reservationService.cancelReservation(999);

        // Assert
        assertFalse(result);
        
        // Verify that no workshop was updated and no reservation was deleted
        verify(workshopRepository, never()).save(any(Workshop.class));
        verify(reservationRepository, never()).delete(any(Reservation.class));
    }

    @Test
    void testCountReservationsByWorkshopId() {
        // Arrange
        when(reservationRepository.countByWorkshopId(availableWorkshop.getId())).thenReturn(5L);

        // Act
        long result = reservationService.countReservationsByWorkshopId(availableWorkshop.getId());

        // Assert
        assertEquals(5L, result);
    }
}