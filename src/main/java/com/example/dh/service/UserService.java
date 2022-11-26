package com.example.dh.service;

import com.example.dh.repository.Books;
import com.example.dh.repository.Users;
import com.example.dh.repository.UsersRepository;
import com.example.dh.vo.UserVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class UserService {

    @Autowired
    UsersRepository ur;

    public void join(UserVo uVo) {
        Users user = new Users(uVo.getId(), uVo.getPw(), uVo.getName());
        ur.save(user);
    }

    public Users idChk(String id) {
        return ur.findByUserId(id);
    }

    public Users loginOk(UserVo uVo) {

        Users user = ur.findByUserId(uVo.getId());

        if (user == null) {
            return null;
        }

        if(!user.getPw().equals(uVo.getPw())) {
            return null;
        }

        return user;
    }

    public List<Books> search(String search) throws IOException {
        int page = 1;
        List<Books> lst = new ArrayList<>();

        URL url = new URL("https://dapi.kakao.com/v3/search/book?target=title");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        httpConn.setRequestProperty("Authorization", "KakaoAK 2b24f06df2137983cc98995c1ddce575");
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("query=" + search + "&page=" + page);
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        JsonElement element = JsonParser.parseString(response);

        while(!element.getAsJsonObject().get("meta").getAsJsonObject().get("is_end").getAsBoolean()) {
            URL url2 = new URL("https://dapi.kakao.com/v3/search/book?target=title");
            HttpURLConnection httpConn2 = (HttpURLConnection) url.openConnection();
            httpConn2.setRequestMethod("GET");

            httpConn2.setRequestProperty("Authorization", "KakaoAK 2b24f06df2137983cc98995c1ddce575");
            httpConn2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            httpConn2.setDoOutput(true);
            OutputStreamWriter writer2 = new OutputStreamWriter(httpConn2.getOutputStream());
            writer2.write("query=" + search + "&page=" + page);
            writer2.flush();
            writer2.close();
            httpConn2.getOutputStream().close();

            InputStream responseStream2 = httpConn2.getResponseCode() / 100 == 2
                    ? httpConn2.getInputStream()
                    : httpConn2.getErrorStream();
            Scanner s2 = new Scanner(responseStream2).useDelimiter("\\A");
            String response2 = s2.hasNext() ? s2.next() : "";
            JsonElement element2 = JsonParser.parseString(response2);
            try {
                String ans2 = element2.getAsJsonObject().get("documents").toString();
                JsonArray ans = (JsonArray) JsonParser.parseString(ans2);
                for (int i = 0; i < ans.size(); i++) {
                    JsonObject obj = (JsonObject) ans.get(i);
                    String authors = obj.getAsJsonObject().get("authors").toString();
                    if (authors.equals("[]")) {
                        continue;
                    }
                    String contents = obj.getAsJsonObject().get("contents").toString();
                    String datetime = obj.getAsJsonObject().get("datetime").toString();
                    String isbn = obj.getAsJsonObject().get("isbn").toString();
                    String price = obj.getAsJsonObject().get("price").toString();
                    String publisher = obj.getAsJsonObject().get("publisher").toString();
                    String salePrice = obj.getAsJsonObject().get("sale_price").toString();
                    String thumbnail = obj.getAsJsonObject().get("thumbnail").toString();
                    thumbnail = thumbnail.substring(1);
                    thumbnail = thumbnail.substring(0, thumbnail.length() - 1);
                    String title = obj.getAsJsonObject().get("title").toString();
                    Long no;
                    if(page == 1) {
                        no = new Long(i+1);
                    } else {
                        no = new Long((i+1)+((page-1)*10));
                    }
                    lst.add(new Books(no, title, thumbnail, contents, isbn, authors, publisher, datetime, price, salePrice));
                }
                if (element.getAsJsonObject().get("meta").getAsJsonObject().get("is_end").getAsBoolean() == false) {
                    page = page + 1;
                }
            } catch (NullPointerException e) {
                break;
            }

        }


        return lst;
    }


}
