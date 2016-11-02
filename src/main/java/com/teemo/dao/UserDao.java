package com.teemo.dao;


import com.teemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Anni on 2016/10/25.
 */

@Mapper
@Repository
public interface UserDao {

    public List<User> getUsers();

}
