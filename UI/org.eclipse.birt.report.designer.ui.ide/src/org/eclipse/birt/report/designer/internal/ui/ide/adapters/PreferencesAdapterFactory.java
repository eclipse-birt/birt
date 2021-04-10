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
