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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;

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
 * <p>
 * Module allow to use the components defined in <code>Library</code>.
 * <ul>
 * <li> User can call {@link #includeLibrary(String, String)}to include one
 * library.
 * <li> User can create one report item based on the one in library, and add it
 * into design file.
 * <li> User can use style, data source, and data set, which are defined in
 * library, in design file.
 * </ul>
 * 
 * <pre>
 *                                          // Include one library
 *                                          
 *                                          ReportDesignHandle designHandle = ...;
 *                                          designHandle.includeLibrary( &quot;libA.rptlibrary&quot;, &quot;LibA&quot; );
 *                                          LibraryHandle libraryHandle = designHandle.getLibrary(&quot;LibA&quot;);
 *                                           
 *                                          // Create one label based on the one in library
 *                                         
 *                                          LabelHandle labelHandle = (LabelHandle) libraryHandle.findElement(&quot;companyNameLabel&quot;);
 *                                          LabelHandle myLabelHandle = (LabelHandle) designHandle.getElementFactory().newElementFrom( labelHandle, &quot;myLabel&quot; );
 *                                         
 *                                          // Add the new label into design file
 *                                         
 *                                          designHandle.getBody().add(myLabelHandle);
 *                                       
 * </pre>
 * 
 * @see org.eclipse.birt.report.model.elements.ReportDesign
 */

public class ReportDesignHandle extends ModuleHandle
		implements
			IReportDesignModel
{

	boolean isInitialized = false;

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
	 * Returns the script called just after closing the report document file in
	 * the Factory.
	 * 
	 * @return the script
	 * @deprecated
	 */

	public String getAfterCloseDoc( )
	{
		return ""; //$NON-NLS-1$
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
	 * @deprecated
	 */

	public String getAfterOpenDoc( )
	{
		return ""; //$NON-NLS-1$
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
	 * @deprecated
	 */

	public String getBeforeCloseDoc( )
	{
		return ""; //$NON-NLS-1$
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
	 * @deprecated
	 */

	public String getBeforeOpenDoc( )
	{
		return ""; //$NON-NLS-1$
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
	 * Returns the iterator over all data source bindings. Each one is the
	 * instance of <code>DataSourceParamBindingHandle</code>
	 * 
	 * @return the iterator over all data source bindings.
	 * @see DataSourceParamBindingHandle
	 */

	public Iterator dataSourceBindingsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( DATA_SOURCE_BINDINGS_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Sets the script called just after closing the report document file in the
	 * Factory.
	 * 
	 * @param value
	 *            the script to set.
	 * @deprecated
	 */

	public void setAfterCloseDoc( String value )
	{
		// try
		// {
		// setStringProperty( AFTER_CLOSE_DOC_METHOD, value );
		// }
		// catch ( SemanticException e )
		// {
		// assert false;
		// }
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
	 * @deprecated
	 */

	public void setAfterOpenDoc( String value )
	{
		// try
		// {
		// setStringProperty( AFTER_OPEN_DOC_METHOD, value );
		// }
		// catch ( SemanticException e )
		// {
		// assert false;
		// }
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
	 * @deprecated
	 */

	public void setBeforeCloseDoc( String value )
	{
		// try
		// {
		// setStringProperty( BEFORE_CLOSE_DOC_METHOD, value );
		// }
		// catch ( SemanticException e )
		// {
		// assert false;
		// }
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
	 * @deprecated
	 */

	public void setBeforeOpenDoc( String value )
	{
		// try
		// {
		// setStringProperty( BEFORE_OPEN_DOC_METHOD, value );
		// }
		// catch ( SemanticException e )
		// {
		// assert false;
		// }
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

	/**
	 * Returns a slot handle to work with the styles within the report. Note
	 * that the order of the styles within the slot is unimportant.
	 * 
	 * @return A handle for working with the styles.
	 */
	public SlotHandle getStyles( )
	{
		return getSlot( IReportDesignModel.STYLE_SLOT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#importCssStyles(org.eclipse.birt.report.model.api.css.CssStyleSheetHandle,
	 *      java.util.List)
	 */

	public void importCssStyles( CssStyleSheetHandle stylesheet,
			List selectedStyles )
	{
		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( );
		for ( int i = 0; i < selectedStyles.size( ); i++ )
		{
			SharedStyleHandle style = (SharedStyleHandle) selectedStyles
					.get( i );
			if ( stylesheet.findStyle( style.getName( ) ) != null )
			{
				try
				{
					module.makeUniqueName( style.getElement( ) );
					addElement( style, IReportDesignModel.STYLE_SLOT );
				}
				catch ( ContentException e )
				{
					assert false;
				}
				catch ( NameException e )
				{
					assert false;
				}
			}
		}

		stack.commit( );
	}

	/**
	 * Sets the resource key of the display name.
	 * 
	 * @param displayNameKey
	 *            the resource key of the display name
	 * @throws SemanticException
	 *             if the display name resource-key property is locked or not
	 *             defined on this design.
	 */

	public void setDisplayNameKey( String displayNameKey )
			throws SemanticException
	{
		setStringProperty( DesignElement.DISPLAY_NAME_ID_PROP, displayNameKey );
	}

	/**
	 * Gets the resource key of the display name.
	 * 
	 * @return the resource key of the display name
	 */

	public String getDisplayNameKey( )
	{
		return getStringProperty( DesignElement.DISPLAY_NAME_ID_PROP );
	}

	/**
	 * Sets the display name.
	 * 
	 * @param displayName
	 *            the display name
	 * @throws SemanticException
	 *             if the display name property is locked or not defined on this
	 *             design.
	 */

	public void setDisplayName( String displayName ) throws SemanticException
	{
		setStringProperty( DesignElement.DISPLAY_NAME_PROP, displayName );
	}

	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */

	public String getDisplayName( )
	{
		return getStringProperty( DesignElement.DISPLAY_NAME_PROP );
	}

	/**
	 * Sets the design icon/thumbnail file path.
	 * 
	 * @param iconFile
	 *            the design icon/thumbnail file path to set
	 * @throws SemanticException
	 *             if the property is locked or not defined on this design.
	 */

	public void setIconFile( String iconFile ) throws SemanticException
	{
		setStringProperty( ReportDesign.ICON_FILE_PROP, iconFile );
	}

	/**
	 * Gets the design icon/thumbnail file path.
	 * 
	 * @return the design icon/thumbnail file path
	 */

	public String getIconFile( )
	{
		return getStringProperty( ReportDesign.ICON_FILE_PROP );
	}

	/**
	 * Sets the design cheat sheet file path.
	 * 
	 * @param cheatSheet
	 *            the design cheat sheet file path to set
	 * @throws SemanticException
	 *             if the property is locked or not defined on this design.
	 * 
	 * @deprecated by {@link #setCheatSheet(String)}
	 */

	public void setCheetSheet( String cheatSheet ) throws SemanticException
	{
		setCheatSheet( cheatSheet );
	}

	/**
	 * Sets the design cheat sheet file path.
	 * 
	 * @param cheatSheet
	 *            the design cheat sheet file path to set
	 * @throws SemanticException
	 *             if the property is locked or not defined on this design.
	 */

	public void setCheatSheet( String cheatSheet ) throws SemanticException
	{
		setStringProperty( ReportDesign.CHEAT_SHEET_PROP, cheatSheet );
	}

	/**
	 * Gets the design cheat sheet file path.
	 * 
	 * @return the design cheat sheet file path
	 * 
	 * @deprecated by {@link #getCheatSheet()}
	 */

	public String getCheetSheet( )
	{
		return getCheatSheet( );
	}

	/**
	 * Gets the design cheat sheet file path.
	 * 
	 * @return the design cheat sheet file path
	 */

	public String getCheatSheet( )
	{
		return getStringProperty( ReportDesign.CHEAT_SHEET_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getAllBookmarks()
	 */
	public List getAllBookmarks( )
	{
		return module.getBookmarksFrom( BODY_SLOT );
	}

	/**
	 * Initializes the report design when it is just created.
	 * <li> Check if the reprot has a master page defined, if no, add one. </li>
	 * <li> Set the value to the properties on repot design element which need
	 * the initialize valuel. </li>
	 * 
	 * All initialize operations will not go into the command stack and can not
	 * be undo redo.
	 * 
	 * @param properties
	 *            the property name value pairs.Those properties in the map are
	 *            which need to be initialized.
	 * @throws SemanticException
	 *             SemamticException will throw out when the give properties map
	 *             contians invlid property name or property value.
	 */
	public void initializeReportDesign( Map properties )
			throws SemanticException
	{
		// if this report deisgn has been initialized, return.
		if ( isInitialized )
			return;

		String name = null;
		Object value = null;
		ReportDesign design = (ReportDesign) getElement( );
		Set propNames = properties.keySet( );

		// check whether this report has a masterPage, if no, create one.
		if ( getMasterPages( ).getCount( ) == 0 )
		{
			MasterPageHandle page = getElementFactory( ).newSimpleMasterPage(
					"Simple MasterPage" );
			TextItemHandle text = getElementFactory( ).newTextItem( null );

			( (TextItem) text.getElement( ) ).setProperty(
					TextItem.CONTENT_TYPE_PROP,
					DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
			( (TextItem) text.getElement( ) ).setProperty(
					TextItem.CONTENT_PROP, "<value-of>new Date()</value-of>" );

			page.getElement().getSlot( SimpleMasterPage.PAGE_FOOTER_SLOT )
					.add( text.element );
			design.getSlot( ReportDesign.PAGE_SLOT ).add( page.element );
		}

		// initialize the properties for the reprot design.
		Iterator itre = propNames.iterator( );
		while ( itre.hasNext( ) )
		{
			name = (String) itre.next( );
			value = PropertyValueValidationUtil.validateProperty( this, name,
					properties.get( name ) );
			design.setProperty( name, value );
		}

		isInitialized = true;

	}
}