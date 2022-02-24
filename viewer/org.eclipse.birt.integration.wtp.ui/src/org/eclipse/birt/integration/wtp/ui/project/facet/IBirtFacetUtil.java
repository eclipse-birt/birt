/*************************************************************************************
 * Copyright (c) 2008 JBoss, a division of Red Hat and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     JBoss, a division of Red Hat - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.project.facet;

import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.webapplication.WebAppBean;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.SimpleImportOverwriteQuery;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * @author snjeza
 * 
 */
public interface IBirtFacetUtil {

	public void configureWebApp(WebAppBean webAppBean, IProject project, SimpleImportOverwriteQuery query,
			IProgressMonitor monitor);

	public void configureContextParam(Map map, IProject project, SimpleImportOverwriteQuery query,
			IProgressMonitor monitor);

	public void configureListener(Map map, IProject project, SimpleImportOverwriteQuery query,
			IProgressMonitor monitor);

	public void configureServlet(Map map, IProject project, SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	public void configureServletMapping(Map map, IProject project, SimpleImportOverwriteQuery query,
			IProgressMonitor monitor);

	public void configureFilter(Map map, IProject project, SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	public void configureFilterMapping(Map map, IProject project, SimpleImportOverwriteQuery query,
			IProgressMonitor monitor);

	public void configureTaglib(Map map, IProject project, SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	public void initializeWebapp(Map map, IProject project);
}
