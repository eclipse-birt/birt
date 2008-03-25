
package org.eclipse.birt.data.aggregation.plugin;

import java.io.File;

import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.data.aggregation.impl.TempDir;
import org.osgi.framework.BundleContext;

public class AggregationPlugin extends BIRTPlugin
{	
	@Override
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
		String tempDir = System.getProperty( "java.io.tmpdir" )
				+ "AggregationPlugin_" + this.getBundle( ).hashCode( ) + File.separator;
		TempDir.createInstance( tempDir );
	}

	@Override
	public void stop( BundleContext context ) throws Exception
	{
		TempDir.release( );
		super.stop( context );
	}
}
