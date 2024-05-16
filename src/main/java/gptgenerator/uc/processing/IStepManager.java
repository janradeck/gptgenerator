package gptgenerator.uc.processing;

/**
 * Verarbeitet die Dateien in der Warteschlange.<br>
 * Eine Datei in der Warteschlange wird nur verarbeitet, wenn alle ihre Include-Dateien vorhanden sind.<br>
 * Daher kann eine Datei mehrfach geprüft werden.  
 */
public interface IStepManager {
	/**
	 * Are files ready to be processed?
	 * @return
	 */
	boolean filesReady();
	/**
	 * Ist die Warteschlange leer?
	 * @return
	 */
	boolean queueEmpty();
	/**
	 * Verarbeite die Dateien in der Warteschlange.
	 */
	void process();
}
