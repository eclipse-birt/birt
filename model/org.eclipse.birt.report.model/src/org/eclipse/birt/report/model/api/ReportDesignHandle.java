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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetHandleAdapter;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.ModelUtil;

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
 *                      // Include one library
 *                      
 *                      ReportDesignHandle designHandle = ...;
 *                      designHandle.includeLibrary( &quot;libA.rptlibrary&quot;, &quot;LibA&quot; );
 *                      LibraryHandle libraryHandle = designHandle.getLibrary(&quot;LibA&quot;);
 *                       
 *                      // Create one label based on the one in library
 *                     
 *                      LabelHandle labelHandle = (LabelHandle) libraryHandle.findElement(&quot;companyNameLabel&quot;);
 *                      LabelHandle myLabelHandle = (LabelHandle) designHandle.getElementFactory().newElementFrom( labelHandle, &quot;myLabel&quot; );
 *                     
 *                      // Add the new label into design file
 *                     
 *                      designHandle.getBody().add(myLabelHandle);
 *                   
 * </pre>
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
	 * 
	 */
	public SlotHandle getStyles( )
	{
		return getSlot( IReportDesignModel.STYLE_SLOT );
	}

	/**
	 * Gets all css styles sheet
	 * 
	 * @return each item is <code>CssStyleSheetHandle</code>
	 */

	public List getAllCssStyleSheets( )
	{
		ReportDesign design = (ReportDesign) getElement( );
		List allStyles = new ArrayList( );
		List csses = design.getCsses( );
		for ( int i = 0; csses != null && i < csses.size( ); ++i )
		{
			CssStyleSheet sheet = (CssStyleSheet) csses.get( i );
			allStyles.add( sheet.handle( getModule( ) ) );
		}
		return allStyles;
	}

	/**
	 * Find style by style name
	 * 
	 * @param style
	 *            name
	 * @return style handle
	 */

	public SharedStyleHandle findStyle( String name )
	{
		List styles = getAllStyles( );
		for ( int i = 0; i < styles.size( ); ++i )
		{
			SharedStyleHandle style = (SharedStyleHandle) styles.get( i );
			if ( style.getName( ).equals( name ) )
			{
				return style;
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#importCssStyles(org.eclipse.birt.report.model.api.css.CssStyleSheetHandle,
	 *      java.util.List)
	 */

	public void importCssStyles( CssStyleSheetHandle stylesheet,
			List selectedStyles )
	{
		// do nothing now.

		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( );
		for ( int i = 0; i < selectedStyles.size( ); i++ )
		{
			SharedStyleHandle style = (SharedStyleHandle) selectedStyles
					.get( i );
			if ( stylesheet.findStyle( style.getName( ) ) != null )
			{
				module.makeUniqueName( style.getElement( ) );
				// Copy CssStyle to Style
				SharedStyleHandle newStyle = ModelUtil.TransferCssStyleToSharedStyle(
						module, style );

				if ( newStyle == null )
					continue;
				try
				{
					addElement( newStyle, IReportDesignModel.STYLE_SLOT );
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
		setStringProperty( DISPLAY_NAME_ID_PROP, displayNameKey );
	}

	/**
	 * Gets the resource key of the display name.
	 * 
	 * @return the resource key of the display name
	 */

	public String getDisplayNameKey( )
	{
		return getStringProperty( DISPLAY_NAME_ID_PROP );
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
		setStringProperty( DISPLAY_NAME_PROP, displayName );
	}

	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */

	public String getDisplayName( )
	{
		return getStringProperty( DISPLAY_NAME_PROP );
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
		setStringProperty( ICON_FILE_PROP, iconFile );
	}

	/**
	 * Gets the design icon/thumbnail file path.
	 * 
	 * @return the design icon/thumbnail file path
	 */

	public String getIconFile( )
	{
		return getStringProperty( ICON_FILE_PROP );
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
		setStringProperty( CHEAT_SHEET_PROP, cheatSheet );
	}

	/**
	 * Gets the design cheat sheet file path.
	 * 
	 * @return the design cheat sheet file path
	 */

	public String getCheatSheet( )
	{
		return getStringProperty( CHEAT_SHEET_PROP );
	}

	/**
	 * Sets the thumbnail image encoded in ISO-8859-1.
	 * 
	 * @param data
	 *            the thumbnail image to set
	 * @throws SemanticException
	 *             if the property is locked or not defined on this design.
	 */

	public void setThumbnail( byte[] data ) throws SemanticException
	{
		try
		{
			setStringProperty( THUMBNAIL_PROP, new String( data, CHARSET ) );
		}
		catch ( UnsupportedEncodingException e )
		{
			assert false;
		}
	}

	/**
	 * Gets the thumbnail image encoded in ISO-8859-1.
	 * 
	 * @return the thumbnail image
	 */

	public byte[] getThumbnail( )
	{
		return ( (ReportDesign) module ).getThumbnail( );
	}

	/**
	 * Deletes the thumbnail image in the design.
	 * 
	 * @throws SemanticException
	 *             if the property is locked or not defined on this design.
	 */

	public void deleteThumbnail( ) throws SemanticException
	{
		clearProperty( THUMBNAIL_PROP );
	}

	/**
	 * Gets all bookmarks defined in this module.
	 * 
	 * @return All bookmarks defined in this module.
	 */

	public List getAllBookmarks( )
	{
		// bookmark value in row, report item and listing group are the same
		// now.

		return ( (ReportDesign) module ).collectPropValues( BODY_SLOT,
				IReportItemModel.BOOKMARK_PROP );
	}

	/**
	 * Gets all TOCs defined in this module.
	 * 
	 * @return All TOCs defined in this module.
	 */

	public List getAllTocs( )
	{
		List tocs = ( (ReportDesign) module ).collectPropValues( BODY_SLOT,
				IReportItemModel.TOC_PROP );

		// TODO merge with IGroupElementModel.TOC_PROP.

		List resultList = new ArrayList( );
		Iterator iterator = tocs.iterator( );
		while ( iterator.hasNext( ) )
		{
			TOC toc = (TOC) iterator.next( );
			resultList.add( toc.getProperty( module, TOC.TOC_EXPRESSION ) );
		}
		return resultList;
	}

	/**
	 * Gets report items which holds a template definition, that is, report item
	 * in body slot and page slot. Notice, nested template items is excluded.
	 * 
	 * @return report items which holds a template definition, nested template
	 *         items is excluded.
	 */

	public List getReportItemsBasedonTempalates( )
	{
		ArrayList rtnList = new ArrayList( );
		ArrayList tempList = new ArrayList( );

		List contents = new ContainerContext( getElement( ), BODY_SLOT )
				.getContents( module );
		contents.addAll( new ContainerContext( getElement( ), PAGE_SLOT )
				.getContents( module ) );

		findTemplateItemIn( contents.iterator( ), tempList );

		for ( Iterator iter = tempList.iterator( ); iter.hasNext( ); )
		{
			rtnList.add( ( (DesignElement) iter.next( ) ).getHandle( module ) );
		}

		return Collections.unmodifiableList( rtnList );
	}

	/**
	 * Auxilary method, help to find design element which holds and template
	 * definition.
	 * 
	 * @param contents
	 *            the contents to search.
	 * @param addTo
	 *            The list to add to.
	 */

	private void findTemplateItemIn( Iterator contents, List addTo )
	{
		for ( ; contents.hasNext( ); )
		{
			DesignElement e = (DesignElement) contents.next( );
			if ( e.isTemplateParameterValue( module ) )
			{
				addTo.add( e );
				continue;
			}

			LevelContentIterator children = new LevelContentIterator( module,
					e, 1 );

			findTemplateItemIn( children, addTo );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#getCubes()
	 */
	public SlotHandle getCubes( )
	{
		return getSlot( CUBE_SLOT );
	}

	/**
	 * Gets the layout preference of this report design. It can be one of the
	 * following:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT</code>
	 * <li><code>DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT</code>
	 * </ul>
	 * 
	 * @return layout preference of report design
	 */
	public String getLayoutPreference( )
	{
		return getStringProperty( LAYOUT_PREFERENCE_PROP );
	}

	/**
	 * Sets the layout preference of this report design. The input layout can be
	 * one of the following:
	 * <ul>
	 * <li><code>DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT</code>
	 * <li><code>DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT</code>
	 * </ul>
	 * 
	 * @param layout
	 *            the layout to set
	 * @throws SemanticException
	 *             if value is invalid
	 */
	public void setLayoutPreference( String layout ) throws SemanticException
	{
		setStringProperty( LAYOUT_PREFERENCE_PROP, layout );
	}

	/**
	 * Returns the iterator over all included css style sheets. Each one is the
	 * instance of <code>IncludedCssStyleSheetHandle</code>
	 * 
	 * @return the iterator over all included css style sheets.
	 */

	public Iterator includeCssesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( IReportDesignModel.CSSES_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Includes one css with the given css file name. The new css will be
	 * appended to the css list.
	 * 
	 * @param sheetHandle
	 *            css style sheet handle
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list.
	 */

	public void addCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.addCss( sheetHandle );
	}

	/**
	 * Includes one css with the given css file name. The new css will be
	 * appended to the css list.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list.
	 */

	public void addCss( String fileName ) throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.addCss( fileName );
	}

	/**
	 * Drops the given css style sheet of this design file.
	 * 
	 * @param sheetHandle
	 *            the css to drop
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list. Or it maybe
	 *             because that the given css is not found in the design. Or
	 *             that the css has descedents in the current module
	 */

	public void dropCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.dropCss( sheetHandle );
	}

	/**
	 * Check style sheet can be droped or not.
	 * 
	 * @param sheetHandle
	 * @return <code>true</code> can be dropped.else return <code>false</code>
	 */

	public boolean canDropCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canDropCssStyleSheet( sheetHandle );
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @param sheetHandle
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public boolean canAddCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canAddCssStyleSheet( sheetHandle );
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @param fileName
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public boolean canAddCssStyleSheet( String fileName )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canAddCssStyleSheet( fileName );
	}

	/**
	 * Reloads the css with the given css file path. If the css already is
	 * included directly, reload it. If the css is not included, exception will
	 * be thrown.
	 * 
	 * @param sheetHandle
	 *            css style sheet handle.
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list. Or it maybe
	 *             because that the given css is not found in the design. Or
	 *             that the css has descedents in the current module
	 */

	public void reloadCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.reloadCss( sheetHandle );
	}
}
