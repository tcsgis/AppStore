package com.aaa.db;

public class DownloadState {
	public static final byte NONE = 1;
	public static final byte DOWNLOADING = 2;
	public static final byte PAUSE = 4;
	public static final byte DOWNLOADED = 8;
	public static final byte INSTALLED = 16;
}
