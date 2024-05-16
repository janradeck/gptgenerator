package gptgenerator.uc.processing;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gptgenerator.services.FileService;
import gptgenerator.uc.processing.o3install.FileInstaller;

/**
 * Tests for ProcessManager on a top level
 */
class ProcessManagerTest {

	private final static String testBase = "./src/test/resources/processmanager/test";
	
	@BeforeAll
	public static void setup() {
		File src = new File("./src/test/resources/processmanager/init");
		File dst = new File(testBase);
		
		FileService.clearDirectory(testBase);
		try {
			FileInstaller.copyDirectory(src, dst);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * SRC_MISSING<br>
	 * BEFORE<br>
	 * Result.java exists in Input(Prev) but is missing from Input(Cur)<br>
	 * AFTER<BR>
	 * The file does not exist in<br>
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>Install
	 * </ul>
	 */
	@Test
	void test01InputCurSrcMissing() {
		String testCaseBase = testBase + File.separator +"01";
		String testFilename = "java/Result.java";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);
		String inputCurDirectory = ts.inputCurDirectory();
		String inputPrevDirectory = ts.inputPrevDirectory();
		String mergeCurDirectory = ts.mergeCurDirectory();
		String mergePrevDirectory = ts.mergePrevDirectory();
		String installDirectory = ts.getDestDirAbs(testFilename);
		
		String inputPrevFile = ts.inputPrevFile(testFilename); 
				
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(inputPrevFile));
		Assertions.assertEquals(0, FileService.recursiveFileCount(inputCurDirectory));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());
		
