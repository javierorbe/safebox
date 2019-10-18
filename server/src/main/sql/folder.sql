CREATE TABLE IF NOT EXISTS folder(
    id              VARCHAR(36)     NOT NULL,
    user_id         VARCHAR(36)     NOT NULL,
    parent_id       VARCHAR(36)         NULL,
    name            TEXT            NOT NULL,
    creation        TIMESTAMP       NOT NULL,
    last_modified   TIMESTAMP       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)   REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES folder(id)
);
