--liquibase formatted sql
-- changeset TelegaData:1
CREATE TABLE notification_task
(
    id integer PRIMARY KEY,
    text varchar(256),
    chat_id bigint NOT NULL,
    date_time timestamp without time zone
);

