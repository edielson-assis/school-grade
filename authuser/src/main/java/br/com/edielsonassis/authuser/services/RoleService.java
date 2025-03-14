package br.com.edielsonassis.authuser.services;

import br.com.edielsonassis.authuser.models.RoleModel;

public interface RoleService {
    
    RoleModel findbyRole(String name);
}