package gptgenerator.uc.configure.gpt;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a temperature value for an LLM chat.<br>
 * The temperature value must be within the range of MIN and MAX.
 */
public class ChatTemperature {
	public static final double MIN = 0.0;
	private static final double DEF_TEMPERATURE = 1.0;
	public static final double MAX = 2.0;
	
	private Double temperature;

	public ChatTemperature() {
		temperature = DEF_TEMPERATURE;
	}

	public ChatTemperature(ChatTemperature source) {
		this.temperature = source.temperature;
	}

	public static boolean validateString(String temperatureString) {
        try {
            Double parsedTemperature = Double.valueOf(Float.parseFloat(temperatureString)); 
            return validate(parsedTemperature);
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	public static boolean validate(Double temperature) {
		return (temperature >= MIN && temperature <= MAX);
	}

    public Double getTemperature() {
		return temperature;
	}

	/**
	 * Sets the new value if it is valid
	 * @param newTemperatur
	 */
	public void setTemperature(Double newTemperatur) {
		if (validate(newTemperatur)) {
			this.temperature = newTemperatur;
		}
	}

	public void setTemperature(String temperatureString) {
        try {
            Double parsedTemperature = Double.valueOf(Float.parseFloat(temperatureString)); 
            if (validate(parsedTemperature)) {
                temperature = parsedTemperature;
            }
        } catch (NumberFormatException e) {
        }
	}
	
	/**
	 * The temperature as String, with "." as decimal separator
	 * @return
	 */
	@JsonIgnore
	public String getTemperatureString() {
		return String.format(Locale.US, "%.1f", temperature);
	}

	public static String validationMessage() {
		return String.format(Locale.US, "Error! Valid values: %.1f <= temperature <= %.1f", ChatTemperature.MIN, ChatTemperature.MAX);		
	}
}
