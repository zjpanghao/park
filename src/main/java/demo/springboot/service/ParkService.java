package demo.springboot.service;

import demo.springboot.domain.ParkType;
import demo.springboot.service.impl.ParkServiceImpl;
import org.springframework.stereotype.Controller;


public interface ParkService {
    String getFilePath(String baseDir, int n);
    String getFilePathSort(String baseDir, int n);
    boolean moveFile(String destFile, String src);
    boolean setFileThresh(int value);
    boolean addFileThresh(int value);
    int getFileThresh();
}
