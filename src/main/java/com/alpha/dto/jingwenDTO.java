package com.alpha.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class jingwenDTO {
    @NotBlank(message = "请输入内容")
    private String record;
}
