package demo.springboot.service.impl;

import demo.springboot.service.ImageService;
import org.springframework.stereotype.Service;
import util.RectFind;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public String cutByRed(byte [] data) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(data);
        try {
            BufferedImage image = ImageIO.read(inputStream);
            BufferedImage newImage = RectFind.getRectImage(image);
            if (newImage == null) {
                return null;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(newImage, "jpg", byteArrayOutputStream);
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } finally {
            inputStream.close();
        }
    }
}
