package com.fampay.assignment.DTO;

import lombok.*;
import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Response
{
    private String videoId;
    private String title;
    private String description;
    private Timestamp timestamp;
    private String thumbNails;
}
