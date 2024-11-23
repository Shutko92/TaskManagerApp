package org.example.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCommentRequest {
    private long taskId;
    private String content;
    private long authorId;
}
