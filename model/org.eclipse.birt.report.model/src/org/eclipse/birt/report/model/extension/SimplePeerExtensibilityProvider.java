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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.extension.UndefinedPropertyInfo;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.parser.treebuild.ContentTree;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * 
 */
public class SimplePeerExtensibilityProvider extends PeerExtensibilityProvider
{

	/**
	 * Map to store the invalid extension name/value pairs. Only invalid
	 * extensible value is recorded not including rom-defined properties.
	 */
	private LinkedHashMap invalidValueMap = null;

	/**
	 * Map to store the undefined property name/value pairs.
	 */
	private LinkedHashMap undefinedPropertyMap = null;

	/**
	 * Map to store the undefined children map. The key is the container
	 * property and value is list of all the illegal children in this container
	 * property. The order of the children in the list is the order residing in
	 * the xml design file. The item in the list is instance of
	 * <code>UndefinedChildInfo</code>.
	 */
	private Map illegalChildrenMap = null;

	/**
	 * 
	 * @param element
	 * @param extensionName
	 */
	public SimplePeerExtensibilityProvider( DesignElement element,
			String extensionName )
	{
		super( element, extensionName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#handleInvalidPropertyValue(java.lang.String,
	 *      java.lang.Object)
	 */
	public void handleInvalidPropertyValue( String propName, Object value )
	{
		assert value != null;
		assert propName != null;

		PropertyDefn defn = element.getPropertyDefn( propName );
		if ( defn.isExtended( ) )
		{
			if ( invalidValueMap == null )
				invalidValueMap = new LinkedHashMap( );
			String extensionVersion = element.getStringProperty( element
					.getRoot( ), IExtendedItemModel.EXTENSION_VERSION_PROP );
			UndefinedPropertyInfo infor = new UndefinedPropertyInfo( propName,
					value, extensionVersion );
			invalidValueMap.put( propName, infor );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#handleUndefinedChildren(java.lang.String,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */
	public void handleIllegalChildren( String propName, DesignElement child )
	{
		ElementPropertyDefn propDefn = element.getPropertyDefn( propName );
		assert propDefn != null;
		assert child != null;
		if ( illegalChildrenMap == null )
			illegalChildrenMap = new HashMap( );
		List childList = (List) illegalChildrenMap.get( propName );
		if ( childList == null )
			childList = new ArrayList( );

		int count = childList.size( );
		List contents = (List) element.getProperty( null, propDefn );
		count += contents == null ? 0 : contents.size( );
		UndefinedChildInfo infor = new UndefinedChildInfo( child, count );
		childList.add( infor );

		illegalChildrenMap.put( propName, childList );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#handleUndefinedProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public void handleUndefinedProperty( String propName, Object value )
	{
		assert propName != null;
		assert value != null;

		if ( undefinedPropertyMap == null )
			undefinedPropertyMap = new LinkedHashMap( );

		String extensionVersion = element.getStringProperty(
				element.getRoot( ), IExtendedItemModel.EXTENSION_VERSION_PROP );
		// now we can only handle simple type, such as int, string, simple value
		// list; other complex types, such as structure, structure list, we can
		// not handle
		UndefinedPropertyInfo infor = new UndefinedPropertyInfo( propName,
				value, extensionVersion );
		undefinedPropertyMap.put( propName, infor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.treebuild.IContentHandler#getContentTree()
	 */
	public ContentTree getContentTree( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#copyFrom(org.eclipse.birt.report.model.extension.PeerExtensibilityProvider)
	 */
	public void copyFrom( PeerExtensibilityProvider source )
	{
		super.copyFrom( source );

		SimplePeerExtensibilityProvider provider = (SimplePeerExtensibilityProvider) source;
		invalidValueMap = null;
		illegalChildrenMap = null;
		undefinedPropertyMap = null;

		// handle invalid value map
		if ( provider.invalidValueMap != null
				&& !provider.invalidValueMap.isEmpty( ) )
		{
			invalidValueMap = new LinkedHashMap( );
			Iterator iter = provider.invalidValueMap.keySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				UndefinedPropertyInfo infor = (UndefinedPropertyInfo) provider.invalidValueMap
						.get( iter.next( ) );
				if ( infor != null )
				{
					UndefinedPropertyInfo clonedInfor = new UndefinedPropertyInfo(
							infor.getPropName( ), infor.getValue( ), infor
									.getExtensionVersion( ) );
					invalidValueMap.put( clonedInfor.getPropName( ),
							clonedInfor );
				}
			}
		}

		// handle undefined property
		if ( provider.undefinedPropertyMap != null
				&& !provider.undefinedPropertyMap.isEmpty( ) )
		{
			undefinedPropertyMap = new LinkedHashMap( );

			// now the value is simple type, so do this simple handle; otherwise
			// we will handle complex type to do deep clone
			Iterator iter = provider.undefinedPropertyMap.keySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				UndefinedPropertyInfo infor = (UndefinedPropertyInfo) provider.undefinedPropertyMap
						.get( iter.next( ) );
				if ( infor != null )
				{
					UndefinedPropertyInfo clonedInfor = new UndefinedPropertyInfo(
							infor.getPropName( ), infor.getValue( ), infor
									.getExtensionVersion( ) );
					undefinedPropertyMap.put( clonedInfor.getPropName( ),
							clonedInfor );
				}
			}
		}

		// handle undefined children
		if ( provider.illegalChildrenMap != null
				&& !provider.illegalChildrenMap.isEmpty( ) )
		{
			illegalChildrenMap = getCopiedIllegalContents( provider.illegalChildrenMap );
		}
	}

	/**
	 * Gets a deep cloned map for illegal contents.
	 * 
	 * @param illegalContentsMap
	 * @return
	 */
	public static Map getCopiedIllegalContents( Map illegalContentsMap )
	{
		if ( illegalContentsMap == null || illegalContentsMap.isEmpty( ) )
			return Collections.EMPTY_MAP;

		Map ret = new HashMap( );
		Iterator iter = illegalContentsMap.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String propName = (String) iter.next( );
			List childList = (List) illegalContentsMap.get( propName );
			if ( childList != null && !childList.isEmpty( ) )
			{
				List clonedList = new ArrayList( );
				for ( int i = 0; i < childList.size( ); i++ )
				{
					UndefinedChildInfo infor = (UndefinedChildInfo) childList
							.get( i );
					UndefinedChildInfo clonedInfor = new UndefinedChildInfo(
							null, -1 );
					clonedInfor.copyFrom( infor );
					clonedList.add( clonedInfor );
				}

				ret.put( propName, clonedList );
			}
		}

		return ret;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#getIllegalChildren()
	 */
	public Map getIllegalContents( )
	{
		return illegalChildrenMap == null
				? Collections.EMPTY_MAP
				: illegalChildrenMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#getInvalidPropertyValueMap()
	 */
	public Map getInvalidPropertyValueMap( )
	{
		return this.invalidValueMap == null
				? Collections.EMPTY_MAP
				: invalidValueMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#getUndefinedPropertyMap()
	 */
	public Map getUndefinedPropertyMap( )
	{
		return this.undefinedPropertyMap == null
				? Collections.EMPTY_MAP
				: undefinedPropertyMap;
	}

//	private Map getMergedUndefinedProperty( )
//	{
//		Map ret = new HashMap( );
//		if ( this.undefinedPropertyMap != null )
//			ret.putAll( undefinedPropertyMap );
//
//		// search all the inheritances
//		DesignElement e = element.getExtendsElement( ) == null ? element
//				.getVirtualParent( ) : element.getExtendsElement( );
//		while ( e != null )
//		{
//			assert e instanceof ExtendedItem;
//			PeerExtensibilityProvider provider = ( (ExtendedItem) e )
//					.getExtensibilityProvider( );
//			Map tempMap = provider.getLocalUndefinedPropertyMap( );
//			if ( tempMap != null && !tempMap.isEmpty( ) )
//			{
//				Iterator iter = tempMap.keySet( ).iterator( );
//				while ( iter.hasNext( ) )
//				{
//					String propName = (String) iter.next( );
//					// if this property is not re-set in the child, then add it
//					// to the result
//					if ( ret.get( propName ) == null )
//					{
//						Object infor = tempMap.get( propName );
//						if ( infor != null )
//							ret.put( propName, infor );
//					}
//				}
//			}
//
//			e = e.getExtendsElement( ) == null ? e.getVirtualParent( ) : e
//					.getExtendsElement( );
//		}
//
//		return ret;
//	}
//
//	private Map getMergedInvalidProperty( )
//	{
//		Map ret = new HashMap( );
//		if ( this.invalidValueMap != null )
//			ret.putAll( invalidValueMap );
//
//		// search all the inheritances
//		DesignElement e = element.getExtendsElement( ) == null ? element
//				.getVirtualParent( ) : element.getExtendsElement( );
//		while ( e != null )
//		{
//			assert e instanceof ExtendedItem;
//			PeerExtensibilityProvider provider = ( (ExtendedItem) e )
//					.getExtensibilityProvider( );
//			Map tempMap = provider.getLocalInvalidPropertyValueMap( );
//			if ( tempMap != null && !tempMap.isEmpty( ) )
//			{
//				Iterator iter = tempMap.keySet( ).iterator( );
//				while ( iter.hasNext( ) )
//				{
//					String propName = (String) iter.next( );
//					// if this property is not re-set in the child( including
//					// both valid value and invalid value), then add it to the
//					// result
//					if ( ret.get( propName ) == null
//							&& element.getLocalProperty( element.getRoot( ),
//									propName ) == null )
//					{
//						Object infor = tempMap.get( propName );
//						if ( infor != null )
//							ret.put( propName, infor );
//					}
//				}
//			}
//
//			e = e.getExtendsElement( ) == null ? e.getVirtualParent( ) : e
//					.getExtendsElement( );
//		}
//
//		return ret;
//	}

	/**
	 * 
	 */
	static public class UndefinedChildInfo
	{

		/**
		 * The child that can not be inserted to container.
		 */
		protected DesignElement child;

		/**
		 * The index where the child resides in the xml source.
		 */
		protected int index;

		/**
		 * Constructs the infor by the child element and the position.
		 * 
		 * @param child
		 * @param index
		 */
		UndefinedChildInfo( DesignElement child, int index )
		{
			this.child = child;
			this.index = index;
		}

		/**
		 * Copies this from the specified source information.
		 * 
		 * @param source
		 */
		void copyFrom( UndefinedChildInfo source )
		{
			if ( source == null )
				return;
			this.child = source.child;
			this.index = source.index;
			if ( child != null )
			{
				child = ModelUtil.getCopy( child );
			}
		}

		/**
		 * 
		 * @return
		 */
		public DesignElement getChild( )
		{
			return child;
		}

		/**
		 * 
		 * @return
		 */
		public int getIndex( )
		{
			return index;
		}
	}
}
