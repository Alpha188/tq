package com.alpha.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TimeLineDTO {
    private String date;
    private List<String> readNames;
    private List<String> unreadNames;
}
