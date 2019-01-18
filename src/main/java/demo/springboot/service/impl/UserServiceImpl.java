package demo.springboot.service.impl;

import demo.springboot.entity.Project;
import demo.springboot.entity.Role;
import demo.springboot.entity.User;
import demo.springboot.repository.UserRepository;
import demo.springboot.service.UserService;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public Set<String> getUserRoles(String userName) {
        User user = userRepository.findUserByUserName(userName);
        Set<String> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
           roles.add(role.getRole());
        }
        return roles;
    }

    @Override
    public boolean checkPassword(String userName, String password) {
        Long count = userRepository.countByUserNameAndPassword(userName, password);
        return count != null && count > 0;
    }

    @Override
    public List<Project> getUserProjects(String name) {
        User user = userRepository.findUserByUserName(name);
        List<Project> projects = new ArrayList<>();
        for (Project project : user.getProjects()) {
            projects.add(project);
        }
        return projects;
    }
}
