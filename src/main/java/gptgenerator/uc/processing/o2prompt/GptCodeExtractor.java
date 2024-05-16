package gptgenerator.uc.processing.o2prompt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract the code part of the ChatGPT reply<br>
 * Remove the leading and trailing areas, that are delimited by ``` from the file, if they exist
 */
public class GptCodeExtractor {

	public static String extractCode(String reply) {
		if (containsQuote(reply)) {
			return extractContent(reply);
		} else {
			return reply;
		}
	}

	private static boolean containsQuote(String reply) {
		Pattern pattern = Pattern.compile("^```");
		String[] lines = reply.split("\n");
		for (String curLine : lines) {
			Matcher matcher = pattern.matcher(curLine);
			if (matcher.find()) {
				return true;
			}
		}
		return false;
	}

	private static String extractContent(String reply) {
		StringBuffer result = new StringBuffer();
		Pattern pattern = Pattern.compile("^```");
		String[] lines = reply.split("\n");
		boolean inQuote = false;
		for (String curLine : lines) {
			Matcher matcher = pattern.matcher(curLine);
			if (matcher.find()) {
				inQuote = ! inQuote;
				continue;
			}
			if (inQuote) {
				result.append(curLine+"\n"); 
			}
		}

		return result.toString();
	}
}
