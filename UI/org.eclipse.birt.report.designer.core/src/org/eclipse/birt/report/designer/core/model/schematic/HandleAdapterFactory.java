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

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.ExtendedItemHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapt;
import org.eclipse.birt.report.designer.core.model.ReportDesignHandleAdapter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

/**
 * Adapter factory class Populate HandleAdapter
 *  
 */
public class HandleAdapterFactory
{

	private static HandleAdapterFactory factory = null;

	private Map map;

	private HandleAdapterFactory( )
	{
		map = new WeakHashMap( );
	}

	/**
	 * Get the design element handle adapter for specified report element handle
	 * 
	 * @param obj
	 * @param mark
	 * @return
	 */
	public DesignElementHandleAdapter getDesignElementHandleAdapter(
			Object obj, IModelAdapterHelper mark )
	{
		
		if ( obj instanceof ReportDesignHandle )
		{
			return getReportDesignHandleAdapter( );
		}
		else if (obj instanceof LibraryHandle)
		{
			return getLibraryHandleAdapter( );
		}
		else if ( obj instanceof SimpleMasterPageHandle )
		{
			return getReportDesignHandleAdapter( );
		}
		else if ( obj instanceof TableHandle )
		{
			return getTableHandleAdapter( obj, mark );
		}
		if ( obj instanceof RowHandle )
		{
			return getRowHandleAdapter( obj, mark );
		}
		if ( obj instanceof ColumnHandle )
		{
			return getColumnHandleAdapter( obj, mark );
		}
		if ( obj instanceof CellHandle )
		{
			return getCellHandleAdapter( obj, mark );
		}
		if ( obj instanceof ImageHandle )
		{
			return getImageHandleAdapter( obj, mark );
		}
		if ( obj instanceof GridHandle )
		{
			return getGridHandleAdapter( obj, mark );
		}
		if ( obj instanceof ListHandle )
		{
			return getListHandleAdapter( obj, mark );
		}
		if ( obj instanceof LabelHandle )
		{
			return getLabelHandleAdapter( obj, mark );
		}
		if ( obj instanceof DataItemHandle )
		{
			return getDataItemHandleAdapter( obj, mark );
		}
		if ( obj instanceof TextItemHandle )
		{
			return getTextItemHandleAdapter( obj, mark );
		}
		if ( obj instanceof ExtendedItemHandle )
		{
		    return getExtendedItemHandleAdapter( obj, mark );
		}

		return null;
	}

