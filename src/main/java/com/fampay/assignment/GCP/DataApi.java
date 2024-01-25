package com.fampay.assignment.GCP;

import com.fampay.assignment.DataBase.youtube_data_database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class DataApi
{
    public static final Logger logger = LoggerFactory.getLogger(DataApi.class);
    @Autowired
    private youtube_data_database database;
    private static String url="https://www.googleapis.com/youtube/v3/search";
    private RestTemplate template=new RestTemplate();

    private  List<String> keys;

    private List<String> topics;
    public void searchContent()  {
        try{
            UriComponentsBuilder uri=setQueryParams();
            ResponseEntity<String> response=template.getForEntity(uri.toUriString(),String.class);
            String body=response.getBody();
            database.insert(body);
        }
        catch (Exception e)
        {
            logger.error("Failed while hitting data api",e);
        }
    }
    private UriComponentsBuilder setQueryParams()
    {
        //added a simple logic to fetch the data with the different keys and topics with same probability
        if(keys==null)arrangeDetails();
        String key= keys.remove(0);
        String topic=topics.remove(0);
        logger.info("Finding topic "+topic);
        UriComponentsBuilder uri=UriComponentsBuilder.fromHttpUrl(url);
        uri.queryParam("key",key);
        uri.queryParam("q",topic);
        uri.queryParam("type","video");
        uri.queryParam("part","snippet");
        uri.queryParam("order","date");
        uri.queryParam("maxResults",10);
        keys.add(key);
        topics.add(topic);
        return uri;
    }
    private void arrangeDetails()
    {
        try(InputStream input = new FileInputStream("src/main/resources/application.yml")){
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            Map<String, Object> mydata = (Map<String, Object>) data.get("mydata");

            List<String> keys=(List<String>) mydata.get("key");
            List<String> topics= (List<String>) mydata.get("topics");

            this.keys=keys;
            this.topics=topics;

        } catch (Exception e) {

            logger.error("connection failed ",e);
        }
    }
}
