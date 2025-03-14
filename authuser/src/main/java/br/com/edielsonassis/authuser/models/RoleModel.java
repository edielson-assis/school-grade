package br.com.edielsonassis.authuser.models;

import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import br.com.edielsonassis.authuser.models.enums.RoleType;
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

@EqualsAndHashCode(of = "roleId")
@Setter
@Getter
@Entity
@Table(name = "roles")
public class RoleModel implements GrantedAuthority {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private UUID roleId;

    @Enumerated(EnumType.STRING)
	@Column(nullable =  false, unique = true, name = "role_name", length = 30)
    private RoleType roleName;

    @Override
    public String getAuthority() {
        return this.roleName.toString();
    }
}