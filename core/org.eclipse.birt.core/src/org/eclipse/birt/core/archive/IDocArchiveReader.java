/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.IOException;
import java.util.List;

/**
 * An interface that wraps around a report archive for reading. A report archive
 * may be, but is not limited to a zip file in compressed format, a folder in
 * uncompressed format.
 *
 * Notice that the interface does not define archive file name, nor does it
 * define folder name to store/uncompress the archive to. Setting such
 * environments up is implementation class's responsibility. To external users
 * of IReportArchive, it only cares what he can retrieve from the archive.
 */
public interface IDocArchiveReader {

	/**
	 * @return the archive name
	 */
	String getName();

	/**
	 * This functiona must be called before the reader is used. initialize the
	 * document archive. For example, the index stream mey be read into memory.
	 */
	void open() throws IOException;

	/**
	 * returns a sequential access file.
	 *
	 * @param relativePath - the relative stream path in the archive. The relative
	 *                     path is based on Unix syntax, with the root of the
	 *                     archive denoted by "/". The initial "/" character can be
	 *                     skipped. Used mainly for sequential streams in report.
	 * 
	 * @return RAInputStream
	 */
	RAInputStream getStream(String relativePath) throws IOException;

	RAInputStream getInputStream(String relativePath) throws IOException;

	/**
	 * @param relativePath - the relative stream path in the archive. The relative
	 *                     path is based on Unix syntax, with the root of the
	 *                     archive denoted by "/". The initial "/" character can be
	 *                     skipped. Used mainly for sequential streams in report.
	 * 
	 * @return whether the stream exist
	 */
	boolean exists(String relativePath);

	/**
	 * @param relativeStoragePath - the relative stream path in the archive. The
	 *                            relative path is based on Unix syntax, with the
	 *                            root of the archive denoted by "/". The initial
	 *                            "/" character can be skipped.
	 *
	 * @return a list of strings representing the underlying stream names. The
	 *         return values are in the relative path format too.
	 */
	List<String> listStreams(String relativeStoragePath) throws IOException;

	/**
	 * get all the stream in the archive file.
	 *
	 * @return
	 * @throws IOException
	 */
	List<String> listAllStreams() throws IOException;

	/**
	 * This function must be called after the reader is used. close the archive.
	 */
	void close() throws IOException;

	/**
	 * try to lock the stream
	 *
	 * @param stream
	 * @return the locker.
	 * @throws IOException
	 */
	Object lock(String stream) throws IOException;

	/**
	 * unlock the stream locked by the object.
	 *
	 * @param locker object returned by the lock().
	 * @throws IOException
	 */
	void unlock(Object locker);
}
