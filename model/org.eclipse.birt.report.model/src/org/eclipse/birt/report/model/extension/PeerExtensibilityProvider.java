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

package org.eclipse.birt.report.model.extension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionModelPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionPropertyDefn;
import org.eclipse.birt.report.model.metadata.MethodInfo;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Represents the extensibility provider which supports the peer extension. The
 * peer extension means the third-party can define the extension element with
 * extension properties and extension model in extension definition file. The
 * extension element has its own display name, and attributes, which makes it
 * looks like a new element. However, the extension element is based on the
 * <code>ReportItem</code>, which Model defines, and can have style
 * properties. The extension element can define two type of properties:
 * <ul>
 * <li>The Extension Property - It is also defined in extension definition
 * file. It provides the name, data type, display name key and so on, as the
 * system property works for the Model-defined element.
 * <li>The Extension Model Property - It is defined by {@link IReportItem},
 * instead in extension definition file. <code>IReportItem</code> defines its
 * own dynamic model. The extension model property definition is defined by
 * {@link IPropertyDefinition}. The dynamic model means the extension element
 * can switch from one set of extension model properties to another under some
 * case. They can be serialized to an XML-type extension property when saving to
 * design file, and deserialized from this XML-type extension property when
 * loading design file.
 * </ul>
 * 
 */

public class PeerExtensibilityProvider extends ModelExtensibilityProvider
{

	/**
	 * Peer element for the extension. It is created when the application
	 * invokes UI for the item.
	 */

	IReportItem reportItem = null;

	/**
	 * Cached property values for extension properties, which is defined in
	 * extension definition file.
	 */

	HashMap extensionPropValues = new HashMap( );

	/**
	 * Constructs the peer extensibility provider with the extensible element
	 * and the extension name.
	 * 
	 * @param element
	 *            the extensible element
	 * @param extensionName
	 *            the extension name
	 */

	public PeerExtensibilityProvider( DesignElement element,
			String extensionName )
	{
		super( element, extensionName );
	}

	/**
	 * Returns the read-only list of all property definitions, including not
	 * only those defined in Model and extension definition file, but those
	 * defined by <code>IReportItem</code>. The returned list is read-only,
	 * so no modification is allowed on this list. Each one in list is the
	 * instance of <code>IPropertyDefn</code>.
	 * 
	 * @return the read-only list of all property definitions. Return empty list
	 *         if there is no property defined.
	 */

	public List getPropertyDefns( )
	{
		List props = super.getPropertyDefns( );

		PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn( );

		// if no extension definition exists, just return the definition on
		// extended item.

		if ( extDefn == null )
			return props;

		// If the extension provides dynamic property list, add them.

		IPropertyDefinition[] dynamicProps = getExtensionModelPropertyDefns( );
		if ( dynamicProps != null )
		{
			for ( int i = 0; i < dynamicProps.length; i++ )
			{
				IPropertyDefinition extProp = dynamicProps[i];
				props.add( new ExtensionModelPropertyDefn( extProp, extDefn
						.getReportItemFactory( ).getMessages( ) ) );
			}
		}

		return props;
	}

	/**
	 * Returns the methods defined on the element. Not only the method on the
	 * extension element definition but also include those defined inside the
	 * extension model.
	 * 
	 * @return the method list
	 */

	public List getModelMethodDefns( )
	{

		List methods = new ArrayList( );
		PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn( );
		if ( extDefn == null )
			return null;

		if ( extDefn.getMethods( ) != null )
			methods.addAll( extDefn.getMethods( ) );

		if ( reportItem == null )
			return null;

		IPropertyDefinition[] dynamicMethods = reportItem.getMethods( );
		if ( dynamicMethods != null )
		{
			for ( int i = 0; i < dynamicMethods.length; i++ )
			{
				IPropertyDefinition extProp = dynamicMethods[i];
				MethodInfo methodInfo = (MethodInfo) extProp.getMethodInfo( );
				ExtensionModelPropertyDefn modelPropDefn = new ExtensionModelPropertyDefn(
						extProp, extDefn.getReportItemFactory( ).getMessages( ) );
				modelPropDefn.setDetails( methodInfo );
				methods.add( modelPropDefn );
			}
		}
		return methods;
	}

	/**
	 * Returns the element property definition with the given name from Model,
	 * the extension definition file or extension model defined by
	 * <code>IReportItem</code>.
	 * 
	 * @param propName
	 *            name of the property
	 * @return the element property definition with the given name
	 */

