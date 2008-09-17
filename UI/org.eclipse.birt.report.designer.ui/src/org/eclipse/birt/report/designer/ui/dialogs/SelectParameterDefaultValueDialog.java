/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.util.ULocale;

/**
 * Presents a list of values from dataset, allows user to select to define
 * default value for dynamic parameter
 * 
 */
public class SelectParameterDefaultValueDialog extends BaseDialog
{

	//	private static final String STANDARD_DATE_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss a"; //$NON-NLS-1$
	private List selectValueList = null;
	private java.util.List columnValueList = new ArrayList( );
	private final String NULL_VALUE_DISPLAY = Messages.getString( "SelectValueDialog.SelectValue.NullValue" ); //$NON-NLS-1$

	public SelectParameterDefaultValueDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	public void setColumnValueList( Collection valueList )
	{
		columnValueList.clear( );
		columnValueList.addAll( valueList );
	}

	public String getSelectedValue( )
	{
		String[] result = (String[]) getResult( );
		return ( result != null && result.length > 0 ) ? result[0] : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		Label label = new Label( composite, SWT.NONE );
		label.setText( Messages.getString( "SelectParameterDefaultValueDialog.Title" ) ); //$NON-NLS-1$

		selectValueList = new List( composite, SWT.SINGLE
				| SWT.V_SCROLL
				| SWT.H_SCROLL );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.heightHint = 250;
		data.widthHint = 300;
		selectValueList.setLayoutData( data );
		// selectValueList.add( Messages.getString(
		// "SelectValueDialog.retrieving" ) ); //$NON-NLS-1$
		selectValueList.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
			{
				if ( selectValueList.getSelectionCount( ) > 0 )
				{
					okPressed( );
				}
			}
		} );

		PlatformUI.getWorkbench( ).getDisplay( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				populateList( );
			}

		} );

		UIUtil.bindHelp( parent,
				IHelpContextIds.SELECT_PARAMETER_DEFAULT_VALUE_DIALOG_ID );
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		String[] selection = selectValueList.getSelection( );
		int index = selectValueList.getSelectionIndex( );
		if ( selection.length > 0
				&& NULL_VALUE_DISPLAY.equals( selection[0] )
				&& columnValueList.get( index ) == null )
		{
			setResult( null );

		}
		else
		{
			setResult( selectValueList.getSelection( ) );
		}

		super.okPressed( );
	}

	private String convertToStandardFormat( Date date )
	{
		if ( date == null )
		{
			return null;
		}
		if ( date instanceof java.sql.Date )
		{
			return new DateFormatter( "yyyy-MM-dd", ULocale.US ).format( date ); //$NON-NLS-1$
		}
		else if ( date instanceof java.sql.Time )
		{
			return new DateFormatter( "HH:mm:ss", ULocale.US ).format( date ); //$NON-NLS-1$
		}
		else
		{
			return new DateFormatter( "yyyy-MM-dd HH:mm:ss.SSS", ULocale.US ).format( date ); //$NON-NLS-1$
		}

	}

	/**
	 * populate all available value in selectValueList
	 */
	private void populateList( )
	{
		try
		{
			getOkButton( ).setEnabled( false );
			selectValueList.removeAll( );
			selectValueList.deselectAll( );
			if ( columnValueList != null )
			{
				Iterator iter = columnValueList.iterator( );
				while ( iter.hasNext( ) )
				{
					Object obj = iter.next( );
					String candiateValue = null;
					if ( obj == null )
					{
						candiateValue = NULL_VALUE_DISPLAY;
					}
					else if ( obj instanceof Date )
					{
						candiateValue = convertToStandardFormat( (Date) obj );
					}
					else
					{
						candiateValue = String.valueOf( obj );
					}

					selectValueList.add( candiateValue );
				}
			}
			if ( selectValueList.getItemCount( ) > 0 )
			{
				selectValueList.select( 0 );
				getOkButton( ).setEnabled( true );
			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}
}
