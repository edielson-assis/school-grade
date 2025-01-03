package br.com.edielsonassis.authuser.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.edielsonassis.authuser.models.enums.UserStatus;
import br.com.edielsonassis.authuser.models.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "userId")
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, unique = true, length = 50, name = "user_name")
    private String userName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 150, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false, name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(length = 20, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 15)
    private String cpf;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(nullable = false, name = "creation_date")
    private LocalDateTime creationDate;

    @Column(nullable = false, name = "last_update_date")
    private LocalDateTime lastUpdateDate;
}