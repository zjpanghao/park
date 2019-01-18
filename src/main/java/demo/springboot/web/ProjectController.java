package demo.springboot.web;

import com.google.gson.Gson;
import demo.springboot.domain.CorrectBack;
import demo.springboot.domain.ParkUpload;
import demo.springboot.entity.*;
import demo.springboot.http.HttpUtil;
import demo.springboot.service.ProjectService;
import demo.springboot.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import util.FileUtil;

import javax.imageio.ImageIO;
import javax.security.auth.Subject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Controller
public class ProjectController {
    private final Log logger = LogFactory.getLog(ProjectController.class);
    private final String PROJECT_LIST = "projectList";
    private final String PROJECT_OUTLINE = "projectOutline";
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @RequestMapping("/project/retrieve")
    public String viewProject(ModelMap modelMap) {
        org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
        modelMap.addAttribute("projectList",userService.getUserProjects(subject.getPrincipal().toString()));
        return PROJECT_LIST;
    }

    @RequestMapping("/project/upload/{projectId}")
    public String uploadProject(@PathVariable Integer projectId, ModelMap modelMap) {
        List<String> categories = new ArrayList(projectService.getProjectCategory(projectId));
        modelMap.addAttribute("categoryList", categories);
        modelMap.addAttribute("projectId", projectId);
        return "projectUpload";
    }

    @RequestMapping("/project/newData/{projectId}")
    public String viewProjectnewData(@PathVariable Integer projectId, ModelMap modelMap) {
        List<String> categories = new ArrayList(projectService.getProjectCategory(projectId));
        modelMap.addAttribute("categoryList", categories);
        modelMap.addAttribute("projectId", projectId);
        return "projectNewData";
    }

    @RequestMapping("/project/newData/retrieveFile")
    @ResponseBody
    public List<PictureItem> viewProjectnewDataFile(@RequestBody String body) {
        PictureView pictureView = new Gson().fromJson(body, PictureView.class);
        Project project = projectService.getProject(pictureView.getProjectId());
        List<PictureItem> pictureItems = new ArrayList<>();
        for (int i = 0; i < pictureView.getSize(); i++) {
            PictureItem pictureItem = new PictureItem();
            int fileIndex = (pictureView.getPage() - 1) * pictureView.getSize() + i;
            pictureItem.setIndex(fileIndex);
            pictureItem.setPath(
                    FileUtil.getFileSort(project.getDataFullDir()
                            + File.separator + pictureView.getCategory(), fileIndex));
            try {
                if (pictureItem.getPath() != null) {
                    pictureItem.setImageBase64(FileUtil.getImageBase64(pictureItem.getPath()));
                }
            } catch (IOException e) {
            }
            pictureItems.add(pictureItem);
        }
        return pictureItems;
    }

    @RequestMapping("/project/deleteData")
    @ResponseBody
    public boolean deleteProjectnewDataFile(@RequestBody String body) {
        String filePath = body;
        boolean f = FileUtil.deleteFile(filePath);
        return f;
    }

    @RequestMapping("/project/randRetrieveFile/{projectId}")
    @ResponseBody
    public PictureItem getProjectFile(@PathVariable int projectId) {
        Project project = projectService.getProject(projectId);
        int n = (int)(Math.random() * FileUtil.getCount(project.getFilterFullDir()));
        while (n >= 0) {
            String index = FileUtil.getFile(project.getFilterFullDir(), n);
            if (index != null) {
                PictureItem pictureItem = new PictureItem();
                String [] items = index.split(File.separator.equals("\\") ? "\\\\" : File.separator);
                pictureItem.setCategory(items[items.length - 2]);
                String [] scoreItems = index.split("_");
                if (scoreItems.length < 3) {
                    return null;
                }
                String score = scoreItems[scoreItems.length - 2];
                try {
                    pictureItem.setScore(Double.parseDouble(score));
                } catch (NumberFormatException e) {
                    pictureItem.setScore(-1);
                }
                pictureItem.setPath(Base64.getEncoder().encodeToString(index.getBytes()));
                return pictureItem;
            } else if (n == 0) {
                return null;
            }
            n /= 2;
        }
        return null;
    }


