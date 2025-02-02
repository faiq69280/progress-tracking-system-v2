package com.project.progress_tracking_system_v2.repository;

import com.project.progress_tracking_system_v2.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, String> {
    public List<Task> findByAssignedUser_Uuid(String userUuid);
    public Task save(Task task);
}
