package com.project.progress_tracking_system_v2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.progress_tracking_system_v2.constants.Progress;
import com.project.progress_tracking_system_v2.constants.ROLE;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;
import java.util.List;

@Entity
public class Task extends BaseEntity {
   @Column
   @NotNull
   private String name;

   @Column
   private String description;

   @Column
   private Date dueDate;

   @Column
   @Enumerated(EnumType.STRING)
   private Progress progress;


   @JsonBackReference(value = "task-reference")
   @ManyToOne
   @JoinColumn(name = "parent_id")
   private Task parentTask;

   @JsonManagedReference(value = "task-reference")
   @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Task> subTasks;

   @JsonBackReference(value = "employee-task")
   @ManyToOne
   @JoinColumn(name = "assigned_user_uuid")
   User assignedUser;

   public Task() {}

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Date getDueDate() {
      return dueDate;
   }

   public void setDueDate(Date dueDate) {
      this.dueDate = dueDate;
   }

   public Progress getProgress() {
      return progress;
   }

   public void setProgress(Progress progress) {
      this.progress = progress;
   }

   public Task getParentTask() {
      return parentTask;
   }

   public void setParentTask(Task parentTask) {
      this.parentTask = parentTask;
   }

   public List<Task> getSubTasks() {
      return subTasks;
   }

   public void setSubTasks(List<Task> subTasks) {
      this.subTasks = subTasks;
   }

   public User getUser() {
      return assignedUser;
   }

   public void setUser(User user) {
      this.assignedUser = user;
   }

   public @NotNull String getName() {
      return name;
   }

   public void setName(@NotNull String name) {
      this.name = name;
   }

}