    @RequestMapping("/project/uploadData")
    @ResponseBody
    public boolean uploadProjectData(@RequestBody String body, ModelMap modelMap) {
        PictureUpload pictureUpload = new Gson().fromJson(body, PictureUpload.class);
        Project project = projectService.getProject(pictureUpload.getProjectId());
        byte [] bytes = Base64.getDecoder().decode(pictureUpload.getImageBase64());
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            String destDir = project.getDataFullDir() + File.separator + pictureUpload.getCategory();
            String randName =
                    destDir + File.separator + "upload" + System.currentTimeMillis() + "_" + Math.random() * 10000 + ".jpg";
            ImageIO.write(image, "jpg", new File(randName));
            return true;
        } catch (IOException e) {
            logger.error("上传失败:" + e.getMessage());
        }
        return false;
    }

    @RequestMapping("/project/identify")
    @ResponseBody
    public PictureItem identifyProject(@RequestBody String body) {
        PictureUpload pictureUpload = new Gson().fromJson(body, PictureUpload.class);
        Project project = projectService.getProject(pictureUpload.getProjectId());
        try {
            IdentifyResult result = projectService.getIdentifyResult(project.getId(), pictureUpload.getImageBase64());
            if (result.getErrorCode() == 0 && result.getIdentifyItemList().size() > 0) {
                PictureItem pictureItem = new PictureItem();
                pictureItem.setScore(result.getIdentifyItemList().get(0).getScore());
                pictureItem.setCategory(result.getIdentifyItemList().get(0).getName());
                return pictureItem;
            }
        } catch (IOException e) {
            logger.error("识别错误 project:" + project.getName() + " error:" + e.getMessage());
        }
        return null;
    }


    @RequestMapping("/project/check/{projectId}")
    public String checkProject(@PathVariable int projectId, ModelMap modelMap) {
//        org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
        modelMap.addAttribute("projectId",projectId);
        return "projectCheck";
    }

    @RequestMapping("/project/filter/count/{projectId}")
    @ResponseBody
    public int getFilterTotal(@PathVariable int projectId) {
        Project project = projectService.getProject(projectId);
        return FileUtil.getCount(project.getFilterFullDir());
    }

    @RequestMapping("/project/outline")
    public String viewProjectOutline(ModelMap modelMap) {
//        org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
//        modelMap.addAttribute("projectList",userService.getUserProjects(subject.getPrincipal().toString()));
        return PROJECT_OUTLINE;
    }


    @RequestMapping(value = "/project/category/{projectId}_{category}")
    public String getProjectCategory(@PathVariable Integer projectId, @PathVariable String category, ModelMap modelMap) {
        List<String> categoryList = projectService.getProjectCategory(projectId);
        if (categoryList != null) {
            List<String> categories = new ArrayList(categoryList);
            categories.removeIf(s -> s.equals(category));
            modelMap.addAttribute("categoryList", categories);
        } else {
            modelMap.addAttribute("categoryList", null);
            logger.error("找不到分类");
        }
        return "category";
    }


    @RequestMapping("/project/image")
    @ResponseBody
    public String getProjectImage(@RequestParam String path) {
        String decodePath = new String(Base64.getDecoder().decode(path));
        try {
            String image64 = FileUtil.getImageBase64(decodePath);
            return image64;
        } catch (IOException e) {
            logger.error("获取图像失败:" + e.getMessage());
        }
        return null;
    }

    @RequestMapping("/project/correctAi")
    @ResponseBody
    public boolean correctAi(@RequestBody String body,  ModelMap modelMap) {
        Gson gson = new Gson();
        CorrectAiRequest correctAiRequest = gson.fromJson(body, CorrectAiRequest.class);
        Project project = projectService.getProject(correctAiRequest.getProjectId());
        String path = new String(Base64.getDecoder().decode(correctAiRequest.getPath()));
        int inx = path.lastIndexOf(File.separator);
        if (inx == -1) {
            return false;
        }

        if (correctAiRequest.getCategory().length() == 0) {
            boolean f = FileUtil.deleteFile(path);
            return f;
        }
        String desFile = project.getDataFullDir() + File.separator + correctAiRequest.getCategory() + File.separator + path.substring(inx + 1);
        try {
            FileUtil.moveFile(path, desFile);
            return true;
        } catch (IOException e) {
            logger.error("移动文件失败:" + e.getMessage());
        }
        return false;
    }
}
