package demo.springboot.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import demo.springboot.domain.ParkJsonResult;
import demo.springboot.domain.ParkResult;
import demo.springboot.domain.ParkType;
import demo.springboot.service.ParkCheck;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
@Component
public class ParkCheckImpl implements ParkCheck{
    private final String PARK_SERVER = "http://180.164.59.226:8887/debang_park_image";
    public ParkResult getParkResult(byte [] data)  {
        DataOutputStream  out = null;
        DataInputStream in = null;
        try {

            URL url = new URL(PARK_SERVER);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("image", Base64.getEncoder().encodeToString(data));
            out.writeBytes(jsonObject.toString());
            out.flush();
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                in = new DataInputStream(connection.getInputStream());
                byte [] buffer = new byte[100];
                int n = 0;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while ((n =in.read(buffer, 0, 100)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, n);
                }
                ParkResult parkResult = new ParkResult();
                parkResult.setParkType(ParkType.UNKNOWN);
                ParkJsonResult parkJsonResult = new Gson().fromJson(byteArrayOutputStream.toString(), ParkJsonResult.class);
                if (parkJsonResult.getErrorCode() != null && parkJsonResult.getErrorCode() != 0) {

                    return parkResult;
                }

                if (parkJsonResult.getResults() != null && parkJsonResult.getResults().size() > 0) {
                    ParkJsonResult.ParkResultItem parkResultItem = parkJsonResult.getResults().get(0);
                    parkResult.setParkType(parkResultItem.getName().equals("park") ? ParkType.PARK : ParkType.IDLE);
                    parkResult.setScore(parkResultItem.getScore());
                    return parkResult;
                }
                return parkResult;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
