/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class SvgFile
{
	static boolean isSvg = false;
	private static Logger logger = Logger.getLogger( SvgFile.class.getName( ) );
	
	public static boolean isSvg(String uri)
	{
		if ( uri != null && uri.endsWith( ".svg" ) )
		{
			isSvg = true;
		}
		else isSvg = false;
		return isSvg;
	}
	
	public static boolean isSvg(String mimeType,String uri,String extension)
	{
		isSvg = ( ( mimeType != null ) && mimeType.equalsIgnoreCase( "image/svg+xml" ) ) //$NON-NLS-1$
				|| ( ( uri != null ) && uri.toLowerCase( )
						.endsWith( ".svg" ) ) //$NON-NLS-1$
				|| ( ( extension != null ) && extension.toLowerCase( ).endsWith(".svg" ) ); //$NON-NLS-1$
	     return isSvg;
	}
	
	public static byte[] transSvgToArray( String uri ) throws IOException
	{
		InputStream in = null;
		in = new URL( uri ).openStream( );
		return transSvgToArray( in );
	}

	public static byte[] transSvgToArray( InputStream inputStream )
	{
		try
		{
			JPEGTranscoder transcoder = new JPEGTranscoder( );
			transcoder.addTranscodingHint( JPEGTranscoder.KEY_QUALITY, new Float(
					.8 ) );
			ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
			TranscoderInput input = new TranscoderInput( getDocument( inputStream ) );
			TranscoderOutput output = new TranscoderOutput( ostream );
			transcoder.transcode( input, output );
			ostream.flush( );
			return ostream.toByteArray( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Failed to transform svg file to image.", e );
		}
		return new byte[0];
	}
	
	private static SVGDocument getDocument( InputStream in ) throws IOException, ParserConfigurationException, SAXException
	{
		SAXParserFactory saxFactory = SAXParserFactory.newInstance( );
		SAXParser saxParser = saxFactory.newSAXParser( );
		XMLReader reader = saxParser.getXMLReader( );
		String xmlReaderClassName = reader.getClass( ).getName( );
		SAXSVGDocumentFactory documentFactory = new SAXSVGDocumentFactory(
				xmlReaderClassName );
		return (SVGDocument) documentFactory.createDocument( null, in );
	}
}
