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

import java.util.List;

import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.SubTotalInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class SubTotalProvider extends TotalProvider implements
		ITableLabelProvider,
		IStructuredContentProvider
{

	TableViewer viewer;

	public SubTotalProvider( TableViewer viewer )
	{
		this.viewer = viewer;
	}

	public String[] getColumnNames( )
	{
		return columnNames;
	}

	//private CellEditor[] editors;
	private String[] columnNames = new String[]{
			"", Messages.getString("SubTotalProvider.Column.AggregateOn"),//Messages.getString("SubTotalProvider.Column.DataField"), Messages.getString("SubTotalProvider.Column.Function") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	};

	/*
	public CellEditor[] getEditors( Table table )
	{
		if ( editors == null )
		{
			editors = new CellEditor[columnNames.length];

			editors[3] = new ComboBoxCellEditor( table,
					new String[0],
					SWT.READ_ONLY );
			String[] items = getFunctionDisplayNames( );
			( (ComboBoxCellEditor) editors[3] ).setItems( items );

		}
		return editors;
	}
*/
	/*
	public boolean canModify( Object element, String property )
	{
		int index = Arrays.asList( columnNames ).indexOf( property );
		if ( index == 3 )
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
		if ( index == 3 )
		{
			if ( element instanceof TableItem )
			{
				Object obj = ( (TableItem) element ).getData( );
				if ( obj instanceof SubTotalInfo )
				{
					String functionDisplayName = value.toString( );
					int functionIndex = Arrays.asList( getFunctionDisplayNames( ) )
							.indexOf( functionDisplayName );
					if ( functionIndex > -1
							&& functionIndex < getFunctionNames( ).length )
					{
						( (SubTotalInfo) obj ).setFunction( getFunctionNames( )[functionIndex] );
					}
					viewer.refresh( );
				}
			}
		}

	}
*/
	public Image getColumnImage( Object element, int columnIndex )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText( Object element, int columnIndex )
	{
		SubTotalInfo info = (SubTotalInfo) element;
		switch ( columnIndex )
		{
			case 0 :
				return ""; //$NON-NLS-1$
			case 1 :
				return info.getLevel( ).getName( )+"- "+(info.getAggregateOnMeasure( ) == null ? "" //$NON-NLS-1$
						: info.getAggregateOnMeasure( ).getName( ));
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
				20, 300
		};
	}

}
