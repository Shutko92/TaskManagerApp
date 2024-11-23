package org.example.taskmanager.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.dto.NewCommentRequest;
import org.example.taskmanager.dto.NewTaskRequest;
import org.example.taskmanager.dto.TasksRequest;
import org.example.taskmanager.exception.EntityNotFoundException;
import org.example.taskmanager.model.Comment;
import org.example.taskmanager.model.Task;
import org.example.taskmanager.model.TaskStatus;
import org.example.taskmanager.model.User;
import org.example.taskmanager.repository.CommentRepository;
import org.example.taskmanager.repository.TaskRepository;
import org.example.taskmanager.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagerService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Operation(summary = "Create a new task", description = "Creates a new task associated with the given author ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Task createTask(@Parameter(description = "New task request containing task details")
                               NewTaskRequest request) {
        Task task = formTask(request);

        return taskRepository.save(task);
    }

    @Operation(summary = "Add a comment to a task", description = "Adds a comment to the specified task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @ApiResponse(responseCode = "404", description = "Task or author not found")
    })
    public Comment addCommentToTask(@Parameter(description = "New comment request containing comment details")
                                        NewCommentRequest request) {
        Task task = getExistingTask(request.getTaskId());
        User author = getExistingUser(request.getAuthorId());

        Comment comment = formComment(request.getContent(), task, author.getId());

        bindCommentOnTask(task, comment);

        return commentRepository.save(comment);
    }

    private Comment formComment(String content, Task task, long author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTaskId(task.getId());
        comment.setAuthor(author);
        return comment;
    }

    private void bindCommentOnTask(Task task, Comment comment) {
        if (task.getComments()!=null) {
            List<Comment> comments = task.getComments();
            comments.add(comment);
            task.setComments(comments);
            taskRepository.save(task);
        } else {
            task.setComments(List.of(comment));
            taskRepository.save(task);
        }
    }

    @Operation(summary = "Set assignee for a task", description = "Assigns a user to a specific task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignee set successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public Task setAssignee(@Parameter(description = "ID of the assignee") long assigneeId,
                            @Parameter(description = "ID of the task") long taskId) {
        Task task = getExistingTask(taskId);
        User assignee = getExistingUser(assigneeId);
        task.setAssignee(assignee.getId());
        return taskRepository.save(task);
    }

    @Operation(summary = "Get paginated tasks", description = "Retrieves tasks based on author or assignee ID with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    public Page<Task> getTasksPaged(@Parameter(description = "New task request containing task details") TasksRequest request) {
        if ((request.getAuthorId() != null && request.getAssigneeId() != null) ||
                (request.getAuthorId() == null && request.getAssigneeId() == null)) {
            throw new RuntimeException("Either authorId or assigneeId must be provided, but not both.");
        }

        Pageable pageable = PageRequest.of(request.getOffset(), request.getPageSize());

        return request.getAuthorId() != null?
                taskRepository.findAllByAuthorAndCreationDateBetween(request.getAuthorId(), request.getFrom(), request.getTo(), pageable):
                taskRepository.findAllByAssigneeAndCreationDateBetween(request.getAssigneeId(), request.getFrom(), request.getTo(), pageable);
    }

    @Operation(summary = "Change status of a task", description = "Updates the status of the specified task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public Task changeTaskStatus(@Parameter(description = "ID of the task") long id,
                                 @Parameter(description = "New status for the task") TaskStatus newStatus) {
        Task task = getExistingTask(id);
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    @Operation(summary = "Delete a task", description = "Deletes the specified task from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public void deleteTask(@Parameter(description="ID of the task to delete") long taskId) {
        taskRepository.deleteById(taskId);
    }

    private Task formTask(NewTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAuthor(request.getAuthorId());
        task.setAssignee(request.getAssignee());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(request.getPriority());
        task.setCreationDate(LocalDate.now());
        return task;
    }

    private User getExistingUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
    }

    private Task getExistingTask(long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }
}
