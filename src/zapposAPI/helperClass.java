// Using the rest Api given by Zappos
// Vinodh Sankaravadivel., USC
package zapposAPI;

import java.io.*;
import java.net.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class helperClass {
	public static final String BASEURL = "http://api.zappos.com/Search?key=52ddafbe3ee659bad97fcce7c53592916a6bfd73";
	
	
	public static String prompt (String s)
	{
    	try {
    		System.out.print(s);
    		System.out.flush();
    		return (new BufferedReader(new InputStreamReader(System.in))).readLine();
    	}
    	catch (IOException e) { System.err.println(e); return ""; }
    }
	
	
	public static String httpGet(String urlStr) throws IOException 
	{
		URL url = new URL(urlStr);
		HttpURLConnection conn =
		      (HttpURLConnection) url.openConnection();
	
		if (conn.getResponseCode() != 200) {
			System.out.println(conn.getResponseCode());
		    throw new IOException(conn.getResponseMessage());
		}
	
		BufferedReader rd = new BufferedReader(
	      new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
	
		conn.disconnect();
		return sb.toString();
	}
	
	public static JSONObject parseReply(String reply) throws ParseException
	{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(reply);
		JSONObject object = (JSONObject)obj;
		return object;
	}
	
	
	public static JSONArray getResults(JSONObject reply)
	{
		Object resultObject = reply.get("results");
		JSONArray resultArray = (JSONArray)resultObject;
		return resultArray;
	}
}
