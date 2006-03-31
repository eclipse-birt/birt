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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog to choose the table/grid row/column number when create a table/grid.
 *  
 */
public class TableOptionDialog extends BaseDialog
{

	private static final String MSG_REMEMBER_DIMENSIONS_FOR_NEW_GRIDS = Messages.getString( "TableOptionDialog.message.RememberGrid" ); //$NON-NLS-1$
	private static final String MSG_REMEMBER_DIMENSIONS_FOR_NEW_TABLES = Messages.getString( "TableOptionDialog.message.RememberTable" ); //$NON-NLS-1$
	private static final String MSG_NUMBER_OF_GRID_ROWS = Messages.getString( "TableOptionDialog.text.GridRow" ); //$NON-NLS-1$
	private static final String MSG_NUMBER_OF_TABLE_ROWS = Messages.getString( "TableOptionDialog.text.TableDetail" ); //$NON-NLS-1$
	private static final String MSG_NUMBER_OF_COLUMNS = Messages.getString( "TableOptionDialog.text.Column" ); //$NON-NLS-1$
	private static final String MSG_GRID_SIZE = Messages.getString( "TableOptionDialog.text.GridSize" ); //$NON-NLS-1$
	private static final String MSG_TABLE_SIZE = Messages.getString( "TableOptionDialog.text.TableSize" ); //$NON-NLS-1$
	private static final String MSG_INSERT_GRID = Messages.getString( "TableOptionDialog.title.InsertGrid" ); //$NON-NLS-1$
	private static final String MSG_INSERT_TABLE = Messages.getString( "TableOptionDialog.title.InsertTable" ); //$NON-NLS-1$

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

	private SimpleSpinner rowEditor;

	private SimpleSpinner columnEditor;

	private Button chkbox;

	private int rowCount, columnCount;

	private boolean insertTable = true;

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
		columnEditor = new SimpleSpinner( innerPane, 0 );
		columnEditor.setText( String.valueOf( columnCount ) );
		columnEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( innerPane, SWT.NONE ).setText( insertTable ? MSG_NUMBER_OF_TABLE_ROWS
				: MSG_NUMBER_OF_GRID_ROWS );
		rowEditor = new SimpleSpinner( innerPane, 0 );
		rowEditor.setText( String.valueOf( rowCount ) );
		rowEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Label lb = new Label( composite, SWT.NONE );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.horizontalSpan = 2;
		lb.setLayoutData( gdata );

		chkbox = new Button( composite, SWT.CHECK );
		chkbox.setText( insertTable ? MSG_REMEMBER_DIMENSIONS_FOR_NEW_TABLES
				: MSG_REMEMBER_DIMENSIONS_FOR_NEW_GRIDS );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.horizontalSpan = 2;
		chkbox.setLayoutData( gdata );

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		try
		{
			rowCount = Integer.parseInt( rowEditor.getText( ) );
		}
		catch ( NumberFormatException e )
		{
			rowCount = insertTable ? DEFAULT_TABLE_ROW_COUNT
					: DEFAULT_ROW_COUNT;
		}

		try
		{
			columnCount = Integer.parseInt( columnEditor.getText( ) );
		}
		catch ( NumberFormatException e )
		{
			columnCount = DEFAULT_COLUMN_COUNT;
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

		setResult( new int[]{
				rowCount, columnCount
		} );

		if ( chkbox.getSelection( ) )
		{
			savePreference( );
		}

		super.okPressed( );
	}

	/**
	 * SimpleSpinner
	 */
	static class SimpleSpinner extends Composite
	{

		private static final int BUTTON_WIDTH = 16;

		private Text text;
		private Button up;
		private Button down;

		/**
		 * The constructor.
		 * 
		 * @param parent
		 * @param style
		 */
		public SimpleSpinner( Composite parent, int style )
		{
			super( parent, style );

			text = new Text( this, SWT.BORDER | SWT.SINGLE );
			text.addVerifyListener( new VerifyListener( ) {

				public void verifyText( VerifyEvent e )
				{
					if ( e.keyCode == 8 || e.keyCode == 127 )
					{
						e.doit = true;
						return;
					}

					try
					{
						if(e.text.length( ) != 0)
						{
							Integer.parseInt( e.text );
						}						
						e.doit = true;
					}
					catch ( Exception _ )
					{
						e.doit = false;
					}
				}
			} );
			text.addFocusListener( new FocusAdapter( ) {

				public void focusGained( FocusEvent e )
				{
					text.selectAll( );
				}
			} );

			up = new Button( this, style | SWT.ARROW | SWT.UP );
			down = new Button( this, style | SWT.ARROW | SWT.DOWN );

			up.addListener( SWT.Selection, new Listener( ) {

				public void handleEvent( Event e )
				{
					up( );
				}
			} );

			down.addListener( SWT.Selection, new Listener( ) {

				public void handleEvent( Event e )
				{
					down( );
				}
			} );

			addListener( SWT.Resize, new Listener( ) {

				public void handleEvent( Event e )
				{
					resize( );
				}
			} );

			addListener( SWT.FocusIn, new Listener( ) {

				public void handleEvent( Event e )
				{
					focusIn( );
				}
			} );

		}

		void setText( String val )
		{
			if ( text != null )
			{
				text.setText( val );
			}
		}

		String getText( )
		{
			if ( text != null )
			{
				return text.getText( );
			}

			return null;
		}

		void up( )
		{
			if ( text != null )
			{
				try
				{
					int v = Integer.parseInt( text.getText( ) );
					text.setText( String.valueOf( v + 1 ) );
				}
				catch ( NumberFormatException e )
				{
					text.setText( String.valueOf( 1 ) );
				}
			}
		}

		/**
		 * Processes down action
		 */
		void down( )
		{
			if ( text != null )
			{
				try
				{
					int v = Integer.parseInt( text.getText( ) );
					if ( v < 2 )
					{
						v = 2;
					}
					text.setText( String.valueOf( v - 1 ) );
				}
				catch ( NumberFormatException e )
				{
					text.setText( String.valueOf( 1 ) );
				}
			}
		}

		void focusIn( )
		{
			if ( text != null )
			{
				text.setFocus( );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.widgets.Control#computeSize(int, int, boolean)
		 */
		public Point computeSize( int wHint, int hHint, boolean changed )
		{
			return new Point( 80, 20 );
		}

		void resize( )
		{
			Point pt = computeSize( -1, -1 );

			setSize( pt );

			int textWidth = pt.x - BUTTON_WIDTH;
			text.setBounds( 0, 0, textWidth, pt.y );

			int buttonHeight = pt.y / 2;
			up.setBounds( textWidth, 0, BUTTON_WIDTH, buttonHeight );
			down.setBounds( textWidth,
					pt.y - buttonHeight,
					BUTTON_WIDTH,
					buttonHeight );
		}

	}

}