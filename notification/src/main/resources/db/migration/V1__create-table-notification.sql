CREATE TABLE notifications (
    notification_id UUID,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    notification_status VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL,

    CONSTRAINT pk_notifications PRIMARY KEY (notification_id)
);