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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.jst.common.project.facet.JavaFacetUtils;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetUtils;
import org.eclipse.wst.common.project.facet.core.IDynamicPreset;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectBase;
import org.eclipse.wst.common.project.facet.core.IPresetFactory;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.PresetDefinition;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * Preset factory for the Chart runtime component.
 */
public class BirtFacetPresetFactory implements IPresetFactory {

	/**
	 * Creates a preset for the web module version >= 2.3
	 * 
	 * @see org.eclipse.wst.common.project.facet.core.IPresetFactory#createPreset(java.lang.String,
	 *      java.util.Map)
	 */
	public PresetDefinition createPreset(final String presetId, final Map<String, Object> context)
			throws CoreException {
		final IFacetedProjectBase project = (IFacetedProjectBase) context
				.get(IDynamicPreset.CONTEXT_KEY_FACETED_PROJECT);

		final IProjectFacetVersion webFacetVersion = project.getProjectFacetVersion(WebFacetUtils.WEB_FACET);

		if (webFacetVersion != null && webFacetVersion.compareTo(WebFacetUtils.WEB_23) >= 0) {
			final Set<IProjectFacetVersion> facets = new HashSet<IProjectFacetVersion>();

			final IProjectFacet birtFacet = ProjectFacetsManager.getProjectFacet("birt.chart.runtime"); //$NON-NLS-1$

			final IProjectFacetVersion chartFacetVersion = birtFacet.getVersion("4.5.0"); //$NON-NLS-1$

			facets.add(chartFacetVersion);
			facets.add(webFacetVersion);
			facets.add(JavaFacetUtils.JAVA_50);

			return new PresetDefinition(Resources.BIRT_FACET_TEMPLATE_LABEL, Resources.BIRT_FACET_TEMPLATE_DESCRIPTION,
					facets);
		}

		return null;
	}

	/**
	 * Loads preset resources.
	 */
	private static final class Resources extends NLS

	{

		public static String BIRT_FACET_TEMPLATE_LABEL;
		public static String BIRT_FACET_TEMPLATE_DESCRIPTION;

		static {
			initializeMessages("plugin", Resources.class); //$NON-NLS-1$
		}
	}

}
