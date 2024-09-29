// eslint-disable-next-line no-unused-vars
import React, { useEffect, useRef } from 'react';
import videojs from 'video.js';
import Hls from 'hls.js';
import "video.js/dist/video-js.css";

// eslint-disable-next-line react/prop-types
function VideoPlayer({src}) {
  const videoRef=useRef(null)
  const playerRef=useRef(null)

  //Changing the src will manipulate 
  useEffect(()=>{
    //for init
    playerRef.current=videojs(videoRef.current,{
      controls:true,
      autoplay:true,
      muted: true,
      preload: "auto"
    });
    if(Hls.isSupported()){
      const hls=new Hls()
      hls.loadSource(src)
      hls.attachMedia(videoRef.current)
      hls.on(Hls.Events.MANIFEST_PARSED,()=>{
        videoRef.current.play();
      });
      return()=>{
        hls.destroy();
      }
    }else if(videoRef.current.canPlayType("application/vnd.apple.mpegurl")){
      videoRef.current.src=src;
      videoRef.current.addEventListener("CanPlay",()=>{
      });
    }else{
      console.log("Video Formate not Supported")
    }
  },[src]);
  return (
    <div>
      <div data-vjs-player>

        <video ref={videoRef} style={{
          width:"100%",
          height: "500px",
        }} 
        className="video-js vjs-control-bar"></video>
        
      </div>
    </div>
  )
}

export default VideoPlayer