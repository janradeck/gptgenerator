package gptgenerator.services;

import java.io.IOException;

public class ExecutionService {

	public static int executeCommandline(String commandLine) {
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		String command = isWindows? String.format("cmd.exe /c %s", commandLine) : String.format("/bin/sh -c %s", commandLine);
		int result = -1;
		try {
            // Execute the command using Runtime.getRuntime().exec()
            Process process = Runtime.getRuntime().exec(command);

            // Wait for the process to finish
            result = process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
		return result;
	}
}
