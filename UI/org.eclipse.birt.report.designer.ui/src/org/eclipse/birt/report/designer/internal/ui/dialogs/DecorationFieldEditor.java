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

public class DecorationFieldEditor extends AbstractFieldEditor
{

	/**
	 * The parent Composite contains this field editor.
	 */
	private Composite parent;

	/**
	 * The field editor's label text.
	 */
	private String labelText;

	/**
	 * The <code>Button</code> widgets.
	 */
	private Button bUnderLine;

	private boolean wasSelected1;
	private boolean isSelected1;
	private boolean isDirty1;

	private Button bOverLine;

	private boolean wasSelected2;
	private boolean isSelected2;
	private boolean isDirty2;

	private Button bLineThrough;

	private boolean wasSelected3;
	private boolean isSelected3;
	private boolean isDirty3;

	/**
	 * The names of the preferences displayed in this field editor.
	 */
	private String underline_prop;
	private String underline_text;

	private String overline_prop;
	private String overline_text;

	private String line_through_prop;
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
		control.setLayoutData( gd );

		bUnderLine = getUnderLinePropControl( parent );
		gd = new GridData( );
		gd.widthHint = 88;
		bUnderLine.setLayoutData( gd );

		bOverLine = getOverLinePropControl( parent );
		gd = new GridData( );
		gd.widthHint = 88;
		bOverLine.setLayoutData( gd );

		bLineThrough = getLineThroughPropControl( parent );
		gd = new GridData( );
		gd.widthHint = 88;
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
					boolean isSelected = bUnderLine.getSelection( );
					setPresentsDefaultValue( false );
					if ( isSelected1 != isSelected )
					{
						fireValueChanged( VALUE, null, null );
						fireStateChanged( VALUE, isSelected1, isSelected );
						isSelected1 = isSelected;
						isDirty1 = true;
					}
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
					boolean isSelected = bOverLine.getSelection( );
					setPresentsDefaultValue( false );
					if ( isSelected2 != isSelected )
					{
						fireValueChanged( VALUE, null, null );
						fireStateChanged( VALUE, isSelected2, isSelected );
						isSelected2 = isSelected;
						isDirty2 = true;
					}
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
					boolean isSelected = bLineThrough.getSelection( );
					setPresentsDefaultValue( false );
					if ( isSelected3 != isSelected )
					{
						fireValueChanged( VALUE, null, null );
						fireStateChanged( VALUE, isSelected3, isSelected );
						isSelected3 = isSelected;
						isDirty3 = true;
					}
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
			wasSelected1 = DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals( value );
			bUnderLine.setSelection( wasSelected1 );
		}
		if ( bOverLine != null )
		{
			String value = getPreferenceStore( ).getString( getOverLinePropName( ) );
			wasSelected2 = DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals( value );
			bOverLine.setSelection( wasSelected2 );
		}
		if ( bLineThrough != null )
		{
			String value = getPreferenceStore( ).getString( getLineThroughPropName( ) );
			wasSelected3 = DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals( value );
			bLineThrough.setSelection( wasSelected3 );
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
			wasSelected1 = DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals( value );
			bUnderLine.setSelection( wasSelected1 );
		}
		if ( bOverLine != null )
		{
			String value = getPreferenceStore( ).getDefaultString( getOverLinePropName( ) );
			wasSelected2 = DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals( value );
			bOverLine.setSelection( wasSelected2 );
		}
		if ( bLineThrough != null )
		{
			String value = getPreferenceStore( ).getDefaultString( getLineThroughPropName( ) );
			wasSelected3 = DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals( value );
			bLineThrough.setSelection( wasSelected3 );
		}
	}

	/*
	 * @see FieldEditor#doStore()
	 */
	protected void doStore( )
	{
		//		checkDirty( );
		if ( isDirty1 )
		{
			getPreferenceStore( ).setValue( underline_prop,
					getUnderLinePropValue( ) );
		}
		if ( isDirty2 )
		{
			getPreferenceStore( ).setValue( overline_prop,
					getOverLinePropValue( ) );
		}
		if ( isDirty3 )
		{
			getPreferenceStore( ).setValue( line_through_prop,
					getLineThroughPropValue( ) );
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractFieldEditor#getValue()
	 */
	protected String getStringValue( )
	{
		return null;
	}
}