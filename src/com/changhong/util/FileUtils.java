package com.changhong.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * @see <a href="https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java">FileUtils.java</a> for full version
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public final class FileUtils {
	public FileUtils() {} //private constructor to enforce Singleton pattern

	/** TAG for log messages. */
	static final String TAG = "FileUtils";
	private static final boolean DEBUG = false; // Set to true to enable logging

	/**
	 * @return Whether the URI is a local one.
	 */
	public static boolean isLocal(String url) {
		if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
			return true;
		}
		return false;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is {@link LocalStorageProvider}.
	 * @author paulburke
	 */
	public static boolean isLocalStorageDocument(Uri uri) {
		return "com.ianhanniballake.localstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 * @author paulburke
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 * @author paulburke
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 * @author paulburke
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 * @author paulburke
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = MediaStore.MediaColumns.DATA;
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				if (DEBUG)
					DatabaseUtils.dumpCursor(cursor);

				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.<br>
	 * <br>
	 * Callers should check whether the path is local before assuming it
	 * represents a local file.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @see #isLocal(String)
	 * @see #getFile(android.content.Context, android.net.Uri)
	 * @author paulburke
	 */
	public static String getPath(final Context context, final Uri uri) {
		if (uri != null) {
			if (DEBUG)
				Log.d(TAG + " File -",
						"Authority: " + uri.getAuthority() +
								", Fragment: " + uri.getFragment() +
								", Port: " + uri.getPort() +
								", Query: " + uri.getQuery() +
								", Scheme: " + uri.getScheme() +
								", Host: " + uri.getHost() +
								", Segments: " + uri.getPathSegments().toString()
				);

			final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
			//FIXME:对文件名直传且存在#的情况有bug, 强行修复
			//不使用fromParts, 不支持getAuthority(), 无法查询
			final Uri uri2;
			if (uri.getFragment() == null)
				uri2 = uri;
			else
				uri2 = Uri.parse(uri.toString().replace("#", "%23"));

			// DocumentProvider
			if (isKitKat && DocumentsContract.isDocumentUri(context, uri2)) {
				if (isLocalStorageDocument(uri2)) {
					// LocalStorageProvider
					// The path is the id
					return DocumentsContract.getDocumentId(uri2);
				}
				else if (isExternalStorageDocument(uri2)) {
					// ExternalStorageProvider
					final String docId = DocumentsContract.getDocumentId(uri2);
					final String[] split = docId.split(":");
					final String type = split[0];

					if ("primary".equalsIgnoreCase(type)) {
						return Environment.getExternalStorageDirectory() + "/" + split[1];
					} else {
						// TODO handle non-primary volumes
						return Environment.getExternalStorageDirectory() + "/" + split[1];
					}

				} else if (isDownloadsDocument(uri2)) {
					// DownloadsProvider

					final String id = DocumentsContract.getDocumentId(uri2);
					final Uri contentUri = ContentUris.withAppendedId(
							Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

					return getDataColumn(context, contentUri, null, null);
				} else if (isMediaDocument(uri2)) {
					// MediaProvider
					final String docId = DocumentsContract.getDocumentId(uri2);
					final String[] split = docId.split(":");
					final String type = split[0];

					Uri contentUri = null;
					if ("image".equals(type)) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(type)) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(type)) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}

					final String selection = BaseColumns._ID + "=?";
					final String[] selectionArgs = new String[]{
							split[1]
					};

					return getDataColumn(context, contentUri, selection, selectionArgs);
				}
			} else if ("content".equalsIgnoreCase(uri2.getScheme())) {
				// MediaStore (and general)

				// Return the remote address
				if (isGooglePhotosUri(uri2))
					return uri.getLastPathSegment();

				return getDataColumn(context, uri2, null, null);
			} else if ("file".equalsIgnoreCase(uri2.getScheme())) {
				// File
				return uri2.getPath();
			}
		}

		return null;
	}

	/**
	 * Convert Uri into File, if possible.
	 *
	 * @return file A local file that the Uri was pointing to, or null if the
	 *         Uri is unsupported or pointed to a remote resource.
	 * @see #getPath(android.content.Context, android.net.Uri)
	 * @author paulburke
	 */
	public static File getFile(Context context, Uri uri) {
		if (uri != null) {
			String path = getPath(context, uri);
			if (path != null && isLocal(path)) {
				return new File(path);
			}
		}
		return null;
	}

}
