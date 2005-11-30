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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemQuery;

/**
 * Manages engine extensions. Currently, engine supports 4 types of extensions: emitters,
 * extended items Query, Generation, Presentation time extensions
 */
public class ExtensionManager
{
	protected static Logger logger = Logger.getLogger( ExtensionManager.class.getName( ) );
	
	public final static String EXTENSION_POINT_EMITTERS = "org.eclipse.birt.report.engine.emitters";	// $NON-NLS-1$
	public final static String EXTENSION_POINT_GENERATION = "org.eclipse.birt.report.engine.reportitemGeneration"; // $NON-NLS-1$
	public final static String EXTENSION_POINT_PRESENTATION = "org.eclipse.birt.report.engine.reportitemPresentation"; // $NON-NLS-1$
	public final static String EXTENSION_POINT_QUERY = "org.eclipse.birt.report.engine.reportitemQuery"; // $NON-NLS-1$
	
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
	
	/**
	 * stores all the mime types that are supported
	 */
	protected HashMap mimeTypes = new HashMap();
	
	/**
	 * Dummy constructor
	 */
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
	
	/**
	 * @param itemType the type of the extended item, i.e., "chart"
	 * @return an object that is used for query preparation time extended item processing 
	 */
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
	public IContentEmitter createEmitter(String format, String emitterID)
	{
		IConfigurationElement config = (IConfigurationElement)emitterExtensions.get(format);
		if (config != null)
		{
			Object object = createObject(config, "class"); //$NON-NLS-1$
			if (object instanceof IContentEmitter)
			{
				return (IContentEmitter)object;
			}
		}
		return null;
	}
	
	/**
	 * @return all the emitter extensions
	 */
	public HashMap getEmitterExtensions()
	{
		return this.emitterExtensions;
	}
	
	/**
	 * @param config
	 * @param property 
	 * @return
	 */
	protected Object createObject(IConfigurationElement config, String property)
	{
		try
		{
			Object object = config.createExecutableExtension(property);
			if (object != null)
				return object;
		}
		catch(FrameworkException ex)
		{
			if (logger.isLoggable( Level.WARNING ))
			{
				logger.log(Level.WARNING, "Can not instantiate class {0} with property {1}.",	// $NON-NLS-1$ 
						new String[] {config.getAttribute("class"), property} );	// $NON-NLS-1$
			}
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
			return;

		IExtension[] exts = extPoint.getExtensions();
		logger.log(Level.FINE, "Start load extension point: {0}", EXTENSION_POINT_GENERATION); //$NON-NLS-1$
		for (int i = 0; i < exts.length; i++)
		{
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++)
			{
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				generationExtensions.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load generation extension: {0}", itemName); //$NON-NLS-1$
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
			return;

		IExtension[] exts = extPoint.getExtensions();
		logger.log(Level.FINE, "Start load extension point: {0}", EXTENSION_POINT_PRESENTATION); //$NON-NLS-1$
		for (int i = 0; i < exts.length; i++)
		{
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++)
			{
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				presentationExtensions.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load prsentation extension: {0}", itemName); //$NON-NLS-1$
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
			return;

		IExtension[] exts = extPoint.getExtensions();
		logger.log(Level.FINE, "Start load extension point: {0}", EXTENSION_POINT_QUERY); //$NON-NLS-1$
		for (int i = 0; i < exts.length; i++)
		{
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++)
			{
				String itemName = configs[j].getAttribute("name"); //$NON-NLS-1$
				queryExtensions.put(itemName, configs[j]);
				logger.log(Level.FINE, "Load query extension: {0}", itemName); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * load report item emitters extension definitions
	 */
	protected void loadEmitterExtensionDefns()
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry(); 
		IExtensionPoint extPoint = registry.getExtensionPoint(EXTENSION_POINT_EMITTERS);
		if(extPoint==null)
			return;

		IExtension[] exts = extPoint.getExtensions();
		logger.log(Level.FINE, "Start load extension point: {0}", EXTENSION_POINT_EMITTERS); //$NON-NLS-1$
		for (int i = 0; i < exts.length; i++)	// loop at emitters level, i.e., fo or html
		{
			IConfigurationElement[] configs = exts[i].getConfigurationElements();
			for (int j = 0; j < configs.length; j++)	// loop at emitter level 
			{				
				String format = configs[j].getAttribute("format");	// $NON-NLS-1$
				String mimeType = configs[j].getAttribute("mimeType");	// $NON-NLS-1$
				if(!emitterExtensions.containsKey(format))
				{
					emitterExtensions.put(format, configs[j]);
					mimeTypes.put(format, mimeType);
					logger.log(Level.FINE, "Load {0} emitter", format); //$NON-NLS-1$
				}
				
			}
		}
	}
	
	/**
	 * @param format the output format
	 * @return the mime type for the specific format
	 */
	public String getMIMEType(String format)
	{
		if(mimeTypes.containsKey(format))
		{
			return (String)mimeTypes.get(format);
		}
		return null;
	}
}
