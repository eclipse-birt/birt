/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import junit.framework.TestCase;

public class DocumentArchiveTest extends TestCase {

	static final String ARCHIVE_DOCUMENT_NAME = "org.eclipse.birt.core.archive.archive.zip"; //$NON-NLS-1$
	static final String ARCHIVE_FOLDER_NAME = "org.eclipse.birt.core.archive.archive_folder"; //$NON-NLS-1$

	/**
	 * @param name
	 */
	public DocumentArchiveTest() {
		delete(new File(ARCHIVE_DOCUMENT_NAME));
	}

	@After
	public void documentArchiveTestTearDown() {
		File file = new File(ARCHIVE_DOCUMENT_NAME);
		if (file.exists()) {
			delete(file);
		}
		file = new File(ARCHIVE_FOLDER_NAME);
		if (file.exists()) {
			delete(file);
		}

		file = new File(fileArchiveName);
		if (file.exists()) {
			delete(file);
		}
		file = new File(folderArchiveName);
		if (file.exists()) {
			delete(file);
		}
	}

	private void delete(File dir) {
		if (dir.isFile()) {
			dir.delete();
		}

		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				delete(files[i]);
			}
			dir.delete();
		}

	}

	static final String fileArchiveName = "./FileArchive"; //$NON-NLS-1$
	static final String folderArchiveName = "./FolderArchive"; //$NON-NLS-1$
	static final String coreStream = "/Core.txt"; //$NON-NLS-1$
	static final String designStream = "/Design.txt"; //$NON-NLS-1$
	static final String random1Stream = "/Random1.txt"; //$NON-NLS-1$
	static final String random2Stream = "/Random2.txt"; //$NON-NLS-1$
	static final String contentStream = "/Folder1/Folder2/Content.txt"; //$NON-NLS-1$

	static final String CoreStreamContent = "This is string for testing core stream."; //$NON-NLS-1$
	static final String DesignStreamContent = "This is string for testing design stream."; //$NON-NLS-1$
	static final String Random1StreamContent = "This is string for testing random stream 1."; //$NON-NLS-1$
	static final String Random2StreamContent = "This is string for testing random stream 2."; //$NON-NLS-1$
	static final String ContentStreamContent = "This is string for testing nested folder."; //$NON-NLS-1$

	@Test
	public void testArchiveWriterAndArchiveReader() throws Exception {
		///////////////// Testing FileArchiveWriter /////////////////////////////
		FileArchiveWriter compoundWriter = new FileArchiveWriter(fileArchiveName);
		compoundWriter.initialize();
		RAOutputStream out;

		assertTrue(!compoundWriter.exists(coreStream));
		out = compoundWriter.createRandomAccessStream(coreStream);
		out.write(CoreStreamContent.getBytes());
		out.close();
		assertTrue(compoundWriter.exists(coreStream));

		out = compoundWriter.createRandomAccessStream(designStream);
		out.write(DesignStreamContent.getBytes());
		out.close();

		out = compoundWriter.createRandomAccessStream(random1Stream);
		out.write(Random1StreamContent.getBytes());
		out.close();

		out = compoundWriter.createRandomAccessStream(random2Stream);
		out.write(Random2StreamContent.getBytes());
		out.close();

		assertTrue(!compoundWriter.exists(contentStream));
		out = compoundWriter.createRandomAccessStream(contentStream);
		out.write(ContentStreamContent.getBytes());
		out.close();
		assertTrue(compoundWriter.exists(contentStream));

		compoundWriter.finish();

		///////////////// Testing FileArchiveReader /////////////////////////////
		FileArchiveReader compoundReader = new FileArchiveReader(fileArchiveName);
		compoundReader.open();

		RAInputStream in;
		String contentFromString;

		assertTrue(compoundReader.exists(coreStream));
		assertTrue(compoundReader.exists(random1Stream));
		assertTrue(compoundReader.exists(contentStream));

		in = compoundReader.getStream(coreStream);
		contentFromString = readStreamContent(in);
		assertTrue(CoreStreamContent.equals(contentFromString));

		in = compoundReader.getStream(contentStream);
		contentFromString = readStreamContent(in);
		assertTrue(ContentStreamContent.equals(contentFromString));

		in = compoundReader.getStream(random1Stream);
		assertTrue(in.read() != -1);
		assertTrue(in.skip(2) == 2);
		in.close();

		assertTrue(compoundReader.listStreams("/").size() == 4); //$NON-NLS-1$
		assertTrue(compoundReader.listStreams("/Folder1").size() == 0); //$NON-NLS-1$
		assertTrue(compoundReader.listStreams("/Folder1/Folder2").size() == 1); //$NON-NLS-1$
		assertTrue(compoundReader.listStreams("/Folder1/Folder2/Content.txt").size() == 0); //$NON-NLS-1$

		// expand the file archive to a folder archive
		compoundReader.expandFileArchive(folderArchiveName);

		compoundReader.close();

		///////////////// Testing FolderArchiveReader /////////////////////////////
		FolderArchiveReader folderReader = new FolderArchiveReader(folderArchiveName);
		folderReader.open();

		assertTrue(folderReader.exists(coreStream));
		assertTrue(folderReader.exists(random1Stream));
		assertTrue(folderReader.exists(contentStream));

		in = folderReader.getStream(coreStream);
		contentFromString = readStreamContent(in);
		assertTrue(CoreStreamContent.equals(contentFromString));

		in = (RAFolderInputStream) folderReader.getStream(contentStream);
		contentFromString = readStreamContent(in);
		assertTrue(ContentStreamContent.equals(contentFromString));

		in = (RAFolderInputStream) folderReader.getStream(random1Stream);
		assertTrue(in.read() != -1);
		assertTrue(in.skip(2) == 2);
		in.close();

		assertTrue(folderReader.listStreams("/").size() == 4); //$NON-NLS-1$
		assertTrue(folderReader.listStreams("/Folder1").size() == 0); //$NON-NLS-1$
		assertTrue(folderReader.listStreams("/Folder1/Folder2").size() == 1); //$NON-NLS-1$
		assertTrue(folderReader.listStreams("/Folder1/Folder2/Content.txt").size() == 0); //$NON-NLS-1$

		folderReader.close();

		///////////////// Testing FolderArchiveWriter /////////////////////////////
		FolderArchiveWriter folderWriter = new FolderArchiveWriter(folderArchiveName);
		folderWriter.initialize();

		// delete streams
		folderWriter.dropStream(coreStream);
		assertTrue(!folderWriter.exists(coreStream));
		folderWriter.dropStream(contentStream);
		assertTrue(!folderReader.exists(contentStream));

		// add streams
		out = folderWriter.createRandomAccessStream(coreStream);
		out.write(CoreStreamContent.getBytes());
		out.close();
		assertTrue(folderWriter.exists(coreStream));

		out = folderWriter.createRandomAccessStream(contentStream);
		out.write(ContentStreamContent.getBytes());
		out.close();
		assertTrue(folderWriter.exists(contentStream));

		folderWriter.finish();
	}

	/**
	 * Utility funtion to read the content from RAInputStream and convert it to a
	 * string.
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String readStreamContent(RAInputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int i = 0;
		byte[] buf = new byte[512]; // 512 byte buffer

		while ((i = in.read(buf)) != -1) {
			out.write(buf, 0, i);
		}

		in.close();
		out.close();

		return out.toString();
	}

	int runningThread;
	int THREAD_COUNT = 5;
	int VALUE_COUNT = 10000;

	@Test
	public void testReadMutipleThreads() throws IOException {
		// create a stream
		FileArchiveWriter writer = new FileArchiveWriter(fileArchiveName);
		writer.initialize();
		for (int i = 0; i < THREAD_COUNT; i++) {
			RAOutputStream out = writer.createRandomAccessStream("STREAM_" + i);
			for (int j = 0; j < VALUE_COUNT; j++) {
				out.writeInt(i);
			}
			out.close();
		}
		writer.finish();

		FileArchiveReader reader = new FileArchiveReader(fileArchiveName);
		reader.open();
		for (int i = 0; i < THREAD_COUNT; i++) {
			new Thread(new ReadThread(reader, "STREAM_" + i, i)).start();
		}
		long waitTime = 0;
		while (runningThread > 0) {
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
			}
			waitTime += 100;
			if (waitTime > 5000) {
				fail();
			}
		}
		reader.close();
	}

	private class ReadThread implements Runnable {

		IDocArchiveReader reader;
		String name;
		int value;

		ReadThread(IDocArchiveReader reader, String name, int value) {
			this.reader = reader;
			this.name = name;
			this.value = value;
			runningThread++;
		}

		@Override
		public void run() {
			try {
				RAInputStream in = reader.getStream(name);
				for (int i = 0; i < VALUE_COUNT; i++) {
					int readValue = in.readInt();
					assertEquals(value, readValue);
				}
				in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				runningThread--;
			}
		}
	}
}
