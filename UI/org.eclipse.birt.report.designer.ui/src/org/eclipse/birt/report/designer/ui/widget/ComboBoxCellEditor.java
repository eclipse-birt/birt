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

package org.eclipse.birt.report.designer.ui.widget;

import java.util.Arrays;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ComboBoxCellEditor extends CellEditor
{

	/**
	 * The ComboBox to keep the system defined and customer defined colors
	 */
	private CCombo comboBox;

	/**
	 * The list of items to present in the combo box.
	 */
	private String[] items;

	/**
	 * The zero-based index of the selected item.
	 */
	int selection;

	/**
	 * The composite to keep the combobox and button together
	 */
	private Composite composite;

	private int inProcessing = 0;

	/**
	 * Creates a new dialog cell editor parented under the given control. The
	 * combobox lists is <code>null</code> initially
	 * 
	 * @param parent
	 *            the parent control
	 */
	public ComboBoxCellEditor( Composite parent )
	{
		super( parent );
		setStyle( defaultStyle );
	}

	/**
	 * Creates a new dialog cell editor parented under the given control. The
	 * combo box box lists is initialized with the items parameter
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the initilizing combobox list
	 */
	public ComboBoxCellEditor( Composite parent, String[] items )
	{
		this( parent, items, defaultStyle );
	}

	/**
	 * Creates a new dialog cell editor parented under the given control and
	 * givend style. The combo box box lists is initialized with the items
	 * parameter
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the initilizing combobox list
	 * @param style
	 *            the style of this editor
	 */
	public ComboBoxCellEditor( Composite parent, String[] items, int style )
	{
		super( parent, style );
		if ( items != null )
		{
			Arrays.sort( items );
		}
		setItems( items );
	}

	/**
	 * Returns the list of choices for the combo box
	 * 
	 * @return the list of choices for the combo box
	 */
	public String[] getItems( )
	{
		return this.items;
	}

	/**
	 * Sets the list of choices for the combo box
	 * 
	 * @param items
	 *            the list of choices for the combo box
	 */
	public void setItems( String[] items )
	{
		Assert.isNotNull( items );
		this.items = items;
		populateComboBoxItems( );
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems( )
	{
		if ( comboBox != null && items != null )
		{
			comboBox.removeAll( );
			for ( int i = 0; i < items.length; i++ )
				comboBox.add( items[i], i );

			setValueValid( true );
			selection = 0;
		}
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected Control createContents( final Composite cell )
	{

		Color bg = cell.getBackground( );

		cell.addFocusListener( new FocusAdapter( ) {

			public void focusLost( FocusEvent e )
			{
				ComboBoxCellEditor.this.focusLost( );
			}
		} );
		composite = new Composite( cell, getStyle( ) );
		composite.setBackground( bg );
		composite.setLayout( new FillLayout( ) );

		comboBox = new CCombo( composite, getStyle( ) );
		comboBox.setBackground( bg );
		comboBox.setFont( cell.getFont( ) );

		comboBox.addSelectionListener( new SelectionAdapter( ) {

			public void widgetDefaultSelected( SelectionEvent event )
			{
				applyEditorValueAndDeactivate( );
			}

			public void widgetSelected( SelectionEvent event )
			{
				applyEditorValueAndDeactivate( );
			}
		} );

		comboBox.addKeyListener( new KeyAdapter( ) {

			// hook key pressed - see PR 14201
			public void keyPressed( KeyEvent e )
			{
				keyReleaseOccured( e );
			}
		} );

		comboBox.addTraverseListener( new TraverseListener( ) {

			public void keyTraversed( TraverseEvent e )
			{
				if ( e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN )
				{
					e.doit = false;
				}
			}
		} );

		comboBox.addFocusListener( new FocusAdapter( ) {

			public void focusLost( FocusEvent e )
			{
				ComboBoxCellEditor.this.focusLost( );
			}
		} );

		return composite;
	}

	/**
	 * Applies the currently selected value and deactiavates the cell editor
	 */
	void applyEditorValueAndDeactivate( )
	{
		inProcessing = 1;
		doValueChanged( );
		fireApplyEditorValue( );
		deactivate( );
		inProcessing = 0;
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected void updateContents( Object value )
	{
		if ( comboBox == null )
			return;

		String text = "";//$NON-NLS-1$
		if ( value != null )
		{
			text = value.toString( );
		}

		comboBox.setText( text );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
	 */
	protected void keyReleaseOccured( KeyEvent keyEvent )
	{
		if ( keyEvent.character == '\u001b' )
		{ // Escape character
			fireCancelEditor( );
		}
		else if ( keyEvent.character == '\t' )
		{ // tab key
			applyEditorValueAndDeactivate( );
		}
		else if ( keyEvent.character == '\r' )
		{ // Return key
			applyEditorValueAndDeactivate( );
		}
	}

	/**
	 * Processes a focus lost event that occurred in this cell editor.
	 * <p>
	 * The default implementation of this framework method applies the current
	 * value and deactivates the cell editor. Subclasses should call this method
	 * at appropriate times. Subclasses may also extend or reimplement.
	 * </p>
	 */
	protected void focusLost( )
	{
		if ( inProcessing == 1 )
			return;
		super.focusLost( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus( )
	{
		comboBox.setFocus( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.property.widgets.CDialogCellEditor#doValueChanged()
	 */
	protected void doValueChanged( )
	{
		if ( selection != comboBox.getSelectionIndex( ) )
		{
			markDirty( );
		}
		// must set the selection before getting value
		selection = comboBox.getSelectionIndex( );
		Object newValue = null;
		if ( selection == -1 )
		{
			newValue = comboBox.getText( );
		}
		else
		{
			newValue = comboBox.getItem( selection );
		}

		if ( newValue != null )
		{
			boolean newValidState = isCorrect( newValue );
			if ( newValidState )
			{
				doSetValue( newValue );
				markDirty( );
			}
			else
			{
			}
		}
	}

	/**
	 * The editor control.
	 */
	private Composite editor;

	/**
	 * The value of this cell editor; initially <code>null</code>.
	 */
	private Object value = null;

	/**
	 * Default DialogCellEditor style
	 */
	private static final int defaultStyle = SWT.NONE;

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	protected Control createControl( Composite parent )
	{

		Font font = parent.getFont( );
		Color bg = parent.getBackground( );

		editor = new Composite( parent, getStyle( ) );
		editor.setFont( font );
		editor.setBackground( bg );
		editor.setLayout( new FillLayout( ) );

		createContents( editor );
		updateContents( value );

		setValueValid( true );

		return editor;
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	protected Object doGetValue( )
	{
		return value;
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	protected void doSetValue( Object value )
	{
		this.value = value;
		updateContents( value );
	}

}