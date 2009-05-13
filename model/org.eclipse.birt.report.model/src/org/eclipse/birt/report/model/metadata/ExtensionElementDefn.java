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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;

/**
 * Represents the extension element definition based on Model extension point.
 * This class only used for those extension definition from third-party, not the
 * Model-defined standard elements.
 * 
 * <h3>Property Visibility</h3> All extension element definition support
 * property visibility, which defines to something like read-only, or hide. This
 * is used to help UI display the property value or the entire property. When
 * extension element defines the visibility for a Model-defined property, the
 * property definition will be copied and overridden in this extension element
 * definition.
 */

public abstract class ExtensionElementDefn extends ElementDefn
{

	private static Logger logger = Logger.getLogger( ExtensionElementDefn.class
			.getName( ) );

	/**
	 * The extension point that this extension definition extended from.
	 */

	protected String extensionPoint = null;

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

		buildPropertiesVisibility( );

		buildContainerProperties( );

		// set the xml-name to that of ExtendedItem
		ElementDefn defn = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.EXTENDED_ITEM );
		setXmlName( defn.getXmlName( ) );

		// build slot
		buildSlots( );

		// build validation trigger
		buildTriggerDefnSet( );

		// if name is not defined, the set the name options
		if ( cachedProperties.get( IDesignElementModel.NAME_PROP ) == null )
		{
			nameConfig.nameOption = MetaDataConstants.NO_NAME;
			nameConfig.nameSpaceID = MetaDataConstants.NO_NAME_SPACE;
			nameConfig.holder = null;
		}

		isBuilt = true;
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

	/**
	 * Gets the extension point of this extension element.
	 * 
	 * @return the extension point of this extension element
	 */

	public String getExtensionPoint( )
	{
		return this.extensionPoint;
	}

	/**
	 * Reflects to clone new instance of property definition.
	 * 
	 * @param defn
	 *            property definition
	 * @return shadow cloned property definition.
	 */

	protected PropertyDefn reflectClass( PropertyDefn defn )
	{
		ElementPropertyDefn retDefn = null;

		String className = defn.getClass( ).getName( );
		try
		{
			Class<? extends Object> clazz = Class.forName( className );
			retDefn = (ElementPropertyDefn) clazz.newInstance( );

			Class<? extends Object> ownerClass = defn.getClass( );
			Class<? extends Object> clonedClass = retDefn.getClass( );

			shadowCopyProperties( defn, retDefn, ownerClass, clonedClass );
		}
		catch ( InstantiationException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$				
		}
		catch ( IllegalAccessException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
		}
		catch ( ClassNotFoundException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
			MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
		}

		if ( retDefn == null )
			return null;

		shadowCopyProperties( defn, retDefn, defn.getClass( ),
				ExtensionPropertyDefn.class );

		return retDefn;
	}

	/**
	 * Shadow copy all properties to cloned property definition instance.
	 * 
	 * @param defn
	 *            property definition
	 * @param clonedDefn
	 *            cloned property definition
	 * @param ownerClass
	 *            property definition class
	 * @param clonedClass
	 *            cloned property definition class
	 */

	private void shadowCopyProperties( PropertyDefn defn,
			PropertyDefn clonedDefn, Class<? extends Object> ownerClass,
			Class<? extends Object> clonedClass )
	{
		if ( ownerClass == null || clonedClass == null )
			return;

		Field[] fields = ownerClass.getDeclaredFields( );
		for ( int i = 0; i < fields.length; ++i )
		{
			Field field = fields[i];
			if ( ( field.getModifiers( ) & Modifier.STATIC ) != 0 )
				continue;

			try
			{
				Object property = field.get( defn );
				Field clonedField = ownerClass.getDeclaredField( field
						.getName( ) );
				clonedField.set( clonedDefn, property );
			}
			catch ( IllegalArgumentException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	

				continue;
			}
			catch ( IllegalAccessException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
			catch ( SecurityException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
			catch ( NoSuchFieldException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
				MetaLogManager.log( "Overrides property error", e ); //$NON-NLS-1$	
				continue;
			}
		}
		shadowCopyProperties( defn, clonedDefn, ownerClass.getSuperclass( ),
				clonedClass.getSuperclass( ) );
	}
}