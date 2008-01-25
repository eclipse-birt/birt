/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * Dialog to choose the table/grid row/column number when create a table/grid.
 * 
 */
public class TableOptionDialog extends BaseDialog
{

	private static final String MSG_DATA_SET = Messages.getString( "TableOptionDialog.text.DataSet" ); //$NON-NLS-1$

	private static final String MSG_REMEMBER_DIMENSIONS_FOR_NEW_GRIDS = Messages.getString( "TableOptionDialog.message.RememberGrid" ); //$NON-NLS-1$

	private static final String MSG_REMEMBER_DIMENSIONS_FOR_NEW_TABLES = Messages.getString( "TableOptionDialog.message.RememberTable" ); //$NON-NLS-1$

	private static final String MSG_NUMBER_OF_GRID_ROWS = Messages.getString( "TableOptionDialog.text.GridRow" ); //$NON-NLS-1$

	private static final String MSG_NUMBER_OF_TABLE_ROWS = Messages.getString( "TableOptionDialog.text.TableDetail" ); //$NON-NLS-1$

	private static final String MSG_NUMBER_OF_COLUMNS = Messages.getString( "TableOptionDialog.text.Column" ); //$NON-NLS-1$

	private static final String MSG_GRID_SIZE = Messages.getString( "TableOptionDialog.text.GridSize" ); //$NON-NLS-1$

	private static final String MSG_TABLE_SIZE = Messages.getString( "TableOptionDialog.text.TableSize" ); //$NON-NLS-1$

	private static final String MSG_INSERT_GRID = Messages.getString( "TableOptionDialog.title.InsertGrid" ); //$NON-NLS-1$

	private static final String MSG_INSERT_TABLE = Messages.getString( "TableOptionDialog.title.InsertTable" ); //$NON-NLS-1$

	private static final String NONE = Messages.getString( "BindingPage.None" );//$NON-NLS-1$

	private static final int DEFAULT_TABLE_ROW_COUNT = 1;

	private static final int DEFAULT_ROW_COUNT = 3;

	private static final int DEFAULT_COLUMN_COUNT = 3;

	/**
	 * Comment for <code>DEFAULT_TABLE_ROW_COUNT_KEY</code>
	 */
	public static final String DEFAULT_TABLE_ROW_COUNT_KEY = "Default table row count"; //$NON-NLS-1$

	/**
	 * Comment for <code>DEFAULT_TABLE_COLUMN_COUNT_KEY</code>
	 */
	public static final String DEFAULT_TABLE_COLUMN_COUNT_KEY = "Default table column count"; //$NON-NLS-1$

	/**
	 * Comment for <code>DEFAULT_GRID_ROW_COUNT_KEY</code>
	 */
	public static final String DEFAULT_GRID_ROW_COUNT_KEY = "Default grid row count"; //$NON-NLS-1$

	/**
	 * Comment for <code>DEFAULT_GRID_COLUMN_COUNT_KEY</code>
	 */
	public static final String DEFAULT_GRID_COLUMN_COUNT_KEY = "Default grid column count"; //$NON-NLS-1$

	private Spinner rowEditor;

	private Spinner columnEditor;

	private Button chkbox;

	private int rowCount, columnCount;

	private boolean insertTable = true;

	private Combo dataSetCombo;

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public TableOptionDialog( Shell parentShell, boolean insertTable )
	{
		super( parentShell, insertTable ? MSG_INSERT_TABLE : MSG_INSERT_GRID );

		this.insertTable = insertTable;
	}

	private void loadPreference( )
	{
		if ( insertTable )
		{
			columnCount = ReportPlugin.getDefault( )
					.getPreferenceStore( )
					.getInt( DEFAULT_TABLE_COLUMN_COUNT_KEY );
			rowCount = ReportPlugin.getDefault( )
					.getPreferenceStore( )
					.getInt( DEFAULT_TABLE_ROW_COUNT_KEY );
		}
		else
		{
			columnCount = ReportPlugin.getDefault( )
					.getPreferenceStore( )
					.getInt( DEFAULT_GRID_COLUMN_COUNT_KEY );
			rowCount = ReportPlugin.getDefault( )
					.getPreferenceStore( )
					.getInt( DEFAULT_GRID_ROW_COUNT_KEY );
		}

		if ( columnCount <= 0 )
		{
			columnCount = DEFAULT_COLUMN_COUNT;
		}
		if ( rowCount <= 0 )
		{
			rowCount = insertTable ? DEFAULT_TABLE_ROW_COUNT
					: DEFAULT_ROW_COUNT;
		}

	}

