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

package org.eclipse.birt.chart.integration.wtp.ui.project.facet;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.integration.wtp.ui.internal.util.WebArtifactUtil;
import org.eclipse.birt.chart.integration.wtp.ui.internal.webapp.WebAppBean;
import org.eclipse.birt.chart.integration.wtp.ui.internal.wizards.BirtWizardUtil;
import org.eclipse.birt.chart.integration.wtp.ui.internal.wizards.IBirtWizardConstants;
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
public class BirtFacetInstallDelegate extends J2EEFacetInstallDelegate implements IDelegate {

	/**
	 * Invoke "INSTALL" event for project facet
	 * 
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject,
	 *      org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
	 *      java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor)
			throws CoreException {
		if (monitor != null) {
			monitor.beginTask("", 1); //$NON-NLS-1$
		}

		try {
			final IDataModel model = (IDataModel) config;

			// get destination path
			IDataModel dataModel = (IDataModel) model.getProperty("FacetInstallDataModelProvider.MASTER_PROJECT_DM"); //$NON-NLS-1$
			IPath destPath = null;

			if (dataModel != null) {
				String dest = BirtWizardUtil.getConfigFolder(dataModel);
				IFolder folder = BirtWizardUtil.getFolder(project, dest);

				if (folder != null)
					destPath = folder.getFullPath();
			}

			if (destPath == null) {
				destPath = BirtWizardUtil.getWebContentPath(project);
			}

			// import birt runtime componenet
			BirtWizardUtil.doImports(project, null, destPath, monitor, new SimpleImportOverwriteQuery());

			Map properties = new HashMap();

			// initialize webapp settings from Extension
			BirtWizardUtil.initWebapp(properties);

			// initialize webapp settings from existed web.xml
			WebArtifactUtil.initializeWebapp(properties, project);

			// process defined folders
			BirtWizardUtil.processCheckFolder(properties, project, destPath.toFile().getName(), monitor);

			// configurate web.xml
			processConfiguration(properties, project, monitor);
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	protected void processConfiguration(Map properties, IProject project, IProgressMonitor monitor)
			throws CoreException {
		SimpleImportOverwriteQuery query = new SimpleImportOverwriteQuery();

		// configure WebArtifact
		WebArtifactUtil.configureWebApp((WebAppBean) properties.get(IBirtWizardConstants.EXT_WEBAPP), project, query,
				monitor);

		WebArtifactUtil.configureContextParam((Map) properties.get(IBirtWizardConstants.EXT_CONTEXT_PARAM), project,
				query, monitor);

		WebArtifactUtil.configureListener((Map) properties.get(IBirtWizardConstants.EXT_LISTENER), project, query,
				monitor);

		WebArtifactUtil.configureServlet((Map) properties.get(IBirtWizardConstants.EXT_SERVLET), project, query,
				monitor);

		WebArtifactUtil.configureServletMapping((Map) properties.get(IBirtWizardConstants.EXT_SERVLET_MAPPING), project,
				query, monitor);

		WebArtifactUtil.configureTaglib((Map) properties.get(IBirtWizardConstants.EXT_TAGLIB), project, query, monitor);
	}
}
