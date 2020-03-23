package co.edu.udistrital.volley.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Applicant.
 */
@Entity
@Table(name = "applicant")
public class Applicant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(unique = true)
    private Letter letter;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Applicant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Letter getLetter() {
        return letter;
    }

    public Applicant letter(Letter letter) {
        this.letter = letter;
        return this;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Applicant)) {
            return false;
        }
        return id != null && id.equals(((Applicant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Applicant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
