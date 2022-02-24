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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.util.HashMap;

import org.eclipse.birt.report.designer.ui.preferences.IReportPreferenceFactory;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Plugin;

public class PreferencesAdapterFactory implements IAdapterFactory {

	private HashMap factoryMap = new HashMap();

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		String pluginId = ((Plugin) adaptableObject).getBundle().getSymbolicName();
		if (!factoryMap.containsKey(pluginId)) {
			IDEReportPreferenceFactory factory = new IDEReportPreferenceFactory((Plugin) adaptableObject);
			factoryMap.put(pluginId, factory);
		}
		return factoryMap.get(pluginId);
	}

	public Class[] getAdapterList() {
		return new Class[] { IReportPreferenceFactory.class };
	}

}
