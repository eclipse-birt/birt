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

package org.eclipse.birt.report.engine.plugin;

import org.eclipse.birt.report.engine.api.ReportRunner;
import org.eclipse.core.runtime.IPlatformRunnable;

public class ReportExecutor implements IPlatformRunnable
{

	public Object run( Object args ) throws Exception
	{
		// as we know that the last argument must be a report design
		// or report document, so remove any extra arguments which
		// added by the eclipse platform (which is started with '-').
		if ( args instanceof String[] )
		{
			String[] platformArgs = (String[]) args;
			int appArgLength = platformArgs.length;
			for ( ; appArgLength > 0; appArgLength-- )
			{
				String arg = platformArgs[appArgLength - 1];
				if ( arg.charAt( 0 ) != '-' )
				{
					break;
				}
			}

			String[] appArgs = new String[appArgLength];
			System.arraycopy( args, 0, appArgs, 0, appArgLength );
			new ReportRunner( appArgs ).execute( );
		}
		return null;
	}
}
