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

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.osgi.framework.msg.MessageFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

;

/**
 * The Color Cell Editor of IARD. The editor inlucde a combo box and a builder
 * button. All system predefined and customer defined color are listed in the
 * combobox. User can select the color in that list, input the RGB value into
 * the comobox or click the builder button to open the color dialog to select
 * the right color.
 */
public class ComboBoxColorCellEditor extends DialogCellEditor
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
	 * Default ComboBoxCellEditor style
	 */
	private static final int defaultStyle = SWT.NONE;

	/**
	 * The composite to keep the combobox and button together
	 */
	private Composite composite;

	/**
	 * Creates a new dialog cell editor parented under the given control. The
	 * combobox lists is <code>null</code> initially
	 * 
	 * @param parent
	 *            the parent control
	 */
	public ComboBoxColorCellEditor( Composite parent )
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
	public ComboBoxColorCellEditor( Composite parent, String[] items )
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
	public ComboBoxColorCellEditor( Composite parent, String[] items, int style )
	{
		super( parent, style );
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
	protected Control createContents( Composite cell )
	{

		Color bg = cell.getBackground( );
		composite = new Composite( cell, getStyle( ) );
		composite.setBackground( bg );
		composite.setLayout( new FillLayout( ) );

		comboBox = new CCombo( composite, SWT.READ_ONLY );
		comboBox.setBackground( bg );
		comboBox.setFont( cell.getFont( ) );

		comboBox.addSelectionListener( new SelectionAdapter( ) {

			public void widgetDefaultSelected( SelectionEvent event )
			{
			}

			public void widgetSelected( SelectionEvent event )
			{
				selection = comboBox.getSelectionIndex( );
				comboBox.select( selection );
				doSetValue( comboBox.getItem( selection ) );
				applyEditorValueAndDeactivate( );
			}
		} );

		return composite;
	}

	/**
	 * Applies the currently selected value and deactiavates the cell editor
	 */
	void applyEditorValueAndDeactivate( )
	{
		//	must set the selection before getting value
		selection = comboBox.getSelectionIndex( );
		Object newValue = doGetValue( );

		markDirty( );
		boolean isValid = isCorrect( newValue );
		setValueValid( isValid );
		if ( !isValid )
		{
			// try to insert the current value into the error message.
			setErrorMessage( MessageFormat.format( getErrorMessage( ),
					new Object[]{
						items[selection]
					} ) );
		}
		fireApplyEditorValue( );
		deactivate( );
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected Object openDialogBox( Control cellEditorWindow )
	{
		ColorDialog dialog = new ColorDialog( cellEditorWindow.getShell( ) );
		Object value = getValue( );

		value = dialog.open( );
		if ( dialog.getRGB( ) != null )
		{
			return dialog.getRGB( );
		}

		return value;
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
			if ( value instanceof RGB )
			{
				text = "0x" //$NON-NLS-1$
						+ Integer.toHexString( DEUtil.getRGBInt( (RGB) value ) );
			}
			else
			{
				text = value.toString( );
			}
		}
		comboBox.setText( text );
	}
}