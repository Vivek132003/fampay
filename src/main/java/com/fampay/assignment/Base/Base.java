package com.fampay.assignment.Base;

import com.fampay.assignment.DTO.Response;

import java.util.List;

public interface Base
{
    public List<Response> searchData(String title, String description);
}
