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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.xml.sax.Attributes;

/**
 * Base class for all report item parse states.
 *  
 */

public abstract class ReportItemState extends ReportElementState
{

	/**
	 * Constructs the report item state with the design parser handler, the
	 * container element and the container slot of the report item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ReportItemState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/**
	 * Intializes a report item with the properties common to all report items.
	 * 
	 * @param attrs
	 *            the SAX attributes object
	 */

	protected void initElement( Attributes attrs )
	{
		super.initElement( attrs, false );
	}

}