
package org.eclipse.birt.report.engine.emitter.config.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
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

    private static final Logger logger = Logger
            .getLogger( JarConfigLoader.class.getName( ) );

    private static final String OPTIONS_CONFIG_FILE = "RenderDefaults.cfg";

    private static final String RENDER_OPTIONS_FILE = "RenderOptions.xml";

    private static final String EMITTER_QNAME = "emitter";

    private static final String OPTION_QNAME = "option";

    private static final String OPTION_NAME = "name";

    private static final String OPTION_DEFAULT = "default";

    private static final String OPTION_ENABLED = "enabled";

    public Map<String, RenderOptionDefn> loadConfigFor(
            final String bundleName, final IEmitterDescriptor descriptor )
    {

        final Map<String, RenderOptionDefn> options = new HashMap<String, RenderOptionDefn>( );
        try
        {

            parseConfigFor( bundleName, descriptor, options );
        }
        catch ( Exception e )
        {
            logger.log( Level.WARNING, "fail to parser config", e );
        }
        return options;
    }

    public int getPriority( )
    {
        return 0;
    }

    protected void parseConfigFor( final String bundleName,
            final IEmitterDescriptor descriptor,
            final Map<String, RenderOptionDefn> options ) throws Exception
    {
        // first, load .cfg file
        loadCfgFile( getResourceURL( bundleName, OPTIONS_CONFIG_FILE ),
                descriptor, options );
        // then load .xml file, this overrides the .cfg file
        loadXMLFile( bundleName, RENDER_OPTIONS_FILE, descriptor, options );
    }

    private void loadCfgFile( final URL url,
            final IEmitterDescriptor descriptor,
            final Map<String, RenderOptionDefn> options ) throws Exception
    {
        if ( url != null )
        {
            final InputStream in = url.openStream( );
            final Properties defaultValues = new Properties( );
            defaultValues.load( in );
            for ( Entry<Object, Object> entry : defaultValues.entrySet( ) )
            {
                final String name = entry.getKey( ).toString( );
                final String value = entry.getValue( ).toString( );
                options.put( name, new RenderOptionDefn( name, value, true ) );
            }
            in.close( );
        }
    }

    protected void loadXMLFile( final String bundleName, final String fileName,
            final IEmitterDescriptor descriptor,
            final Map<String, RenderOptionDefn> options ) throws Exception
    {
        final URL url = getResourceURL( bundleName, fileName );
        if ( url != null )
        {
            final InputStream in = url.openStream( );
            parseConfigXML( in, descriptor, options );
            in.close( );
        }
    }

    private void parseConfigXML( final InputStream in,
            final IEmitterDescriptor descriptor,
            final Map<String, RenderOptionDefn> options ) throws Exception
    {

        final SAXParser parser = SAXParserFactory.newInstance( ).newSAXParser( );
        try
        {
            parser.parse( in, new RenderOptionHandler( descriptor, options ) );
        }
        finally
        {
            // even there is XML exception, need to release the resource.
            try
            {
                parser.reset( );
            }
            catch ( Exception e1 )
            {
                logger.log( Level.WARNING, "failed to parse config", e1 );
            }
        }
    }

    private URL getResourceURL( final String bundleName,
            final String resourceName )
    {
        final IBundle bundle = Platform.getBundle( bundleName ); //$NON-NLS-1$
        if ( bundle != null )
        {
            return bundle.getEntry( resourceName );
        }
        return null;
    }

    private class RenderOptionHandler extends DefaultHandler
    {

        private IEmitterDescriptor descriptor;
        private Map<String, RenderOptionDefn> options;

        RenderOptionHandler( IEmitterDescriptor descriptor,
                Map<String, RenderOptionDefn> options )
        {
            this.descriptor = descriptor;
            this.options = options;
        }

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
                    options.put( name, new RenderOptionDefn( name, defualt,
                            enabled ) );
                }
            }
            if ( EMITTER_QNAME.equalsIgnoreCase( qName ) )
            {
                String enabledStr = attributes.getValue( OPTION_ENABLED );
                if ( !isEmpty( enabledStr ) )
                {
                    ( (AbstractEmitterDescriptor) descriptor )
                            .setEnabled( Boolean.valueOf( enabledStr ) );
                }
            }
        }

        private boolean isEmpty( String str )
        {
            return str == null || str.length( ) == 0;
        }
    }
}
