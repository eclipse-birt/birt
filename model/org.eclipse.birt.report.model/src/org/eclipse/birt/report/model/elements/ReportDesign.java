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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.MasterPageRequiredValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * This class represents the root element in the report design hierarchy.
 * Contains the list of data sets, data sources, master pages, components, body
 * content, scratch pad and more. Code modules in the report gives
 * specifications for global scripts that apply to the report as a whole.Report
 * design is valid if it is opened without error or with semantic error.
 * Otherwise, it's invalid.
 * 
 */

public class ReportDesign extends Module implements IReportDesignModel
{

	/**
	 * The default units for the design.
	 */

	protected String units = null;

	/**
	 * Default constructor.
	 * 
	 * @deprecated
	 */

	public ReportDesign( )
	{
		super( null );
		initSlots( );
		onCreate( );
	}

	/**
	 * Constructs the report design with the session.
	 * 
	 * @param session
	 *            the session that owns this design
	 */

	public ReportDesign( DesignSession session )
	{
		super( session );
		initSlots( );
		onCreate( );
	}

	/**
	 * Makes a clone of this root element. The error list, file name are set to
	 * empty. A new ID map was generated for the new cloned element.
	 * 
	 * @return Object the cloned report design element.
	 * 
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone( ) throws CloneNotSupportedException
	{
		ReportDesign design = (ReportDesign) super.clone( );
		design.allErrors = new ArrayList( );
		design.initSlots( );
		for ( int i = 0; i < slots.length; i++ )
		{
			design.slots[i] = slots[i].copy( design, i );
		}
		design.translations = (TranslationTable) translations.clone( );
		design.fileName = null;

		NameSpace.rebuildNamespace( design );

		return design;
	}

	/**
	 * Generate new ID map for the cloned report design.
	 * 
	 * @param design
	 *            the new design
	 * @param element
	 *            the element is traversed on
	 */
	private void generateIdMap( ReportDesign design, DesignElement element )
	{

		if ( element == null )
			return;

		IElementDefn defn = element.getDefn( );
		int id = design.getNextID( );
		element.setID( id );
		design.addElementID( element );
		// design.idMap.put( new Integer(id), element);

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = element.getSlot( i );

			if ( slot == null )
				continue;

			for ( int pos = 0; pos < slot.getCount( ); pos++ )
			{
				DesignElement innerElement = slot.getContent( pos );
				generateIdMap( design, innerElement );
			}

		}

	}

	/**
	 * Builds the default elements for a new design. The elements include the
	 * standard styles. The elements are build directly, without the use of
	 * commands or the command stack. The creation operation cannot be undone.
	 * This operation must be done before the first activity stack operation.
	 */

	protected void onCreate( )
	{
		super.onCreate( );

		// Pass the validation executor to activity stack.

		activityStack.setValidationExecutor( validationExecutor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitReportDesign( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( Module module )
	{
		List list = super.validate( module );

		// Must there is more than one master page in setup page

		list.addAll( MasterPageRequiredValidator.getInstance( ).validate( this,
				this ) );

		list.addAll( validateStructureList( module, IMAGES_PROP ) );
		list.addAll( validateStructureList( module, COLOR_PALETTE_PROP ) );

		list.addAll( validateStructureList( module, INCLUDE_SCRIPTS_PROP ) );
		list.addAll( validateStructureList( module, INCLUDE_LIBRARIES_PROP ) );

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.REPORT_DESIGN_ELEMENT;
	}

	/**
	 * Return a list of user-defined message keys. The list contained resource
	 * keys defined in the report itself and the keys defined in the referenced
	 * message files for the current thread's locale. The list returned contains
	 * no duplicate keys.
	 * 
	 * @return a list of user-defined message keys.
	 */

	public List getMessageKeys( )
	{
		Set keys = new LinkedHashSet( );

		String[] transKeys = translations.getResourceKeys( );
		if ( transKeys != null )
		{
			for ( int i = 0; i < transKeys.length; i++ )
				keys.add( transKeys[i] );
		}

		// find from the referenced message files.
		// e.g: message

		String baseName = getStringProperty( this, INCLUDE_RESOURCE_PROP );
		if ( baseName == null )
			return new ArrayList( keys );

		File msgFolder = getDesignFolder( );
		if ( msgFolder == null )
			return new ArrayList( keys );

		Collection msgKeys = BundleHelper.getHelper( msgFolder, baseName )
				.getMessageKeys( ThreadResources.getLocale( ) );
		keys.addAll( msgKeys );

		return new ArrayList( keys );
	}

	/**
	 * Return the folder in which the design file is located. The search depend
	 * on the {@link #getFileName()}.
	 * 
	 * @return the folder in which the design file is located. Return
	 *         <code>null</code> if the folder can not be found.
	 */

	private File getDesignFolder( )
	{
		String designPath = getFileName( );
		File designFile = new File( designPath );
		if ( !designFile.exists( ) )
			return null;

		if ( designFile.isFile( ) )
			return designFile.getParentFile( );

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @return an API handle for this element
	 */

	public ReportDesignHandle handle( )
	{
		if ( handle == null )
		{
			handle = new ReportDesignHandle( this );
		}
		return (ReportDesignHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( propName.equals( UNITS_PROP ) )
		{
			return units;
		}
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
		if ( propName.equals( UNITS_PROP ) )
			units = (String) value;
		else
			super.setIntrinsicProperty( propName, value );
	}

	/**
	 * Gets the default units for the design.
	 * 
	 * @return the default units used in the design
	 */

	public String getUnits( )
	{
		if ( !StringUtil.isBlank( units ) )
			return units;
		return (String) getPropertyDefn( ReportDesign.UNITS_PROP ).getDefault( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.module#close()
	 */

	public void close( )
	{
		isValid = false;
		saveState = activityStack.getCurrentTransNo( );
		super.close( );
	}

	/**
	 * Checks if the file with <code>fileName</code> exists. The search steps
	 * are described in {@link #findResource(String, int)}.
	 * 
	 * @param fileName
	 *            the file name to check
	 * @param fileType
	 *            the file type
	 * @return true if the file exists, false otherwise.
	 */

	public boolean isFileExist( String fileName, int fileType )
	{
		URL url = findResource( fileName, fileType );

		return url != null;
	}


	/**
	 * Gets a list containing all the include scripts.
	 * 
	 * @return a list containing all the include scripts. Return
	 *         <code>null</code> if there were no scripts defined.
	 */

	public List getIncludeScripts( )
	{
		return (ArrayList) getLocalProperty( this, INCLUDE_SCRIPTS_PROP );
	}

	/**
	 * Finds an include script by the file name.
	 * 
	 * @param fileName
	 *            the script file name
	 * @return the defined include script that matches, or <code>null</code>
	 *         if the file name was not found in the include scripts list.
	 */

	public IncludeScript findIncludeScript( String fileName )
	{
		ArrayList list = (ArrayList) getLocalProperty( null,
				INCLUDE_SCRIPTS_PROP );
		if ( list == null )
			return null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			IncludeScript script = (IncludeScript) list.get( i );
			if ( script.getFileName( ) != null
					&& script.getFileName( ).equals( fileName ) )
				return script;
		}

		return null;
	}

	/**
	 * Gets a list containing all the include libraries.
	 * 
	 * @return a list containing all the include libraries. Return
	 *         <code>null</code> if there were no include libraries defined.
	 */

	public ArrayList getIncludeLibraries( )
	{
		return new ArrayList( (List) getLocalProperty( this,
				INCLUDE_LIBRARIES_PROP ) );
	}

	/**
	 * Finds user-defined messages for the given locale.
	 * <p>
	 * First we look up in the report itself, then look into the referenced
	 * message file. Each search uses a reduced form of Java locale-driven
	 * search algorithm: Language&Country, language, default.
	 * 
	 * @param resourceKey
	 *            Resource key of the user defined message.
	 * @param locale
	 *            locale of message, if the input <code>locale</code> is
	 *            <code>null</code>, the locale for the current thread will
	 *            be used instead.
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>""</code> if translation can not be found, or
	 *         <code>resourceKey</code> is blank or <code>null</code>.
	 */

	public String getMessage( String resourceKey, Locale locale )
	{
		if ( StringUtil.isBlank( resourceKey ) )
			return ""; //$NON-NLS-1$

		if ( locale == null )
			locale = ThreadResources.getLocale( );

		// find it in the design itself.

		String msg = translations.getMessage( resourceKey, locale );
		if ( msg != null )
			return msg;

		// find it in the linked resource file.

		String baseName = getStringProperty( this, INCLUDE_RESOURCE_PROP );
		if ( baseName == null )
			return ""; //$NON-NLS-1$

		File msgFolder = getDesignFolder( );
		if ( msgFolder == null )
			return ""; //$NON-NLS-1$

		return BundleHelper.getHelper( msgFolder, baseName ).getMessage(
				resourceKey, locale );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.module#getSlotCount()
	 */

	protected int getSlotCount( )
	{
		return SLOT_COUNT;
	}

}