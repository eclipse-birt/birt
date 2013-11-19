/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers.URILocator;


public final class ResourceLocator
{

	private static Logger logger = Logger.getLogger( ResourceLocator.class.getName( ) );
	

	public static void resolveConnectionProperties(
			Properties connectionProperties, String driverClass, Map appContext )
			throws OdaException
	{
		JDBCDriverInformation info = JDBCDriverInfoManager.getInstance( )
				.getDriversInfo( driverClass );

		if ( info != null )
		{
			List<PropertyGroup> group = info.getPropertyGroup( );
			for ( int i = 0; i < group.size( ); i++ )
			{
				List<PropertyElement> elements = group.get( i ).getProperties( );

				for ( int j = 0; j < elements.size( ); j++ )
				{
					String propertyName = elements.get( j )
							.getAttribute( DriverInfoConstants.DRIVER_INFO_PROPERTY_NAME );

					if ( connectionProperties.containsKey( propertyName ) )
					{
						String type = elements.get( j )
								.getAttribute( DriverInfoConstants.DRIVER_INFO_PROPERTY_TYPE );
						if ( DriverInfoConstants.DRIVER_INFO_PROPERTY_TYPE_RESOURCE.equals( type ) )
						{
							String path = ResourceLocator.resolveResource( connectionProperties.getProperty( propertyName ),
									appContext );
							connectionProperties.setProperty( propertyName,
									path );
						}
					}
				}
			}
		}
	}

	public static String resolveResource( String location, Map appContext ) throws OdaException
	{
		String absolutePath = null;
		if ( location != null )
		{
			File docFile = null;
			if ( appContext == null )
			{
				logger.warning( "No ResourceIdentifiers instance is provided from appContext" ); //$NON-NLS-1$
				absolutePath = location;
			}
			else if ( ( new File( location ) ).isAbsolute( ) )
				absolutePath = location;
			else
			{
				Object obj = appContext.get( ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS );
				if ( obj != null )
				{
					try
					{
						absolutePath = getResourcePath( obj,
								new URI( encode( location ) ) );
					}
					catch ( URISyntaxException e )
					{
						logger.log( Level.WARNING, "Failed to resolve path", e ); //$NON-NLS-1$
						absolutePath = location;
					}
				}
				else
				{
					logger.warning( "No ResourceIdentifiers instance is provided from appContext" ); //$NON-NLS-1$
					absolutePath = location;
				}
			}

			if ( absolutePath == null )
			{
				logger.logp( java.util.logging.Level.SEVERE,
						location,
						"open",
						"cannot find file under location " + location );
				throw new OdaException( "cannot find file " + location );
			}

			docFile = new File( absolutePath );
			if ( docFile == null || !docFile.exists( ) )
			{
				throw new OdaException( "cannot find file under location "
						+ absolutePath );
			}
		}
		return absolutePath;
	}
	
	/**
	 * Acquire the resource path.
	 * @param resourceIdentifiersObj
	 * @return
	 * @throws OdaException 
	 */
	private static String getResourcePath( Object resourceIdentifiersObj, URI path ) throws OdaException
	{
	    if( resourceIdentifiersObj == null )
	        return null;
	    
	    if ( resourceIdentifiersObj instanceof ResourceIdentifiers )
		{
			URILocator appLocator = ( (ResourceIdentifiers) resourceIdentifiersObj ).getApplResourceURILocator( );
			URILocator designLocator = ( (ResourceIdentifiers) resourceIdentifiersObj ).getDesignResourceURILocator( );
			if ( appLocator == null && designLocator == null )
			{
				throw new OdaException( "cannot find resource identifier" );
			}
			URI target = null;
			if ( appLocator != null )
			{
				target = appLocator.resolve( path );
			}
			if ( target == null )
			{
				target = designLocator.resolve( path );
			}
			if ( target == null )
			{
				return null;
			}
			else
			{
				return target.getPath( );
			}
		}
	    else   // probably different class loader was used; use reflective API instead
	    {
	    	Method resolveAppResourceMethod = findMethod( resourceIdentifiersObj, "resolveApplResource", new Class[]{URI.class}  ); //$NON-NLS-1$
	        Method resolveDesignResourceMethod = findMethod( resourceIdentifiersObj, "resolveDesignResource", new Class[]{URI.class}   ); //$NON-NLS-1$
	        
	        Object result = null;
	        if ( resolveAppResourceMethod != null )
	        {
	        	result = invokeMethod( resourceIdentifiersObj, resolveAppResourceMethod, new Object[]{path});
	        }
	        if ( result == null || !(result instanceof URI) )
	        {
	        	result = invokeMethod( resourceIdentifiersObj, resolveDesignResourceMethod, new Object[]{path});
	        }
	        if ( result instanceof URI )
	        {
	        	 return ((URI) result).getPath( );
	        }
	        return null;        
	    }
	}
	
    private static Object invokeMethod( Object anObj, Method objMethod, Object[] arg )
    {
        Object returnValue = null;
        try
        {
            returnValue = objMethod.invoke( anObj, arg );
        }
        catch( IllegalArgumentException ex )
        {
            // TODO - log warning
        }
        catch( IllegalAccessException ex )
        {
            // TODO - log warning
        }
        catch( InvocationTargetException ex )
        {
            // TODO - log warning
        }
        return returnValue;
    }
    
    private static Method findMethod( Object anObj, String methodName, Class[] argument )
    {
        Class clazz = anObj.getClass();
        Method theMethod = null;
        try
        {
            theMethod = clazz.getDeclaredMethod( methodName, argument );
        }
        catch( SecurityException ex )
        {
            // TODO - log warning
        }
        catch( NoSuchMethodException ex )
        {
            // TODO - log warning
        }
        
        return theMethod;
    }
    
	/**
	 * 
	 * @param location
	 * @return
	 */
	private static String encode( String location )
	{
		try
		{
			if ( File.separatorChar != '/' )
				location = location.replace( File.separatorChar, '/' );
			if( location.startsWith( "/" ) )
			{
				return new File( location ).toURI( )
						.toASCIIString( )
						.replace( new File( "/" ).toURI( ).toASCIIString( ), "/" );				
			}
			else
				return new File( location ).toURI( )
					.toASCIIString( )
					.replace( new File( "" ).toURI( ).toASCIIString( ), "" );
		}
		catch ( Exception e )
		{
			return location;
		}
	}    
}
