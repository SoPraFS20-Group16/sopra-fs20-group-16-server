package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CoordinateTest {

    private Coordinate testCoordinate1;
    private Coordinate testCoordinate2;

    @BeforeEach
    void setUp() {
        testCoordinate1 = new Coordinate();
        testCoordinate1.setX(1);
        testCoordinate1.setY(1);

        testCoordinate2 = new Coordinate();
    }

    @Test
    void testEquals_true() {
        testCoordinate2.setX(1);
        testCoordinate2.setY(1);

        assertEquals(testCoordinate1, testCoordinate2, "The coordinates should be equal!");
        assertEquals(testCoordinate1.hashCode(), testCoordinate2.hashCode(), "The hashcode should match!");
    }

    @Test
    void testEquals_false() {
        testCoordinate2.setX(2);
        testCoordinate2.setY(1);

        assertNotEquals(testCoordinate1, testCoordinate2, "The coordinates should not be equal!");
        assertNotEquals(testCoordinate1.hashCode(), testCoordinate2.hashCode(),
                "The hash code should not be equal!");
    }
}
