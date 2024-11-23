package org.example.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TasksRequest {
    private Long authorId;
    private Long assigneeId;
    private LocalDate from;
    private LocalDate to;
    private int offset;
    private int pageSize;
}
