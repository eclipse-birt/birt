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

package org.eclipse.birt.data.engine.i18n;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.i18n.ResourceHandle;

import com.ibm.icu.util.ULocale;

/**
 * Implementation of ResourceHandle in DtE project
 */
public class DataResourceHandle extends ResourceHandle {
	private static DataResourceHandle resourceHandle;
	private static Map localeResourceHandleMap;

	/**
	 * @param locale
	 */
	private DataResourceHandle(ULocale locale) {
		super(locale);
	}

	/**
	 * @return the DataResourceHandle with default ULocale
	 */
	public synchronized static DataResourceHandle getInstance() {
		if (resourceHandle == null)
			resourceHandle = new DataResourceHandle(ULocale.getDefault());

		return resourceHandle;
	}

	/**
	 * @param locale
	 * @return the DataResourceHandle with specified ULocale
	 */
	public synchronized static DataResourceHandle getInstance(ULocale locale) {
		if (localeResourceHandleMap == null)
			localeResourceHandleMap = new HashMap();

		DataResourceHandle ret = (DataResourceHandle) localeResourceHandleMap.get(locale);

		if (ret == null) {
			ret = new DataResourceHandle(locale);
			localeResourceHandleMap.put(locale, ret);
		}

		return ret;
	}

}
