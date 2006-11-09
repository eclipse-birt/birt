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

package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Provides the specific property searching route for <code>ReportItem</code>,
 * especially about how to apply CSS rules on <code>ReportItem</code>.
 */

public class ReportItemPropSearchStrategy extends PropertySearchStrategy
{

	private final static ReportItemPropSearchStrategy instance = new ReportItemPropSearchStrategy( );

	/**
	 * Protected constructor.
	 */

	protected ReportItemPropSearchStrategy( )
	{
	}

	/**
	 * Returns the instance of <code>ReportItemPropSearchStrategy</code> which
	 * provide the specific property searching route for <code>ReportItem</code>.
	 * 
	 * @return the instance of <code>ReportItemPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance( )
	{
		return instance;
	}
	
	/**
	 * Tests if the property of a cell is inheritable in the context.
	 * <p>
	 * If the cell resides in the row and the property is "vertical-align",
	 * return <code>true</code>. Otherwise, return the value from its super
	 * class.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#isInheritableProperty(org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */
	
	protected boolean isInheritableProperty( DesignElement element, ElementPropertyDefn prop )
	{
		assert prop != null;

		if ( IStyleModel.VERTICAL_ALIGN_PROP.equalsIgnoreCase( prop.getName( ) )
				&& element.getContainer( ) instanceof Cell )
			return true;

		return super.isInheritableProperty( element, prop );
	}
}
