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
	private XMLDataInputStream inputStream;
	
	//The XPathHolder instance that hold the information of element currently
	//being proceed
	private XPathHolder pathHolder;
	
	//The ISaxParserConsumer instance that servers as middle-man between
	//ResultSet and SaxParser.
	private ISaxParserConsumer spConsumer;
	
	//This HashMap records the occurance of element being proceed.
	private HashMap currentElementRecoder;
	
	//The boolean indicates that whether the parsing has started.
	private boolean start;
	
	//The boolean indicates that whether the parsing thread is alive or not.
	private boolean alive;
	
    /*	We will override method	org.xml.sax.helpers.DefaultHandler.characters(char[], int start, int length) to
	rechieve value of an xml element.

	In the Xerces2 Java Parser 2.6.2 implementation (the one we used), the
	first argument, that is, char[], which is a cache of xml input stream, passed
	by the Xerces parser would always be of 2048 bytes in length. If a value of an
	xml element exceeds 2048 bytes, or only parts of its value being cached on the
	rear of the char array, then the method characters() will be called multiple
	times so that the whole value could be achieved.

	Based on the above consideration, we decide to cache the chars fetched from the 
	characters method and proceed them when endDocument method is called */
	private String currentCacheValue;

	private boolean stopCurrentThread;
	/**
	 * 
	 * @param fileName
	 * @param consumer
	 */
	public SaxParser( XMLDataInputStream stream, ISaxParserConsumer consumer )
	{
		inputStream = stream;
		spConsumer = consumer;
		start = true;
		alive = true;
		currentCacheValue = "";
		currentElementRecoder = new HashMap();
		stopCurrentThread = false;
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
			xr = new SAXParser( );
			xr.setContentHandler( this );
			xr.setErrorHandler( this );

			try{
				xr.parse( new InputSource( this.inputStream ) );
			}catch ( ThreadStopException tsE )
			{
				//This exception is thrown out to stop the execution of current
				//thread.
				tsE.printStackTrace();
			}

			this.inputStream.reStart();
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
		//If the current thread should be stopped and current parsing should not continue any more, then
		//throw a ThreadStopException so that it can be catched later in run method to stop the current thread
		//execution.
		if( this.stopCurrentThread )
			throw new ThreadStopException();
		
		String elementName = getElementName( uri, qName, name );
		String parentPath = pathHolder.getPath();
		//Record the occurance of elements
		if(this.currentElementRecoder.get(parentPath+UtilConstants.XPATH_SLASH+elementName)==null)
		{
			this.currentElementRecoder.put(parentPath+UtilConstants.XPATH_SLASH+elementName,new Integer(1));
		}else
		{
			this.currentElementRecoder.put(parentPath+UtilConstants.XPATH_SLASH+elementName, new Integer(((Integer)this.currentElementRecoder.get(parentPath+UtilConstants.XPATH_SLASH+elementName)).intValue()+1 )); 
		}
		pathHolder.push( elementName+"["+((Integer)this.currentElementRecoder.get(parentPath+UtilConstants.XPATH_SLASH+elementName)).intValue()+"]" );
		spConsumer.detectNewRow( pathHolder.getPath( ), true );
		for ( int i = 0; i < atts.getLength( ); i++ )
		{
			spConsumer.manipulateData( getAttributePath( atts, i ), atts.getValue( i ) );
			spConsumer.detectNewRow( getAttributePath( atts, i ),true );
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
		//Manipulate the data. The currentCacheValue is trimed to delimite
		//the heading and tailing junk spaces.
		spConsumer.manipulateData( pathHolder.getPath( ), this.currentCacheValue.trim() );
		this.currentCacheValue = "";
		spConsumer.detectNewRow( pathHolder.getPath( ), false );
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
		for ( int i = 0; i < length; i++ )
		{
			this.currentCacheValue = this.currentCacheValue + ch[start + i];
		}	
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
	 * Set the member data stopCurrentThread to "true" value so that the current thread can be stopped afterwise.
	 *
	 */
	public void stopParsing()
	{
		this.stopCurrentThread = true;
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
	
	/**
	 * This class wrapps a RuntimeException. It is used to stop the execution of current thread.
	 */
	private class ThreadStopException extends RuntimeException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7871277314833138093L;

		ThreadStopException(){}
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