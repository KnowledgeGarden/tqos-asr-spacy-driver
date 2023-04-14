/*
 * Copyright 2022 TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.os.asr.driver.sp;

//import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.topicquests.os.asr.pd.api.ISentenceParser;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 *
 */
public class SpacyAgent implements ISentenceParser {
	private SpacyDriverEnvironment environment;
	private HttpClient http;
	//private final List<String> models;
	private final String
		//SERVER,
		//PORT,
		URL;

	/**
	 * 
	 */
	public SpacyAgent(SpacyDriverEnvironment env, HttpClient cl) {
		environment = env;
		http = cl;



		//SERVER = environment.getStringProperty("ServerURl");
		//PORT = environment.getStringProperty("ServerPort");
		URL = environment.getStringProperty("ServerURL"); //"http://"+SERVER+":"+Integer.parseInt(PORT)+"/all";
	}
	
	//appears we are not using these models
	
	List<String> modelCollectionOne() {
		List<String> models = new ArrayList<String>();
		models.add("en_ner_jnlpba_md");
		models.add("en_ner_bc5cdr_md");
		models.add("en_ner_bionlp13cg_md"); //
		models.add("en_ner_craft_md"); //
		models.add("en_core_web_lg");  //
		models.add("en_core_sci_lg"); //
		models.add("en_core_web_trf"); //
		models.add("en_core_sci_scibert"); //
		models.add("stanza;craft;anatem"); //
		models.add("stanza;craft;bc5cdr"); //
		models.add("stanza;craft;jnlbpa"); //
		models.add("stanza;craft;bionlp13cg"); //
		models.add("stanza;craft;ncbi_disease"); //
		return models;
	}
	

	@Override
	public IResult processParagraph(String stext) {
		long starttime = System.currentTimeMillis();
		JSONObject paragraph;
		IResult result = new ResultPojo();
		//result.setResultObject(results);
		//sentence.put("models", models);
		environment.logDebug("SpacyAgent\n"+stext);
		//String text = paragraph.toJSONString();
		//TODO may have to url encode this
		List<String> models = modelCollectionOne();
		IResult r; //= 
		Iterator<String>itr = models.iterator();
		String json, mdl;
		JsonArray sentences = new JsonArray();;
		result.setResultObject(sentences);
		JsonObject jo;
		try {
			while (itr.hasNext()) {
				mdl = itr.next();
				paragraph = new JSONObject();
				paragraph.put("text", stext);
				paragraph.put("model", mdl);
				r = http.put(URL, paragraph.toJSONString());
				json = (String)r.getResultObject();
				jo = parseJsonObject(json);
				environment.logDebug("SpacyAgent-1\n"+jo);
		
				if (json != null) {
					sentences.add(jo);
	
				}
			}
		} catch (Exception e) {
			environment.logError("SpacyAgent "+e.getMessage(),e);
			result.addErrorString(e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		long delta = (endTime-starttime)/1000;
		environment.logDebug("Finished in "+delta+" seconds");
		return result;
	}

	JsonObject parseJsonObject(String json)  throws Exception {
		return (JsonObject)JsonParser.parseString(json);
	}
}
