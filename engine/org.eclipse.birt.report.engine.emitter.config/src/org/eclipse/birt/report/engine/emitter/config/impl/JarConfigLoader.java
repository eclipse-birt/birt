
package org.eclipse.birt.report.engine.emitter.config.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.emitter.config.IDefaultConfigLoader;
import org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.RenderOptionDefn;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * loads the config from emitter.config.jar
 */
public class JarConfigLoader implements IDefaultConfigLoader
{

	private static final String OPTIONS_CONFIG_FILE = "RenderDefaults.cfg";

	private static final String RENDER_OPTIONS_FILE = "RenderOptions.xml";

	private static final String OPTION_QNAME = "option";

	private static final String OPTION_NAME = "name";

	private static final String OPTION_DEFAULT = "default";

	private static final String OPTION_ENABLED = "enabled";

	protected Map<String, RenderOptionDefn> options = new HashMap<String, RenderOptionDefn>( );

	public Map<String, RenderOptionDefn> loadConfigFor( String bundleName,
			IEmitterDescriptor descriptor )
	{
		options.clear( );
		try
		{
			parseConfigFor( bundleName );
		}
		catch ( Exception e )
		{

		}
		return options;
	}

	public int getPriority( )
	{
		return 0;
	}

	protected void parseConfigFor( String bundleName ) throws Exception
	{
		// first, load .cfg file
		loadCfgFile( getResourceURL( bundleName, OPTIONS_CONFIG_FILE ) );
		// then load .xml file, this overrides the .cfg file
		loadXMLFile( bundleName, RENDER_OPTIONS_FILE );
	}

	protected DefaultHandler getHandler( )
	{
		return new RenderOptionHandler( );
	}

	protected void loadCfgFile( URL url ) throws Exception
	{
		if ( url != null )
		{
			InputStream in = url.openStream( );
			Properties defaultValues = new Properties( );
			defaultValues.load( in );
			for ( Entry<Object, Object> entry : defaultValues.entrySet( ) )
			{
				String name = entry.getKey( ).toString( );
				String value = entry.getValue( ).toString( );
				options.put( name, new RenderOptionDefn( name, value, true ) );
			}
			in.close( );
		}
	}

	protected void loadXMLFile( String bundleName, String fileName )
			throws Exception
	{
		URL url = getResourceURL( bundleName, fileName );
		if ( url != null )
		{
			InputStream in = url.openStream( );
			parseConfigXML( in );
			in.close( );
		}
	}

	private void parseConfigXML( InputStream in ) throws Exception
	{

		SAXParser parser = SAXParserFactory.newInstance( ).newSAXParser( );
		try
		{
			parser.parse( in, getHandler( ) );
		}
		finally
		{
			// even there is XML exception, need to release the resource.
			try
			{
				parser.reset( );
				parser = null;
			}
			catch ( Exception e1 )
			{

			}
		}
	}

	private URL getResourceURL( String bundleName, String resourceName )
	{
		IBundle bundle = Platform.getBundle( bundleName ); //$NON-NLS-1$
		if ( bundle != null )
		{
			return bundle.getEntry( resourceName );
		}
		return null;
	}

	protected class RenderOptionHandler extends DefaultHandler
	{

		@Override
		public void startElement( String uri, String localName, String qName,
				Attributes attributes ) throws SAXException
		{
			if ( OPTION_QNAME.equalsIgnoreCase( qName ) )
			{
				String name = attributes.getValue( OPTION_NAME );
				if ( !isEmpty( name ) )
				{
					String defualt = attributes.getValue( OPTION_DEFAULT );
					Boolean enabled = Boolean.TRUE;
					String enabledStr = attributes.getValue( OPTION_ENABLED );
					if ( !isEmpty( enabledStr ) )
					{
						enabled = Boolean.valueOf( enabledStr );
					}
					options.put( name, new RenderOptionDefn( name,
							defualt,
							enabled ) );
				}
			}
		}

		protected boolean isEmpty( String str )
		{
			return str == null || str.length( ) == 0;
		}
	}
}
