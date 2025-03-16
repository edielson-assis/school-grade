package br.com.edielsonassis.authuser.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.edielsonassis.authuser.models.enums.UserStatus;
import br.com.edielsonassis.authuser.models.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "userId")
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModel implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, unique = true, length = 50, name = "user_name")
    private String userNameCustom;

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

    @Column(nullable = false, name = "is_account_non_expired")
    private boolean accountNonExpired = true;

    @Column(nullable = false, name = "is_account_non_locked")
    private boolean accountNonLocked = true;

    @Column(nullable = false, name = "is_credentials_non_expired")
    private boolean credentialsNonExpired = true;

    @Column(nullable = false, name = "is_enabled")
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn (name = "user_id")}, inverseJoinColumns = {@JoinColumn (name = "role_id")})
    private final List<RoleModel> permissions = new ArrayList<>();

    public List<String> getRoles() {
		return permissions.stream().map(RoleModel::getRoleName).collect(Collectors.toList());
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}