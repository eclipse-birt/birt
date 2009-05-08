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
package org.eclipse.birt.report.designer.data.ui.datasource;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ExportDataSourceDialog extends BaseDialog
{
	private DataSourceHandle dataSourceHandle;
	private boolean isSelected = false;
	
    protected ExportDataSourceDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	public ExportDataSourceDialog( Shell parentShell, String title,
			DataSourceHandle selection )
	{
		this( parentShell, title );
		this.dataSourceHandle = selection;
	}


	protected Control createDialogArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.None );

		GridLayout parentLayout = new GridLayout( );
		parentLayout.marginLeft = parentLayout.marginTop = parentLayout.marginRight = parentLayout.marginBottom = 10;
		composite.setLayout( parentLayout );
		GridData data = new GridData( GridData.FILL_BOTH );
		composite.setLayoutData( data );
		
		Label message = new Label( composite, SWT.BOLD );
		message.setFont( new Font( Display.getCurrent( ),
				"Times New Roman",
				13,
				SWT.BOLD ) );
		message.setText( Messages.getFormattedString( "datasource.exportToCP.message",
				new Object[]{
					this.dataSourceHandle.getQualifiedName( )
				} ) );

		Label separator = new Label( composite, SWT.HORIZONTAL | SWT.SEPARATOR );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.verticalIndent = 10;
		separator.setLayoutData( gd );

		Button button = new Button( composite, SWT.CHECK );
		button.setText( Messages.getString( "datasource.exportToCP.checkBox" ) );
		setResult( isSelected );
		button.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent arg0 )
			{
			}

			public void widgetSelected( SelectionEvent arg0 )
			{
				isSelected = !isSelected;
				setResult( isSelected );
			}
		} );
		return parent;
	}
    
}
