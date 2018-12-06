package com.xuyu.house_biz.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

@Service
public class FileService {

    @Value("${file.path}")
    private String filePath;


    public List<String> getImgPaths(List<MultipartFile> files) {
        if (Strings.isNullOrEmpty(filePath)) {
            filePath = getResourcePath();
            //System.out.println("为空");
        }
        List<String> paths = Lists.newArrayList();
        files.forEach(file -> {
            File localFile = null;
            if (!file.isEmpty()) {
                try {
                  //  System.out.println("不为空");
                    localFile =  saveToLocal(file, filePath);
                    //System.out.println(localFile.getAbsolutePath());
                    String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(),"E:\\imgs");
                    //System.out.println(path);
                    paths.add(path);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        //System.out.println(paths+paths.get(0)+"这是路径");
        return paths;
    }

    public static String getResourcePath(){
        File file = new File(".");
        String absolutePath = file.getAbsolutePath();//获得绝对路径
        return absolutePath;
    }

    private File saveToLocal(MultipartFile file, String filePath2) throws IOException {
        File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() +"/"+file.getOriginalFilename());
        if (!newFile.exists()) {
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
        Files.write(file.getBytes(), newFile);
        return newFile;
    }

}
