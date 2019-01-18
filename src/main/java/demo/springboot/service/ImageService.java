package demo.springboot.service;

import java.io.IOException;

public interface ImageService {
    String cutByRed(byte [] data) throws IOException;
}
