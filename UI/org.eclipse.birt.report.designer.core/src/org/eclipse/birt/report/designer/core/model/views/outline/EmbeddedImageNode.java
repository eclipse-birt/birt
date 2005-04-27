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

package org.eclipse.birt.report.designer.core.model.views.outline;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * Model class for embedded image node in the outline view
 */

public class EmbeddedImageNode
{

	private ReportDesignHandle reportHandle;

	/**
	 * Constructor
	 * @param reportHandle
	 */
	public EmbeddedImageNode( ReportDesignHandle reportHandle )
	{
		this.reportHandle = reportHandle;
	}

	/**
	 * Get container of embedded images.
	 * @return report design handle, which contains embedded images.
	 */
	public ReportDesignHandle getReportDesignHandle( )
	{
		return reportHandle;
	}

	/**
	 * @return Array of embedded images.
	 */
	public Object[] getChildren( )
	{
		ArrayList children = new ArrayList( );
		for ( Iterator itor = reportHandle.imagesIterator( ); itor.hasNext( ); )
		{
			children.add( itor.next( ) );
		}
		return children.toArray( );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object arg0 )
	{
		if ( arg0 == this )
		{
			return true;
		}
		if ( arg0 instanceof EmbeddedImageNode )
		{
			return ( (EmbeddedImageNode) arg0 ).reportHandle == reportHandle;
		}
		return false;
	}
}
