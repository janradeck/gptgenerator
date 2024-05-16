package gptgenerator.services;

public class CacheEntry {
	private String filename;
    private long lastModified;
    private String content;
    private boolean empty = true;

    /**
     * @param filename
     * @param lastModified
     * @param content
     */
    public CacheEntry(String filename, long lastModified, String content) {
        this.filename = filename;
        this.lastModified = lastModified;
        this.content = content;
        this.empty = false; 
    }
    
    /**
     * An empty entry, to mark a non existent file
     */
    public CacheEntry() {
    	empty = true;
    }

	/**
	 * CopyConstructor
	 * @param entry
	 */
	public CacheEntry(CacheEntry entry) {
        this.filename = entry.filename;
        this.lastModified = entry.lastModified;
        this.content = entry.content;
        this.empty = entry.empty;
	}
	
	/**
	 * Return an "empty" entry.
	 * @param filename
	 * @return
	 */
	public static CacheEntry emptyEntry(String filename) {
    	CacheEntry emptyEntry = new CacheEntry();
    	emptyEntry.filename = filename;
    	return emptyEntry;
	}
	
	public String getFilename() {
		return filename;
	}

	public long getLastModified() {
		return lastModified;
	}

	public String getContent() {
		return content;
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CacheEntry otherEntry = (CacheEntry) obj;
        return filename.equals(otherEntry.filename) &&
                (lastModified == otherEntry.lastModified) &&
                content.equals(otherEntry.content) &&
                (empty == otherEntry.empty);
    }   
	
	@Override
	public String toString() {
		return
		        this.filename + " " + 
		        this.lastModified + " " +
		        this.content; 
	}

}

