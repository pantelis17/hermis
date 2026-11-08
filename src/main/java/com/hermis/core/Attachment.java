package com.hermis.core;

public record Attachment(String url, String filename, String mimeType, long size) {
}
