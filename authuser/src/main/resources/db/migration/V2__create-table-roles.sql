ALTER TABLE users 
ADD COLUMN is_account_non_expired BOOLEAN NOT NULL,
ADD COLUMN is_account_non_locked BOOLEAN NOT NULL,
ADD COLUMN is_credentials_non_expired BOOLEAN NOT NULL,
ADD COLUMN is_enabled BOOLEAN NOT NULL;

CREATE TABLE roles(
    role_id UUID,
    role_name VARCHAR(30) NOT NULL,
    
    CONSTRAINT pk_role PRIMARY KEY(role_id)
);

CREATE TABLE user_role(
    user_id UUID,
    role_id UUID,

    CONSTRAINT pk_user_role PRIMARY KEY(user_id, role_id),
    CONSTRAINT fk_user_role FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT fk_role_user FOREIGN KEY(role_id) REFERENCES roles(role_id)
);