/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AccessibilityValue;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.SeriesValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Action Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 */
public class ActionValueImpl extends EObjectImpl implements ActionValue
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ActionValueImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass( )
	{
		return AttributePackage.Literals.ACTION_VALUE;
	}

	private static ActionValue copyInstanceThis( ActionValue src )
	{
		if ( src == null )
		{
			return null;
		}

		ActionValueImpl dest = new ActionValueImpl( );

		return dest;
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static ActionValue copyInstance( ActionValue src )
	{
		if ( src == null )
		{
			return null;
		}

		if ( src instanceof AccessibilityValue )
		{
			return AccessibilityValueImpl.copyInstance( (AccessibilityValue) src );
		}
		else if ( src instanceof CallBackValue )
		{
			return CallBackValueImpl.copyInstance( (CallBackValue) src );
		}
		else if ( src instanceof ScriptValue )
		{
			return ScriptValueImpl.copyInstance( (ScriptValue) src );
		}
		else if ( src instanceof SeriesValue )
		{
			return SeriesValueImpl.copyInstance( (SeriesValue) src );
		}
		else if ( src instanceof TooltipValue )
		{
			return TooltipValueImpl.copyInstance( (TooltipValue) src );
		}
		else if ( src instanceof URLValue )
		{
			return URLValueImpl.copyInstance( (URLValue) src );
		}
		else if ( src instanceof MultiURLValues )
		{
			return MultiURLValuesImpl.copyInstance( (MultiURLValues) src );
		}
		else
		{
			return copyInstanceThis( src );
		}

	}

} // ActionValueImpl
