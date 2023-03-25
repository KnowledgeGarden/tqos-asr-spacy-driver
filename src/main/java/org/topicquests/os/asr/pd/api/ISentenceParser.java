/**
 * 
 */
package org.topicquests.os.asr.pd.api;

import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public interface ISentenceParser {

	/**
	 * 
	 * @param stext = a JSON string
	 * @return returns a JSONObject
	 */
	IResult processParagraph(String stext);
}
