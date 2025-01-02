CREATE TABLE users(
    user_id UUID,
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    user_status VARCHAR(50) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    cpf VARCHAR(15) NOT NULL,
    img_url VARCHAR(255),
    creation_date TIMESTAMP NOT NULL,
    last_update_date TIMESTAMP NOT NULL,

    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT uk_user_name UNIQUE (user_name),
    CONSTRAINT uk_email UNIQUE (email),
    CONSTRAINT uk_cpf UNIQUE (cpf)
);