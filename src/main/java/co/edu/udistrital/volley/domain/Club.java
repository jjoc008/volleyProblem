package co.edu.udistrital.volley.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Club.
 */
@Entity
@Table(name = "club")
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("clubs")
    private Administrator administrator;

    @ManyToOne
    @JsonIgnoreProperties("clubs")
    private Secretary secretary;

    @ManyToOne
    @JsonIgnoreProperties("clubs")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public Club identificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
        return this;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getName() {
        return name;
    }

    public Club name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public Club administrator(Administrator administrator) {
        this.administrator = administrator;
        return this;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public Secretary getSecretary() {
        return secretary;
    }

    public Club secretary(Secretary secretary) {
        this.secretary = secretary;
        return this;
    }

    public void setSecretary(Secretary secretary) {
        this.secretary = secretary;
    }

    public Member getMember() {
        return member;
    }

    public Club member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        return id != null && id.equals(((Club) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Club{" +
            "id=" + getId() +
            ", identificationNumber='" + getIdentificationNumber() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
