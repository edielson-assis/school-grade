CREATE TABLE courses (
    course_id UUID,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(255) NOT NULL,
    image_url VARCHAR(255),
    creation_date TIMESTAMP NOT NULL,
    last_update_date TIMESTAMP NOT NULL,
    course_status VARCHAR(20) NOT NULL,
    course_level VARCHAR(20) NOT NULL,
    user_instructor UUID NOT NULL,

    CONSTRAINT pk_courses PRIMARY KEY(course_id)
);

CREATE TABLE modules (
    module_id UUID,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    course_id UUID NOT NULL,

    CONSTRAINT pk_modules PRIMARY KEY(module_id),
    CONSTRAINT fk_modules_course FOREIGN KEY(course_id) REFERENCES courses(course_id)
);

CREATE TABLE lessons (
    lesson_id UUID,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(255) NOT NULL,
    video_url VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    module_id UUID NOT NULL,

    CONSTRAINT pk_lessons PRIMARY KEY(lesson_id),
    CONSTRAINT fk_lessons_module FOREIGN KEY(module_id) REFERENCES modules(module_id)
);