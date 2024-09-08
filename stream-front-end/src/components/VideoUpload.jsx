/* eslint-disable no-unused-vars */
import axios from "axios";
import React, { useState } from "react";
import videoLogo from "../assets/upload.png";
import {
  Button,
  Card,
  Label,
  Textarea,
  TextInput,
  Progress,
  Alert,
} from "flowbite-react";

function VideoUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [progress, setProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");
  const [meta, setMeta] = useState({
    title: "",
    description: "",
  });

  function handleFileChange(event) {
    // console.log(event.target.files[0]);
    setSelectedFile(event.target.files[0]);
  }

  function formFieldChange(event) {
    // console.log(event.target.name);
    // console.log(event.target.value);
    setMeta({
      ...meta,
      [event.target.name]: event.target.value,
    });
  }

  function handleForm(formEvent) {
    formEvent.preventDefault(); // closing the Auto sumbit of the form

    if (!selectedFile) {
      alert("Select the File!!");
      return;
    }

    //Submit File to the server
    saveVideoToServer(selectedFile, meta);
  }

  //Resetting Form
  function resetForm(){
    setMeta({
      title:"",
      description:"",
    });
    setSelectedFile(null);
    setUploading(false);
    // setMessage("");
  }

  //submit file to server
  async function saveVideoToServer(video, videoMetaData) {
    setUploading(true);

    //Api call
    try {
      let formData = new FormData();
      formData.append("title", videoMetaData.title);
      formData.append("description", videoMetaData.description);
      formData.append("file", selectedFile);

      let response = await axios.post(
        `http://localhost:8080/api/v1/videos`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
          onUploadProgress: (progressEvent) => {
            const progress = Math.round(
              (progressEvent.loaded * 100) / progressEvent.total
            );

            console.log(progress);
          },
        }
      );
      // console.log(response);
      setMessage("File Uploaded");
      setProgress(0);
      resetForm();
    } catch (error) {
      console.log(error);
      setMessage("Error in Uploading File!!");
    } finally {
      setUploading(false);
    }
  }

  return (
    <div className="text-white">
      <Card>
        <h3 className="text-center">Upload Videos</h3>
        <div>
          <form noValidate className="space-y-5" onSubmit={handleForm}>
            {/* div for the title */}
            <div>
              <div className="mb-2 block">
                <Label htmlFor="file-upload" value="Video Title" />
              </div>
              <TextInput
              value={meta.title}
                onChange={formFieldChange}
                name="title"
                placeholder="Video Title"
              />
            </div>

            {/* div for the text area */}
            <div className="max-w-md">
              <div className="mb-2 block">
                <Label htmlFor="comment" value="Video Description" />
              </div>
              <Textarea
              value={meta.description}
                onChange={formFieldChange}
                name="description"
                id="comment"
                placeholder="Write the video description..."
                required
                rows={4}
              />
            </div>

            {/* div for the items inside the cards */}
            <div className="flex items-center space-x-9 justify-center">
              <div className="shrink-0">
                <img
                  className="h-16 w-16 object-cover "
                  src={videoLogo}
                  alt="Current Video"
                />
              </div>
              <label className="block">
                {/* <span className="sr-only ">Choose profile photo</span> */}
                <input
                  name="file"
                  onChange={handleFileChange}
                  type="file"
                  className="block w-full text-sm text-slate-500
      file:mr-4 file:py-2 file:px-4
      file:rounded-full file:border-0
      file:text-sm file:font-semibold
      file:bg-violet-50 file:text-violet-700
      hover:file:bg-violet-100
    "
                />
              </label>
            </div>

            {/* Setting the progress bar */}
            <div>
              {uploading && <Progress
                progress={progress}
                size="lg"
                textLabel="Uploaing.."
                labelProgress
                labelText
              />}
            </div>

            {/* Setting the alert theme */}
            <div>
             {message &&  <Alert
                color="success"
                onDismiss={() => setMessage("")}
              >
                <span className="font-medium">Success Alert!</span> {message}
              </Alert>}
            </div>

            {/* for the submit button */}
            <div className="flex justify-center ">
              <Button disabled={uploading} type="submit">Upload</Button>
            </div>
          </form>
        </div>
      </Card>
    </div>
  );
}
export default VideoUpload;
