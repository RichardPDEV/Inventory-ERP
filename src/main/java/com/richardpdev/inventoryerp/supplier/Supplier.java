package com.richardpdev.inventoryerp.supplier;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 120)
    private String name;

    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    public Supplier() {}

    public Supplier(String name, String contactEmail, String contactPhone) {
        this.name = name;
        this.contactEmail = contactEmail != null ? contactEmail.trim().isEmpty() ? null : contactEmail.trim() : null;
        this.contactPhone = contactPhone != null ? contactPhone.trim().isEmpty() ? null : contactPhone.trim() : null;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getContactEmail() { return contactEmail; }
    public String getContactPhone() { return contactPhone; }

    public void setName(String name) { this.name = name; }
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail != null && !contactEmail.trim().isEmpty() ? contactEmail.trim() : null;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone != null && !contactPhone.trim().isEmpty() ? contactPhone.trim() : null;
    }
}
