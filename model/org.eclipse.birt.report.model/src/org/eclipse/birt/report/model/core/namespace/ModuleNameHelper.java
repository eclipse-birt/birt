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

package org.eclipse.birt.report.model.core.namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.GroupNameValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.NamePropertyType;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;

/**
 * 
 */
public class ModuleNameHelper extends AbstractNameHelper
{

	protected Module module = null;

	/**
	 * The array of cached content names. These elements have not been added to
	 * the name space in fact. However, name of them are reserved and can not be
	 * used again to avoid the duplicate. This may be used when some extensions
	 * is not well-parsed or other reasons.
	 */
	private List<String> cachedContentNames[] = new ArrayList[Module.NAME_SPACE_COUNT];

	/**
	 * This map to store all level elements for the backward compatibility after
	 * convert level name to local unique in dimension. It just used in parser.
	 * After parser, we will clear it.
	 */
	private Map<String, DesignElement> cachedLevelNames = new HashMap<String, DesignElement>( );

	/**
	 * 
	 * @param module
	 */
	public ModuleNameHelper( Module module )
	{
		super( );
		this.module = module;
		initialize( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.AbstractNameHelper#initialize
	 * ()
	 */
	protected void initialize( )
	{
		int count = getNameSpaceCount( );
		nameContexts = new INameContext[count];
		for ( int i = 0; i < count; i++ )
		{
			nameContexts[i] = NameContextFactory.createModuleNameContext(
					module, i );
			cachedContentNames[i] = new ArrayList( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#getNameSpaceCount
	 * ()
	 */
	public int getNameSpaceCount( )
	{
		return Module.NAME_SPACE_COUNT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#getUniqueName
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */
	public String getUniqueName( DesignElement element )
	{
		if ( element == null )
			return null;

		if ( element instanceof GroupElement )
		{
			String groupName = getUniqueGroupName( (GroupElement) element );
			return groupName;
		}

		ElementDefn eDefn = (ElementDefn) element.getDefn( );

		// if the element does not reside in the module, then get name helper
		// for the element and generate unique name from there
		if ( !module.getDefn( ).isKindOf(
				eDefn.getNameConfig( ).getNameContainer( ) ) )
		{
			INameHelper nameHelper = new NameExecutor( element )
					.getNameHelper( module );
			return nameHelper == null ? null : nameHelper
					.getUniqueName( element );
		}

		String name = StringUtil.trimString( element.getName( ) );

		// replace all the illegal chars with '_'
		name = NamePropertyType.validateName( name );

		// Some elements can have a blank name.
		if ( eDefn.getNameOption( ) == MetaDataConstants.NO_NAME )
			return null;

		if ( eDefn.getNameOption( ) == MetaDataConstants.OPTIONAL_NAME
				&& name == null && module instanceof ReportDesign )
			return null;

		if ( module instanceof Library && element instanceof StyleElement
				&& element.getContainer( ) == null && name != null )
		{
			return name;
		}

		// If the element already has a unique name, return it.
		int nameSpaceID = eDefn.getNameSpaceID( );
		NameSpace nameSpace = getCachedNameSpace( nameSpaceID );
		List<String> cachedContentNames = getCachedContentNames( nameSpaceID );
		NameSpace moduleNameSpace = nameContexts[nameSpaceID].getNameSpace( );
		if ( name != null && isValidInNameSpace( nameSpace, element, name )
				&& isValidInNameSpace( moduleNameSpace, element, name )
				&& !cachedContentNames.contains( name ) )
			return name;

		// If the element has no name, create it as "New<new name>" where
		// "<new name>" is the new element display name for the element. Both
		// "New" and the new element display name are localized to the user's
		// locale.

		if ( name == null )
		{
			// When creating a new report element which requires a name, the
			// default name will be "New" followed by the element name, such as
			// "New Label"; also, if "NewLabel" already exists, then a number
			// will be appended, such as "NewLabel1", etc.

			if ( element instanceof ExtendedItem )
			{
				ExtensionElementDefn extDefn = ( (ExtendedItem) element )
						.getExtDefn( );

				PeerExtensionElementDefn peerDefn = (PeerExtensionElementDefn) extDefn;
				IReportItemFactory peerFactory = peerDefn
						.getReportItemFactory( );

				assert peerFactory != null;

				String extensionDefaultName = null;
				IMessages msgs = peerFactory.getMessages( );
				if ( msgs != null )
					extensionDefaultName = msgs
							.getMessage( (String) extDefn.getDisplayNameKey( ),
									ThreadResources.getLocale( ) );

				if ( StringUtil.isBlank( extensionDefaultName ) )
					extensionDefaultName = peerDefn.getName( );

				name = ModelMessages
						.getMessage( MessageConstants.NAME_PREFIX_NEW_MESSAGE );

				name = name + extensionDefaultName;

			}
			else
			{
				name = ModelMessages.getMessage( "New." //$NON-NLS-1$
						+ element.getDefn( ).getName( ) );
				name = name.trim( );
			}
		}

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String baseName = name;
		while ( nameSpace.contains( name ) || moduleNameSpace.contains( name )
				|| cachedContentNames.contains( name ) )
		{
			name = baseName + ++index;
		}

		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#makeUniqueName
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */
	public void makeUniqueName( DesignElement element )
	{
		// if element is groupelement , set unique group name.
		if ( element instanceof GroupElement )
		{
			String name = getUniqueName( element );

			if ( name != null )
				setUniqueGroupName( (GroupElement) element, name );
			return;
		}

		super.makeUniqueName( element );
	}

	/**
	 * Gets the cached content name list with the given id.
	 * 
	 * @param id
	 *            the name space id to get
	 * @return the cached content name list with the given id
	 */
	List<String> getCachedContentNames( int id )
	{
		assert id >= 0 && id < Module.NAME_SPACE_COUNT;
		return cachedContentNames[id];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#dropElement(
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */
	public void dropElement( DesignElement element )
	{
		if ( element == null )
			return;

		ElementDefn defn = (ElementDefn) element.getDefn( );
		int id = defn.getNameSpaceID( );
		NameSpace ns = getCachedNameSpace( id );
		if ( ns.getElement( element.getName( ) ) == element )
			ns.remove( element );
	}

	/**
	 * Returns a unique name for the group element. The name is unique in the
	 * scope of the table.
	 * 
	 * @param element
	 *            the group element.
	 * @return unique name of group element.
	 * 
	 */
	private String getUniqueGroupName( GroupElement group )
	{
		if ( group == null || group.getContainer( ) == null )
			return null;

		ListingHandle listing = (ListingHandle) group.getContainer( )
				.getHandle( module );

		String groupName = (String) group.getLocalProperty( module,
				IGroupElementModel.GROUP_NAME_PROP );

		// replace all the illegal chars with '_'
		groupName = NamePropertyType.validateName( groupName );

		if ( StringUtil.isBlank( groupName ) )
		{
			GroupElement virtualGroup = (GroupElement) group.getVirtualParent( );

			while ( virtualGroup != null )
			{
				groupName = (String) virtualGroup.getLocalProperty(
						virtualGroup.getRoot( ),
						IGroupElementModel.GROUP_NAME_PROP );
				if ( !StringUtil.isBlank( groupName ) )
				{
					break;
				}

				virtualGroup = (GroupElement) virtualGroup.getVirtualParent( );
			}
		}

		String namePrefix = groupName;
		int level = group.getGroupLevel( );

		if ( StringUtil.isBlank( namePrefix ) )
		{
			namePrefix = ModelMessages.getMessage( "New." //$NON-NLS-1$ 
					+ group.getDefn( ).getName( ) );
			namePrefix = namePrefix.trim( );
			groupName = namePrefix + level;
		}

		while ( true )
		{
			if ( GroupNameValidator.getInstance( )
					.validateForRenamingGroup( listing,
							(GroupHandle) group.getHandle( module ), groupName )
					.size( ) == 0 )
			{
				break;
			}

			groupName = namePrefix + level;
			level++;

		}

		return groupName;

	}

	/**
	 * Creates a unique name for the group element. The name is unique in the
	 * scope of the table.
	 * 
	 * @param element
	 *            the group element.
	 * @param groupName
	 *            name of group element.
	 * 
	 */
	private void setUniqueGroupName( GroupElement group, String groupName )
	{
		assert groupName != null;

		String localGroupName = (String) group.getLocalProperty( module,
				IGroupElementModel.GROUP_NAME_PROP );
		if ( groupName.equals( localGroupName ) )
			return;

		group.setProperty( IGroupElementModel.GROUP_NAME_PROP, groupName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#addContentName
	 * (int, java.lang.String)
	 */
	public void addContentName( int id, String name )
	{
		if ( id >= 0 && id < Module.NAME_SPACE_COUNT )
		{
			if ( !cachedContentNames[id].contains( name ) )
				cachedContentNames[id].add( name );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#getElement()
	 */
	public DesignElement getElement( )
	{
		return module;
	}

	/**
	 * 
	 * @param level
	 * @return true if level is correctly added, otherwise false
	 */
	public boolean addCachedLevel( DesignElement level )
	{
		if ( !( level instanceof Level ) )
			return true;
		String name = level.getName( );

		if ( name == null )
			return true;
		if ( cachedLevelNames.get( name ) != null
				&& cachedLevelNames.get( name ) != level )
			return false;
		this.cachedLevelNames.put( level.getName( ), level );
		return true;
	}

	/**
	 * Finds a level by the given qualified name.
	 * 
	 * @param elementName
	 * @return the level if found, otherwise null
	 */
	public Level findCachedLevel( String elementName )
	{
		if ( elementName == null )
			return null;

		String namespace = StringUtil.extractNamespace( elementName );
		String name = StringUtil.extractName( elementName );
		if ( namespace == null )
			return (Level) cachedLevelNames.get( name );
		Library lib = module.getLibraryWithNamespace( namespace );
		return lib == null ? null : (Level) ( (ModuleNameHelper) lib
				.getNameHelper( ) ).findCachedLevel( name );
	}

	/**
	 * 
	 */
	public void clearCachedLevels( )
	{
		cachedLevelNames = null;
		List<Library> libs = module.getAllLibraries( );
		if ( libs == null )
			return;
		for ( int i = 0; i < libs.size( ); i++ )
		{
			Library lib = libs.get( i );
			( (ModuleNameHelper) lib.getNameHelper( ) ).cachedLevelNames = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.AbstractNameHelper#cacheValues
	 * ()
	 */

	public void cacheValues( )
	{
		// do the cache for all resolved styles.

		AbstractModuleNameContext tmpContext = (AbstractModuleNameContext) getNameContext( Module.STYLE_NAME_SPACE );
		tmpContext.cacheValues( );
	}
}
