package org.example.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.dto.NewCommentRequest;
import org.example.taskmanager.dto.NewTaskRequest;
import org.example.taskmanager.dto.TasksRequest;
import org.example.taskmanager.model.Comment;
import org.example.taskmanager.model.Task;
import org.example.taskmanager.model.TaskStatus;
import org.example.taskmanager.service.TaskManagerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {
    private final TaskManagerService taskManagerService;

    @Operation(summary = "Create a new task", description = "Creates a new task associated with the given author ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/task")
    public ResponseEntity<Task> createTask(@RequestBody NewTaskRequest request) {
        return ResponseEntity.ok(taskManagerService.createTask(request));
    }

    @Operation(summary = "Add a comment to a task", description = "Adds a comment to the specified task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @ApiResponse(responseCode = "404", description = "Task or author not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/task/comment")
    public ResponseEntity<Comment> addComment(@RequestBody NewCommentRequest request) {
        return ResponseEntity.ok(taskManagerService.addCommentToTask(request));
    }

    @Operation(summary = "Assign a user to a task", description = "Assigns a user to a specific task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignee set successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/task/assign/{assigneeId}")
    public ResponseEntity<Task> setAssignee(@PathVariable long assigneeId, @RequestHeader long taskId) {
        return ResponseEntity.ok(taskManagerService.setAssignee(assigneeId, taskId));
    }

    @Operation(summary = "Get tasks with pagination", description = "Retrieves tasks based on author or assignee ID with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/tasks")
    public ResponseEntity<Page<Task>> getTasks(@RequestBody TasksRequest request) {

        return ResponseEntity.ok(taskManagerService.getTasksPaged(request));
    }

    @Operation(summary = "Delete a task", description = "Deletes the specified task from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/task/{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable long taskId) {
        taskManagerService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary="Change status of a task", description="Updates the status of the specified task.")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="Status updated successfully"),
            @ApiResponse(responseCode="404", description="Task not found"),
            @ApiResponse(responseCode="403", description="Access denied")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<Task> changeTaskStatus(@PathVariable long id, @RequestBody TaskStatus newStatus) {
        Task updatedTask = taskManagerService.changeTaskStatus(id, newStatus);
        return ResponseEntity.ok(updatedTask);
    }
}
