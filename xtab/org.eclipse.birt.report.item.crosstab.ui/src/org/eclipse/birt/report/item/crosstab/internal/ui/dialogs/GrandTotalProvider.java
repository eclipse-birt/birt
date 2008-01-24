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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.GrandTotalInfo;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;

public class GrandTotalProvider extends TotalProvider implements
		ITableLabelProvider,
		IStructuredContentProvider,
		ICellModifier
{

	private CellEditor[] cellEditor;
	TableViewer viewer;
	private String[] comboItems = null;
	private IAggregationCellViewProvider[] providers;
	private String[] viewNames;

	private CrosstabReportItemHandle crosstab;

	private void initialization( )
	{

		String firstItem = Messages.getString( "GrandTotalProvider.ViewStatus" );
		List viewNameList = new ArrayList( );
		List itemList = new ArrayList( );

		itemList.add( firstItem );
		viewNameList.add( "" );

		Object obj = ElementAdapterManager.getAdapters( crosstab.getModelHandle( ),
				IAggregationCellViewProvider.class );
		if ( obj instanceof Object[] )
		{
			Object arrays[] = (Object[]) obj;
			providers = new IAggregationCellViewProvider[arrays.length + 1];
			providers[0] = null;
			for ( int i = 0; i < arrays.length; i++ )
			{
				IAggregationCellViewProvider tmp = (IAggregationCellViewProvider) arrays[i];
				String viewName = tmp.getViewName( );
				viewNameList.add( viewName );
				providers[i + 1] = tmp;
				itemList.add( Messages.getString( "GrandTotalProvider.ShowAs",
						new String[]{
							viewName
						} ) );
			}
		}

		comboItems = (String[]) itemList.toArray( new String[itemList.size( )] );
		viewNames = (String[]) viewNameList.toArray( new String[viewNameList.size( )] );

	}

	public GrandTotalProvider( TableViewer viewer,
			CrosstabReportItemHandle crosstab )
	{
		this.viewer = viewer;
		this.crosstab = crosstab;
		initialization( );
	}

	public String[] getColumnNames( )
	{
		return columnNames;
	}

	public CellEditor[] getCellEditors( )
	{
		if ( cellEditor != null )
		{
			return cellEditor;
		}

		ComboBoxCellEditor comboCell = new ComboBoxCellEditor( viewer.getTable( ),
				comboItems,
				SWT.READ_ONLY );
		cellEditor = new CellEditor[]{
				null, null, comboCell
		};
		return cellEditor;
	}

	// private CellEditor[] editors;
	private String[] columnNames = new String[]{
			"", Messages.getString( "GrandTotalProvider.Column.DataField" ),// Messages.getString("GrandTotalProvider.Column.Function")
																			// //$NON-NLS-1$
																			// //$NON-NLS-2$
																			// //$NON-NLS-3$
			Messages.getString( "GrandTotalProvider.Column.View" )
	};

	public Image getColumnImage( Object element, int columnIndex )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText( Object element, int columnIndex )
	{
		GrandTotalInfo info = (GrandTotalInfo) element;
		switch ( columnIndex )
		{
			case 0 :
				return ""; //$NON-NLS-1$
			case 1 :
				return info.getMeasure( ) == null ? "" : info.getMeasure( ) //$NON-NLS-1$
						.getName( );
			case 2 :
				String expectedView = info.getExpectedView( );
				if ( expectedView == null || expectedView.length( ) == 0 )
				{
					return comboItems[0];
				}
				int index = Arrays.asList( viewNames ).indexOf( expectedView );
				if ( index <= 0 )
				{
					index = 0;
				}
				return comboItems[index];
			default :
				break;
		}
		return ""; //$NON-NLS-1$
	}

	public Object[] getElements( Object inputElement )
	{
		if ( inputElement instanceof List )
			return ( (List) inputElement ).toArray( );
		return new Object[]{};

	}

	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{
	}

	public int[] columnWidths( )
	{
		return new int[]{
				20, 210, 120
		};
	}

	public boolean canModify( Object element, String property )
	{
		// TODO Auto-generated method stub
		if ( Arrays.asList( columnNames ).indexOf( property ) == 2 )
		{
			if(viewer instanceof CheckboxTableViewer)
			{
				return ((CheckboxTableViewer)viewer).getChecked( element );
			}else
			{
				return true;
			}
			
		}
		else
		{
			return false;
		}
	}

	public Object getValue( Object element, String property )
	{
		// TODO Auto-generated method stub
		if ( element instanceof Item )
		{
			element = ( (Item) element ).getData( );
		}
		Object value = null;

		int index = Arrays.asList( columnNames ).indexOf( property );
		switch ( index )
		{
			case 1 :
				break;
			case 2 :
				String expectedView = ( (GrandTotalInfo) ( element ) ).getExpectedView( );
				if ( expectedView == null || expectedView.length( ) == 0 )
				{
					return new Integer( 0 );
				}
				int sel = Arrays.asList( viewNames ).indexOf( expectedView );
				value = sel <= 0 ? new Integer( 0 ) : new Integer( sel );
				break;
			default :
		}
		return value;
	}

	public void modify( Object element, String property, Object value )
	{
		// TODO Auto-generated method stub
		if ( element instanceof Item )
		{
			element = ( (Item) element ).getData( );
		}

		int index = Arrays.asList( columnNames ).indexOf( property );
		switch ( index )
		{
			case 0 :
				break;
			case 1 :
				break;
			case 2 :
				int sel = ( (Integer) value ).intValue( );
				if ( sel == 0 )
				{
					( (GrandTotalInfo) ( element ) ).setExpectedView( "" );
				}
				else
				{
					( (GrandTotalInfo) element ).setExpectedView( viewNames[sel] );
				}
				break;
			default :
		}
		viewer.refresh( );
	}

}
