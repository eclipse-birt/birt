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
import java.io.InputStream;

public abstract class RAInputStream extends InputStream
{
	/**
	 * @param localPos
	 * @throws IOException
	 */
	public abstract void seek( long localPos ) throws IOException;
	
	public abstract long getOffset() throws IOException;
	
	public abstract long length() throws IOException;
    /**
     * @return
     * @throws IOException
     */
    public abstract int readInt() throws IOException;   
    
    public abstract long readLong() throws IOException;

	public abstract void readFully( byte b[], int off, int len ) throws IOException;
	
	public abstract void refresh( ) throws IOException;
}