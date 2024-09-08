package com.stream.app.controllers;
import com.stream.app.payload.CustomMessage;

import com.stream.app.entities.Video;
import com.stream.app.services.VideoService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RequestMapping("/api/v1/videos")
@RestController
@CrossOrigin("*")
public class videoController {
    private VideoService videoService;

    public videoController(VideoService videoService){
        this.videoService=videoService;
    }

    //for creating the videos.
    @PostMapping
    public ResponseEntity<?> create(

            @RequestParam("file")MultipartFile file,
            @RequestParam("title")String title,
            @RequestParam ("description") String description
            ){

        Video video =new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoId(UUID.randomUUID().toString());


       Video savedvideo= videoService.save(video,file);
       if(savedvideo!=null){
           return  ResponseEntity.status(HttpStatus.OK).body(video);
       }else{
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(CustomMessage.builder()
                           .message("Video not uploaded")
                           .success(false)
                           .build()
                   );
       }

    }
}
