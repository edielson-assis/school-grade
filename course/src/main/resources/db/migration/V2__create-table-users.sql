CREATE TABLE users(
    user_id UUID NOT NULL,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    user_status VARCHAR(50) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    cpf VARCHAR(15) NOT NULL,
    img_url VARCHAR(255),

    CONSTRAINT pk_user PRIMARY KEY(user_id),
    CONSTRAINT uk_email UNIQUE(email),
    CONSTRAINT uk_cpf UNIQUE(cpf)
);

CREATE TABLE course_user(
    course_id UUID NOT NULL,
    user_id UUID NOT NULL,

    CONSTRAINT pk_course_user PRIMARY KEY(course_id, user_id),
    CONSTRAINT fk_course FOREIGN KEY(course_id) REFERENCES courses(course_id),
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(user_id)
);