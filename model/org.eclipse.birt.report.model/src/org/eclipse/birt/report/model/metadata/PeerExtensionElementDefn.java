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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the extension element definition for peer extension support. The
 * details of peer extension, please refer to
 * {@link org.eclipse.birt.report.model.extension.PeerExtensibilityProvider}.
 * This class is only used for those extension element definition from
 * third-party, not the BIRT-defined standard elements. The extension element
 * definition must include an instance of
 * {@link org.eclipse.birt.report.model.api.extension.IReportItemFactory}. The
 * included IElmentFactory gives the information about the internal model
 * properties of the extension element, how to instantiate
 * {@link org.eclipse.birt.report.model.api.extension.IReportItem}and other
 * information.
 */

public final class PeerExtensionElementDefn extends ExtensionElementDefn
{

	/**
	 * The element factory of the extended element.
	 */

	protected IReportItemFactory reportItemFactory = null;

	/**
	 * Extension defined allowed units.
	 */

	protected Map overrideAllowedUnits = new HashMap( );

	/**
	 * The factory to create scriptable classes. 
	 */
	
	private IScriptableObjectClassInfo scriptableFactory = null;
	
	/**
	 * Constructs the peer extension element definition with the element
	 * definition name and report item factory.
	 * 
	 * @param name
	 *            the name of the extension element definition
	 * @param reportItemFactory
	 *            the report item factory of the extension element
	 */

	public PeerExtensionElementDefn( String name,
			IReportItemFactory reportItemFactory )
	{
		assert name != null;
		assert reportItemFactory != null;
		this.name = name;
		this.reportItemFactory = reportItemFactory;
	}

	/**
	 * Gets the report item factory of this extension element definition.
	 * 
	 * @return the report item factory of the extension element definition
	 */

	public IReportItemFactory getReportItemFactory( )
	{
		return reportItemFactory;
	}

	/*
	 * Returns the localized display name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return name of
	 * this element definition.
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ObjectDefn#getDisplayName()
	 */

