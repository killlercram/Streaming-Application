package com.stream.app.services;


import com.stream.app.entities.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    //Save Video
    Video save(Video video, MultipartFile file);

    //get Video by id
    Video get(String videoId);

    //get video by title
    Video getByTitle(String title);

    //getting all Videos
    List<Video> getAll();


    //Video Processing
    String processVideo(String videoId);


}
