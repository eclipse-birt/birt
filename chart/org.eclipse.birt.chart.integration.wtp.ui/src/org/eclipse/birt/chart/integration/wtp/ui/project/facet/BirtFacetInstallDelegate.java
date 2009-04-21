/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.chart.integration.wtp.ui.project.facet;

import org.eclipse.birt.chart.integration.wtp.ui.internal.wizards.BirtWizardUtil;
import org.eclipse.birt.chart.integration.wtp.ui.internal.wizards.SimpleImportOverwriteQuery;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.project.facet.J2EEFacetInstallDelegate;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Delegate class for invoke "INSTALL" event
 * 
 */
public class BirtFacetInstallDelegate extends J2EEFacetInstallDelegate
		implements
			IDelegate
{

	/**
	 * Invoke "INSTALL" event for project facet
	 * 
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject,
	 *      org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
	 *      java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute( IProject project, IProjectFacetVersion fv,
			Object config, IProgressMonitor monitor ) throws CoreException
	{
		if ( monitor != null )
		{
			monitor.beginTask( "", 1 ); //$NON-NLS-1$
		}

		try
		{
			final IDataModel model = (IDataModel) config;

			// get destination path
			IDataModel dataModel = (IDataModel) model.getProperty( "FacetInstallDataModelProvider.MASTER_PROJECT_DM" ); //$NON-NLS-1$
			IPath destPath = null;

			if ( dataModel != null )
			{
				String dest = BirtWizardUtil.getConfigFolder( dataModel );
				IFolder folder = BirtWizardUtil.getFolder( project, dest );

				if ( folder != null )
					destPath = folder.getFullPath( );
			}
			else
			{
				destPath = BirtWizardUtil.getWebContentPath( project );
			}

			// import birt runtime componenet
			BirtWizardUtil.doImports( project,
					null,
					destPath,
					monitor,
					new SimpleImportOverwriteQuery( ) );
		}
		finally
		{
			if ( monitor != null )
			{
				monitor.done( );
			}
		}
	}

}