	public IPropertyDefn getPropertyDefn( String propName )
	{
		IPropertyDefn propDefn = super.getPropertyDefn( propName );
		if ( propDefn == null )
		{
			PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn( );

			IPropertyDefinition[] extProps = getExtensionModelPropertyDefns( );
			if ( extProps != null )
			{
				for ( int i = 0; i < extProps.length; i++ )
				{
					if ( propName.equalsIgnoreCase( extProps[i].getName( ) ) )
					{
						return new ExtensionModelPropertyDefn( extProps[i],
								extDefn.getReportItemFactory( ).getMessages( ) );
					}
				}
			}
		}

		return propDefn;
	}

	/**
	 * Gets all the extension model properties defined by
	 * <code>IReportItem</code>.
	 * 
	 * @return the extension model properties, null if extended element is null
	 */

	private IPropertyDefinition[] getExtensionModelPropertyDefns( )
	{
		if ( reportItem == null )
			return null;

		return reportItem.getPropertyDefinitions( );
	}

	/**
	 * Gets the list of style masks for Model style properties.
	 * 
	 * @return the list of the style masks
	 */

	protected List getStyleMasks( )
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns the value of extension property or extension model property.
	 * 
	 * @param propName
	 *            name of the property
	 * @return the value of the given property. If the property is not found, or
	 *         the value is not set, return null.
	 */

	public Object getExtensionProperty( String propName )
	{

		if ( isExtensionXMLProperty( propName ) && hasOwnModel( propName ) )
		{
			if ( reportItem != null )
			{
				ByteArrayOutputStream stream = reportItem.serialize( propName );
				if ( stream == null )
					return null;

				String retValue = null;
				try
				{
					retValue = stream.toString( UnicodeUtil.SIGNATURE_UTF_8 );
				}
				catch ( UnsupportedEncodingException e )
				{
					assert false;
				}
				return retValue;
			}
		}
		else if ( isExtensionModelProperty( propName ) )
		{
			// If this property is extension model property, the instance of
			// IReportItem must exist.

			assert reportItem != null;

			return reportItem.getProperty( propName );
		}

		return extensionPropValues.get( propName );
	}

	/**
	 * Sets the value for extension property or extension model property.
	 * 
	 * @param prop
	 *            the definition of the property
	 * @param value
	 *            the value to set
	 */

	public void setExtensionProperty( ElementPropertyDefn prop, Object value )
	{
		if ( isExtensionXMLProperty( prop.getName( ) )
				&& hasOwnModel( prop.getName( ) ) )
		{
			if ( reportItem != null )
			{
				try
				{
					if ( value != null )
					{
						byte[] raw = null;
						try
						{
							raw = value.toString( ).getBytes(
									UnicodeUtil.SIGNATURE_UTF_8 );
						}
						catch ( UnsupportedEncodingException e )
						{
							assert false;
						}

						reportItem.deserialize( prop.getName( ),
								new ByteArrayInputStream( raw ) );
					}
					else
						reportItem.deserialize( prop.getName( ),
								new ByteArrayInputStream( new byte[0] ) );
				}
				catch ( ExtendedElementException e )
				{
					assert false;
				}
			}
			else
			{
				setExtensionPropertyValue( prop.getName( ), value );
			}
		}
		else if ( isExtensionModelProperty( prop.getName( ) ) )
		{
			// If this property is extension model property, the instance of
			// IReportItem must exist.

			assert reportItem != null;

			reportItem.setProperty( prop.getName( ), value );
		}
		else
		{
			setExtensionPropertyValue( prop.getName( ), value );
		}
	}

	/**
	 * Sets the value of the given property, which is extension property.
	 * 
	 * @param propName
	 *            the name of the property
	 * @param value
	 *            the value to set
	 */

	private void setExtensionPropertyValue( String propName, Object value )
	{
		if ( value != null )
			extensionPropValues.put( propName, value );
		else
			extensionPropValues.remove( propName );
	}

	/**
	 * Initializes the extension element instance of <code>IReportItem</code>.
	 * 
	 * @param module
	 *            module
	 * @throws ExtendedElementException
	 *             if the extension is not found or it's failed to initialized
	 *             the extension element instance.
	 */

	public void initializeReportItem( Module module )
			throws ExtendedElementException
	{
		if ( reportItem != null )
			return;

		PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn( );
		if ( extDefn == null )
			throw new ExtendedElementException( element,
					ModelException.PLUGIN_ID,
					SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND, null );

		IReportItemFactory elementFactory = extDefn.getReportItemFactory( );
		assert elementFactory != null;

		reportItem = elementFactory.newReportItem( element.getHandle( module ) );

		List localPropDefns = getExtDefn( ).getLocalProperties( );
		for ( int i = 0; i < localPropDefns.size( ); i++ )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) localPropDefns
					.get( i );

			if ( propDefn.getTypeCode( ) != IPropertyType.XML_TYPE
					|| !propDefn.canInherit( ) )
				continue;

			String propName = propDefn.getName( );
			if ( !hasOwnModel( propName ) )
				continue;

