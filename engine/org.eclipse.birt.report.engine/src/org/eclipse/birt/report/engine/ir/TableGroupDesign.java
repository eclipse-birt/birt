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

/**
 * group defined in table item
 * 
 * @see TableItemDesign
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:08:26 $
 */
public class TableGroupDesign extends GroupDesign
{

	/**
	 * Header band.
	 */
	protected TableBandDesign header = null;
	/**
	 * Footer band
	 */
	protected TableBandDesign footer = null;

	/**
	 * @return Returns the footer.
	 */
	public TableBandDesign getFooter( )
	{
		return footer;
	}

	/**
	 * @param footer
	 *            The footer to set.
	 */
	public void setFooter( TableBandDesign footer )
	{
		this.footer = footer;
	}

	/**
	 * @return Returns the header.
	 */
	public TableBandDesign getHeader( )
	{
		return header;
	}

	/**
	 * @param header
	 *            The header to set.
	 */
	public void setHeader( TableBandDesign header )
	{
		this.header = header;
	}
}
