/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.attributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.page.ChartAlterPage;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.page.ChartEventHandlerPage;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.page.ChartGeneralPage;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;

/**
 * The default implement of ICategoryProviderFactory
 */

public class ChartCategoryProviderFactory extends CategoryProviderFactory {

	private static ICategoryProviderFactory instance = new ChartCategoryProviderFactory();

	protected ChartCategoryProviderFactory() {
	}

	/**
	 * 
	 * @return The unique CategoryProviderFactory instance
	 */
	public static ICategoryProviderFactory getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.views.attributes.providers.
	 * ICategoryProviderFactory#getCategoryProvider(java.lang.Object)
	 */
	public ICategoryProvider getCategoryProvider(Object input) {
		CategoryHolder customHolder = new CategoryHolder(new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_ALTTEXT, },
				new String[] { Messages.getString("ChartPageGenerator.List.General"), //$NON-NLS-1$
						Messages.getString("ChartPageGenerator.List.AltText"), //$NON-NLS-1$
				}, new Class[] { ChartGeneralPage.class, ChartAlterPage.class, });

		List<String> categories = new ArrayList<String>(Arrays
				.asList(new String[] { null, CATEGORY_KEY_BORDERS, CATEGORY_KEY_MARGIN, null, CATEGORY_KEY_SECTION,
						CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS,
						CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, }));

		if (AttributesUtil.containCategory(AttributesUtil.EVENTHANDLER)) {
			customHolder.insertBefore(null, AttributesUtil.EVENTHANDLER,
					AttributesUtil.getCategoryDisplayName(AttributesUtil.EVENTHANDLER), ChartEventHandlerPage.class);

			categories.add(categories.size() - 1, null);
		}

		return AttributesUtil.createCategoryProvider(categories.toArray(new String[categories.size()]),
				customHolder.getKeys(), customHolder.getLabels(), customHolder.getClasses());
	}
}
