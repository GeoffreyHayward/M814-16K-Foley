package run.geoffrey.walton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import run.geoffrey.walton.dto.ReservationDTO;
import run.geoffrey.walton.dto.WorkshopDTO;
import run.geoffrey.walton.service.ReservationService;
import run.geoffrey.walton.service.WorkshopService;

import java.util.List;

/**
 * Controller for reservation-related endpoints.
 */
@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final WorkshopService workshopService;

    @Autowired
    public ReservationController(ReservationService reservationService, WorkshopService workshopService) {
        this.reservationService = reservationService;
        this.workshopService = workshopService;
    }

    /**
     * Displays a form for creating a new reservation for a specific workshop.
     *
     * @param workshopId The ID of the workshop to reserve
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/create/{workshopId}")
    public String showReservationForm(@PathVariable Integer workshopId, Model model) {
        return workshopService.getWorkshopById(workshopId)
                .filter(WorkshopDTO::isAvailable)
                .map(workshop -> {
                    model.addAttribute("workshop", workshop);
                    return "reservations/create";
                })
                .orElse("redirect:/workshops");
    }

    /**
     * Processes the form submission for creating a new reservation.
     *
     * @param workshopId The ID of the workshop to reserve
     * @param attendeeName The name of the attendee
     * @param attendeeEmail The email of the attendee
     * @param redirectAttributes Attributes to add to the redirect
     * @return The name of the view to render
     */
    @PostMapping("/create/{workshopId}")
    public String createReservation(
            @PathVariable Integer workshopId,
            @RequestParam String attendeeName,
            @RequestParam String attendeeEmail,
            RedirectAttributes redirectAttributes) {
        
        return reservationService.createReservation(workshopId, attendeeName, attendeeEmail)
                .map(reservation -> {
                    redirectAttributes.addFlashAttribute("success", "Reservation created successfully!");
                    return "redirect:/reservations/confirmation/" + reservation.getId();
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Workshop is no longer available.");
                    return "redirect:/workshops";
                });
    }

    /**
     * Displays a confirmation page for a newly created reservation.
     *
     * @param id The ID of the reservation to display
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/confirmation/{id}")
    public String showConfirmation(@PathVariable Integer id, Model model) {
        return reservationService.getReservationById(id)
                .map(reservation -> {
                    model.addAttribute("reservation", reservation);
                    return "reservations/confirmation";
                })
                .orElse("redirect:/workshops");
    }

    /**
     * Displays a list of all reservations for a specific workshop.
     * This endpoint is for administrative purposes.
     *
     * @param workshopId The ID of the workshop to get reservations for
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/workshop/{workshopId}")
    public String listReservationsByWorkshop(@PathVariable Integer workshopId, Model model) {
        List<ReservationDTO> reservations = reservationService.getReservationsByWorkshopId(workshopId);
        
        return workshopService.getWorkshopById(workshopId)
                .map(workshop -> {
                    model.addAttribute("workshop", workshop);
                    model.addAttribute("reservations", reservations);
                    return "reservations/list";
                })
                .orElse("redirect:/workshops");
    }

    /**
     * Displays a form for canceling a reservation.
     *
     * @param id The ID of the reservation to cancel
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/cancel/{id}")
    public String showCancelForm(@PathVariable Integer id, Model model) {
        return reservationService.getReservationById(id)
                .map(reservation -> {
                    model.addAttribute("reservation", reservation);
                    return "reservations/cancel";
                })
                .orElse("redirect:/workshops");
    }

    /**
     * Processes the form submission for canceling a reservation.
     *
     * @param id The ID of the reservation to cancel
     * @param redirectAttributes Attributes to add to the redirect
     * @return The name of the view to render
     */
    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean canceled = reservationService.cancelReservation(id);
        
        if (canceled) {
            redirectAttributes.addFlashAttribute("success", "Reservation canceled successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Reservation not found.");
        }
        
        return "redirect:/workshops";
    }
}