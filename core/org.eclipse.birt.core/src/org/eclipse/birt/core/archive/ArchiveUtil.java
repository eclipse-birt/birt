/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.eclipse.birt.core.archive.compound.ArchiveFileV3;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class ArchiveUtil {

	private static final Logger logger = Logger.getLogger(ArchiveUtil.class.getName());

	// Stream transformation flat used during copy to indicate that the stream is to
	// be copied as is
	private static final String IDENTITY_MAPPING = "I";

	// Stream transformation flag used during copy to indicate that the specified
	// stream should not be copied to target.
	private static final String DELETION_MAPPING = "D";

	// We need this because the report document should be platform neutral. Here
	// we define the neutral as unix separator.
	public static final char UNIX_SEPARATOR_CHAR = '/';
	public static final String UNIX_SEPERATOR = "/";

	/**
	 * To support file/folder with same name, use the FILE_EXTENSION for files.
	 */
	public final static String FILE_EXTENSION = ".content";

	/**
	 * @param rootPath     - the absolute path of the root folder.
	 * @param relativePath - the relative path.
	 * @return the absolute path which concats rootPath and relativePath. The
	 *         returned absolute path can be used directly to locate the file.
	 */
	public static String getFolderPath(final String rootPath, final String entryName) {
		assert (rootPath != null && entryName != null);
		String path = getFullPath(rootPath, getFilePath(entryName));
		if (path.charAt(path.length() - 1) != '/') {
			path = path + '/';
		}
		return path;
	}

	public static String getFilePath(final String rootPath, final String entryName) {
		assert (rootPath != null && entryName != null);
		String path = getFilePath(entryName);
		return getFullPath(rootPath, path + FILE_EXTENSION);
	}

	/**
	 *
	 * @param root     use unix path separator
	 * @param relative use unix path separator
	 * @return
	 */
	public static String getFullPath(String root, String relative) {
		StringBuilder sb = new StringBuilder();
		root = toUnixPath(root);
		relative = toUnixPath(relative);

		sb.append(root);
		if (root.charAt(root.length() - 1) != '/') {
			sb.append('/');
		}
		if (relative.length() > 0) {
			int start = relative.charAt(0) == '/' ? 1 : 0;
			sb.append(relative, start, relative.length());
		}
		return sb.toString();
	}

	/**
	 * split strings by the character.
	 *
	 * It implements javascript's behavior as always return count(splitChar)+1
	 *
	 * for example, split char is '/':
	 *
	 * <dl>
	 * <li>'/' => ['', '']</li>
	 * <li>'/abc/' => ['', 'abc', '']</li>
	 * <li>'abc' => 'abc'</li>
	 * </dl>
	 *
	 * @param value
	 * @param splitChar
	 * @return
	 */
	public static String[] split(String value, char splitChar) {
		ArrayList<String> result = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (ch == splitChar) {
				result.add(sb.toString());
				sb.setLength(0);
			} else {
				sb.append(ch);
			}
		}
		result.add(sb.toString());
		return result.toArray(new String[result.size()]);
	}

	/**
	 * escape entry name to a valid file path
	 *
	 * duplicate '/' will be removed
	 *
	 * @param name
	 * @return
	 */
	public static String getFilePath(String entryName) {
		if (entryName == null || entryName.length() == 0 || entryName.equals("/")) {
			return "/";
		}

		StringBuilder sb = new StringBuilder(entryName.length());
		String[] names = split(entryName, '/');
		int start = 0;
		int end = names.length - 1;
		if (names[start].length() == 0) {
			start++;
		}
		if (names[end].length() == 0) {
			end--;
		}
		for (int i = start; i <= end; i++) {
			sb.append("/");
			sb.append(toFileName(names[i]));
		}
		if (names[names.length - 1].length() == 0) {
			sb.append("/");
		}
		return sb.toString();
	}

	/**
	 * encode entry name to a valid file name
	 *
	 * entry name is not null, not empty, doesn't contain '/'.
	 *
	 * @param name
	 * @return
	 */
	private static String toFileName(String name) {
		if (name == null || name.length() == 0) {
			return "%2F";
		}
		if (name.equals(".")) {
			return "%2E";
		}
		if (name.equals("..")) {
			return "%2E%2E";
		}
		try {
			String path = URLEncoder.encode(name, "UTF-8"); //$NON-NLS-1$
			// handle case /abc./ where the File Object will remove "."
			// unexpectedly by encoding . to %2E
			if (path.contains(".")) //$NON-NLS-1$
			{
				path = path.replace(".", "%2E"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			// handle case /abc*/ where the File Object will remove "*"
			// unexpectedly by encoding * to %2A
			if (path.contains("*")) //$NON-NLS-1$
			{
				path = path.replace("*", "%2A"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return path;
		} catch (UnsupportedEncodingException ex) {
			return name;
		}
	}

	/**
	 * escape entry name to a valid file name
	 *
	 * @param filePath , a relative file path, start with "/"
	 * @return entry name
	 */
	public static String getEntryName(String filePath) {
		if (filePath == null || filePath.length() == 0 || "/".equals(filePath)) {
			return "/";
		}
		StringBuilder sb = new StringBuilder(filePath.length());
		String[] names = split(filePath, '/');
		int start = 0;
		int end = names.length - 1;
		if (names[start].length() == 0) {
			start++;
		}
		if (names[end].length() == 0) {
			end--;
		}
		for (int i = start; i <= end; i++) {
			sb.append("/");
			sb.append(toEntryName(names[i]));
		}
		if (names[names.length - 1].length() == 0) {
			sb.append("/");
		}
		return sb.toString();
	}

	/**
	 * decode file name to entry name.
	 *
	 * @param path
	 * @return
	 */
	private static String toEntryName(String path) {
		if (path.equals("%2F")) {
			return "";
		}
		if (path.equals("%2E")) {
			return ".";
		}
		if (path.equals("%2E%2E")) {
			return "..";
		}
		try {
			return URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException ex) {
			return path;
		}
	}

	/**
	 * convert a folder to entry name.
	 *
	 * @param rootPath   - the absolute path of the root folder. The path is
	 *                   seperated by system's File seperator.
	 * @param fullString - the absolute path of the stream. The path is seperated by
	 *                   system's File seperator.
	 * @return the relative path string. The path is based on Unix syntax and starts
	 *         with "/".
	 */
	public static String getEntryName(String rootPath, String fullPath) {
		String relative = getRelativePath(rootPath, fullPath);
		if (relative.endsWith(FILE_EXTENSION)) {
			relative = relative.substring(0, relative.length() - FILE_EXTENSION.length());
		}
		return getEntryName(relative);
	}

	public static String getRelativePath(String rootPath, String fullPath) {
		// change to ux format
		String uxRoot = toUnixPath(rootPath);
		String uxPath = toUnixPath(fullPath);

		// remove root's last /
		if (uxRoot.charAt(uxRoot.length() - 1) == '/') {
			uxRoot = uxRoot.substring(0, uxRoot.length() - 1);
		}
		// test if we can get relative path
		if (!uxPath.startsWith(uxRoot)) {
			throw new IllegalArgumentException(fullPath + " must start with " + rootPath);
		}
		String relative = uxPath.substring(uxRoot.length());
		if (relative.length() == 0) {
			return "/";
		}
		return relative;
	}

	/**
	 * @param path - the path that could be in system format (seperated by
	 *             File.seperator) or Unix format (seperated by "/").
	 * @return the path that is in Unix format.
	 */
	private static String toUnixPath(String path) {
		if (path == null) {
			return null;
		}
		return path.replace('\\', '/');
	}

	/**
	 * Generate a unique file or folder name which is in the same folder as the
	 * originalName
	 *
	 * @param originalName - the original Name. For example, it could be the name of
	 *                     the file archive
	 * @return a unique file or folder name which is in the same folder as the
	 *         originalName
	 */
	synchronized public static String generateUniqueFileFolderName(String originalName) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); //$NON-NLS-1$
		String dateTimeString = df.format(new Date());

		StringBuilder folderName = new StringBuilder(originalName);
		folderName.append('_');
		folderName.append(dateTimeString);

		Random generator = new Random();
		File folder = new File(folderName.toString());
		while (folder.exists()) {
			folderName.append(generator.nextInt());
			folder = new File(folderName.toString());
		}

		return folderName.toString();

	}

	/**
	 * If the parent folder of the file doesn't exsit, create the parent folder.
	 */
	public static void createParentFolder(File fd) {
		if (fd != null && fd.getParentFile() != null && !fd.getParentFile().exists()) {
			fd.getParentFile().mkdirs();
		}
	}

	/**
	 * Recursively delete all the files and folders under dirOrFile
	 *
	 * @param dirOrFile - the File object which could be either a folder or a file.
	 */
	public static void deleteAllFiles(File dirOrFile) {
		if (!dirOrFile.exists()) {
			return;
		}

		if (dirOrFile.isFile()) {
			dirOrFile.delete();
		} else
		// dirOrFile is directory
		{
			if ((dirOrFile.listFiles() != null) && (dirOrFile.listFiles().length > 0)) {
				File[] fileList = dirOrFile.listFiles();
				for (int i = 0; i < fileList.length; i++) {
					deleteAllFiles(fileList[i]);
				}
			}

			// Directory can only be deleted when it is empty.
			dirOrFile.delete();
		}
	}

	public static void zipFolderToStream(String tempFolderPath, OutputStream ostream) {
		ZipOutputStream zipOutput = new ZipOutputStream(ostream);
		File rootDir = new File(tempFolderPath);
		File[] files = rootDir.listFiles();

		try {
			zipFiles(zipOutput, files, tempFolderPath);
			zipOutput.close();
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Utility funtion to write files/directories to a ZipOutputStream. For
	 * directories, all the files and subfolders are written recursively.
	 */
	private static void zipFiles(ZipOutputStream zipOut, File[] files, String tempFolderPath)
			throws FileNotFoundException, IOException {
		if (files == null) {
			return;
		}

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) { // if file is a directory, get child files and recursively call
										// this method
				File[] dirFiles = file.listFiles();
				zipFiles(zipOut, dirFiles, tempFolderPath);
			} else { // if file is a file, create a new ZipEntry and write out the
						// file.
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
				try (in) {
					String relativePath = getEntryName(tempFolderPath, file.getPath());
					ZipEntry entry = new ZipEntry(relativePath);
					try {
						entry.setTime(file.lastModified());
						zipOut.putNextEntry(entry); // Create a new zipEntry

						int len;
						byte[] buf = new byte[1024 * 5];
						while ((len = in.read(buf)) > 0) {
							zipOut.write(buf, 0, len);
						}
					} finally {
						zipOut.closeEntry();
					}
				}
			}
		} // end of for ( int i = 0; i < files.length; i++ )
	}

	public static void unzipArchive(File zipArchive, String tempFolderPath) {
		try {
			ZipFile zipFile = new ZipFile(zipArchive);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) { // Assume directories are stored parents first then
											// children.
					String dirName = getFullPath(tempFolderPath, entry.getName());
					// TODO: handle the error case where the folder can not be
					// created!
					File dir = new File(dirName);
					dir.mkdirs();
				} else {
					try (InputStream in = zipFile.getInputStream(entry)) {
						File file = new File(getFullPath(tempFolderPath, entry.getName()));

						File dir = new File(file.getParent());
						if (dir.exists()) {
							assert (dir.isDirectory());
						} else {
							dir.mkdirs();
						}

						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
						int len;
						byte[] buf = new byte[1024 * 5];
						try {
							while ((len = in.read(buf)) > 0) {
								out.write(buf, 0, len);
							}
						} finally {
							out.close();
						}
					}
				}
			}
			zipFile.close();

		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	public static void copy(IArchiveFile inArchive, IArchiveFile outArchive) throws IOException {
		copy(inArchive, outArchive, new HashMap<String, String>());
	}

	/**
	 *
	 * @param inArchive       Source Archive
	 * @param outArchive      Destination Archive
	 * @param transformations Optional transformations of streams specified via java
	 *                        regex patterns. The map is keyed in by a regex pattern
	 *                        and the value will be the replacement expression which
	 *                        may refer capturing groups in the key. While copying
	 *                        the streams, if any of the key regex patterns matches
	 *                        the current source stream path being copied, the
	 *                        replacement expression is used to transform the source
	 *                        stream path to a target stream path.
	 *
	 * @throws IOException
	 */
	public static void copy(IArchiveFile inArchive, IArchiveFile outArchive, Map<String, String> transformations)
			throws IOException {
		if (inArchive == null || outArchive == null) {
			throw new IOException(CoreMessages.getString(ResourceConstants.NULL_SOURCE));
		}
		if (transformations == null || transformations.isEmpty()) {
			copy(new ArchiveReader(inArchive), new ArchiveWriter(outArchive));
		} else {
			copy(new ArchiveReader(inArchive), new ArchiveWriter(outArchive), transformations);
		}
	}

	static public void copy(IDocArchiveReader reader, IDocArchiveWriter writer) throws IOException {
		List<String> streamList = reader.listAllStreams();
		for (int i = 0; i < streamList.size(); i++) {
			String streamPath = streamList.get(i);
			RAInputStream in = reader.getStream(streamPath);
			try (in) {
				RAOutputStream out = writer.createRandomAccessStream(streamPath);
				try {
					copyStream(in, out);
				} finally {
					out.close();
				}
			}
		}
	}

	/**
	 *
	 * @param reader          Source
	 * @param writer          Destination
	 * @param transformations Optional Stream Transformations specified using Java
	 *                        regex patterns
	 * @throws IOException
	 */
	static public void copy(IDocArchiveReader reader, IDocArchiveWriter writer, Map<String, String> transformations)
			throws IOException {
		List<String> streamList = reader.listAllStreams();

		// Build a map of src to target paths. The special value '1' stands for identity
		// mapping

		Map<String, String> normalizedStreamMappings = new HashMap<>();
		List<String> overridenStreams = new ArrayList<>();

		String srcStreamPath = null;
		String targetStreamPath = null;

		for (int i = 0; i < streamList.size(); i++) {
			srcStreamPath = streamList.get(i);
			targetStreamPath = getTransformedPath(srcStreamPath, transformations);

			if (targetStreamPath != DELETION_MAPPING) {
				normalizedStreamMappings.put(srcStreamPath, targetStreamPath);
				if (targetStreamPath != IDENTITY_MAPPING) {
					// Add the target stream path to the overriden streams.
					overridenStreams.add(targetStreamPath);
				}
			}
		}

		// Remove the overriden Streams if present from the copy list
		for (String streamPath : overridenStreams) {
			normalizedStreamMappings.remove(streamPath);
		}

		for (Map.Entry<String, String> entry : normalizedStreamMappings.entrySet()) {
			srcStreamPath = entry.getKey();
			targetStreamPath = entry.getValue();
			if (targetStreamPath == IDENTITY_MAPPING) {
				targetStreamPath = srcStreamPath;
			}
			RAInputStream in = reader.getStream(srcStreamPath);
			try (in) {
				RAOutputStream out = writer.createRandomAccessStream(targetStreamPath);
				try {
					copyStream(in, out);
				} finally {
					out.close();
				}
			}
		}
	}

	/*
	 * Utility method that transforms the given source stream path by running it
	 * through the specified transformations map. The Key of the map is a regex
	 * pattern and the value is the replacement string. The method iterates over the
	 * supplied transformations and if any of the key pattern matches the src stream
	 * path it transforms the source stream path to a target stream path using the
	 * replacement string. The key pattern may capture groups which may be referred
	 * to in the replacement string.
	 *
	 */
	private static String getTransformedPath(String srcStreamPath, Map<String, String> transformations) {
		Set<Map.Entry<String, String>> transformationSet = transformations.entrySet();

		String srcPatternExpr = null;
		String targetPath = null;
		Pattern pattern = null;
		Matcher matcher = null;

		for (Map.Entry<String, String> entry : transformationSet) {
			srcPatternExpr = entry.getKey();

			pattern = Pattern.compile(srcPatternExpr);
			matcher = pattern.matcher(srcStreamPath);
			int groupCount = 0;
			String groupStr = null;
			String groupToken = null;

			if (matcher.find()) {
				targetPath = entry.getValue();
				if (targetPath == null || targetPath.trim().length() == 0) {
					// Null transformation - this stream is to be skipped.
					targetPath = DELETION_MAPPING;
				} else {
					groupCount = matcher.groupCount();
					for (int i = 1; i <= groupCount; i++) {
						groupStr = matcher.group(i);
						groupToken = "\\\\" + i;
						targetPath = targetPath.replaceAll(groupToken, groupStr);
					}
				}
				break;
			}
		}
		if (targetPath == null) {
			// No transformation specified for this source stream path. Copy it as is in the
			// target stream
			targetPath = IDENTITY_MAPPING;
		}
		return targetPath;
	}

	static private void copyStream(RAInputStream in, RAOutputStream out) throws IOException {
		byte[] buf = new byte[4096];
		int readSize = in.read(buf);
		while (readSize != -1) {
			out.write(buf, 0, readSize);
			readSize = in.read(buf);
		}
	}

	static public void archive(String folder, String file) throws IOException {
		archive(folder, null, file);
	}

	static public void convertFolderArchive(String folder, String file) throws IOException {
		FolderArchiveFile folderArchive = new FolderArchiveFile(folder);
		try {
			ArchiveFileV3 fileArchive = new ArchiveFileV3(file, "rw");
			try {

				copy(folderArchive, fileArchive);

				String systemId = folderArchive.getSystemId();
				if (systemId != null) {
					fileArchive.setSystemId(systemId);
				}
				String dependId = folderArchive.getDependId();
				if (dependId != null) {
					fileArchive.setDependId(dependId);
				}
			} finally {
				fileArchive.close();
			}
		} finally {
			folderArchive.close();
		}
	}

	/**
	 * Compound File Format: <br>
	 * 1long(stream section position) + 1long(entry number in lookup map) + lookup
	 * map section + stream data section <br>
	 * The Lookup map is a hash map. The key is the relative path of the stram. The
	 * entry contains two long number. The first long is the start postion. The
	 * second long is the length of the stream. <br>
	 *
	 * @param tempFolder
	 * @param fileArchiveName - the file archive name
	 * @return Whether the compound file was created successfully.
	 */
	static public void archive(String folderName, IStreamSorter sorter, String fileName) throws IOException {
		// Delete existing file or folder that has the same
		// name of the file archive.
		folderName = new File(folderName).getCanonicalPath();
		FolderArchiveReader reader = new FolderArchiveReader(folderName);
		try {
			reader.open();
			File file = new File(fileName);
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				}
			}
			FileArchiveWriter writer = new FileArchiveWriter(fileName);
			try {
				writer.initialize();
				copy(reader, writer);
			} finally {
				writer.finish();
			}
		} finally {
			reader.close();
		}
	}

	/**
	 * Compound File Format: <br>
	 * 1long(stream section position) + 1long(entry number in lookup map) + lookup
	 * map section + stream data section <br>
	 * The Lookup map is a hash map. The key is the relative path of the stram. The
	 * entry contains two long number. The first long is the start postion. The
	 * second long is the length of the stream. <br>
	 *
	 * @param tempFolder
	 * @param fileArchiveName - the file archive name
	 * @return Whether the compound file was created successfully.
	 */
	static public void archive(String folderName, IStreamSorter sorter, String fileName, boolean contentEscape)
			throws IOException {
		// Delete existing file or folder that has the same
		// name of the file archive.
		folderName = new File(folderName).getCanonicalPath();
		FolderArchiveReader reader = new FolderArchiveReader(folderName, contentEscape);
		try {
			reader.open();
			File file = new File(fileName);
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				}
			}
			FileArchiveWriter writer = new FileArchiveWriter(fileName);
			try {
				writer.initialize();
				copy(reader, writer);
			} finally {
				writer.finish();
			}
		} finally {
			reader.close();
		}
	}

	/**
	 * files used to record the reader count reference.
	 */
	static final String READER_COUNT_FILE_NAME = "/.reader.count";
	/**
	 * file to save the metadata info of an archive.
	 */
	static final String META_DATA_FILE_NAME = "/.metadata";
	/**
	 * files which should be ignored in list stream
	 */
	static final String[] SKIP_FILES = { META_DATA_FILE_NAME, READER_COUNT_FILE_NAME };

	static boolean needSkip(String file) {
		for (int i = 0; i < SKIP_FILES.length; i++) {
			if (SKIP_FILES[i].equals(file)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get all the files under the specified folder (including all the files under
	 * sub-folders)
	 *
	 * @param dir      - the folder to look into
	 * @param fileList - the fileList to be returned
	 */
	public static void listAllFiles(File dir, ArrayList<? super File> fileList) {
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files == null) {
				return;
			}

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					fileList.add(file);
				} else if (file.isDirectory()) {
					listAllFiles(file, fileList);
				}
			}
		}
	}

	static public void expand(String file, String folder) throws IOException {
		FileArchiveReader reader = new FileArchiveReader(file);
		try {
			reader.open();
			reader.expandFileArchive(folder);
		} finally {
			reader.close();
		}
	}

	/**
	 * Assemble four bytes to an int value, make sure that the passed bytes length
	 * is larger than 4.
	 *
	 * @param bytes
	 * @return int value of bytes
	 */
	public final static int bytesToInteger(byte[] b) {
		assert b.length >= 4;
		return ((b[0] & 0xFF) << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + ((b[3] & 0xFF) << 0);
	}

	public final static int bytesToInteger(byte[] b, int off) {
		assert b.length - off >= 4;
		return ((b[off++] & 0xFF) << 24) + ((b[off++] & 0xFF) << 16) + ((b[off++] & 0xFF) << 8)
				+ ((b[off] & 0xFF) << 0);
	}

	/**
	 * Assemble eight bytes to an long value, make sure that the passed bytes length
	 * larger than 8.
	 *
	 * @param bytes
	 * @return int value of bytes
	 */
	public final static long bytesToLong(byte[] b) {
		assert b.length >= 8;
		return ((b[0] & 0xFFL) << 56) + ((b[1] & 0xFFL) << 48) + ((b[2] & 0xFFL) << 40) + ((b[3] & 0xFFL) << 32)
				+ ((b[4] & 0xFFL) << 24) + ((b[5] & 0xFFL) << 16) + ((b[6] & 0xFFL) << 8) + ((b[7] & 0xFFL) << 0);

	}

	public final static long bytesToLong(byte[] b, int off) {
		assert b.length - off >= 8;
		return ((b[off++] & 0xFFL) << 56) + ((b[off++] & 0xFFL) << 48) + ((b[off++] & 0xFFL) << 40)
				+ ((b[off++] & 0xFFL) << 32) + ((b[off++] & 0xFFL) << 24) + ((b[off++] & 0xFFL) << 16)
				+ ((b[off++] & 0xFFL) << 8) + ((b[off] & 0xFFL) << 0);
	}

	public final static void integerToBytes(int v, byte[] b) {
		assert b.length >= 4;
		b[0] = (byte) ((v >>> 24) & 0xFF);
		b[1] = (byte) ((v >>> 16) & 0xFF);
		b[2] = (byte) ((v >>> 8) & 0xFF);
		b[3] = (byte) ((v >>> 0) & 0xFF);
	}

	public final static void integerToBytes(int v, byte[] b, int off) {
		assert b.length - off >= 4;
		b[off++] = (byte) ((v >>> 24) & 0xFF);
		b[off++] = (byte) ((v >>> 16) & 0xFF);
		b[off++] = (byte) ((v >>> 8) & 0xFF);
		b[off] = (byte) ((v >>> 0) & 0xFF);
	}

	public final static void longToBytes(long v, byte[] b) {
		assert b.length >= 8;
		b[0] = (byte) ((v >>> 56) & 0xFF);
		b[1] = (byte) ((v >>> 48) & 0xFF);
		b[2] = (byte) ((v >>> 40) & 0xFF);
		b[3] = (byte) ((v >>> 32) & 0xFF);
		b[4] = (byte) ((v >>> 24) & 0xFF);
		b[5] = (byte) ((v >>> 16) & 0xFF);
		b[6] = (byte) ((v >>> 8) & 0xFF);
		b[7] = (byte) ((v >>> 0) & 0xFF);
	}

	public final static void longToBytes(long v, byte[] b, int off) {
		assert b.length - off >= 8;
		b[off++] = (byte) ((v >>> 56) & 0xFF);
		b[off++] = (byte) ((v >>> 48) & 0xFF);
		b[off++] = (byte) ((v >>> 40) & 0xFF);
		b[off++] = (byte) ((v >>> 32) & 0xFF);
		b[off++] = (byte) ((v >>> 24) & 0xFF);
		b[off++] = (byte) ((v >>> 16) & 0xFF);
		b[off++] = (byte) ((v >>> 8) & 0xFF);
		b[off] = (byte) ((v >>> 0) & 0xFF);
	}

	public static boolean removeFileAndFolder(File file) {
		assert (file != null);
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					removeFileAndFolder(children[i]);
				}
			}
		}
		if (file.exists()) {
			return file.delete();
		}
		return true;
	}

	public final static IDocArchiveReader createReader(final IDocArchiveWriter writer) {
		return new IDocArchiveReader() {

			@Override
			public String getName() {
				return writer.getName();
			}

			@Override
			public void open() throws IOException {
			}

			@Override
			public RAInputStream getStream(String relativePath) throws IOException {
				return writer.getInputStream(relativePath);
			}

			@Override
			public RAInputStream getInputStream(String relativePath) throws IOException {
				return writer.getInputStream(relativePath);
			}

			@Override
			public boolean exists(String relativePath) {
				return writer.exists(relativePath);
			}

			@Override
			public List<String> listStreams(String relativeStoragePath) throws IOException {
				return writer.listStreams(relativeStoragePath);
			}

			@Override
			public List<String> listAllStreams() throws IOException {
				return writer.listAllStreams();
			}

			@Override
			public void close() throws IOException {
			}

			@Override
			public Object lock(String stream) throws IOException {
				return writer.lock(stream);
			}

			@Override
			public void unlock(Object locker) {
				writer.unlock(locker);
			}
		};
	}

}
