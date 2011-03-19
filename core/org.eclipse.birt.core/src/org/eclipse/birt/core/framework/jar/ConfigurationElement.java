/*******************************************************************************
 * Copyright (c) 2010, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework.jar;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Status;

public class ConfigurationElement implements IConfigurationElement
{

	protected Object parent;
	protected Bundle bundle;
	protected IExtension extension;
	protected String name;
	protected String label;
	protected String value;
	protected HashMap<String, String> attributes;
	protected ConfigurationElement[] children;

	public Object createExecutableExtension( String propertyName )
			throws CoreException
	{
		String value = attributes.get( propertyName );
		if ( value != null )
		{
			try
			{
				Class<?> clazz = Class.forName( value );
				Object inst = clazz.newInstance( );

				if( inst instanceof IExecutableExtension )
				{
				    ((IExecutableExtension)inst).setInitializationData( 
				            this, propertyName, null ); // TODO support adapter data
				}
				return inst;
			}
			catch ( Exception e )
			{
				throw new CoreException( new Status( IStatus.ERROR,
						"org.eclipse.birt.core", 0, e.getMessage( ), e ) ); //$NON-NLS-1$
			}
		}
		return null;
	}

	public String getAttribute( String name )
	{
		return attributes.get( name );
	}

	public String[] getAttributeNames( )
	{
		return attributes.keySet( ).toArray( new String[attributes.size( )] );
	}

	public IConfigurationElement[] getChildren( )
	{
		return children;
	}

	public IConfigurationElement[] getChildren( String name )
	{
		ArrayList<IConfigurationElement> namedChildren = new ArrayList<IConfigurationElement>( );
		for ( IConfigurationElement child : children )
		{
			if ( name.equals( child.getName( ) ) )
			{
				namedChildren.add( child );
			}
		}
		return namedChildren.toArray( new IConfigurationElement[namedChildren
				.size( )] );
	}

	public IExtension getDeclaringExtension( )
	{
	    if( extension != null )
	        return extension;
        if( parent instanceof IExtension )
            return (IExtension)parent;
	    if( parent instanceof ConfigurationElement )
	        return ((ConfigurationElement)parent).getDeclaringExtension();
	    return null;
	}

	public String getName( )
	{
		return name;
	}

	public Object getParent( )
	{
		return parent;
	}

	public String getValue( )
	{
		return value;
	}

	public String getAttribute( String arg0, String arg1 )
	{
		return null;
	}

	public String getAttributeAsIs( String name )
	{
		return getAttribute( name );
	}

	public IContributor getContributor( ) throws InvalidRegistryObjectException
	{
        if( bundle == null )
        {
            IExtension declaringExtn = getDeclaringExtension();
            if( declaringExtn != null )
                return declaringExtn.getContributor();
            return null;
        }
        
		return bundle.getContributor( );
	}

	public String getNamespace( ) throws InvalidRegistryObjectException
	{
		return bundle.getSymbolicName( );
	}

	public String getNamespaceIdentifier( )
			throws InvalidRegistryObjectException
	{
		return bundle.getSymbolicName( );
	}

	public String getValue( String arg ) throws InvalidRegistryObjectException
	{
		return null;
	}

	public String getValueAsIs( ) throws InvalidRegistryObjectException
	{
		return value;
	}

	public boolean isValid( )
	{
		return true;
	}

}
