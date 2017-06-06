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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.eclipse.birt.core.util.CommonUtil;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.NamePropertyType;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.parser.ActionStructureState;
import org.eclipse.birt.report.model.parser.DesignReader;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.parser.GenericModuleReader;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.parser.ModuleParserErrorHandler;
import org.eclipse.birt.report.model.parser.ModuleParserHandler;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.DataTypeConversionUtil;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionControlMgr;
import org.eclipse.birt.report.model.util.VersionInfo;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.eclipse.birt.report.model.writer.IndentableXMLWriter;
import org.eclipse.birt.report.model.writer.ModuleWriter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ibm.icu.util.ULocale;

/**
 * Provides some tool methods about the modules.
 */

public class ModuleUtil
{

	/**
	 * The library type.
	 */

	public final static int LIBRARY = 0;

	/**
	 * The report design type.
	 */

	public final static int REPORT_DESIGN = 1;

	/**
	 * The invalid module.
	 */

	public final static int INVALID_MODULE = 2;

	/**
	 * Parser handler used to parse only the Action part of the design. The
	 * existing action state and property validating is reused.
	 */

	private static class ActionParserHandler extends ModuleParserHandler
	{

		/**
		 * A fake element with the given action. Used to reuse the existing
		 * action parser logic.
		 */

		DesignElement element = null;

		/**
		 * 
		 * @param element
		 * @param theModule
		 */
		public ActionParserHandler( DesignElement element )
		{
			super( null, null );
			this.element = element;
			module = new ReportDesign( null );

			setVersionNumber( DesignSchemaConstants.REPORT_VERSION_NUMBER );
			module.setID( module.getNextID( ) );
			module.addElementID( module );
		}

		public AbstractParseState createStartState( )
		{
			return new StartState( );
		}

		/**
		 * Recognizes the top-level tags: Report or Library
		 */

		class StartState extends InnerParseState
		{

			public AbstractParseState startElement( String tagName )
			{
				if ( DesignSchemaConstants.STRUCTURE_TAG
						.equalsIgnoreCase( tagName ) )
				{
					ActionStructureState state = new ActionStructureState(
							ActionParserHandler.this, element );
					state.setName( IImageItemModel.ACTION_PROP );
					return state;
				}
				return super.startElement( tagName );
			}
		}

	}

	/**
	 * Deserialize an input stream into an Action.
	 * 
	 * @param streamData
	 *            a stream represent an action.
	 * @return an internal Action structure
	 * @throws DesignFileException
	 *             if the exception occur when interpret the stream data.
	 */

	public static ActionHandle deserializeAction( InputStream streamData )
			throws DesignFileException
	{
		return deserializeAction( streamData, null );
	}

	/**
	 * Deserialize an input stream into an Action.
	 * 
	 * @param streamData
	 *            a stream represent an action.
	 * @param element
	 * @return an internal Action structure
	 * @throws DesignFileException
	 *             if the exception occur when interpret the stream data.
	 */

	public static ActionHandle deserializeAction( InputStream streamData,
			DesignElementHandle element ) throws DesignFileException
	{

		// A fake element with the given action. Used to reuse the existing
		// action parser logic.

		DesignElement image = new ImageItem( );
		DesignElement e = element == null ? image : element.getElement( );
		ActionParserHandler handler = new ActionParserHandler( image );

		Module module = element == null ? handler.getModule( ) : element
				.getModule( );

		ElementPropertyDefn propDefn = e
				.getPropertyDefn( IImageItemModel.ACTION_PROP );

		if ( streamData == null )
		{
			Action action = StructureFactory.createAction( );
			List<Action> actions = new ArrayList<Action>( );
			actions.add( action );

			e.setProperty( propDefn, actions );
			action.setContext( new StructureContext( e, propDefn, action ) );
			return getActionHandle( e.getHandle( module ) );
		}

		if ( !streamData.markSupported( ) )
			streamData = new BufferedInputStream( streamData );

		assert streamData.markSupported( );
		parse( handler, streamData, "" ); //$NON-NLS-1$

		if ( element != null )
		{
			List actions = (List) image.getProperty( handler.getModule( ),
					propDefn );
			assert actions != null && actions.size( ) == 1;
			Action action = (Action) actions.get( 0 );
			e.setProperty( propDefn, actions );
			action.setContext( new StructureContext( e, e
					.getPropertyDefn( IImageItemModel.ACTION_PROP ), action ) );
		}

		return getActionHandle( e.getHandle( module ) );
	}

