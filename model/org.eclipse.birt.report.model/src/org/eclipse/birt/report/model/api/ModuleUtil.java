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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.parser.ActionStructureState;
import org.eclipse.birt.report.model.parser.DesignReader;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.parser.GenericModuleReader;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.parser.ModuleParserErrorHandler;
import org.eclipse.birt.report.model.parser.ModuleParserHandler;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionInfo;
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

		public ActionParserHandler( DesignElement element )
		{
			super( null, null );
			this.element = element;
			module = new ReportDesign( null );

			setVersionNumber( DesignSchemaConstants.REPORT_VERSION_NUMBER );
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
					return new ActionStructureState( ActionParserHandler.this,
							element );
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

		// A fake element with the given action. Used to reuse the existing
		// action parser logic.

		ImageItem image = new ImageItem( );
		ActionParserHandler handler = new ActionParserHandler( image );

		if ( streamData == null )
		{
			Action action = StructureFactory.createAction( );
			image.setProperty( ImageHandle.ACTION_PROP, action );
			return ( (ImageHandle) image.getHandle( handler.getModule( ) ) )
					.getActionHandle( );
		}

		if ( !streamData.markSupported( ) )
			streamData = new BufferedInputStream( streamData );

		assert streamData.markSupported( );
		parse( handler, streamData, "" ); //$NON-NLS-1$

		ImageHandle imageHandle = (ImageHandle) image.getHandle( handler
				.getModule( ) );
		return imageHandle.getActionHandle( );
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
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance( );
			SAXParser parser = saxParserFactory.newSAXParser( );
			InputSource inputSource = new InputSource( streamData );
			inputSource.setEncoding( UnicodeUtil.SIGNATURE_UTF_8 );
			parser.parse( inputSource, handler );
		}
		catch ( SAXException e )
		{
			List errors = handler.getErrorHandler( ).getErrors( );

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
	 * Deserialize a string into an ActionHandle, notice that the handle is
	 * faked, the action is not in the design tree, the operation to the handle
	 * is not able to be undoned.
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
		return deserializeAction( is );
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
			writeAction( action, ImageItem.ACTION_PROP );
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
	 * design/library, return <code>ModuleUtil.REPORT_DESIGN</code>/<code>ModuleUtil.LIBRARY</code>,
	 * otherwise, <code>ModuleUtil.INVALID</code> is return.
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

		String version = null;

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
	 *         instance of <code>IVersionInfo</code>. If the size of the
	 *         return list is 0, there is no auto-conversion.
	 */

	private static List checkVersion( InputStream streamData, String filename )
			throws DesignFileException
	{
		VersionParserHandler handler = new VersionParserHandler( );

		InputStream inputStreamToParse = streamData;
		if ( !inputStreamToParse.markSupported( ) )
			inputStreamToParse = new BufferedInputStream( streamData );

		parse( handler, inputStreamToParse, filename );

		return ModelUtil.checkVersion( handler.version );
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
			url = new URL( fileName );
			inputStream = url.openStream( );
		}
		catch ( MalformedURLException e2 )
		{
			// do nothing
		}
		catch ( IOException e )
		{
			rtnList
					.add( new VersionInfo( null,
							VersionInfo.INVALID_DESIGN_FILE ) );
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
			rtnList
					.add( new VersionInfo( null,
							VersionInfo.INVALID_DESIGN_FILE ) );
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

}
