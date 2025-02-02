package com.project.progress_tracking_system_v2.security.aspect;

import com.project.progress_tracking_system_v2.constants.ROLE;
import com.project.progress_tracking_system_v2.entity.Task;
import com.project.progress_tracking_system_v2.repository.TaskRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Aspect
public class TaskAuthorizationAspect {
    @Autowired
    private TaskRepository taskRepository;

    @Pointcut("@annotation(com.project.progress_tracking_system_v2.annotation.annotation.TaskAuthorization)")
    public void taskAuthorizationPointcut() {
        // Pointcut for methods annotated with @TaskAuthorization
    }

    @Before("taskAuthorizationPointcut() && args(task,..)")
    public void checkTaskAuthorization(Task task) throws IllegalAccessException {

        Objects.requireNonNull(task);
        Objects.requireNonNull(task.getUuid(), "Can't check authorization for a task that doesn't exist");
        Task fetchedTask = taskRepository.findById(task.getUuid())
                .orElseThrow(() -> new IllegalArgumentException("Task with this uuid doesn't exist"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> grantedAuthorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        if (!grantedAuthorities.contains(ROLE.ADMIN.toString())) {
            if (Objects.isNull(fetchedTask.getUser())) {
                throw new IllegalAccessException("Only admins are allowed to modify unassigned tasks");
            } else if (fetchedTask.getUser().getName().equals(authentication.getName())) {
                throw new IllegalAccessException("Non admins can only modify their own tasks");
            }
        }
    }

}
