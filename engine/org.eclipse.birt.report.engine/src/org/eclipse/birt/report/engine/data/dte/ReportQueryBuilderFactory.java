
package org.eclipse.birt.report.engine.data.dte;

import java.util.logging.Level;

import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import java.util.logging.Logger;

public class ReportQueryBuilderFactory
{
    
    private static Logger logger = Logger
            .getLogger( ReportQueryBuilderFactory.class.getName( ) );

    /**
     * static factory instance
     */
    static protected ReportQueryBuilderFactory instance;

    /**
     * get instance of factory
     * 
     * @return the factory instance
     */
    synchronized public static ReportQueryBuilderFactory getInstance( )
    {
        if ( instance == null )
        {
            instance = loadQueryBuilderExtension( );
            if ( instance == null )
            {
                instance = new ReportQueryBuilderFactory( );
            }
        }
        return instance;
    }
    
    private static ReportQueryBuilderFactory loadQueryBuilderExtension()
    {
        IExtensionRegistry registry = Platform.getExtensionRegistry( );
        IExtensionPoint extensionPoint = registry
                .getExtensionPoint( "org.eclipse.birt.core.FactoryService" );
        IExtension[] extensions = extensionPoint.getExtensions( );
        for ( IExtension extension : extensions )
        {
            IConfigurationElement[] elements = extension
                    .getConfigurationElements( );
            for ( IConfigurationElement element : elements )
            {
                String type = element.getAttribute( "type" );
                if ( "org.eclipse.birt.report.engine.data.querybuilderfactory"
                        .equals( type ) )
                {
                    try
                    {
                        Object factoryObject = element
                                .createExecutableExtension( "class" );
                        if ( factoryObject instanceof ReportQueryBuilderFactory )
                        {
                            return (ReportQueryBuilderFactory) factoryObject;
                        }
                    }
                    catch ( CoreException ex )
                    {
                        logger.log( Level.WARNING,
                                        "can not load the engine extension factory",
                                ex );
                    }
                }
            }
        }
        return null;
    }

    /**
     * create ReportQueryBuilders
     * @return
     */
    public ReportQueryBuilder createBuilder( Report report,
            ExecutionContext context, DataRequestSession dteSession )
    {
        return new ReportQueryBuilder( report, context, dteSession );
    }
}
