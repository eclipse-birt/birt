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
import java.net.URL;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.command.LibraryCommand;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
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

public class ReportDesignHandle extends ModuleHandle
		implements
			IReportDesignModel
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

	/**
	 * Adds one library with the given library file name. The new library will
	 * be appended to the library list. 
	 * 
	 * 
	 * @param libraryFileName
	 *            library file name
	 * @param namespace
	 *            library namespace
	 * @throws DesignFileException
	 *             if the library file is not found, or has fatal error.
	 * @throws SemanticException
	 */

	public void addLibrary( String libraryFileName, String namespace )
			throws DesignFileException, SemanticException
	{
		LibraryCommand command = new LibraryCommand( module );
		command.addLibrary( libraryFileName, namespace );
	}

	/**
	 * 
	 * @param library
	 * @throws SemanticException
	 */
	
	public void dropLibrary( LibraryHandle library ) throws SemanticException
	{
		LibraryCommand command = new LibraryCommand( module );
		command.dropLibrary( (Library) library.getElement( ) );
	}
	
	/**
	 * Returns the script called just after closing the report document file in
	 * the Factory.
	 * 
	 * @return the script
	 */

	public String getAfterCloseDoc( )
	{
		return getStringProperty( AFTER_CLOSE_DOC_METHOD );
	}

	/**
	 * Returns the script called at the end of the Factory after closing the
	 * report document (if any). This is the last method called in the Factory.
	 * 
	 * @return the script
	 */

	public String getAfterFactory( )
	{
		return getStringProperty( AFTER_FACTORY_METHOD );
	}

	/**
	 * Returns the script called just after opening the report document in the
	 * Factory.
	 * 
	 * @return the script
	 */

	public String getAfterOpenDoc( )
	{
		return getStringProperty( AFTER_OPEN_DOC_METHOD );
	}

	/**
	 * Returns the script called after starting a presentation time action.
	 * 
	 * @return the script
	 */

	public String getAfterRender( )
	{
		return getStringProperty( AFTER_RENDER_METHOD );
	}

	/**
	 * Returns the base directory to use when computing relative links from this
	 * report. Especially used for searching images, library and so.
	 * 
	 * @return the base directory
	 */

	public String getBase( )
	{
		return module.getStringProperty( module, BASE_PROP );
	}

	/**
	 * Returns the script called just before closing the report document file in
	 * the Factory.
	 * 
	 * @return the script
	 */

	public String getBeforeCloseDoc( )
	{
		return getStringProperty( BEFORE_CLOSE_DOC_METHOD );
	}

	/**
	 * Returns the script called at the start of the Factory after the
	 * initialize( ) method and before opening the report document (if any).
	 * 
	 * @return the script
	 */

	public String getBeforeFactory( )
	{
		return getStringProperty( BEFORE_FACTORY_METHOD );
	}

	/**
	 * Returns the script called just before opening the report document in the
	 * Factory.
	 * 
	 * @return the script
	 */

	public String getBeforeOpenDoc( )
	{
		return getStringProperty( BEFORE_OPEN_DOC_METHOD );
	}

	/**
	 * Returns the script called before starting a presentation time action.
	 * 
	 * @return the script
	 */

	public String getBeforeRender( )
	{
		return getStringProperty( BEFORE_RENDER_METHOD );
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
		return getSlot( BODY_SLOT );
	}

	/**
	 * Get the base name of the customer-defined resource bundle.
	 * 
	 * @return the base name of the customer-defined resource bundle.
	 */

	public String getIncludeResource( )
	{
		return getStringProperty( INCLUDE_RESOURCE_PROP );
	}

	/**
	 * Returns the refresh rate when viewing the report.
	 * 
	 * @return the refresh rate
	 */

	public int getRefreshRate( )
	{
		return getIntProperty( REFRESH_RATE_PROP );
	}

	/**
	 * Returns a slot handle to work with the scratched elements within the
	 * report, which are no longer needed or are in the process of rearranged.
	 * 
	 * @return A handle for working with the scratched elements.
	 */

	public SlotHandle getScratchPad( )
	{
		return getSlot( SCRATCH_PAD_SLOT );
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
		PropertyHandle propHandle = getPropertyHandle( INCLUDE_LIBRARIES_PROP );
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
		PropertyHandle propHandle = getPropertyHandle( INCLUDE_SCRIPTS_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
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
			setStringProperty( AFTER_CLOSE_DOC_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setStringProperty( AFTER_FACTORY_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setStringProperty( AFTER_OPEN_DOC_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setStringProperty( AFTER_RENDER_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setProperty( BASE_PROP, base );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setStringProperty( BEFORE_CLOSE_DOC_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setStringProperty( BEFORE_FACTORY_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setStringProperty( BEFORE_OPEN_DOC_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setStringProperty( BEFORE_RENDER_METHOD, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
	 */

	public void setDefaultUnits( String units ) throws SemanticException
	{
		setStringProperty( UNITS_PROP, units );
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

	public void setIncludeResource( String baseName )
	{
		try
		{
			setProperty( INCLUDE_RESOURCE_PROP, baseName );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
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
			setIntProperty( REFRESH_RATE_PROP, rate );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#save()
	 */

	public void save( ) throws IOException
	{
		String fileName = getFileName( );
		assert fileName != null;
		if ( fileName == null )
			return;
		module.prepareToSave( );
		DesignWriter writer = new DesignWriter( (ReportDesign) module );
		writer.write( new File( fileName ) );
		module.onSave( );
	}

	/**
	 * Returns the <code>URL</code> object if the file with
	 * <code>fileName</code> exists. This method takes the following search
	 * steps:
	 * <ul>
	 * <li>Search file taking <code>fileName</code> as absolute file name;
	 * <li>Search file taking <code>fileName</code> as relative file name and
	 * basing "base" property of report design;
	 * <li>Search file with the file locator (<code>IResourceLocator</code>)
	 * in session.
	 * </ul>
	 * 
	 * @param fileName
	 *            file name to search
	 * @param fileType
	 *            file type. The value should be one of:
	 *            <ul>
	 *            <li><code>IResourceLocator.IMAGE</code>
	 *            <li><code>IResourceLocator.LIBRARY</code>
	 *            </ul>
	 *            Any invalid value will be treated as
	 *            <code>IResourceLocator.IMAGE</code>.
	 * @return the <code>URL</code> object if the file with
	 *         <code>fileName</code> is found, or null otherwise.
	 */

	public URL findResource( String fileName, int fileType )
	{
		return module.findResource( fileName, fileType );
	}

}