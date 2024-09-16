package com.stream.app.services.impl;

import ch.qos.logback.core.util.StringUtil;
import com.stream.app.entities.Video;
import com.stream.app.repositeries.VideoRepository;
import com.stream.app.services.VideoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
import java.util.List;
@Service
public class VideoServiceImpl implements VideoService {

    @Value("${files.video}")
    String DIR;


    private VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }



    @PostConstruct
    public void init(){
        File file=new File(DIR);
        if(!file.exists()){
            file.mkdir();
            System.out.println("folder Created");
        }else{
            System.out.println("Folder Already Created");
        }

    }

    @Override
    public Video save(Video video, MultipartFile file) {

        try {
            //original File Name
            String filename = file.getOriginalFilename();
            String contentType = file.getContentType();
            //Reading the data.
            InputStream inputStream = file.getInputStream();


            //folder path:create

            //file path
            String cleanFileName= StringUtils.cleanPath(filename);
            //folder path:create
            String cleanFolder=StringUtils.cleanPath(DIR);

            //getting the path and the folder name representation
            //folder path with filename
            Path path= Paths.get(cleanFolder,cleanFileName);
            System.out.println(path);


            //copy file to the folder

            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);


            //video meta data
            video.setContentType(contentType);
            video.setFilePath(path.toString());


            //metadata save
            return videoRepository.save(video);

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public Video get(String videoId) {
        return videoRepository.findById(videoId).orElseThrow(()->new RuntimeException("Video Not Found!"));
    }
    @Override
    public Video getByTitle(String title) {
        return null;
    }
    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }
}
