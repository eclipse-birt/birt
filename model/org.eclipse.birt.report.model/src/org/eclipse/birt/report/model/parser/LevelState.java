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
import org.eclipse.birt.report.model.core.namespace.ModuleNameHelper;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * Abstract level state for all OLAP level parser.
 */
abstract public class LevelState extends ReportElementState
{

	/**
	 * Constructs level state with the design parser handler, the container
	 * element and the container property name of the report element.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param prop
	 *            the slot in which this element appears
	 */

	public LevelState( ModuleParserHandler handler, DesignElement theContainer,
			String prop )
	{
		super( handler, theContainer, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#end()
	 */
	public void end( ) throws SAXException
	{
		super.end( );

		// to do the backward compatibility, we cached all level elements and
		// helps to resolve
		if ( handler.versionNumber < VersionUtil.VERSION_3_2_13 )
		{
			((ModuleNameHelper)handler.module.getNameHelper( )).addCachedLevel( getElement( ) );
		}

	}

}
