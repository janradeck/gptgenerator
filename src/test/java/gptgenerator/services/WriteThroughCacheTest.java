package gptgenerator.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WriteThroughCacheTest {
	private static final String inputDirectory = "./src/test/resources/cache/input";
	private static final String outputDirectory = "./src/test/resources/cache/output";

	@Test
	void o01_readTest() {
		WriteThroughCache cache = new WriteThroughCache(inputDirectory);
		Assertions.assertEquals(4, cache.entries.size());
	}
	
	@Test
	void o02_writeTest() {
		WriteThroughCache inputCache = new WriteThroughCache(inputDirectory);
		FileService.clearAndDeleteDirectory(outputDirectory);
		Assertions.assertEquals(4, inputCache.entries.size());
		WriteThroughCache outputCache = new WriteThroughCache(outputDirectory);
		Assertions.assertEquals(0, outputCache.entries.size());
		
		for (CacheEntry cur: inputCache.entries.values()) {
			outputCache.saveAndAdd(cur);
		}
		Assertions.assertEquals(4, outputCache.entries.size());
	}
	
	@Test
	void o03_filterTest() {
		WriteThroughCache inputCache = new WriteThroughCache(inputDirectory);
		FileService.clearAndDeleteDirectory(outputDirectory);
		Assertions.assertEquals(4, inputCache.entries.size());
		WriteThroughCache outputCache = new WriteThroughCache(outputDirectory);
		Assertions.assertEquals(0, outputCache.entries.size());
		
		WriteThroughCache promptCache = inputCache.passFilter("\\.prompt$");
		
		Assertions.assertEquals(2, promptCache.entries.size());
		
	}
	
	

}
