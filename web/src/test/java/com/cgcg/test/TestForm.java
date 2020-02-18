package com.cgcg.test;

import lombok.Getter;
import lombok.Setter;
import org.cgcgframework.web.parameter.MultipartFile;

@Setter
@Getter
public class TestForm {

    private MultipartFile file;

    private String myName;
}