			Object value = extensionPropValues.get( propName );
			if ( value == null )
			{
				// Get the raw xml data from parent.
				ExtendedItem parent = (ExtendedItem)ModelUtil.getParent( element );
				while ( parent != null )
				{
					// get the value from the parent provider: read from the
					// hashmap or the reporItem
					PeerExtensibilityProvider parentProvider = parent
							.getExtensibilityProvider( );
					HashMap propValues = parentProvider.extensionPropValues;
					value = propValues.get( propName );
					if ( value == null )
					{
						if ( parentProvider.reportItem != null )
							value = parentProvider.reportItem
									.serialize( propName );
					}

					if ( value != null )
						break;

					parent = (ExtendedItem)ModelUtil.getParent( parent );
				}
			}
			else
			{
				// if the item caches the property values of extension, transfer
				// them and then clear the cached values

				this.extensionPropValues.remove( propName );
			}

			if ( value != null )
			{
				byte[] raw = null;
				try
				{
					raw = value.toString( ).getBytes(
							UnicodeUtil.SIGNATURE_UTF_8 );
				}
				catch ( UnsupportedEncodingException e )
				{
					assert false;
				}
				reportItem.deserialize( propName,
						new ByteArrayInputStream( raw ) );
			}
		}
	}

	/**
	 * Tests whether the property is an extension model property or not.
	 * 
	 * @param propName
	 *            name of the property to check
	 * @return true if the property is extension model property, otherwise false
	 */

	public boolean isExtensionModelProperty( String propName )
	{
		if ( reportItem != null )
		{
			IPropertyDefinition[] extProps = reportItem
					.getPropertyDefinitions( );
			if ( extProps != null )
			{
				for ( int i = 0; i < extProps.length; i++ )
				{
					IPropertyDefinition extProp = extProps[i];
					assert extProp != null;

					if ( propName.equals( extProp.getName( ) ) )
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Tests whether the property is the extension property which holds the
	 * serialized XML value for extension model properties. The property type
	 * should be XML.
	 * 
	 * @param propName
	 *            name of the property to check
	 * @return true if the property is XML type and holds the serialized XML
	 *         value for extension model properties, otherwise false
	 */

	public boolean isExtensionXMLProperty( String propName )
	{
		ExtensionElementDefn extDefn = getExtDefn( );
		if ( extDefn != null )
		{
			IPropertyDefn propDefn = extDefn.getProperty( propName );
			if ( propDefn != null
					&& IPropertyType.XML_TYPE == propDefn.getTypeCode( ) )
				return true;
		}

		return false;
	}

	/**
	 * Tests whether the property is the extension property which holds the
	 * serialized XML value for extension model properties. The property type
	 * should be XML.
	 * 
	 * @param propName
	 *            name of the property to check
	 * @return true if the property is XML type and holds the serialized XML
	 *         value for extension model properties, otherwise false
	 */

	private boolean hasOwnModel( String propName )
	{
		PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn( );
		ExtensionPropertyDefn tmpPropDefn = (ExtensionPropertyDefn) extDefn
				.getProperty( propName );

		if ( tmpPropDefn == null )
			return false;
		return tmpPropDefn.hasOwnModel( );
	}

	/**
	 * Copies the extension values and extension element instance, which
	 * implements <code>IReportItem</code>.
	 * 
	 * @param source
	 *            the source peer extensibility provider
	 */

	public void copyFrom( PeerExtensibilityProvider source )
	{
		// if the extended element is not null, just copy it

		reportItem = null;
		if ( source.reportItem != null )
		{
			reportItem = source.reportItem.copy( );
		}

		// extension Properties

		extensionPropValues = new HashMap( );

		Iterator it = source.extensionPropValues.keySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			String propName = (String) it.next( );
			PropertyDefn propDefn = element.getPropertyDefn( propName );

			Object value = source.extensionPropValues.get( propName );
			Object valueToSet = ModelUtil.copyValue( propDefn, value );
			extensionPropValues.put( propName, valueToSet );
		}
	}

	/**
	 * Return the extension element, which implements the interface
	 * <code>IReportItem</code>.
	 * 
	 * @return the extension element
	 */

	public IReportItem getExtensionElement( )
	{
		return reportItem;
	}

	/**
	 * Gets the script definition of this extended element.
	 * 
	 * @return the script definition
	 */

	public IPropertyDefinition getScriptPropertyDefinition( )
	{
		if ( reportItem != null )
			return reportItem.getScriptPropertyDefinition( );
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.ExtensibilityProvider#hasLocalPropertyValues()
	 */

	public boolean hasLocalPropertyValues( )
	{
		if ( !extensionPropValues.isEmpty( ) || reportItem != null )
			return true;

		return false;
	}
}
