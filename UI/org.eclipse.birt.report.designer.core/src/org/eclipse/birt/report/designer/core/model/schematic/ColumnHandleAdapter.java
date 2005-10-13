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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement ColumnHandleAdapter responds to model ColumnHandle
 */
public class ColumnHandleAdapter extends DesignElementHandleAdapter
{

	public static final int DEFAULT_MINWIDTH = 20;

	/**
	 * @param handle
	 */
	public ColumnHandleAdapter( ColumnHandle column )
	{
		this( column, null );
	}

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public ColumnHandleAdapter( ColumnHandle column, IModelAdapterHelper mark )
	{
		super( column, mark );
	}

	/**
	 * Gets the width
	 * 
	 * @return
	 */
	public int getWidth( )
	{
		DimensionHandle handle = getColumnHandle( ).getWidth( );

		if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( handle.getUnits( ) ) )
		{
			Object obj = getTableParent( );

			if ( obj instanceof GridHandle )
			{
				obj = HandleAdapterFactory.getInstance( )
						.getGridHandleAdapter( obj );
			}
			else
			{
				obj = HandleAdapterFactory.getInstance( )
						.getTableHandleAdapter( obj );
			}

			if ( obj instanceof TableHandleAdapter )
			{
				int containerWidth = ( (TableHandleAdapter) obj ).getClientAreaSize( ).width;

				return (int) ( handle.getMeasure( ) * containerWidth / 100 );
			}
		}

		int px = (int) DEUtil.convertoToPixel( handle );
		if ( px <= 0 )
		{
			TableHandleAdapter adapter = HandleAdapterFactory.getInstance( )
					.getTableHandleAdapter( getTableParent( ) );
			return adapter.getDefaultWidth( getColumnNumber( ) );
		}

		return px;
	}

	/**
	 * Returns the raw column with, if it's a fix value, covert it to Pixel
	 * unit, if it's a relative value or none, retain it.
	 * 
	 * @return
	 */
	public String getRawWidth( )
	{
		DimensionHandle handle = getColumnHandle( ).getWidth( );

		String unit = handle.getUnits( );

		if ( unit == null || unit.length( ) == 0 )
		{
			return ""; //$NON-NLS-1$
		}
		else if ( unit.equals( DesignChoiceConstants.UNITS_PERCENTAGE ) )
		{
			return String.valueOf( handle.getMeasure( ) ) + unit;
		}
		else
		{
			int px = (int) DEUtil.convertoToPixel( handle );

			if ( px <= 0 )
			{
				TableHandleAdapter adapter = HandleAdapterFactory.getInstance( )
						.getTableHandleAdapter( getTableParent( ) );

				return String.valueOf( adapter.getDefaultWidth( getColumnNumber( ) ) );
			}

			return String.valueOf( px );
		}
	}

	/**
	 * If the user define the row height
	 * 
	 * @return
	 */
	public boolean isCustomWidth( )
	{
		DimensionHandle handle = getColumnHandle( ).getWidth( );
		return handle.getMeasure( ) > 0;
	}

	/**
	 * Gets the columns number
	 * 
	 * @return
	 */
	public int getColumnNumber( )
	{

		TableHandleAdapter adapter = HandleAdapterFactory.getInstance( )
				.getTableHandleAdapter( getTableParent( ) );
		return adapter.getColumns( ).indexOf( getColumnHandle( ) ) + 1;
	}

	private Object getTableParent( )
	{
		DesignElementHandle element = getColumnHandle( ).getContainer( );
		if ( element instanceof TableGroupHandle )
		{
			element = element.getContainer( );
		}
		return element;
	}

	private ColumnHandle getColumnHandle( )
	{
		return (ColumnHandle) getHandle( );
	}

	/**
	 * @param rowHeight
	 * @throws SemanticException
	 */
	public void setWidth( int columnWidth ) throws SemanticException
	{
		double value = MetricUtility.pixelToPixelInch( columnWidth );
		DimensionValue dimensionValue = new DimensionValue(
				value, DesignChoiceConstants.UNITS_IN );
		getColumnHandle( ).getWidth( ).setValue(dimensionValue);
		//getColumnHandle(
		// ).getWidth().setStringValue(String.valueOf(rowHeight) +
		// DesignChoiceConstants.UNITS_PX);
	}

	/**
	 * copy a column
	 * 
	 * @returnSemanticException
	 */
	public Object copy( ) throws SemanticException
	{
		SlotHandle slotHandle = getColumnHandle( ).getContainerSlotHandle( );

		ColumnHandle retValue = slotHandle.getElementHandle( )
				.getElementFactory( )
				.newTableColumn( );

		Iterator iter = getColumnHandle( ).getPropertyIterator( );
		while ( iter.hasNext( ) )
		{
			PropertyHandle handle = (PropertyHandle) iter.next( );
			String key = handle.getDefn( ).getName( );
			if ( handle.isLocal( ) )
			{
				//retValue.setProperty( key, getColumnHandle( ).getProperty( key ) );
				getColumnHandle( ).copyPropertyTo( key, retValue );
			}
		}
		return retValue;
	}
}