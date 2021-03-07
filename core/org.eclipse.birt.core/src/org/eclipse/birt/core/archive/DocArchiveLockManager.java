/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * an IArchiveLockManager implemented through file lock.
 */
class DocArchiveLockManager implements IArchiveLockManager {

	protected static Logger log = Logger.getLogger(DocArchiveLockManager.class.getName());

	DocArchiveLockManager() {
	}

	private static class Lock {

		String name;
		FileLock lock;
	}

	private static class Channel {

		RandomAccessFile file;
		FileChannel channel;
		int refCount;
	}

	protected HashMap<String, Channel> channels = new HashMap<>();

	protected synchronized FileChannel getChannel(String name) throws IOException {
		Channel channel = channels.get(name);
		if (channel == null) {
			channel = new Channel();
			channel.file = new RandomAccessFile(name, "rw");
			channel.channel = channel.file.getChannel();
			channels.put(name, channel);
		}
		channel.refCount++;
		return channel.channel;
	}

	protected synchronized void releaseChannel(String name) {
		Channel channel = channels.get(name);
		if (channel != null) {
			channel.refCount--;
			if (channel.refCount == 0) {
				channels.remove(name);
				try {
					channel.file.close();
					new File(name).delete();
				} catch (IOException ex) {
					log.log(Level.FINE, "failed to close the file", ex);
				}
			}
		}
	}

	/**
	 * try to lock the file. If some thread is locking this file, wait until it
	 * sucess.
	 *
	 * @param name file name.
	 * @return the lock object used to unlock.
	 * @throws IOException
	 */
	@Override
	public Object lock(String name) throws IOException {
		FileChannel channel = getChannel(name);
		try {
			while (true) {
				synchronized (channel) {
					try {
						FileLock fLock = channel.lock();
						Lock lock = new Lock();
						lock.name = name;
						lock.lock = fLock;
						return lock;
					} catch (OverlappingFileLockException ov) {
						// another thread has locked the file, so wait them
						// release the lock and retry
						try {
							channel.wait();
						} catch (InterruptedException ex) {
						}
					}
				}
			}
		} catch (IOException ex) {
			releaseChannel(name);
			throw ex;
		}
	}

	/**
	 * unlock the previous locked file.
	 *
	 * @param lockObj the object get from the lock.
	 */
	@Override
	public void unlock(Object lockObj) {
		if (lockObj instanceof Lock) {
			Lock lock = (Lock) lockObj;
			FileLock fLock = lock.lock;
			FileChannel channel = fLock.channel();
			try {
				fLock.release();
			} catch (Exception ex) {
				log.log(Level.FINE, "exception occus while release the lock", ex);
			}
			// after unlock the file, notify the waiting threads
			synchronized (channel) {
				channel.notify();
			}
			releaseChannel(lock.name);
		}
	}
}
