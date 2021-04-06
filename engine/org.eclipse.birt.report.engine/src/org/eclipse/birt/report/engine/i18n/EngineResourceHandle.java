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

package org.eclipse.birt.report.engine.i18n;

import java.util.Locale;
import com.ibm.icu.util.ULocale;

import org.eclipse.birt.core.i18n.ResourceHandle;

/**
 * A derived class to handle engine resources. Its existence is to make sure
 * that the engine resources can be found when the message files are placed
 * under the same directory as this file.
 */

public class EngineResourceHandle extends ResourceHandle {
	private static EngineResourceHandle resourceHandle;

	public static EngineResourceHandle getInstance() {
		synchronized (EngineResourceHandle.class) {
			if (resourceHandle == null) {
				resourceHandle = new EngineResourceHandle(ULocale.getDefault());
			}
		}
		return resourceHandle;
	}

	public EngineResourceHandle(ULocale locale) {
		super(locale);
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public EngineResourceHandle(Locale locale) {
		super(ULocale.forLocale(locale));
	}
}