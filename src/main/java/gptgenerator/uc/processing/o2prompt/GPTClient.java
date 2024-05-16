package gptgenerator.uc.processing.o2prompt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GPTClient implements IChatClient {
	private final static String OPENAI_API_KEY = "OPENAI_API_KEY";
	private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

	private String apiKey = "";
	private double temperature;

	public GPTClient(double temperature) {
		String variableValue = System.getenv(OPENAI_API_KEY);

		this.temperature = temperature;
		if (variableValue != null) {
			apiKey = variableValue;
		} else {
			System.err.println("Environment variable " + OPENAI_API_KEY + " not found.");
		}
	}

	@Override
	public String sendPrompt(String systemMessage, String requestString) {
		if (apiKey == null) {
			System.err.println("Environment variable " + OPENAI_API_KEY + " not found.");
			return "";
		}

		try {
			URL url = new URL(OPENAI_API_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// System.err.println("Establishing connection");
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Bearer " + apiKey);

			connection.setDoOutput(true);

			ChatRequest request = new ChatRequest(systemMessage, requestString);
			request.setTemperature(temperature);

			//System.err.println("Sending prompt");
			//System.err.println(requestString);

			String payload = new ObjectMapper().writeValueAsString(request);
			try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
				outputStream.write(payload.getBytes());
			}

			// Get the response
			int responseCode = connection.getResponseCode();
			// System.err.println("Get response code");

			if (responseCode == HttpURLConnection.HTTP_OK) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					StringBuilder response = new StringBuilder();
					String line;

					while ((line = in.readLine()) != null) {
						response.append(line);
					}

					// Parse the JSON response using Jackson
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonResponse = objectMapper.readTree(response.toString());

					String reply = jsonResponse.get("choices").get(0).get("message").get("content").asText();
					return (GptCodeExtractor.extractCode(reply));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException npe) {
					npe.printStackTrace();
				}
			} else {
				System.err.println("Error: " + responseCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}