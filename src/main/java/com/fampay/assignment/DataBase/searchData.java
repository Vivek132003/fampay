package com.fampay.assignment.DataBase;

import com.fampay.assignment.DTO.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class searchData
{
    public static final Logger logger = LoggerFactory.getLogger(searchData.class);
    private static Connection connection;
    private void EnableConnnection()
    {
        try(InputStream input = new FileInputStream("src/main/resources/application.yml")){
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);

            Map<String, Object> spring = (Map<String, Object>) data.get("spring");
            Map<String, Object> datasource = (Map<String, Object>) spring.get("datasource");

            String url=(String) datasource.get("url");
            String username=(String) datasource.get("username");
            String password=(String) datasource.get("password");
            connection= DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            System.out.println("connection failed "+e.getMessage());
        }
    }
    public List<Response> searchData(String title, String description)  {
        try
        {
            if(connection==null)EnableConnnection();
            String searchQuery="select * from youtube_data where title like ? and description like ? order by published_on desc limit 10;";
            PreparedStatement statement=connection.prepareStatement(searchQuery);
            statement.setString(1,"%"+title+"%");
            statement.setString(2,"%"+description+"%");

            ResultSet resultSet=statement.executeQuery();

            List<Response> responseList=new ArrayList<>();
            while(resultSet.next())
            {
                Response content=new Response();
                String videoId=resultSet.getString("video_id");
                String video_description=resultSet.getString("description");
                String video_titile=resultSet.getString("title");
                Timestamp timestamp=resultSet.getTimestamp("published_on");
                String thumbnails=resultSet.getString("thumbnails");
                content.setDescription(video_description);
                content.setTitle(video_titile);
                content.setVideoId(videoId);
                content.setTimestamp(timestamp);
                content.setThumbNails(thumbnails);

                responseList.add(content);
            }
            return responseList;

        }
        catch (SQLException exception)
        {
            logger.error("Failed while fetching the details with ", exception.getMessage());
        }
        catch (Exception exception)
        {
            logger.error("Failed with reason ", exception.getMessage());
        }
        return null;

    }

}
