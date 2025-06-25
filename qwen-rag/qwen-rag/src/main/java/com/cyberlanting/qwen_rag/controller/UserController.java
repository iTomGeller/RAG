package com.cyberlanting.qwen_rag.controller;

import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.dto.UserDTO;
import com.cyberlanting.qwen_rag.pojo.vo.UserVO;
import com.cyberlanting.qwen_rag.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/update")
    public Result<UserVO> updateUser(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return Result.success();
    }
}
