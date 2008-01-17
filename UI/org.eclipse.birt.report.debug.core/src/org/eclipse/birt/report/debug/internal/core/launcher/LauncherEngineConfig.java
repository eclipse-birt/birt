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

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

/**
 * LauncherEngineConfig
 */
public class LauncherEngineConfig extends EngineConfig
{

	/** Path of image files. */
	public static final String IMAGE_PATH = "image"; //$NON-NLS-1$

	/**
	 * constructor
	 */
	public LauncherEngineConfig( )
	{
		super( );

		HTMLRenderOption emitterConfig = (HTMLRenderOption) getEmitterConfigs( ).get( RenderOption.OUTPUT_FORMAT_HTML );

		emitterConfig.setImageHandler( new HTMLCompleteImageHandler( ) );
	}

}
