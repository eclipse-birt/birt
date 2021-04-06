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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * The loader for scriptable object class info extensions.
 */

public class ScriptableObjectExtensionLoader extends ExtensionLoader {

	/**
	 * The name of extension point.
	 */

	public static final String EXTENSION_POINT = "org.eclipse.birt.report.model.IScriptableObjectClassInfo"; //$NON-NLS-1$

	private static final String ELEMENT_TAG = "scriptableClassInfo"; //$NON-NLS-1$

	private static final String DEFAULT_ROM = "BIRT_ROM"; //$NON-NLS-1$

	/**
	 * Default constructor
	 */

	public ScriptableObjectExtensionLoader() {
		super(EXTENSION_POINT);
	}

	protected void loadExtension(IExtension extension) {
		IConfigurationElement[] configElements = extension.getConfigurationElements();

		ScriptableObjectElementLoader loader = new ScriptableObjectElementLoader();

		IConfigurationElement currentTag = configElements[0];
		if (ELEMENT_TAG.equals(currentTag.getName())) {
			loader.loadElement(currentTag);
		}

	}

	private class ScriptableObjectElementLoader extends ExtensionElementLoader {

		/**
		 * Loads the extension.
		 * 
		 * @param elementTag the element tag
		 */

		public void loadElement(IConfigurationElement elementTag) {
			String extensionName = elementTag.getAttribute(EXTENSION_NAME_ATTRIB);
			String className = elementTag.getAttribute(CLASS_ATTRIB);

			if (!checkRequiredAttribute(CLASS_ATTRIB, className))
				return;

			try {
				IScriptableObjectClassInfo factory = (IScriptableObjectClassInfo) elementTag
						.createExecutableExtension(CLASS_ATTRIB);

				MetaDataDictionary metaData = MetaDataDictionary.getInstance();
				if (StringUtil.isBlank(extensionName) || DEFAULT_ROM.equalsIgnoreCase(extensionName)) {
					metaData.setScriptableFactory(factory);
					return;
				}

				IElementDefn elementDefn = metaData.getExtension(extensionName);
				if (elementDefn == null || !(elementDefn instanceof PeerExtensionElementDefn)) {
					handleError(new ExtensionException(new String[] { extensionName },
							ExtensionException.DESIGN_EXCEPTION_INVALID_ELEMENT_TYPE));
					return;
				}

				PeerExtensionElementDefn peerDefn = (PeerExtensionElementDefn) elementDefn;
				peerDefn.setScriptableFactory(factory);

			} catch (FrameworkException e) {
				handleError(new ExtensionException(new String[] { className },
						ExtensionException.DESIGN_EXCEPTION_FAILED_TO_CREATE_INSTANCE));
				return;
			}
		}

	}

}
