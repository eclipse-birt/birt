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

/**
 * An interface that wraps around a report archive for reading. A report 
 * archive may be, but is not limited to a zip file in compressed format, 
 * a folder in uncompressed format. 
 * 
 * Notice that the interface does not define archive file name, nor 
 * does it define folder name to store/uncompress the archive to. 
 * Setting such environments up is implementation class's  
 * responsibility. 
 */
public interface IDocArchiveWriter {

	/**
	 * This function must be called before the writer is used.
	 *  initialize the document archive. 
	 */
	public void initialize( ) throws IOException;
	
	/**
	 * @return the archive name
	 */
	public String getName();

	/**
	 * returns a File. The path is based on Unix 
	 * syntax, with the root of the archive denoted by "/". The 
 	 * initial "/" character can be skipped. 
	 * 
	 * All the parent folder of the specified stream are created if 
	 * it is not exists, but the user need to create the stream itself. 
	 * 
	 * @param relative path to report archive path 
	 * @return a File object for the specific stream
	 */
	public RAOutputStream createRandomAccessStream(String relativePath) throws IOException;
	
	/**
	 * This function must be called after the writer is used.
	 * finalizes the socument archive. This may involve compressing the archive
	 * to a single file. This also closes and finishes using the archive.
	 */
	public void finish() throws IOException;
}
