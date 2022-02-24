/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class FileArchiveTest extends TestCase {

	static final String ARCHIVE_NAME = "./utest/test.archive";
	static final String TEMP_FOLDER_NAME = "./utest/test.archive.tmpfolder";
	static final String STREAM_NAME = "/teststream";

	@Before
	public void setUp() {
		ArchiveUtil.deleteAllFiles(new File(ARCHIVE_NAME));
		ArchiveUtil.deleteAllFiles(new File(TEMP_FOLDER_NAME));
	}

	@After
	public void tearDown() {
		ArchiveUtil.deleteAllFiles(new File(TEMP_FOLDER_NAME));
		ArchiveUtil.deleteAllFiles(new File(ARCHIVE_NAME));
	}

	@Test
	public void testReaderAfterWriter() throws Exception {
		FileArchiveWriter writer = new FileArchiveWriter(ARCHIVE_NAME);
		writer.initialize();
		RAOutputStream ws = writer.createRandomAccessStream(STREAM_NAME);
		ws.writeInt(1);
		ws.flush();
		ws.writeLong(-1L);
		ws.flush();
		ws.close();
		writer.finish();

		FileArchiveReader reader = new FileArchiveReader(ARCHIVE_NAME);
		reader.open();
		RAInputStream rs = reader.getStream(STREAM_NAME);
		assertEquals(1, rs.readInt());
		assertEquals(-1L, rs.readLong());
		rs.close();
		reader.close();
	}

	@Test
	public void testOpenEmptyFile() throws IOException {
		new RandomAccessFile(ARCHIVE_NAME, "rw").close();
		try {
			FileArchiveReader reader = new FileArchiveReader(ARCHIVE_NAME);
			try {
				reader.open();
			} finally {
				reader.close();
			}
		} catch (IOException ex) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	@Test
	public void testOpenNoneExistFile() {
		new File(ARCHIVE_NAME).delete();
		try {
			FileArchiveReader reader = new FileArchiveReader(ARCHIVE_NAME);
			try {
				reader.open();
			} finally {
				reader.close();
			}
		} catch (IOException ex) {
			assertTrue(!new File(ARCHIVE_NAME).exists());
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	@Test
	public void testMutipleThreadReadWrite() {
		Command wrtCmd = new Command();
		Command readCmd = new Command();

		new Thread(new WriterRunnable(wrtCmd)).start();
		new Thread(new ReaderRunnable(readCmd)).start();
		new Thread(new ReaderRunnable(readCmd)).start();
		new Thread(new ReaderRunnable(readCmd)).start();
		new Thread(new ReaderRunnable(readCmd)).start();
		new Thread(new ReaderRunnable(readCmd)).start();
		new Thread(new ReaderRunnable(readCmd)).start();
		sendCommand(wrtCmd, Command.OPEN);
		sendCommand(wrtCmd, Command.WRITING);
		sendCommand(wrtCmd, Command.CLOSE);
		sendCommand(wrtCmd, Command.EXIT);
		sendCommand(readCmd, Command.OPEN);
		sendCommand(readCmd, Command.READING);
		sendCommand(readCmd, Command.CLOSE);
		sendCommand(readCmd, Command.OPEN);
		sendCommand(readCmd, Command.READING);
		sendCommand(readCmd, Command.CLOSE);
		sendCommand(readCmd, Command.EXIT);
	}

	protected void sendCommand(Command command, int code) {
		synchronized (command) {
			command.command = code;
			command.ex = null;
			command.status = Command.STATUS_START;
			command.notifyAll();
		}
		while (command.status != Command.STATUS_FINISH) {
			try {
				Thread.sleep(20);
			} catch (Exception ex) {

			}
		}
		if (command.ex != null) {
			fail("Exception occurs");
		}
	}

	static class Command {

		static final int STATUS_START = 1;
		static final int STATUS_FINISH = 0;

		static final int OPEN = 1;
		static final int CLOSE = 2;
		static final int WRITING = 3;
		static final int READING = 4;
		static final int EXIT = 6;
		int command;
		int status;
		int threads;
		Exception ex;
	}

	class WriterRunnable implements Runnable {

		Command command;
		FileArchiveWriter writer;

		WriterRunnable(Command command) {
			this.command = command;
			this.command.threads++;
		}

		protected void doOpen() throws Exception {
			if (writer == null) {
				writer = new FileArchiveWriter(ARCHIVE_NAME);
				writer.initialize();
			}
		}

		protected void doWrite() throws Exception {
			if (writer != null) {
				RAOutputStream os = writer.createRandomAccessStream("/test.txt");
				os.writeInt(-1);
				os.writeInt(-1);
				os.close();
			}
		}

		protected void doClose() throws Exception {
			if (writer != null) {
				writer.finish();
				writer = null;
			}
		}

		public void run() {
			while (true) {
				synchronized (command) {
					try {
						command.wait();
					} catch (InterruptedException e) {

					}
				}
				System.out.println(command.command);
				try {
					switch (command.command) {
					case Command.OPEN:
						doOpen();
						break;
					case Command.WRITING:
						doWrite();
						break;
					case Command.CLOSE:
						doClose();
						break;
					case Command.EXIT:
						command.status = Command.STATUS_FINISH;
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					command.ex = ex;
				}
				command.status = Command.STATUS_FINISH;
			}
		}
	}

	class ReaderRunnable implements Runnable {

		Command command;
		FileArchiveReader reader;

		ReaderRunnable(Command command) {
			this.command = command;
			this.command.threads++;
		}

		protected void doOpen() throws Exception {
			if (reader == null) {
				reader = new FileArchiveReader(ARCHIVE_NAME);
				reader.open();
			}

		}

		protected void doRead() throws Exception {
			if (reader != null) {
				RAInputStream is = reader.getStream("/test.txt");
				is.readInt();
				is.readInt();
				is.close();
			}

		}

		protected void doClose() throws Exception {
			if (reader != null) {
				reader.close();
				reader = null;
			}
		};

		public void run() {
			while (true) {
				synchronized (command) {
					try {
						command.wait();
					} catch (InterruptedException e) {

					}
				}

				System.out.println(command.command);
				try {
					switch (command.command) {
					case Command.OPEN:
						doOpen();
						break;
					case Command.READING:
						doRead();
						break;
					case Command.CLOSE:
						doClose();
						break;
					case Command.EXIT:
						command.status = Command.STATUS_FINISH;
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					command.ex = ex;
				}
				command.status = Command.STATUS_FINISH;
			}

		}
	}

	/**
	 * test the open stream, get length etc.
	 */
	@Test
	public void testOpenStream() throws Exception {
		FileArchiveWriter writer = new FileArchiveWriter(ARCHIVE_NAME);
		writer.initialize();
		RAOutputStream ws = writer.createRandomAccessStream(STREAM_NAME);
		ws.writeInt(1);
		assertEquals(4, ws.length());
		ws.flush();
		ws.writeLong(-1L);
		assertEquals(12, ws.length());
		ws.flush();
		ws.close();
		ws = writer.openRandomAccessStream(STREAM_NAME);
		assertEquals(12, ws.length());
		writer.finish();
	}

	@Test
	public void testReadWriteStream() throws Exception {
		FileArchiveWriter writer = new FileArchiveWriter(ARCHIVE_NAME);
		writer.initialize();
		RAOutputStream os = writer.createOutputStream(STREAM_NAME);
		RAInputStream is = writer.getInputStream(STREAM_NAME);
		os.writeInt(1);
		os.flush();
		is.refresh();
		assertEquals(1, is.readInt());

		os.writeLong(-1L);
		os.flush();
		is.refresh();
		assertEquals(-1L, is.readLong());

		assertEquals(12, os.length());
		assertEquals(12, is.length());
		writer.finish();
	}
}
