
import './App.css'
import VideoUpload from './components/VideoUpload'
import {useState} from 'react';

function App() {
  // eslint-disable-next-line no-unused-vars
  const [videoId,setVideoId]=useState('2de6a435-9e0a-4e19-a28a-8755f539da46');

  return (
   <>
   <div className='flex flex-col items-center justify-center py-9 space-y-5'>
    <h1 className='text-3xl font-bold  text-gray-700 dark:text-gray-100 '>Streaming Application</h1>

    <div>
      <h1 className='text-white'>
        Playing Video
      </h1>
      <video
      style={{
        width:"100%",
      }}
      //  src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`} controls>
       src="http://localhost:8080/api/v1/videos/ecf154e5-9c45-42b2-b1a1-df9a25398dde/index.master.m3u8" controls >

       </video>
    </div>

    <VideoUpload/>
   </div>
   </>
  )
}

export default App
