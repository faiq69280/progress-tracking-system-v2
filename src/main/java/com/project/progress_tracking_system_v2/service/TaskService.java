package com.project.progress_tracking_system_v2.service;

import com.project.progress_tracking_system_v2.constants.Progress;
import com.project.progress_tracking_system_v2.constants.ROLE;
import com.project.progress_tracking_system_v2.dto.UserTaskAssignmentDTO;
import com.project.progress_tracking_system_v2.entity.Role;
import com.project.progress_tracking_system_v2.entity.Task;
import com.project.progress_tracking_system_v2.entity.User;
import com.project.progress_tracking_system_v2.repository.TaskRepository;
import com.project.progress_tracking_system_v2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    public List<Task> getUserTasks(String userName) {
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find this user"));
        return taskRepository.findByAssignedUser_Uuid(user.getUuid());
    }

    public Task save(Task task) {
        Objects.requireNonNull(task);
        if (task.getUuid() != null) { throw new IllegalArgumentException("Task with this UUID is already created"); }
        task.setProgress(Objects.requireNonNullElse(task.getProgress(), Progress.NOT_STARTED));
        return taskRepository.save(task);
    }

    public Task assignToUser(UserTaskAssignmentDTO userTaskAssignmentDTO) {
        Objects.requireNonNull(userTaskAssignmentDTO.taskUuid());
        Objects.requireNonNull(userTaskAssignmentDTO.userName());
        Task fetchedTask = taskRepository.findById(userTaskAssignmentDTO.taskUuid())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        User user = userRepository.findByName(userTaskAssignmentDTO.userName())
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find userName : %s".formatted(userTaskAssignmentDTO.userName())));
        fetchedTask.setUser(user);
        return taskRepository.save(fetchedTask);
    }

    public Task update(Task task) {
        Objects.requireNonNull(task);
        Objects.requireNonNull(task.getUuid());
        Task fetchedTask = taskRepository.findById(task.getUuid()).orElseThrow(() -> new IllegalArgumentException("Task with this uuid doesn't exist"));
        if (ObjectUtils.isEmpty(fetchedTask.getSubTasks())) {
            fetchedTask.setProgress(Objects.requireNonNullElse(task.getProgress(), fetchedTask.getProgress()));
        }
        fetchedTask.setDueDate(Objects.requireNonNullElse(task.getDueDate(), fetchedTask.getDueDate()));
        fetchedTask.setName(Objects.requireNonNullElse(task.getName(), fetchedTask.getName()));
        fetchedTask.setDescription(Objects.requireNonNullElse(task.getDescription(), fetchedTask.getDescription()));
        return taskRepository.save(fetchedTask);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>((Collection) taskRepository.findAll());
    }

}
