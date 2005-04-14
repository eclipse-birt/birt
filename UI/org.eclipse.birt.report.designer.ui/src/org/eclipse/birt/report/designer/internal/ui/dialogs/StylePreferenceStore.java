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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.model.api.FormatHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Shell;

/**
 * Provides get/set of model for field editors
 */

public class StylePreferenceStore implements IPreferenceStore
{

	private Object model;

	private boolean hasError = false;

	/**
	 * The constructor.
	 * 
	 * @param model
	 */
	public StylePreferenceStore( Object model )
	{
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
	 */
	public void addPropertyChangeListener( IPropertyChangeListener listener )
	{
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#contains(java.lang.String)
	 */
	public boolean contains( String name )
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#firePropertyChangeEvent(java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void firePropertyChangeEvent( String name, Object oldValue,
			Object newValue )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getBoolean(java.lang.String)
	 */
	public boolean getBoolean( String name )
	{
		return ( (StyleHandle) model ).getBooleanProperty( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultBoolean(java.lang.String)
	 */
	public boolean getDefaultBoolean( String name )
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultDouble(java.lang.String)
	 */
	public double getDefaultDouble( String name )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultFloat(java.lang.String)
	 */
	public float getDefaultFloat( String name )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultInt(java.lang.String)
	 */
	public int getDefaultInt( String name )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultLong(java.lang.String)
	 */
	public long getDefaultLong( String name )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultString(java.lang.String)
	 */
	public String getDefaultString( String name )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDouble(java.lang.String)
	 */
	public double getDouble( String name )
	{
		// TODO Auto-generated method stub
		return ( (StyleHandle) model ).getFloatProperty( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getFloat(java.lang.String)
	 */
	public float getFloat( String name )
	{
		return (float) ( (StyleHandle) model ).getFloatProperty( name );
	}

	/**
	 *  
	 */
	public FormatHandle getStringFormat( )
	{
		return ( (StyleHandle) model ).getStringFormat( );
	}

	/**
	 *  
	 */
	public FormatHandle getDateTimeFormat( )
	{
		return ( (StyleHandle) model ).getDateTimeFormat( );
	}

	/**
	 *  
	 */
	public FormatHandle getNumberFormat( )
	{
		return ( (StyleHandle) model ).getNumberFormat( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getInt(java.lang.String)
	 */
	public int getInt( String name )
	{
		return ( (StyleHandle) model ).getIntProperty( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getLong(java.lang.String)
	 */
	public long getLong( String name )
	{
		//TODO
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getString(java.lang.String)
	 */
	public String getString( String name )
	{
		return ( (StyleHandle) model ).getStringProperty( name );
	}

	public PropertyHandle getPropertyHandle( String name )
	{
		return ( (StyleHandle) model ).getPropertyHandle( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#isDefault(java.lang.String)
	 */
	public boolean isDefault( String name )
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#needsSaving()
	 */
	public boolean needsSaving( )
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#putValue(java.lang.String,
	 *      java.lang.String)
	 */
	public void putValue( String name, String value )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
	 */
	public void removePropertyChangeListener( IPropertyChangeListener listener )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
	 *      double)
	 */
	public void setDefault( String name, double value )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
	 *      float)
	 */
	public void setDefault( String name, float value )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
	 *      int)
	 */
	public void setDefault( String name, int value )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
	 *      long)
	 */
	public void setDefault( String name, long value )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
	 *      java.lang.String)
	 */
	public void setDefault( String name, String defaultObject )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
	 *      boolean)
	 */
	public void setDefault( String name, boolean value )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setToDefault(java.lang.String)
	 */
	public void setToDefault( String name )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 *      double)
	 */
	public void setValue( String name, double value )
	{
		try
		{
			( (StyleHandle) model ).setProperty( name, new Double( value ) );
		}
		catch ( SemanticException e )
		{
			hasError = true;
			WidgetUtil.processError( new Shell( ), e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 *      float)
	 */
	public void setValue( String name, float value )
	{
		try
		{
			( (StyleHandle) model ).setProperty( name, new Double( value ) );
		}
		catch ( SemanticException e )
		{
			hasError = true;
			WidgetUtil.processError( new Shell( ), e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 *      int)
	 */
	public void setValue( String name, int value )
	{
		try
		{
			( (StyleHandle) model ).setProperty( name, new Integer( value ) );
		}
		catch ( SemanticException e )
		{
			hasError = true;
			WidgetUtil.processError( new Shell( ), e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 *      long)
	 */
	public void setValue( String name, long value )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 *      java.lang.String)
	 */
	public void setValue( String name, String value )
	{
		try
		{
			( (StyleHandle) model ).setProperty( name, value );
		}
		catch ( SemanticException e )
		{
			hasError = true;
			WidgetUtil.processError( new Shell( ), e );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 *      boolean)
	 */
	public void setValue( String name, boolean value )
	{
		try
		{
			( (StyleHandle) model ).setProperty( name, Boolean.valueOf( value ) );
		}
		catch ( SemanticException e )
		{
			hasError = true;
			WidgetUtil.processError( new Shell( ), e );
		}
	}

	/**
	 * Clears the error token.
	 */
	public void clearError( )
	{
		hasError = false;
	}

	/**
	 * Checks if the previous operations cause error.
	 * 
	 * @return
	 */
	public boolean hasError( )
	{
		return hasError;
	}
}