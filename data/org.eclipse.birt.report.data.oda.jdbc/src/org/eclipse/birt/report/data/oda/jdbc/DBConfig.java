package org.eclipse.birt.report.data.oda.jdbc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;



/**
 * This class fetch the DBConfig from config.xml file.
 * 
 *
 */
public class DBConfig
{
	private static final String CONFIG_XML = "config.xml";
	public static final int NORMAL = 0;
	public static final int EXEC_QUERY_AND_CACHE = 1;
	public static final int EXEC_QUERY_WITHOUT_CACHE = 2;
	public static final int DEFAULT_POLICY = -1;
	public static final int IGNORE_UNIMPORTANT_EXCEPTION = 3;
	public static final int TRY_COMMIT_THEN_CLOSE = 4;
	public static final int SET_COMMIT_TO_FALSE = 5;
	private HashMap<Integer, Set<String>> driverPolicy = null;
	private volatile static DBConfig config = null;

	public static DBConfig getInstance( )
	{
		if( config == null )
		{
			synchronized(DBConfig.class)
			{
				if( config == null )
					config =  new DBConfig();
			}
		}
		return config;
	}
	
	//
	DBConfig()
	{
		driverPolicy = new HashMap<Integer, Set<String>>();
		new SaxParser( this ).parse( );				
	}
	
	/**
	 * 
	 * @param driverName
	 * @return
	 */
	public boolean qualifyPolicy( String driverName, int policyNumber )
	{
		if( driverName == null )
			return false;
		Set<String> policySet = driverPolicy.get( policyNumber );
		if( policySet == null )
			return false;
		return policySet.contains( driverName.toUpperCase() );
	}
	
	/**
	 * 
	 * @param driverName
	 * @param policy
	 */
	public void putPolicy( String driverName, int policy )
	{
		if( driverName == null )
			return;
		if( !driverPolicy.containsKey(policy))
		{
			driverPolicy.put( policy, new HashSet<String>());	
		}
		driverPolicy.get(policy).add(driverName.toUpperCase());
	}
	
	/**
	 * 
	 * @return
	 */
	public URL getConfigURL()
	{
		URL u = this.getClass( ).getResource( CONFIG_XML );
		return u;
	}
	
}

/**
 * 
 * @author Administrator
 *
 */
class SaxParser extends DefaultHandler
{
	//
	private static final String TYPE = "type";
	private static final String POLICY = "Policy";
	private static final String NAME = "name";
	private static final String DRIVER = "Driver";
	private int currentPolicy = DBConfig.DEFAULT_POLICY;
	private DBConfig dbConfig;
	
	/**
	 * Constructor
	 * @param config
	 */
	public SaxParser( DBConfig config )
	{
		this.dbConfig = config;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String uri, String name, String qName,
			Attributes atts )
	{
		String elementName = qName;
		if ( elementName.equals( DRIVER ) )
		{
			dbConfig.putPolicy( atts.getValue( NAME ),
					currentPolicy );
		}
		else if ( elementName.equals( POLICY ) )
		{
			String type = atts.getValue( TYPE );
			try
			{
				currentPolicy = Integer.parseInt( type );
			}
			catch ( NumberFormatException e )
			{
				currentPolicy = DBConfig.DEFAULT_POLICY;
			}
		} 
	}
	
	/**
	 * 
	 */
	public void parse()
	{
		Object xmlReader;
		try
		{
			if( this.dbConfig.getConfigURL( ) == null )
				return;
			xmlReader = createXMLReader( );

			setContentHandler( xmlReader );

			setErrorHandler( xmlReader );

			parse( xmlReader );

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param xmlReader
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void parse( Object xmlReader ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		Method parse = this.getMethod( "parse",
				xmlReader.getClass( ),
				new Class[]{
					InputSource.class
				} );
		InputStream is;
		try
		{
			is = new BufferedInputStream( this.dbConfig.getConfigURL( )
					.openStream( ) );
			InputSource source = new InputSource( is );
			source.setEncoding( source.getEncoding( ) );
			parse.invoke( xmlReader, new Object[]{
				source
			} );
			is.close( );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

	}

	/**
	 * 
	 * @param xmlReader
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void setErrorHandler( Object xmlReader ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		Method setErrorHandler = this.getMethod( "setErrorHandler",
				xmlReader.getClass( ),
				new Class[]{
					ErrorHandler.class
				} );
		this.invokeMethod( setErrorHandler, xmlReader, new Object[]{this} );
	}

	/**
	 * 
	 * @param xmlReader
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void setContentHandler( Object xmlReader ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		Method setContentHandler = this.getMethod( "setContentHandler",
				xmlReader.getClass( ),
				new Class[]{
					ContentHandler.class
				} );
		
		this.invokeMethod( setContentHandler, xmlReader, new Object[]{
				this
			} );
	}

	/**
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private Object createXMLReader( ) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		try
		{
			Object xmlReader = Thread.currentThread( )
					.getContextClassLoader( )
					.loadClass( "org.apache.xerces.parsers.SAXParser" )
					.newInstance( );
			return xmlReader;
		}
		catch ( ClassNotFoundException e )
		{
			return Class.forName( "org.apache.xerces.parsers.SAXParser" )
					.newInstance( );
		}

	}

	/**
	 * Return a method using reflect.
	 * 
	 * @param methodName
	 * @param targetClass
	 * @param argument
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private Method getMethod(String methodName, Class targetClass, Class[] argument) throws SecurityException, NoSuchMethodException
	{
		assert methodName != null;
		assert targetClass != null;
		assert argument != null;
		
		return targetClass.getMethod( methodName, argument );
	}
	
	/**
	 * Invoke a method.
	 * 
	 * @param method
	 * @param targetObject
	 * @param argument
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void invokeMethod( Method method, Object targetObject, Object[] argument ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		method.invoke( targetObject, argument );
	}
}