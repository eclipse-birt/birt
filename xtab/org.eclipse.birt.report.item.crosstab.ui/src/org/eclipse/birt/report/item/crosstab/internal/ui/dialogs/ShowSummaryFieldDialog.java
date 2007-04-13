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
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ShowSummaryFieldDialog extends BaseDialog
{

	public ShowSummaryFieldDialog( Shell parentShell )
	{
		super( parentShell, Messages.getString("ShowSummaryFieldDialog.Title") ); //$NON-NLS-1$
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite dialogArea = (Composite) super.createDialogArea( parent );

		Label infoLabel = new Label( dialogArea, SWT.NONE );
		infoLabel.setText( Messages.getString("ShowSummaryFieldDialog.Label.Info") ); //$NON-NLS-1$

		createSummaryFiledViewer( dialogArea );

		init( );

		return dialogArea;
	}

	private List input = new ArrayList( );
	private CheckboxTableViewer summaryFieldViewer;

	public void setInput( List input )
	{
		if ( input != null )
			this.input.addAll( input );
	}

	private void createSummaryFiledViewer( Composite dialogArea )
	{
		Table table = new Table( dialogArea, SWT.BORDER|SWT.SINGLE
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION
				| SWT.CHECK );
		table.setLinesVisible( false );
		table.setHeaderVisible( false );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		table.setLayoutData( gd );

		summaryFieldViewer = new CheckboxTableViewer( table );
		SummaryFieldProvider provider = new SummaryFieldProvider( );

		TableColumn column = new TableColumn( table, SWT.LEFT );
		column.setWidth( 200 );

		summaryFieldViewer.setUseHashlookup( true );
		summaryFieldViewer.setContentProvider( provider );
		summaryFieldViewer.setLabelProvider( provider );

		summaryFieldViewer.addCheckStateListener( new ICheckStateListener( ) {

			public void checkStateChanged( CheckStateChangedEvent event )
			{
				MeasureInfo info = (MeasureInfo) event.getElement( );
				if ( event.getChecked( ) )
				{
					info.setShow( true );
				}
				else
					info.setShow( false );
			}

		} );
	}

	private void init( )
	{
		if ( input != null )
		{
			summaryFieldViewer.setInput( input );
			for ( int i = 0; i < summaryFieldViewer.getTable( ).getItemCount( ); i++ )
			{
				TableItem item = summaryFieldViewer.getTable( ).getItem( i );
				if ( item.getData( ) != null
						&& item.getData( ) instanceof MeasureInfo )
				{
					item.setChecked( ( (MeasureInfo) item.getData( ) ).isShow( ) );
				}
			}
		}

	}
	
	public Object getResult(){
		return input;
	}

	class SummaryFieldProvider extends LabelProvider implements
			ITableLabelProvider,
			IStructuredContentProvider
	{

		public Image getColumnImage( Object element, int columnIndex )
		{
			return CrosstabUIHelper.getImage( CrosstabUIHelper.MEASURE_IMAGE );
		}

		public String getColumnText( Object element, int columnIndex )
		{
			if ( element instanceof MeasureInfo )
				return ( (MeasureInfo) element ).getMeasure( ) == null ? "" //$NON-NLS-1$
						: ( (MeasureInfo) element ).getMeasure( ).getName( );
			return ""; //$NON-NLS-1$
		}

		public Object[] getElements( Object inputElement )
		{
			if ( inputElement instanceof List )
				return ( (List) inputElement ).toArray( );
			return new Object[0];
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
			// TODO Auto-generated method stub

		}

	}

	/**
	 * GrandTotalInfo
	 */
	public static class MeasureInfo
	{

		private MeasureHandle measure;

		private boolean isShow = false;

		public MeasureInfo copy( )
		{
			MeasureInfo retValue = new MeasureInfo( );
			retValue.setShow( isShow( ) );
			retValue.setMeasure( getMeasure( ) );
			return retValue;
		}

		public void setShow( boolean show )
		{
			isShow = show;
		}

		public boolean isShow( )
		{
			return isShow;
		}

		public MeasureHandle getMeasure( )
		{
			return measure;
		}

		public void setMeasure( MeasureHandle measure )
		{
			this.measure = measure;
		}

//		public boolean isSameInfo( Object obj )
//		{
//			if ( !( obj instanceof MeasureInfo ) )
//			{
//				return false;
//			}
//			MeasureInfo temp = (MeasureInfo) obj;
//			return temp.getMeasure( ) == measure;
//		}
		
		public boolean equals( Object obj )
		{
			if ( !( obj instanceof MeasureInfo ) )
			{
				return false;
			}
			MeasureInfo temp = (MeasureInfo) obj;
			return temp.getMeasure( ) == measure && temp.isShow( ) == isShow;
		}
	}
}
