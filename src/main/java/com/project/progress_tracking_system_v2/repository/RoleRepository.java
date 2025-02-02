package com.project.progress_tracking_system_v2.repository;

import com.project.progress_tracking_system_v2.constants.ROLE;
import com.project.progress_tracking_system_v2.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
    public Optional<Role> save(Role role);
    public Set<Role> findByNameIn(Set<ROLE> roles);
}
