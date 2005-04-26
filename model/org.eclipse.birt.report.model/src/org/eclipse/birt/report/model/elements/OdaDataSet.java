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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.ExtensionValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSetModel;
import org.eclipse.birt.report.model.extension.AddOnExtensibilityProvider;
import org.eclipse.birt.report.model.extension.ExtensibilityProvider;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;

/**
 * Represents an extended data set.
 */

public class OdaDataSet extends DataSet implements IExtendableElement, IOdaDataSetModel
{

	

	private ExtensibilityProvider provider = null;

	/**
	 * ODA data set can support extension. It has a unique name to identify the
	 * extension. Using this name, BIRT can get the extension definition. The
	 * name is an internal name for an implementation of extension.
	 * <p>
	 * The name does not occur in a name space.
	 */

	protected String extensionName = null;

	/**
	 * Default constructor.
	 */

	public OdaDataSet( )
	{
		super( );
	}

	/**
	 * Constructs an extended data set with name.
	 * 
	 * @param theName
	 *            the name of this extended data set
	 */

	public OdaDataSet( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */
	public void apply( ElementVisitor visitor )
	{
		visitor.visitOdaDataSet( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName( )
	{
		return ReportDesignConstants.ODA_DATA_SET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */
	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element
	 */

	public OdaDataSetHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new OdaDataSetHandle( design, this );
		}
		return (OdaDataSetHandle) handle;
	}

	/**
	 * Gets the definition of the extension element.
	 * 
	 * @return the definition of the extension element if found, or null if the
	 *         extended item is not extensible or the extension element is not
	 *         registered in BIRT
	 */

	public ExtensionElementDefn getExtDefn( )
	{
		if ( provider != null )
			return provider.getExtDefn( );

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefns()
	 */
	
	public List getPropertyDefns( )
	{
		if ( provider != null )
			return provider.getPropertyDefns( );

		return super.getPropertyDefns( );
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefn(java.lang.String)
	 */
	
	public ElementPropertyDefn getPropertyDefn( String propName )
	{
		assert propName != null;

		ElementPropertyDefn propDefn = super.getPropertyDefn( propName );
		if ( propDefn != null  )
			return propDefn;
		
		if (provider != null) 
			return (ElementPropertyDefn) provider.getPropertyDefn( propName );
		
		return propDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( EXTENSION_NAME_PROP.equals( propName ) )
			return extensionName;

		return super.getIntrinsicProperty( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( EXTENSION_NAME_PROP.equals( propName ) )
		{
			extensionName = (String) value;
			provider = new AddOnExtensibilityProvider( this, extensionName );
		}
		else
		{
			super.setIntrinsicProperty( propName, value );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkExtends(org.eclipse.birt.report.model.core.DesignElement)
	 */
	
	public void checkExtends( DesignElement parent ) throws ExtendsException
	{
		super.checkExtends( parent );

		if ( provider != null )
			provider.checkExtends( parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		list
				.addAll( ExtensionValidator.getInstance( ).validate( design,
						this ) );

		return list;
	}
}