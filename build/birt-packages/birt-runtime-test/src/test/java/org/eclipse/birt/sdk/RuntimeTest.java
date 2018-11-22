
package org.eclipse.birt.sdk;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class RuntimeTest extends BaseTestTemplate
{
	
    public int run( String[] args ) throws Exception
    {    
    	if ( mainClass == null )
    	{
	        System.clearProperty( "BIRT_HOME" );
	        loader = createClassLoader( "./target/birt-runtime/ReportEngine/lib" ); //$NON-NLS-1$
	        mainClass = loader.loadClass( "org.eclipse.birt.report.engine.api.ReportRunner" );  //$NON-NLS-1$
    	}
        Constructor constructor = mainClass.getConstructor( String[].class );
        Object runner = constructor.newInstance( new Object[]{args} );
        Method execute = mainClass.getMethod( "execute", null ); 
        Object result = execute.invoke( runner, null );
        return ( (Integer) result ).intValue( );
    }

}
