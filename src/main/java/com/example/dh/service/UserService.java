package com.example.dh.service;

import com.example.dh.repository.*;
import com.example.dh.vo.UserVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UsersRepository ur;

    @Autowired
    BooksRepository br;

    @Autowired
    SearchHistoryRepository sr;

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

        if (!user.getPw().equals(uVo.getPw())) {
            return null;
        }

        return user;
    }

    @Transactional
    public Map<String, Object> search(String search, int page, Users authUser) throws IOException {

        List<Books> lst = new ArrayList<>();
        Map<String, Object> bMap = new HashMap<>();

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
        String ans2 = element.getAsJsonObject().get("documents").toString();
        JsonArray ans = (JsonArray) JsonParser.parseString(ans2);

        for (int i = 0; i < ans.size(); i++) {
            JsonObject obj = (JsonObject) ans.get(i);
            String title = obj.getAsJsonObject().get("title").toString();
            if (br.findByTitle(title) == null) {
                String authors = obj.getAsJsonObject().get("authors").toString();
                String contents = obj.getAsJsonObject().get("contents").toString().length() > 255
                        ? obj.getAsJsonObject().get("contents").toString().substring(0, 255)
                        : obj.getAsJsonObject().get("contents").toString();
                String datetime = obj.getAsJsonObject().get("datetime").toString();
                String isbn = obj.getAsJsonObject().get("isbn").toString();
                String price = obj.getAsJsonObject().get("price").toString();
                String publisher = obj.getAsJsonObject().get("publisher").toString();
                String salePrice = obj.getAsJsonObject().get("sale_price").toString();
                String thumbnail = obj.getAsJsonObject().get("thumbnail").toString();
                thumbnail = thumbnail.substring(1);
                thumbnail = thumbnail.substring(0, thumbnail.length() - 1);
                lst.add(new Books(title, thumbnail, contents, isbn, authors, publisher, datetime, price, salePrice));
                br.save(lst.get(i));
            } else {
                lst.add(br.findByTitle(title));
            }
        }
        //page가 100이 넘어가면 kakao api request가 정상적으로 data를 주지 않는듯?
        int totalPage = Integer.parseInt(element.getAsJsonObject().get("meta").getAsJsonObject().get("total_count").getAsString()) > 1000
                ? 1000
                : Integer.parseInt(element.getAsJsonObject().get("meta").getAsJsonObject().get("total_count").getAsString());

        bMap.put("lst", lst);
        bMap.put("totalPage", (int) Math.ceil((double) totalPage / 10));
        bMap.put("search", search);
        bMap.put("page", page);
        SearchHistroy sh = new SearchHistroy(search, authUser.getNo());
        sr.save(sh);
        List<SearchHistroy> shList = sr.findAllByUserNo(authUser.getNo());
        List<String> sList = new ArrayList<>();
        for(int j=0;j<shList.size();j++) {
            sList.add(shList.get(j).getWord());
        }
        bMap.put("searchList", sList);



        return bMap;

    }

    @Transactional
    public Books searchDeep(String title) {
        Books books = br.findByTitle(title);
        books.updateHit(books.getHit());
        return books;
    }
}
