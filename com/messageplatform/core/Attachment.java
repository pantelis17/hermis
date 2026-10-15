package com.messageplatform.core;

public class Attachment {
    private final String url;
    private final String filename;
    private final String mimeType;
    private final long size;

    public Attachment(String url, String filename, String mimeType, long size) {
        this.url = url;
        this.filename = filename;
        this.mimeType = mimeType;
        this.size = size;
    }

    public String getUrl() { return url; }
    public String getFilename() { return filename; }
    public String getMimeType() { return mimeType; }
    public long getSize() { return size; }
}
