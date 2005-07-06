/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework.eclipse;

import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.IPlatform;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


/**
 * 
 * @version $Revision: 1.6 $ $Date: 2005/05/08 06:58:29 $
 */
public class EclipsePlatform implements IPlatform
{
	public EclipsePlatform()
	{
	}
	
	public IExtensionRegistry getExtensionRegistry()
	{
		return new EclipseExtensionRegistry(Platform.getExtensionRegistry());
	}
	
	public IBundle getBundle(String symbolicName)
	{
		Bundle bundle = Platform.getBundle(symbolicName);
		if (bundle != null)
		{
			return new EclipseBundle(bundle);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.framework.IPlatform#find(org.eclipse.birt.core.framework.IBundle, org.eclipse.core.runtime.IPath)
	 */
	public URL find(IBundle bundle, IPath path) 
	{
		if ( bundle instanceof Bundle )
		{
			return Platform.find( (Bundle)bundle, path );
		}
		
		return null;
	}

	public URL asLocalURL(URL url)  throws IOException
	{
		return Platform.asLocalURL(url);
	}
	
	static IConfigurationElement wrap(org.eclipse.core.runtime.IConfigurationElement object)
	{
		return new EclipseConfigurationElement(object);
	}
	
	static IConfigurationElement[] wrap(org.eclipse.core.runtime.IConfigurationElement[] objects)
	{
		if (objects == null)
		{
			return new IConfigurationElement[0];
		}
		IConfigurationElement[] wraps = new IConfigurationElement[objects.length];
		for (int i = 0; i < objects.length; i++)
		{
			wraps[i] = new EclipseConfigurationElement(objects[i]);
		}
		return wraps;
	}
	
	static IExtensionPoint wrap(org.eclipse.core.runtime.IExtensionPoint object)
	{
		return new EclipseExtensionPoint(object);
	}
	
	static IExtensionPoint[] wrap(org.eclipse.core.runtime.IExtensionPoint[] objects)
	{
		if (objects == null)
		{
			return new IExtensionPoint[0];
		}
		IExtensionPoint[] wraps = new IExtensionPoint[objects.length];
		for (int i = 0; i < objects.length; i++)
		{
			wraps[i] = new EclipseExtensionPoint(objects[i]);
		}
		return wraps;
	}	
	
	static IExtension wrap(org.eclipse.core.runtime.IExtension object)
	{
		return new EclipseExtension(object);
	}
	
	static IExtension[] wrap(org.eclipse.core.runtime.IExtension[] objects)
	{
		if (objects == null)
		{
			return new IExtension[0];
		}
		IExtension[] wraps = new IExtension[objects.length];
		for (int i = 0; i < objects.length; i++)
		{
			wraps[i] = new EclipseExtension(objects[i]);
		}
		return wraps;
	}
	
	static Object wrap(Object object)
	{
		if (object instanceof org.eclipse.core.runtime.IConfigurationElement)
		{
			return EclipsePlatform.wrap((org.eclipse.core.runtime.IConfigurationElement)object);
		}
		else if (object instanceof org.eclipse.core.runtime.IExtension)
		{
			return EclipsePlatform.wrap((org.eclipse.core.runtime.IExtension)object);
		}
		else if (object instanceof org.eclipse.core.runtime.IExtensionPoint)
		{
			return EclipsePlatform.wrap((org.eclipse.core.runtime.IExtensionPoint)object);
		}
		return object;
	}
}