	/**
	 * Gets the action handle of this element.
	 * 
	 * @param element
	 * @return action handle
	 */

	private static ActionHandle getActionHandle( DesignElementHandle element )
	{
		PropertyHandle propHandle = element
				.getPropertyHandle( IImageItemModel.ACTION_PROP );
		List actions = (List) propHandle.getValue( );
		assert actions != null && actions.size( ) == 1;
		Action action = (Action) actions.get( 0 );

		if ( action == null )
			return null;
		return (ActionHandle) action.getHandle( propHandle );
	}

	/**
	 * Auxiliary method to help parse the input stream.
	 * 
	 * @param handler
	 *            the parse handler
	 * @param streamData
	 *            the input stream
	 * @throws DesignFileException
	 *             any exception if error happens
	 */

	private static void parse( XMLParserHandler handler,
			InputStream streamData, String filename )
			throws DesignFileException
	{
		try
		{
			ModelUtil.checkUTFSignature( streamData, filename );
			SAXParser parser = CommonUtil.createSAXParser( );
			InputSource inputSource = new InputSource( streamData );
			inputSource.setEncoding( UnicodeUtil.SIGNATURE_UTF_8 );
			parser.parse( inputSource, handler );
		}
		catch ( SAXException e )
		{
			List<XMLParserException> errors = handler.getErrorHandler( )
					.getErrors( );

			// Syntax error is found

			if ( e.getException( ) instanceof DesignFileException )
			{
				throw (DesignFileException) e.getException( );
			}

			// Invalid xml error is found

			throw new DesignFileException( null, errors, e );
		}
		catch ( ParserConfigurationException e )
		{
			throw new DesignFileException( null, handler.getErrorHandler( )
					.getErrors( ), e );
		}
		catch ( IOException e )
		{
			throw new DesignFileException( null, handler.getErrorHandler( )
					.getErrors( ), e );
		}
	}

	/**
	 * Deserializes a string into an ActionHandle, notice that the handle is
	 * faked, the action is not in the design tree, the operation to the handle
	 * is not able to be undone.
	 * 
	 * @param strData
	 *            a string represent an action.
	 * @return a handle to the action.
	 * @throws DesignFileException
	 *             if the exception occur when interpret the stream data.
	 */

	public static ActionHandle deserializeAction( String strData )
			throws DesignFileException
	{
		return deserializeAction( strData, null );
	}

	/**
	 * Deserialize a string into an ActionHandle, notice that the handle is
	 * faked, the action is not in the design tree, the operation to the handle
	 * is not able to be undoned.
	 * 
	 * @param strData
	 *            a string represent an action.
	 * @param element
	 * @return a handle to the action.
	 * @throws DesignFileException
	 *             if the exception occur when interpret the stream data.
	 */

	public static ActionHandle deserializeAction( String strData,
			DesignElementHandle element ) throws DesignFileException
	{
		InputStream is = null;
		String streamToOpen = StringUtil.trimString( strData );
		if ( streamToOpen != null )
		{
			try
			{
				is = new ByteArrayInputStream( streamToOpen
						.getBytes( UnicodeUtil.SIGNATURE_UTF_8 ) );
			}
			catch ( UnsupportedEncodingException e )
			{
				assert false;
			}

		}
		return deserializeAction( is, element );
	}

	/**
	 * Serialize an action into a stream, the stream is in UTF-8 encoding.
	 * 
	 * @param action
	 *            a given action structure.
	 * @return an output stream represent the action xml data.
	 * @throws IOException
	 *             if I/O exception occur when writing the stream.
	 */

