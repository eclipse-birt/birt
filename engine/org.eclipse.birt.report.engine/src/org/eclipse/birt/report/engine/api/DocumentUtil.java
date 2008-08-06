/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.IOException;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.compound.IArchiveFile;

public class DocumentUtil
{

	public static void copy( IArchiveFile source, IArchiveFile target,
			IReportRunnable runnable ) throws EngineException
	{
		try
		{
			ArchiveUtil.copy( source, target );
		}
		catch ( IOException ex )
		{
			throw new EngineException( "exception when copying archives", ex );
		}

		if ( runnable != null )
		{
			IDocumentWriter writer = runnable.getReportEngine( )
					.openDocumentWriter( target );
			writer.setRunnable( runnable );
			writer.close( );
		}

	}
}
