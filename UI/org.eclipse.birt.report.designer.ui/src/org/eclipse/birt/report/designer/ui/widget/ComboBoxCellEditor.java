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

import org.eclipse.birt.report.designer.internal.ui.swt.custom.CCombo;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A cell editor that presents a list of items in a combo box. Cutomized
 * combobox celleditor of eclipse to support text value input.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class ComboBoxCellEditor extends CellEditor
{

	/**
	 * The list of items to present in the combo box.
	 */
	private String[] items;

	/**
	 * The zero-based index of the selected item.
	 */
	int selection;

	/**
	 * The custom combo box control.
	 */
	CCombo comboBox;

	/**
	 * Default BirtComboBoxCellEditor style
	 */
	private static final int defaultStyle = SWT.NONE;

	private Object value;

	private int inProcessing=0;

	/**
	 * Creates a new cell editor with no control and no st of choices.
	 * Initially, the cell editor has no cell validator.
	 * 
	 * @since 2.1
	 * @see #setStyle
	 * @see #create
	 * @see #setItems
	 * @see #dispose
	 */
	public ComboBoxCellEditor( )
	{
		setStyle( defaultStyle );
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of
	 * choices and parented under the given control. The cell editor value is
	 * the zero-based index of the selected item. Initially, the cell editor has
	 * no cell validator and the first item in the list is selected.
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the list of strings for the combo box
	 */
	public ComboBoxCellEditor( Composite parent, String[] items )
	{
		this( parent, items, defaultStyle );
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of
	 * choices and parented under the given control. The cell editor value is
	 * the zero-based index of the selected item. Initially, the cell editor has
	 * no cell validator and the first item in the list is selected.
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the list of strings for the combo box
	 * @param style
	 *            the style bits
	 * @since 2.1
	 */
	public ComboBoxCellEditor( Composite parent, String[] items, int style )
	{
		super( parent, style );
		setItems( items );
	}

	/**
	 * The <code>BirtComboBoxCellEditor</code> implementation of this
	 * <code>ComboBoxCellEditor</code> framework method returns the String
	 * value of the current selection.
	 * 
	 * @return the value of the current selection wrapped as an
	 *         <code>String</code>
	 */
	protected Object doGetValue( )
	{
		CCombo comboBox = (CCombo) super.getControl( );

		if ( selection != -1 )
			return comboBox.getItem( selection );

		return comboBox.getText( );
	}

	/**
	 * The <code>BirtComboBoxCellEditor</code> implementation of this
	 * <code>ComboBoxCellEditor</code> framework method accepts a String
	 * value.
	 * 
	 * @param value
	 *            the value to be set as an <code>String</code>
	 */
	protected void doSetValue( Object value )
	{
		CCombo comboBox = (CCombo) super.getControl( );

		Assert.isTrue( comboBox != null );
		if ( value instanceof Integer )
		{
			selection = ( (Integer) value ).intValue( );
			comboBox.select( selection );
		}
		else
		{
			comboBox.setText( (String) value );
			this.value = value;
//			comboBox.select( Arrays.asList( comboBox.getItems( ) ).indexOf(
//					value ) );
			selection = Arrays.asList( comboBox.getItems( ) ).indexOf( value );
		}
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

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	protected Control createControl( Composite parent )
	{
		comboBox = new CCombo( parent, getStyle( ) );
		comboBox.setFont( parent.getFont( ) );

		comboBox.addKeyListener( new KeyAdapter( ) {

			// hook key pressed - see PR 14201
			public void keyPressed( KeyEvent e )
			{
				keyReleaseOccured( e );
			}
		} );

		comboBox.addSelectionListener( new SelectionAdapter( ) {

			public void widgetDefaultSelected( SelectionEvent event )
			{
				markDirty();
				applyEditorValueAndDeactivate( );
			}

			public void widgetSelected( SelectionEvent event )
			{
				applyEditorValueAndDeactivate( );
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
		return comboBox;
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	protected void doSetFocus( )
	{
		comboBox.setFocus( );
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method sets the minimum width of the
	 * cell. The minimum width is 10 characters if <code>comboBox</code> is
	 * not <code>null</code> or <code>disposed</code> eles it is 60 pixels
	 * to make sure the arrow button and some text is visible. The list of
	 * CCombo will be wide enough to show its longest item.
	 */
	public LayoutData getLayoutData( )
	{
		LayoutData layoutData = super.getLayoutData( );
		if ( ( comboBox == null ) || comboBox.isDisposed( ) )
			layoutData.minimumWidth = 60;
		else
		{
			// make the comboBox 10 characters wide
			GC gc = new GC( comboBox );
			layoutData.minimumWidth = ( gc.getFontMetrics( )
					.getAverageCharWidth( ) * 10 ) + 10;
			gc.dispose( );
		}
		return layoutData;
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

	/**
	 * Applies the currently selected value and deactiavates the cell editor
	 */
	void applyEditorValueAndDeactivate( )
	{

		inProcessing=1;
		if ( selection != comboBox.getSelectionIndex( ) )
		{
			markDirty( );
		}
		//	must set the selection before getting value
		selection = comboBox.getSelectionIndex( );
		Object newValue = null;
		if ( selection == -1 )
		{
			newValue = comboBox.getText( );
		}
		else
		{
			newValue=comboBox.getItem( selection );
		}
		
		doSetValue( newValue );

		boolean isValid = isCorrect( newValue );
		setValueValid( isValid );
		fireApplyEditorValue( );
		deactivate( );
		inProcessing=0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	protected void focusLost( )
	{
		if(inProcessing == 1) return;
		if(!comboBox.getText().equals(value))
		{
			markDirty();
		}
		if ( isActivated( ) )
		{
			applyEditorValueAndDeactivate( );
		}
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
		{ // tab key
			markDirty();
			applyEditorValueAndDeactivate( );
		}

	}
}