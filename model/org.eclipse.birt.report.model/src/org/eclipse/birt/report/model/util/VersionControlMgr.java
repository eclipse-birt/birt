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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Provides the API compatibility. The version control is to remember the
 * version number and corresponding elements. So that, provide specified
 * functions on the elements.
 * <p>
 * Currently, it remembers:
 * <ul>
 * <li>data items whose "setValueExpr" methods were called. The version number
 * is "3". 
 * </ul>
 */

public class VersionControlMgr
{

	/**
	 * The map to control the file version for backward compatibility.
	 */

	private List dataItems = null;

	/**
	 * Adds a data item for the backward compatibility of the obsolete valueExpr
	 * property of a data item.
	 * 
	 * @param element
	 *            the data item
	 */

	public void addValueExprCompatibleElement( DesignElement element )
	{
		if ( element == null )
			return;

		if ( dataItems == null )
		{
			dataItems = new ArrayList( );
		}
		dataItems.add( element );
	}

	/**
	 * Returns a list containing data items for the backward compatibility of
	 * the obsolete valueExpr property of a data item. The version number for
	 * this compatibility is "3".
	 * 
	 * @param version
	 *            the version number
	 */

	public List getCompatibleElement( String version )
	{
		if ( "3".equalsIgnoreCase( version ) ) //$NON-NLS-1$
			return dataItems;

		return Collections.EMPTY_LIST;
	}
}
