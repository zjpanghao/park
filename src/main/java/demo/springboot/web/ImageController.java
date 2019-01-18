package demo.springboot.web;

import com.google.gson.Gson;
import demo.springboot.entity.PictureUpload;
import demo.springboot.http.HttpUtil;
import demo.springboot.service.ImageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.RectFind;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Controller
public class ImageController {
    private final Log logger = LogFactory.getLog(ImageController.class);
    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/image/cut")
    @ResponseBody
    public String getCut(@RequestBody String body) {
        PictureUpload pictureUpload = new Gson().fromJson(body, PictureUpload.class);
        byte [] data = Base64.getDecoder().decode(pictureUpload.getImageBase64());
        try {
            String result =  imageService.cutByRed(data);
            return result;
        } catch (IOException e) {
            logger.error("剪切红框失败:" + e.getMessage());
        }
        return null;
    }
}
