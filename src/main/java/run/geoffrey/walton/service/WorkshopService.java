package run.geoffrey.walton.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.geoffrey.walton.dto.WorkshopDTO;
import run.geoffrey.walton.model.Workshop;
import run.geoffrey.walton.repository.WorkshopRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Workshop entity.
 * Contains business logic for workshop operations.
 */
@Service
public class WorkshopService {

    private final WorkshopRepository workshopRepository;

    @Autowired
    public WorkshopService(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    /**
     * Gets all workshops.
     *
     * @return A list of all workshops as DTOs
     */
    public List<WorkshopDTO> getAllWorkshops() {
        return workshopRepository.findAll().stream()
                .map(WorkshopDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all upcoming workshops (workshops that are scheduled to take place after the current date and time).
     *
     * @return A list of upcoming workshops as DTOs
     */
    public List<WorkshopDTO> getUpcomingWorkshops() {
        return workshopRepository.findByDateTimeAfterOrderByDateTimeAsc(LocalDateTime.now()).stream()
                .map(WorkshopDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all past workshops (workshops that took place before the current date and time).
     *
     * @return A list of past workshops as DTOs
     */
    public List<WorkshopDTO> getPastWorkshops() {
        return workshopRepository.findByDateTimeBeforeOrderByDateTimeDesc(LocalDateTime.now()).stream()
                .map(WorkshopDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all available workshops (workshops that have available slots and are not in the past).
     *
     * @return A list of available workshops as DTOs
     */
    public List<WorkshopDTO> getAvailableWorkshops() {
        return workshopRepository.findAvailableWorkshops().stream()
                .map(WorkshopDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all fully booked workshops (workshops that have no available slots but are not in the past).
     *
     * @return A list of fully booked workshops as DTOs
     */
    public List<WorkshopDTO> getFullyBookedWorkshops() {
        return workshopRepository.findFullyBookedWorkshops().stream()
                .map(WorkshopDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets a workshop by its ID.
     *
     * @param id The ID of the workshop to get
     * @return An Optional containing the workshop as a DTO if found, or an empty Optional if not found
     */
    public Optional<WorkshopDTO> getWorkshopById(Integer id) {
        return workshopRepository.findById(id)
                .map(WorkshopDTO::new);
    }

    /**
     * Checks if a workshop is available for booking.
     *
     * @param id The ID of the workshop to check
     * @return true if the workshop is available, false otherwise
     */
    public boolean isWorkshopAvailable(Integer id) {
        return workshopRepository.findById(id)
                .map(Workshop::isAvailable)
                .orElse(false);
    }

    /**
     * Gets the number of available slots for a workshop.
     *
     * @param id The ID of the workshop to check
     * @return The number of available slots, or 0 if the workshop is not found
     */
    public int getAvailableSlots(Integer id) {
        return workshopRepository.findById(id)
                .map(Workshop::getAvailableSlots)
                .orElse(0);
    }
}
