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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.core.namespace.ModuleNameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.css.CssNameManager;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.ExtendedItem.StatusInfo;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.NamePropertyType;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.EncryptionUtil;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * Abstract handler for the XML module files. Holds the module being created.
 */

public abstract class ModuleParserHandler extends XMLParserHandler
{

	/**
	 * The design session that will own this module.
	 */

	protected DesignSession session = null;

	/**
	 * The module being created.
	 */

	protected Module module = null;

	/**
	 * Cached name of the module file.
	 */

	protected String fileName = null;

	/**
	 * Number value for the version string.
	 */

	int versionNumber = 0;

	/**
	 * Status identify whether the design file version is the current supported
	 * version.
	 */

	boolean isCurrentVersion = false;

	/**
	 * Control flag identify whether need mark line number of the design
	 * element.
	 */

	protected boolean markLineNumber = true;

	/**
	 * Temporary variable to cache line number information of the design
	 * elements.
	 */

	protected HashMap<Object, Integer> tempLineNumbers = null;

	/**
	 * The temporary value for parser compatible.
	 */

	protected HashMap<Object, Object> tempValue = new HashMap<Object, Object>( );

	/**
	 * Cached element list whose id is not handle and added to the id map.
	 */

	protected List<DesignElement> unhandleIDElements = new ArrayList<DesignElement>( );

	/**
	 * Lists of those extended-item whose name is not allocated.
	 */

	private List<DesignElement> unnamedReportItems = new ArrayList<DesignElement>( );

	/**
	 * Lists of those listing element whose group need to be recovered.
	 */

	private List<ListingElement> unresolvedListingElements = new ArrayList<ListingElement>( );

	/**
	 * Lists of all the extended items. In the endDocument we will handle
	 * extension parser compatibilities.
	 */

	private List<DesignElement> extendedItemList = new ArrayList<DesignElement>( );

	/**
	 * The map contains libraries that have been reload.
	 */

	protected Map<String, Library> reloadLibs = new HashMap<String, Library>( );

	/**
	 * Status identifying whether to only read simple property in report root
	 * element or to parse the whole design file.
	 */
	protected boolean isReadOnlyModuleProperties = false;

	/**
	 * List to record all the elements that set the style property.
	 */
	protected List<DesignElement> styledElements = null;

	/**
	 * Constructs the module parser handler with the design session.
	 * 
	 * @param theSession
	 *            the design session that is to own this module
	 * @param fileName
	 *            name of the module file
	 */

	protected ModuleParserHandler( DesignSession theSession, String fileName )
	{
		super( new ModuleParserErrorHandler( ) );
		this.session = theSession;
		this.fileName = fileName;
	}

	/**
	 * Constructs the module parser handler with the design session.
	 * 
	 * @param theSession
	 *            the design session that is to own this module
	 * @param fileName
	 *            name of the module file
	 * @param reloadLibs
	 */

	protected ModuleParserHandler( DesignSession theSession, String fileName,
			Map<String, Library> reloadLibs )
	{
		super( new ModuleParserErrorHandler( ) );
		this.session = theSession;
		this.fileName = fileName;
		this.reloadLibs = reloadLibs;
	}

	/**
	 * Returns the file name the handler is treating.
	 * 
	 * @return the file name the handler is treating.
	 */

	String getFileName( )
	{
		return this.fileName;
	}

	/**
	 * Returns <code>true</code> if the version of the module file this handler
	 * is parsing equals the given version.
	 * 
	 * @param toCompare
	 *            the version to compare
	 * @return <code>true</code> if the version of the module file this handler
	 *         is parsing equals <code>toCompare</code>.
	 */

	public boolean isVersion( int toCompare )
	{
		return versionNumber == toCompare;
	}

	/**
	 * Returns the module being created.
	 * 
	 * @return the module being created
	 */

	public Module getModule( )
	{
		return module;
	}

