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

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * A field editor for a combo box that allows the drop-down selection of one of
 * a list of items, with a additional combo box for adjusting measurements.
 */

public class ComboBoxMeasureFieldEditor extends AbstractFieldEditor
{

	/**
	 * the parent composite contains this field editor.
	 */
	private Composite parent;

	/**
	 * the combo widget.
	 */
	private Combo fCombo;

	/**
	 * the text widget.
	 */
	private Text fText;

	/**
	 * the combo widget for measure.
	 */
	private Combo fmeasure;

	// indicator for having a Combo Box or a Text control.
	private boolean hasChoice;

	/**
	 * The names (labels) and underlying values to populate the combo widgets.
	 * These should be arranged as: { {name1, value1}, {name2, value2}, ...}
	 */
	private String[][] fBoxNamesAndValues;

	private String[][] fMeasureNamesAndValues;

	private ModifyListener comboModifyListener;

	private ModifyListener textModifyListener;

	/**
	 * Constructs new instance width value choice and measure choice field
	 * editor. Put the editor working in Combo Mode.
	 * 
	 * @param prop_name
	 *            preference name of the field editor
	 * @param labelText
	 *            label text of the preference
	 * @param entryNamesAndValues
	 *            names and values list for entry Combo
	 * @param measureNamesAndValues
	 *            names and values list for measure Combo
	 * @param parent
	 *            parent Composite of field editors
	 */
	public ComboBoxMeasureFieldEditor( String prop_name, String labelText,
			String[][] entryNamesAndValues, String[][] measureNamesAndValues,
			Composite parent )
	{
		hasChoice = true;
		init( prop_name, labelText );
		Assert.isTrue( checkArray( entryNamesAndValues ) );
		Assert.isTrue( checkArray( measureNamesAndValues ) );

		fBoxNamesAndValues = entryNamesAndValues;
		fMeasureNamesAndValues = measureNamesAndValues;
		this.parent = parent;

		createControl( parent );
	}

	/**
	 * Creates a editable combo field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param entryNamesAndValues
	 *            the entyr name and value choices of the combox of the field
	 *            editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public ComboBoxMeasureFieldEditor( String prop_name, String labelText,
			String[][] measureNamesAndValues, Composite parent )
	{
		hasChoice = false;
		init( prop_name, labelText );
		Assert.isTrue( checkArray( measureNamesAndValues ) );

		fMeasureNamesAndValues = measureNamesAndValues;
		this.parent = parent;

		createControl( parent );
	}

	/**
	 * Checks whether given <code>String[][]</code> is of "type"
	 * <code>String[][2]</code>.
	 * 
	 * @return <code>true</code> if it is OK, and <code>false</code> otherwise
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
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls( )
	{
		return 3;
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

		if ( hasChoice )
		{
			( (GridData) getComboBoxControl( parent ).getLayoutData( ) ).horizontalSpan = 1;
			( (GridData) getComboBoxControl( parent ).getLayoutData( ) ).widthHint = 70;
		}
		else
		{
			( (GridData) getTextControl( parent ).getLayoutData( ) ).horizontalSpan = 1;
			( (GridData) getTextControl( parent ).getLayoutData( ) ).widthHint = 85;
		}
		numColumns--;

		( (GridData) getMeasureControl( parent ).getLayoutData( ) ).horizontalSpan = numColumns;
		( (GridData) getMeasureControl( parent ).getLayoutData( ) ).widthHint = 65;

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

		control = getValueControl( parent );
		gd = new GridData( );
		gd.horizontalSpan = 1;
		control.setLayoutData( gd );

		control = getMeasureControl( parent );
		gd = new GridData( );
		gd.horizontalSpan = 1;
		control.setLayoutData( gd );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault( )
	{
		if ( fCombo != null )
			fCombo.removeModifyListener( comboModifyListener );
		else if ( fText != null )
			fText.removeModifyListener( textModifyListener );
		String value = getPreferenceStore( ).getDefaultString( getPreferenceName( ) );
		measureValueSetting( value );
		setDefaultValue( getStringValue( ) );

		if ( this.getPreferenceStore( ) instanceof StylePreferenceStore )
		{
			StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore( );
			if ( store.hasLocalValue( getPreferenceName( ) ) )
				markDirty( true );
			else
				markDirty( false );
		}
		else
			markDirty( true );

		if ( fCombo != null )
		{
			handleComboModifyEvent( );
			fCombo.addModifyListener( comboModifyListener );
		}
		else if ( fText != null )
		{
			handleTextModifyEvent( );
			fText.addModifyListener( textModifyListener );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see FieldEditor#doLoad()
	 */
	protected void doLoad( )
	{
		// oldValue for storing the value got from preference store.
		String value = getPreferenceStore( ).getString( getPreferenceName( ) );
		// split the total value to insert it into the combos.
		measureValueSetting( value );
		setOldValue( getStringValue( ) );
	}

