/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.project.facet;

import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.exception.BirtCoreException;
import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.Logger;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.WebAppBean;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.BirtWizardUtil;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.IBirtWizardConstants;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.SimpleImportOverwriteQuery;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
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
		implements IDelegate, IBirtFacetConstants, IBirtWizardConstants {

	/**
	 * Invoke "INSTALL" event for project facet
	 * 
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject,
	 *      org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
	 *      java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor)
			throws CoreException {
		monitor.beginTask("", 4); //$NON-NLS-1$

		try {
			IDataModel facetDataModel = (IDataModel) config;
			IDataModel masterDataModel = (IDataModel) facetDataModel
					.getProperty(FacetInstallDataModelProvider.MASTER_PROJECT_DM);

			IPath destPath = null;
			// get web content folder
			if (masterDataModel != null) {
				String configFolder = BirtWizardUtil.getConfigFolder(masterDataModel);

				if (configFolder != null) {
					IFolder folder = BirtWizardUtil.getFolder(project, configFolder);
					if (folder != null)
						destPath = folder.getFullPath();
				}
			} else {
				destPath = BirtWizardUtil.getWebContentPath(project);
			}

			if (destPath == null) {
				String message = BirtWTPMessages.BIRTErrors_wrong_webcontent;
				Logger.log(Logger.ERROR, message);
				throw BirtCoreException.getException(message, null);
			}

			Map birtProperties = (Map) facetDataModel.getProperty(BirtFacetInstallDataModelProvider.BIRT_CONFIG);

			monitor.worked(1);

			// process BIRT Configuration
			preConfiguration(project, birtProperties, destPath.toFile().getName(), monitor);

			monitor.worked(1);

			processConfiguration(project, birtProperties, monitor);

			monitor.worked(1);

			// import birt runtime componenet
			BirtWizardUtil.doImports(project, null, destPath, monitor, new SimpleImportOverwriteQuery());

			monitor.worked(1);
		} finally {
			monitor.done();
		}
	}

	/**
	 * Action before process configuration
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	protected void preConfiguration(IProject project, Map birtProperties, String configFolder, IProgressMonitor monitor)
			throws CoreException {
		// check folder settings
		BirtWizardUtil.processCheckFolder(birtProperties, project, configFolder, monitor);
	}

	/**
	 * Process BIRT deployment configuration.
	 * <p>
	 * Save user-defined settings into web.xml file.
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	protected void processConfiguration(final IProject project, final Map birtProperties,
			final IProgressMonitor monitor) throws CoreException {
		// Simple OverwriteQuery
		final SimpleImportOverwriteQuery query = new SimpleImportOverwriteQuery();
		IModelProvider modelProvider = ModelProviderManager.getModelProvider(project);
		IPath modelPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
		boolean exists = project.getProjectRelativePath().append(modelPath).toFile().exists();
		if (BirtFacetUtilFactory.isWebApp25(modelProvider.getModelObject()) && !exists) {
			modelPath = IModelProvider.FORCESAVE;
		}
		final IBirtFacetUtil util = BirtFacetUtilFactory.getInstance(modelProvider.getModelObject());
		modelProvider.modify(new Runnable() {
			public void run() {
				util.configureWebApp((WebAppBean) birtProperties.get(EXT_WEBAPP), project, query, monitor);
				util.configureContextParam((Map) birtProperties.get(EXT_CONTEXT_PARAM), project, query, monitor);
				util.configureListener((Map) birtProperties.get(EXT_LISTENER), project, query, monitor);

				util.configureServlet((Map) birtProperties.get(EXT_SERVLET), project, query, monitor);

				util.configureServletMapping((Map) birtProperties.get(EXT_SERVLET_MAPPING), project, query, monitor);

				util.configureFilter((Map) birtProperties.get(EXT_FILTER), project, query, monitor);

				util.configureFilterMapping((Map) birtProperties.get(EXT_FILTER_MAPPING), project, query, monitor);

				util.configureTaglib((Map) birtProperties.get(EXT_TAGLIB), project, query, monitor);
			}
		}, modelPath);

	}
}
