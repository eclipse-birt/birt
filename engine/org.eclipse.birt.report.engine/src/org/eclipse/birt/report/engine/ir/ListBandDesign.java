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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;

/**
 * List Band.
 * 
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:08:26 $
 */
public class ListBandDesign
{

	/**
	 * section in the listBand
	 */
	protected ArrayList contents = new ArrayList( );

	/**
	 * get section in this band
	 * 
	 * @param index
	 *            section index
	 * @return Returns the sections.
	 */
	public ReportItemDesign getContent( int index )
	{
		assert ( index >= 0 && index < contents.size( ) );
		return (ReportItemDesign) contents.get( index );
	}

	/**
	 * get total sections
	 * 
	 * @return total count sections in this list band.
	 */
	public int getContentCount( )
	{
		return this.contents.size( );
	}

	/**
	 * get all the sections.
	 * 
	 * @return array list contains all the sections
	 */
	public ArrayList getContents( )
	{
		return this.contents;
	}

	/**
	 * set the section of this band
	 * 
	 * @param item
	 *            The sections to set.
	 */
	public void addContent( ReportItemDesign item )
	{
		this.contents.add( item );
	}
}
