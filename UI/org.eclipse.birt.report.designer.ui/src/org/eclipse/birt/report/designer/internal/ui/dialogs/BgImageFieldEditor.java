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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

/**
 * A field editor containing a Text field, with a additional button for getting
 * file name.
 */

public class BgImageFieldEditor extends FieldEditor
{

	/**
	 * the text widget.
	 */
	private Text fText;

	/**
	 * the button widget.
	 */
	private Button fButton;

	/**
	 * value of the field editor.
	 */
	protected String fValue;

	private String oldValue;

	/**
	 * @param name
	 *            property name of the field editor.
	 * @param labelText
	 *            the display label for the field editor.
	 * @param parent
	 *            parent composite
	 */
	public BgImageFieldEditor( String name, String labelText, Composite parent )
	{
		init( name, labelText );
		createControl( parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.EditableComboFieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls( )
	{
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad( )
	{
		oldValue = getPreferenceStore( ).getString( getPreferenceName( ) );
		if ( oldValue != null )
		{
			fValue = oldValue;
			fText.setText( fValue );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault( )
	{
		oldValue = getPreferenceStore( ).getDefaultString( getPreferenceName( ) );
		if ( oldValue != null )
		{
			fValue = oldValue;
			fText.setText( fValue );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
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

	/*
	 * @see EditableComboFieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns( int numColumns )
	{
		Control control = getLabelControl( );
		if ( control != null )
		{
			( (GridData) control.getLayoutData( ) ).horizontalSpan = 1;
			numColumns--;
		}
		( (GridData) getTextControl( null ).getLayoutData( ) ).horizontalSpan = 1;
		( (GridData) getTextControl( null ).getLayoutData( ) ).widthHint = 85;
		numColumns--;

		( (GridData) getButtonControl( null ).getLayoutData( ) ).horizontalSpan = numColumns;
		( (GridData) getButtonControl( null ).getLayoutData( ) ).widthHint = 65;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.EditableComboFieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
	protected void doFillIntoGrid( Composite parent, int numColumns )
	{
		Control control = getLabelControl( parent );
		control.setLayoutData( new GridData( ) );

		control = getTextControl( parent );
		control.setLayoutData( new GridData( ) );

		Button button = getButtonControl( parent );
		button.setText( Messages.getString( "BgImageFieldEditor.displayname.Browse" ) ); //$NON-NLS-1$
		button.setLayoutData( new GridData( ) );
	}

	/**
	 * @param object
	 * @return
	 */
	private Text getTextControl( Composite parent )
	{
		if ( fText == null )
		{
			fText = new Text( parent, SWT.BORDER );
			fText.setFont( parent.getFont( ) );
			fText.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					String oldValue = fValue;
					fValue = fText.getText( );
					setPresentsDefaultValue( false );
					fireValueChanged( VALUE, oldValue, fValue );
				}
			} );
		}
		return fText;
	}

	/**
	 * Lazily creates and returns the button control.
	 * 
	 * @param parent
	 *            The parent Composite contains the control.
	 * @return button
	 */
	protected Button getButtonControl( final Composite parent )
	{
		if ( fButton == null )
		{
			fButton = new Button( parent, SWT.PUSH );

			fButton.setFont( parent.getFont( ) );
			fButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent evt )
				{
					FileDialog fd = new FileDialog( parent.getShell( ),
							SWT.OPEN );
					fd.setFilterExtensions( new String[]{
						"*.gif; *.jpg; *.png; *.ico; *.bmp" //$NON-NLS-1$
						} );
					fd.setFilterNames( new String[]{
						"SWT image" + " (gif, jpeg, png, ico, bmp)" //$NON-NLS-1$ //$NON-NLS-2$
					} );

					String file = fd.open( );
					if ( file != null )
					{
						String oldValue = getTextControl( null ).getText( );
						getTextControl( null ).setText( file );
						fValue = file;
						fireValueChanged( VALUE, oldValue, fValue );
					}
				}
			} );
		}
		return fButton;
	}

}