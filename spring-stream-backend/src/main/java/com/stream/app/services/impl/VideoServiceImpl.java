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

    @Value("${files.video.hsl}")
    String HSL_DIR;


    private VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }



    @PostConstruct
    public void init(){
        File file=new File(DIR);
        try {
            Files.createDirectories(Paths.get(HSL_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

            Video SavedVideo=videoRepository.save(video);

            //Processing Video
            processVideo(SavedVideo.getVideoId());

            //Delete Actual video file if

            //metadata save
            return SavedVideo;



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

    @Override
    public String processVideo(String videoId) {
        Video video = this.get(videoId);
        String filePath=video.getFilePath();

        //Path Where to store data;
        Path videoPath=Paths.get(filePath);
        try {
        //ffmpeg Command
            Path outputPath=Paths.get(HSL_DIR,videoId);
            Files.createDirectories(outputPath);
            String ffmpegCmd = String.format(
                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\" \"%s/index.master.m3u8\"",
                    videoPath, outputPath, outputPath
            );
            System.out.println(ffmpegCmd);
            //file this command
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exit = process.waitFor();
            if (exit != 0) {
                throw new RuntimeException("video processing failed!!");
            }
            return videoId;

        } catch (IOException ex) {
            throw new RuntimeException("Video processing fail!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
