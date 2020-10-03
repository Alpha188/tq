package com.alpha.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class InfoSaveDTO {
    private List<Date> dates;
    private List<String> infos;
}
