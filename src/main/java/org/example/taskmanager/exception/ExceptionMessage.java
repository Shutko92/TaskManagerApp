package org.example.taskmanager.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    BINDING_RESULTS_ERROR("Ошибка в данных запроса");

    private final String message;
}
