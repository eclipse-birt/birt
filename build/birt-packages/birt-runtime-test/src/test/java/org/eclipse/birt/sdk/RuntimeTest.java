
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
import org.junit.Test;

public class RuntimeTest
{

    private Class mainClass;

    public int run( String[] args ) throws Exception
    {
        if ( mainClass == null )
        {
            ClassLoader loader = createClassLoader( "./target/birt-runtime/ReportEngine/lib" );
            mainClass = loader.loadClass( "org.eclipse.birt.report.engine.api.ReportRunner" );
        }
        Constructor constructor = mainClass.getConstructor( String[].class );
        Object runner = constructor.newInstance( new Object[]{args} );
        Method execute = mainClass.getMethod( "execute", null );
        Object result = execute.invoke( runner, null );
        return ( (Integer) result ).intValue( );
    }

    @Test
    public void testMain( ) throws Exception
    {
        String output = "./target/output.html";
        new File( output ).delete( );
        int result = run( new String[]{"-o", output, "-m", "RunAndRender",
                "./target/birt-runtime/ReportEngine/samples/hello_world.rptdesign"} );
        Assert.assertEquals( 0, result );
        Assert.assertTrue( new File( output ).exists( ) );
        Assert.assertTrue( new String( Files.readAllBytes( Paths.get( output ) ), StandardCharsets.UTF_8 )
                .contains( "If you can see this report, it means that the BIRT viewer is installed correctly." ) );
    }

    @Test
    public void testTable( ) throws Exception
    {
        String output = "./target/table.html";
        new File( output ).delete( );
        int result = run( new String[]{"-o", output, "-m", "RunAndRender", "./src/test/resources/table.rptdesign"} );
        Assert.assertEquals( 0, result );
        Assert.assertTrue( new File( output ).exists( ) );
        //USA's customer count is 36
        Assert.assertTrue(
                new String( Files.readAllBytes( Paths.get( output ) ), StandardCharsets.UTF_8 ).contains( "36" ) );
    }

    @Test
    public void testXtab( ) throws Exception
    {
        String output = "./target/xtab.html";
        new File( output ).delete( );
        int result = run( new String[]{"-o", output, "-m", "RunAndRender", "./src/test/resources/xtab.rptdesign"} );
        Assert.assertEquals( 0, result );
        Assert.assertTrue( new File( output ).exists( ) );
        //USA's customer count is 36
        Assert.assertTrue(
                new String( Files.readAllBytes( Paths.get( output ) ), StandardCharsets.UTF_8 ).contains( "36" ) );
    }

    @Test
    public void testChart( ) throws Exception
    {
        String output = "./target/chart.html";
        new File( output ).delete( );
        int result = run( new String[]{"-o", output, "-m", "RunAndRender", "./src/test/resources/chart.rptdesign"} );
        Assert.assertEquals( 0, result );
        Assert.assertTrue( new File( output ).exists( ) );
        //there is a svg image output as type="image/svg+xml"
        Assert.assertTrue( new String( Files.readAllBytes( Paths.get( output ) ), StandardCharsets.UTF_8 )
                .contains( "image/svg+xml" ) );
    }

    private File[] listJars( String folder )
    {
        return new File( folder ).listFiles( new FilenameFilter( ) {

            @Override
            public boolean accept( File dir, String name )
            {
                if ( name.endsWith( ".jar" ) )
                {
                    return true;
                }
                return false;
            }

        } );
    }

    private ClassLoader createClassLoader( String folder ) throws MalformedURLException
    {
        File[] jarFiles = listJars( folder );
        URL[] urls = new URL[jarFiles.length];
        for ( int i = 0; i < jarFiles.length; i++ )
        {
            urls[i] = jarFiles[i].toURI( ).toURL( );
        }
        return new URLClassLoader( urls );
    }
}
