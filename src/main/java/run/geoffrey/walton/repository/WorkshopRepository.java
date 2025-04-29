package run.geoffrey.walton.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import run.geoffrey.walton.model.Workshop;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Workshop entity.
 * Provides methods to access and manipulate workshop data in the database.
 */
@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Integer> {

    /**
     * Finds all workshops that are scheduled to take place after the current date and time.
     *
     * @param now The current date and time
     * @return A list of upcoming workshops
     */
    List<Workshop> findByDateTimeAfterOrderByDateTimeAsc(LocalDateTime now);

    /**
     * Finds all workshops that are scheduled to take place before the current date and time.
     *
     * @param now The current date and time
     * @return A list of past workshops
     */
    List<Workshop> findByDateTimeBeforeOrderByDateTimeDesc(LocalDateTime now);

    /**
     * Finds all workshops that have available slots.
     *
     * @return A list of workshops with available slots
     */
    @Query("SELECT w FROM Workshop w WHERE w.currentBookings < w.capacity AND w.dateTime > CURRENT_TIMESTAMP ORDER BY w.dateTime ASC")
    List<Workshop> findAvailableWorkshops();

    /**
     * Finds all workshops that are fully booked.
     *
     * @return A list of fully booked workshops
     */
    @Query("SELECT w FROM Workshop w WHERE w.currentBookings >= w.capacity AND w.dateTime > CURRENT_TIMESTAMP ORDER BY w.dateTime ASC")
    List<Workshop> findFullyBookedWorkshops();
}
