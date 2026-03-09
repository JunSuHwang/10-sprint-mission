CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at timestamptz         NOT NULL,
    updated_at timestamptz,
    username   varchar(50) unique  NOT NULL,
    email      varchar(100) unique NOT NULL,
    password   varchar(60)         NOT NULL,
    profile_id UUID unique
);
CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  timestamptz                                       NOT NULL,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) check (type in ('PUBLIC', 'PRIVATE')) NOT NULL
);
CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    content    TEXT,
    channel_id UUID        NOT NULL,
    author_id  UUID,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL
);
CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     timestamptz NOT NULL,
    updated_at     timestamptz,
    user_id        UUID unique NOT NULL,
    last_active_at timestamptz NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   timestamptz NOT NULL,
    updated_at   timestamptz,
    user_id      UUID        NOT NULL,
    channel_id   UUID        NOT NULL,
    last_read_at timestamptz NOT NULL,
    UNIQUE (user_id, channel_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);
CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   timestamptz  NOT NULL,
    file_name    varchar(255) NOT NULL,
    size         bigint       NOT NULL,
    content_type varchar(100) NOT NULL,
    bytes        bytea        NOT NULL
);
CREATE TABLE message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL,
    UNIQUE (message_id, attachment_id),
    FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);
ALTER TABLE users
    ADD CONSTRAINT fk_users_binary_contents FOREIGN KEY (profile_id)
        REFERENCES binary_contents (id) ON DELETE SET NULL;
ALTER TABLE binary_contents
    DROP COLUMN bytes;