package com.project.progress_tracking_system_v2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.progress_tracking_system_v2.constants.ROLE;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Role extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    ROLE name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    Set<User> users;

    Role() {}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ROLE getName() {
        return name;
    }

    public void setName(ROLE name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.toString().equals(role.name.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name.toString());
    }
}
