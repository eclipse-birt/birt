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

package org.eclipse.birt.report.designer.internal.ui.extension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.preference.IPreferenceChangeListener;
import org.eclipse.birt.core.preference.PreferenceChangeEvent;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.birt.report.designer.ui.extensions.IProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The ExtensionPoinyManager is utility class to retrieve IExtendedElementUI
 * extensions by model extension ID, or full list.It caches the information to
 * avoid reading the extensions each time.
 */
public class ExtensionPointManager implements IPreferenceChangeListener {

	private Map<String, ExtendedElementUIPoint> reportItemUIMap = null;

	private Map<String, IMenuBuilder> menuBuilderMap = null;

	private Map<String, IProviderFactory> providerFactoryMap = null;

	private Map<String, List<String>> prefereces = new HashMap<String, List<String>>();
	private Map<String, List<IPreferenceChangeListener>> prefListeners = new HashMap<String, List<IPreferenceChangeListener>>();

	private volatile static ExtensionPointManager instance = null;

	private ExtensionPointManager() {
	}

	public static ExtensionPointManager getInstance() {
		if (instance == null) {
			synchronized (ExtensionPointManager.class) {
				if (instance == null) {
					instance = new ExtensionPointManager();
				}
			}
		}
		return instance;
	}

	/**
	 * Gets the list of all the extended element points.
	 * 
	 * @return Returns the list of all the extended element point
	 *         (ExtendedElementUIPoint).
	 */
	public List<ExtendedElementUIPoint> getExtendedElementPoints() {
		return Arrays
				.asList(getReportItemUIMap().values().toArray(new ExtendedElementUIPoint[getReportItemUIMap().size()]));
	}

	/**
	 * Gets the extended element point with the specified extension name.
	 * 
	 * @param extensionName the extension name of the extended element
	 * 
	 * @return Returns the extended element point, or null if any problem exists
	 */
	public ExtendedElementUIPoint getExtendedElementPoint(String extensionName) {
		assert extensionName != null;
		return getReportItemUIMap().get(extensionName);
	}

	/**
	 * Returns the menu builder for the given element.
	 * 
	 * @param elementName the name of the element
	 * @return the menu builder, or null if there's no builder defined for the
	 *         element
	 */
	public IMenuBuilder getMenuBuilder(String elementName) {
		return getMenuBuilderMap().get(elementName);
	}

	/**
	 * Returns the provider factory for the given element.
	 * 
	 * @param elementName the name of the element
	 * @return the provider factory, or null if there's no factory defined for the
	 *         element
	 */
	public IProviderFactory getProviderFactory(String elementName) {
		return getProviderFactoryMap().get(elementName);
	}

	private Map<String, ExtendedElementUIPoint> getReportItemUIMap() {
		synchronized (this) {
			if (reportItemUIMap == null) {
				reportItemUIMap = new HashMap<String, ExtendedElementUIPoint>();

				for (Iterator<IExtension> iter = getExtensionElements(IExtensionConstants.EXTENSION_REPORT_ITEM_UI)
						.iterator(); iter.hasNext();) {
					IExtension extension = iter.next();

					ExtendedElementUIPoint point = createReportItemUIPoint(extension);
					if (point != null) {
						reportItemUIMap.put(point.getExtensionName(), point);
					}
				}
			}

			return reportItemUIMap;
		}

	}

	private Map<String, IMenuBuilder> getMenuBuilderMap() {
		synchronized (this) {
			if (menuBuilderMap == null) {
				menuBuilderMap = new HashMap<String, IMenuBuilder>();

				for (Iterator<IExtension> iter = getExtensionElements(IExtensionConstants.EXTENSION_MENU_BUILDERS)
						.iterator(); iter.hasNext();) {
					IExtension extension = iter.next();
					IConfigurationElement[] elements = extension.getConfigurationElements();
					for (int i = 0; i < elements.length; i++) {
						if (IExtensionConstants.ELEMENT_MENU_BUILDER.equals(elements[i].getName())) {
							String elementId = elements[i].getAttribute(IExtensionConstants.ATTRIBUTE_ELEMENT_NAME);
							try {
								Object menuBuilder = elements[i]
										.createExecutableExtension(IExtensionConstants.ATTRIBUTE_CLASS);
								if (menuBuilder instanceof IMenuBuilder) {
									menuBuilderMap.put(elementId, (IMenuBuilder) menuBuilder);
								}
							} catch (CoreException e) {
							}
						}
					}
				}
			}
			return menuBuilderMap;
		}

	}

