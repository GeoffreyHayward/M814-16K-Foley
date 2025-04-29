package run.geoffrey.walton.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a reservation for a workshop at the Walton Sustainability Centre.
 * This class is used to store information about reservations, including the workshop
 * being reserved, the attendee's name and email, and the reservation date.
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    @Column(nullable = false)
    private String attendeeName;

    @Column(nullable = false)
    private String attendeeEmail;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    /**
     * Default constructor required by JPA.
     */
    public Reservation() {
    }

    /**
     * Constructor with all required fields.
     *
     * @param workshop        The workshop being reserved
     * @param attendeeName    The name of the attendee
     * @param attendeeEmail   The email of the attendee
     * @param reservationDate The date when the reservation was made
     */
    public Reservation(Workshop workshop, String attendeeName, String attendeeEmail, LocalDateTime reservationDate) {
        this.workshop = workshop;
        this.attendeeName = attendeeName;
        this.attendeeEmail = attendeeEmail;
        this.reservationDate = reservationDate;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public String getAttendeeEmail() {
        return attendeeEmail;
    }

    public void setAttendeeEmail(String attendeeEmail) {
        this.attendeeEmail = attendeeEmail;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", workshop=" + workshop.getId() +
                ", attendeeName='" + attendeeName + '\'' +
                ", attendeeEmail='" + attendeeEmail + '\'' +
                ", reservationDate=" + reservationDate +
                '}';
    }
}