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

package org.eclipse.birt.report.engine.extension.internal;
import java.util.HashMap;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemQuery;

/**
 * Manages engine extensions. Currently, engine supports three types of extensions: emitter set,
 * extended items generation time extension, and extended items presentation time extension
 */
public class ExtensionManager
{
	public final static String EXTENSION_POINT_EMITTER = "org.eclipse.birt.report.engine.emitters";	
	public final static String EXTENSION_POINT_GENERATION = "org.eclipse.birt.report.engine.reportitemGeneration"; 
	public final static String EXTENSION_POINT_PRESENTATION = "org.eclipse.birt.report.engine.reportitemPresentation";
	public final static String EXTENSION_POINT_QUERY = "org.eclipse.birt.report.engine.reportitemQuery";
	
	/**
	 * the singleton isntance
	 */
	static protected ExtensionManager sm_instance;
	
	/**
	 * stores references to all the generation extensions
	 */
	protected HashMap generationExtensions = new HashMap();
	
	/**
	 * stores references to all the presentation extensions
	 */
	protected HashMap presentationExtensions = new HashMap();
	
	/**
	 * reference to all the query extesions.
	 */
	protected HashMap queryExtensions = new HashMap();
	
	/**
	 * stores all the emitter extensions 
	 */
	protected HashMap emitterExtensions = new HashMap();
	
	protected HashMap formatMimeType = new HashMap();
	
	protected HashMap formatOptions = new HashMap();
	
	ExtensionManager()
	{
		loadGenerationExtensionDefns();
		loadPresentationExtensionDefns();
		loadQueryExtensionDefns();
		loadEmitterExtensionDefns();
	}
	
	/**
	 * create the static instance. It is a separate function so that getInstance do not need to be synchronized
	 */
	private synchronized static void createInstance()
	{
		if (sm_instance == null)
			sm_instance = new ExtensionManager();
	}
	
	/**
	 * @return the single instance for the extension manager 
	 */
	static public ExtensionManager getInstance()
	{
		if (sm_instance == null)
			createInstance();
		
		return sm_instance;
	}

	/**
	 * @param itemType 
	 * @return an object that is used for generation time extended item processing 
	 */
	public IReportItemGeneration createGenerationItem(String itemType)
	{
		IConfigurationElement config = (IConfigurationElement)generationExtensions.get(itemType);
		if (config != null)
		{
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IReportItemGeneration)
			{
				return (IReportItemGeneration)object;
			}
		}
		return null;
	}
	
	/**
	 * @param itemType the type of the extended item, i.e., "chart"
	 * @return an object that is used for presentation time extended item processing 
	 */
	public IReportItemPresentation createPresentationItem(String itemType)
	{
		IConfigurationElement config = (IConfigurationElement)presentationExtensions.get(itemType);
		if (config != null)
		{
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IReportItemPresentation)
			{
				return (IReportItemPresentation)object;
			}
		}
		return null;
	}
	public IReportItemQuery createQueryItem(String itemType)
	{
		IConfigurationElement config = (IConfigurationElement)queryExtensions.get(itemType);
		if (config != null)
		{
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IReportItemQuery)
			{
				return (IReportItemQuery)object;
			}
		}
		return null;
	}
	
	/**
	 * @param format the format that the extension point supports
	 * @return an emitter
	 */
	public IReportEmitter createEmitter(String format)
	{
		IConfigurationElement config = (IConfigurationElement)emitterExtensions.get(format);
		if (config != null)
		{
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IReportEmitter)
			{
				return (IReportEmitter)object;
			}
		}
		return null;
	}
	/**
	 * 
	 * @return
	 */
	public HashMap getEmitterExtensions()
	{
		return this.emitterExtensions;
	}
	
	protected Object createObject(IConfigurationElement config, String property)
	{
		try
		{
			Object object = config.createExecutableExtension(property);
			if (object != null)
			{
				return object;
			}
		}
		catch(FrameworkException ex)
		{
			//TODO log it out
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * load report item generation extension definitions 
	 */
	protected void loadGenerationExtensionDefns()
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry(); 
		IExtensionPoint extPoint = registry.getExtensionPoint(EXTENSION_POINT_GENERATION);
		if(extPoint==null)
		{
			return;
		}
		IExtension[] exts = extPoint.getExtensions();
		for (int i = 0; i < exts.length; i++)
		{
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++)
			{
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				generationExtensions.put(itemName, configs[i]);
			}
		}
	}
	
	/**
	 * load report item presentation extension definitions 
	 */
	protected void loadPresentationExtensionDefns()
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry(); 
		IExtensionPoint extPoint = registry.getExtensionPoint(EXTENSION_POINT_PRESENTATION);
		if(extPoint==null)
		{
			return;
		}
		IExtension[] exts = extPoint.getExtensions();
		for (int i = 0; i < exts.length; i++)
		{
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++)
			{
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				presentationExtensions.put(itemName, configs[i]);
			}
		}
	}
	
	/**
	 * load report item query extension definitions 
	 */
	protected void loadQueryExtensionDefns()
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry(); 
		IExtensionPoint extPoint = registry.getExtensionPoint(EXTENSION_POINT_QUERY);
		if(extPoint==null)
		{
			return;
		}
		IExtension[] exts = extPoint.getExtensions();
		for (int i = 0; i < exts.length; i++)
		{
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++)
			{
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				queryExtensions.put(itemName, configs[i]);
			}
		}
	}
	
	/**
	 * load report item emitters extension definitions
	 */
	protected void loadEmitterExtensionDefns()
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry(); 
		IExtensionPoint extPoint = registry.getExtensionPoint(EXTENSION_POINT_EMITTER);
		if(extPoint==null)
		{
			return;
		}
		IExtension[] exts = extPoint.getExtensions();
		for (int i = 0; i < exts.length; i++)
		{
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++)
			{				
				IConfigurationElement[] formats = configs[j].getChildren("format"); //$NON-NLS-1$
				
				if(formats != null && formats.length>0)
				{
					for(int k=0; k<formats.length; k++)
					{
						String formatName = formats[k].getAttribute("name"); //$NON-NLS-1$
						String mimeType = formats[k].getAttribute("mimeType"); //$NON-NLS-1$
						if(!emitterExtensions.containsKey(formatName))
						{
							emitterExtensions.put(formatName, configs[j]);
							formatMimeType.put(formatName, mimeType);
							IConfigurationElement[] options = formats[k].getChildren("option"); //$NON-NLS-1$
							if(options!=null && options.length>0)
							{
								String[] optionNames = new String[options.length];
								for(int l=0; l<options.length; l++)
								{
									optionNames[l] = options[l].getAttribute("name"); //$NON-NLS-1$
								}
								formatMimeType.put(formatName, optionNames);
							}
						}
					}
				}
				
				
			}
		}
	}
	
	public String[] getOptions(String format)
	{
		if(formatOptions.containsKey(format))
		{
			return (String[])formatOptions.get(format);
		}
		return null;
	}
	
	public String getMIMEType(String format)
	{
		if(formatMimeType.containsKey(format))
		{
			return (String)formatMimeType.get(format);
		}
		return null;
	}
}
