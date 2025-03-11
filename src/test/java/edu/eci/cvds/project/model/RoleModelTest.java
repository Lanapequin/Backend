package edu.eci.cvds.project.model;

import edu.eci.cvds.project.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleModelTest {

    @Test
    public void testRoleToString() {
        assertEquals("ADMIN", Role.ADMIN.toString());
        assertEquals("USER", Role.USER.toString());
    }
}