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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes;

import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.RowGrandTotalPage;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.RowPageBreak;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.RowSubTotalPage;

/**
 * RowAreaCategoryProviderFactory
 */
public class RowAreaCategoryProviderFactory extends CategoryProviderFactory {

	private static ICategoryProviderFactory instance = new RowAreaCategoryProviderFactory();

	public final static String SUB_TOTLES = "SubTotals"; //$NON-NLS-1$
	public final static String GRAND_TOTALS = "GrandTotals"; //$NON-NLS-1$
	public final static String PAGE_BREAK = "PageBreak"; //$NON-NLS-1$

	protected RowAreaCategoryProviderFactory() {
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
		return AttributesUtil.createCategoryProvider(null,
				new String[] { RowAreaCategoryProviderFactory.SUB_TOTLES, RowAreaCategoryProviderFactory.GRAND_TOTALS,
						RowAreaCategoryProviderFactory.PAGE_BREAK, },
				new String[] { Messages.getString("CrosstabPageGenerator.List.SubTotals"), //$NON-NLS-1$
						Messages.getString("CrosstabPageGenerator.List.GrandTotals"), //$NON-NLS-1$
						Messages.getString("CrosstabPageGenerator.List.PageBreak"), //$NON-NLS-1$
				}, new Class[] { RowSubTotalPage.class, RowGrandTotalPage.class, RowPageBreak.class, });
	}
}
