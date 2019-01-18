package demo.springboot.service;

import demo.springboot.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface UserService {
    Set<String> getUserRoles(String userName);
    boolean checkPassword(String userName, String password);
    List<Project> getUserProjects(String name);
}
