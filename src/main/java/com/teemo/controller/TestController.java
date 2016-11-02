package com.teemo.controller;

import com.teemo.exception.MyException;
import com.teemo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Anni on 2016/10/25.
 */

@RestController
@RequestMapping(value = "/", method= RequestMethod.POST)
@Api(value = "测试" ,description = "测试desc")
public class TestController {

    @Autowired
    private UserService userService ;

    @ApiOperation(value = "测试")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Object test(){
        return userService.getUsers() ;
    }

    @ApiOperation(value = "异常测试")
    @RequestMapping(value = "/error", method = RequestMethod.POST)
    public String testError() throws MyException {
        throw new MyException("异常测试");
    }
}
