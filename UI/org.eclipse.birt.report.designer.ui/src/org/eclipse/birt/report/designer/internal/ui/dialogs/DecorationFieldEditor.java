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

import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A field editor for decoration that contains three check box button.
 *  
 */

public class DecorationFieldEditor extends FieldEditor
{

	/**
	 * The parent Composite contains this field editor.
	 */
	private Composite parent;

	/**
	 * The <code>Button</code> widgets.
	 */
	private Button bUnderLine;

	private Button bOverLine;

	private Button bLineThrough;

	/**
	 * The field editor's label text.
	 */
	private String labelText;

	/**
	 * The names of the preferences displayed in this field editor.
	 */
	private String underline_prop;

	private String overline_prop;

	private String line_through_prop;

	private String underline_text;

	private String overline_text;

	private String line_through_text;

	/**
	 * Constructs a new instance of decoration field editor.
	 * 
	 * @param prop_name1
	 *            preference name of underline_prop
	 * @param prop_name2
	 *            preference name of overline_prop
	 * @param prop_name3
	 *            preference name of line_through_prop
	 * @param label
	 *            label text of the preference
	 * @param parent
	 *            parent Composite
	 */
	public DecorationFieldEditor( String prop_name1, String prop_label1,
			String prop_name2, String prop_label2, String prop_name3,
			String prop_label3, String label, Composite parent )
	{
		super( );
		underline_prop = prop_name1;
		underline_text = prop_label1;
		overline_prop = prop_name2;
		overline_text = prop_label2;
		line_through_prop = prop_name3;
		line_through_text = prop_label3;
		labelText = label;
		this.parent = parent;

		createControl( parent );
	}

	/*
	 * @see FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls( )
	{
		return 4;
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
		( (GridData) getUnderLinePropControl( parent ).getLayoutData( ) ).horizontalSpan = 1;
		numColumns--;

		( (GridData) getOverLinePropControl( parent ).getLayoutData( ) ).horizontalSpan = 1;
		numColumns--;

		( (GridData) getLineThroughPropControl( parent ).getLayoutData( ) ).horizontalSpan = numColumns;
	}

	/*
	 * @see FieldEditor#doFillIntoGrid(Composite, int)
	 */
	protected void doFillIntoGrid( Composite parent, int numColumns )
	{
		Control control = getLabelControl( parent );
		GridData gd = new GridData( GridData.HORIZONTAL_ALIGN_CENTER );
		gd.horizontalSpan = 1;
		control.setLayoutData( gd );

		bUnderLine = getUnderLinePropControl( parent );
		gd = new GridData( );
		gd.horizontalSpan = 1;
		bUnderLine.setLayoutData( gd );

		bOverLine = getOverLinePropControl( parent );
		gd = new GridData( );
		gd.horizontalSpan = 1;
		bOverLine.setLayoutData( gd );

		bLineThrough = getLineThroughPropControl( parent );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 1;
		bLineThrough.setLayoutData( gd );
	}

