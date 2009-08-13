/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.model.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.model.IChartModelHelper;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.impl.AttributeFactoryImpl;
import org.eclipse.emf.common.util.EList;

/**
 * 
 */

public class ChartModelHelper implements IChartModelHelper
{

	private static IChartModelHelper instance = new ChartModelHelper( );

	protected ChartModelHelper( )
	{

	}

	public static void initInstance( IChartModelHelper newInstance )
	{
		instance = newInstance;
	}

	public static IChartModelHelper instance( )
	{
		return instance;
	}

	public void updateExtendedProperties( EList<ExtendedProperty> properties )
	{
		ExtendedProperty extendedProperty = AttributeFactoryImpl.init( )
				.createExtendedProperty( );
		extendedProperty.setName( IDeviceRenderer.AREA_ALT_ENABLED );
		extendedProperty.setValue( Boolean.FALSE.toString( ) );
		properties.add( extendedProperty );
	}

	public List<String> getBuiltInExtendedProperties( )
	{
		return Collections.emptyList( );
	}

}