	public static String serializeAction( ActionHandle action )
			throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream( );
		ActionWriter writer = new ActionWriter( );
		writer.write( os, (Action) action.getStructure( ) );
		try
		{
			return os.toString( UnicodeUtil.SIGNATURE_UTF_8 );
		}
		catch ( UnsupportedEncodingException e )
		{
			assert false;
		}

		return os.toString( );
	}

	private static class SectionXMLWriter extends IndentableXMLWriter
	{

		/**
		 * 
		 * @param os
		 * @param signature
		 * @throws IOException
		 */
		public SectionXMLWriter( OutputStream os, String signature )
				throws IOException
		{
			super( );
			out = new PrintStream( os, false, OUTPUT_ENCODING );
		}
	}

	/**
	 * Write an action into a stream in UTF-8 encoding.
	 */

	private static class ActionWriter extends ModuleWriter
	{

		/**
		 * Wirte the action in to a stream.
		 * 
		 * @param os
		 * @param action
		 * @throws IOException
		 */
		public void write( OutputStream os, Action action ) throws IOException
		{
			writer = new SectionXMLWriter( os, UnicodeUtil.SIGNATURE_UTF_8 );
			writeAction( action );
		}

		protected Module getModule( )
		{
			return null;
		}
	}

	/**
	 * Justifies whether a given input stream is a valid report design.
	 * 
	 * @param sessionHandle
	 *            the current session of the report design
	 * @param fileName
	 *            the file name of the report design
	 * @param is
	 *            the input stream of the report design
	 * @return true if the input stream is a valid report design, otherwise
	 *         false
	 */

	public static boolean isValidDesign( SessionHandle sessionHandle,
			String fileName, InputStream is )
	{
		ReportDesign design = null;
		try
		{
			ModuleOption options = new ModuleOption( );
			options.setSemanticCheck( false );
			design = DesignReader.getInstance( ).read(
					sessionHandle.getSession( ), fileName, is, options );
			return design != null;
		}
		catch ( DesignFileException e )
		{
			return false;
		}
	}

	/**
	 * Justifies whether a library resource with the given file name is a valid
	 * library.
	 * 
	 * @param sessionHandle
	 *            the current session of the library
	 * @param fileName
	 *            the file name of the library
	 * @param is
	 *            the input stream of the library
	 * @return true if the library resource is a valid library, otherwise false
	 */

	public static boolean isValidLibrary( SessionHandle sessionHandle,
			String fileName, InputStream is )
	{
		Library lib = null;
		try
		{
			ModuleOption options = new ModuleOption( );
			options.setSemanticCheck( false );
			lib = LibraryReader.getInstance( ).read(
					sessionHandle.getSession( ), fileName, is, options );
			return lib != null;
		}
		catch ( DesignFileException e )
		{
			return false;
		}
	}

	/**
	 * Checks the input stream with given file name. If it is a valid
	 * design/library, return <code>ModuleUtil.REPORT_DESIGN</code>/
	 * <code>ModuleUtil.LIBRARY</code>, otherwise,
	 * <code>ModuleUtil.INVALID</code> is return.
	 * 
	 * @param sessionHandle
	 *            the current session of the library
	 * @param fileName
	 *            the file name of the library
	 * @param is
	 *            the input stream of the library
	 * @return <code>ModuleUtil.REPORT_DESIGN</code> if the input stream is a
	 *         report design, <code>ModuleUtil.LIBRARY</code> if the input
	 *         stream is a library, <code>ModuleUtil.INVALID</code> otherwise.
	 */

	public static int checkModule( SessionHandle sessionHandle,
			String fileName, InputStream is )
	{
		Module rtnModule = null;
		try
		{
			ModuleOption options = new ModuleOption( );
			options.setSemanticCheck( false );
			rtnModule = GenericModuleReader.getInstance( ).read(
					sessionHandle.getSession( ), fileName, is, options );
		}
		catch ( DesignFileException e )
		{
			return INVALID_MODULE;
		}

		return rtnModule instanceof Library ? LIBRARY : REPORT_DESIGN;
	}

	/**
	 * Parser handler used to parse only the version attribute of the module.
	 * The existing report and library state is reused.
	 */

	private static class VersionParserHandler extends XMLParserHandler
	{

		private String version = null;

		/**
		 * Default constructor.
		 */
		public VersionParserHandler( )
		{
			super( new ModuleParserErrorHandler( ) );
		}

		public AbstractParseState createStartState( )
		{
			return new StartState( );
		}

		/**
		 * Recognizes the top-level tags: Report or Library
		 */

		class StartState extends InnerParseState
		{

			public AbstractParseState startElement( String tagName )
			{
				if ( DesignSchemaConstants.REPORT_TAG
						.equalsIgnoreCase( tagName )
						|| DesignSchemaConstants.LIBRARY_TAG
								.equalsIgnoreCase( tagName ) )
					return new VersionState( );
				return super.startElement( tagName );
			}
		}

		/**
		 * Recognizes the top-level tags: Report or Library
		 */

		class VersionState extends InnerParseState
		{

			public void parseAttrs( Attributes attrs )
					throws XMLParserException
			{
				String version = attrs
						.getValue( DesignSchemaConstants.VERSION_ATTRIB );
				VersionParserHandler.this.version = version;
			}

			public void end( ) throws SAXException
			{
			}
		}
	}

	/**
	 * Checks whether the input stream holds a version number before some
	 * specific features is supported. This method is used to remind user that
	 * opening this stream may need to convert the original file automatically.
	 * 
	 * @param streamData
	 *            the input stream
	 * @return a list whose entry is of <code>IVersionInfo</code> type. Each
	 *         kind of automatical conversion information is stored in one
	 *         instance of <code>IVersionInfo</code>. If the size of the return
	 *         list is 0, there is no auto-conversion.
	 */

	private static List<IVersionInfo> checkVersion( InputStream streamData,
			String filename ) throws DesignFileException
	{
		DesignSession session = new DesignSession( ThreadResources.getLocale( ) );
		byte[] buf = new byte[512];
		int len;
		boolean isSupportedUnknownVersion = false;

		ByteArrayOutputStream bySteam = new ByteArrayOutputStream( );
		byte[] data = null;
		try
		{
			while ( ( len = streamData.read( buf ) ) > 0 )
			{
				bySteam.write( buf, 0, len );
				bySteam.flush( );
			}

			data = bySteam.toByteArray( );
			bySteam.close( );
		}
		catch ( IOException e1 )
		{
			// do nothing
		}

		try
		{
			InputStream inputStreamToParse = new ByteArrayInputStream( data );
			Module module = session.openModule( filename, inputStreamToParse );

			String version = module.getVersionManager( ).getVersion( );
			if( module.getOptions( ) != null )
			{
				isSupportedUnknownVersion = module.getOptions( ).isSupportedUnknownVersion( );
			}
			List<IVersionInfo> retList = ModelUtil.checkVersion( version, isSupportedUnknownVersion );
			if ( hasCompatibilities( module ) )
				retList.add( new VersionInfo( version,
						VersionInfo.EXTENSION_COMPATIBILITY ) );
			return retList;
		}
		catch ( DesignFileException e )
		{
			if ( data != null )
			{
				VersionParserHandler handler = new VersionParserHandler( );

				InputStream inputStreamToParse = new ByteArrayInputStream( data );
				if ( !inputStreamToParse.markSupported( ) )
					inputStreamToParse = new BufferedInputStream( streamData );

				parse( handler, inputStreamToParse, filename );
				
				return ModelUtil.checkVersion( handler.version, isSupportedUnknownVersion );
			}
			return Collections.emptyList( );
		}
	}

	private static boolean hasCompatibilities( Module module )
	{
		VersionControlMgr versionMgr = module.getVersionManager( );
		if ( versionMgr.hasExtensionCompatibilities( ) )
			return true;

		// check included libraries
		List<Library> libs = module.getAllLibraries( );
		if ( libs != null && !libs.isEmpty( ) )
		{
			for ( int i = 0; i < libs.size( ); i++ )
			{
				Library lib = libs.get( i );
				if ( lib.getVersionManager( ).hasExtensionCompatibilities( ) )
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the opening design file holds a version number before the
	 * some specific features is supported. This method is used to remind user
	 * that opening the file may need convert the original file automatically.
	 * 
	 * @param fileName
	 *            the file name with full path of the design file
	 * @return a list whose entry is of <code>IVersionInfo</code> type. Each
	 *         kind of automatical conversion information is stored in one
	 *         instance of <code>IVersionInfo</code>. Note that if the design
	 *         file does not exist, or it is an invalid design file, an instance
	 *         of <code>IVersionInfo</code> will also generate. If the size of
	 *         the return list is 0, there is no auto-conversion.
	 */

	public static List checkVersion( String fileName )
	{
		List rtnList = new ArrayList( );
		InputStream inputStream = null;

		URL url;
		try
		{
			url = ModelUtil.getURLPresentation( fileName );
			inputStream = url.openStream( );
		}
		catch ( MalformedURLException e2 )
		{
			// do nothing
		}
		catch ( IOException e )
		{
			rtnList.add( new VersionInfo( null, VersionInfo.INVALID_DESIGN_FILE ) );
			return rtnList;
		}

		if ( inputStream == null )
		{
			try
			{
				inputStream = new FileInputStream( fileName );
			}
			catch ( FileNotFoundException e2 )
			{
				rtnList.add( new VersionInfo( null,
						VersionInfo.INVALID_DESIGN_FILE ) );
				return rtnList;
			}
		}

		try
		{
			inputStream = new BufferedInputStream( inputStream );
			rtnList.addAll( checkVersion( inputStream, fileName ) );
		}
		catch ( DesignFileException e1 )
		{
			rtnList.add( new VersionInfo( null, VersionInfo.INVALID_DESIGN_FILE ) );
		}
		finally
		{
			try
			{
				inputStream.close( );
			}
			catch ( IOException e )
			{
			}
		}

		return rtnList;
	}

	/**
	 * Returns externalized message for the given locale.
	 * 
	 * @param element
	 *            the report element.
	 * @param key
	 *            the display key property value
	 * @param value
	 *            the property value
	 * @param locale
	 *            the locale
	 * @return externalized message.
	 */

	public static String getExternalizedValue( DesignElementHandle element,
			String key, String value, ULocale locale )
	{
		if ( element == null )
			return value;

		DesignElement tmpElement = element.getElement( );
		while ( tmpElement != null )
		{
			Module root = tmpElement.getRoot( );
			if ( root == null )
				break;

			String externalizedText = root.getMessage( key, locale );
			if ( externalizedText != null )
				return externalizedText;

			if ( !tmpElement.isVirtualElement( ) )
				tmpElement = tmpElement.getExtendsElement( );
			else
				tmpElement = tmpElement.getVirtualParent( );
		}

		return value;
	}

	/**
	 * This API is only for helping GUI to draw the dimension joint condition
	 * editor pad. When user extends an library cube to design, the hierarchy
	 * handle from the joint condition should be same with the virtual hierarchy
	 * in the reprot design cube.
	 * 
	 * @param conditionHierarchy
	 *            the hierarchy handle from the dimension joint condition
	 *            structure.
	 * @param cubeHierarchy
	 *            the hierarchy handle from the cube.
	 * @return
	 */
	public static boolean isEqualHierarchiesForJointCondition(
			HierarchyHandle conditionHierarchy, HierarchyHandle cubeHierarchy )
	{

		if ( conditionHierarchy == cubeHierarchy )
			return true;

		if ( ( conditionHierarchy != null ) && ( cubeHierarchy != null ) )
		{
			if ( conditionHierarchy.equals( cubeHierarchy ) )
				return true;

			DesignElement virtualParent = cubeHierarchy.getElement( )
					.getVirtualParent( );
			if ( virtualParent == null )
				return false;

			if ( conditionHierarchy.getElement( ).equals( virtualParent ) )
				return true;

			return false;
		}
		return false;
	}

	/**
	 * checks if the name of the element is valid. The following case the name
	 * will be considered as invalid. <li>contains the following characters:
	 * "/","\\", ".", "!", ";",","</li>
	 * 
	 * @param elementHandle
	 *            the design element need to be checked the name property value.
	 * @param propName
	 *            the property name which is name property type of this design
	 *            element.
	 * @param nameValue
	 *            the value of the name property.
	 * 
	 * @return true if the value of the name property is valid, false if it is
	 *         not valid.
	 */
	public static boolean isValidElementName(
			DesignElementHandle elementHandle, String propName, String nameValue )
	{
		PropertyDefn propDefn = (PropertyDefn) elementHandle
				.getPropertyDefn( propName );

		if ( propDefn == null )
			return false;

		PropertyType propType = propDefn.getType( );

		if ( propType.getTypeCode( ) != PropertyType.NAME_TYPE )
			return false;

		ElementDefn metaData = (ElementDefn) elementHandle.getDefn( );

		if ( ( nameValue == null ) || StringUtil.isEmpty( nameValue ) )
		{
			if ( metaData.getNameOption( ) == MetaDataConstants.REQUIRED_NAME )
				return false;
		}
		try
		{
			Module module = elementHandle.getModule( );
			DesignElement element = elementHandle.getElement( );
			propType.validateValue( module, element, propDefn, nameValue );

			NameExecutor executor = new NameExecutor( module, element );
			if ( executor.hasNamespace( ) )
			{
				DesignElement existedElement = executor.getElement( nameValue );
				if ( existedElement != null )
					return false;
			}
			return true;
		}
		catch ( PropertyValueException e )
		{
			return false;
		}

	}

	/**
	 * checks is the name value is valid for the design element.
	 * 
	 * @param elementHandle
	 *            element need to be checked for the name.
	 * @param nameValue
	 *            name of the element.
	 * @return true if the name is valid, false if the name is not valid.
	 */
	public static boolean isValidElementName( DesignElementHandle elementHandle )
	{

		return isValidElementName( elementHandle,
				IDesignElementModel.NAME_PROP, elementHandle.getName( ) );

	}

	/**
	 * Determine if the value1 of this filter condition is a list.
	 * 
	 * @param filter
	 *            the filter need to check
	 * @return true if the value1 value is a list, false if it is a single
	 *         value.
	 * 
	 */
	public static boolean isListFilterValue( FilterConditionHandle filter )
	{
		if ( filter == null )
			return false;

		if ( DesignChoiceConstants.FILTER_OPERATOR_IN.equals( filter
				.getOperator( ) ) )
			return true;

		if ( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN.equals( filter
				.getOperator( ) ) )
			return true;

		return false;

	}

	/**
	 * Determine if the value1 of this map rule condition is a list.
	 * 
	 * @param rule
	 *            the map rule need to check
	 * @return true if the value1 value is a list, false if it is a single
	 *         value.
	 * 
	 */
	public static boolean isListStyleRuleValue( StyleRuleHandle rule )
	{
		if ( rule == null )
			return false;

		if ( DesignChoiceConstants.MAP_OPERATOR_IN.equals( rule.getOperator( ) ) )
			return true;

		if ( DesignChoiceConstants.MAP_OPERATOR_NOT_IN.equals( rule
				.getOperator( ) ) )
			return true;

		return false;

	}

	/**
	 * Determine if the value1 of this filter condition is a list.
	 * 
	 * @param filter
	 *            the filter need to check
	 * @return true if the value1 value is a list, false if it is a single
	 *         value.
	 * 
	 */
	public static boolean isListFilterValue( FilterConditionElementHandle filter )
	{
		if ( filter == null )
			return false;

		if ( DesignChoiceConstants.FILTER_OPERATOR_IN.equals( filter
				.getOperator( ) ) )
			return true;

		if ( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN.equals( filter
				.getOperator( ) ) )
			return true;

		return false;

	}

	/**
	 * Convert param type to column data type.
	 * 
	 * @param type
	 * @return
	 */
	public static String convertParamTypeToColumnType( String type )
	{
		return DataTypeConversionUtil.converToColumnDataType( type );

	}

	/**
	 * Convert column data type to param type.
	 * 
	 * @param type
	 * @return
	 */

	public static String convertColumnTypeToParamType( String type )
	{
		return DataTypeConversionUtil.converToParamType( type );
	}

	/**
	 * Gets the script id if instance has expression.
	 * 
	 * @param instance
	 *            <code>PropertyHandle</code> which type or sub type should be
	 *            script or expression.
	 * @return the script uid if type or sub type is script or expression, null
	 *         if not or meet error.
	 */

	public static String getScriptUID( Object instance )
	{
		if ( isValidScript( instance ) )
			return XPathUtil.getXPath( instance );
		return null;
	}

	/**
	 * Gets the script id if instance has expression. Support list-value which
	 * sub type is expression.
	 * 
	 * @param instance
	 *            <code>PropertyHandle</code> which type or sub type should be
	 *            script or expression.
	 * @param index
	 *            index should be in valid range, should be more than zero and
	 *            less than list value size.
	 * @return the script uid if type or sub type is script or expression, null
	 *         if not or meet error.
	 */

	public static String getScriptUID( Object instance, int index )
	{
		if ( isValidScript( instance ) )
			return XPathUtil.getXPath( instance, index );
		return null;
	}

	/**
	 * Gets the script value.
	 * 
	 * @param module
	 *            module handle
	 * @param uid
	 *            the script uid
	 * @return the script value if script uid is valid;else return null.
	 */

	public static String getScript( ModuleHandle module, String uid )
	{
		Object instance = XPathUtil.getInstance( module, uid );
		if ( instance == null )
			return null;

		if ( instance instanceof String )
			return (String) instance;

		if ( isValidScript( instance ) )
			return ( (SimpleValueHandle) instance ).getStringValue( );
		return null;
	}

	/**
	 * Gets the script object .
	 * 
	 * @param module
	 *            module handle
	 * @param uid
	 *            the script uid
	 * @return the script object if script uid is valid;else return null.
	 */

	public static Object getScriptObject( ModuleHandle module, String uid )
	{
		Object instance = XPathUtil.getInstance( module, uid );

		if ( isValidScript( instance ) )
			return instance;

		return null;
	}

	/**
	 * Checks instance's type is script or not.
	 * 
	 * @param instance
	 *            <code>PropertyHandle</code> which type need to be checked.
	 * @return return true if it is script,else return false.
	 */

	private static boolean isValidScript( Object instance )
	{
		if ( instance instanceof PropertyHandle )
		{
			SimpleValueHandle temp = (SimpleValueHandle) instance;
			PropertyDefn defn = (PropertyDefn) temp.getDefn( );
			if ( defn.getTypeCode( ) == IPropertyType.LIST_TYPE )
			{
				if ( defn.getSubType( ).getTypeCode( ) == IPropertyType.EXPRESSION_TYPE )
					return true;
			}
			else
			{
				if ( defn.getTypeCode( ) == IPropertyType.EXPRESSION_TYPE
						|| defn.getTypeCode( ) == IPropertyType.SCRIPT_TYPE )
					return true;
			}
		}
		return false;

	}

	/**
	 * Returns the serialized id for the given element. The serialized id may or
	 * may not be equal to the element id. It is for the BIRT internal usage.
	 * 
	 * @param element
	 *            the element
	 * @return the serialized id of the given element
	 */

	public static long gerSerializedID( DesignElementHandle element )
	{
		if ( element == null )
			return DesignElement.NO_ID;

		if ( element instanceof MultiViewsHandle
				|| element.getContainer( ) instanceof MultiViewsHandle )
		{
			DesignElementHandle tmpContainer = element.getContainer( );
			if ( tmpContainer != null )
				return tmpContainer.getID( );
		}

		return element.getID( );
	}

	/**
	 * Gets the current version of the report files when users want to save it
	 * calling Model's related APIs.
	 * 
	 * @return the current version of the report files
	 */
	public static String getReportVersion( )
	{
		return DesignSchemaConstants.REPORT_VERSION;
	}

	/**
	 * Compares two specifies report version. 1 returned if the former of the
	 * two is greater than the latter, 0 if former equals to the latter and -1
	 * if the former smaller than the latter.
	 * 
	 * @param version1
	 * @param version2
	 * @return 1 returned if the former of the two is greater than the latter, 0
	 *         if former equals to the latter and -1 if the former smaller than
	 *         the latter
	 * @throws IllegalArgumentException
	 *             thrown if either of the two given version string is illegal
	 */
	public static int compareReportVersion( String version1, String version2 )
			throws IllegalArgumentException
	{
		int intVersion1 = VersionUtil.parseVersion( version1 );
		int intVersion2 = VersionUtil.parseVersion( version2 );
		return intVersion1 < intVersion2 ? -1 : ( intVersion1 == intVersion2
				? 0
				: 1 );
	}

	/**
	 * Checks whether a library with the specified file name is directly or
	 * indirectly included by the module. The given file name must be absolute.
	 * Method will not correctly handle the case if file name is relative.
	 * 
	 * @param moduleHandle
	 *            the module handle which to include the library
	 * @param fileName
	 *            the absolute file name of the library
	 * @return true if a library is found to be directly or indirectly included
	 *         by the module, otherwise false
	 */
	public static boolean isInclude( ModuleHandle moduleHandle, String fileName )
	{
		if ( moduleHandle == null || StringUtil.isBlank( fileName ) )
			return false;

		URL fileLocation = ModelUtil.getURLPresentation( fileName );

		// if fileLocation is null, return false
		if ( fileLocation == null )
			return false;

		return moduleHandle.getModule( ).getLibraryByLocation(
				fileLocation.toExternalForm( ), IAccessControl.ARBITARY_LEVEL ) == null
				? false
				: true;
	}

	/**
	 * Gets all the elements that is kind of the specified type. All the type
	 * should be the constants in {{@link ReportDesignConstants}.
	 * 
	 * @param moduleHandle
	 *            the module handle that the elements reside in
	 * @param elementType
	 *            the type of the elements to retrieve
	 * @return the list of the elements that is kind of the specified type
	 */
	public static List<DesignElementHandle> getElementsByType(
			ModuleHandle moduleHandle, String elementType )
	{
		if ( moduleHandle == null || StringUtil.isBlank( elementType ) )
			return Collections.emptyList( );
		IElementDefn elementDefn = MetaDataDictionary.getInstance( )
				.getElement( elementType );
		return getElementsByType( moduleHandle, elementDefn );
	}

	/**
	 * Gets all the elements that is kind of the specified type.
	 * 
	 * @param moduleHandle
	 *            the module handle that the elements reside in
	 * @param elementType
	 *            the type of the elements to retrieve
	 * @return the list of the elements that is kind of the specified type
	 */
	private static List<DesignElementHandle> getElementsByType(
			ModuleHandle moduleHandle, IElementDefn elementType )
	{
		if ( moduleHandle == null || elementType == null )
			return Collections.emptyList( );
		List<DesignElementHandle> retList = new ArrayList<DesignElementHandle>( );
		Module module = moduleHandle.getModule( );
		List<DesignElement> elements = module.getAllElements( );
		if ( elements != null )
		{
			for ( DesignElement element : elements )
			{
				if ( element != null
						&& element.getDefn( ).isKindOf( elementType ) )
					retList.add( element.getHandle( module ) );
			}
		}
		return retList;
	}
	
	/**
	 * Validates the given group name. Returned value is a valid one.
	 * @param groupHandle
	 * @param groupName
	 * @return
	 */
	public static String validteGroupName( GroupHandle groupHandle, String groupName )
	{
		return NamePropertyType.validateName( groupName );
	}
}
