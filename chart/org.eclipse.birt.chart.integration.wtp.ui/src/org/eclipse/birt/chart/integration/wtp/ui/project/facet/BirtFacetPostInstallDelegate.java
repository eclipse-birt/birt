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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.project.facet.J2EEFacetInstallDelegate;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Delegate class for invoke "POST_INSTALL" event
 *
 */
public final class BirtFacetPostInstallDelegate extends J2EEFacetInstallDelegate implements IDelegate {

	/**
	 * Invoke "POST_INSTALL" event for project facet
	 *
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject,
	 *      org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
	 *      java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor)
			throws CoreException {

	}

}
