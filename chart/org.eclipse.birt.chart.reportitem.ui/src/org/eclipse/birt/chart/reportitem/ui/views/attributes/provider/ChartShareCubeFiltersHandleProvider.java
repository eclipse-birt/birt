/*******************************************************************************
* Copyright (c) 2007 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 
package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * 
 */

public class ChartShareCubeFiltersHandleProvider
		extends
			ChartCubeFilterHandleProvider
{
	@Override
	public Object[] getElements( Object inputElement )
	{
		if ( inputElement instanceof List )
		{
			List<DesignElementHandle> elements = new ArrayList<DesignElementHandle>( );
			for ( Iterator<DesignElementHandle> iter = ( (List<DesignElementHandle>) inputElement ).iterator( ); iter.hasNext( ); )
			{
				DesignElementHandle handle = iter.next( );
				if ( handle instanceof ReportItemHandle
						&& ( (ReportItemHandle) handle ).getDataBindingReference( ) != null )
				{
					elements.add( ( (ReportItemHandle) handle ).getDataBindingReference( ) );
				}
				else
				{
					elements.add( handle );
				}
			}
			contentInput = elements;
		}
		else
		{
			contentInput = new ArrayList( );
			if ( inputElement instanceof ReportItemHandle
					&& ( (ReportItemHandle) inputElement ).getDataBindingReference( ) != null )
			{
				contentInput.add( ( (ReportItemHandle) inputElement ).getDataBindingReference( ) );
			}
			else
			{
				contentInput.add( inputElement );
			}
		}
		
		Object[] elements = modelAdapter.getElements( contentInput );
		return elements;
	}
	
	@Override
	public boolean isEditable( )
	{
		return false;
	}

}
