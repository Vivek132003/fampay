package com.fampay.assignment.GCP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HitDataApi
{
    //Helps in triggering the data api continuously according to the scheduled time
    @Autowired
    private DataApi dataApi;
    @Scheduled(fixedRate = 100000) // 1000 milliseconds = 1 second
    public void performApiRequest() {
        dataApi.searchContent();
    }

}
