CREATE TABLE IF NOT EXISTS sb_item(
    id              VARCHAR(36)     NOT NULL,
    user_id         VARCHAR(36)     NOT NULL,
    type            TINYINT         NOT NULL,
    data            TEXT            NOT NULL,
    creation        TIMESTAMP       NOT NULL,
    last_modified   TIMESTAMP       NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT fk_item_user FOREIGN KEY (user_id) REFERENCES sb_user(id)
);

CREATE INDEX ix_item_user ON sb_item (user_id);
