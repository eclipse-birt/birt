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

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormPage;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FilterListDialog extends BaseDialog
{

	private FormPage filterForm;

	public FilterListDialog( )
	{
		super( Messages.getString( "FilterListDialog.Shell.Title" ) ); //$NON-NLS-1$
	}

	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.CUBE_FILTER_LIST_DIALOG ); //$NON-NLS-1$

		Composite dialogArea = (Composite) super.createDialogArea( parent );

		Composite content = new Composite( dialogArea, SWT.NONE );
		content.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		layout.marginRight = 0;
		content.setLayout( layout );
		createFilterArea( content );

		init( );

		return dialogArea;
	}

	private ReportElementHandle input;

	public void setInput( ReportElementHandle input )
	{
		this.input = input;
	}

	private void init( )
	{
		List inputs = new ArrayList( );
		inputs.add( input );
		filterForm.setInput( inputs );
	}

	private void createFilterArea( Composite content )
	{
		filterForm = new FormPage( content,
				FormPage.FULL_FUNCTION,
				new FilterHandleProvider( ) {

					public int[] getColumnWidths( )
					{
						return new int[]{
								100, 100, 100, 100
						};
					}

					public boolean doAddItem( int pos )
							throws SemanticException
					{
						// return modelAdapter.doAddItem( input.get( 0 ), pos );
						Object item = input.get( 0 );
						if ( item instanceof DesignElementHandle )
						{
							DatasetFilterConditionBuilder dialog = new DatasetFilterConditionBuilder( UIUtil.getDefaultShell( ),
									FilterConditionBuilder.DLG_TITLE_NEW,
									FilterConditionBuilder.DLG_MESSAGE_NEW );
							dialog.setDesignHandle( (DesignElementHandle) item,
									new CubeExpressionProvider( (DesignElementHandle) item ) );
							dialog.setInput( null );
							dialog.setBindingParams( bindingParams );
							if ( item instanceof ReportItemHandle )
							{
								dialog.setReportElement( (ReportItemHandle) item );
							}
							else if ( item instanceof GroupHandle )
							{
								dialog.setReportElement( (ReportItemHandle) ( (GroupHandle) item ).getContainer( ) );
							}
							if ( dialog.open( ) == Dialog.CANCEL )
							{
								return false;
							}

						}
						return true;
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.IFormHandleProvider#doEditItem(int)
					 */
					public boolean doEditItem( int pos )
					{

						Object item = input.get( 0 );
						if ( item instanceof DesignElementHandle )
						{
							DesignElementHandle element = (DesignElementHandle) item;
							PropertyHandle propertyHandle = element.getPropertyHandle( ListingHandle.FILTER_PROP );
							FilterConditionHandle filterHandle = (FilterConditionHandle) ( propertyHandle.getAt( pos ) );
							if ( filterHandle == null )
							{
								return false;
							}

							FilterConditionBuilder dialog = new FilterConditionBuilder( UIUtil.getDefaultShell( ),
									FilterConditionBuilder.DLG_TITLE_EDIT,
									FilterConditionBuilder.DLG_MESSAGE_EDIT );
							dialog.setDesignHandle( (DesignElementHandle) item,
									new CubeExpressionProvider( (DesignElementHandle) item ) );
							dialog.setInput( filterHandle );
							dialog.setBindingParams( bindingParams );
							if ( item instanceof ReportItemHandle )
							{
								dialog.setReportElement( (ReportItemHandle) item );
							}
							else if ( item instanceof GroupHandle )
							{
								dialog.setReportElement( (ReportItemHandle) ( (GroupHandle) item ).getContainer( ) );
							}
							if ( dialog.open( ) == Dialog.CANCEL )
							{
								return false;
							}

						}
						return true;
					}
				},
				true ) {

			protected void fullLayout( )
			{
				FormLayout layout = new FormLayout( );
				layout.marginHeight = WidgetUtil.SPACING;
				layout.marginWidth = WidgetUtil.SPACING;
				layout.spacing = WidgetUtil.SPACING;
				setLayout( layout );

				int btnWidth = 60;
				FormData data = new FormData( );
				data.right = new FormAttachment( 100 );
				data.top = new FormAttachment( 0, 0 );
				data.width = Math.max( btnWidth,
						btnAdd.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
				btnAdd.setLayoutData( data );

				data = new FormData( );
				data.top = new FormAttachment( btnAdd, 0, SWT.BOTTOM );
				data.left = new FormAttachment( btnAdd, 0, SWT.LEFT );
				data.width = Math.max( btnWidth,
						btnEdit.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
				btnEdit.setLayoutData( data );

				data = new FormData( );
				data.top = new FormAttachment( btnEdit, 0, SWT.BOTTOM );
				data.left = new FormAttachment( btnEdit, 0, SWT.LEFT );
				data.width = Math.max( btnWidth,
						btnDel.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
				btnDel.setLayoutData( data );

				data = new FormData( );
				data.top = new FormAttachment( btnDel, 0, SWT.BOTTOM );
				data.left = new FormAttachment( btnDel, 0, SWT.LEFT );
				data.width = Math.max( btnWidth,
						btnUp.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
				btnUp.setLayoutData( data );
				btnUp.setVisible( false );

				data = new FormData( );
				data.top = new FormAttachment( btnUp, 0, SWT.BOTTOM );
				data.left = new FormAttachment( btnUp, 0, SWT.LEFT );
				data.width = Math.max( btnWidth,
						btnDown.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
				btnDown.setLayoutData( data );
				btnDown.setVisible( false );

				data = new FormData( );
				data.top = new FormAttachment( btnAdd, 0, SWT.TOP );
				data.bottom = new FormAttachment( 100 );
				data.left = new FormAttachment( title, 0, SWT.LEFT );
				data.right = new FormAttachment( btnAdd, 0, SWT.LEFT );
				table.setLayoutData( data );

				title.setVisible( false );
			}
		};

	}
}
