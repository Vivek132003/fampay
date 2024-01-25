package com.fampay.assignment.service;

import com.fampay.assignment.Base.Base;
import com.fampay.assignment.DTO.Response;
import com.fampay.assignment.DataBase.searchData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service implements Base {

    @Override
    public List<Response> searchData(String title, String description) {
        return new searchData().searchData(title,description);
    }
}
