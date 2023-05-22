/*
* User Service
*/
package phuongnq.asm3.service;

import phuongnq.asm3.entity.User;

public interface UserService {

    User save(User theUser);

    User findById(int userId);

    User findByEmail(String email);
}