	/**
	 * Lazily creates and returns the Button control.
	 * 
	 * @param parent
	 *            The parent Composite contains the button.
	 * @return Button
	 */
	public Button getUnderLinePropControl( Composite parent )
	{
		if ( bUnderLine == null )
		{
			bUnderLine = new Button( parent, SWT.CHECK );
			bUnderLine.setText( underline_text );
			bUnderLine.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent evt )
				{
					setPresentsDefaultValue( false );
					fireValueChanged( VALUE, null, null );
				}
			} );

		}
		return bUnderLine;
	}

	/**
	 * Lazily creates and returns the Button button.
	 * 
	 * @param parent
	 *            The parent Composite contains the control.
	 * @return Button
	 */
	public Button getOverLinePropControl( Composite parent )
	{
		if ( bOverLine == null )
		{
			bOverLine = new Button( parent, SWT.CHECK );
			bOverLine.setText( overline_text );
			bOverLine.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent evt )
				{
					setPresentsDefaultValue( false );
					fireValueChanged( VALUE, null, null );
				}
			} );

		}
		return bOverLine;
	}

	/**
	 * Lazily creates and returns the Button control.
	 * 
	 * @param parent
	 *            The parent Composite contains the button.
	 * @return Button
	 */
	public Button getLineThroughPropControl( Composite parent )
	{
		if ( bLineThrough == null )
		{
			bLineThrough = new Button( parent, SWT.CHECK );
			bLineThrough.setText( line_through_text );
			bLineThrough.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent evt )
				{
					setPresentsDefaultValue( false );
					fireValueChanged( VALUE, null, null );
				}
			} );

		}
		return bLineThrough;
	}

	/*
	 * @see FieldEditor#doLoad()
	 */
	protected void doLoad( )
	{
		if ( bUnderLine != null )
		{
			String value = getPreferenceStore( ).getString( getUnderlinePropName( ) );
			bUnderLine.setSelection( DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals( value ) );
		}
		if ( bOverLine != null )
		{
			String value = getPreferenceStore( ).getString( getOverLinePropName( ) );
			bOverLine.setSelection( DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals( value ) );
		}
		if ( bLineThrough != null )
		{
			String value = getPreferenceStore( ).getString( getLineThroughPropName( ) );
			bLineThrough.setSelection( DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals( value ) );
		}

	}

	/*
	 * @see FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault( )
	{
		if ( bUnderLine != null )
		{
			String value = getPreferenceStore( ).getDefaultString( getUnderlinePropName( ) );
			bUnderLine.setSelection( DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals( value ) );
		}
		if ( bOverLine != null )
		{
			String value = getPreferenceStore( ).getDefaultString( getOverLinePropName( ) );
			bOverLine.setSelection( DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals( value ) );
		}
		if ( bLineThrough != null )
		{
			String value = getPreferenceStore( ).getDefaultString( getLineThroughPropName( ) );
			bLineThrough.setSelection( DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals( value ) );
		}
	}

	/*
	 * @see FieldEditor#doStore()
	 */
	protected void doStore( )
	{

		getPreferenceStore( ).setValue( underline_prop, getUnderLinePropValue( ) );
		getPreferenceStore( ).setValue( overline_prop, getOverLinePropValue( ) );
		getPreferenceStore( ).setValue( line_through_prop,
				getLineThroughPropValue( ) );
	}

	/**
	 * Returns this field editor's label text.
	 * 
	 * @return the label text
	 */
	public String getLabelText( )
	{
		return labelText;
	}

	/**
	 * Returns the name of the preference this field editor operates on.
	 * 
	 * @return the name of the preference
	 */
	public String getUnderlinePropName( )
	{
		return underline_prop;
	}

	/**
	 * Returns the name of the preference this field editor operates on.
	 * 
	 * @return the name of the preference
	 */
	public String getOverLinePropName( )
	{
		return overline_prop;
	}

	/**
	 * Returns the name of the preference this field editor operates on.
	 * 
	 * @return the name of the preference
	 */
	public String getLineThroughPropName( )
	{
		return line_through_prop;
	}

	/**
	 * Gets values for the given property.
	 * 
	 * @return
	 */
	private String getUnderLinePropValue( )
	{
		if ( bUnderLine.getSelection( ) )
		{
			return DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE;
		}
		else
		{
			return DesignChoiceConstants.TEXT_UNDERLINE_NONE;
		}
	}

	/**
	 * Gets values for the given property.
	 * 
	 * @return
	 */
	private String getOverLinePropValue( )
	{
		if ( bOverLine.getSelection( ) )
		{
			return DesignChoiceConstants.TEXT_OVERLINE_OVERLINE;
		}
		else
		{
			return DesignChoiceConstants.TEXT_OVERLINE_NONE;
		}
	}

	/**
	 * Gets values for the given property.
	 * 
	 * @return
	 */
	private String getLineThroughPropValue( )
	{
		if ( bLineThrough.getSelection( ) )
		{
			return DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH;
		}
		else
		{
			return DesignChoiceConstants.TEXT_LINE_THROUGH_NONE;
		}
	}
}