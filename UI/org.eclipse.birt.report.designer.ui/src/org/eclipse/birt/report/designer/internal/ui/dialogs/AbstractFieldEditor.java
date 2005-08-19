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
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public abstract class AbstractFieldEditor extends FieldEditor
{

	/**
	 * Indicates that no value change should fired when the field editor is not
	 * loaded.
	 */
	protected boolean isLoaded = false;

	private boolean isDirty = false;

	private String oldValue = ""; //$NON-NLS-1$

	private String propValue = ""; //$NON-NLS-1$

	private String defaultUnit = ""; //$NON-NLS-1$

	/**
	 * Creates a new abstract field editor.
	 */
	public AbstractFieldEditor( )
	{
		super( );
	}

	/**
	 * Creates a new abstract field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public AbstractFieldEditor( String name, String labelText, Composite parent )
	{
		super( name, labelText, parent );
	}

	/**
	 * @param defaultUnit
	 *            The defaultUnit to set.
	 */
	public void setDefaultUnit( String defaultUnit )
	{
		this.defaultUnit = defaultUnit;
	}

	/**
	 * @return Returns the defaultUnit.
	 */
	public String getDefaultUnit( )
	{
		return defaultUnit;
	}

	public void load( )
	{
		if ( getPreferenceStore( ) != null )
		{
			setPresentsDefaultValue( false );

			isLoaded = false;
			doLoad( );
			isLoaded = true;

			refreshValidState( );
		}
	}

	public void loadDefault( )
	{
		if ( getPreferenceStore( ) != null )
		{
			setPresentsDefaultValue( true );

			isLoaded = false;
			doLoadDefault( );
			isLoaded = true;

			refreshValidState( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore( )
	{
		if ( propValue == null )
		{
			getPreferenceStore( ).setToDefault( getPreferenceName( ) );
			return;
		}
		if ( isDirty( ) )
		{
			if ( propValue.length( ) > 0 )
			{
				getPreferenceStore( ).setValue( getPreferenceName( ), propValue );
			}
			else
			{
				getPreferenceStore( ).setValue( getPreferenceName( ), null );
			}

		}
	}

	/**
	 * Gets old value of the field editor.
	 * 
	 * @return Returns the old value.
	 */
	public String getOldValue( )
	{
		return oldValue;
	}

	/**
	 * Gets property value of the field editor.
	 * 
	 * @return Returns the property value.
	 */
	public String getPropValue( )
	{
		return propValue;
	}

	/**
	 * Sets old value of the field editor.
	 * 
	 * @param oldValue
	 *            The oldValue to set.
	 */
	protected void setOldValue( String oldValue )
	{
		if ( oldValue == null )
		{
			oldValue = ""; //$NON-NLS-1$
		}
		this.oldValue = oldValue;
		this.propValue = oldValue;
		markDirty( false );
	}

	/**
	 * Sets property value of the field editor.
	 * 
	 * @param newValue
	 *            The newValue to set.
	 */
	protected void setPropValue( String newValue )
	{
		if ( newValue == null )
		{
			newValue = ""; //$NON-NLS-1$
		}
		this.propValue = newValue;
	}

	/**
	 * Gets string value of the field editor.
	 * 
	 */
	protected abstract String getStringValue( );

	/**
	 * Performs value changes.
	 * 
	 * @param name
	 *            value name to changed.
	 */
	protected void valueChanged( String name )
	{
		if ( !isLoaded )
		{
			return;
		}
		String curValue = getPropValue( );
		String newValue = getStringValue( );
		setPresentsDefaultValue( false );
		if ( !curValue.equals( newValue ) )
		{
			fireValueChanged( name, curValue, newValue );
			setPropValue( newValue );
			markDirty( true );
		}
	}

	/**
	 * Marks the field editor is dirty.
	 */
	protected void markDirty( boolean value )
	{
		isDirty = value;
	}

	/**
	 * Returns the dirty marker of the field editor.
	 */
	protected boolean isDirty( )
	{
		return isDirty;
		// if ( oldValue.equals( propValue ) )
		// {
		// return false;
		// }
		// return true;
	}
}