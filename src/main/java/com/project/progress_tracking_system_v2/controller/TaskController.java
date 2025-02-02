package com.project.progress_tracking_system_v2.controller;

import com.project.progress_tracking_system_v2.dto.UserTaskAssignmentDTO;
import com.project.progress_tracking_system_v2.entity.Task;
import com.project.progress_tracking_system_v2.annotation.TaskAuthorization;
import com.project.progress_tracking_system_v2.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("userTasks")
    public ResponseEntity<List<Task>> getUserTasks() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(taskService.getUserTasks(userName), HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Task> save(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.save(task), HttpStatus.OK);
    }

    @PostMapping("update")
    @TaskAuthorization
    public ResponseEntity<Task> update(@RequestBody Task task) throws IllegalAccessException {
        return new ResponseEntity<>(taskService.update(task), HttpStatus.OK);
    }

    @PostMapping("assignToUser")
    public ResponseEntity<Task> assignToUser(@RequestBody UserTaskAssignmentDTO userTaskAssignmentDTO) {
       return new ResponseEntity<>(taskService.assignToUser(userTaskAssignmentDTO), HttpStatus.OK);
    }
}
