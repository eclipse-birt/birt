/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.report.designer.ui.cubebuilder.BuilderPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * This class has been created to hold methods that provide specific
 * functionality or services.
 */
public final class UIHelper {

	/**
	 * This method returns an URL for a resource given its plugin relative path. It
	 * is intended to be used to abstract out the usage of the UI as a plugin or
	 * standalone component when it comes to accessing resources.
	 * 
	 * @param sPluginRelativePath The path to the resource relative to the plugin
	 *                            location.
	 * @return URL representing the location of the resource.
	 */
	public static URL getURL(String sPluginRelativePath) {
		URL url = null;
		if (Platform.getExtensionRegistry() != null) {
			try {
				url = new URL(BuilderPlugin.getDefault().getBundle().getEntry("/"), sPluginRelativePath); //$NON-NLS-1$
			} catch (MalformedURLException e) {
				ExceptionUtil.handle(e);
			}
		} else {
			try {
				url = new URL("file:///" + new File(sPluginRelativePath).getAbsolutePath()); //$NON-NLS-1$
			} catch (MalformedURLException e) {
				ExceptionUtil.handle(e);
			}
		}

		return url;
	}

	private static Image createImage(String sPluginRelativePath) {
		Image img = null;
		try {
			try {
				img = new Image(Display.getCurrent(), getURL(sPluginRelativePath).openStream());
			} catch (MalformedURLException e1) {
				img = new Image(Display.getCurrent(), new FileInputStream(getURL(sPluginRelativePath).toString()));
			}
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}

		// If still can't load, return a dummy image.
		if (img == null) {
			img = new Image(Display.getCurrent(), 1, 1);
		}
		return img;
	}

	/**
	 * This is a convenience method to get an imgIcon from a URL.
	 * 
	 * @param sPluginRelativePath The URL for the imgIcon.
	 * @return The imgIcon represented by the given URL.
	 * @see #setImageCached( boolean )
	 */
	public static Image getImage(String sPluginRelativePath) {
		ImageRegistry registry = JFaceResources.getImageRegistry();
		Image image = registry.get(sPluginRelativePath);
		if (image == null) {
			image = createImage(sPluginRelativePath);
			registry.put(sPluginRelativePath, image);
		}
		return image;
	}

	public static boolean existIntProperty(ModuleHandle module, String id, String key) {
		UserPropertyDefnHandle property = module
				.getUserPropertyDefnHandle(id + BuilderConstants.PROPERTY_SEPARATOR + key);
		if (property == null)
			return false;
		else if (property.getType() != PropertyType.INTEGER_TYPE)
			return false;
		else
			return true;
	}

	public static int getIntProperty(ModuleHandle module, String id, String key) {
		return module.getIntProperty(id + BuilderConstants.PROPERTY_SEPARATOR + key);
	}

	public static void createIntPropertyDefn(ModuleHandle module, String id, String key) throws UserPropertyException {
		UserPropertyDefnHandle property = module
				.getUserPropertyDefnHandle(id + BuilderConstants.PROPERTY_SEPARATOR + key);
		if (property != null && property.getType() != PropertyType.INTEGER_TYPE)
			module.dropUserPropertyDefn(property.getName());

		UserPropertyDefn propertyDefn = new UserPropertyDefn();
		propertyDefn.setName(id + BuilderConstants.PROPERTY_SEPARATOR + key);
		propertyDefn.setType(DEUtil.getMetaDataDictionary().getPropertyType(PropertyType.INTEGER_TYPE));
		propertyDefn.setVisible(false);
		module.addUserPropertyDefn(propertyDefn);
	}

	public static void setIntProperty(ModuleHandle module, String id, String key, int value) throws SemanticException {
		if (!existIntProperty(module, id, key)) {
			createIntPropertyDefn(module, id, key);
		}
		module.setIntProperty(id + BuilderConstants.PROPERTY_SEPARATOR + key, value);
	}

	public static String getId(Object model, TabularCubeHandle carrier) {
		if (model instanceof DataSetHandle) {
			return carrier.getName() + BuilderConstants.PROPERTY_SEPARATOR + (((DesignElementHandle) model).getName());
		}
		if (model instanceof HierarchyHandle) {
			return carrier.getName() + BuilderConstants.PROPERTY_SEPARATOR
					+ ((HierarchyHandle) model).getContainer().getName() + BuilderConstants.PROPERTY_SEPARATOR
					+ (((DesignElementHandle) model).getName());
		}
		return ""; //$NON-NLS-1$
	}

	public static void dropDimensionProperties(DimensionHandle dimension) {
		TabularCubeHandle cube = (TabularCubeHandle) dimension.getContainer();
		HierarchyHandle hierarcy = dimension.getDefaultHierarchy();
		if (hierarcy == null)
			return;
		try {
			dropProperty(cube, hierarcy, BuilderConstants.POSITION_X);
			dropProperty(cube, hierarcy, BuilderConstants.POSITION_Y);
			dropProperty(cube, hierarcy, BuilderConstants.SIZE_WIDTH);
			dropProperty(cube, hierarcy, BuilderConstants.SIZE_HEIGHT);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private static void dropProperty(TabularCubeHandle cube, HierarchyHandle hierarcy, String type) throws Exception {
		ModuleHandle module = cube.getRoot();
		if (UIHelper.existIntProperty(hierarcy.getRoot(), UIHelper.getId(hierarcy, cube), type)) {

			if (module.getProperty(getId(hierarcy, cube) + BuilderConstants.PROPERTY_SEPARATOR + type) != null)
				module.clearProperty(getId(hierarcy, cube) + BuilderConstants.PROPERTY_SEPARATOR + type);

			module.dropUserPropertyDefn(getId(hierarcy, cube) + BuilderConstants.PROPERTY_SEPARATOR + type);

		}
	}
}
