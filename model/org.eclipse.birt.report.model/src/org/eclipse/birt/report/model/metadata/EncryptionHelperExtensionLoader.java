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

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;

/**
 * Represents the extension loader for encryption helper.
 */

public class EncryptionHelperExtensionLoader extends ExtensionLoader {

	/**
	 * The name of extension point
	 */

	public static final String EXTENSION_POINT = "org.eclipse.birt.report.model.encryptionHelper"; //$NON-NLS-1$

	private static final String ENCRYPTION_HELPER_TAG = "encryptionHelper"; //$NON-NLS-1$

	/**
	 * Default constructor
	 */

	public EncryptionHelperExtensionLoader() {
		super(EXTENSION_POINT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.ExtensionLoader#loadExtension(org.
	 * eclipse.birt.core.framework.IExtension)
	 */

	@Override
	protected void loadExtension(IExtension extension) {
		IConfigurationElement[] configElements = extension.getConfigurationElements();

		EncryptionHelperElementLoader loader = new EncryptionHelperElementLoader();

		for (int i = 0; i < configElements.length; i++) {
			IConfigurationElement currentTag = configElements[i];
			if (ENCRYPTION_HELPER_TAG.equals(currentTag.getName())) {
				loader.loadElement(currentTag);
			}
		}
	}

	class EncryptionHelperElementLoader extends ExtensionElementLoader {

		private static final String IS_DEFAULT_ATTRIB = "isDefault"; //$NON-NLS-1$

		/**
		 * Loads the extension.
		 *
		 * @param elementTag the element tag
		 */

		@Override
		public void loadElement(IConfigurationElement elementTag) {
			String extensionName = elementTag.getAttribute(EXTENSION_NAME_ATTRIB);
			String className = elementTag.getAttribute(CLASS_ATTRIB);

			if (!checkRequiredAttribute(EXTENSION_NAME_ATTRIB, extensionName)
					|| !checkRequiredAttribute(CLASS_ATTRIB, className)) {
				return;
			}

			boolean isDefault = getBooleanAttrib(elementTag, IS_DEFAULT_ATTRIB, false);
			try {
				IEncryptionHelper helper = (IEncryptionHelper) elementTag.createExecutableExtension(CLASS_ATTRIB);

				MetaDataDictionary dd = MetaDataDictionary.getInstance();
				dd.addEncryptionHelper(extensionName, helper);

				// set default
				if (isDefault) {
					String defaultEncryption = dd.getDefaultEncryptionHelperID();
					if (SimpleEncryptionHelper.ENCRYPTION_ID.equals(defaultEncryption)) {
						dd.setDefaultEncryptionHelper(extensionName);
					} else {
						handleError(new ExtensionException(new String[] { extensionName, defaultEncryption },
								ExtensionException.DESIGN_EXCEPTION_DEFAULT_ENCRYPTION_EXIST));
					}

				}
			} catch (FrameworkException e) {
				handleError(new ExtensionException(new String[] { className },
						ExtensionException.DESIGN_EXCEPTION_FAILED_TO_CREATE_INSTANCE));
				return;
			} catch (MetaDataException e) {
				handleError(e);
				return;
			}
		}

	}
}
