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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * An editable version of ComboFieldEditor. Allows selection from the drop-down
 * list or direct input.
 */

public class EditableComboFieldEditor extends FieldEditor
{

	/**
	 * The <code>Combo</code> widget.
	 */
	protected Combo fCombo;

	/**
	 * The value (not the name) of the currently selected item in the Combo
	 * widget.
	 */
	protected String fValue;

	private String oldValue;

	/**
	 * The names (labels) and underlying values to populate the combo widget.
	 * These should be arranged as: { {name1, value1}, {name2, value2}, ...}
	 */
	protected String[][] fEntryNamesAndValues;

	/**
	 * The constructor.
	 * 
	 * @param name
	 * @param labelText
	 * @param entryNamesAndValues
	 * @param parent
	 */
	public EditableComboFieldEditor( String name, String labelText,
			String[][] entryNamesAndValues, Composite parent )
	{
		init( name, labelText );
		Assert.isTrue( checkArray( entryNamesAndValues ) );
		fEntryNamesAndValues = entryNamesAndValues;
		createControl( parent );
	}

	/**
	 * Checks whether given <code>String[][]</code> is of "type"
	 * <code>String[][2]</code>.
	 * 
	 * @return <code>true</code> if it is ok, and <code>false</code>
	 *         otherwise
	 */
	private boolean checkArray( String[][] table )
	{
		if ( table == null )
		{
			return false;
		}
		for ( int i = 0; i < table.length; i++ )
		{
			String[] array = table[i];
			if ( array == null || array.length != 2 )
			{
				return false;
			}
		}
		return true;
	}

	/*
	 * @see FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls( )
	{
		return 2;
	}

	/*
	 * @see FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns( int numColumns )
	{
		Control control = getLabelControl( );
		if ( control != null )
		{
			( (GridData) control.getLayoutData( ) ).horizontalSpan = 1;
			numColumns--;
		}
		( (GridData) fCombo.getLayoutData( ) ).horizontalSpan = numColumns;
	}

	/*
	 * @see FieldEditor#doFillIntoGrid(Composite, int)
	 */
	protected void doFillIntoGrid( Composite parent, int numColumns )
	{
		Control control = getLabelControl( parent );
		GridData gd = new GridData( );
		gd.horizontalSpan = 1;
		control.setLayoutData( gd );
		control = getComboBoxControl( parent );
		gd = new GridData( );
		gd.horizontalSpan = 1;
		control.setLayoutData( gd );
	}

	/*
	 * @see FieldEditor#doLoad()
	 */
	protected void doLoad( )
	{
		updateComboForValue( getPreferenceStore( ).getString( getPreferenceName( ) ) );
	}

	/*
	 * @see FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault( )
	{
		updateComboForValue( getPreferenceStore( ).getDefaultString( getPreferenceName( ) ) );
	}

	/**
	 * Set the name in the combo widget to match the specified value.
	 */
	protected void updateComboForValue( String value )
	{
		fValue = value;
		oldValue = value;
		for ( int i = 0; i < fEntryNamesAndValues.length; i++ )
		{
			if ( fEntryNamesAndValues[i][1].equals( value ) )
			{
				fCombo.setText( fEntryNamesAndValues[i][0] );
				return;
			}
		}

		if ( value == null )
		{
			fCombo.setText( "" ); //$NON-NLS-1$
		}
		else
		{
			fCombo.setText( value );
		}
	}

	/*
	 * @see FieldEditor#doStore()
	 */
	protected void doStore( )
	{
		if ( fValue == null )
		{
			getPreferenceStore( ).setToDefault( getPreferenceName( ) );
			return;
		}

		if ( ( oldValue != null ) && oldValue.equals( fValue ) )
		{
			return;
		}
		getPreferenceStore( ).setValue( getPreferenceName( ), fValue );
	}

	/**
	 * Lazily create and return the Combo control.
	 * 
	 * @param parent
	 * @return
	 */
	public Combo getComboBoxControl( Composite parent )
	{
		if ( fCombo == null )
		{
			fCombo = new Combo( parent, SWT.NONE );
			for ( int i = 0; i < fEntryNamesAndValues.length; i++ )
			{
				fCombo.add( fEntryNamesAndValues[i][0], i );
			}
			fCombo.setFont( parent.getFont( ) );
			fCombo.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent evt )
				{
					String oldValue = fValue;
					String name = fCombo.getText( );
					fValue = getValueForName( name );
					setPresentsDefaultValue( false );
					fireValueChanged( VALUE, oldValue, fValue );
				}
			} );

			fCombo.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					String oldValue = fValue;
					String name = fCombo.getText( );
					fValue = name;
					setPresentsDefaultValue( false );
					fireValueChanged( VALUE, oldValue, fValue );
				}
			} );
		}
		return fCombo;
	}

	/**
	 * Given the name (label) of an entry, return the corresponding value.
	 */
	protected String getValueForName( String name )
	{
		for ( int i = 0; i < fEntryNamesAndValues.length; i++ )
		{
			String[] entry = fEntryNamesAndValues[i];
			if ( name.equals( entry[0] ) )
			{
				return entry[1];
			}
		}
		return null;
	}
}