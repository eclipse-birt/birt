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

package org.eclipse.birt.report.data.adapter.i18n;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.i18n.ResourceHandle;

import com.ibm.icu.util.ULocale;

/**
 * Implementation of ResourceHandle in DtE project
 */
public class AdapterResourceHandle extends ResourceHandle {
	private static AdapterResourceHandle resourceHandle;
	private static Map localeResourceHandleMap;

	/**
	 * @param locale
	 */
	public AdapterResourceHandle(ULocale locale) {
		super(locale);
	}

	/**
	 * @return the DataResourceHandle with default ULocale
	 */
	public synchronized static AdapterResourceHandle getInstance() {
		if (resourceHandle == null) {
			resourceHandle = getInstance(ULocale.getDefault());
		}

		return resourceHandle;
	}

	/**
	 * @return the DataResourceHandle with default ULocale
	 */
	public synchronized static AdapterResourceHandle getInstance(ULocale locale) {
		if (localeResourceHandleMap == null) {
			localeResourceHandleMap = new HashMap();
		}

		AdapterResourceHandle ret = (AdapterResourceHandle) localeResourceHandleMap.get(locale);

		if (ret == null) {
			ret = new AdapterResourceHandle(locale);
			localeResourceHandleMap.put(locale, ret);
		}
		return ret;
	}

}
