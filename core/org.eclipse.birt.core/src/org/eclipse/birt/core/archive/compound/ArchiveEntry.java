/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.IOException;

/**
 * the user must close the archive 
 */
abstract public class ArchiveEntry
{

	abstract public String getName( ) throws IOException;

	abstract public long getLength( ) throws IOException;

	abstract public void setLength( long length ) throws IOException;

	abstract public void flush( ) throws IOException;

	abstract public void refresh( ) throws IOException;

	abstract public int read( long pos, byte[] b, int off, int len )
			throws IOException;

	abstract public void write( long pos, byte[] b, int off, int len )
			throws IOException;

	abstract public void close( ) throws IOException;
}