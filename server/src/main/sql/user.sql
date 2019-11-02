CREATE TABLE IF NOT EXISTS user(
    id              VARCHAR(36)     NOT NULL,
    name            VARCHAR(50)         NULL,
    email           VARCHAR(50)     NOT NULL,
    password        VARCHAR(99)     NOT NULL,
    creation        DATE            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_user_email ON user (email);
