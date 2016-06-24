package controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.HttpHeaders;

@RestController
@EnableAutoConfiguration
@ComponentScan
@RequestMapping("/hortonworks/support-tool/v1/*")
public class AppController {
		

		@CrossOrigin(origins = "*")
		@RequestMapping(value = "/test", method = RequestMethod.GET)
		public @ResponseBody String cancelReservation(){
			System.out.println("hi");
			return "<html><h1>Support Tool RESTful web service.</h1><p><font color='blue'>Hello Team! This is a sample RESTful API which would download the logs at backend "
					+ "and would respond with the recommendations</font></p><p>- akhare@hortonworks.com</p>";
		}
		
		
		
		@CrossOrigin(origins = "*")
		@RequestMapping(value="/downloadLogFile",method=RequestMethod.GET	)
		public void getLogFile(HttpSession session,HttpServletResponse response) throws Exception {
		    try {
		    	String fileName="Arpit";
		        String filePathToBeServed ="/Users/akhare/Desktop/text.rtf"; //complete file name with path;
		        File fileToDownload = new File(filePathToBeServed);
		        InputStream inputStream = new FileInputStream(fileToDownload);
		        response.setContentType("application/force-download");
		        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".txt"); 
		        IOUtils.copy(inputStream, response.getOutputStream());
		        response.flushBuffer();
		        inputStream.close();
		    } catch (Exception e){
		       e.printStackTrace();
		    }

		}
		
		@CrossOrigin(origins = "*")	    
	    @RequestMapping(value="/upload", method=RequestMethod.GET)
	    public @ResponseBody String provideUploadInfo() {
	        return "You can upload a file by posting to this same URL.";
	    }
		
		@CrossOrigin(origins = "*")
	    @RequestMapping(value="/upload", method=RequestMethod.POST, consumes = {"multipart/form-data"})
	    public @ResponseBody String handleFileUpload(/*@RequestParam("name") String name,*/
	            @RequestParam("files") MultipartFile file){
	    	String name="ArpitKhare.log";
	        if (!file.isEmpty()) {
	            try {
	                byte[] bytes = file.getBytes();
	                BufferedOutputStream stream =
	                        new BufferedOutputStream(new FileOutputStream(new File(name)));
	                stream.write(bytes);
	                stream.close();
	                return "You successfully uploaded " + name + "!";
	            } catch (Exception e) {
	                return "You failed to upload " + name + " => " + e.getMessage();
	            }
	        } else {
	            return "You failed to upload " + name + " because the file was empty.";
	        }
	    }
		//version
		@CrossOrigin(origins = "*")
		@RequestMapping(value = "/checkForVersion", method = RequestMethod.POST)//)
		public @ResponseBody String checkForVersion(
				@RequestParam("components") String components,
				@RequestParam("jira_id") String jira_id){
			
			System.out.println("hi: "+components+"   "+jira_id);
			
			StringBuilder totalOutput=new StringBuilder();
			BufferedReader reader;
			String param1=components+"-"+jira_id;
			
			String locationOfScript="/root/hadoop/find_hdp_commit.sh";
			String exportToFile=">> /tmp/ARPIT.txt";
			String parameter = param1+exportToFile;
			
			//Runtime runtime = Runtime.getRuntime();

//	        ProcessBuilder pb=new ProcessBuilder("/root/hadoop/find_hdp_commit.sh");
//	        pb.environment().put("param1", components+"-"+jira_id);
//	        
//	        try {
//				Process script_exec = pb.start();
//				 reader = new BufferedReader(new InputStreamReader(        
//						script_exec.getInputStream()));                                          
//	                String output="";                                                                
//	                while ((output = reader.readLine()) != null) {  
//	                	totalOutput.append(output);
//	                  System.out.println("Script output: " + output);                             
//	                }
//			} catch (IOException e) {
//				
//				e.printStackTrace();
//			}
			try {
				System.out.println("[DEBUG] command: "+locationOfScript+ " "+parameter);
				Process process = Runtime.getRuntime().exec(locationOfScript+ " "+parameter);
				System.out.println("[DEBUG] .exec() ");
				try {
					System.out.println("[DEBUG] process.waitFor() starts ");
					process.waitFor();
					System.out.println("[DEBUG] process.waitFor() ends ");
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
				 reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                     String line = "";           
                     while ((line = reader.readLine())!= null) {
                    	 System.out.println("[DEBUG] writing output to a variable (not file) ");
                    	 totalOutput.append(line + "\n");
         }
				  // in.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			return "<html><h1>Support Tool RESTful web service.</h1><p><font color='blue'>"+totalOutput+"<br><br> Hello Team! This is a sample RESTful API which would download the logs at backend "
					+ "and would respond with the recommendations</font></p><p>- akhare@hortonworks.com</p>";
		}
	    
		

}
