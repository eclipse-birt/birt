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

package org.eclipse.birt.report.designer.internal.ui.palette;

import org.eclipse.gef.requests.CreationFactory;

/**
 * Returns type of element wants created.
 */
public class ReportElementFactory implements CreationFactory
{

	private Object elementName;

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public ReportElementFactory( Object name )
	{
		elementName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	public Object getNewObject( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	public Object getObjectType( )
	{
		return elementName;
	}

}