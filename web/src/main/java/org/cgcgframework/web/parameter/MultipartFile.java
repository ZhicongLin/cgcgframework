package org.cgcgframework.web.parameter;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Setter
@Getter
public class MultipartFile {

    private File file;
    private String fileName;
    private String type;
}
