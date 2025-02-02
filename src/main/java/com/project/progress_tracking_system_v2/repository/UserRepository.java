package com.project.progress_tracking_system_v2.repository;


import com.project.progress_tracking_system_v2.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
   public Optional<User> findByName(String name);
   public User save(User user);
   public List<User> findAll();
}
