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

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class FolderArchiveTest extends TestCase {

	static final String ARCHIVE_NAME = "./utest/test.archive.folder/";
	static final String STREAM_NAME = "/teststream";

	@Override
	@Before
	public void setUp() {
		ArchiveUtil.deleteAllFiles(new File(ARCHIVE_NAME));
	}

	@Override
	@After
	public void tearDown() {
		ArchiveUtil.deleteAllFiles(new File(ARCHIVE_NAME));
	}

	/**
	 * reader and writer read/write interactivily. After writing, the writer should
	 * flush the data into disk. before reading, the reader should refersh the data
	 * from the disk. check to see if the data read out is same with what we saved.
	 *
	 * @throws Exception
	 */
	@Test
	public void testReaderDuringWriter() throws Exception {
		FolderArchiveWriter writer = new FolderArchiveWriter(ARCHIVE_NAME);
		writer.initialize();
		FolderArchiveReader reader = new FolderArchiveReader(ARCHIVE_NAME);
		reader.open();
		RAOutputStream ws = writer.createRandomAccessStream(STREAM_NAME);
		ws.writeInt(1);
		ws.flush();
		RAInputStream rs = reader.getStream(STREAM_NAME);
		assertEquals(1, rs.readInt());
		ws.writeLong(-1L);
		ws.flush();
		rs.refresh();
		assertEquals(-1L, rs.readLong());
		ws.seek(2);
		ws.writeLong(-2L);
		ws.flush();
		rs.refresh();
		rs.seek(2);
		assertEquals(-2L, rs.readLong());
		rs.close();

		ws.close();

		reader.close();
		writer.finish();
	}

	/**
	 * writer writes the data into disk. then reader reads them out. check the data
	 * to see if they are the same.
	 *
	 * @throws Exception
	 */
	@Test
	public void testReaderAfterWriter() throws Exception {
		FolderArchiveWriter writer = new FolderArchiveWriter(ARCHIVE_NAME);
		writer.initialize();
		RAOutputStream ws = writer.createRandomAccessStream(STREAM_NAME);
		ws.writeInt(1);
		ws.flush();
		ws.writeLong(-1L);
		ws.flush();
		ws.close();
		writer.finish();

		FolderArchiveReader reader = new FolderArchiveReader(ARCHIVE_NAME);
		reader.open();
		RAInputStream rs = reader.getStream(STREAM_NAME);
		assertEquals(1, rs.readInt());
		assertEquals(-1L, rs.readLong());
		rs.close();
		reader.close();
	}

	/**
	 * open a reader twice once before the writer's close and the other after the
	 * writer's close. test to see if the two readers return the same data.
	 *
	 * @throws Exception
	 */
	@Test
	public void testReaderCrossWriter() throws Exception {

		FolderArchiveWriter writer = new FolderArchiveWriter(ARCHIVE_NAME);
		writer.initialize();

		RAOutputStream ws = writer.createRandomAccessStream(STREAM_NAME);
		ws.writeInt(1);
		ws.flush();

		FolderArchiveReader reader = new FolderArchiveReader(ARCHIVE_NAME);
		reader.open();

		ws.writeLong(-1L);
		ws.flush();
		ws.close();
		writer.finish();

		RAInputStream rs = reader.getStream(STREAM_NAME);
		assertEquals(1, rs.readInt());
		assertEquals(-1L, rs.readLong());
		rs.close();
		reader.close();

		reader = new FolderArchiveReader(ARCHIVE_NAME);
		reader.open();
		rs = reader.getStream(STREAM_NAME);
		assertEquals(1, rs.readInt());
		assertEquals(-1L, rs.readLong());
		rs.close();
		reader.close();
	}

	/**
	 * open a empty folder to see if there are any exception throw out. It should be
	 * sucessful.
	 */
	@Test
	public void testOpenEmptyFolder() {
		try {
			new File(ARCHIVE_NAME).mkdirs();
			FolderArchiveReader reader = new FolderArchiveReader(ARCHIVE_NAME);
			reader.open();
			assertTrue(reader.listStreams("/").isEmpty());
			reader.close();
		} catch (IOException ex) {
			assertFalse(true);
			return;
		}
	}

	/**
	 * open an none exits folder. it should be failed and no folder is created.
	 *
	 */
	@Test
	public void testOpenNoneExistFolder() {
		try {
			ArchiveUtil.deleteAllFiles(new File(ARCHIVE_NAME));
			FolderArchiveReader reader = new FolderArchiveReader(ARCHIVE_NAME);
			reader.open();
			reader.close();
		} catch (IOException ex) {
			assertTrue(!new File(ARCHIVE_NAME).exists());
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	/**
	 * not a unit test
	 */
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
		sendCommand(readCmd, Command.OPEN);
		sendCommand(readCmd, Command.READING);
		sendCommand(wrtCmd, Command.CLOSE);
		sendCommand(wrtCmd, Command.EXIT);
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
				Thread.sleep(1000);
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
		FolderArchiveWriter writer;

		WriterRunnable(Command command) {
			this.command = command;
			this.command.threads++;
		}

		protected void doOpen() throws Exception {
			if (writer == null) {
				writer = new FolderArchiveWriter(ARCHIVE_NAME);
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

		@Override
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
		FolderArchiveReader reader;

		ReaderRunnable(Command command) {
			this.command = command;
			this.command.threads++;
		}

		protected void doOpen() throws Exception {
			if (reader == null) {
				reader = new FolderArchiveReader(ARCHIVE_NAME);
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
		}

		@Override
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
		FolderArchiveWriter writer = new FolderArchiveWriter(ARCHIVE_NAME);
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

}
