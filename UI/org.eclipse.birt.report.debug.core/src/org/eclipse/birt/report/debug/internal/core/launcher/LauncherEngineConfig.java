/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.launcher;

import java.net.URLEncoder;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.RenderOption;

/**
 * LauncherEngineConfig
 */
public class LauncherEngineConfig extends EngineConfig {

	private static final String IMAGE_PATH = "image"; //$NON-NLS-1$

	/**
	 * constructor
	 */
	public LauncherEngineConfig() {
		super();

		HTMLRenderOption emitterConfig = (HTMLRenderOption) getEmitterConfigs().get(RenderOption.OUTPUT_FORMAT_HTML);

		if (emitterConfig == null) {
			emitterConfig = new HTMLRenderOption();
		}

		emitterConfig.setActionHandler(new HTMLActionHandler() {

			@Override
			public String getURL(IAction actionDefn, Object context) {
				if (actionDefn.getType() == IAction.ACTION_DRILLTHROUGH) {
					return "birt://" //$NON-NLS-1$
							+ URLEncoder.encode(super.getURL(actionDefn, context));
				}
				return super.getURL(actionDefn, context);
			}

		});

		emitterConfig.setImageDirectory(IMAGE_PATH);

		getEmitterConfigs().put(RenderOption.OUTPUT_FORMAT_HTML, emitterConfig);

	}

}
