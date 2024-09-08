
import './App.css'
import VideoUpload from './components/VideoUpload'

function App() {
 

  return (
   <>
   <div className='flex flex-col items-center justify-center py-9 space-y-5'>
    <h1 className='text-3xl font-bold  text-gray-700 dark:text-gray-100 '>Streaming Application</h1>

    <VideoUpload/>
   </div>
   </>
  )
}

export default App
