/**
 * 
 */
package org.topicquests.os.asr.driver.sp;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.topicquests.os.asr.pd.api.ISentenceParser;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 *
 */
public class SpacyAgent implements ISentenceParser {
	private SpacyDriverEnvironment environment;
	private HttpClient http;
	private final List<String> models;
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
		models = new ArrayList<String>();
		models.add("en_ner_jnlpba_md");
		models.add("en_ner_bc5cdr_md");
		models.add("en_ner_bionlp13cg_md");
		models.add("en_ner_craft_md");
		models.add("en_core_web_lg");
		models.add("en_core_sci_lg");
		//SERVER = environment.getStringProperty("ServerURl");
		//PORT = environment.getStringProperty("ServerPort");
		URL = environment.getStringProperty("ServerURL"); //"http://"+SERVER+":"+Integer.parseInt(PORT)+"/all";
	}

	@Override
	public IResult processSentence(String stext) {
		JSONObject sentence = new JSONObject();
		IResult result = new ResultPojo();
		sentence.put("text", stext);
		//sentence.put("models", models);
//environment.logDebug("SpacyAgent\n"+stext);
		result.setResultObject(sentence);
		String text = sentence.toJSONString();
		//TODO may have to url encode this
		IResult r = http.put(URL, text);
		//environment.logDebug("A "+r.getErrorString());
		//environment.logDebug("B "+r.getResultObject());
		String json = (String)r.getResultObject();
environment.logDebug("SpacyAgent-1 "+(json != null)+" "+r.getErrorString());
	
		if (json != null) {
			try {
				JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
				JSONObject jo = (JSONObject)p.parse(json);
				sentence.put("results", jo);
				//sentence.remove("models");

			} catch (Exception e) {
				e.printStackTrace();
				result.addErrorString(e.getLocalizedMessage());
				environment.logError(e.getLocalizedMessage(), e);
			}
		}
		return result;
	}

}
