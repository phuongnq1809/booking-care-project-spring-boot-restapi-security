package phuongnq.asm3.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import phuongnq.asm3.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
