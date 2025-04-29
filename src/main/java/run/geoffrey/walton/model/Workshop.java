package run.geoffrey.walton.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a workshop at the Walton Sustainability Centre.
 * This class is used to store information about workshops, including their
 * name, description, date and time, capacity, and current number of bookings.
 */
@Entity
@Table(name = "workshops")
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer currentBookings;

    @Column(nullable = false)
    private Double price;

    /**
     * Default constructor required by JPA.
     */
    public Workshop() {
    }

    /**
     * Constructor with all required fields.
     *
     * @param name            The name of the workshop
     * @param description     The description of the workshop
     * @param dateTime        The date and time when the workshop will take place
     * @param capacity        The maximum number of participants
     * @param currentBookings The current number of bookings
     * @param price           The price of the workshop
     */
    public Workshop(String name, String description, LocalDateTime dateTime, Integer capacity, Integer currentBookings, Double price) {
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.capacity = capacity;
        this.currentBookings = currentBookings;
        this.price = price;
    }

    /**
     * Checks if the workshop is available for booking.
     *
     * @return true if the workshop has available slots, false otherwise
     */
    public boolean isAvailable() {
        return currentBookings < capacity && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Checks if the workshop is in the past.
     *
     * @return true if the workshop date is in the past, false otherwise
     */
    public boolean isPast() {
        return dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Gets the number of available slots for the workshop.
     *
     * @return the number of available slots
     */
    public int getAvailableSlots() {
        return capacity - currentBookings;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workshop workshop = (Workshop) o;
        return Objects.equals(id, workshop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Workshop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", capacity=" + capacity +
                ", currentBookings=" + currentBookings +
                '}';
    }
}
