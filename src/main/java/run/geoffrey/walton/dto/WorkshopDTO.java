package run.geoffrey.walton.dto;

import run.geoffrey.walton.model.Workshop;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data Transfer Object for Workshop entity.
 * Used to transfer workshop data between the service and controller layers.
 */
public class WorkshopDTO {

    private Integer id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String formattedDateTime;
    private Integer capacity;
    private Integer currentBookings;
    private Integer availableSlots;
    private boolean available;
    private boolean past;

    /**
     * Default constructor.
     */
    public WorkshopDTO() {
    }

    /**
     * Constructor that creates a DTO from a Workshop entity.
     *
     * @param workshop The Workshop entity
     */
    public WorkshopDTO(Workshop workshop) {
        this.id = workshop.getId();
        this.name = workshop.getName();
        this.description = workshop.getDescription();
        this.dateTime = workshop.getDateTime();
        this.formattedDateTime = formatDateTime(workshop.getDateTime());
        this.capacity = workshop.getCapacity();
        this.currentBookings = workshop.getCurrentBookings();
        this.availableSlots = workshop.getAvailableSlots();
        this.available = workshop.isAvailable();
        this.past = workshop.isPast();
    }

    /**
     * Formats the date and time for display.
     *
     * @param dateTime The date and time to format
     * @return The formatted date and time string
     */
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
        return dateTime.format(formatter);
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.formattedDateTime = formatDateTime(dateTime);
    }

    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentBookings() {
        return currentBookings;
    }

    public void setCurrentBookings(Integer currentBookings) {
        this.currentBookings = currentBookings;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isPast() {
        return past;
    }

    public void setPast(boolean past) {
        this.past = past;
    }
}
