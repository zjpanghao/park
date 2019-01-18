package demo.springboot.service;

import demo.springboot.entity.IdentifyResult;
import demo.springboot.entity.Project;

import java.io.IOException;
import java.util.List;

public interface ProjectService {
    List<String> getProjectCategory(int projectId);
    Project getProject(int projectId);
    IdentifyResult getIdentifyResult(int projectId, String data) throws IOException;
}
