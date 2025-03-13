package edu.eci.cvds.project.model;

import edu.eci.cvds.project.model.Laboratory;
import edu.eci.cvds.project.model.Reservation;
import edu.eci.cvds.project.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationModelTest {

    @Test
    void testConstructorAndGettersAndSetters() {
        String id = "RES-001";
        String laboratoryname = "LAB-001";
        User user = new User();
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(2);
        String purpose = "Project meeting";
        boolean status = true;

        Reservation reservation = new Reservation(id,laboratoryname , user, startDateTime, endDateTime, purpose, status);

        assertEquals(id, reservation.getId());
        assertEquals(laboratoryname, reservation.getLaboratoryname());
        assertEquals(user, reservation.getUser());
        assertEquals(startDateTime, reservation.getStartDateTime());
        assertEquals(endDateTime, reservation.getEndDateTime());
        assertEquals(purpose, reservation.getPurpose());
        assertTrue(reservation.getStatus());

        String newPurpose = "Study session";
        boolean newStatus = false;

        reservation.setPurpose(newPurpose);
        reservation.setStatus(newStatus);

        assertEquals(newPurpose, reservation.getPurpose());
        assertFalse(reservation.getStatus());
    }

    @Test
    void testToString() {
        String id = "RES-001";
        String laboratoryname = "LAB-001";
        User user = new User();
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(2);
        String purpose = "Project meeting";
        boolean status = true;

        Reservation reservation = new Reservation(id, laboratoryname, user, startDateTime, endDateTime, purpose, status);

        String expectedString = "Reservation(id=RES-001, laboratoryname=" + "LAB-001" + ", user=" + user.toString() + ", startDateTime=" + startDateTime.toString() + ", endDateTime=" + endDateTime.toString() + ", purpose=Project meeting, Status=true)";
        assertEquals(expectedString, reservation.toString());
    }

    @Test
    void testGetStatusBoolean() {
        Reservation reservation = new Reservation();
        reservation.setStatus(true);
        assertTrue(reservation.getStatus());

        reservation.setStatus(false);
        assertFalse(reservation.getStatus());
    }
}
