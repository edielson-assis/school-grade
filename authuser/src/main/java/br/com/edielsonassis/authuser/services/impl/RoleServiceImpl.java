package br.com.edielsonassis.authuser.services.impl;

import org.springframework.stereotype.Service;

import br.com.edielsonassis.authuser.models.RoleModel;
import br.com.edielsonassis.authuser.repositories.RoleRepository;
import br.com.edielsonassis.authuser.services.RoleService;
import br.com.edielsonassis.authuser.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    
    private final RoleRepository repository;

    public RoleModel findbyRole(String name) {
        log.info("Verifying for role: {}", name);
        return repository.findByRoleName(name).orElseThrow(() -> {
            log.error("Role not found: {}", name);
            return new ObjectNotFoundException("Role not found");
        });
    }
}