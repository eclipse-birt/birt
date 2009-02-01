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
package org.eclipse.birt.report.data.oda.plugin;

import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.core.runtime.Plugin;


/**
 * Obtain support from Plugin
 */
public class OdaJdbcPlugin extends Plugin
{
	public void shutdown()
	{
		JDBCDriverManager.getInstance( ).close( );
	}
}
