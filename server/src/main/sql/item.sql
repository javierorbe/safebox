CREATE TABLE IF NOT EXISTS item(
    id              VARCHAR(36)     NOT NULL,
    user_id         VARCHAR(36)     NOT NULL,
    folder_id       VARCHAR(36)         NULL,
    type            TINYINT         NOT NULL,
    data            TEXT            NOT NULL,
    creation        TIMESTAMP       NOT NULL,
    last_modified   TIMESTAMP       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)   REFERENCES user(id),
    FOREIGN KEY (folder_id) REFERENCES folder(id)
);
