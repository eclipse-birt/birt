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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.command.ExtendsException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.extension.ExtendedElementException;
import org.eclipse.birt.report.model.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.extension.IReportItem;
import org.eclipse.birt.report.model.extension.IReportItemFactory;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionModelPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * This class represents an extended item element. The extended report item
 * allows third-party developers to create report items that work within BIRT
 * virtually identically to BIRT-defined items. Extended items can use the
 * user-properties discussed above to define properties, can use a ¡°black-box¡±
 * approach, or a combination of the two. Extended items are defined in a Java
 * plug-in that contributes behavior to the Eclipse Report Developer, to the
 * Factory and to the Presentation Engine. The extended item can fully
 * participate with the other BIRT extension facilities, meaning that report
 * developers can additional properties and scripts to an extended item,
 * providing a very powerful way to create application-specific functionality.
 * An extended item is defined by a plug-in. The plug-in is specific to BIRT,
 * and is different from an Eclipse plug-in. Each item plug-in has four parts:
 * <ul>
 * <li>Design: handles the model that describes the report item.
 * <li>User Interface: the UI displayed for the item. This is in the form of an
 * Eclipse plug-in.
 * <li>Factory: how to gather the data for the extended item, and compute its
 * default size in the Factory.
 * <li>Presentation: how to render the extended item when rendering the report
 * to HTML, PDF or other formats.
 * </ul>
 * 
 *  
 */

public class ExtendedItem extends ReportItem
{

	/**
	 * Name of the property that identifies the name of the extension. BIRT uses
	 * the property to find extension definition in our meta-data dictionary.
	 */

	public static final String EXTENSION_PROP = "extension"; //$NON-NLS-1$

	/**
	 * Extended item can support extension. It has a unique name to identify the
	 * extension. Using this name, BIRT can get the extension definition. The
	 * name is an internal name for an implementation of extension.
	 * <p>
	 * The name does not occur in a name space.
	 */

	protected String extName = null;

	/**
	 * Cached property values for those defined by extension if there is no
	 * instance of the peer element. When we created the instance of peer, clear
	 * all contents in the map and store them in the peer itself.
	 */

	HashMap extValues = new HashMap( );

	/**
	 * Peer element for the extension. It is created when the application
	 * invokes UI for the item.
	 */

	IReportItem extElement = null;

	/**
	 * The extension definition of the item has.
	 */

	ExtensionElementDefn cachedExtDefn = null;

	//	/**
	//	 * The style masks list of the extension defined. The object in it is the
	//	 * property name of those BIRT standard properties made invisible.
	//	 */
	//
	//	ArrayList styleMasks = null;

	/**
	 * Default constructor.
	 */

	public ExtendedItem( )
	{
	}

	/**
	 * Constructs the extended item with an optional name.
	 * 
	 * @param theName
	 *            optional item name
	 */

	public ExtendedItem( String theName )
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
		visitor.visitExtendedItem( this );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.EXTENDED_ITEM;
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
	 * 
	 * @return an API handle for this element.
	 */

