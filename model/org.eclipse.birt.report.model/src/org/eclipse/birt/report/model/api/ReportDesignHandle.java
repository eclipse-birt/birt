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

package org.eclipse.birt.report.model.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.command.CustomMsgCommand;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.writer.DesignWriter;

/**
 * Represents the overall report design. The report design defines a set of
 * properties that describe the design as a whole like author, base and comments
 * etc.
 * <p>
 * 
 * Besides properties, it also contains a variety of elements that make up the
 * report. These include:
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Content Item</th>
 * <th width="40%">Description</th>
 * 
 * <tr>
 * <td>Code Modules</td>
 * <td>Global scripts that apply to the report as a whole.</td>
 * </tr>
 * 
 * <tr>
 * <td>Parameters</td>
 * <td>A list of Parameter elements that describe the data that the user can
 * enter when running the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Data Sources</td>
 * <td>The connections used by the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Data Sets</td>
 * <td>Data sets defined in the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Color Palette</td>
 * <td>A set of custom color names as part of the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Styles</td>
 * <td>User-defined styles used to format elements in the report. Each style
 * must have a unique name within the set of styles for this report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Page Setup</td>
 * <td>The layout of the master pages within the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Components</td>
 * <td>Reusable report items defined in this design. Report items can extend
 * these items. Defines a "private library" for this design.</td>
 * </tr>
 * 
 * <tr>
 * <td>Body</td>
 * <td>A list of the visual report content. Content is made up of one or more
 * sections. A section is a report item that fills the width of the page. It can
 * contain Text, Grid, List, Table, etc. elements</td>
 * </tr>
 * 
 * <tr>
 * <td>Scratch Pad</td>
 * <td>Temporary place to move report items while restructuring a report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Translations</td>
 * <td>The list of externalized messages specifically for this report.</td>
 * </tr>
 * 
 * <tr>
 * <td>Images</td>
 * <td>A list of images embedded in this report.</td>
 * </tr>
 * 
 * </table>
 * 
 * @see org.eclipse.birt.report.model.elements.ReportDesign
 */

public class ReportDesignHandle extends DesignElementHandle
{

	/**
	 * Constructs a handle with the given design. The application generally does
	 * not create handles directly. Instead, it uses one of the navigation
	 * methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 */

	public ReportDesignHandle( ReportDesign design )
	{
		super( design );
	}

	// Implementation of abstract method defined in base class.

	public DesignElement getElement( )
	{
		return design;
	}

	/**
	 * Finds a named element in the name space.
	 * 
	 * @param name
	 *            the name of the element to find
	 * @return a handle to the element, or <code>null</code> if the element
	 *         was not found.
	 */

	public DesignElementHandle findElement( String name )
	{
		DesignElement element = design.findElement( name );
		if ( element == null )
			return null;
		return element.getHandle( design );
	}

	/**
	 * Finds a style by its name.
	 * 
	 * @param name
	 *            name of the style
	 * @return a handle to the style, or <code>null</code> if the style is not
	 *         found
	 */

	public SharedStyleHandle findStyle( String name )
	{
		StyleElement style = design.findStyle( name );
		if ( style == null )
			return null;
		return (SharedStyleHandle) style.getHandle( design );
	}

	/**
	 * Finds a data source by name.
	 * 
	 * @param name
	 *            name of the data source
	 * @return a handle to the data source, or <code>null</code> if the data
	 *         source is not found
	 */

	public DataSourceHandle findDataSource( String name )
	{
		DesignElement element = design.findDataSource( name );
		if ( element == null )
			return null;
		return (DataSourceHandle) element.getHandle( design );
	}

	/**
	 * Finds a data set by name.
	 * 
	 * @param name
	 *            name of the data set
	 * @return a handle to the data set, or <code>null</code> if the data set
	 *         is not found
	 */

	public DataSetHandle findDataSet( String name )
	{
		DesignElement element = design.findDataSet( name );
		if ( element == null )
			return null;
		return (DataSetHandle) element.getHandle( design );
	}

	/**
	 * Finds a master page by name.
	 * 
	 * @param name
	 *            the name of the master page
	 * @return a handle to the master page, or <code>null</code> if the page
	 *         is not found
	 */

