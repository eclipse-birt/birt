/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import java.util.List;

public interface IArchiveFile
{

	public String getName( );

	public void close( ) throws IOException;

	public void flush( ) throws IOException;

	public void refresh( ) throws IOException;

	public boolean exists( String name );

	public void setCacheSize( long cacheSize );

	public long getUsedCache( );

	public ArchiveEntry openEntry( String name ) throws IOException;

	public List<String> listEntries( String namePattern );

	public ArchiveEntry createEntry( String name ) throws IOException;

	public boolean removeEntry( String name ) throws IOException;

	public Object lockEntry( String entry ) throws IOException;

	public void unlockEntry( Object locker ) throws IOException;

	public String getSystemId( );

	public String getDependId( );

	public void save( ) throws IOException;
	
	public long getLength( );
}
