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

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ui.dialogs.widget.FormPage;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.IFormHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The class is responsible to set filter conditions.
 * 
 */
public class ExtendedItemFilterDialog extends TrayDialog
{

	/** The report item handle. */
	private ExtendedItemHandle fReportItemHandle;

	/** The field will provide all controls and operations for filter setting. */
	private IFormHandleProvider fFilterHandleProvider = new FilterHandleProvider( ) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider#getColumnWidths()
		 */
		public int[] getColumnWidths( )
		{
			return new int[]{
					150, 100, 150, 150
			// Default width of columns.
			};
		}
	};

	/**
	 * Consturctor of the class.
	 * 
	 * @param reportItemHandle
	 */
	public ExtendedItemFilterDialog( ExtendedItemHandle reportItemHandle )
	{
		super( UIUtil.getDefaultShell( ) );
		setShellStyle( SWT.RESIZE );
		fReportItemHandle = reportItemHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#setShellStyle(int)
	 */
	protected void setShellStyle( int newShellStyle )
	{
		super.setShellStyle( newShellStyle
				| SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		// Binding default help.
		ChartUIUtil.bindHelp( parent,
				ChartHelpContextIds.DIALOG_DATA_SET_FILTER );
		getShell( ).setText( Messages.getString( "dataset.editor.filters" ) ); //$NON-NLS-1$

		// Create filter page.
		Composite composite = (Composite) super.createDialogArea( parent );
		GridLayout gl = new GridLayout( );
		composite.setLayout( gl );

		FormPage filterFormPage = new FormPage( composite,
				FormPage.FULL_FUNCTION,
				fFilterHandleProvider,
				true );
		filterFormPage.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		List handleList = new ArrayList( );
		handleList.add( fReportItemHandle );
		filterFormPage.setInput( handleList );

		return composite;
	}
}