	public MasterPageHandle findMasterPage( String name )
	{
		DesignElement element = design.findPage( name );
		if ( element == null )
			return null;
		return (MasterPageHandle) element.getHandle( design );
	}

	/**
	 * Finds a parameter by name.
	 * 
	 * @param name
	 *            the name of the parameter
	 * @return a handle to the parameter, or <code>null</code> if the
	 *         parameter is not found
	 */

	public ParameterHandle findParameter( String name )
	{
		DesignElement element = design.findParameter( name );
		if ( element == null )
			return null;
		return (ParameterHandle) element.getHandle( design );
	}

	/**
	 * Returns the command stack that manages undo/redo operations for the
	 * design.
	 * 
	 * @return a command stack
	 * 
	 * @see CommandStack
	 */

	public CommandStack getCommandStack( )
	{
		return design.getActivityStack( );
	}

	/**
	 * Saves the design to an existing file name. Call this only when the file
	 * name has been set.
	 * 
	 * @throws IOException
	 *             if the file cannot be saved on the storage
	 * 
	 * @see #saveAs(String)
	 */

	public void save( ) throws IOException
	{
		String fileName = getFileName( );
		assert fileName != null;
		if ( fileName == null )
			return;
		design.prepareToSave( );
		DesignWriter writer = new DesignWriter( design );
		writer.write( new File( fileName ) );
		design.onSave( );
	}

	/**
	 * Saves the design to the file name provided. The file name is saved in the
	 * design, and subsequent calls to <code>save( )</code> will save to this
	 * new name.
	 * 
	 * @param newName
	 *            the new file name
	 * @throws IOException
	 *             if the file cannot be saved
	 * 
	 * @see #save()
	 */

	public void saveAs( String newName ) throws IOException
	{
		design.setFileName( newName );
		save( );
	}

	/**
	 * Finds the handle to an element by a given element ID. Returns
	 * <code>null</code> if the ID is not valid, or if this session does not
	 * use IDs.
	 * 
	 * @param id
	 *            ID of the element to find
	 * @return A handle to the element, or <code>null</code> if the element
	 *         was not found or this session does not use IDs.
	 */

	public DesignElementHandle getElementByID( int id )
	{
		DesignElement element = design.getElementByID( id );
		if ( element == null )
			return null;
		return element.getHandle( design );
	}

	/**
	 * Returns a slot handle to work with the sections in the report's Body
	 * slot. The order of sections within the slot determines the order in which
	 * the sections print.
	 * 
	 * @return A handle for working with the report sections.
	 */

	public SlotHandle getBody( )
	{
		return getSlot( ReportDesign.BODY_SLOT );
	}

	/**
	 * Returns a slot handle to work with the styles within the report. Note
	 * that the order of the styles within the slot is unimportant.
	 * 
	 * @return A handle for working with the styles.
	 */

	public SlotHandle getStyles( )
	{
		return getSlot( ReportDesign.STYLE_SLOT );
	}

	/**
	 * Returns a slot handle to work with the data sources within the report.
	 * Note that the order of the data sources within the slot is unimportant.
	 * 
	 * @return A handle for working with the data sources.
	 */

	public SlotHandle getDataSources( )
	{
		return getSlot( ReportDesign.DATA_SOURCE_SLOT );
	}

	/**
	 * Returns a slot handle to work with the data sets within the report. Note
	 * that the order of the data sets within the slot is unimportant.
	 * 
	 * @return A handle for working with the data sets.
	 */

	public SlotHandle getDataSets( )
	{
		return getSlot( ReportDesign.DATA_SET_SLOT );
	}

	/**
	 * Returns a slot handle to work with the master pages within the report.
	 * Note that the order of the master pages within the slot is unimportant.
	 * 
	 * @return A handle for working with the master pages.
	 */

	public SlotHandle getMasterPages( )
	{
		return getSlot( ReportDesign.PAGE_SLOT );
	}

	/**
	 * Returns a slot handle to work with the top-level parameters and parameter
	 * groups within the report. The order that the items appear within the slot
	 * determines the order in which they appear in the "requester" UI.
	 * 
	 * @return A handle for working with the parameters and parameter groups.
	 */

	public SlotHandle getParameters( )
	{
		return getSlot( ReportDesign.PARAMETER_SLOT );
	}

