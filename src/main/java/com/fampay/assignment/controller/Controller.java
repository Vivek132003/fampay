package com.fampay.assignment.controller;

import com.fampay.assignment.Base.Base;
import com.fampay.assignment.DTO.Response;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller
{
    @Autowired
    Base base;
    @GetMapping(value = "/searchData")
    public ResponseEntity<JSONObject> searchData(@RequestParam String title, @RequestParam String description)
    {
        List<Response> responseList=base.searchData(title,description);
        JSONObject object=new JSONObject();
        if(responseList.isEmpty())
        {
            object.put("Failure Reason","No Data Found");
            return  new ResponseEntity<>(object,HttpStatus.NOT_FOUND);
        }
        object.put("details",responseList);
        return  new ResponseEntity<>(object,HttpStatus.OK);
    }

}
