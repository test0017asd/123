package com.example.dh.service;

import com.example.dh.repository.Users;
import com.example.dh.repository.UsersRepository;
import com.example.dh.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UsersRepository ur;

    @Autowired
    PasswordEncoder encode;

    public void join(UserVo uVo) {
        Users user = new Users(uVo.getId(), uVo.getPw(), uVo.getName());
        user.hashPw(encode);
        ur.save(user);
    }

    public Users idChk(String id) {
        return ur.findByUserId(id);
    }

    public Users loginOk(UserVo uVo) {

        Users user = ur.findByUserId(uVo.getId());

        if (user == null || !encode.matches(uVo.getPw(), user.getPw())) {
            return null;
        }

        return user;
    }
}
