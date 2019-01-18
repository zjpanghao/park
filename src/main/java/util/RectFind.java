package util;

import demo.springboot.domain.Point;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RectFind {
    static private final int LINE_WIDTH = 300;
    static private final int CHANGE_THRESH = 10;
    static int getBlue(int pix) {
        return pix & 0xff;
    }

    static int getGreen(int pix) {
        return (pix & 0xff00) >> 8;
    }

    static int getRed(int pix) {
        return (pix & 0xff0000) >> 16;
    }
    static private boolean isPixRed(int pix) {
        int r = getRed(pix);
        int g = getGreen(pix);
        int b = getBlue(pix);
        if (getGreen(pix) == getRed(pix) && getGreen(pix) == getBlue(pix)) {
            return false;
        }
        if (getRed(pix) < getBlue(pix) || getRed(pix) < getGreen(pix)) {
            return false;
        }

        double min = g < b ? g : b;
        double s = (r - min) / r;
        if (r < 200 || s < 0.8) {
            return false;
        }
        double value = 60 *((g - b) / (r - min));
        if (g < b) {
            value += 360;
        }
        return (value < 10) || (value > 350);
    }

    static private boolean isPixRed(int pix, int up, int down, int left, int right) {
        int r = getRed(pix);
        int g = getGreen(pix);
        int b = getBlue(pix);

        int validPoint = 1;
        if (up != -1) {
            r += getRed(up);
            g += getGreen(up);
            b += getBlue(up);
            validPoint++;
        }
        if (down != -1) {
            r += getRed(down);
            g += getGreen(down);
            b += getBlue(down);
            validPoint++;
        }

        if (left != -1) {
            r += getRed(left);
            g += getGreen(left);
            b += getBlue(left);
            validPoint++;
        }
        if (right != -1) {
            r += getRed(right);
            g += getGreen(right);
            b += getBlue(right);
            validPoint++;
        }
        r /= validPoint;
        g /= validPoint;
        b /= validPoint;

        if (getGreen(pix) == getRed(pix) && getGreen(pix) == getBlue(pix)) {
            return false;
        }
        if (getRed(pix) < getBlue(pix) || getRed(pix) < getGreen(pix)) {
            return false;
        }

        double min = g < b ? g : b;
        double s = (r - min) / r;
        if (r < 200 || s < 0.8) {
            return false;
        }
        double value = 60 *((g - b) / (r - min));
        if (g < b) {
            value += 360;
        }
        return (value < 10) || (value > 350);
    }


    static private boolean nearPixRed(int pix, int oldPix) {
        return (Math.pow(getBlue(pix) - getBlue(oldPix), 2) + Math.pow(getGreen(pix) - getGreen(oldPix), 2) + Math.pow(getRed(pix) - getRed(oldPix), 2))
                < 75;
    }

    public static BufferedImage getRectImage(BufferedImage image, int startx, int starty, int width, int height) {
        BufferedImage bufImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                bufImage.setRGB(i, j, image.getRGB(startx + i, starty + j));
            }
        }
        return bufImage;
    }

    public static BufferedImage getRectImage(BufferedImage image) {
        List<Point> result = getRect(image);
        if (result == null) {
            return null;
        }
        int width = result.get(1).getX() - result.get(0).getX();
        int height = result.get(1).getY() - result.get(0).getY();
        BufferedImage bufImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                bufImage.setRGB(i, j, image.getRGB(result.get(0).getX() + i, result.get(0).getY() + j));
            }
        }
        return bufImage;
    }

    public static List<Point> getRect(BufferedImage image) {
        List<Point> result = new ArrayList<>();
        int redCount = 0;
        int startx = -1;
        final int MAREN = 5;
        int change = 0;
        boolean findLine = false;
        boolean findUpLine = false;
        int len = 0;
        for (int j = 0; j < image.getHeight();) {
            findLine = false;
            change = 0;
            for (int i = 0; i < image.getWidth() && !findLine; i++) {
                if (result.size() > 0 && j > result.get(0).getY() + 100 && !findUpLine) {
                    len = 0;
                    findUpLine = true;
                }
                int up = j > 0 ? image.getRGB(i, j - 1) :  -1;
                int down = j < image.getHeight() - 1 ? image.getRGB(i, j + 1) :  -1;
                int left = i > 0 ? image.getRGB(i - 1, j) :  -1;;
                int right = i < image.getWidth() - 1 ? image.getRGB(i + 1, j) :  -1;
                if (isPixRed(image.getRGB(i, j), up, down, left, right)) {
                    if (startx == -1) {
                        startx = i;
                        change = 0;
                    }
                    redCount++;
                } else {
                    change++;
                    if (change > CHANGE_THRESH) {
                        if (redCount > LINE_WIDTH) {
                            if (!findUpLine) {
                                int tmp = i - startx;
                                if (tmp > len) {
                                    result.clear();
                                    len = tmp;
                                    Point point = new Point( startx + MAREN,  j + MAREN );
                                    result.add(point);
                                    findLine = true;
                                }
                            } else {
                                int tmp = i - startx;
                                if (tmp > len) {
                                    Point pt = result.get(0);
                                    result.clear();;
                                    result.add(pt);
                                    len = tmp;
                                    Point point = new Point( i -CHANGE_THRESH - MAREN,  j - MAREN);
                                    result.add(point);
                                    findLine = true;
                                }
                            }
                        }
                        redCount = 0;
                        startx = -1;
                        change = 0;
                    }
                }
                // last
                if (j == image.getWidth()) {
                    if (redCount > LINE_WIDTH) {
                        if (!findUpLine) {
                            int tmp = i - startx;
                            if (tmp > len) {
                                result.clear();
                                len = tmp;
                                Point point = new Point( startx + MAREN,  j + MAREN );
                                result.add(point);
                                findLine = true;
                            }
                        }
                    }
                }
            }
            j ++;
        }
        if (result.size() == 2) {
            return result;
        }
        return null;
    }
}
