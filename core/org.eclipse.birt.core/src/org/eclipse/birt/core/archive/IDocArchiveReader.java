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

import java.io.IOException;
import java.util.List;

/**
 * An interface that wraps around a report archive for reading. A report 
 * archive may be, but is not limited to a zip file in compressed format, 
 * a folder in uncompressed format. 
 * 
 * Notice that the interface does not define archive file name, nor 
 * does it define folder name to store/uncompress the archive to. 
 * Setting such environments up is implementation class's  
 * responsibility. To external users of IReportArchive, it only
 * cares what he can retrieve from the archive. 
 */
public interface IDocArchiveReader {
	
	/**
	 * @return the archive name
	 */
	public String getName();
	
	/**
	 * This functiona must be called before the reader is used.
	 * initialize the document archive. For example, if the archive is in a
	 * compressed format, it may be decompressed. Also, the index stream mey be
	 * read into memory. 
	 */
	public void open( ) throws IOException;
	
	/**
	 * returns a sequential access file.
	 * 
	 * path is based on Unix syntax, with the root of the archive denoted 
	 * by "/". The initial "/" character can be skipped. 
	 * Used mainly for sequential streams in report.  
	 * 
	 * @param path the full path to the file 
	 * @return a File object for the specific stream
	 */
	public RAInputStream getStream(String path) throws IOException;
	
	/**
	 * @param path the full path to the file
	 * @return whether the stream exist
	 */
	public boolean exists(String path) throws IOException;
	
	/**
	 * @param path the path for a storage, or stream. 
	 * @return a list of strings representing the underlying stream 
	 * names. If a stream path is passed to the function, returns null.  
	 */
	public List listStreams(String storagePath) throws IOException;
	
	/**
	 * This function must be called after the reader is used.
	 * close the archive.
	 */
	public void close() throws IOException;
}
