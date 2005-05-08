/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 * List group
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class ListGroupDesign extends GroupDesign
{

	/**
	 * group header
	 */
	protected ListBandDesign header = new ListBandDesign( );
	/**
	 * group footer
	 */
	protected ListBandDesign footer = new ListBandDesign( );

	/**
	 * @return Returns the footer.
	 */
	public ListBandDesign getFooter( )
	{
		return footer;
	}

	/**
	 * @param footer
	 *            The footer to set.
	 */
	public void setFooter( ListBandDesign footer )
	{
		this.footer = footer;
	}

	/**
	 * @return Returns the header.
	 */
	public ListBandDesign getHeader( )
	{
		return header;
	}

	/**
	 * @param header
	 *            The header to set.
	 */
	public void setHeader( ListBandDesign header )
	{
		this.header = header;
	}
}
