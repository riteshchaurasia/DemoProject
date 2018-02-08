package com.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ContentProcess {

	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream("application.properties");
		// load a properties file
		prop.load(input);
		
		String solrUrl = prop.getProperty("solrUrl");
		URL obj = new URL(solrUrl);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		JSONObject jsonObj = new JSONObject(response.toString());
		System.out.println("Result : "+jsonObj);
		
		 //Remove blank spaces
		 String prettyJsonString = jsonObj.toString();
	     ObjectMapper objectMapper = new ObjectMapper();
	     JsonNode jsonNode = objectMapper.readValue(prettyJsonString, JsonNode.class);
	     System.out.println(jsonNode.toString());
		
	   //  String minifiedJson = new Minify().minify(jsonNode.toString());
		 FileOutputStream fileStream = new FileOutputStream(new File(prop.getProperty("destFileLocation")));
		 OutputStreamWriter writer = new OutputStreamWriter(fileStream);
		 writer.write(jsonNode.toString());
		 System.out.println("***Written JSON***");
		 writer.flush();
		 writer.close();
	}

}

