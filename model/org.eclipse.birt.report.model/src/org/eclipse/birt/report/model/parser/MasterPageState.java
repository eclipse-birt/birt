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

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.MasterPage;

/**
 * This class parses a master page.
 * 
 */

public abstract class MasterPageState extends ReportElementState
{

	/**
	 * The master page being created.
	 */

	protected MasterPage	element	= null;

	/**
	 * Constructs the master page state with the design file parser handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public MasterPageState( ModuleParserHandler handler )
	{
		super( handler, handler.getModule( ), IModuleModel.PAGE_SLOT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}
}
