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

package org.eclipse.birt.integration.wtp.ui.project.facet;

import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.exception.BirtCoreException;
import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.Logger;
import org.eclipse.birt.integration.wtp.ui.internal.util.WebArtifactUtil;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.WebAppBean;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.BirtWizardUtil;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.IBirtWizardConstants;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.SimpleImportOverwriteQuery;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.project.facet.J2EEFacetInstallDelegate;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Delegate class for invoke "INSTALL" event
 * 
 */
public class BirtFacetInstallDelegate extends J2EEFacetInstallDelegate
		implements
			IDelegate,
			IBirtFacetConstants,
			IBirtWizardConstants
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
		monitor.beginTask( "", 4 ); //$NON-NLS-1$

		try
		{
			IDataModel facetDataModel = (IDataModel) config;
			IDataModel masterDataModel = (IDataModel) facetDataModel
					.getProperty( FacetInstallDataModelProvider.MASTER_PROJECT_DM );

			// get web content folder
			String configFolder = BirtWizardUtil
					.getConfigFolder( masterDataModel );
			Map birtProperties = (Map) facetDataModel
					.getProperty( BirtFacetInstallDataModelProperties.BIRT_CONFIG );

			if ( configFolder == null )
			{
				String message = BirtWTPMessages.BIRTErrors_wrong_webcontent;
				Logger.log( Logger.ERROR, message );
				throw BirtCoreException.getException( message, null );
			}

			monitor.worked( 1 );

			// process BIRT Configuration
			preConfiguration( project, birtProperties, configFolder, monitor );

			monitor.worked( 1 );

			processConfiguration( project, birtProperties, monitor );

			monitor.worked( 1 );

			IFolder folder = BirtWizardUtil.getFolder( project, configFolder );
			IPath destPath = null;
			if ( folder != null )
				destPath = folder.getFullPath( );

			// import birt runtime componenet
			BirtWizardUtil.doImports( project, null, destPath, monitor,
					new SimpleImportOverwriteQuery( ) );

			monitor.worked( 1 );
		}
		finally
		{
			monitor.done( );
		}
	}

	/**
	 * Action before process configuration
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	protected void preConfiguration( IProject project, Map birtProperties,
			String configFolder, IProgressMonitor monitor )
			throws CoreException
	{
		// check folder settings
		BirtWizardUtil.processCheckFolder( birtProperties, project,
				configFolder, monitor );
	}

	/**
	 * Process BIRT deployment configuration.
	 * <p>
	 * Save user-defined settings into web.xml file.
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	protected void processConfiguration( IProject project, Map birtProperties,
			IProgressMonitor monitor ) throws CoreException
	{
		// Simple OverwriteQuery
		SimpleImportOverwriteQuery query = new SimpleImportOverwriteQuery( );

		// configure WebArtifact
		WebArtifactUtil.configureWebApp( (WebAppBean) birtProperties
				.get( EXT_WEBAPP ), project, query, monitor );

		WebArtifactUtil.configureContextParam( (Map) birtProperties
				.get( EXT_CONTEXT_PARAM ), project, query, monitor );

		WebArtifactUtil.configureListener( (Map) birtProperties
				.get( EXT_LISTENER ), project, query, monitor );

		WebArtifactUtil.configureServlet( (Map) birtProperties
				.get( EXT_SERVLET ), project, query, monitor );

		WebArtifactUtil.configureServletMapping( (Map) birtProperties
				.get( EXT_SERVLET_MAPPING ), project, query, monitor );

		WebArtifactUtil
				.configureFilter( (Map) birtProperties.get( EXT_FILTER ),
						project, query, monitor );

		WebArtifactUtil.configureFilterMapping( (Map) birtProperties
				.get( EXT_FILTER_MAPPING ), project, query, monitor );

		WebArtifactUtil
				.configureTaglib( (Map) birtProperties.get( EXT_TAGLIB ),
						project, query, monitor );
	}
}
