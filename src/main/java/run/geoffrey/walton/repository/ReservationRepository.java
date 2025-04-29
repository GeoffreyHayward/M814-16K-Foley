package run.geoffrey.walton.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import run.geoffrey.walton.model.Reservation;
import run.geoffrey.walton.model.Workshop;

import java.util.List;

/**
 * Repository interface for Reservation entity.
 * Provides methods to access and manipulate reservation data in the database.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /**
     * Finds all reservations for a specific workshop.
     *
     * @param workshop The workshop to find reservations for
     * @return A list of reservations for the workshop
     */
    List<Reservation> findByWorkshop(Workshop workshop);

    /**
     * Finds all reservations for a specific workshop ID.
     *
     * @param workshopId The ID of the workshop to find reservations for
     * @return A list of reservations for the workshop
     */
    List<Reservation> findByWorkshopId(Integer workshopId);

    /**
     * Finds all reservations for a specific attendee email.
     *
     * @param email The email of the attendee
     * @return A list of reservations for the attendee
     */
    List<Reservation> findByAttendeeEmail(String email);

    /**
     * Counts the number of reservations for a specific workshop.
     *
     * @param workshop The workshop to count reservations for
     * @return The number of reservations for the workshop
     */
    long countByWorkshop(Workshop workshop);

    /**
     * Counts the number of reservations for a specific workshop ID.
     *
     * @param workshopId The ID of the workshop to count reservations for
     * @return The number of reservations for the workshop
     */
    long countByWorkshopId(Integer workshopId);
}