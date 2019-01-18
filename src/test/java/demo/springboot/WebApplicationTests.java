package demo.springboot;

import demo.springboot.domain.ParkResult;
import demo.springboot.service.ParkService;
import demo.springboot.service.UserService;
import demo.springboot.service.impl.ParkCheckImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import util.RectFind;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {
	private Log logger = LogFactory.getLog(WebApplicationTests.class);
	@Autowired
	private ParkCheckImpl parkCheck;

	@Autowired
	private ParkService parkService;

	@Autowired
    private UserService userService;

	@Test
	public void contextLoads() {
		try {
			InputStream in = new FileInputStream(new File("d:\\1.jpg"));
			byte buffer [] = new byte [100];
			int n = 0;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			while ((n = in.read(buffer, 0, 100)) != -1) {
				outputStream.write(buffer, 0, n);
			}
			ParkResult parkResult = parkCheck.getParkResult(outputStream.toByteArray());
			parkResult.getParkType();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public  void findLine() {
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(new File("D:\\TSBrowserDownloads\\6.png")));
			BufferedImage bufferedImage = RectFind.getRectImage(image);
			if (bufferedImage != null) {
				ImageIO.write(bufferedImage, "jpg", new File("d:\\b.jpg"));
			}
			//List<Point>  rect = RectFind.getRect(image);
			//rect.size();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public  void testConfig() {
		try {
			InputStream inputStream = new FileInputStream(new File("D:\\TSBrowserDownloads\\001.png"));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte [] buffer = new byte[100];
			int len = 0;
			while ((len = inputStream.read(buffer, 0, 100)) != -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}

			ParkResult result = parkCheck.getParkResult(byteArrayOutputStream.toByteArray());
			result.getParkType();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testThread() {
		ExecutorService service = Executors.newFixedThreadPool(1);
			service.execute(new Runnable() {
				@Override
				public void run() {
					int a = 5 / 0;
					System.out.println(a);
					try {
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});


			service.execute(new Runnable() {
				@Override
				public void run() {
					int a = 5 / 0;
					System.out.println(a);
					try {
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

			System.out.println("haha");
		try {
			Thread.sleep(1000* 20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testShiro() {
	    boolean  f = userService.checkPassword("panghao", "1223");
	    System.out.println(f);
	    userService.getUserRoles("panghao");
    }
}