	public ExtendedItemHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new ExtendedItemHandle( design, this );
		}
		return (ExtendedItemHandle) handle;
	}

	/**
	 * Gets a property value given its definition. This version checks not only
	 * this one object, but also the extended element this item has. That is, it
	 * gets the "local" property value. The property name must also be valid for
	 * this object.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getLocalProperty(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getLocalProperty( ReportDesign design,
			ElementPropertyDefn prop )
	{
		assert prop != null;

		if ( !prop.isExtended( ) )
			return super.getLocalProperty( design, prop );

		if ( isExtensionXMLType( prop.getName( ) ) )
		{
			if ( extElement != null )
			{
				ByteArrayOutputStream stream = extElement.serialize( prop
						.getName( ) );
				if ( stream == null )
					return null;
				return stream.toString( );
			}

			return extValues.get( prop.getName( ) );
		}
		if ( isExtensionModelProperty( prop.getName( ) ) )
		{
			if ( extElement != null )
				return extElement.getProperty( prop.getName( ) );
			return extValues.get( prop.getName( ) );
		}
		return extValues.get( prop.getName( ) );
	}

	/**
	 * Sets the value of a property. The value must have already been validated,
	 * and must be of the correct type for the property. The property must be
	 * valid for this object. The property can be a system, user-defined
	 * property and a property from the extended element of this item. The value
	 * is set locally. If the value is null, then the property is "unset."
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setProperty(org.eclipse.birt.report.model.metadata.ElementPropertyDefn,
	 *      java.lang.Object)
	 */

	public void setProperty( ElementPropertyDefn prop, Object value )
	{
		assert prop != null;

		if ( !prop.isExtended( ) )
			super.setProperty( prop, value );
		else if ( isExtensionXMLType( prop.getName( ) ) )
		{
			if ( extElement != null )
			{
				try
				{
					if ( value != null )
						extElement.deserialize( prop.getName( ),
								new ByteArrayInputStream( value.toString( )
										.getBytes( ) ) );
					else
						extElement.deserialize( prop.getName( ),
								new ByteArrayInputStream( null ) );
				}
				catch ( ExtendedElementException e )
				{
					assert false;
				}
			}
		}
		if ( isExtensionModelProperty( prop.getName( ) ) )
		{
			if ( extElement != null )
				extElement.setProperty( prop.getName( ), value );
		}
		else
		{
			if ( value != null )
				extValues.put( prop.getName( ), value );
			else
				extValues.remove( prop.getName( ) );
		}
	}

	/**
	 * Gets the property data for either a system-defined, user-defined property
	 * or extension property from extended element of this item.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefn(java.lang.String)
	 */

	public ElementPropertyDefn getPropertyDefn( String propName )
	{
		assert propName != null;

		ElementPropertyDefn defn = super.getPropertyDefn( propName );

		// if the extended item is a normal report item, then return it

		assert hasExtension( );

		// if the extended item has the extension, then check the
		// style masks first

		if ( defn != null )
		{
			if ( isMasked( defn.getName( ) ) )
				return null;
			return defn;
		}

		// check whether the property is defined by extension

		ExtensionElementDefn extDefn = getExtDefn( );
		assert extDefn != null;
		ElementPropertyDefn prop = extDefn.getProperty( propName );

		if ( prop != null )
			return prop;

		IPropertyDefinition[] extProps = getExtensionModel( );
		if ( extProps == null )
			return null;
		for ( int i = 0; i < extProps.length; i++ )
		{
			IPropertyDefinition extProp = extProps[i];
			if ( propName.equalsIgnoreCase( extProp.getName( ) ) )
				return new ExtensionModelPropertyDefn( extProp, getExtDefn( )
						.getElementFactory( ).getMessages( ) );
		}

		return null;
	}

	/**
	 * Gets the list of property definitions available to this element. Includes
	 * all properties defined for this element, all user-defined properties
	 * defined on this element or its ancestors, any style properties that this
	 * element supports and extension properties that the extended element of
	 * this item supports.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefns()
	 */

	public List getPropertyDefns( )
	{
		List props = super.getPropertyDefns( );
		assert props != null;

		if ( !hasExtension( ) )
			return props;

		List temp = new ArrayList( );
		for ( int i = 0; i < props.size( ); i++ )
		{
			PropertyDefn prop = (PropertyDefn) props.get( i );
			if ( !isMasked( prop.getName( ) ) )
				temp.add( prop );
		}
		props = temp;

		ExtensionElementDefn extDefn = getExtDefn( );
		assert extDefn != null;

		if ( extDefn.getProperties( ) != null )
		{
			props.addAll( extDefn.getProperties( ) );
		}

		// if we support dynamic property list, there
		// will be those properties of IElement

		IPropertyDefinition[] dynamicProps = getExtensionModel( );
		if ( dynamicProps == null )
			return props;
		for ( int i = 0; i < dynamicProps.length; i++ )
		{
			IPropertyDefinition extProp = dynamicProps[i];
			props.add( new ExtensionModelPropertyDefn( extProp, getExtDefn( )
					.getElementFactory( ).getMessages( ) ) );
		}

		return props;
	}

	/**
	 * Gets all the extension dynamic properties.
	 * 
	 * @return the extension dynamic properties, null if extended element is
	 *         null
	 */

	private IPropertyDefinition[] getExtensionModel( )
	{
		if ( extElement == null )
			return null;
		return extElement.getPropertyDefinitions( );
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
		assert extName != null;
		if ( cachedExtDefn != null )
			return cachedExtDefn;

		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		cachedExtDefn = dd.getExtension( extName );

		assert cachedExtDefn != null;
		return cachedExtDefn;
	}

	/**
	 * Gets the list of style masks for BIRT style properties.
	 * 
	 * @return the list of the style masks
	 */

	protected ArrayList getStyleMasks( )
	{
		//		if ( !isExtended( ) )
		//			return null;
		//		IROMExtension peer = getExtDefn( );
		//		assert peer != null;
		//
		//		if ( styleMasks != null )
		//			return styleMasks;
		//		if ( peer != null )
		//			styleMasks = peer.getStyleMasks( );
		//
		//		return styleMasks;
		return null;
	}

	/**
	 * Justifies whether the extended item is extensible or it is a normal
	 * report item.
	 * 
	 * @return true if the extended item is extensible, otherwise false
	 */

	protected boolean hasExtension( )
	{
		return !StringUtil.isBlank( extName );
	}

	/**
	 * Checks whether the property has the mask defined by the peer extension
	 * given the property name.
	 * 
	 * @param propName
	 *            the property name to check
	 * @return true if the style masks defined by peer extension of the item is
	 *         found, otherwise false
	 */

	protected boolean isMasked( String propName )
	{
		ArrayList masks = getStyleMasks( );
		if ( masks == null )
			return false;
		return masks.contains( propName );
	}

	/**
	 * Creates an instance of <code>IPeerElement</code> to store the
	 * information of the peer extension. When the application invokes UI for
	 * the extended item, it calls this method to get the instance of the peer
	 * extension and reads the information--property values from the BIRT ROM
	 * properties. If there is no instance of peer for the item before the
	 * calling and then it is successfully created. If the item has no extension
	 * peer for it or the peer instance has been created before, then there is
	 * no operation.
	 * 
	 * @param design
	 *            the report design the peer element has
	 * @throws ExtendedElementException
	 *             if the serialized model is invalid
	 */

	public void newPeerElement( ReportDesign design )
			throws ExtendedElementException
	{
		assert hasExtension( );
		if ( extElement != null )
			return;
		ExtensionElementDefn extDefn = getExtDefn( );
		assert extDefn != null;
		IReportItemFactory elementFactory = extDefn.getElementFactory( );
		assert elementFactory != null;
		extElement = elementFactory.newReportItem( design.handle( ) );

		// if the item caches the property values of extension, transfer them
		// and then clear the cached values

		List names = new ArrayList( );

		Collection values = extValues.keySet( );
		if ( values != null )
		{
			Iterator iter = values.iterator( );
			while ( iter.hasNext( ) )
			{
				String propName = (String) iter.next( );
				ElementPropertyDefn prop = getPropertyDefn( propName );
				assert prop != null;

				Object value = extValues.get( propName );
				assert value != null;
				if ( prop.getTypeCode( ) == PropertyType.XML_TYPE )
				{
					extElement.deserialize( prop.getName( ),
							new ByteArrayInputStream( value.toString( )
									.getBytes( ) ) );
					names.add( propName );
				}
			}

		}

		for ( int i = 0; i < names.size( ); i++ )
		{
			extValues.remove( names.get( i ) );
		}
	}

	/**
	 * Sets the extension name of the item.
	 * 
	 * @param extName
	 *            the extension name to set
	 */

	public void setExtension( String extName )
	{
		this.extName = extName;
	}

	/**
	 * Gets the extension name of the item.
	 * 
	 * @return the extension name of the item
	 */

	public String getExtension( )
	{
		return this.extName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( EXTENSION_PROP.equals( propName ) )
			return extName;
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
		if ( EXTENSION_PROP.equals( propName ) )
			extName = (String) value;
		else
			super.setIntrinsicProperty( propName, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		if ( extElement != null )
		{
			try
			{
				extElement.validate( );
			}
			catch ( ExtendedElementException e )
			{
				list.add( e );
			}
		}
		return list;
	}

	/**
	 * Gets the effective extension element of this extended item.
	 * 
	 * @return the effective extension element
	 */

	public IReportItem getExtendedElement( )
	{
		return extElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkExtends(org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void checkExtends( DesignElement parent ) throws ExtendsException
	{
		super.checkExtends( parent );
		String parentExt = (String) parent.getProperty( null,
				ExtendedItem.EXTENSION_PROP );

		assert extName != null;
		if ( !extName.equalsIgnoreCase( parentExt ) )
			throw new ExtendsException( this, parent,
					ExtendsException.WRONG_EXTENSION_TYPE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		ExtendedItem element = (ExtendedItem) super.clone( );
		element.cachedExtDefn = null;
		element.extElement = null;
		element.extValues = null;

		// clear the cached extension definition

		// if the extended element is not null, just copy it

		if ( extElement != null )
		{
			element.extElement = extElement.copy( );
		}

		// extension Properties

		Iterator it = extValues.keySet( ).iterator( );
		element.extValues = new HashMap( );
		while ( it.hasNext( ) )
		{
			String key = (String) it.next( );
			PropertyDefn propDefn = getPropertyDefn( key );

			if ( propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE )
			{
				if ( propDefn.isList( ) )
				{
					element.extValues
							.put( key, cloneStructList( (ArrayList) extValues
									.get( key ) ) );
				}
				else
				{
					element.extValues.put( key, ( (Structure) extValues
							.get( key ) ).copy( ) );
				}
			}
			else if ( propDefn.getTypeCode( ) != PropertyType.ELEMENT_REF_TYPE )
			{
				//Primitive or immutable values

				element.extValues.put( key, extValues.get( key ) );
			}
		}

		return element;
	}

	/**
	 * Tests whether the property is a dynamic property of extended element or
	 * not.
	 * 
	 * @param propName
	 *            the property name to check
	 * @return true if the property is one of the dynamic properties for
	 *         extended element, otherwise false
	 */

	public boolean isExtensionModelProperty( String propName )
	{
		if ( extElement != null )
		{
			IPropertyDefinition[] extProps = extElement
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
	 * Tests whether the property is just the model property for the extended
	 * element, and its type is XML.
	 * 
	 * @param propName
	 *            the property name to check
	 * @return true if the property is XML type and it is model property for
	 *         extended element, otherwise false
	 */

	public boolean isExtensionXMLType( String propName )
	{
		ExtensionElementDefn extDefn = getExtDefn( );
		assert extDefn != null;
		PropertyDefn prop = extDefn.getProperty( propName );
		if ( prop != null && PropertyType.XML_TYPE == prop.getTypeCode( ) )
			return true;
		return false;
	}
}