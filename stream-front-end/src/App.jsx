
import './App.css'
import VideoUpload from './components/VideoUpload'

function App() {
 

  return (
   <>
   <div className='flex flex-col items-center justify-center py-9 space-y-5'>
    <h1 className='text-3xl font-bold  text-gray-700 dark:text-gray-100 '>Streaming Application</h1>

    <div>
      <h1 className='text-white'>
        Playing Video
      </h1>
      {/* <video
    id="my-video"
    className="video-js"
    controls
    preload="auto"
    width="640"
    height="264"
    data-setup="{}"
  >
    <source src={`http://localhost:8080/api/v1/videos/stream/range/${"2de6a435-9e0a-4e19-a28a-8755f539da46"}`}  type="video/mp4" />
    <p className="vjs-no-js">
      To view this video please enable JavaScript, and consider upgrading to a
      web browser that
      <a href="https://videojs.com/html5-video-support/" target="_blank"
        >supports HTML5 video</a
      >
    </p>
  </video> */}
      <video
      style={{
        width:300,
        height:250,
      }}
       src={`http://localhost:8080/api/v1/videos/stream/range/${"2de6a435-9e0a-4e19-a28a-8755f539da46"}`} controls></video>
    </div>

    <VideoUpload/>
   </div>
   </>
  )
}

export default App
