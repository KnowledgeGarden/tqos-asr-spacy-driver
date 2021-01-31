/**
 * 
 */
package org.topicquests.os.asr.driver.sp;

import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import java.io.*;
import java.net.*;

/**
 * @author jackpark
 *
 */
public class HttpClient {
	private SpacyDriverEnvironment environment;

	/**
	 * 
	 */
	public HttpClient(SpacyDriverEnvironment env) {
		environment = env;
	}

	/**
	 * Process a sentence {@code queryString}, a JSON string
	 * @param url
	 * @param queryString
	 * @return can return {@code null}
	 */
	public IResult put(String url, String queryString) {
		IResult result = new ResultPojo();
		BufferedReader rd = null;
		HttpURLConnection con = null;
		OutputStream os = null;
		PrintWriter p = null;
		int counter = 0;
		try {
			URL urx = new URL(url);
			while (counter < 4) {
				try {
					con = (HttpURLConnection) urx.openConnection();
					//con.setReadTimeout(TIMEOUT);
					con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
					con.setRequestMethod("POST");
					con.setDoInput(true);
					con.setDoOutput(true);
					os = con.getOutputStream();
					p = new PrintWriter(os);
					p.print(queryString);
					p.flush();
					p.close();
				} catch (Exception x) {
					environment.logError(x.getMessage()+" Count="+counter, x);
					try {
						wait(3000); // short delay
					} catch (Exception xyz) {};
					counter++;
				}
			}
			int code = con.getResponseCode();
			System.out.println("CODE "+code);
			if (code == 200) {
				rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuilder buf = new StringBuilder();
	
				String line;
				while ((line = rd.readLine()) != null) {
					buf.append(line + '\n');
				}
				result.setResultObject(buf.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.addErrorString(e.getMessage());
		}
		return result;
	}
}