	private Map<String, IProviderFactory> getProviderFactoryMap() {
		synchronized (this) {
			if (providerFactoryMap == null) {
				providerFactoryMap = new HashMap<String, IProviderFactory>();

				for (Iterator<IExtension> iter = getExtensionElements(IExtensionConstants.EXTENSION_PROVIDER_FACTORIES)
						.iterator(); iter.hasNext();) {
					IExtension extension = iter.next();
					IConfigurationElement[] elements = extension.getConfigurationElements();
					for (int i = 0; i < elements.length; i++) {
						if (IExtensionConstants.ELEMENT_PROVIDER_FACTORY.equals(elements[i].getName())) {
							String elementId = elements[i].getAttribute(IExtensionConstants.ATTRIBUTE_ELEMENT_NAME);
							try {
								Object factory = elements[i]
										.createExecutableExtension(IExtensionConstants.ATTRIBUTE_CLASS);
								if (factory instanceof IProviderFactory) {
									providerFactoryMap.put(elementId, (IProviderFactory) factory);
								}
							} catch (CoreException e) {
							}
						}
					}
				}
			}
			return providerFactoryMap;
		}

	}

	private List<IExtension> getExtensionElements(String id) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry == null) {// extension registry cannot be resolved
			return Collections.emptyList();
		}
		IExtensionPoint extensionPoint = registry.getExtensionPoint(id);
		if (extensionPoint == null) {// extension point cannot be resolved
			return Collections.emptyList();
		}
		return Arrays.asList(extensionPoint.getExtensions());
	}

	private ExtendedElementUIPoint createReportItemUIPoint(IExtension extension) {
		IConfigurationElement[] elements = extension.getConfigurationElements();
		if (elements != null && elements.length > 0) {
			return loadElements(elements);
		}
		return null;
	}

	private ExtendedElementUIPoint loadElements(IConfigurationElement[] elements) {

		ExtendedElementUIPoint newPoint = new ExtendedElementUIPoint();

		if (elements != null) {
			try {
				for (int i = 0; i < elements.length; i++) {
					loadAttributes(newPoint, elements[i]);
				}
			} catch (Exception e) {
				ExceptionHandler.handle(e);
				return null;
			}

		}
		if (DEUtil.getMetaDataDictionary().getExtension(newPoint.getExtensionName()) == null) {
			// Non-defined element. Ignore
			return null;
		}
		if (Policy.TRACING_EXTENSION_LOAD) {
			System.out.println("GUI Extesion Manager >> Loads " //$NON-NLS-1$
					+ newPoint.getExtensionName());
		}
		return newPoint;
	}

	private void loadAttributes(ExtendedElementUIPoint newPoint, IConfigurationElement element) throws CoreException {
		String elementName = element.getName();
		if (IExtensionConstants.ELEMENT_MODEL.equals(elementName)) {
			String value = element.getAttribute(IExtensionConstants.ATTRIBUTE_EXTENSION_NAME);
			newPoint.setExtensionName(value);
		} else if (IExtensionConstants.ELEMENT_REPORT_ITEM_FIGURE_UI.equals(elementName)
				|| IExtensionConstants.ELEMENT_REPORT_ITEM_IMAGE_UI.equals(elementName)
				|| IExtensionConstants.ELEMENT_REPORT_ITEM_LABEL_UI.equals(elementName)) {
			String className = element.getAttribute(IExtensionConstants.ATTRIBUTE_CLASS);
			if (className != null) {
				Object ui = element.createExecutableExtension(IExtensionConstants.ATTRIBUTE_CLASS);
				newPoint.setReportItemUI(new ExtendedUIAdapter(ui));
			}
		} else if (IExtensionConstants.ELEMENT_BUILDER.equals(elementName)) {
			loadClass(newPoint, element, IExtensionConstants.ATTRIBUTE_CLASS, IExtensionConstants.ELEMENT_BUILDER);
		} else if (IExtensionConstants.ELEMENT_PROPERTYEDIT.equals(elementName)) {
			loadClass(newPoint, element, IExtensionConstants.ATTRIBUTE_CLASS, IExtensionConstants.ELEMENT_PROPERTYEDIT);
		}

		else if (IExtensionConstants.ELEMENT_PALETTE.equals(elementName)) {
			loadIconAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_KEY_PALETTE_ICON,
					IExtensionConstants.ATTRIBUTE_ICON, false);
			loadIconAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_KEY_PALETTE_ICON_LARGE,
					IExtensionConstants.ATTRIBUTE_ICON_LARGE, false);
			loadStringAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY);
			loadStringAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY_DISPLAYNAME);
		} else if (IExtensionConstants.ELEMENT_EDITOR.equals(elementName)) {
			loadBooleanAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER);
			loadStringAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER_BY_PREFERENCE);
			loadBooleanAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_MASTERPAGE);
			loadBooleanAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_EDITOR_CAN_RESIZE);
			loadStringAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_EDITOR_MENU_LABEL);
		} else if (IExtensionConstants.ELEMENT_OUTLINE.equals(elementName)) {
			loadIconAttribute(newPoint, element, IExtensionConstants.ATTRIBUTE_KEY_OUTLINE_ICON,
					IExtensionConstants.ATTRIBUTE_ICON, true);
		} else if (IExtensionConstants.ELEMENT_DESCRIPTION.equals(elementName)) {
			String value = element.getValue();
			if (value != null) {
				newPoint.setAttribute(IExtensionConstants.ATTRIBUTE_KEY_DESCRIPTION, value);
			}
		}
	}

	/**
	 * @param newPoint  the extension point instance
	 * @param element   the configuration element
	 * @param className the name of the class attribute
	 */
	private void loadClass(ExtendedElementUIPoint newPoint, IConfigurationElement element, String className,
			String attributeName) {
		String value = element.getAttribute(className);
		if (value != null) {
			try {
				newPoint.setClass(attributeName, element.createExecutableExtension(className));
			} catch (CoreException e) {
			}
		}

	}

	private ImageDescriptor getImageDescriptor(IConfigurationElement element, String attrName) {
		assert element != null;
		IExtension extension = element.getDeclaringExtension();
		String iconPath = element.getAttribute(attrName);
		if (iconPath == null) {
			return null;
		}
		URL path = Platform.getBundle(extension.getNamespace()).getEntry("/"); //$NON-NLS-1$
		try {
			return ImageDescriptor.createFromURL(new URL(path, iconPath));
		} catch (MalformedURLException e) {
		}
		return null;
	}

	private void loadStringAttribute(ExtendedElementUIPoint newPoint, IConfigurationElement element,
			String attributeName) {
		String value = element.getAttribute(attributeName);
		if (value != null) {
			newPoint.setAttribute(attributeName, value);
		}

	}

	private void loadBooleanAttribute(ExtendedElementUIPoint newPoint, IConfigurationElement element,
			String attributeName) {
		String value = element.getAttribute(attributeName);
		if (value != null) {
			newPoint.setAttribute(attributeName, Boolean.valueOf(value));
		}
	}

	private void loadIconAttribute(ExtendedElementUIPoint newPoint, IConfigurationElement element, String keyName,
			String attributeName, boolean shared) {
		ImageDescriptor imageDescriptor = getImageDescriptor(element, attributeName);
		if (imageDescriptor != null) {
			if (shared) {
				String symbolName = ReportPlatformUIImages.getIconSymbolName(newPoint.getExtensionName(), keyName);
				ReportPlatformUIImages.declareImage(symbolName, imageDescriptor);
			}
			newPoint.setAttribute(keyName, imageDescriptor);
		}
	}

	public void preferenceChange(PreferenceChangeEvent event) {
		Iterator<String> exntesions = prefereces.keySet().iterator();
		while (exntesions.hasNext()) {
			String extension = exntesions.next();
			List<String> prefs = prefereces.get(extension);
			if (prefs.contains(event.getKey())) {
				List<IPreferenceChangeListener> listeners = prefListeners.get(extension);
				if (listeners != null) {
					for (int i = 0; i < listeners.size(); i++) {
						listeners.get(i).preferenceChange(event);
					}
				}
			}
		}
	}

	public void addPreference(String extension, String preference) {
		if (preference != null && extension != null) {
			if (!prefereces.containsKey(extension)) {
				prefereces.put(extension, new ArrayList<String>());
			}

			List<String> prefs = prefereces.get(extension);
			if (!prefs.contains(preference))
				prefs.add(preference);
		}
	}

	public void removePreference(String extension, String preference) {
		if (preference != null && extension != null) {
			if (!prefereces.containsKey(extension)) {
				return;
			}

			List<String> prefs = prefereces.get(extension);
			if (prefs.contains(preference))
				prefs.remove(preference);
		}
	}

	public void addPreferenceChangeListener(String extension, IPreferenceChangeListener listener) {
		if (listener != null && extension != null) {
			if (!prefListeners.containsKey(extension)) {
				prefListeners.put(extension, new ArrayList<IPreferenceChangeListener>());
			}

			List<IPreferenceChangeListener> lisnteners = prefListeners.get(extension);
			if (!lisnteners.contains(listener))
				lisnteners.add(listener);
		}
	}

	public void removePreferenceChangeListener(String extension, IPreferenceChangeListener listener) {
		if (listener != null && extension != null) {
			if (!prefListeners.containsKey(extension)) {
				return;
			}

			List<IPreferenceChangeListener> listeners = prefListeners.get(extension);
			if (listeners.contains(listener))
				listeners.remove(listener);
		}
	}

}