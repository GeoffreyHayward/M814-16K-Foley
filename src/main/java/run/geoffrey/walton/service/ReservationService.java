package run.geoffrey.walton.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import run.geoffrey.walton.dto.ReservationDTO;
import run.geoffrey.walton.model.Reservation;
import run.geoffrey.walton.model.Workshop;
import run.geoffrey.walton.repository.ReservationRepository;
import run.geoffrey.walton.repository.WorkshopRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Reservation entity.
 * Contains business logic for reservation operations.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WorkshopRepository workshopRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, WorkshopRepository workshopRepository) {
        this.reservationRepository = reservationRepository;
        this.workshopRepository = workshopRepository;
    }

    /**
     * Gets all reservations.
     *
     * @return A list of all reservations as DTOs
     */
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all reservations for a specific workshop.
     *
     * @param workshopId The ID of the workshop to get reservations for
     * @return A list of reservations for the workshop as DTOs
     */
    public List<ReservationDTO> getReservationsByWorkshopId(Integer workshopId) {
        return reservationRepository.findByWorkshopId(workshopId).stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all reservations for a specific attendee email.
     *
     * @param email The email of the attendee
     * @return A list of reservations for the attendee as DTOs
     */
    public List<ReservationDTO> getReservationsByAttendeeEmail(String email) {
        return reservationRepository.findByAttendeeEmail(email).stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets a reservation by its ID.
     *
     * @param id The ID of the reservation to get
     * @return An Optional containing the reservation as a DTO if found, or an empty Optional if not found
     */
    public Optional<ReservationDTO> getReservationById(Integer id) {
        return reservationRepository.findById(id)
                .map(ReservationDTO::new);
    }

    /**
     * Creates a new reservation for a workshop.
     * This method checks if the workshop is available before creating the reservation.
     * If the workshop is available, it creates the reservation and increments the workshop's current bookings.
     *
     * @param workshopId     The ID of the workshop to reserve
     * @param attendeeName   The name of the attendee
     * @param attendeeEmail  The email of the attendee
     * @return An Optional containing the created reservation as a DTO if successful, or an empty Optional if the workshop is not available
     */
    @Transactional
    public Optional<ReservationDTO> createReservation(Integer workshopId, String attendeeName, String attendeeEmail) {
        return workshopRepository.findById(workshopId)
                .filter(Workshop::isAvailable)
                .map(workshop -> {
                    // Create the reservation
                    Reservation reservation = new Reservation(
                            workshop,
                            attendeeName,
                            attendeeEmail,
                            LocalDateTime.now()
                    );
                    
                    // Increment the workshop's current bookings
                    workshop.setCurrentBookings(workshop.getCurrentBookings() + 1);
                    workshopRepository.save(workshop);
                    
                    // Save and return the reservation
                    return new ReservationDTO(reservationRepository.save(reservation));
                });
    }

    /**
     * Cancels a reservation by its ID.
     * This method decrements the workshop's current bookings when a reservation is cancelled.
     *
     * @param id The ID of the reservation to cancel
     * @return true if the reservation was cancelled, false if it was not found
     */
    @Transactional
    public boolean cancelReservation(Integer id) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    // Decrement the workshop's current bookings
                    Workshop workshop = reservation.getWorkshop();
                    workshop.setCurrentBookings(workshop.getCurrentBookings() - 1);
                    workshopRepository.save(workshop);
                    
                    // Delete the reservation
                    reservationRepository.delete(reservation);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Counts the number of reservations for a specific workshop.
     *
     * @param workshopId The ID of the workshop to count reservations for
     * @return The number of reservations for the workshop
     */
    public long countReservationsByWorkshopId(Integer workshopId) {
        return reservationRepository.countByWorkshopId(workshopId);
    }
}