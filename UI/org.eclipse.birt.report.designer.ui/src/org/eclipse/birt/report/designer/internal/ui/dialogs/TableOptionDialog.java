/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Base Class for TableOption Dialog
 *  
 */
public class TableOptionDialog extends Dialog
{

	private Text textEditor;

	private Text lineEditor;

	private Text columnEditor;

	private HashMap map = new HashMap( );

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public TableOptionDialog( Shell parentShell )
	{
		super( parentShell );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		( (GridLayout) composite.getLayout( ) ).numColumns = 2;

		new Label( composite, SWT.LEFT ).setText( "Text:" );
		textEditor = new Text( composite, SWT.BORDER | SWT.SINGLE );
		textEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( composite, SWT.CENTER ).setText( "Row:" );
		lineEditor = new Text( composite, SWT.BORDER | SWT.SINGLE );
		lineEditor.setLayoutData( new GridData( ) );
		lineEditor.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				e.doit = ( "0123456789".indexOf( e.text ) >= 0 );
			}
		} );

		new Label( composite, SWT.RIGHT ).setText( "Column:" );
		columnEditor = new Text( composite, SWT.BORDER | SWT.SINGLE );
		columnEditor.setLayoutData( new GridData( ) );
		columnEditor.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				e.doit = ( "0123456789".indexOf( e.text ) >= 0 );
			}
		} );

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		String tableName = textEditor.getText( );
		Integer rowCount;
		try
		{
			rowCount = Integer.valueOf( lineEditor.getText( ) );
		}
		catch ( NumberFormatException e )
		{
			rowCount = new Integer( -1 );
		}
		Integer columnCount;
		try
		{
			columnCount = Integer.valueOf( columnEditor.getText( ) );
		}
		catch ( NumberFormatException e )
		{
			columnCount = new Integer( -1 );
		}
		map.put( "TableName", tableName );
		map.put( "RowCount", rowCount );
		map.put( "ColumnCount", columnCount );
		super.okPressed( );
	}

	/**
	 * Returns the property map.
	 * 
	 * @return property map
	 */
	public HashMap getPropertyMap( )
	{
		if ( map.size( ) == 0 )
		{
			map.put( "TableName", "" );
			map.put( "RowCount", new Integer( -1 ) );
			map.put( "ColumnCount", new Integer( -1 ) );
		}
		return map;
	}
}