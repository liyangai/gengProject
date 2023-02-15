package com.gengproject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gengproject.util.http.Base64Util;
import com.gengproject.util.http.FileUtil;
import com.gengproject.util.http.HttpUtil;
import okhttp3.*;

import com.gengproject.dao.TagDao;
import com.gengproject.domain.Tag;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@SpringBootTest
class GengprojectApplicationTests {


    @Autowired
    TagDao tagDac;

//    @Test
    void contextLoads() {
        Tag tag = new Tag() ;
        tag.setTagName("tag1");
        tagDac.insert(tag);
    }

//    @Test
    void testTess4J() throws TesseractException {
        ITesseract instance = new Tesseract();
        // 语言库位置
        instance.setDatapath("D:\\test\\IdeaProjects\\gengProject\\geng-backend\\src\\main\\resources\\javaResource\\tessdata-master");
        // 中英文库
        instance.setLanguage("eng+chi_sim");
        // 简体中文库
//        instance.setLanguage("chi_sim");
        // 待识别的图片路径
        File imageLocation = new File("D:\\test\\IdeaProjects\\gengProject\\geng-backend\\src\\main\\resources\\javaResource\\gengImages");
        for (File image : imageLocation.listFiles()) {
            System.out.println(image.getName() + "---" + instance.doOCR(image));
        }
    }




//    @Test
    void testBaiduApi() {
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        try {
            // 本地文件路径
            String filePath = "D:\\download\\图片23-01-29\\2022-07-05 093219.png";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.1df65347b34a0142ef47d1f59f88a568.2592000.1678884509.282335-30398225";

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(result, Map.class);
            List<Map<String,String>> list =  (List<Map<String,String>>)map.get("words_result");
            List<String>  resultList = new ArrayList<>();
            StringBuilder resultString = new StringBuilder();
            for(Map<String,String> item :list){
                resultString.append(item.get("words"));
                resultString.append("\n");
            }
            System.out.println(resultList);
            System.out.println(resultString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
    void getBaiduToken2() throws IOException, JSONException {
        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

        String getTokenUrl = "https://aip.baidubce.com/oauth/2.0/token";
        String API_KEY = "jUIb5gj0d2Mjiq5T0jijPz8S";
        String SECRET_KEY = "1n7K3vxQgp69OGzXgeioX2FTzrvb6MkD";
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String content = "grant_type=client_credentials&client_id=" + API_KEY  + "&client_secret=" + SECRET_KEY;
        RequestBody body = RequestBody.create(mediaType,content);
//        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
//                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String result = new JSONObject(response.body().string()).getString("access_token");
        System.out.println(result);
    }


}



















