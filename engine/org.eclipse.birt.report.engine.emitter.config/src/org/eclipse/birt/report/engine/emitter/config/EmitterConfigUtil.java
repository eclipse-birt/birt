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

package org.eclipse.birt.report.engine.emitter.config;

import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.IReportEngine;

/**
 * EmitterConfigUtil
 */
public class EmitterConfigUtil {

	private EmitterConfigUtil() {

	}

	/**
	 * Returns the default emitter descriptor for given format. If the default
	 * emitter is already specified in <code>EngineConfig</code> for this format, it
	 * will be returned; otherwise, the first matched emitter will be returned.
	 * 
	 * @param format  The render format. e.g. "html", "pdf".
	 * @param manager The emitter configuration manager instance.
	 * @param engine  The report engine instance.
	 * @return The descriptor. Could be <code>null</code> if no matching found.
	 */
	public static IEmitterDescriptor getDefaultDescriptor(String format, IEmitterConfigurationManager manager,
			IReportEngine engine) {
		String defaultID = engine.getConfig().getDefaultEmitter(format);

		if (defaultID == null) {
			EmitterInfo[] eis = engine.getEmitterInfo();

			if (eis != null) {
				for (int i = 0; i < eis.length; i++) {
					if (format.equals(eis[i].getFormat())) {
						defaultID = eis[i].getID();
						break;
					}
				}
			}
		}

		if (defaultID != null) {
			return manager.getEmitterDescriptor(defaultID);
		}

		return null;
	}
}
