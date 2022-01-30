package org.cgcgframework.web.parameter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@Slf4j
public class HttpServletRequestReader {

    // 字符串读取
    // 方法一
    public static String readAsChars(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    // 方法二
    public static void readAsChars2(HttpServletRequest request) {
        InputStream is = null;
        try {
            is = request.getInputStream();
            StringBuilder sb = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                sb.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // 二进制读取
    public static byte[] readAsBytes(HttpServletRequest request) {

        int len = request.getContentLength();
        byte[] buffer = new byte[len];
        ServletInputStream in = null;

        try {
            in = request.getInputStream();
            in.read(buffer, 0, len);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }

    public static Object readFormData(HttpServletRequest request, Class<?> type) {
        //创建一个解析器工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //文件上传解析器
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 判断enctype属性是否为multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Object param = null;
        try {
            param = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        if (isMultipart) {
            try {
                //解析请求，将表单中每个输入项封装成一个FileItem对象
                List<FileItem> fileItems = upload.parseRequest(request);
                // 迭代表单数据
                for (FileItem fileItem : fileItems) {
                    Field field = null;
                    try {
                        field = type.getDeclaredField(fileItem.getFieldName());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    if (field == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    //判断输入的类型是 普通输入项 还是文件
                    if (fileItem.isFormField()) {
                        //普通输入项 ,得到input中的name属性的值,fileItem.getFieldName()
                        ////得到输入项中的值,fileItem.getString("UTF-8"),"UTF-8"防止中文乱码
                        field.set(param, fileItem.getString("UTF-8"));
                    } else {
                        //上传的是文件，获得文件上传字段中的文件名
                        //注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名
                        String fileName = fileItem.getName();
                        //Substring是字符串截取，返回值是一个截取后的字符串
                        //lastIndexOf(".")是从右向左查,获取.之后的字符串
                        String ext = fileName.substring(fileName.lastIndexOf("."));
                        //UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法, UUID的唯一缺陷在于生成的结果串会比较长
                        String name = UUID.randomUUID() + ext;
                        //系统临时文件目录
                        String folder = System.getProperty("java.io.tmpdir");
                        //将FileItem对象中保存的主体内容保存到某个指定的文件中
                        File file = new File(folder + name);
                        fileItem.write(file);
                        final MultipartFile multipartFile = new MultipartFile();
                        multipartFile.setFile(file);
                        multipartFile.setFileName(fileName);
                        multipartFile.setType(ext);
                        field.set(param, multipartFile);
                        FileTempContext.put(file.getPath());
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        } else {
            log.error("请求的头部信息应为[Content-Type=multipart/form-data]");
        }
        return param;
    }

}