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

package org.eclipse.birt.report.model.plugin;

import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.report.model.api.BundleFactory;
import org.eclipse.birt.report.model.extension.oda.ODAProviderFactory;
import org.osgi.framework.BundleContext;

/**
 * The class to use the eclipse tracing facilities.
 */

public class ModelPlugin extends BIRTPlugin
{

	public void start( BundleContext context ) throws Exception
	{
		super.start( context );

		ODAProviderFactory.initeTheFactory( new ODABaseProviderFactory( ) );

		BundleFactory.setBundleFactory( new PlatformBundleFactory( ) );
	}

}