	private void measureValueSetting( String value )
	{
		String[] sptValue = DEUtil.splitString( value );

		// System.out.println( "sptValue: " + sptValue[0] + " --" + sptValue[1]
		// );
		if ( hasChoice )
		{// has combo box.
			if ( sptValue[0] == null )
			{
				// value is only for combo.
				if ( !( updateComboForValue( sptValue[1] ) ) )
				{
					// for illegal value got.
					fCombo.setText( resolveNull( sptValue[1] ) );
				}
			}
			else
			{
				// value for custom input.
				fCombo.setText( resolveNull( sptValue[0] ) );
				updateMeasureForValue( sptValue[1] );
			}
		}
		else
		{// has text box.
			if ( sptValue[0] == null )
			{
				fText.setText( resolveNull( sptValue[1] ) );
			}
			else
			{
				fText.setText( resolveNull( sptValue[0] ) );
				updateMeasureForValue( sptValue[1] );
			}
		}
	}

	/**
	 * Sets the name in the combo widget to match the specified value.
	 */
	private boolean updateComboForValue( String value )
	{
		if ( value == null )
		{
			return false;
		}
		// legal value for the combo, gets name for value from arrays, set it
		// and return true.
		for ( int i = 0; i < fBoxNamesAndValues.length; i++ )
		{
			if ( value.equals( fBoxNamesAndValues[i][1] ) )
			{
				fCombo.setText( fBoxNamesAndValues[i][0] );
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the name in the measure combo widget to match the specified value.
	 */
	private void updateMeasureForValue( String value )
	{
		// value == null: gets no measure(unit) from store , set it default to
		// "inch".
		if ( value == null )
		{
			fmeasure.setText( "" ); //$NON-NLS-1$
			return;
		}

		// legal measure(unit) value, gets name for value from arrays.
		for ( int i = 0; i < fMeasureNamesAndValues.length; i++ )
		{
			if ( value.equals( fMeasureNamesAndValues[i][1] ) )
			{
				fmeasure.setText( fMeasureNamesAndValues[i][0] );
				return;
			}
		}
		// for illegal value of the measure(unit), set it default also.
		if ( fMeasureNamesAndValues.length > 0 )
		{
			fmeasure.setText( "" ); //$NON-NLS-1$
		}
	}

	/**
	 * Resolves null value.
	 * 
	 * @param src
	 * @return
	 */
	private String resolveNull( String src )
	{
		return src == null ? "" : src; //$NON-NLS-1$
	}

	/**
	 * Gets value for the preference property of this field editor.
	 * 
	 * @return the value
	 */
	protected String getStringValue( )
	{
		if ( hasChoice )
		{
			if ( inComboNamesList( getComboBoxControl( parent ).getText( ) ) )
			{
				return getBoxValueForName( getComboBoxControl( parent ).getText( ) );
			}
			return getComboBoxControl( parent ).getText( )
					+ getMeasureValueForName( getMeasureControl( parent ).getText( ) );

		}
		return getTextControl( parent ).getText( )
				+ getMeasureValueForName( getMeasureControl( parent ).getText( ) );
	}

	/**
	 * Checks whether it is a item of the Combo box. If in Text mode, always
	 * returns false;
	 * 
	 * @param name
	 * @return
	 */
	public boolean inComboNamesList( String name )
	{
		if ( name == null )
		{
			return false;
		}
		for ( int i = 0; i < fBoxNamesAndValues.length; i++ )
		{
			if ( name.equals( fBoxNamesAndValues[i][0] ) )
				return true;
		}
		return false;
	}

	/**
	 * Returns the control which holds the value, this could be a Combo or a
	 * Text.
	 * 
	 * @param parent
	 * @return
	 */
	public Control getValueControl( Composite parent )
	{
		if ( hasChoice )
		{
			return getComboBoxControl( parent );
		}
		return getTextControl( parent );
	}

	/**
	 * Lazing creates and returns the text control for the editor. If in choice
	 * mode, always returns null.
	 * 
	 * @param parent
	 * @return
	 */
	public Text getTextControl( Composite parent )
	{
		if ( hasChoice )
		{
			return null;
		}
		if ( fText == null )
		{
			fText = new Text( parent, SWT.BORDER );
			fText.setFont( parent.getFont( ) );

			textModifyListener = new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					handleTextModifyEvent( );
					valueChanged( VALUE );
				}
			};

			fText.addModifyListener( textModifyListener );
		}
		return fText;
	}

	/**
	 * Lazily creates and returns the Combo control for the editor. If in text
	 * mode, always returns null.
	 */
	public Combo getComboBoxControl( Composite parent )
	{
		if ( !hasChoice )
		{
			return null;
		}
		if ( fCombo == null )
		{
			fCombo = new Combo( parent, SWT.DROP_DOWN );
			for ( int i = 0; i < fBoxNamesAndValues.length; i++ )
			{
				fCombo.add( fBoxNamesAndValues[i][0], i );
			}
			fCombo.setFont( parent.getFont( ) );
			fCombo.setVisibleItemCount( 30 );
			fCombo.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent evt )
				{
					if ( fmeasure != null )
					{
						fmeasure.setEnabled( false );
					}
					valueChanged( VALUE );
				}
			} );

