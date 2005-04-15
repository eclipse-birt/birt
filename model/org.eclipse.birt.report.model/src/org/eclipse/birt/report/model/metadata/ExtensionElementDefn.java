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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;


/**
 * Represents the extension element definition based on Model extension point.
 * This class only used for those extension definition from third-party, not the
 * Model-defined standard elements.
 * 
 * <h3>Property Visibility</h3>
 * All extension element definition support property visibility, which defines
 * to something like read-only, or hide. This is used to help UI display the
 * property value or the entire property. When extension element defines the
 * visibility for a Model-defined property, the property definition will be
 * copied and overridden in this extension element definition.
 */

public abstract class ExtensionElementDefn extends ElementDefn
{

	/**
	 * The list contains the information that how the property sheet shows an
	 * property for an extension element.
	 */

	protected Map propVisibilites = null;

	/**
	 * The list contains the overridden system properties. Each one is the instance
	 * of <code>IPropertyDefn</code> whose visibility is modified.
	 */

	protected Map overriddenSystemProps = null;
	
	/**
	 * the extension point that this extension definition extended from.
	 */
	String extensionPoint = null;

	/**
	 * Adds an invisible property to the list.
	 * 
	 * @param propName
	 *            the property name
	 * @param propVisibility
	 *            the level that how to show the property in the property sheet.
	 */

	protected void addPropertyVisibility( String propName, String propVisibility )
	{
		if ( propVisibilites == null )
			propVisibilites = new HashMap( );

		propVisibilites.put( propName, propVisibility );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementDefn#build()
	 */
	protected void build( ) throws MetaDataException
	{
		if ( isBuilt )
			return;

		buildDefn( );

		// Cache data for properties defined here. Note, done here so
		// we don't repeat the work for any style properties copied below.

		buildProperties( );

		buildStyleProperties( );

		buildOverriddenSystemProperties( );

		isBuilt = true;
	}

	/**
	 * Creates the overridden system properties for <code>PropertyInvisible</code>
	 * tags.
	 */

	protected void buildOverriddenSystemProperties( )
	{
		if ( propVisibilites == null )
			return;

		for ( Iterator iter = propVisibilites.keySet( ).iterator( ); iter
				.hasNext( ); )
		{
			String propName = (String) iter.next( );
			SystemPropertyDefn sysDefn = (SystemPropertyDefn) parent
					.getProperty( propName );

			if ( sysDefn != null
					&& ( sysDefn.isVisible( ) || !sysDefn.isReadOnly( ) ) )
			{
				if ( overriddenSystemProps == null )
					overriddenSystemProps = new HashMap( );

				SystemPropertyDefn defn = createPropertyDefn( sysDefn );
				defn.setVisibility( (String) propVisibilites.get( propName ) );
				overriddenSystemProps.put( propName, defn );
			}
		}
	}

	/**
	 * Returns a overridden system property definition.
	 * 
	 * @param propName
	 *            the property name
	 * @return the <code>SystemPropertyDefn</code> of the corresponding
	 *         <code>propName</code>.
	 */

	public SystemPropertyDefn getOverriddenSystemProperty( String propName )
	{
		if ( overriddenSystemProps == null )
			return null;

		return (SystemPropertyDefn) overriddenSystemProps.get( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.IElementDefn#getProperties()
	 */

	public List getProperties( )
	{
		List props = super.getProperties( );

		// Update the definition of the property whose visibility is modified.

		List newProps = new ArrayList( );
		for ( int i = 0; i < props.size( ); i++ )
		{
			ElementPropertyDefn prop = (ElementPropertyDefn) props.get( i );
			if ( !isMasked( prop.getName( ) ) )
			{
				// if extension redefined the property visibility, uses it.

				SystemPropertyDefn overriddenDefn = getOverriddenSystemProperty( prop
						.getName( ) );

				if ( overriddenDefn != null )
					newProps.add( overriddenDefn );
				else
					newProps.add( prop );
			}
		}

		return newProps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.IElementDefn#getProperty(java.lang.String)
	 */

	public IElementPropertyDefn getProperty( String propName )
	{
		IElementPropertyDefn propDefn = super.getProperty( propName );
		if ( propDefn != null )
		{
			SystemPropertyDefn overriddenPropDefn = getOverriddenSystemProperty( propName );
			if ( overriddenPropDefn != null )
				propDefn = overriddenPropDefn;
		}

		return propDefn;
	}

	/**
	 * Creates a new <code>SystemPropertyDefn</code> with a given
	 * <code>SystemPropertyDefn</code>.
	 * 
	 * @param defn
	 *            the given system property definition
	 * @return the new created <code>SystemPropertyDefn</code>
	 */

	private static SystemPropertyDefn createPropertyDefn(
			SystemPropertyDefn defn )
	{
		assert defn != null;

		SystemPropertyDefn newDefn = new SystemPropertyDefn( );

		// 10 members on the PropertyDefn

		newDefn.setDefault( defn.getDefault( ) );
		newDefn.setAllowedChoices( (ChoiceSet)defn.getAllowedChoices( ) );
		newDefn.setDisplayNameID( defn.getDisplayNameID( ) );
		newDefn.setIntrinsic( defn.isIntrinsic( ) );
		newDefn.setExtended( defn.isExtended( ) );
		newDefn.setIsList( defn.isList( ) );
		newDefn.setName( defn.getName( ) );
		newDefn.setType( defn.getType( ) );
		newDefn.setValueValidator( defn.valueValidator );
		newDefn.details = defn.details;

		// 2 members on the ElementPropertyDefn

		newDefn.setStyleProperty( defn.isStyleProperty( ) );

		newDefn.setGroupNameKey( defn.getGroupNameKey( ) );

		// 1 member on the SystemPropertyDefn

		newDefn.setStyleProperty( defn.isStyleProperty( ) );

		return newDefn;
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

	public boolean isMasked( String propName )
	{
		// TODO: the mask for style property is not supported now.

		return false;
	}

}