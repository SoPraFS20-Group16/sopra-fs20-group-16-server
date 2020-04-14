package ch.uzh.ifi.seal.soprafs20.entity.game.coordinate;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "COORDINATE")
@IdClass(CoordinateKey.class)
public class Coordinate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false, nullable = false)
    private int x;

    @Id
    @Column(updatable = false, nullable = false)
    private int y;

    @Size(max = 3)
    @ManyToMany
    private List<Coordinate> neighbors;

    public Coordinate() {
        this.neighbors = new ArrayList<>();
    }

    public Coordinate(int x, int y) {
        this.setX(x);
        this.setY(y);
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Coordinate> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Coordinate> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        Coordinate other = (Coordinate) o;

        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    @Override
    public int hashCode() {

        int hash = 7;
        hash = 31 * hash + x;
        hash = 31 * hash + y;

        return hash;
    }
}
