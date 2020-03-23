package co.edu.udistrital.volley.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A RegistryBook.
 */
@Entity
@Table(name = "registry_book")
public class RegistryBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_identification")
    private Integer memberIdentification;

    @ManyToOne
    @JsonIgnoreProperties("registryBooks")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMemberIdentification() {
        return memberIdentification;
    }

    public RegistryBook memberIdentification(Integer memberIdentification) {
        this.memberIdentification = memberIdentification;
        return this;
    }

    public void setMemberIdentification(Integer memberIdentification) {
        this.memberIdentification = memberIdentification;
    }

    public Member getMember() {
        return member;
    }

    public RegistryBook member(Member member) {
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
        if (!(o instanceof RegistryBook)) {
            return false;
        }
        return id != null && id.equals(((RegistryBook) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RegistryBook{" +
            "id=" + getId() +
            ", memberIdentification=" + getMemberIdentification() +
            "}";
    }
}