	/**
	 * Overrides the super method. This method first parses attributes of the
	 * current state, and then query whether to use a new state or the current
	 * one according to the attributes value.
	 * 
	 * @param namespaceURI
	 * @param localName
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */

	public void startElement( String namespaceURI, String localName,
			String qName, Attributes atts ) throws SAXException
	{
		errorHandler.setCurrentElement( qName );
		AbstractParseState newState = topState.startElement( qName );
		newState.parseAttrs( atts );
		AbstractParseState jumpToState = newState.jumpTo( );
		if ( jumpToState != null )
		{
			pushState( jumpToState );
			return;
		}

		newState.setElementName( qName );
		pushState( newState );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */

	public void endDocument( ) throws SAXException
	{
		super.endDocument( );

		this.tempValue = null;

		// rename invalid names that contains "." , "/".

		if ( versionNumber < VersionUtil.VERSION_3_2_13 )
		{
			List<Exception> handledExceptions = handleInvalidName( getErrorHandler( )
					.getErrors( ) );
			getErrorHandler( ).getErrors( ).removeAll( handledExceptions );
		}

		handleUnresolveListingElements( );

		// add all the exceptions to the module

		module.getAllExceptions( ).addAll( getErrorHandler( ).getErrors( ) );

		// Check whether duplicate library namespace exists.

		List<Library> libraries = module.getAllLibraries( );
		{
			Iterator<Library> iter = libraries.iterator( );
			while ( iter.hasNext( ) )
			{
				Library library = iter.next( );

				if ( !library.isValid( ) )
				{
					// Forward the fatal error to top level.

					Exception fatalException = ModelUtil
							.getFirstFatalException( library.getAllExceptions( ) );
					if ( fatalException != null )
					{
						XMLParserException exception = errorHandler
								.semanticError( fatalException );
						module.getAllExceptions( ).add( exception );
					}
				}
			}
		}

		// Skip the semantic check if we've already found errors.
		// Doing the semantic check would just uncover bogus errors
		// due to the ones we've already seen.

		if ( !module.getAllErrors( ).isEmpty( )
				|| module.getFatalException( ) != null )
		{
			// The most errors which are found during parsing cannot be
			// recovered.

			module.setValid( false );
			List<Exception> allExceptions = new ArrayList<Exception>( );
			allExceptions.addAll( module.getAllExceptions( ) );
			allExceptions.addAll( errorHandler.getWarnings( ) );

			DesignFileException exception = new DesignFileException( module
					.getFileName( ), allExceptions );

			throw new SAXException( exception );
		}

		// the module is ok, then allocate the id for it and its contents

		if ( !unhandleIDElements.isEmpty( ) )
		{
			handleID( );
			unhandleIDElements = null;
		}

		// add un-named extended items to name-space
		if ( !unnamedReportItems.isEmpty( )
				&& versionNumber <= VersionUtil.VERSION_3_2_12 )
			handleUnnamedReportItems( );

		// if module is a report design or the directly opened library, its
		// namespace is null, then we will clear all the cached level names
		if ( versionNumber <= VersionUtil.VERSION_3_2_13
				&& StringUtil.isBlank( module.getNamespace( ) ) )
		{
			( (ModuleNameHelper) module.getNameHelper( ) ).clearCachedLevels( );
		}

		// build the line number information of design elements if needed.
		// this has to be done after element id has been set since slots uses
		// xpath as keys.

		if ( markLineNumber && tempLineNumbers != null )
		{
			handleLineNumber( );
		}

		// handle the style name backward compatibilities, this must do before
		// the semantic check to avoid wrong resolve
		if ( versionNumber < VersionUtil.VERSION_3_2_19 )
		{
			handleStyleNameCompatibilities( );
			handleEncryptionForPropertyBinding( );
		}

		// if module options not set the parser-semantic check options or set it
		// to true, then perform semantic check. Semantic error is recoverable.

		ModuleOption options = module.getOptions( );
		if ( options == null || options.useSemanticCheck( ) )
			module.semanticCheck( module );

		// translates warnings during parsing design files to ErrorDetail.

		if ( errorHandler.getWarnings( ) != null )
		{
			module.getAllExceptions( ).addAll( errorHandler.getWarnings( ) );
		}

		// do some parser compatibility about extended elements

		if ( !extendedItemList.isEmpty( ) )
		{
			module.getVersionManager( ).setHasExtensionCompatibilities(
					handleExtendedItemCompatibility( ) );
		}
	}

	private boolean handleExtendedItemCompatibility( )
	{
		assert !module.isReadOnly( );

		List<Exception> errorList = module.getAllExceptions( );
		boolean hasCompatibilities = false;
		for ( int i = 0; i < extendedItemList.size( ); i++ )
		{
			ExtendedItem element = (ExtendedItem) extendedItemList.get( i );
			StatusInfo status = element.checkCompatibility( module );
			assert status != null;
			errorList.addAll( status.getErrors( ) );
			if ( !hasCompatibilities && status.hasCompatibilities( ) )
				hasCompatibilities = true;
		}

		// clear the activity stack and save state
		module.getActivityStack( ).flush( );
		module.setSaveState( 0 );

		return hasCompatibilities;
	}

	/**
	 * @param versionNumber
	 *            the versionNumber to set
	 */
	public void setVersionNumber( int versionNumber )
	{
		this.versionNumber = versionNumber;
	}

	/**
	 * Allocates a unique id for all the unhandled elements.
	 */

	private void handleID( )
	{
		for ( int i = 0; i < unhandleIDElements.size( ); i++ )
		{
			DesignElement element = unhandleIDElements.get( i );

			if ( element.getExtendsElement( ) == null )
			{
				if ( element.getRoot( ) == module )
				{
					assert element.getID( ) == DesignElement.NO_ID;
					element.setID( module.getNextID( ) );
					module.addElementID( element );
				}
			}
			else
				module.manageId( element, true );
		}
	}

	/**
	 * Adds unnamed extended items to name-space.
	 * 
	 */

	private void handleUnnamedReportItems( )
	{
		for ( int i = 0; i < unnamedReportItems.size( ); i++ )
		{
			DesignElement element = unnamedReportItems.get( i );
			ModelUtil.addElement2NameSpace( module, element );
		}
	}

	/**
	 * If the name contains the invalid characters, rename it.
	 * 
	 */

	private List<Exception> handleInvalidName(
			List<? extends Exception> exceptions )
	{
		List<Exception> handledExceptions = new ArrayList<Exception>( );
		List<DesignElement> processElements = new ArrayList<DesignElement>( );

		for ( int i = 0; i < exceptions.size( ); i++ )
		{
			Exception tmpObj = exceptions.get( i );
			if ( !( tmpObj instanceof XMLParserException ) )
				continue;

			Exception exception = ( (XMLParserException) tmpObj )
					.getException( );

			if ( !( exception instanceof NameException ) )
				continue;

			NameException nameException = (NameException) exception;
			DesignElement tmpElement = nameException.getElement( );

			// for invalid name case.

			if ( tmpElement.getName( ) != null
					&& nameException.getErrorCode( ) == NameException.DESIGN_EXCEPTION_INVALID_NAME )
			{
				String oldName = nameException.getName( );
				String newName = NamePropertyType.validateName( oldName );

				if ( oldName.equals( newName ) )
					continue;

				tmpElement.setName( newName );
				int ns = ( (ElementDefn) tmpElement.getDefn( ) )
						.getNameSpaceID( );
				if ( ns != MetaDataConstants.NO_NAME_SPACE )
					new NameExecutor( tmpElement ).getNameSpace( module )
							.rename( tmpElement, oldName, newName );

				processElements.add( tmpElement );
				handledExceptions.add( tmpObj );
			}
			// if the name has other exceptions, also add to return list for
			// removing them

			else if ( processElements.contains( tmpElement ) )
				handledExceptions.add( tmpObj );
		}

		return handledExceptions;
	}

	/**
	 * Adds unnamed extended items to name-space.
	 * 
	 */

	private void handleUnresolveListingElements( )
	{
		for ( int i = 0; i < unresolvedListingElements.size( ); i++ )
		{
			ListingElement tmpElement = unresolvedListingElements.get( i );
			RecoverDataGroupUtil.checkListingGroup( tmpElement, this );
		}
	}

	private void handleLineNumber( )
	{
		Iterator iter = tempLineNumbers.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );

			Object key = entry.getKey( );
			module.addLineNo( key, (Integer) entry.getValue( ) );
		}

