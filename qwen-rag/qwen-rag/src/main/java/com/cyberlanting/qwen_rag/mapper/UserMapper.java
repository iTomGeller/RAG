package com.cyberlanting.qwen_rag.mapper;

import com.cyberlanting.qwen_rag.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 新增用户数据
     * @param user 用户数据
     */
    void save(User user);

    /**
     * 动态条件查找用户
     * @param user 仅包含特定条件属性值的对象
     */
    List<User> findUser(User user);

    /**
     * 根据id查找条件
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * 更新用户信息
     */
    void update(User user);


}
