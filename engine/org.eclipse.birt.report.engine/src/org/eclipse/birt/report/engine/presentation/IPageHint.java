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

package org.eclipse.birt.report.engine.presentation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IPageHint
{

	/**
	 * get the page number of this section
	 * 
	 * @return
	 */
	long getPageNumber( );

	/**
	 * get the page offset from the page content stream.
	 * 
	 * @return
	 */
	long getOffset( );

	/**
	 * get the sections contains in the content.
	 * 
	 * @return
	 */
	int getSectionCount( );

	/**
	 * get the start offset of the section.
	 * 
	 * @param section
	 * @return
	 */
	long getSectionStart( int section );

	/**
	 * get the end offset of the section.
	 * 
	 * @param section
	 * @return
	 */
	long getSectionEnd( int section );

	/**
	 * write the page hints into the stream.
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeObject( DataOutputStream out ) throws IOException;

	/**
	 * load the page hint from the stream
	 * 
	 * @param in
	 * @throws IOException
	 */
	public void readObject( DataInputStream in ) throws IOException;
}