	private void savePreference( )
	{
		if ( insertTable )
		{
			ReportPlugin.getDefault( )
					.getPreferenceStore( )
					.setValue( DEFAULT_TABLE_COLUMN_COUNT_KEY, columnCount );
			ReportPlugin.getDefault( )
					.getPreferenceStore( )
					.setValue( DEFAULT_TABLE_ROW_COUNT_KEY, rowCount );
		}
		else
		{
			ReportPlugin.getDefault( )
					.getPreferenceStore( )
					.setValue( DEFAULT_GRID_COLUMN_COUNT_KEY, columnCount );
			ReportPlugin.getDefault( )
					.getPreferenceStore( )
					.setValue( DEFAULT_GRID_ROW_COUNT_KEY, rowCount );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */

	protected Control createDialogArea( Composite parent )
	{
		loadPreference( );

		Composite composite = (Composite) super.createDialogArea( parent );
		( (GridLayout) composite.getLayout( ) ).numColumns = 2;

		new Label( composite, SWT.NONE ).setText( insertTable ? MSG_TABLE_SIZE
				: MSG_GRID_SIZE );
		Label sp = new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL );
		sp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite innerPane = new Composite( composite, SWT.NONE );
		GridData gdata = new GridData( GridData.FILL_BOTH );
		gdata.horizontalSpan = 2;
		innerPane.setLayoutData( gdata );
		GridLayout glayout = new GridLayout( 2, false );
		glayout.marginWidth = 10;
		innerPane.setLayout( glayout );

		new Label( innerPane, SWT.NONE ).setText( MSG_NUMBER_OF_COLUMNS );
		columnEditor = new Spinner( innerPane, SWT.BORDER );
		columnEditor.setMinimum( 1 );
		columnEditor.setMaximum( Integer.MAX_VALUE );
		columnEditor.setIncrement( 1 );
		columnEditor.setSelection( columnCount );
		columnEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( innerPane, SWT.NONE ).setText( insertTable ? MSG_NUMBER_OF_TABLE_ROWS
				: MSG_NUMBER_OF_GRID_ROWS );
		rowEditor = new Spinner( innerPane, SWT.BORDER );
		rowEditor.setMinimum( 1 );
		rowEditor.setMaximum( Integer.MAX_VALUE );
		rowEditor.setIncrement( 1 );
		rowEditor.setSelection( rowCount );
		rowEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		if ( insertTable )
		{
			new Label( innerPane, SWT.NONE ).setText( MSG_DATA_SET );
			dataSetCombo = new Combo( innerPane, SWT.BORDER
					| SWT.SINGLE
					| SWT.READ_ONLY );
			dataSetCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			String[] dataSets = ChoiceSetFactory.getDataSets( );
			String[] newList = new String[dataSets.length + 1];
			System.arraycopy( dataSets, 0, newList, 1, dataSets.length );
			newList[0] = NONE;
			dataSetCombo.setItems( newList );
			dataSetCombo.select( 0 );
		}

		else
		{
			Label lb = new Label( composite, SWT.NONE );
			gdata = new GridData( GridData.FILL_HORIZONTAL );
			gdata.horizontalSpan = 2;
			lb.setLayoutData( gdata );
		}
		chkbox = new Button( composite, SWT.CHECK );
		chkbox.setText( insertTable ? MSG_REMEMBER_DIMENSIONS_FOR_NEW_TABLES
				: MSG_REMEMBER_DIMENSIONS_FOR_NEW_GRIDS );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.horizontalSpan = 2;
		chkbox.setLayoutData( gdata );

		if ( insertTable )
		{
			UIUtil.bindHelp( parent, IHelpContextIds.TABLE_OPTION_DIALOG_ID );
		}
		else
		{
			UIUtil.bindHelp( parent, IHelpContextIds.Grid_OPTION_DIALOG_ID );
		}

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		rowCount = rowEditor.getSelection( );
		columnCount = columnEditor.getSelection( );

		if ( columnCount <= 0 )
		{
			columnCount = DEFAULT_COLUMN_COUNT;
		}
		if ( rowCount <= 0 )
		{
			rowCount = insertTable ? DEFAULT_TABLE_ROW_COUNT
					: DEFAULT_ROW_COUNT;
		}

		if ( insertTable )
		{
			setResult( new Object[]{
					new Integer(rowCount),
					new Integer(columnCount),
					dataSetCombo.getItem( dataSetCombo.getSelectionIndex( ) )
							.toString( )
			} );
		}
		else
			setResult( new Object[]{
					new Integer(rowCount), new Integer(columnCount)
			} );

		if ( chkbox.getSelection( ) )
		{
			savePreference( );
		}

		super.okPressed( );
	}
}
