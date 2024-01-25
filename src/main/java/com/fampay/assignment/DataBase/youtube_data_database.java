package com.fampay.assignment.DataBase;

import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class youtube_data_database {
    public static final Logger logger = LoggerFactory.getLogger(searchData.class);
    private static Connection connection;

    private void EnableConnnection() {
        try (InputStream input = new FileInputStream("src/main/resources/application.yml")) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);

            Map<String, Object> spring = (Map<String, Object>) data.get("spring");
            Map<String, Object> datasource = (Map<String, Object>) spring.get("datasource");

            String url = (String) datasource.get("url");
            String username = (String) datasource.get("username");
            String password = (String) datasource.get("password");
            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            logger.error("connection failed ", e.getMessage());
        }
    }

    public synchronized void insert(String body) {
        if (connection == null) {
            EnableConnnection();
        }
        Map<String, JSONObject> videoDetails = parseTheBody(body);
        for (String videoId : videoDetails.keySet()) {
            try {


                String insertQuery = "Insert into youtube_data(video_id,title,description,published_on,thumbnails) values (?,?,?,?,?);";
                PreparedStatement statement = connection.prepareStatement(insertQuery);

                statement.setString(1, videoId);
                statement.setString(2, videoDetails.get(videoId).get("title").toString());
                statement.setString(3, videoDetails.get(videoId).get("description").toString());
                String time = videoDetails.get(videoId).get("publishedAt").toString();
                statement.setTimestamp(4, Timestamp.from(Instant.parse(time)));

                JSONObject thumbNails = new JSONObject();
                String Default = cleanUrl(videoDetails.get(videoId).get("defaultThumbnail").toString());
                String medium = cleanUrl(videoDetails.get(videoId).get("mediumThumbnail").toString());
                String high = cleanUrl(videoDetails.get(videoId).get("highThumbnail").toString());
                thumbNails.put("default", Default);
                thumbNails.put("medium", medium);
                thumbNails.put("high", high);
                String allThumbnails = thumbNails.toJSONString();
                statement.setObject(5, allThumbnails, java.sql.Types.OTHER);

                statement.execute();


            } catch (SQLException exception) {
                if (exception.getMessage().contains("duplicate")) {
                    logger.error("Duplicate Data Found");
                } else {
                    logger.error("Failed while inserting the data ", exception);
                }
            } catch (Exception exception) {
                logger.error("Failed with expection ", exception);
            }
        }


    }

    private Map<String, JSONObject> parseTheBody(String body) {
        List<String> videoIds = JsonPath.read(body, "$.items[*].id.videoId");
        List<Object> videoData = JsonPath.read(body, "$.items[*].snippet");
        Map<String, JSONObject> dataMapper = new HashMap<>();
        for (int i = 0; i < videoIds.size(); i++) {
            String videoId = videoIds.get(i);
            Object content = videoData.get(i);
            String publishedAt = JsonPath.read(content, "$.publishedAt");
            String title = JsonPath.read(content, "$.title");
            String description = JsonPath.read(content, "$.description");
            String defaultThumbnail = JsonPath.read(body, "$.items[" + i + "].snippet.thumbnails.default.url");
            String mediumThumbnail = JsonPath.read(body, "$.items[" + i + "].snippet.thumbnails.medium.url");
            String highThumbnail = JsonPath.read(body, "$.items[" + i + "].snippet.thumbnails.high.url");

            JSONObject data = new JSONObject();
            data.put("publishedAt", publishedAt);
            data.put("title", title);
            data.put("description", description);
            data.put("defaultThumbnail", defaultThumbnail);
            data.put("mediumThumbnail", mediumThumbnail);
            data.put("highThumbnail", highThumbnail);

            dataMapper.put(videoId, data);
        }
        return dataMapper;
    }

    private String cleanUrl(String url) {
        return new String(url.replace("\\/", "/"));
    }


}
