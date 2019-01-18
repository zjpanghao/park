package demo.springboot.service.impl;

import com.google.gson.Gson;
import demo.springboot.domain.ParkType;
import demo.springboot.entity.ParkConf;
import demo.springboot.repository.ParkConfRepository;
import demo.springboot.service.ParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ParkServiceImpl implements ParkService{
    @Autowired
    private ParkConfRepository parkConfRepository;

    class RefCount {
        private int count;

        public RefCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public String getFilePath(String baseDir, int n) {
        return getFilePath(baseDir, new RefCount(n));
    }

    @Override
    public String getFilePathSort(String baseDir, int n) {
        return getFilePathSort(baseDir, new RefCount(n));
    }


    @Override
    public boolean moveFile(String destFile, String src) {
        try {
            Files.move(Paths.get(src), Paths.get(destFile), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean setFileThresh(int value) {
        ParkConf parkConf = parkConfRepository.findById(1L).get();
        parkConf.setStoreRate(value);
        parkConfRepository.save(parkConf);
        return true;
    }

    @Override
    public boolean addFileThresh(int value) {
        ParkConf parkConf = parkConfRepository.findById(1L).get();
        parkConf.setStoreRate(value + parkConf.getStoreRate());
        parkConfRepository.save(parkConf);
        return true;
    }

    @Override
    public int getFileThresh() {
        ParkConf parkConf = parkConfRepository.findById(1L).get();
        return parkConf.getStoreRate();
    }

    private String getFilePath(String path, RefCount n) {
        File dir = new File(path);
        if (dir.isDirectory() && !dir.getName().equals(".") && !dir.getName().equals("..")) {
            for (String child : dir.list()) {
                String result = getFilePath(path + File.separator + child, n);
                if (null != result) {
                    return result;
                }
            }
        } else if (dir.isFile()) {
            //System.out.println(path +"  " + n.getCount());
            n.setCount(n.getCount() - 1);
            if (n.getCount() < 0 ) {
                return path;
            }
        }
        return  null;
    }

    private String getFilePathSort(String path, RefCount n) {
        File dir = new File(path);
        if (dir.isDirectory() && !dir.getName().equals(".") && !dir.getName().equals("..")) {
            List<String> childs = Arrays.asList(dir.list());
            childs.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            for (String child : childs) {
                String result = getFilePath(path + File.separator + child, n);
                if (null != result) {
                    return result;
                }
            }
        } else if (dir.isFile()) {
            //System.out.println(path +"  " + n.getCount());
            n.setCount(n.getCount() - 1);
            if (n.getCount() < 0 ) {
                return path;
            }
        }
        return  null;
    }

}
