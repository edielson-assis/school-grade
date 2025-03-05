package br.com.edielsonassis.course.template;

import br.com.edielsonassis.course.dtos.request.NotificationCommandRequest;
import br.com.edielsonassis.course.models.CourseModel;
import br.com.edielsonassis.course.models.UserModel;

public class NotificationTemplate {
    
    private NotificationTemplate() {}

    public static NotificationCommandRequest buildMessage(CourseModel course, UserModel user) {
        var notificationCommandRequest = new NotificationCommandRequest();
        notificationCommandRequest.setTitle("Bem-Vindo(a) ao Curso: " + course.getName());
        notificationCommandRequest.setMessage(user.getFullName() + ", a sua inscrição foi realizada com sucesso!");
        notificationCommandRequest.setUserId(user.getUserId());
        return notificationCommandRequest;
    }
}