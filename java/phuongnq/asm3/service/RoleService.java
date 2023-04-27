package phuongnq.asm3.service;

import phuongnq.asm3.entity.Role;

public interface RoleService {
    Role findById(int roleId);

    void setUserAuthorities(String username, String roleName);
}
