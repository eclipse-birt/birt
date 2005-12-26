/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.xml.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This instance interacts with a SaxParserConsumer instance to populate
 * the ResultSet data.
 * 
 */
public class SaxParser extends DefaultHandler implements Runnable
{
	//The xml file being proceed.
	private String xmlFile;
	
	//The XPathHolder instance that hold the information of element currently
	//being proceed
	private XPathHolder pathHolder;
	
	//The ISaxParserConsumer instance that servers as middle-man between
	//ResultSet and SaxParser.
	private ISaxParserConsumer spConsumer;
	
	//This HashMap records the occurance of element being proceed.
	private HashMap currentElementRecoder;
	
	//The boolean indicates that whether the parsing has started.
	boolean start;
	
	//The boolean indicates that whether the parsing thread is alive or not.
	boolean alive;

	/**
	 * 
	 * @param fileName
	 * @param consumer
	 */
	public SaxParser( String fileName, ISaxParserConsumer consumer )
	{
		xmlFile = fileName;
		spConsumer = consumer;
		start = true;
		alive = true;
		currentElementRecoder = new HashMap();
	}

	/*
	 *  (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run( )
	{
		XMLReader xr;
		try
		{
			// xr = XMLReaderFactory.createXMLReader(
			// "org.apache.xerces.parsers.SAXParser" );
		
			xr = new SAXParser( );
			xr.setContentHandler( this );
			xr.setErrorHandler( this );

			URL url = null;
			Reader file = null;
			//First try to parse the input string as file name.
			try 
			{
				File f = new File(xmlFile);
				url = f.toURL();
				file = new InputStreamReader( url.openStream() );
			}
			catch ( IOException e )
			{
				url = null;
			} 
			
			//Then try to parse the input string as a url in web.
			if ( url == null )
			{
				url = new URL( xmlFile );
			}
		
			InputStream is = getInputStream( url );
			file = new InputStreamReader( is );
				
			xr.parse( new InputSource( file ) );				
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e.getLocalizedMessage());
		}
		finally
		{
			this.alive = false;
			spConsumer.wakeup( );
		}
	}

	/**
	 * This method remove the microsoft utf bom from the input stream, if any.
	 * 
	 * @param is
	 * @return 
	 * @throws IOException
	 */
	private InputStream getInputStream( URL url ) throws IOException
	{
		InputStream is = url.openStream();
		byte[] buff = new byte[3];
		is.read( buff );
		//The UTF8BOM will add three bytes, -17,-69,-65 to the header of a file.
		boolean isUTF8BOM = (buff[0] == -17 && buff[1] == -69 && buff[2]== -65);
	//	boolean isUTF16BOM = (buff[0] == -1 && buff[1] == -2)||(buff[0] == -2&&buff[1] == -1);
	//	if(isUTF16BOM)
	//	{
	//		is = url.openStream();
	//		byte[] b = new byte[10];
	//		
	//		is.read( b );
	//		b = null;
	//	}else 
		if ( !isUTF8BOM )
		{
			is = url.openStream();
		}
		return is;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument( )
	{
		pathHolder = new XPathHolder( );

	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument( )
	{
		this.alive = false;
		this.spConsumer.wakeup();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String uri, String name, String qName,
			Attributes atts )
	{

		String elementName = getElementName( uri, qName, name );
		String parentPath = pathHolder.getPath();
		//Record the occurance of elements
		if(this.currentElementRecoder.get(parentPath+Constants.XPATH_SLASH+elementName)==null)
		{
			this.currentElementRecoder.put(parentPath+Constants.XPATH_SLASH+elementName,new Integer(1));
		}else
		{
			this.currentElementRecoder.put(parentPath+Constants.XPATH_SLASH+elementName, new Integer(((Integer)this.currentElementRecoder.get(parentPath+Constants.XPATH_SLASH+elementName)).intValue()+1 )); 
		}
		pathHolder.push( elementName+"["+((Integer)this.currentElementRecoder.get(parentPath+Constants.XPATH_SLASH+elementName)).intValue()+"]" );
		
		for ( int i = 0; i < atts.getLength( ); i++ )
		{
			spConsumer.manipulateData( getAttributePath( atts, i ), atts.getValue( i ) );
			spConsumer.detectNewRow( getAttributePath( atts, i ) );
		}
	}

	/**
	 * Build the xpath of an attribute.
	 * 
	 * @param atts
	 * @param i
	 * @return
	 */
	private String getAttributePath( Attributes atts, int i )
	{
		return pathHolder.getPath( )
				+ "[@"
				+ getElementName( atts.getURI( i ),
						atts.getQName( i ),
						atts.getLocalName( i ) )+"]";
	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement( String uri, String localName, String qName )
			throws SAXException
	{
		spConsumer.detectNewRow( pathHolder.getPath( ) );
		//	this.currentElementRecoder.clear();
		
		String path = pathHolder.getPath();
		Object[] keys = this.currentElementRecoder.keySet().toArray();
		for(int i= 0; i < keys.length&&path!=""; i++)
		{
			if (keys[i].toString().startsWith(path)&&(!keys[i].toString().equals(path)))
			{
				this.currentElementRecoder.remove(keys[i]);
			}
		}
		pathHolder.pop( );
	}

	/**
	 * Get the elementName
	 * 
	 * @param uri
	 * @param qName
	 * @param name
	 * @return
	 */
	private String getElementName( String uri, String qName, String name )
	{
		//if ( "".equals( uri ) )
			return qName;
		//else
		//	return "["+ uri.replaceAll("\\Q\\\\E","/")+ "]" + name;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters( char ch[], int start, int length )
	{
		String s = "";
		for ( int i = 0; i < length; i++ )
		{
			s = s + ch[start + i];
		}
		spConsumer.manipulateData( pathHolder.getPath( ), s );		
	}

	/**
	 * Set the status of current thread, might either be "started" or "suspended"
	 * @param start
	 */
	public void setStart( boolean start )
	{
		this.start = start;
		if ( start )
		{
			synchronized ( this )
			{
				notify( );
			
			}
		}else
		{
			synchronized ( this )
			{
				try
				{
					spConsumer.wakeup();
					wait( );
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
			
			}
		}
	}

	/**
	 * Return whether the thread that host the SaxParser is suspended.
	 * @return
	 */
	public boolean isSuspended( )
	{
		return !start;
	}

	/**
	 * Return whether the thread that host the SaxParser is alive or destoried.
	 * 
	 * @return
	 */
	public boolean isAlive( )
	{
		return this.alive;
	}
}

/**
 * The instance of this class is used to populate the Xpath expression of 
 * current XML path.
 * 
 */
class XPathHolder
{
	private Vector holder;

	public XPathHolder( )
	{
		holder = new Vector( );
	}

	/**
	 * Get the path string according to the current status of XPathHolder instance.
	 * @return
	 */
	public String getPath( )
	{
		String result = "";
		Iterator it = holder.iterator( );
		while ( it.hasNext( ) )
		{
			result = result	+ "/" + (String) it.next( );
		}
		return result;
	}

	/**
	 * Pop a value from stack.
	 *
	 */
	public void pop( )
	{
		holder.remove( holder.size( ) - 1 );
	}

	/**
	 * Push a value to stack.
	 * 
	 * @param path
	 */
	public void push( String path )
	{
		holder.add( path );
	}
}