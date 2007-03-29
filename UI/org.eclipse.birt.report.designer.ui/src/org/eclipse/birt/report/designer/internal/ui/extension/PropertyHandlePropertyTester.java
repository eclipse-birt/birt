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

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.core.expressions.PropertyTester;

/**
 * 
 */

public class PropertyHandlePropertyTester extends PropertyTester
{

	public PropertyHandlePropertyTester( )
	{
		// TODO Auto-generated constructor stub
	}

	public boolean test( Object receiver, String property, Object[] args,
			Object expectedValue )
	{
		if ( "handleProperty".equals( property ) )
		{
			if ( receiver instanceof PropertyHandle )
			{
				String propertyName = expectedValue.toString( );
				return propertyName.equals( ( (PropertyHandle) receiver ).getPropertyDefn( ).getName( ) );

			}
		}
		return false;
	}

}
