/**
 * 
 */
package org.topicquests.os.asr.driver.sp;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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
		JSONObject paragraph = new JSONObject();
		IResult result = new ResultPojo();
		List<JSONObject>results = new ArrayList<JSONObject>();
		result.setResultObject(results);
		paragraph.put("text", stext);
		//sentence.put("models", models);
environment.logDebug("SpacyAgent\n"+stext);
		result.setResultObject(paragraph);
		//String text = paragraph.toJSONString();
		//TODO may have to url encode this
		List<String> models = modelCollectionOne();
		IResult r; //= 
		//environment.logDebug("SpacyAgent-A "+r.getErrorString());
		//environment.logDebug("B "+r.getResultObject());
		Iterator<String>itr = models.iterator();
		String mdl, json;
		while (itr.hasNext()) {
			mdl = itr.next();
			paragraph.put("model", mdl);
			r = http.put(URL, paragraph.toJSONString());
			json = (String)r.getResultObject();
environment.logDebug("SpacyAgent-1 "+(json != null)+" "+r.getErrorString());
	
			if (json != null) {
				try {
					JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
					JSONObject jo = (JSONObject)p.parse(json);
					paragraph.put("results", jo);
					results.add(paragraph);
	
				} catch (Exception e) {
					e.printStackTrace();
					result.addErrorString(e.getLocalizedMessage());
					environment.logError(e.getLocalizedMessage(), e);
				}
			}
		}

		return result;
	}

}
