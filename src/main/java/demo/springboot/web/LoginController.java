package demo.springboot.web;

import demo.springboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.Subject;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private final String LOGIN = "login";
    private final String INDEX = "index";
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/login")
    public String login() {
       return LOGIN;
    }

    @RequestMapping(value = "/loginUser/{userName}-{password}")
    @ResponseBody
    public String loginUser(@PathVariable String userName, @PathVariable String password) {
        System.out.println(userName + password);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
        try {
            System.out.println("获取到信息，开始验证！！");
            subject.login(token);//登陆成功的话，放到session中
           // subject.
            return "true";
        } catch (Exception e) {
            return "用户名或者密码错误";
        }
    }
}
