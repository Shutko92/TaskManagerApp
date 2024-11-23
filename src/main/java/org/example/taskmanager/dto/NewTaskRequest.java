package org.example.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.taskmanager.model.TaskPriority;

@Getter
@Setter
public class NewTaskRequest {
    private long authorId;
    private String title;
    private String description;
    private TaskPriority priority;
    private long assignee;
}
