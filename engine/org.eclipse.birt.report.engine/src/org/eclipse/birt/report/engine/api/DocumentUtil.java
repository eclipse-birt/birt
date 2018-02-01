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
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

public class DocumentUtil
{

	public static void copy( IArchiveFile source, IArchiveFile target,
			IReportRunnable runnable) throws EngineException
	{
		copy(source, target, runnable, new HashMap<String,String>());
	}
	
	public static void copy( IArchiveFile source, IArchiveFile target,
			IReportRunnable runnable, Map<String, String> transformations ) throws EngineException
	{
		try
		{
			ArchiveUtil.copy( source, target, transformations );
		}
		catch ( IOException ex )
		{
			throw new EngineException( MessageConstants.COPY_ARCHIVES_EXCEPTION, ex );
		}

		if ( runnable != null )
		{
			IDocumentWriter writer = runnable.getReportEngine( )
					.openDocumentWriter( target );
			writer.setRunnable( runnable );
			writer.close( );
		}

	}

	/**
	 * @return a collection of all bookmark info; null is returned if no
	 *         bookmark info.
	 */
	public static Collection<IBookmarkInfo> getBookmarks(
			IReportDocument document, Locale locale ) throws EngineException
	{
		if ( document instanceof IReportDocumentHelper )
		{
			IReportDocumentHelper helper = (IReportDocumentHelper) document;
			return helper.getBookmarkInfos( locale );
		}
		return null;
	}

	/**
	 * Judge whether it's a cube based on instance id
	 * 
	 * @param document
	 *            a report document
	 * @param instanceId
	 *            an instance id
	 * @return
	 */
	public static boolean isCube( IReportDocument document,
			InstanceID instanceId )
	{
		assert document != null;
		assert instanceId != null;

		ReportDesignHandle report = document.getReportDesign( );
		InstanceID iid = instanceId;
		while ( iid != null )
		{
			long id = iid.getComponentID( );
			DesignElementHandle handle = report.getElementByID( id );
			if ( handle instanceof ReportItemHandle )
			{
				ReportItemHandle rhandle = (ReportItemHandle) handle;
				DataSetHandle dsHandle = rhandle.getDataSet( );
				CubeHandle cbHandle = rhandle.getCube( );
				if ( dsHandle != null )
				{
					return false;
				}
				if ( cbHandle != null )
				{
					return true;
				}
			}
			iid = iid.getParentID( );
		}
		return false;
	}
}