	/**
	 * Returns a slot handle to work with the scratched elements within the
	 * report, which are no longer needed or are in the process of rearranged.
	 * 
	 * @return A handle for working with the scratched elements.
	 */

	public SlotHandle getScratchPad( )
	{
		return getSlot( ReportDesign.SCRATCH_PAD_SLOT );
	}

	/**
	 * Returns a slot handle to work with the top-level components within the
	 * report.
	 * 
	 * @return A handle for working with the components.
	 */

	public SlotHandle getComponents( )
	{
		return getSlot( ReportDesign.COMPONENT_SLOT );
	}

	/**
	 * Returns the flatten Parameters/ParameterGroups of the design. This method
	 * put all Parameters and ParameterGroups into a list then return it. The
	 * return list is sorted by on the display name of the parameters.
	 * 
	 * @return the sorted, flatten parameters and parameter groups.
	 */

	public List getFlattenParameters( )
	{
		ArrayList list = new ArrayList( );
		SlotHandle slotHandle = getParameters( );
		Iterator it = slotHandle.iterator( );
		while ( it.hasNext( ) )
		{
			DesignElementHandle h = (DesignElementHandle) it.next( );
			list.add( h );
			if ( h instanceof ParameterGroupHandle )
			{
				addParameters( list, (ParameterGroupHandle) h );
			}
		}
		DesignElementHandle.doSort( list );
		return list;
	}

	/**
	 * Adds all the parameters under the given parameter group to a list.
	 * 
	 * @param list
	 *            the list to which the parameters are added.
	 * @param handle
	 *            the handle to the parameter group.
	 */

	private void addParameters( ArrayList list, ParameterGroupHandle handle )
	{
		SlotHandle h = handle.getParameters( );
		Iterator it = h.iterator( );
		while ( it.hasNext( ) )
		{
			list.add( it.next( ) );
		}
	}

	/**
	 * Returns the name of the author of the design report.
	 * 
	 * @return the name of the author.
	 */

	public String getAuthor( )
	{
		return getStringProperty( ReportDesign.AUTHOR_PROP );
	}

	/**
	 * Sets the name of the author of the design report.
	 * 
	 * @param author
	 *            the name of the author.
	 */

	public void setAuthor( String author )
	{
		try
		{
			setStringProperty( ReportDesign.AUTHOR_PROP, author );
		}
		catch ( SemanticException e )
		{
			assert false;
		}

	}

	/**
	 * Returns the default units for the design. These are the units that are
	 * used for dimensions that don't explicitly specify units.
	 * 
	 * @return the default units for the design.
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 * @deprecated
	 */

	public String getDefaultUnits( )
	{
		return design.getUnits( );
	}

	/**
	 * Sets the default units for the design. These are the units that are used
	 * for dimensions that don't explicitly specify units.
	 * <p>
	 * 
	 * For a report design, it allows the following constants that defined in
	 * <code>{@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}</code>:
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * </ul>
	 * 
	 * @param units
	 *            the default units for the design.
	 * @throws SemanticException
	 *             if the input unit is not one of allowed.
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 * @deprecated
	 */

	public void setDefaultUnits( String units ) throws SemanticException
	{
		setStringProperty( ReportDesign.UNITS_PROP, units );
	}

	/**
	 * Returns the file name of the design. This is the name of the file from
	 * which the design was read, or the name to which the design was last
	 * written.
	 * 
	 * @return the file name
	 */

	public String getFileName( )
	{
		return design.getFileName( );
	}

	/**
	 * Sets the design file name.
	 * 
	 * @param newName
	 *            the new file name
	 */

	public void setFileName( String newName )
	{
		design.setFileName( newName );
	}

	/**
	 * Returns the base directory to use when computing relative links from this
	 * report. Especially used for searching images, library and so.
	 * 
	 * @return the base directory
	 */

	public String getBase( )
	{
		return design.getStringProperty( design, ReportDesign.BASE_PROP );
	}

	/**
	 * Sets the base directory to use when computing relative links from this
	 * report. Especially used for searching images, library and so.
	 * 
	 * @param base
	 *            the base directory to set
	 */

