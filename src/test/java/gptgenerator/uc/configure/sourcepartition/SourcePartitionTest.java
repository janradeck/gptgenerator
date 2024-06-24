package gptgenerator.uc.configure.sourcepartition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SourcePartitionTest {
	private final static String DEST = "destDirAbs";
	@Test
	void testInstallCfg() {
		SourcePartitionModel cfg = new SourcePartitionModel();
		cfg.setDestDirAbs(DEST);
		Assertions.assertEquals(DEST, cfg.getDestDirAbs());
	}

}
