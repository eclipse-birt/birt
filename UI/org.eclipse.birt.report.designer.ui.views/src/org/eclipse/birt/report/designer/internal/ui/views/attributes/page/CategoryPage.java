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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage;
import org.eclipse.birt.report.designer.ui.views.attributes.TabPage;

/**
 * CategoryPage
 */
public class CategoryPage implements ICategoryPage {

	private String displayLabel;
	private String displayTitle;
	private Class pageClass;
	private String categoryKey;

	public CategoryPage(String categoryKey, String displayLabel, Class pageClass) {
		this.categoryKey = categoryKey;
		this.displayLabel = displayLabel;
		this.displayTitle = displayLabel;
		assert TabPage.class.isAssignableFrom(pageClass);

		this.pageClass = pageClass;
	}

	public CategoryPage(String categoryKey, String displayLabel, String displayTitle, Class pageClass) {
		this.categoryKey = categoryKey;
		this.displayLabel = displayLabel;
		this.displayTitle = displayTitle;
		assert TabPage.class.isAssignableFrom(pageClass);

		this.pageClass = pageClass;
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage#getDisplayLabel()
	 */
	public String getDisplayLabel() {
		return displayLabel;
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage#getDisplayLabel()
	 */
	public String getDisplayTitle() {
		return displayTitle;
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage#createPage(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
	public TabPage createPage() {
		try {
			return (TabPage) pageClass.getConstructor(null).newInstance(null);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			return null;
		}
	}

	public String getCategoryKey() {
		return categoryKey;
	}
}
