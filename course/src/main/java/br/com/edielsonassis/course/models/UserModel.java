package br.com.edielsonassis.course.models;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.edielsonassis.course.models.interfaces.Authentication;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
public class UserModel implements Authentication {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 150, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "user_status")
    private String userStatus;

    @Column(nullable = false, name = "user_type")
    private String userType;

    @Column(length = 20, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 15)
    private String cpf;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, name = "is_enabled")
    private boolean enabled = true;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private Set<CourseModel> courses;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}