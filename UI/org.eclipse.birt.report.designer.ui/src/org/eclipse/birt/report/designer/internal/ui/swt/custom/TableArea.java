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

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * 
 */

public class TableArea extends Composite
{

	private static final String BUTTON_NEW = "&New...";
	private static final String BUTTON_EDIT = "&Edit...";
	private static final String BUTTON_REMOVE = "&Remove";
	private static final String BUTTON_UP = "&Up";
	private static final String BUTTON_DOWN = "D&own";

	private TableViewer tableViewer;
	private ITableAreaModifier modifier;

	private Button newButton, editButton, removeButton, upButton, downButton;

	public TableArea( Composite parent, int tableStyle,
			ITableAreaModifier modifier )
	{
		super( parent, SWT.NONE );
		Assert.isNotNull( modifier );
		setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );
		this.modifier = modifier;
		createTableViewer( tableStyle );
		createButtonBar( );
	}

	private void createTableViewer( int tableStyle )
	{
		Table table = new Table( this, tableStyle
				| SWT.FULL_SELECTION
				| SWT.BORDER );
		table.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		tableViewer = new TableViewer( table );
		table.addKeyListener( new KeyAdapter( ) {

			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			public void keyReleased( KeyEvent e )
			{
				if ( !getSelection( ).isEmpty( )
						&& modifier.removeItem( getSelection( ).toArray( ) ) )
				{
					tableViewer.refresh( );
					updateButtons( );
				}
			}
		} );
	}

	private void createButtonBar( )
	{
		Composite buttonBar = new Composite( this, SWT.NONE );
		buttonBar.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );
		buttonBar.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );

		newButton = new Button( buttonBar, SWT.PUSH );
		newButton.setText( BUTTON_NEW );
		setButtonLayout( newButton );
		newButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( modifier.newItem( ) )
				{
					tableViewer.refresh( );
					updateButtons( );
				}
			}
		} );

		editButton = new Button( buttonBar, SWT.PUSH );
		editButton.setText( BUTTON_EDIT );
		setButtonLayout( editButton );
		editButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( modifier.editItem( getSelection( ).getFirstElement( ) ) )
				{
					tableViewer.refresh( );
					updateButtons( );
				}
			}
		} );
		editButton.setEnabled( false );

		removeButton = new Button( buttonBar, SWT.PUSH );
		removeButton.setText( BUTTON_REMOVE );
		setButtonLayout( removeButton );
		removeButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{

				if ( modifier.removeItem( getSelection( ).toArray( ) ) )
				{
					tableViewer.refresh( );
					updateButtons( );
				}
			}
		} );
		removeButton.setEnabled( false );

		tableViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
			}

		} );

		if ( modifier instanceof ISortedTableAreaModifier )
		{
			upButton = new Button( buttonBar, SWT.PUSH );
			upButton.setText( BUTTON_UP );
			setButtonLayout( upButton );
			upButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection( );
					if ( ( (ISortedTableAreaModifier) modifier ).moveUp( selection.getFirstElement( ) ) )
					{
						tableViewer.refresh( );
						updateButtons( );
					}
				}
			} );
			upButton.setEnabled( false );

			downButton = new Button( buttonBar, SWT.PUSH );
			downButton.setText( BUTTON_DOWN );
			setButtonLayout( downButton );
			downButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection( );
					if ( ( (ISortedTableAreaModifier) modifier ).moveDown( selection.getFirstElement( ) ) )
					{
						tableViewer.refresh( );
						updateButtons( );
					}
				}
			} );
			downButton.setEnabled( false );

		}

		tableViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				updateButtons( );
			}

		} );

	}

	private void setButtonLayout( Button button )
	{
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		button.setLayoutData( gd );
	}

	private IStructuredSelection getSelection( )
	{
		return (IStructuredSelection) tableViewer.getSelection( );
	}

	private void updateButtons( )
	{
		boolean enable = ( getSelection( ).size( ) == 1 );
		editButton.setEnabled( enable );
		if ( modifier instanceof ISortedTableAreaModifier )
		{
			int index = tableViewer.getTable( ).getSelectionIndex( );
			upButton.setEnabled( enable && index != 0 );
			downButton.setEnabled( enable
					&& index != tableViewer.getTable( ).getItemCount( ) - 1 );
		}
		removeButton.setEnabled( !getSelection( ).isEmpty( ) );
	}

	public Table getTable( )
	{
		if ( tableViewer != null )
		{
			return tableViewer.getTable( );
		}
		return null;
	}

	public TableViewer getTableViewer( )
	{
		return tableViewer;
	}

	public void setInput( Object input )
	{
		tableViewer.setInput( input );
		updateButtons( );
	}
}
