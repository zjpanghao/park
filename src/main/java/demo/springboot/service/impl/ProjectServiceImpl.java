package demo.springboot.service.impl;

import com.google.gson.Gson;
import demo.springboot.entity.IdentifyResult;
import demo.springboot.entity.Project;
import demo.springboot.http.HttpUtil;
import demo.springboot.repository.ProjectRepository;
import demo.springboot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<String> getProjectCategory(int projectId) {
        Project project = projectRepository.findById(projectId).get();
        String path = project.getDataFullDir();
        File f = new File(path);
        return Arrays.asList(f.list());
    }

    @Override
    public Project getProject(int projectId) {
        Project project = projectRepository.findById(projectId).get();
        return project;
    }

    @Override
    public IdentifyResult getIdentifyResult(int projectId, String data) throws IOException{
        Project project = getProject(projectId);
        Map<String, String> params = new HashMap<>();
        params.put("image", data);
        String result = HttpUtil.post(project.getServIp(), params);
        IdentifyResult identifyResult = new Gson().fromJson(result, IdentifyResult.class);
        return identifyResult;
    }
}
