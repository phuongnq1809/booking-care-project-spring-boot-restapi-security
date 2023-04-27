package phuongnq.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phuongnq.asm3.dao.AuthorityRepository;
import phuongnq.asm3.dao.RoleRepository;
import phuongnq.asm3.entity.Authority;
import phuongnq.asm3.entity.Role;
import phuongnq.asm3.exception.EntityNotFoundException;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository theRoleRepository, AuthorityRepository theAuthorityRepository) {
        roleRepository = theRoleRepository;
        authorityRepository = theAuthorityRepository;
    }

    @Override
    public Role findById(int roleId) {
        Optional<Role> result = roleRepository.findById(roleId);

        Role theRole = null;

        if (result.isPresent()) {
            theRole = result.get();
        } else {
            // we didn't find the role
            throw new EntityNotFoundException("Did not find role id - " + roleId);
        }

        return theRole;
    }

    @Override
    public void setUserAuthorities(String username, String roleName) {
        String theAuthorityName = "ROLE_" + roleName;
        Authority authority = new Authority(username, theAuthorityName);
        authorityRepository.save(authority);
    }
}
