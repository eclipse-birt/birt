/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.ui.views.attributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.CrosstabEventHandlerPage;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.CrosstabGeneralPage;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.CrosstabSectionPage;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.EmptyRowColumnPage;

/**
 * CrosstabCategoryProviderFactory
 */
public class CrosstabCategoryProviderFactory extends CategoryProviderFactory {

	private static final String CATEGORY_KEY_EMPTYROWCOLUMN = "EmptyRowColumn"; //$NON-NLS-1$

	private static ICategoryProviderFactory instance = new CrosstabCategoryProviderFactory();

	protected CrosstabCategoryProviderFactory() {
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
		CategoryHolder customHolder = new CategoryHolder(
				new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_SECTION, CATEGORY_KEY_EMPTYROWCOLUMN, },
				new String[] { Messages.getString("CrosstabPageGenerator.List.General"), //$NON-NLS-1$
						Messages.getString("CrosstabPageGenerator.List.Section"), //$NON-NLS-1$
						Messages.getString("CrosstabPageGenerator.List.EmptyRowColumn"), //$NON-NLS-1$
				}, new Class[] { CrosstabGeneralPage.class, CrosstabSectionPage.class, EmptyRowColumnPage.class, });

		List<String> categories = new ArrayList<String>(
				Arrays.asList(new String[] { null, CATEGORY_KEY_MARGIN, CATEGORY_KEY_FONT, CATEGORY_KEY_BORDERS, null,
						CATEGORY_KEY_VISIBILITY, null, CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS,
						CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, }));

		if (AttributesUtil.containCategory(AttributesUtil.EVENTHANDLER)) {
			customHolder.insertBefore(null, AttributesUtil.EVENTHANDLER,
					AttributesUtil.getCategoryDisplayName(AttributesUtil.EVENTHANDLER), CrosstabEventHandlerPage.class);

			categories.add(categories.size() - 1, null);
		}

		return AttributesUtil.createCategoryProvider(categories.toArray(new String[categories.size()]),
				customHolder.getKeys(), customHolder.getLabels(), customHolder.getClasses());

	}
}
