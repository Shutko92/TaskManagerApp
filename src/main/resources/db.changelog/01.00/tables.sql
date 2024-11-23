CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    user_role VARCHAR CHECK (user_role IN ('ADMIN', 'USER'))
    );

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
    priority VARCHAR CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
    author BIGINT NOT NULL,
    assignee BIGINT,
    creation_date TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    content TEXT NOT NULL,
    task_id BIGINT NOT NULL,
    author BIGINT NOT NULL
    );