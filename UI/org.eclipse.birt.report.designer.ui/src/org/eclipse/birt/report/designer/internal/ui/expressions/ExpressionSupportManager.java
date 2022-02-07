/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.expressions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * ExpressionSupportManager
 */
public class ExpressionSupportManager {

	private ExpressionSupportManager() {
	}

	/**
	 * @return Returns all available expressoin supports in current context.
	 */
	public static IExpressionSupport[] getExpressionSupports() {
		String[] exts = getScriptExtensions();

		if (exts == null || exts.length == 0) {
			return null;
		}

		List<IExpressionSupport> supports = new ArrayList<IExpressionSupport>();

		for (String scriptName : exts) {
			Object adapter = ElementAdapterManager.getAdapter(scriptName, IExpressionSupport.class);

			if (adapter instanceof IExpressionSupport) {
				supports.add((IExpressionSupport) adapter);
			}
		}

		if (supports.size() == 0) {
			return null;
		}

		return supports.toArray(new IExpressionSupport[supports.size()]);
	}

	/**
	 * !! this method is experimental and should be replaced by API from birt.core
	 * later
	 * 
	 * @return
	 */
	private static String[] getScriptExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();

		if (registry == null) {
			return null;
		}

		IExtensionPoint extensionPoint = registry.getExtensionPoint("org.eclipse.birt.core.ScriptEngineFactory"); //$NON-NLS-1$

		if (extensionPoint == null) {
			return null;
		}

		List<String> exts = new ArrayList<String>();

		for (IExtension extension : extensionPoint.getExtensions()) {
			if (extension != null) {
				IConfigurationElement[] elements = extension.getConfigurationElements();

				if (elements != null) {
					for (IConfigurationElement element : elements) {
						if (element != null) {
							// final String id = element.getAttribute( "scriptID" ); //$NON-NLS-1$
							String name = element.getAttribute("scriptName"); //$NON-NLS-1$

							if (name != null && name.length() > 0) {
								exts.add(name);
							}
						}
					}
				}
			}
		}

		if (exts.size() == 0) {
			return null;
		}

		return exts.toArray(new String[exts.size()]);

	}
}
