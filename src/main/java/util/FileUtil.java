package util;
import demo.springboot.service.impl.ParkServiceImpl;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

public class FileUtil {
    public static boolean deleteFile(String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        return file.delete();
    }

    public static String getFile(String path, int n) {
        return getFile(path, new RefCount(n));
    }

    private static String getFile(String path, RefCount n) {
        File dir = new File(path);
        if (dir.isDirectory() && !dir.getName().equals(".") && !dir.getName().equals("..")) {
            for (String child : dir.list()) {
                String result = getFile(path + File.separator + child, n);
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

    public static int getCount(String baseDir) {
        File dir = new File(baseDir);
        int n = 0;
        if (dir.isDirectory()) {
            for (String fname : dir.list()) {
                if (!fname.equals(".") && !fname.equals("..")) {
                    n += getCount(baseDir + File.separator + fname);
                }
            }
        } else {
            n++;
        }
        return  n;
    }

    public static String getImageBase64(String path) throws IOException {
        BufferedInputStream inputStream = null;
        if (path == null) {
            throw new NullPointerException();
        }
        try {
            inputStream = new BufferedInputStream(new FileInputStream(path));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte buffer [] = new byte[100];
            int len = 0;
            while ((len = inputStream.read(buffer, 0, 100)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return "data:image/jpg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }  finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static void moveFile(String src, String destFile) throws IOException{
        Files.move(Paths.get(src), Paths.get(destFile), StandardCopyOption.REPLACE_EXISTING);
    }

    public static String getFileSort(String path, int n) {
        return getFilePathSort(path, new RefCount(n));
    }

    private static String getFilePathSort(String path, RefCount n) {
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
                String result = getFilePathSort(path + File.separator + child, n);
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
