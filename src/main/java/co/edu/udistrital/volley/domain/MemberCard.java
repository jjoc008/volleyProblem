package co.edu.udistrital.volley.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A MemberCard.
 */
@Entity
@Table(name = "member_card")
public class MemberCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_identification")
    private Integer memberIdentification;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "commencement_date")
    private String commencementDate;

    @OneToOne
    @JoinColumn(unique = true)
    private PostalAddress postalAddress;

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

    public MemberCard memberIdentification(Integer memberIdentification) {
        this.memberIdentification = memberIdentification;
        return this;
    }

    public void setMemberIdentification(Integer memberIdentification) {
        this.memberIdentification = memberIdentification;
    }

    public String getMemberName() {
        return memberName;
    }

    public MemberCard memberName(String memberName) {
        this.memberName = memberName;
        return this;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public MemberCard birthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCommencementDate() {
        return commencementDate;
    }

    public MemberCard commencementDate(String commencementDate) {
        this.commencementDate = commencementDate;
        return this;
    }

    public void setCommencementDate(String commencementDate) {
        this.commencementDate = commencementDate;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public MemberCard postalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
        return this;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberCard)) {
            return false;
        }
        return id != null && id.equals(((MemberCard) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MemberCard{" +
            "id=" + getId() +
            ", memberIdentification=" + getMemberIdentification() +
            ", memberName='" + getMemberName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            "}";
    }
}
