package com.project.progress_tracking_system_v2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.progress_tracking_system_v2.annotation.HideFromReflection;
import com.project.progress_tracking_system_v2.constants.ContactType;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_uuid", "information"}))
public class ContactInformation extends BaseEntity {
    @Column
    private String information;
    @Column
    private ContactType type;

    @Column
    private boolean notify;

    @ManyToOne
    @JoinColumn(name = "user_uuid")
    @JsonBackReference(value = "user-contact")
    @HideFromReflection
    User user;

    public ContactInformation() {}

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean notifiable() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
