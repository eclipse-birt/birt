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

package org.eclipse.birt.report.model.activity;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;


/**
 * This class is the base class for commands that work directly with the
 * DesignElement class.
 * 
 */

public abstract class AbstractElementCommand extends Command
{
	/**
	 * The element to modify.
	 */
	
	protected DesignElement element = null;
	
	/**
	 * Constructor.
	 * 
	 * @param design the report design
	 * @param obj the element to modify
	 */
	
	public AbstractElementCommand( ReportDesign design, DesignElement obj )
	{
		super( design );
		assert obj != null;
		element = obj;
	}
}
