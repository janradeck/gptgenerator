package gptgenerator.uc.configure;

/**
 * Die Anzahl paralleler Threads für die Verarbeitung der einzelnen Prompts
 */
public class ProcessingThreadCount {
	public static final int MIN = 1;
	private static final int DEFAULT = 50;
	public static final int MAX = 200;

	private int count = DEFAULT;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static boolean validate(int count) {
		return (MIN <= count) && (count <= MAX);
	}

	public static boolean validateString(String numberText) {
		try {
			int number = Integer.parseInt(numberText);
			return validate(number);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Setzt den Wert neu, wenn <code>numberText</code> eine gültige Zahl innerhalb des gültigen Bereichs ist
	 * @param numberText
	 */
	public void setCount(String numberText) {
		try {
			int number = Integer.parseInt(numberText);
			if (validate(number)) {
				count = number;
			}
		} catch (NumberFormatException e) {
		}
	}

}
