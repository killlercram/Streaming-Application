package com.stream.app.controllers;
import com.stream.app.payload.CustomMessage;

import com.stream.app.entities.Video;
import com.stream.app.services.VideoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.stream.app.AppConstants.CHUNK_SIZE;

@RequestMapping("/api/v1/videos")
@RestController
@CrossOrigin("*")
public class videoController {
    private VideoService videoService;

    public videoController(VideoService videoService){
        this.videoService=videoService;
    }

    //Video Upload
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

    //get All Videos
    @GetMapping
    public List<Video> getAll(){
        return videoService.getAll();
    }

    //stream Video
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(@PathVariable String videoId){
        Video video=videoService.get(videoId);//Video Information

        //getting the Info of the video
        String contentType=video.getContentType();
        String filePath=video.getFilePath();

        if(contentType==null){
            //if there is no content type in the video then we will set this
            contentType="application/octet-stream";
        }
        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);


    }

    //Streaming Video Chunks

    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoRange(
            @PathVariable String videoId,
            @RequestHeader(value = "Range",required = false) String range
    ){
        System.out.println(range);
        Video video=videoService.get(videoId);
//        Getting the File Path of the Above Video
        Path path= Paths.get(video.getFilePath());
//        Now After Getting the Path we will Make resource
        Resource resource =new FileSystemResource(path);

//        Getting the Content Type

        String contentType=video.getContentType();
        if(contentType==null){
            contentType="application/octet-stream";
        }
        //File Length
        long fileLength=path.toFile().length();

//        If the range is null then will send full Video
        if(range==null) return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);

//        Range Calculation
        long rangeStart;
        long rangeEnd;
        String[] ranges = range.replace("bytes=","").split("-");
        rangeStart=Long.parseLong(ranges[0]);

//        if(ranges.length>1) {
//            rangeEnd = Long.parseLong(ranges[1]);
//        }else{
//            rangeEnd=fileLength-1;
//        }
//        if (rangeEnd>fileLength) {
//            rangeEnd = fileLength - 1;
//        }

        rangeEnd = rangeStart + CHUNK_SIZE - 1;
        if (rangeEnd>=fileLength) {
            rangeEnd = fileLength - 1;
        }

//        place From where we will Read Data
        InputStream inputStream;
        try{
//            Getting the Content of file
//            Generally used For skipping
            inputStream= Files.newInputStream(path);
            inputStream.skip(rangeStart);//skipping the part user don't want
            long contentLength= rangeEnd - rangeStart + 1;//Length of the Content Wanted

            byte[] data=new byte[(int) contentLength];//Adding data in array
            int read= inputStream.read(data,0,data.length);//reading data from the array
            System.out.println("read number of bytes:"+read);

            HttpHeaders headers=new HttpHeaders();
            headers.add("Content-Range","bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
            headers.add("Cache-Control","no-cache,no-store,must-revalidate");
            headers.add("Pragma","no-cache");
            headers.add("Expires","0");
            headers.add("X-Content-Type-Options","nosniff");
            headers.setContentLength(contentLength);

            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new ByteArrayResource(data));

        }catch (IOException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }







    }
}