	public void setBase( String base )
	{
		try
		{
			setProperty( ReportDesign.BASE_PROP, base );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns an external file that provides help information for the report.
	 * 
	 * @return the name of an external file
	 */

	public String getHelpGuide( )
	{
		return getStringProperty( ReportDesign.HELP_GUIDE_PROP );
	}

	/**
	 * Sets an external file that provides help information for the report.
	 * 
	 * @param helpGuide
	 *            the name of an external file
	 */

	public void setHelpGuide( String helpGuide )
	{
		try
		{
			setStringProperty( ReportDesign.HELP_GUIDE_PROP, helpGuide );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the name of the tool that created the design.
	 * 
	 * @return the name of the tool
	 */

	public String getCreatedBy( )
	{
		return getStringProperty( ReportDesign.CREATED_BY_PROP );
	}

	/**
	 * Returns the name of the tool that created the design.
	 * 
	 * @param toolName
	 *            the name of the tool
	 */

	public void setCreatedBy( String toolName )
	{
		try
		{
			setStringProperty( ReportDesign.CREATED_BY_PROP, toolName );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the refresh rate when viewing the report.
	 * 
	 * @return the refresh rate
	 */

	public int getRefreshRate( )
	{
		return getIntProperty( ReportDesign.REFRESH_RATE_PROP );
	}

	/**
	 * Sets the refresh rate when viewing the report.
	 * 
	 * @param rate
	 *            the refresh rate
	 */

	public void setRefreshRate( int rate )
	{
		try
		{
			setIntProperty( ReportDesign.REFRESH_RATE_PROP, rate );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Gets a handle to deal with a translation. A translation is identified by
	 * its resourceKey and locale.
	 * 
	 * @param resourceKey
	 *            the resource key
	 * @param locale
	 *            the locale information
	 * 
	 * @return corresponding <code>TranslationHandle</code>. Or return
	 *         <code>null</code> if the translation is not found in the
	 *         design.
	 * 
	 * @see TranslationHandle
	 */

	public TranslationHandle getTranslation( String resourceKey, String locale )
	{
		Translation translation = design.findTranslation( resourceKey, locale );

		if ( translation != null )
			return translation.handle( getDesign( ) );

		return null;
	}

	/**
	 * Gets a list of translation defined on the report. The content of the list
	 * is the corresponding <code>TranslationHandle</code>.
	 * 
	 * @return a list containing TranslationHandles defined on the report or
	 *         <code>null</code> if the design has no any translations.
	 * 
	 * @see TranslationHandle
	 */

	public List getTranslations( )
	{
		List translations = getDesign( ).getTranslations( );

		if ( translations == null )
			return null;

		List translationHandles = new ArrayList( );

		for ( int i = 0; i < translations.size( ); i++ )
		{
			translationHandles.add( ( (Translation) translations.get( i ) )
					.handle( getDesign( ) ) );
		}

		return translationHandles;
	}

	/**
	 * Returns a string array containing all the resource keys of user-defined
	 * translations for the report.
	 * 
	 * @return a string array containing message resource keys, return
	 *         <code>null</code> if there is no messages defined in the
	 *         design.
	 */

	public String[] getTranslationKeys( )
	{
		return getDesign( ).getTranslationResourceKeys( );
	}

	/**
	 * Adds a new translation to the design.
	 * 
	 * @param resourceKey
	 *            resource key for the message
	 * @param locale
	 *            the string value of a locale for the translation. Locale
	 *            should be in java-defined format( en, en-US, zh_CN, etc.)
	 * @param text
	 *            translated text for the locale
	 * 
	 * @throws CustomMsgException
	 *             if the resource key is duplicate or missing, or locale is not
	 *             a valid format.
	 * 
	 * @see #getTranslation(String, String)
	 */

	public void addTranslation( String resourceKey, String locale, String text )
			throws CustomMsgException
	{
		CustomMsgCommand command = new CustomMsgCommand( getDesign( ) );
		command.addTranslation( resourceKey, locale, text );
	}

	/**
	 * Drops a translation from the design.
	 * 
	 * @param resourceKey
	 *            resource key of the message in which this translation saves.
	 * @param locale
	 *            the string value of the locale for a translation. Locale
	 *            should be in java-defined format( en, en-US, zh_CN, etc.)
	 * @throws CustomMsgException
	 *             if <code>resourceKey</code> is <code>null</code>.
	 * @see #getTranslation(String, String)
	 */

	public void dropTranslation( String resourceKey, String locale )
			throws CustomMsgException
	{
		CustomMsgCommand command = new CustomMsgCommand( getDesign( ) );
		command.dropTranslation( resourceKey, locale );
	}

	/**
	 * Returns the iterator over all structures of color palette. Each one is
	 * the instance of <code>CustomColorHandle</code>
	 * 
	 * @return the iterator over all structures of color palette.
	 * @see CustomColorHandle
	 */

	public Iterator customColorsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ReportDesign.COLOR_PALETTE_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the iterator over all config variables. Each one is the instance
	 * of <code>ConfigVariableHandle</code>
	 * 
	 * @return the iterator over all config variables.
	 * @see ConfigVariableHandle
	 */

	public Iterator configVariablesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ReportDesign.CONFIG_VARS_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Adds a new config variable.
	 * 
	 * @param configVar
	 *            the config variable
	 * @throws SemanticException
	 *             if the name is empty or the same name exists.
	 *  
	 */

	public void addConfigVariable( ConfigVariable configVar )
			throws SemanticException
	{
		ElementPropertyDefn propDefn = design
				.getPropertyDefn( ReportDesign.CONFIG_VARS_PROP );

		if ( configVar != null && StringUtil.isBlank( configVar.getName( ) ) )
		{
			throw new PropertyValueException( getElement( ), propDefn,
					configVar,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE );
		}

		if ( configVar != null
				&& findConfigVariable( configVar.getName( ) ) != null )
		{
			throw new PropertyValueException( getElement( ), propDefn,
					configVar.getName( ),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS );
		}

		PropertyCommand cmd = new PropertyCommand( design, getElement( ) );
		cmd.addItem( new MemberRef( propDefn ), configVar );
	}

	/**
	 * Finds the position of the config variable with the given name.
	 * 
	 * @param name
	 *            the config variable name
	 * @return the index ( from 0 ) of config variable with the given name.
	 *         Return -1, if not found.
	 *  
	 */

	private int findConfigVariablePos( String name )
	{
		List configVars = (List) design.getLocalProperty( design,
				ReportDesign.CONFIG_VARS_PROP );
		if ( configVars == null )
			return -1;

		int i = 0;
		for ( Iterator iter = configVars.iterator( ); iter.hasNext( ); i++ )
		{
			ConfigVariable var = (ConfigVariable) iter.next( );

			if ( var.getName( ).equals( name ) )
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Drops a config variable.
	 * 
	 * @param name
	 *            config variable name
	 * @throws SemanticException
	 *             if no config variable is found.
	 *  
	 */

	public void dropConfigVariable( String name ) throws SemanticException
	{
		PropertyHandle propHandle = this
				.getPropertyHandle( ReportDesign.CONFIG_VARS_PROP );

		int posn = findConfigVariablePos( name );
		if ( posn < 0 )
			throw new PropertyValueException( getElement( ),
					(ElementPropertyDefn) propHandle.getPropertyDefn( ), name,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		propHandle.removeItem( posn );

	}

	/**
	 * Finds the config variable with the given name.
	 * 
	 * @param name
	 *            config variable name
	 * @return config variable with the specified name. Return <code>null</code>,
	 *         if not found.
	 *  
	 */

	public ConfigVariable findConfigVariable( String name )
	{
		int pos = findConfigVariablePos( name );
		if ( pos == -1 )
			return null;

		List configVars = (List) design.getLocalProperty( design,
				ReportDesign.CONFIG_VARS_PROP );

		return (ConfigVariable) configVars.get( pos );
	}

	/**
	 * Replaces the old config variable with the new one.
	 * 
	 * @param oldVar
	 *            the old config variable
	 * @param newVar
	 *            the new config variable
	 * @throws SemanticException
	 *             if the old config variable is not found or the name of new
	 *             one is empty.
	 *  
	 */

	public void replaceConfigVariable( ConfigVariable oldVar,
			ConfigVariable newVar ) throws SemanticException
	{
		replaceObjectInList( ReportDesign.CONFIG_VARS_PROP, oldVar, newVar );
	}

	/**
	 * Returns the iterator over all embedded images. Each one is the instance
	 * of <code>EmbeddedImageHandle</code>
	 * 
	 * @return the iterator over all embedded images.
	 * 
	 * @see EmbeddedImageHandle
	 */

	public Iterator imagesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ReportDesign.IMAGES_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Adds a new embedded image.
	 * 
	 * @param image
	 *            the image to add
	 * @throws SemanticException
	 *             if the name is empty, type is invalid, or the same name
	 *             exists.
	 */

	public void addImage( EmbeddedImage image ) throws SemanticException
	{
		PropertyCommand cmd = new PropertyCommand( design, getElement( ) );
		ElementPropertyDefn propDefn = design
				.getPropertyDefn( ReportDesign.IMAGES_PROP );
		cmd.addItem( new MemberRef( propDefn ), image );
	}

	/**
	 * Finds the position of the image with the given name.
	 * 
	 * @param name
	 *            the image name to find
	 * @return position of image with the specified name. Return -1, if not
	 *         found.
	 */

	private int findImagePos( String name )
	{
		List images = (List) design.getLocalProperty( design,
				ReportDesign.IMAGES_PROP );

		int i = 0;
		for ( Iterator iter = images.iterator( ); iter.hasNext( ); i++ )
		{
			EmbeddedImage image = (EmbeddedImage) iter.next( );

			if ( image.getName( ) != null
					&& image.getName( ).equalsIgnoreCase( name ) )
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Drops an embedded image from the design.
	 * 
	 * @param name
	 *            the image name
	 * @throws SemanticException
	 *             if the image is not found.
	 */

	public void dropImage( String name ) throws SemanticException
	{
		PropertyHandle propHandle = this
				.getPropertyHandle( ReportDesign.IMAGES_PROP );

		int pos = findImagePos( name );
		if ( pos < 0 )
			throw new PropertyValueException( getElement( ),
					(ElementPropertyDefn) propHandle.getPropertyDefn( ), name,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		propHandle.removeItem( pos );
	}

	/**
	 * Finds the image with the given name.
	 * 
	 * @param name
	 *            the image name
	 * @return embedded image with the given name. Return <code>null</code>,
	 *         if not found.
	 */

	public EmbeddedImage findImage( String name )
	{
		return design.findImage( name );
	}

	/**
	 * Replaces the old embedded image with the new one.
	 * 
	 * @param oldVar
	 *            the old embedded image
	 * @param newVar
	 *            the new embedded image
	 * @throws SemanticException
	 *             if the old image is not found or the name of new one is
	 *             empty.
	 */

	public void replaceImage( EmbeddedImage oldVar, EmbeddedImage newVar )
			throws SemanticException
	{
		replaceObjectInList( ReportDesign.IMAGES_PROP, oldVar, newVar );
	}

	/**
	 * Replaces an old object in the structure list with the given new one.
	 * 
	 * @param propName
	 *            the name of the property that holds a structure list
	 * @param oldVar
	 *            an existed object in the list
	 * @param newVar
	 *            a new object
	 * @throws SemanticException
	 *             if the old object is not found or the name of new one is
	 *             empty.
	 */

	private void replaceObjectInList( String propName, Object oldVar,
			Object newVar ) throws SemanticException
	{
		ElementPropertyDefn propDefn = design.getPropertyDefn( propName );

		PropertyCommand cmd = new PropertyCommand( design, getElement( ) );
		cmd.replaceItem( new MemberRef( propDefn ), (Structure) oldVar,
				(Structure) newVar );
	}

	/**
	 * Determines if the design has changed since it was last read from, or
	 * written to, the file. The dirty state reflects the action of the command
	 * stack. If the user saves the design and then changes it, the design is
	 * dirty. If the user then undoes the change, the design is no longer dirty.
	 * 
	 * @return <code>true</code> if the design has changed since the last load
	 *         or save; <code>false</code> if it has not changed.
	 */

	public boolean needsSave( )
	{
		return design.isDirty( );
	}

	/**
	 * Closes the design. The report design handle is no longer valid after
	 * closing the design.
	 */

	public void close( )
	{
		design.close( );
	}

	/**
	 * Writes the report design to the given output stream. The caller must call
	 * <code>onSave</code> if the save succeeds.
	 * 
	 * @param out
	 *            the output stream to which the design is written.
	 * @throws IOException
	 *             if the file cannot be written to the output stream
	 *             successfully.
	 */

	public void serialize( OutputStream out ) throws IOException
	{
		assert out != null;

		design.prepareToSave( );
		DesignWriter writer = new DesignWriter( design );
		writer.write( out );
		design.onSave( );
	}

	/**
	 * Calls to inform a save is successful. Must be called after a successful
	 * completion of a save done using <code>serialize</code>.
	 */

	public void onSave( )
	{
		design.onSave( );
	}

	/**
	 * Returns a list containing errors during parsing the design file.
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * 
	 * @see ErrorDetail
	 */

	public List getErrorList( )
	{
		return design.getErrorList( );
	}

	/**
	 * Returns a list containing warnings during parsing the design file.
	 * 
	 * @return a list containing parsing warnings. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * 
	 * @see ErrorDetail
	 */

	public List getWarningList( )
	{
		return design.getWarningList( );
	}

	/**
	 * Returns the iterator over all included libraries. Each one is the
	 * instance of <code>IncludeLibraryHandle</code>
	 * 
	 * @return the iterator over all included libraries.
	 * @see IncludeLibraryHandle
	 */

	public Iterator includeLibrariesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ReportDesign.INCLUDE_LIBRARIES );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the iterator over all included scripts. Each one is the instance
	 * of <code>IncludeScriptHandle</code>
	 * 
	 * @return the iterator over all included scripts.
	 * @see IncludeScriptHandle
	 */

	public Iterator includeScriptsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ReportDesign.INCLUDE_SCRIPTS );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Sets the script called when the report starts executing.
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setInitialize( String value )
	{
		try
		{
			setStringProperty( ReportDesign.INITIALIZE_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called when the report starts executing.
	 * 
	 * @return the script called when the report starts executing
	 */

	public String getInitialize( )
	{
		return getStringProperty( ReportDesign.INITIALIZE_METHOD );
	}

	/**
	 * Sets the script called at the start of the Factory after the initialize( )
	 * method and before opening the report document (if any).
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setBeforeFactory( String value )
	{
		try
		{
			setStringProperty( ReportDesign.BEFORE_FACTORY_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called at the start of the Factory after the
	 * initialize( ) method and before opening the report document (if any).
	 * 
	 * @return the script
	 */

	public String getBeforeFactory( )
	{
		return getStringProperty( ReportDesign.BEFORE_FACTORY_METHOD );
	}

	/**
	 * Sets the script called at the end of the Factory after closing the report
	 * document (if any). This is the last method called in the Factory.
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setAfterFactory( String value )
	{
		try
		{
			setStringProperty( ReportDesign.AFTER_FACTORY_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called at the end of the Factory after closing the
	 * report document (if any). This is the last method called in the Factory.
	 * 
	 * @return the script
	 */

	public String getAfterFactory( )
	{
		return getStringProperty( ReportDesign.AFTER_FACTORY_METHOD );
	}

	/**
	 * Sets the script called just before opening the report document in the
	 * Factory.
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setBeforeOpenDoc( String value )
	{
		try
		{
			setStringProperty( ReportDesign.BEFORE_OPEN_DOC_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called just before opening the report document in the
	 * Factory.
	 * 
	 * @return the script
	 */

	public String getBeforeOpenDoc( )
	{
		return getStringProperty( ReportDesign.BEFORE_OPEN_DOC_METHOD );
	}

	/**
	 * Sets the script called just after opening the report document in the
	 * Factory.
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setAfterOpenDoc( String value )
	{
		try
		{
			setStringProperty( ReportDesign.AFTER_OPEN_DOC_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called just after opening the report document in the
	 * Factory.
	 * 
	 * @return the script
	 */

	public String getAfterOpenDoc( )
	{
		return getStringProperty( ReportDesign.AFTER_OPEN_DOC_METHOD );
	}

	/**
	 * Sets the script called just before closing the report document file in
	 * the Factory.
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setBeforeCloseDoc( String value )
	{
		try
		{
			setStringProperty( ReportDesign.BEFORE_CLOSE_DOC_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called just before closing the report document file in
	 * the Factory.
	 * 
	 * @return the script
	 */

	public String getBeforeCloseDoc( )
	{
		return getStringProperty( ReportDesign.BEFORE_CLOSE_DOC_METHOD );
	}

	/**
	 * Sets the script called just after closing the report document file in the
	 * Factory.
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setAfterCloseDoc( String value )
	{
		try
		{
			setStringProperty( ReportDesign.AFTER_CLOSE_DOC_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called just after closing the report document file in
	 * the Factory.
	 * 
	 * @return the script
	 */

	public String getAfterCloseDoc( )
	{
		return getStringProperty( ReportDesign.AFTER_CLOSE_DOC_METHOD );
	}

	/**
	 * Sets the script called before starting a presentation time action.
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setBeforeRender( String value )
	{
		try
		{
			setStringProperty( ReportDesign.BEFORE_RENDER_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called before starting a presentation time action.
	 * 
	 * @return the script
	 */

	public String getBeforeRender( )
	{
		return getStringProperty( ReportDesign.BEFORE_RENDER_METHOD );
	}

	/**
	 * Sets the script called after starting a presentation time action.
	 * 
	 * @param value
	 *            the script to set.
	 */

	public void setAfterRender( String value )
	{
		try
		{
			setStringProperty( ReportDesign.AFTER_RENDER_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the script called after starting a presentation time action.
	 * 
	 * @return the script
	 */

	public String getAfterRender( )
	{
		return getStringProperty( ReportDesign.AFTER_RENDER_METHOD );
	}

	/**
	 * Checks the element name in name space of this report.
	 * 
	 * <ul>
	 * <li>If the element name is required and duplicate name is found in name
	 * space, rename the element with a new unique name.
	 * <li>If the element name is not required, clear the name.
	 * </ul>
	 * 
	 * @param elementHandle
	 *            the element handle whose name is need to check.
	 */

	public void rename( DesignElementHandle elementHandle )
	{
		if ( elementHandle == null )
			return;

		IElementDefn defn = elementHandle.getElement( ).getDefn( );

		if ( defn.getNameOption( ) == MetaDataConstants.REQUIRED_NAME )
			design.makeUniqueName( elementHandle.getElement( ) );
		else
			elementHandle.getElement( ).setName( null );

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = elementHandle.getElement( ).getSlot( i );

			if ( slot != null )
			{
				for ( int pos = 0; pos < slot.getCount( ); pos++ )
				{
					DesignElement innerElement = slot.getContent( pos );
					rename( innerElement.getHandle( design ) );
				}
			}
		}
	}

	/**
	 * Checks this whole report.
	 *  
	 */

	public void checkReport( )
	{
		// validate the whole design

		design.semanticCheck( design );
	}

	/**
	 * Set the base name of the customer-defined resource bundle. The name is a
	 * common base name, e.g: "myMessage" without the Language_Country suffix,
	 * then the message file family can be "myMessage_en.properties",
	 * "myMessage_zh_CN.properties" etc. The message file is stored in the same
	 * folder as the design file.
	 * 
	 * @param baseName
	 *            common base name of the customer-defined resource bundle.
	 *  
	 */

	public void setMessageBaseName( String baseName )
	{
		try
		{
			setProperty( ReportDesign.MSG_BASE_NAME_PROP, baseName );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Get the base name of the customer-defined resource bundle.
	 * 
	 * @return the base name of the customer-defined resource bundle.
	 */

	public String getMessageBaseName( )
	{
		return getStringProperty( ReportDesign.MSG_BASE_NAME_PROP );
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
	 *         <code>null</code> if resoueceKey is blank.
	 */

	public String getMessage( String resourceKey, Locale locale )
	{
		return getDesign( ).getMessage( resourceKey, locale );
	}

	/**
	 * Finds user-defined messages for the current thread's locale.
	 * 
	 * @param resourceKey
	 *            Resource key of the user-defined message.
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>null</code> if resoueceKey is blank.
	 * @see #getMessage(String, Locale)
	 */

	public String getMessage( String resourceKey )
	{
		return getDesign( ).getMessage( resourceKey );
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
		return getDesign( ).getMessageKeys( );
	}
}