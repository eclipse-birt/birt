/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.internal.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.birt.integration.wtp.ui.internal.exception.BirtCoreException;
import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.Logger;
import org.eclipse.birt.integration.wtp.ui.internal.util.WebArtifactUtil;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ContextParamBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.FilterBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.FilterMappingBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ListenerBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ServletBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ServletMappingBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.TagLibBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.WebAppBean;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jst.j2ee.project.JavaEEProjectUtilities;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties.FacetDataModelMap;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.osgi.framework.Bundle;

/**
 * Birt Wizard Utility
 * 
 */
public class BirtWizardUtil implements IBirtWizardConstants {

	/**
	 * Find Configuration Elements from Extension Registry by Extension ID
	 * 
	 * @param extensionId
	 * @return
	 */
	public static IConfigurationElement[] findConfigurationElementsByExtension(String extensionId) {
		if (extensionId == null)
			return null;

		// find Extension Point entry
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionId);

		if (extensionPoint == null) {
			return null;
		}

		return extensionPoint.getConfigurationElements();
	}

	/**
	 * Find Configuration Element from Extension Registry by ID
	 * 
	 * @param extensionId String
	 * @param id          String
	 * @return
	 */
	public static IConfigurationElement findConfigurationElementById(String extensionId, String id) {
		// find configuration elements by extension
		IConfigurationElement[] elements = findConfigurationElementsByExtension(extensionId);

		if (elements == null)
			return null;

		// Match the destined Configuration Element by 'id' attribute
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			if (element != null && id.equals(element.getAttribute("id"))) //$NON-NLS-1$
			{
				return element;
			}
		}

		return null;
	}

	/**
	 * Returns IFolder object
	 * 
	 * @param project
	 * @param dir
	 * @return
	 * @throws CoreException
	 */
	public static IFolder getFolder(IProject project, String dest) throws CoreException {
		if (project == null)
			return null;

		// find destination folder
		IFolder folder;
		if (dest == null || dest.length() <= 0) {
			dest = ""; //$NON-NLS-1$
		}

		// if folder doesn't exist, try to create it.
		folder = project.getFolder(dest);
		if (!folder.exists()) {
			folder.create(true, true, null);
		}

		return folder;
	}

	/**
	 * Do import zip file into current project
	 * 
	 * @param project
	 * @param source
	 * @param dest
	 * @param monitor
	 * @param query
	 * @throws CoreException
	 */
	public static void doImports(IProject project, String source, IPath destPath, IProgressMonitor monitor,
			IOverwriteQuery query) throws CoreException {

		IConfigurationElement configElement = BirtWizardUtil.findConfigurationElementById(
				IBirtWizardConstants.EXAMPLE_WIZARD_EXTENSION_POINT, IBirtWizardConstants.BIRTEXAMPLE_WIZARD_ID);

		// if source file is null, try to find it defined extension
		if (source == null) {
			// get zip file name
			if (configElement != null) {
				// get projectsetup fregment
				IConfigurationElement[] projects = configElement.getChildren("projectsetup"); //$NON-NLS-1$
				IConfigurationElement[] imports = null;
				if (projects != null && projects.length > 0) {
					imports = projects[0].getChildren("import"); //$NON-NLS-1$
				}

				// get import fregment
				if (imports != null && imports.length > 0) {
					// get defined zip file name
					source = imports[0].getAttribute("src"); //$NON-NLS-1$
				}
			}
		}

		// if source is null, throw exception
		if (source == null) {
			String message = BirtWTPMessages.BIRTErrors_miss_source;
			Logger.log(Logger.ERROR, message);
			throw BirtCoreException.getException(message, null);
		}

		// create zip entry from source file
		ZipFile zipFile = getZipFileFromPluginDir(source, getContributingPlugin(configElement));

		// extract zip file and import files into project
		importFilesFromZip(zipFile, destPath, new SubProgressMonitor(monitor, 1), query);
	}

	/**
	 * Get file name space from configuration element
	 * 
	 * @param configurationElement
	 * @return
	 */
	private static String getContributingPlugin(IConfigurationElement configurationElement) {
		Object parent = configurationElement;
		while (parent != null) {
			if (parent instanceof IExtension)
				return ((IExtension) parent).getNamespace();
			parent = ((IConfigurationElement) parent).getParent();
		}
		return null;
	}

	/**
	 * Create zip entry from plugin directory
	 * 
	 * @param pluginRelativePath
	 * @param symbolicName
	 * @return
	 * @throws CoreException
	 */
	private static ZipFile getZipFileFromPluginDir(String pluginRelativePath, String symbolicName)
			throws CoreException {
		try {
			Bundle bundle = Platform.getBundle(symbolicName);
			if (bundle == null)
				return null;

			URL starterURL = new URL(bundle.getEntry("/"), pluginRelativePath); //$NON-NLS-1$
			return new ZipFile(FileLocator.toFileURL(starterURL).getFile());
		} catch (IOException e) {
			String message = pluginRelativePath + ": " + e.getMessage(); //$NON-NLS-1$
			Logger.logException(e);
			throw BirtCoreException.getException(message, e);
		}
	}

	/**
	 * extract zip file and import files into project
	 * 
	 * @param srcZipFile
	 * @param destPath
	 * @param monitor
	 * @param query
	 * @throws CoreException
	 */
	private static void importFilesFromZip(ZipFile srcZipFile, IPath destPath, IProgressMonitor monitor,
			IOverwriteQuery query) throws CoreException {
		try {
			ZipFileStructureProvider structureProvider = new ZipFileStructureProvider(srcZipFile);
			List list = prepareFileList(structureProvider, structureProvider.getRoot(), null);
			ImportOperation op = new ImportOperation(destPath, structureProvider.getRoot(), structureProvider, query,
					list);
			op.run(monitor);
		} catch (Exception e) {
			String message = srcZipFile.getName() + ": " + e.getMessage(); //$NON-NLS-1$
			Logger.logException(e);
			throw BirtCoreException.getException(message, e);
		}
	}

	/**
	 * Prepare file list from zip file
	 * 
	 * @param structure
	 * @param entry
	 * @param list
	 * @return
	 */
	private static List prepareFileList(ZipFileStructureProvider structure, ZipEntry entry, List list) {
		if (structure == null || entry == null)
			return null;

		if (list == null) {
			list = new ArrayList();
		}

		// get children
		List son = structure.getChildren(entry);
		if (son == null)
			return list;

		// check if directory
		Iterator it = son.iterator();
		while (it.hasNext()) {
			ZipEntry temp = (ZipEntry) it.next();
			if (temp.isDirectory()) {
				prepareFileList(structure, temp, list);
			} else {
				// if it is file, add to list
				list.add(temp);
			}
		}

		return list;
	}

	/**
	 * Initialize conflict resources settings
	 * 
	 * @param map
	 * @return
	 */
	public static Map initConflictResources(Map map) {
		if (map == null)
			map = new HashMap();

		// find configuration elements
		IConfigurationElement[] elements = findConfigurationElementsByExtension(BIRT_RESOURCES_EXTENSION_POINT);
		if (elements == null || elements.length <= 0)
			return map;

		for (int i = 0; i < elements.length; i++) {
			// filter conflict fragment
			if (!EXT_CONFLICT.equalsIgnoreCase(elements[i].getName()))
				continue;

			// get folder elements
			IConfigurationElement[] folders = elements[i].getChildren(EXT_FOLDER);
			if (folders == null)
				continue;

			for (int j = 0; j < folders.length; j++) {
				// get path attribute
				String path = folders[j].getAttribute("path"); //$NON-NLS-1$
				if (path == null)
					continue;

				// get file elements
				IConfigurationElement[] files = folders[j].getChildren(EXT_FILE);
				List fileList = new ArrayList();
				for (int k = 0; k < files.length; k++) {
					String name = files[k].getAttribute("name"); //$NON-NLS-1$
					if (name != null)
						fileList.add(name);
				}

				map.put(path, fileList);
			}
		}

		return map;
	}

	/**
	 * Initialize web app settings.
	 * 
	 * @param map
	 * @return
	 */
	public static Map initWebapp(Map map) {
		if (map == null)
			map = new HashMap();

		// find configuration elements
		IConfigurationElement[] elements = findConfigurationElementsByExtension(BIRT_RESOURCES_EXTENSION_POINT);
		if (elements == null || elements.length <= 0)
			return map;

		// web appliction
		WebAppBean webAppBean = new WebAppBean();

		for (int i = 0; i < elements.length; i++) {
			if (!EXT_WEBAPP.equalsIgnoreCase(elements[i].getName()))
				continue;

			String webappDesc = elements[i].getAttribute(EXTATTR_DESCRIPTION);
			if (webappDesc != null)
				webAppBean.setDescription(webappDesc);

			IConfigurationElement[] contextParams = elements[i].getChildren(EXT_CONTEXT_PARAM);
			IConfigurationElement[] filters = elements[i].getChildren(EXT_FILTER);
			IConfigurationElement[] filterMappings = elements[i].getChildren(EXT_FILTER_MAPPING);
			IConfigurationElement[] listeners = elements[i].getChildren(EXT_LISTENER);
			IConfigurationElement[] servlets = elements[i].getChildren(EXT_SERVLET);
			IConfigurationElement[] servletMappings = elements[i].getChildren(EXT_SERVLET_MAPPING);
			IConfigurationElement[] taglibs = elements[i].getChildren(EXT_TAGLIB);

			// context param
			if (contextParams != null) {
				Map son = (Map) map.get(EXT_CONTEXT_PARAM);
				if (son == null)
					son = new HashMap();

				for (int j = 0; j < contextParams.length; j++) {
					String name = contextParams[j].getAttribute("name"); //$NON-NLS-1$
					String value = contextParams[j].getAttribute("value"); //$NON-NLS-1$
					String description = contextParams[j].getAttribute("description"); //$NON-NLS-1$

					// create context-param bean
					if (name != null && value != null) {
						ContextParamBean bean = new ContextParamBean(name, value);
						bean.setDescription(description);
						son.put(name, bean);
					}
				}

				map.put(EXT_CONTEXT_PARAM, son);
			}

			// filter
			if (filters != null) {
				Map son = (Map) map.get(EXT_FILTER);
				if (son == null)
					son = new HashMap();

				for (int j = 0; j < filters.length; j++) {
					String name = filters[j].getAttribute("name"); //$NON-NLS-1$
					String className = filters[j].getAttribute("class"); //$NON-NLS-1$
					String description = filters[j].getAttribute("description"); //$NON-NLS-1$

					// create filter bean
					if (name != null && className != null) {
						FilterBean bean = new FilterBean(name, className);
						bean.setDescription(description);
						son.put(name, bean);
					}
				}

				map.put(EXT_FILTER, son);
			}

			// filter mapping
			if (filterMappings != null) {
				Map son = (Map) map.get(EXT_FILTER_MAPPING);
				if (son == null)
					son = new HashMap();

				for (int j = 0; j < filterMappings.length; j++) {
					String name = filterMappings[j].getAttribute("name"); //$NON-NLS-1$
					String servletName = filterMappings[j].getAttribute("servletName"); //$NON-NLS-1$
					String uri = filterMappings[j].getAttribute("uri"); //$NON-NLS-1$

					// create filter-mapping bean
					if (name != null) {
						FilterMappingBean bean = new FilterMappingBean(name, servletName);
						bean.setUri(uri);
						son.put(WebArtifactUtil.getFilterMappingString(name, servletName, uri), bean);
					}
				}

				map.put(EXT_FILTER_MAPPING, son);
			}

			// listener
			if (listeners != null) {
				Map son = (Map) map.get(EXT_LISTENER);
				if (son == null)
					son = new HashMap();

				for (int j = 0; j < listeners.length; j++) {
					String className = listeners[j].getAttribute("class"); //$NON-NLS-1$
					String description = listeners[j].getAttribute("description"); //$NON-NLS-1$

					// create listener bean
					if (className != null) {
						ListenerBean bean = new ListenerBean(className);
						bean.setDescription(description);
						son.put(EXT_LISTENER + j, bean);
					}
				}

				map.put(EXT_LISTENER, son);
			}

			// servlet
			if (servlets != null) {
				Map son = (Map) map.get(EXT_SERVLET);
				if (son == null)
					son = new HashMap();

				for (int j = 0; j < servlets.length; j++) {
					String name = servlets[j].getAttribute("name"); //$NON-NLS-1$
					String className = servlets[j].getAttribute("class"); //$NON-NLS-1$
					String description = servlets[j].getAttribute("description"); //$NON-NLS-1$

					// create servlet bean
					if (name != null && className != null) {
						ServletBean bean = new ServletBean(name, className);
						bean.setDescription(description);
						son.put(name, bean);
					}
				}

				map.put(EXT_SERVLET, son);
			}

			// servlet mapping
			if (servletMappings != null) {
				Map son = (Map) map.get(EXT_SERVLET_MAPPING);
				if (son == null)
					son = new HashMap();

				for (int j = 0; j < servletMappings.length; j++) {
					String name = servletMappings[j].getAttribute("name"); //$NON-NLS-1$
					String uri = servletMappings[j].getAttribute("uri"); //$NON-NLS-1$

					// create servlet-mapping bean
					if (name != null && uri != null) {
						ServletMappingBean bean = new ServletMappingBean(name, uri);
						son.put(uri, bean);
					}
				}

				map.put(EXT_SERVLET_MAPPING, son);
			}

			// taglib
			if (taglibs != null) {
				Map son = (Map) map.get(EXT_TAGLIB);
				if (son == null)
					son = new HashMap();

				for (int j = 0; j < taglibs.length; j++) {
					String uri = taglibs[j].getAttribute("uri"); //$NON-NLS-1$
					String location = taglibs[j].getAttribute("location"); //$NON-NLS-1$

					// create taglib bean
					if (uri != null && location != null) {
						TagLibBean bean = new TagLibBean(uri, location);
						son.put(uri, bean);
					}
				}

				map.put(EXT_TAGLIB, son);
			}
		}

		map.put(EXT_WEBAPP, webAppBean);

		return map;
	}

	/**
	 * get default resource folder setting
	 * 
	 * @return
	 */
	public static String getDefaultResourceFolder() {
		String resourceFolder = ""; //$NON-NLS-1$

		try {
			// check if load plugin
			Bundle bundle = Platform.getBundle(REPORT_PLUGIN_ID);
			if (bundle == null)
				return resourceFolder;

			// get class
			Class reportPluginClass = bundle.loadClass(REPORT_PLUGIN_CLASS);
			if (reportPluginClass != null) {
				// get instance
				Method method = reportPluginClass.getMethod("getDefault", new Class[0]); //$NON-NLS-1$
				Object instance = null;
				if (method != null) {
					instance = method.invoke(null, new Object[0]);
					method = reportPluginClass.getMethod("getResourcePreference", new Class[0]); //$NON-NLS-1$
				}

				if (method != null && instance != null) {
					// invode "getResourcePreference" method
					resourceFolder = (String) method.invoke(instance, new Object[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (resourceFolder == null)
			resourceFolder = ""; //$NON-NLS-1$

		return resourceFolder;
	}

	/**
	 * Returns the web content folder setting value
	 * 
	 * @param dataModel
	 * @return
	 */
	public static String getConfigFolder(IDataModel dataModel) {
		if (dataModel == null)
			return null;

		FacetDataModelMap dataModelMap = (FacetDataModelMap) dataModel
				.getProperty("IFacetProjectCreationDataModelProperties.FACET_DM_MAP"); //$NON-NLS-1$
		if (dataModelMap == null)
			return null;

		IDataModel dataModel1 = dataModelMap.getFacetDataModel("jst.web"); //$NON-NLS-1$
		if (dataModel1 == null)
			return null;

		return (String) dataModel1.getStringProperty("IJ2EEFacetInstallDataModelProperties.CONFIG_FOLDER"); //$NON-NLS-1$
	}

	/**
	 * Returns the web contents folder of the specified project
	 * 
	 * @param project the project which web contents path is needed
	 * @return IPath of the web contents folder
	 */
	public static IPath getWebContentPath(IProject project) {
		IPath path = null;

		if (project != null && JavaEEProjectUtilities.isDynamicWebProject(project)) {
			IVirtualComponent component = ComponentCore.createComponent(project);
			path = component.getRootFolder().getWorkspaceRelativePath();
		}

		return path;
	}

	/**
	 * Create file from plugin directory
	 * 
	 * @param pluginRelativePath
	 * @param pluginId
	 * @return
	 * @throws CoreException
	 */
	public static File getFileFromPluginDir(String path, String pluginId) throws CoreException {
		try {
			Bundle bundle = Platform.getBundle(pluginId);
			if (bundle == null)
				return null;

			URL url = new URL(bundle.getEntry("/"), path); //$NON-NLS-1$
			return new File(FileLocator.toFileURL(url).getFile());
		} catch (IOException e) {
			// throw exception
			Logger.logException(e);
			throw BirtCoreException.getException(null, e);
		}
	}

	/**
	 * Check folders.
	 * 
	 * @param map
	 * @param project
	 * @param webContentFolder
	 * @param monitor
	 */
	public static void processCheckFolder(Map properties, IProject project, String webContentFolder,
			IProgressMonitor monitor) {
		if (properties == null || project == null || webContentFolder == null)
			return;

		// check folder settings
		String[] folders = { BIRT_RESOURCE_FOLDER_SETTING, BIRT_WORKING_FOLDER_SETTING, BIRT_DOCUMENT_FOLDER_SETTING,
				BIRT_IMAGE_FOLDER_SETTING, BIRT_SCRIPTLIB_FOLDER_SETTING, BIRT_LOG_FOLDER_SETTING };

		Map map = (Map) properties.get(EXT_CONTEXT_PARAM);
		if (map == null)
			return;

		for (int i = 0; i < folders.length; i++) {
			String folder = WebArtifactUtil.getContextParamValue(map, folders[i]);
			checkFolder(project, webContentFolder, folder);
		}
	}

	/**
	 * Check folder if exist. If not, create it.
	 * 
	 * @param project
	 * @param webContentFolder
	 * @param folderName
	 */
	private static void checkFolder(IProject project, String webContentFolder, String folderName) {
		if (folderName == null)
			return;

		try {
			File file = new File(folderName);
			if (file != null) {
				if (file.exists())
					return;

				if (file.isAbsolute()) {
					// create absolute folder
					file.mkdir();
				} else {
					// create folder in web content folder
					final IWorkspace ws = ResourcesPlugin.getWorkspace();
					final IPath pjPath = project.getFullPath();

					IPath configPath = pjPath.append(webContentFolder);
					IPath path = configPath.append(folderName);
					BirtWizardUtil.mkdirs(ws.getRoot().getFolder(path));
				}
			}
		} catch (Exception e) {
			Logger.logException(Logger.WARNING, e);
		}
	}

	/**
	 * Returns the file content
	 * 
	 * @param filename
	 * @param pluginId
	 * @return
	 */
	public static String readFile(String filename, String pluginId) throws CoreException {
		// get file
		File file = getFileFromPluginDir(filename, pluginId);
		try {
			// read file content
			FileReader reader = new FileReader(file);
			StringBuffer sbuf = new StringBuffer();
			char[] cbuf = new char[512];
			int len = 0;
			while ((len = reader.read(cbuf)) != -1) {
				sbuf.append(cbuf, 0, len);
			}
			reader.close();
			return sbuf.toString();
		} catch (Exception e) {
			// throw exception
			Logger.logException(e);
			throw BirtCoreException.getException(null, e);
		}
	}

	/**
	 * Write data into workbench file
	 * 
	 * @param file
	 * @param data
	 * @throws IOException
	 */
	public static void writeFile(IFile iFile, byte[] data) throws CoreException {
		if (iFile == null || data == null)
			return;

		try {
			// write file
			File file = iFile.getLocation().toFile();
			OutputStream out = new FileOutputStream(file, false);
			out.write(data);
			out.flush();
			out.close();
		} catch (Exception e) {
			// throw exception
			Logger.logException(e);
			throw BirtCoreException.getException(null, e);
		}
	}

	/**
	 * Make Directory
	 * 
	 * @param folder
	 * @throws CoreException
	 */
	public static void mkdirs(final IFolder folder) throws CoreException {
		if (!folder.exists()) {
			if (folder.getParent() instanceof IFolder) {
				mkdirs((IFolder) folder.getParent());
			}

			folder.create(true, true, null);
		}
	}

	/**
	 * Get Log Level
	 */
	public static String[] getLogLevels() {
		return new String[] { BirtWTPMessages.BIRTConfiguration_loglevel_all,
				BirtWTPMessages.BIRTConfiguration_loglevel_severe, BirtWTPMessages.BIRTConfiguration_loglevel_warning,
				BirtWTPMessages.BIRTConfiguration_loglevel_info, BirtWTPMessages.BIRTConfiguration_loglevel_config,
				BirtWTPMessages.BIRTConfiguration_loglevel_fine, BirtWTPMessages.BIRTConfiguration_loglevel_finer,
				BirtWTPMessages.BIRTConfiguration_loglevel_finest, BirtWTPMessages.BIRTConfiguration_loglevel_off };
	}
}
