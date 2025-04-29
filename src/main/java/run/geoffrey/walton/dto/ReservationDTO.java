package run.geoffrey.walton.dto;

import run.geoffrey.walton.model.Reservation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data Transfer Object for Reservation entity.
 * Used to transfer reservation data between the service and controller layers.
 */
public class ReservationDTO {

    private Integer id;
    private Integer workshopId;
    private String workshopName;
    private String attendeeName;
    private String attendeeEmail;
    private LocalDateTime reservationDate;
    private String formattedReservationDate;
    private String workshopDateTime;

    /**
     * Default constructor.
     */
    public ReservationDTO() {
    }

    /**
     * Constructor that creates a DTO from a Reservation entity.
     *
     * @param reservation The Reservation entity
     */
    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.workshopId = reservation.getWorkshop().getId();
        this.workshopName = reservation.getWorkshop().getName();
        this.attendeeName = reservation.getAttendeeName();
        this.attendeeEmail = reservation.getAttendeeEmail();
        this.reservationDate = reservation.getReservationDate();
        this.formattedReservationDate = formatDateTime(reservation.getReservationDate());
        this.workshopDateTime = formatDateTime(reservation.getWorkshop().getDateTime());
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

    public Integer getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Integer workshopId) {
        this.workshopId = workshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
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
        this.formattedReservationDate = formatDateTime(reservationDate);
    }

    public String getFormattedReservationDate() {
        return formattedReservationDate;
    }

    public String getWorkshopDateTime() {
        return workshopDateTime;
    }

    public void setWorkshopDateTime(String workshopDateTime) {
        this.workshopDateTime = workshopDateTime;
    }
}