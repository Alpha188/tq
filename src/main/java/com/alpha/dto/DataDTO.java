package com.alpha.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataDTO {
    private List<String> xAxis;
    private List<InfoDTO> series;
    private List<String> legend;
}