			comboModifyListener = new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					handleComboModifyEvent( );
					valueChanged( VALUE );
				}
			};
			fCombo.addModifyListener( comboModifyListener );
		}
		return fCombo;
	}

	/**
	 * Lazily creates and returns the measure Combo control.
	 * 
	 * @param parent
	 *            The parent Composite contains the control.
	 * @return Combo
	 */
	public Combo getMeasureControl( Composite parent )
	{
		if ( fmeasure == null )
		{
			fmeasure = new Combo( parent, SWT.READ_ONLY );
			for ( int i = 0; i < fMeasureNamesAndValues.length; i++ )
			{
				fmeasure.add( fMeasureNamesAndValues[i][0], i );
			}
			if ( getTextControl( parent ) != null
					&& getTextControl( parent ).getText( ) != null )
			{
				// fmeasure.select( 0 );
			}
			fmeasure.setFont( parent.getFont( ) );
			fmeasure.setVisibleItemCount( 30 );
			fmeasure.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					valueChanged( VALUE );
				}
			} );
		}
		return fmeasure;
	}

	/**
	 * Given the name (label) of an entry, return the corresponding value, from
	 * arrays.
	 */
	protected String getBoxValueForName( String name )
	{
		for ( int i = 0; i < fBoxNamesAndValues.length; i++ )
		{
			String[] entry = fBoxNamesAndValues[i];
			if ( name.equals( entry[0] ) )
			{
				return entry[1];
			}
		}
		// for illegal names, return null.
		return name;
	}

	/**
	 * Given the name (label) of an measure combo , return the corresponding
	 * value, from arrays.
	 */
	protected String getMeasureValueForName( String name )
	{
		for ( int i = 0; i < fMeasureNamesAndValues.length; i++ )
		{
			String[] entry = fMeasureNamesAndValues[i];
			if ( name.equals( entry[0] ) )
			{
				return entry[1];
			}
		}
		// for illegal names, return null.
		return name;
	}

	private void handleTextModifyEvent( )
	{
		String text = fText.getText( );
		if ( !DEUtil.isValidNumber( text ) )
		{
			fmeasure.deselectAll( );
			fmeasure.setEnabled( false );
		}
		else if ( !fmeasure.isEnabled( ) )
		{
			String unit = getDefaultUnit( );
			if ( !StringUtil.isBlank( unit ) )
			{
				unit = DEUtil.getMetaDataDictionary( )
						.getChoiceSet( DesignChoiceConstants.CHOICE_UNITS )
						.findChoice( unit )
						.getDisplayName( );
			}
			fmeasure.setText( unit == null ? "" : unit ); //$NON-NLS-1$
			fmeasure.setEnabled( true );
		}
	}

	private void handleComboModifyEvent( )
	{
		boolean cusType = !inComboNamesList( fCombo.getText( ) );

		if ( cusType )
		{
			if ( !DEUtil.isValidNumber( fCombo.getText( ) ) )
			{
				fmeasure.deselectAll( );
				fmeasure.setEnabled( false );
			}
			else if ( !( fmeasure.isEnabled( ) ) )
			{
				String unit = getDefaultUnit( );
				if ( !StringUtil.isBlank( unit ) )
				{
					unit = DEUtil.getMetaDataDictionary( )
							.getChoiceSet( DesignChoiceConstants.CHOICE_UNITS )
							.findChoice( unit )
							.getDisplayName( );
				}
				fmeasure.setText( unit == null ? "" : unit ); //$NON-NLS-1$
				fmeasure.setEnabled( true );
			}
		}
		else
		{
			fmeasure.deselectAll( );
			fmeasure.setEnabled( false );
		}
	}
}