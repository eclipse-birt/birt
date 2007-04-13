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

import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.ui.widget.ComboBoxCellEditor;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.GrandTotalInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class GrandTotalProvider extends TotalProvider implements
		ICellModifier,
		ITableLabelProvider,
		IStructuredContentProvider
{

	TableViewer viewer;

	public GrandTotalProvider( TableViewer viewer )
	{
		this.viewer = viewer;
	}

	public String[] getColumnNames( )
	{
		return columnNames;
	}

	private CellEditor[] editors;
	private String[] columnNames = new String[]{
			"", Messages.getString("GrandTotalProvider.Column.DataField"), Messages.getString("GrandTotalProvider.Column.Function") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	};

	public CellEditor[] getEditors( Table table )
	{
		if ( editors == null )
		{
			editors = new CellEditor[columnNames.length];

			editors[2] = new ComboBoxCellEditor( table,
					new String[0],
					SWT.READ_ONLY );
			String[] items = getFunctionDisplayNames( );
			( (ComboBoxCellEditor) editors[2] ).setItems( items );

		}
		return editors;
	}

	

	public boolean canModify( Object element, String property )
	{
		int index = Arrays.asList( columnNames ).indexOf( property );
		if ( index == 2 )
			return true;
		else
			return false;
	}

	public Object getValue( Object element, String property )
	{
		int index = Arrays.asList( columnNames ).indexOf( property );
		String columnText = getColumnText( element, index );
		return columnText;
	}

	public void modify( Object element, String property, Object value )
	{
		int index = Arrays.asList( columnNames ).indexOf( property );
		if ( index == 2 )
		{
			if ( element instanceof TableItem )
			{
				Object obj = ( (TableItem) element ).getData( );
				if ( obj instanceof GrandTotalInfo )
				{
					String functionDisplayName = value.toString( );
					int functionIndex = Arrays.asList( getFunctionDisplayNames( ) )
							.indexOf( functionDisplayName );
					if ( functionIndex > -1
							&& functionIndex < getFunctionNames( ).length )
					{
						( (GrandTotalInfo) obj ).setFunction( getFunctionNames( )[functionIndex] );
					}
					viewer.refresh( );
				}
			}
		}

	}

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
				if ( info.getFunction( ) == null
						|| info.getFunction( ).trim( ).equals( "" ) ) //$NON-NLS-1$
					info.setFunction( getFunctionNames( )[0] );
				return getFunctionDisplayName( info.getFunction( ) );
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
				20, 120, 120, 120
		};
	}

}
