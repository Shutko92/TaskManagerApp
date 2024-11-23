package org.example.taskmanager.repository;

import org.example.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByAuthorAndCreationDateBetween(long author, LocalDate creationDate, LocalDate creationDate2, Pageable pageable);

    Page<Task> findAllByAssigneeAndCreationDateBetween(long assignee, LocalDate from, LocalDate to, Pageable pageable);
}
