/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class VideoController {

	private VideoFileManager videoDataMgr;


	@PostConstruct
   public void init() throws IOException {
         try {

            videoDataMgr = VideoFileManager.get();

            } catch (IOException e) {

            e.printStackTrace();

            }
   }

	@RequestMapping(value="/video", method=RequestMethod.GET)
	    public @ResponseBody Collection<Video> getVideoList() {
		 return videoDataMgr.getVideoList();		
 		 
 	 }
 	 
	 @RequestMapping(value = "/video", method = RequestMethod.POST)
	    public @ResponseBody Video addVideo(@RequestBody Video v){
		 
		Video video = null;
		video = videoDataMgr.save(v);
	        // Do something with the Video
	        // ...
	        return video;
	    }
	 
	 @RequestMapping(value ="/video/{id}/data", method = RequestMethod.POST   )
	 public @ResponseBody VideoStatus setVideoData(
			 @PathVariable("id") String id,
			 @RequestParam("data") MultipartFile data,
			 HttpServletResponse response
			 ){
		 
		 
		 try {
			 Video v = videoDataMgr.getVideoById(Long.parseLong(id));
			videoDataMgr.saveVideo(v, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			response.setStatus(404);
			e.printStackTrace();
		}
		 VideoStatus vstatus = new VideoStatus(VideoState.READY);
		 return vstatus;
		 
	 }
	 
	 @RequestMapping(value ="/video/{id}/data", method = RequestMethod.GET)
	 public  void getData(
			 @PathVariable("id") String id,
			 HttpServletResponse response
			 ){
		
		 try {
			 Video v = videoDataMgr.getVideoById(Long.parseLong(id));
			videoDataMgr.serveVideo(v, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			response.setStatus(404);
			e.printStackTrace();
		}
		 
	 }

	
}
