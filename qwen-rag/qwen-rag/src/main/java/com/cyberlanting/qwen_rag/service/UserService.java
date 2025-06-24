package com.cyberlanting.qwen_rag.service;


import com.cyberlanting.qwen_rag.pojo.dto.UserDTO;
import com.cyberlanting.qwen_rag.pojo.entity.User;

import java.util.List;

public interface UserService {
    void register(UserDTO userDTO);

    User login(UserDTO userDTO);

    User getUserById(Long id);

    void update(UserDTO userDTO);

    User getUserByEmail(String email);

}
