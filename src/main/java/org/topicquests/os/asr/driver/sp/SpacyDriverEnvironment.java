/**
 * 
 */
package org.topicquests.os.asr.driver.sp;

import org.topicquests.os.asr.pd.api.ISentenceParser;
import org.topicquests.support.RootEnvironment;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class SpacyDriverEnvironment extends RootEnvironment implements ISentenceParser {
	private ISentenceParser parser;
	private HttpClient http;
	/**
	 * 
	 */
	public SpacyDriverEnvironment() {
		super("sp-props.xml", "logger.properties");
		http = new HttpClient(this);
		parser = new SpacyAgent(this, http);
	}

	@Override
	public IResult processSentence(String sentence) {
		return parser.processSentence(sentence);
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

}
