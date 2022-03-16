/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.metadata;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * The abstract extension loader which provides the common functionality for
 * extension loaders. This loader will read the extension definition, generate
 * extension element definition and add them into the extension element list of
 * <code>MetaDataDicationary</code>.
 */

public abstract class ExtensionLoader {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ExtensionLoader.class.getName());

	private String extensionPointer = null;

	/**
	 * Constructor with the id of extension pointer which the extension this
	 * extension loader loads implements.
	 *
	 * @param extensionPointer the id of extension pointer
	 */

	ExtensionLoader(String extensionPointer) {
		this.extensionPointer = extensionPointer;
	}

	/**
	 *
	 * @param extension
	 * @throws ExtensionException
	 * @throws MetaDataException
	 */
	abstract protected void loadExtension(IExtension extension);

	/**
	 * Loads the extensions in plug-ins, and add them into metadata dictionary.
	 *
	 */

	public final void load() {
		doLoad();
	}

	/**
	 * Logs the exceptions when extension pointers can't be found.
	 *
	 * @param e the extension exception.
	 */

	protected void handleError(MetaDataException e) {
		logger.log(Level.SEVERE, e.getMessage());
		MetaLogManager.log("Extension loading error", e); //$NON-NLS-1$
	}

	/**
	 * Logs the exceptions when extension pointers can't be found.
	 *
	 * @param e the extension exception.
	 */

	protected void handleError(MetaDataParserException e) {
		logger.log(Level.SEVERE, e.getMessage());
		MetaLogManager.log("Delta Metadata parsing error", e); //$NON-NLS-1$
	}

	/**
	 * Logs the exceptions when extension pointers can't be found.
	 *
	 * @param message the log message
	 */

	protected final void handleError(String message) {
		logger.log(Level.SEVERE, message);
		MetaLogManager.log(message);
	}

	/**
	 * Loads the extended elements in plug-ins, and add them into metadata
	 * dictionary.
	 *
	 * @throws ExtensionException if error is found when loading extension.
	 * @throws MetaDataException  if error encountered when adding the element to
	 *                            metadata dictionary.
	 */

	protected void doLoad() {
		IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
		if (pluginRegistry == null) {
			handleError(new ExtensionException(new String[] { extensionPointer },
					ExtensionException.DESIGN_EXCEPTION_EXTENSION_POINT_NOT_FOUND));
			return;
		}

		IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint(extensionPointer);
		if (extensionPoint == null) {
			handleError(new ExtensionException(new String[] { extensionPointer },
					ExtensionException.DESIGN_EXCEPTION_EXTENSION_POINT_NOT_FOUND));
			return;
		}

		IExtension[] extensions = extensionPoint.getExtensions();
		if (extensions != null) {
			for (int i = 0; i < extensions.length; i++) {
				loadExtension(extensions[i]);
			}
		}
	}

	/**
	 * Represents the loader which loads the top level XML element in extension
	 * definition file. The common constants are defined for parsing.
	 */

	abstract class ExtensionElementLoader {

		protected static final String EXTENSION_NAME_ATTRIB = "extensionName"; //$NON-NLS-1$
		protected static final String CLASS_ATTRIB = "class"; //$NON-NLS-1$

		/**
		 * Default constructor.
		 */

		ExtensionElementLoader() {
		}

		/**
		 * Loads the extension element definition and its properties.
		 *
		 * @param elementTag the element tag
		 * @throws MetaDataException if error encountered when adding the element to
		 *                           metadata dictionary.
		 */

		abstract void loadElement(IConfigurationElement elementTag);

		/**
		 * Checks whether the required attribute is set.
		 *
		 * @param name  the required attribute name
		 * @param value the attribute value
		 * @return true if the attribute is valid, otherwise false
		 * @throws ExtensionException if the value is empty
		 */

		protected boolean checkRequiredAttribute(String name, String value) {
			if (StringUtil.isBlank(value)) {
				handleError(new ExtensionException(new String[] { name },
						ExtensionException.DESIGN_EXCEPTION_VALUE_REQUIRED));
				return false;
			}
			return true;
		}

		/**
		 * Returns the boolean value of the given attribute.
		 *
		 * @param attrs        the element attributes
		 * @param attrName     the attribute name
		 * @param defaultValue the default value
		 * @return the boolean value
		 */

		protected boolean getBooleanAttrib(IConfigurationElement attrs, String attrName, boolean defaultValue) {
			String value = attrs.getAttribute(attrName);
			if (value == null) {
				return defaultValue;
			}

			if ("false".equalsIgnoreCase(value.trim())) { //$NON-NLS-1$
				return false;
			}

			if ("true".equalsIgnoreCase(value.trim())) { //$NON-NLS-1$
				return true;
			}

			return defaultValue;
		}
	}
}
