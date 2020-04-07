package ch.uzh.ifi.seal.soprafs20.entity.gameEntities;


import javax.persistence.*;

@Entity
@Table(name = "COORDINATE")
public class Coordinate {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
