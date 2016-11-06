package com.taotao.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: 牛神成--Tinker
 * @Company:
 * @CreateDate: 16/11/4
 */
@Controller
public class UserController {

    @RequestMapping("/user")
    @ResponseBody
    public int user() {
        return 10000;
    }
}
