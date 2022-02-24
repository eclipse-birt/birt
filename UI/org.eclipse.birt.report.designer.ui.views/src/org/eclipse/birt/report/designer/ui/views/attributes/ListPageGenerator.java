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

package org.eclipse.birt.report.designer.ui.views.attributes;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.GroupHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;

/**
 * Instances of TablePageGenerator take change of creating attribute page
 * correspond to DE Table element.
 */
public class ListPageGenerator extends BasePageGenerator {

	protected TabPage buildTabContent(String tabKey) {
		if (tabKey.equals(GROUPSSTITLE)) {
			return new FormPage(FormPropertyDescriptor.FULL_FUNCTION, new GroupHandleProvider(), true, true);
		}

		return super.buildTabContent(tabKey);
	}

	protected boolean isSupportAggregationBinding() {
		return true;
	}

	protected void createTabItems() {
		createTabItem(BINDINGTITLE, ATTRIBUTESTITLE);
		createTabItem(GROUPSSTITLE, BINDINGTITLE);
		createTabItem(MAPTITLE, GROUPSSTITLE);
		createTabItem(HIGHLIGHTSTITLE, MAPTITLE);
		createTabItem(SORTINGTITLE, HIGHLIGHTSTITLE);
		createTabItem(FILTERTITLE, SORTINGTITLE);
	}
}
