import "./App.css";
import VideoPlayer from "./components/VideoPlayer";
import VideoUpload from "./components/VideoUpload";
import { useState } from "react";
import { TextInput } from "flowbite-react";

function App() {
  // eslint-disable-next-line no-unused-vars
  const [videoId, setVideoId] = useState(
    "ecf154e5-9c45-42b2-b1a1-df9a25398dde"
  );
  const [fieldValue,setFieldValue]=useState(null);
  // eslint-disable-next-line no-unused-vars
  function playVideo(videoId){
    setVideoId(videoId);
  }

  return (
    <>
      <div className="flex flex-col items-center justify-center py-9 space-y-5">
        <h1 className="text-3xl font-bold  text-gray-700 dark:text-gray-100 ">
          Streaming Application
        </h1>

        <div>
          <h1 className="text-white">Playing Video</h1>
          {/* <video
      style={{
        width:"100%",
      }}
      //  src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`} controls>
       src="http://localhost:8080/api/v1/videos/ecf154e5-9c45-42b2-b1a1-df9a25398dde/index.master.m3u8" controls >
       </video> */}

          <div className="flex">
            <TextInput onClick={(event)=>{
              setFieldValue(event.target.value);
            }}
              placeholder="Enter video id here "
              name="video_id_field"
            ></TextInput>
            <button onClick={()=>{
              setVideoId(fieldValue);
            }} className="bg-white">Play</button>
          </div>
          
          <div>
            <VideoPlayer
              src={`http://localhost:8080/api/v1/videos/${videoId}/index.master.m3u8`}
            ></VideoPlayer>
          </div>
        </div>

        <VideoUpload />
      </div>
    </>
  );
}

export default App;