		// Postconditions
		Assertions.assertEquals(0, FileService.recursiveFileCount(inputPrevDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(mergeCurDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(mergePrevDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(installDirectory));
	}

	/**
	 * CHANGED<br>
	 * BEFORE<br>
	 * Result.java exists in Input(Prev) with version [VA] and in Input(Cur) with version [VB], with [VA] != [VB]<br>
	 * AFTER<br>
	 * Result.java exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Install
	 * </ul>
	 * 
	 */
	@Test
	void test02InputCurChanged() {
		String testCaseBase = testBase + File.separator +"02";		
		String testFilename = "java/Result.java";

		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);

		String inputPrevFile = ts.inputPrevFile(testFilename);
		String inputCurFile = ts.inputCurFile(testFilename);
		String mergeCurFile = ts.mergeCurFile(testFilename);
		String installFile = ts.installFile(testFilename);		
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(inputCurFile));
		Assertions.assertTrue(FileService.fileExists(inputPrevFile));
		Assertions.assertFalse(FileService.filesIdentical(inputCurFile, inputPrevFile));

		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, inputPrevFile));
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, mergeCurFile));
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, installFile));
	}

	/**
	 * DST_MISSING<br>
	 * BEFORE<br>
	 * Result.java exists in Input(Cur) but not in Input(Prev)<br>
	 * AFTER<br>
	 * Result.java exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Install
	 * </ul>
	 * 
	 */
	@Test
	void test03InputCurDstMissing() {
		String testCaseBase = testBase + File.separator +"03";		
		String testFilename = "java/Result.java";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);
		String inputPrevFile = ts.inputPrevFile(testFilename);
		String inputCurFile = ts.inputCurFile(testFilename);
		String mergeCurFile = ts.mergeCurFile(testFilename);
		String installFile = ts.installFile(testFilename);		
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(inputCurFile));
		Assertions.assertFalse(FileService.fileExists(inputPrevFile));
		
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());
		
		// Postconditions
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, inputPrevFile));
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, mergeCurFile));
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, installFile));
	}

	/**
	 * UNCHANGED<br>
	 * BEFORE<br>
	 * Result.java exists in Input(Cur) and in Input(Prev). They are identical<br>
	 * AFTER<br>
	 * Result.java exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Install
	 * </ul>
	 * 
	 */
	@Test
	void test04InputCurUnchanged() {
		String testCaseBase = testBase + File.separator +"04";		
		String testFilename = "java/Result.java";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);
		String inputPrevFile = ts.inputPrevFile(testFilename);
		String inputCurFile = ts.inputCurFile(testFilename);
		String mergeCurFile = ts.mergeCurFile(testFilename);
		String installFile = ts.installFile(testFilename);		
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(inputCurFile));
		Assertions.assertTrue(FileService.fileExists(inputPrevFile));
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, inputPrevFile));

		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());
		
		// Postconditions
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, inputPrevFile));
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, mergeCurFile));
		Assertions.assertTrue(FileService.filesIdentical(inputCurFile, installFile));
	}

	/**
	 * SRC_MISSING<br>
	 * BEFORE<br>
	 * Result.java exists in Input(Prev) but is missing from Input(Cur)<br>
	 * Result.java.fragment exists in Input(Prev) but is missing from Input(Cur)<br>
	 * AFTER<BR>
	 * The files do not exist in<br>
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>Install
	 * </ul>
	 */
	@Test
	void test05InputCurSrcMissing() {
		String testCaseBase = testBase + File.separator +"05";		
		String templateFilename = "java/Result.java";
		String fragmentFilename = "java/Result.java.fragment";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);

		String inputCurDirectory = ts.inputCurDirectory();
		String inputPrevDirectory = ts.inputPrevDirectory();
		String mergeCurDirectory = ts.mergeCurDirectory();
		String mergePrevDirectory = ts.mergePrevDirectory();
		String installDirectory = ts.installDirectory(templateFilename);
		
		String templateInputPrev = ts.inputPrevFile(templateFilename);
		String fragmentInputPrev = ts.inputPrevFile(fragmentFilename);
		
		// Preconditions
		Assertions.assertEquals(0, FileService.recursiveFileCount(inputCurDirectory));
		Assertions.assertTrue(FileService.fileExists(templateInputPrev));
		Assertions.assertTrue(FileService.fileExists(fragmentInputPrev));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertEquals(0, FileService.recursiveFileCount(inputPrevDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(mergeCurDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(mergePrevDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(installDirectory));		
	}

	/**
	 * CHANGED<br>
	 * BEFORE<br>
	 * Result.java exists in Input(Prev) with version [VA] and in Input(Cur) with version [VB], with [VA] != [VB]<br>
	 * Result.java.fragment exists in Input(Prev) with version [VA] and in Input(Cur) with version [VB], with [VA] != [VB]<br>
	 * AFTER<br>
	 * Result.java exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>Install
	 * </ul>
	 * Result.java.fragment exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>NOT in Install
	 * </ul>
	 * 
	 */
	@Test
	void test06InputCurChanged() {
		String testCaseBase = testBase + File.separator +"06";		
		String templateFilename = "java/Result.java";
		String fragmentFilename = "java/Result.java.fragment";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);

		String templateInputCur = ts.inputCurFile(templateFilename);
		String templateInputPrev = ts.inputPrevFile(templateFilename);
		String fragmentInputPrev = ts.inputPrevFile(fragmentFilename);
		String fragmentInputCur = ts.inputCurFile(fragmentFilename);
		String fragmentInstall = ts.installFile(fragmentFilename);
		
		String templateMergeCur = ts.mergeCurFile(templateFilename);
		String fragmentMergeCur = ts.mergeCurFile(fragmentFilename);
		String templateInstall = ts.installFile(templateFilename);
		
		// Preparation
		FileService.clearDirectory(ts.mergePrevDirectory());
		FileService.clearDirectory(ts.installDirectory(templateFilename));
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(templateInputCur));
		Assertions.assertTrue(FileService.fileExists(fragmentInputCur));
		
		Assertions.assertTrue(FileService.fileExists(templateInputPrev));
		Assertions.assertTrue(FileService.fileExists(fragmentInputPrev));
		Assertions.assertFalse(FileService.filesIdentical(templateInputCur, templateInputPrev));
		Assertions.assertFalse(FileService.filesIdentical(fragmentInputCur, fragmentInputPrev));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertTrue(FileService.filesIdentical(templateInputCur, templateInputPrev));
		Assertions.assertTrue(FileService.fileExists(templateMergeCur));
		Assertions.assertTrue(FileService.fileExists(templateInstall));
		
		Assertions.assertTrue(FileService.filesIdentical(fragmentInputCur, fragmentInputPrev));
		Assertions.assertTrue(FileService.filesIdentical(fragmentInputCur, fragmentMergeCur));
		Assertions.assertFalse(FileService.fileExists(fragmentInstall));
	}

	/**
	 * DST_MISSING<br>
	 * BEFORE<br>
	 * Result.java exists in Input(Cur) but not in Input(Prev)<br>
	 * Result.java.fragment exists in Input(Cur) but not in Input(Prev)<br>
	 * AFTER<br>
	 * Result.java exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Install
	 * </ul>
	 * Result.java.prompt exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>NOT in Install
	 * </ul>
	 * 
	 */
	@Test
	void test07InputCurDstMissing() {
		String testCaseBase = testBase + File.separator +"07";		
		String templateFilename = "java/Result.java";
		String fragmentFilename = "java/Result.java.fragment";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);

		String templateInputCur = ts.inputCurFile(templateFilename);
		String templateInputPrev = ts.inputPrevFile(templateFilename);
		String fragmentInputPrev = ts.inputPrevFile(fragmentFilename);
		String fragmentInputCur = ts.inputCurFile(fragmentFilename);
		String fragmentInstall = ts.installFile(fragmentFilename);
		
		String templateMergeCur = ts.mergeCurFile(templateFilename);
		String fragmentMergeCur = ts.mergeCurFile(fragmentFilename);
		String templateInstall = ts.installFile(templateFilename);
		
		// Preparation
		FileService.clearDirectory(ts.mergePrevDirectory());
		FileService.clearDirectory(ts.installDirectory(templateFilename));
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(templateInputCur));
		Assertions.assertFalse(FileService.fileExists(templateInputPrev));
		
		Assertions.assertTrue(FileService.fileExists(fragmentInputCur));
		Assertions.assertFalse(FileService.fileExists(fragmentInputPrev));
		
		// Processing
		ProcessManager.processAll( ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertTrue(FileService.filesIdentical(templateInputCur, templateInputPrev));
		Assertions.assertTrue(FileService.fileExists(templateMergeCur));
		Assertions.assertTrue(FileService.fileExists(templateInstall));
		
		Assertions.assertTrue(FileService.filesIdentical(fragmentInputCur, fragmentInputPrev));
		Assertions.assertTrue(FileService.filesIdentical(fragmentInputCur, fragmentMergeCur));
		Assertions.assertFalse(FileService.fileExists(fragmentInstall));
	}

	/**
	 * UNCHANGED<br>
	 * BEFORE<br>
	 * Result.java exists in Input(Cur) and in Input(Prev). They are identical<br>
	 * Result.java.fragment exists in Input(Cur) and in Input(Prev). They are identical<br>
	 * AFTER<br>
	 * Result.java exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>Install
	 * </ul>
	 * Result.java.fragment exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>NOT in Install
	 * </ul>
	 */
	@Test
	void test08InputCurUnchanged() {
		String testCaseBase = testBase + File.separator + "08";		
		String templateFilename = "java/Result.java";
		String fragmentFilename = "java/Result.java.fragment";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);

		String templateInputCur = ts.inputCurFile(templateFilename);
		String templateInputPrev = ts.inputPrevFile(templateFilename);
		String fragmentInputPrev = ts.inputPrevFile(fragmentFilename);
		String fragmentInputCur = ts.inputCurFile(fragmentFilename);
		String fragmentInstall = ts.installFile(fragmentFilename);
		
		String templateMergeCur = ts.mergeCurFile(templateFilename);
		String fragmentMergeCur = ts.mergeCurFile(fragmentFilename);
		String templateInstall = ts.installFile(templateFilename);
		
		// Preparation
		FileService.clearDirectory(ts.mergePrevDirectory());
		FileService.clearDirectory(ts.installDirectory(templateFilename));
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(templateInputCur));
		Assertions.assertTrue(FileService.fileExists(templateInputPrev));
		Assertions.assertTrue(FileService.filesIdentical(templateInputCur, templateInputPrev));
		
		Assertions.assertTrue(FileService.fileExists(fragmentInputCur));
		Assertions.assertTrue(FileService.fileExists(fragmentInputPrev));
		Assertions.assertTrue(FileService.filesIdentical(fragmentInputCur, fragmentInputPrev));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertTrue(FileService.filesIdentical(templateInputCur, templateInputPrev));
		Assertions.assertTrue(FileService.fileExists(templateMergeCur));
		Assertions.assertTrue(FileService.fileExists(templateInstall));
		
		Assertions.assertTrue(FileService.filesIdentical(fragmentInputCur, fragmentInputPrev));
		Assertions.assertTrue(FileService.filesIdentical(fragmentInputCur, fragmentMergeCur));
		Assertions.assertFalse(FileService.fileExists(fragmentInstall));
	}
	
	/**
	 * SRC_MISSING<br>
	 * BEFORE<br>
	 * Result.java.prompt exists in Input(Prev) but is missing from Input(Cur)<br>
	 * Result.java exists in Merge(Cur)
	 * AFTER<BR>
	 * Result.java.prompt does not exist in<br>
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>Install
	 * </ul>
	 * <br>
	 * Result.java
	 * <ul>
	 * <li>does not exist in Merge(Cur)
	 * <li>exists in Merge(Prev)
	 * <li>does not exist in Install
	 * </ul>
	 */
	@Test
	void test09InputCurSrcMissing() {
		String testCaseBase = testBase + File.separator + "09";		
		String promptFilename = "java/Result.java.prompt";
		String replyFilename = "java/Result.java";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);
		
		String inputCurDirectory = ts.inputCurDirectory();
		String inputPrevDirectory = ts.inputPrevDirectory();
		String mergeCurDirectory = ts.mergeCurDirectory();
		String mergePrevDirectory = ts.mergePrevDirectory();
		String installDirectory = ts.installDirectory(promptFilename);
		
		String promptInputPrev = ts.inputPrevFile(promptFilename);
		String replyMergeCur = ts.mergeCurFile(replyFilename);
		String replyMergePrev = ts.mergePrevFile(replyFilename);
		
		// Preconditions
		Assertions.assertEquals(0, FileService.recursiveFileCount(inputCurDirectory));
		Assertions.assertEquals(1, FileService.recursiveFileCount(mergeCurDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(mergePrevDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(installDirectory));		
		
		Assertions.assertTrue(FileService.fileExists(promptInputPrev));
		Assertions.assertTrue(FileService.fileExists(replyMergeCur));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertEquals(0, FileService.recursiveFileCount(inputPrevDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(mergeCurDirectory));
		Assertions.assertEquals(1, FileService.recursiveFileCount(mergePrevDirectory));
		Assertions.assertEquals(0, FileService.recursiveFileCount(installDirectory));
		
		Assertions.assertTrue(FileService.fileExists(replyMergePrev));
	}

	/**
	 * CHANGED<br>
	 * BEFORE<br>
	 * Result.java.prompt exists in Input(Prev) with version [VA] and in Input(Cur) with version [VB], with [VA] != [VB]<br>
	 * AFTER<br>
	 * <code>Result.java.prompt</code> exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * </ul>
	 * File <code>Result.java</code> exists in
	 * <ul>
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>Install
	 * </ul>
	 * 
	 */
	@Test
	void test10InputCurChanged() {
		String testCaseBase = testBase + File.separator + "10";		
		String replyFilename = "java/Result.java";
		String promptFilename = "java/Result.java.prompt";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);

		String replyInputCur = ts.inputCurFile(replyFilename);
		String replyInputPrev = ts.inputPrevFile(replyFilename);
		String promptInputPrev = ts.inputPrevFile(promptFilename);
		String promptInputCur = ts.inputCurFile(promptFilename);
		String promptInstall = ts.installFile(promptFilename);
		
		String replyMergeCur = ts.mergeCurFile(replyFilename);
		String replyMergePrev = ts.mergePrevFile(replyFilename);		
		String promptMergeCur = ts.mergeCurFile(promptFilename);
		String promptMergePrev = ts.mergePrevFile(promptFilename);		
		String replyInstall = ts.installFile(replyFilename);
		
		// Preparation
		FileService.clearDirectory(ts.mergePrevDirectory());
		FileService.clearDirectory(ts.installDirectory(replyFilename));
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(promptInputCur));
		Assertions.assertTrue(FileService.fileExists(promptInputPrev));
		Assertions.assertFalse(FileService.filesIdentical(promptInputCur, promptInputPrev));
		
		Assertions.assertFalse(FileService.fileExists(replyInputCur));
		Assertions.assertFalse(FileService.fileExists(replyInputPrev));
		Assertions.assertFalse(FileService.fileExists(replyMergeCur));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());
		
		// Postconditions
		Assertions.assertTrue(FileService.fileExists(promptInputPrev));
		Assertions.assertTrue(FileService.fileExists(promptMergeCur));
		Assertions.assertTrue(FileService.fileExists(promptMergePrev));
		Assertions.assertFalse(FileService.fileExists(promptInstall));
		
		Assertions.assertTrue(FileService.fileExists(replyMergeCur));
		Assertions.assertFalse(FileService.fileExists(replyMergePrev));
		Assertions.assertTrue(FileService.fileExists(replyInstall));
	}

	/**
	 * DST_MISSING<br>
	 * BEFORE<br>
	 * Result.java.prompt exists in Input(Cur) but not in Input(Prev)<br>
	 * AFTER<br>
	 * Result.java.prompt exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>NOT in Install
	 * </ul>
	 * Result.java exists in
	 * <ul>
	 * <li>Merge(Cur)
	 * <li>Install
	 * </ul>
	 * 
	 */
	@Test
	void test11InputCurDstMissing() {
		String testCaseBase = testBase + File.separator + "11";		
		String replyFilename = "java/Result.java";
		String promptFilename = "java/Result.java.prompt";
		
		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);
		String replyInputCur = ts.inputCurFile(replyFilename);
		String replyInputPrev = ts.inputPrevFile(replyFilename);
		String promptInputPrev = ts.inputPrevFile(promptFilename);
		String promptInputCur = ts.inputCurFile(promptFilename);
		String promptInstall = ts.installFile(promptFilename);
		
		String replyMergeCur = ts.mergeCurFile(replyFilename);
		String replyMergePrev = ts.mergePrevFile(replyFilename);		
		String promptMergeCur = ts.mergeCurFile(promptFilename);
		String promptMergePrev = ts.mergePrevFile(promptFilename);		
		String replyInstall = ts.installFile(replyFilename);
		
		// Preparation
		FileService.clearDirectory(ts.mergePrevDirectory());
		FileService.clearDirectory(ts.installDirectory(replyFilename));
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(promptInputCur));
		Assertions.assertFalse(FileService.fileExists(promptInputPrev));
		
		Assertions.assertFalse(FileService.fileExists(replyInputCur));
		Assertions.assertFalse(FileService.fileExists(replyInputPrev));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertTrue(FileService.fileExists(promptInputPrev));
		Assertions.assertTrue(FileService.fileExists(promptMergeCur));
		Assertions.assertTrue(FileService.fileExists(promptMergePrev));
		Assertions.assertFalse(FileService.fileExists(promptInstall));
		
		Assertions.assertTrue(FileService.fileExists(replyMergeCur));
		// Assertions.assertTrue(FileService.fileExists(replyMergePrev));
		Assertions.assertTrue(FileService.fileExists(replyInstall));		
	}

	/**
	 * UNCHANGED<br>
	 * BEFORE<br>
	 * Result.java.prompt exists in Input(Cur) and in Input(Prev). They are identical<br>
	 * AFTER<br>
	 * Result.java.prompt exists in
	 * <ul>
	 * <li>Input(Prev)
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * <li>NOT in Install
	 * </ul>
	 * Result.java exists in
	 * <ul>
	 * <li>Merge(Cur)
	 * <li>Install
	 * </ul>
	 */
	@Test
	void test12InputCurUnchanged() {
		String testCaseBase = testBase + File.separator + "12";		

		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);

		String replyFilename = "java/Result.java";
		String promptFilename = "java/Result.java.prompt";
		
		String replyInputCur = ts.inputCurFile(replyFilename);
		String replyInputPrev = ts.inputPrevFile(replyFilename);
		String promptInputPrev = ts.inputPrevFile(promptFilename);
		String promptInputCur = ts.inputCurFile(promptFilename);
		String promptInstall = ts.installFile(promptFilename);
		
		String replyMergeCur = ts.mergeCurFile(replyFilename);
		String replyMergePrev = ts.mergePrevFile(replyFilename);		
		String promptMergeCur = ts.mergeCurFile(promptFilename);
		String promptMergePrev = ts.mergePrevFile(promptFilename);		
		String replyInstall = ts.installFile(replyFilename);
		
		// Preparation
		FileService.clearDirectory(ts.mergePrevDirectory());
		FileService.clearDirectory(ts.installDirectory(replyFilename));
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(promptInputCur));
		Assertions.assertTrue(FileService.fileExists(promptInputPrev));
		Assertions.assertTrue(FileService.filesIdentical(promptInputCur, promptInputPrev));
		
		Assertions.assertFalse(FileService.fileExists(replyInputCur));
		Assertions.assertFalse(FileService.fileExists(replyInputPrev));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertTrue(FileService.fileExists(promptInputPrev));
		Assertions.assertTrue(FileService.fileExists(promptMergeCur));
		Assertions.assertTrue(FileService.fileExists(promptMergePrev));
		Assertions.assertFalse(FileService.fileExists(promptInstall));

		Assertions.assertTrue(FileService.fileExists(replyMergeCur));
	//	Assertions.assertTrue(FileService.fileExists(replyMergePrev));
		Assertions.assertTrue(FileService.fileExists(replyInstall));
	}

	/**
	 * CHANGED<br>
	 * BEFORE<br>
	 * addongetdto.model.ts exists in Input(Cur)<br>
	 * addongetdto-check.model.fragment.prompt exists in Input(Cur)<br>
	 * AFTER<br>
	 * addongetdto.model.ts exists in
	 * <ul>
	 * <li>Merge(Cur)
	 * <li>Install
	 * </ul>
	 * addongetdto-check.model.fragment.prompt exists in
	 * <ul>
	 * <li>Merge(Cur)
	 * <li>Merge(Prev)
	 * </ul>
	 * addongetdto-check.model.fragment exists in Merge(Cur)
	 */
	@Test
	void test13IncludeUnMergedAndPrompt() {
		String testCaseBase = testBase + File.separator + "13";		

		ProcessManagerTestSetup ts = new ProcessManagerTestSetup(testCaseBase);

		String sourceFilename  = "js/addongetdto.model.ts";
		String replyFilename  = "js/addongetdto-check.model.fragment";
		String promptFilename = "js/addongetdto-check.model.fragment.prompt";

		String sourceInputCur = ts.inputCurFile(sourceFilename);
		String sourceMergeCur = ts.mergeCurFile(sourceFilename);
		String sourceInstall = ts.installFile(sourceFilename);
		
		String promptInputCur = ts.inputCurFile(promptFilename);
		String promptMergeCur = ts.mergeCurFile(promptFilename);
		String promptMergePrev = ts.mergePrevFile(promptFilename);

		String replyMergeCur = ts.mergeCurFile(replyFilename);

		String inputPrevDirectory = ts.inputPrevDirectory();
		String mergeCurDirectory = ts.mergeCurDirectory();
		String mergePrevDirectory = ts.mergePrevDirectory();
		String installDirectory = ts.installDirectory(promptFilename);
		
		
		// Preparation
		FileService.clearDirectory(ts.mergeCurDirectory());
		FileService.clearDirectory(ts.mergePrevDirectory());
		FileService.clearDirectory(ts.installDirectory(replyFilename));
		
		// Preconditions
		Assertions.assertTrue(FileService.fileExists(sourceInputCur));
		Assertions.assertTrue(FileService.fileExists(promptInputCur));
		
		// Processing
		ProcessManager.processAll(ts.getConfigurationController(), ts.getMergeResultController(), ts.getPromptResultController(), ts.getFilestateController());

		// Postconditions
		Assertions.assertTrue(FileService.fileExists(promptMergeCur));
		Assertions.assertTrue(FileService.fileExists(promptMergePrev));
		Assertions.assertTrue(FileService.fileExists(replyMergeCur));

		Assertions.assertTrue(FileService.fileExists(sourceMergeCur));
		Assertions.assertTrue(FileService.fileExists(sourceInstall));
		
		Assertions.assertEquals(2, FileService.recursiveFileCount(inputPrevDirectory));
		Assertions.assertEquals(3, FileService.recursiveFileCount(mergeCurDirectory));
		Assertions.assertEquals(1, FileService.recursiveFileCount(mergePrevDirectory));
		Assertions.assertEquals(1, FileService.recursiveFileCount(installDirectory));
		
	}
	
}