		tempLineNumbers.clear( );
		tempLineNumbers = null;

	}

	/**
	 * Initializes line number mark flag if needed.
	 * 
	 * @param options
	 *            the options set for this module
	 */

	final protected void buildModuleOptions( ModuleOption options )
	{
		assert module != null;
		if ( options == null )
		{
			markLineNumber = true;
			isReadOnlyModuleProperties = false;
		}
		else
		{
			markLineNumber = options.markLineNumber( );
			Boolean isSimple = (Boolean) options
					.getProperty( ModuleOption.READ_ONLY_MODULE_PROPERTIES );
			if ( isSimple != null && isSimple.booleanValue( ) )
			{
				isReadOnlyModuleProperties = true;
				// if in simple parser, need not do semantic check
				options.setSemanticCheck( false );
			}
			else
				isReadOnlyModuleProperties = false;

		}

		if ( markLineNumber )
		{
			module.initLineNoMap( );
			tempLineNumbers = new HashMap<Object, Integer>( );
		}

		// if read-only key is set to TRUE, then the module must be read-only
		if ( isReadOnlyModuleProperties )
			module.setReadOnly( );
	}

	/**
	 * Adds a unNamed extended-item to the list.
	 * 
	 * @param element
	 *            the element to add
	 */

	final void addUnnamedReportItem( DesignElement element )
	{
		assert element instanceof ReportItem;

		if ( !unnamedReportItems.contains( element ) )
			unnamedReportItems.add( element );
	}

	/**
	 * Adds a unNamed extended-item to the list.
	 * 
	 * @param element
	 *            the element to add
	 */

	final void addUnresolveListingElement( ListingElement element )
	{
		if ( !unresolvedListingElements.contains( element ) )
			unresolvedListingElements.add( element );
	}

	/**
	 * Adds an extended element to the cached list.
	 * 
	 * @param element
	 */
	final void addExtendedItem( ExtendedItem element )
	{
		extendedItemList.add( element );
	}

	/**
	 * Handles the style name compatibilities since the version 3.2.19 Model
	 * changes the style name to be case-insensitive.
	 */
	private void handleStyleNameCompatibilities( )
	{
		// we only need handle the root module is report design, not need
		// for library
		if ( module instanceof ReportDesign )
		{
			List<DesignElement> designStyles = module.getSlot(
					IReportDesignModel.STYLE_SLOT ).getContents( );
			if ( designStyles == null || designStyles.isEmpty( ) )
				return;
			Map<String, DesignElement> styleMap = new HashMap<String, DesignElement>( );
			Theme theme = module.getTheme( );

			// if theme is null, handle the design style name and theme
			// style name
			if ( theme != null )
			{
				List<DesignElement> themeStyles = theme.getSlot(
						IThemeModel.STYLES_SLOT ).getContents( );
				if ( themeStyles != null )
				{
					for ( int i = 0; i < themeStyles.size( ); i++ )
					{
						DesignElement style = themeStyles.get( i );
						String name = style.getName( ).toLowerCase( );
						if ( !styleMap.containsKey( name ) )
							styleMap.put( name, style );
					}
				}

				// build imported css styles in theme
				List<CssStyle> csses = CssNameManager.getStyles( theme );
				for ( int i = 0; csses != null && i < csses.size( ); ++i )
				{
					CssStyle s = csses.get( i );
					String name = s.getName( ).toLowerCase( );
					if ( !styleMap.containsKey( name ) )
						styleMap.put( name, s );
				}
			}

			// build imported css styles in report design
			List<CssStyle> csses = CssNameManager
					.getStyles( (ICssStyleSheetOperation) module );
			for ( int i = 0; csses != null && i < csses.size( ); ++i )
			{
				CssStyle s = csses.get( i );
				String name = s.getName( ).toLowerCase( );
				if ( !styleMap.containsKey( name ) )
					styleMap.put( name, s );
			}

			if ( !styleMap.isEmpty( ) )
			{
				for ( int i = 0; i < designStyles.size( ); i++ )
				{
					DesignElement designStyle = designStyles.get( i );
					String styleName = designStyle.getName( );
					String lowerCaseName = styleName.toLowerCase( );

					NameSpace ns = new NameExecutor( designStyle )
							.getNameHelper( module ).getNameSpace(
									ReportDesign.STYLE_NAME_SPACE );
					if ( styleMap.containsKey( lowerCaseName ) )
					{
						DesignElement existedStyle = styleMap
								.get( lowerCaseName );
						assert existedStyle != null;

						// if style name is not equal and just the same with
						// different cases, then do the rename
						if ( !existedStyle.getName( ).equals( styleName ) )
						{
							int index = 0;
							String baseName = styleName;

							// style name is case-insensitive
							while ( styleMap.containsKey( lowerCaseName )
									|| ns.contains( lowerCaseName ) )
							{
								styleName = baseName + ++index;
								lowerCaseName = styleName.toLowerCase( );
							}

							// set the unique name and add the element to
							// the name manager

							ns.remove( designStyle );
							// check if some element refers the original
							// name, then changes the style reference
							if ( styledElements != null )
							{
								for ( int j = 0; j < styledElements.size( ); j++ )
								{
									StyledElement styledElement = (StyledElement) styledElements
											.get( j );
									if ( designStyle.getName( ).equals(
											styledElement.getStyleName( ) ) )
									{
										styledElement.setStyleName( styleName
												.trim( ) );
									}
								}
							}
							designStyle.setName( styleName.trim( ) );
							ns.insert( designStyle );
						}
					}

				}
			}

		}

	}

	/**
	 * Handles the backward compatibilities for encryption in property binding
	 * value.
	 */
	private void handleEncryptionForPropertyBinding( )
	{
		List<Object> propBindingList = module.getListProperty( module,
				IModuleModel.PROPERTY_BINDINGS_PROP );
		if ( propBindingList != null )
		{
			for ( int i = 0; i < propBindingList.size( ); i++ )
			{
				PropertyBinding propBinding = (PropertyBinding) propBindingList
						.get( i );
				Object oldValue = propBinding.getValue( );
				EncryptionUtil.setEncryptionBindingValue( module, propBinding,
						(PropertyDefn) propBinding.getDefn( ).getMember(
								PropertyBinding.VALUE_MEMBER ), oldValue );
			}
		}
	}

	static class ModuleLexicalHandler implements LexicalHandler
	{

		ModuleParserHandler handler = null;

		/**
		 * 
		 */

		ModuleLexicalHandler( ModuleParserHandler handler )
		{
			this.handler = handler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
		 */
		public void comment( char[] ch, int start, int length )
				throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
		 */
		public void endCDATA( ) throws SAXException
		{
			AbstractParseState tmpState = handler.topState;
			tmpState.setIsCDataSection( true );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#endDTD()
		 */
		public void endDTD( ) throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
		 */
		public void endEntity( String name ) throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
		 */

		public void startCDATA( ) throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String,
		 * java.lang.String, java.lang.String)
		 */
		public void startDTD( String name, String publicId, String systemId )
				throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
		 */
		public void startEntity( String name ) throws SAXException
		{
		}
	}
}
