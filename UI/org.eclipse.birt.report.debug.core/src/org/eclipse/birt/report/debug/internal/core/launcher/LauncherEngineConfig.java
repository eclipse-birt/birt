/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

			public String getURL(IAction actionDefn, Object context) {
				if (actionDefn.getType() == IAction.ACTION_DRILLTHROUGH)
					return "birt://" //$NON-NLS-1$
							+ URLEncoder.encode(super.getURL(actionDefn, context));
				return super.getURL(actionDefn, context);
			}

		});

		emitterConfig.setImageDirectory(IMAGE_PATH);

		getEmitterConfigs().put(RenderOption.OUTPUT_FORMAT_HTML, emitterConfig);

	}

}
