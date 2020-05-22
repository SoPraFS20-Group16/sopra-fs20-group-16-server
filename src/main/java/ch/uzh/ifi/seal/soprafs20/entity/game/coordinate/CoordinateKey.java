package ch.uzh.ifi.seal.soprafs20.entity.game.coordinate;

import java.io.Serializable;

public class CoordinateKey implements Serializable {
    int x;
    int y;

    @Override
    public int hashCode() {

        int hash = 7;
        hash = 31 * hash + x;
        hash = 31 * hash + y;

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        CoordinateKey other = (CoordinateKey) o;

        return this.x == other.x && this.y == other.y;
    }
}