	public String getDisplayName( )
	{
		if ( displayNameKey != null && reportItemFactory != null )
		{
			IMessages messages = reportItemFactory.getMessages( );

			if ( messages != null )
			{
				String displayName = messages.getMessage( displayNameKey,
						ThreadResources.getLocale( ) );

				if ( !StringUtil.isBlank( displayName ) )
					return displayName;
			}
		}

		return getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementDefn#buildProperties()
	 */

	protected void buildProperties( ) throws MetaDataException
	{
		super.buildProperties( );

		if ( PeerExtensionLoader.EXTENSION_POINT
				.equalsIgnoreCase( extensionPoint ) )
		{
			// extensions must have 'extensionName' property
			ElementDefn extendedItem = (ElementDefn) MetaDataDictionary
					.getInstance( ).getElement(
							ReportDesignConstants.EXTENDED_ITEM );
			PropertyDefn extensionName = (PropertyDefn) extendedItem
					.getProperty( IExtendedItemModel.EXTENSION_NAME_PROP );
			if ( getProperty( IExtendedItemModel.EXTENSION_NAME_PROP ) == null )
			{
				properties.put( extensionName.getName( ), extensionName );
				cachedProperties.put( extensionName.getName( ), extensionName );
			}
		}

		// modify extended item's allowed unit.
		// especially for 'height' and 'width' property in Chart.
		
		overrideProperty( );
	}

	/**
	 * Override allowedUnits property.
	 * 
	 */

	private void overrideProperty( )
	{
		Set set = overrideAllowedUnits.keySet( );
		Iterator iterator = set.iterator( );
		while ( iterator.hasNext( ) )
		{
			String propName = (String) iterator.next( );
			String units = (String) overrideAllowedUnits.get( propName );

			// don't support override local property.

			if ( properties.get( propName ) != null )
				continue;

			ChoiceSet choiceSet = buildChoiceSet( units );
			if ( choiceSet == null )
				continue;
			PropertyDefn defn = (PropertyDefn) cachedProperties.get( propName );
			if ( defn == null )
				continue;

			PropertyDefn clonedDefn = reflectClass( defn );
			if ( clonedDefn == null )
				continue;
			clonedDefn.allowedChoices = choiceSet;
			cachedProperties.put( propName, clonedDefn );

		}
	}

	/**
	 * build Choice Set.
	 * 
	 * @param units
	 *            units such as 'in,cm'
	 * @return choice set.
	 */

	private ChoiceSet buildChoiceSet( String units )
	{
		List choiceList = new ArrayList( );
		if ( units != null && units.length( ) > 0 )
		{
			String[] eachUnit = units.split( "," ); //$NON-NLS-1$

			for ( int i = 0; eachUnit != null && i < eachUnit.length; ++i )
			{
				String unit = eachUnit[i];
				if ( unit != null && unit.length( ) > 0 )
				{
					IChoiceSet romSet = MetaDataDictionary.getInstance( )
							.getChoiceSet( DesignChoiceConstants.CHOICE_UNITS );
					IChoice romChoice = romSet.findChoice( unit );

					if ( romChoice != null )
					{
						choiceList.add( romChoice );
					}
				}
			}
		}
		if ( choiceList.size( ) == 0 )
			return null;

		ChoiceSet choiceSet = new ChoiceSet( );

		Choice[] choices = new Choice[choiceList.size( )];
		choiceList.toArray( choices );
		choiceSet.setChoices( choices );

		return choiceSet;
	}

	/**
	 * Reflects to clone new instance of property defn.
	 * 
	 * @param defn
	 *            property defn
	 * @return shadow cloned property defn.
	 */

	private PropertyDefn reflectClass( PropertyDefn defn )
	{
		String className = defn.getClass( ).getName( );
		try
		{
			Class clazz = Class.forName( className );
			PropertyDefn clonedDefn = (PropertyDefn) clazz.newInstance( );

			Class ownerClass = defn.getClass( );
			Class clonedClass = clonedDefn.getClass( );

			shadowCopyProperties( defn, clonedDefn, ownerClass, clonedClass );
			return clonedDefn;
		}
		catch ( InstantiationException e )
		{
		}
		catch ( IllegalAccessException e )
		{
		}
		catch ( ClassNotFoundException e )
		{
		}

		return null;
	}

	/**
	 * Shadow copy all properties to cloned property defn instance.
	 * 
	 * @param defn
	 *            property definition
	 * @param clonedDefn
	 *            cloned property definition
	 * @param ownerClass
	 *            property defn class
	 * @param clonedClass
	 *            cloned property defn class
	 */

	private void shadowCopyProperties( PropertyDefn defn,
			PropertyDefn clonedDefn, Class ownerClass, Class clonedClass )
	{
		if ( ownerClass == null || clonedClass == null )
			return;

		Field[] fields = ownerClass.getDeclaredFields( );
		for ( int i = 0; i < fields.length; ++i )
		{
			Field field = fields[i];
			try
			{
				Object property = field.get( defn );
				Field clonedField = ownerClass.getDeclaredField( field
						.getName( ) );
				clonedField.set( clonedDefn, property );
			}
			catch ( IllegalArgumentException e )
			{
				continue;
			}
			catch ( IllegalAccessException e )
			{
				continue;
			}
			catch ( SecurityException e )
			{
				continue;
			}
			catch ( NoSuchFieldException e )
			{
				continue;
			}
		}
		shadowCopyProperties( defn, clonedDefn, ownerClass.getSuperclass( ),
				clonedClass.getSuperclass( ) );
	}

	/**
	 * Sets extended allowed choices
	 * 
	 * @param prop
	 *            property name
	 * @param allowedUnits
	 *            allowed units. for example: in,cm,pt,%
	 */

	protected void setExtendedAllowedChoices( String prop, String allowedUnits )
	{
		overrideAllowedUnits.put( prop, allowedUnits );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.ElementDefn#isContainer()
	 */
	public boolean isContainer( )
	{
		return isContainer;
	}
	
	/**
	 * Returns the factory to create scriptable class for ROM defined elements.
	 * 
	 * @return the scriptable factory
	 */

	public IScriptableObjectClassInfo getScriptableFactory( )
	{
		return scriptableFactory;
	}

	/**
	 * Sets the factory to create scriptable class for ROM defined elements.
	 * 
	 * @param scriptableFactory
	 *            the scriptable factory to set
	 */

	void setScriptableFactory( IScriptableObjectClassInfo scriptableFactory )
	{
		this.scriptableFactory = scriptableFactory;
	}
}
