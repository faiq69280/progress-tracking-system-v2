package com.project.progress_tracking_system_v2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.project.progress_tracking_system_v2.constants.ContactType;
import com.project.progress_tracking_system_v2.domain.NotificationSubscriber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.*;


@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class User extends BaseEntity implements NotificationSubscriber {

    @Column
    private String name;


    @Column
    private String password;

    @OneToMany(mappedBy = "assignedUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "employee-task")
    @JsonIgnore
    private List<Task> tasks;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_uuid"),
            inverseJoinColumns = @JoinColumn(name = "role_uuid"))
    Set<Role> roles;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "user")
    @JsonManagedReference(value = "user-contact")
    Set<ContactInformation> contactInformation;


    public User() {}

    public User(String uuid, String name, Set<Role> roles) {
        this.uuid = uuid;
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    //@JsonIgnore

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setContactInformation(Set<ContactInformation> contactInformation) {
        this.contactInformation = contactInformation;
    }

    public Set<ContactInformation> getContactInformation() {
        return contactInformation;
    }

    @Override
    public EnumMap<ContactType, String> getSubscriptionInformation() {
        final EnumMap<ContactType, String> subscriptionInformation = new EnumMap<>(ContactType.class);
        contactInformation.stream().filter(ContactInformation::notifiable).forEach(contact -> {
                subscriptionInformation.put(contact.getType(), contact.getInformation());
        });
        return subscriptionInformation;
    }
}