	/**
     * @param obj
     * @param mark
     * @return
     */
    private ExtendedItemHandleAdapter getExtendedItemHandleAdapter( Object obj, IModelAdapterHelper mark )
    {
        ExtendedItemHandleAdapter retValue = (ExtendedItemHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new ExtendedItemHandleAdapter( (ExtendedItemHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
    }

    /**
	 * Get Table Handle Adapter
	 * 
	 * @param obj
	 *            TableItem instance
	 * @return Table Handle Adapter
	 */
	public GridHandleAdapter getGridHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		GridHandleAdapter retValue = (GridHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new GridHandleAdapter( (GridHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Table Handle Adapter
	 * 
	 * @param obj
	 *            TableItem instance
	 * @return Table Handle Adapter
	 */

	public GridHandleAdapter getGridHandleAdapter( Object obj )
	{
		return getGridHandleAdapter( obj, null );
	}

	/**
	 * Get Table Handle Adapter
	 * 
	 * @param obj
	 *            TableItem instance
	 * @return Table Handle Adapter
	 */
	public ListHandleAdapter getListHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		ListHandleAdapter retValue = (ListHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new ListHandleAdapter( (ListHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get List Handle Adapter
	 * 
	 * @param obj
	 *            Listtem instance
	 * @return List Handle Adapter
	 */

	public ListHandleAdapter getListHandleAdapter( Object obj )
	{
		return getListHandleAdapter( obj, null );
	}

	/**
	 * Get singleton instance of factory
	 * 
	 * @return factory instance
	 */
	public static HandleAdapterFactory getInstance( )
	{
		if ( factory == null )
		{
			factory = new HandleAdapterFactory( );
		}
		return factory;
	}

	/**
	 * Get report design handle adapter
	 * 
	 * @return Design handle adapter
	 */
	public ReportDesignHandleAdapter getReportDesignHandleAdapter( )
	{
		return new ReportDesignHandleAdapter( SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( ) );
	}
	
	/**
	 * Get report design handle adapter
	 * 
	 * @return Design handle adapter
	 */
	public ReportDesignHandleAdapter getLibraryHandleAdapter( )
	{
		return new LibraryHandleAdapt( SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( ) );
	}

	/**
	 * Get Table Handle Adapter
	 * 
	 * @param obj
	 *            TableItem instance
	 * @param mark
	 *            Helper instance
	 * @return Table Handle Adapter
	 */
	public TableHandleAdapter getTableHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		TableHandleAdapter retValue = (TableHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			if ( obj instanceof GridHandle )
			{
				retValue = new GridHandleAdapter( (GridHandle) obj, mark );
			}
			else
			{
				retValue = new TableHandleAdapter( (TableHandle) obj, mark );
			}
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Table Handle Adapter
	 * 
	 * @param obj
	 *            TableItem instance
	 * @return Table Handle Adapter
	 */
	public TableHandleAdapter getTableHandleAdapter( Object obj )
	{
		return getTableHandleAdapter( obj, null );
	}

	/**
	 * Get Table Group Handle Adapter
	 * 
	 * @param obj
	 *            TableGroupItem instance
	 * @param mark
	 *            Helper instance
	 * @return Table Group Handle Adapter
	 */
	public TableGroupHandleAdapter getTableGroupHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		TableGroupHandleAdapter retValue = (TableGroupHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new TableGroupHandleAdapter( (TableGroupHandle) obj,
					mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Table Group Handle Adapter
	 * 
	 * @param obj
	 *            TableGroupItem instance
	 * @return Table Group Handle Adapter
	 */
	public TableGroupHandleAdapter getTableGroupHandleAdapter( Object obj )
	{
		return getTableGroupHandleAdapter( obj, null );
	}

	/**
	 * Get Cell Handle Adapter
	 * 
	 * @param obj
	 *            Cell instance
	 * @param mark
	 *            Helper instance
	 * @return Cell Handle Adapter
	 */
	public CellHandleAdapter getCellHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		CellHandleAdapter retValue = (CellHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new CellHandleAdapter( (CellHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Cess Handle Adapter
	 * 
	 * @param obj
	 *            CessItem instance
	 * @return Cess Handle Adapter
	 */

	public CellHandleAdapter getCellHandleAdapter( Object obj )
	{
		return getCellHandleAdapter( obj, null );
	}

	/**
	 * Get Row Handle Adapter
	 * 
	 * @param obj
	 *            Row instance
	 * @param mark
	 *            Helper instance
	 * @return Row Handle Adapter
	 */
	public RowHandleAdapter getRowHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		RowHandleAdapter retValue = (RowHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new RowHandleAdapter( (RowHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Row Handle Adapter
	 * 
	 * @param obj
	 *            Row Item instance
	 * @return Row Handle Adapter
	 */
	public RowHandleAdapter getRowHandleAdapter( Object obj )
	{
		return getRowHandleAdapter( obj, null );
	}

	/**
	 * Get Column Handle Adapter
	 * 
	 * @param obj
	 *            Column instance
	 * @param mark
	 *            Helper instance
	 * @return Column Handle Adapter
	 */
	public ColumnHandleAdapter getColumnHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		ColumnHandleAdapter retValue = (ColumnHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new ColumnHandleAdapter( (ColumnHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;

	}

	/**
	 * Get column Handle Adapter
	 * 
	 * @param obj
	 *            Column Item instance
	 * @return Column Handle Adapter
	 */

	public ColumnHandleAdapter getColumnHandleAdapter( Object obj )
	{
		return getColumnHandleAdapter( obj, null );
	}

	/**
	 * Get Image Handle Adapter
	 * 
	 * @param obj
	 *            ImageItem instance
	 * @param mark
	 *            Helper instance
	 * @return Image Handle Adapter
	 */

	public ImageHandleAdapter getImageHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		ImageHandleAdapter retValue = (ImageHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new ImageHandleAdapter( (ImageHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Image Handle Adapter
	 * 
	 * @param obj
	 *            ImageItem instance
	 * @return Image Handle Adapter
	 */

	public ImageHandleAdapter getImageHandleAdapter( Object obj )
	{
		return getImageHandleAdapter( obj, null );
	}

	/**
	 * Remove cached adapter
	 * 
	 * @param obj
	 *            Key to find the adapter
	 */
	public void remove( Object obj )
	{
		map.remove( obj );
	}

	/**
	 * Get Label Handle Adapter
	 * 
	 * @param obj
	 *            LabelItem instance
	 * @param mark
	 *            Helper instance
	 * @return Label Handle Adapter
	 */
	public LabelHandleAdapter getLabelHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		LabelHandleAdapter retValue = (LabelHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new LabelHandleAdapter( (LabelHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Lable Handle Adapter
	 * 
	 * @param obj
	 *            LabelItem instance
	 * @return Label Handle Adapter
	 */

	public LabelHandleAdapter getLabelHandleAdapter( Object obj )
	{
		return getLabelHandleAdapter( obj, null );
	}

	/**
	 * Get Text Item Handle Adapter
	 * 
	 * @param obj
	 *            TextItem instance
	 * @param mark
	 *            Helper instance
	 * 
	 * @return Table Handle Adapter
	 */
	public TextItemHandleAdapter getTextItemHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		TextItemHandleAdapter retValue = (TextItemHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new TextItemHandleAdapter( (TextItemHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Text Handle Adapter
	 * 
	 * @param obj
	 *            TextItem instance
	 * @return Text Handle Adapter
	 */

	public TextItemHandleAdapter getTextItemHandleAdapter( Object obj )
	{
		return getTextItemHandleAdapter( obj, null );
	}

	/**
	 * Get Data Item Handle Adapter
	 * 
	 * @param obj
	 *            DataItem instance
	 * @param mark
	 *            Helper instance
	 * @return Table Handle Adapter
	 */
	public DataItemHandleAdapter getDataItemHandleAdapter( Object obj,
			IModelAdapterHelper mark )
	{
		DataItemHandleAdapter retValue = (DataItemHandleAdapter) map.get( obj );
		if ( retValue == null )
		{
			retValue = new DataItemHandleAdapter( (DataItemHandle) obj, mark );
			map.put( obj, retValue );
		}
		return retValue;
	}

	/**
	 * Get Data Handle Adapter
	 * 
	 * @param obj
	 *            DataItem instance
	 * @return Data Handle Adapter
	 */

	public DataItemHandleAdapter getDataItemHandleAdapter( Object obj )
	{
		return getDataItemHandleAdapter( obj, null );
	}

}