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

package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * IDECategoryProviderFactory
 */
public class IDECategoryProviderFactory extends CategoryProviderFactory {

	private static ICategoryProviderFactory instance = new IDECategoryProviderFactory();

	protected IDECategoryProviderFactory() {
	}

	public static ICategoryProviderFactory getInstance() {
		return instance;
	}

	public static final String CATEGORY_KEY_EVENTHANDLER = AttributesUtil.EVENTHANDLER;

	protected CategoryHolder getCategories(String elementName) {
		CategoryHolder holder = super.getCategories(elementName);

		if (holder == null) {
			return null;
		}

		if (ReportDesignConstants.CELL_ELEMENT.equals(elementName)
				|| ReportDesignConstants.COLUMN_ELEMENT.equals(elementName)
				|| ReportDesignConstants.DATA_ITEM.equals(elementName)
				|| ReportDesignConstants.GRID_ITEM.equals(elementName)
				|| ReportDesignConstants.IMAGE_ITEM.equals(elementName)
				|| ReportDesignConstants.LABEL_ITEM.equals(elementName)
				|| ReportDesignConstants.LIST_ITEM.equals(elementName)
				|| ReportDesignConstants.REPORT_DESIGN_ELEMENT.equals(elementName)
				|| ReportDesignConstants.ROW_ELEMENT.equals(elementName)
				|| ReportDesignConstants.TABLE_ITEM.equals(elementName)
				|| ReportDesignConstants.TEXT_DATA_ITEM.equals(elementName)
				|| ReportDesignConstants.TEXT_ITEM.equals(elementName)
				|| ReportDesignConstants.AUTOTEXT_ITEM.equals(elementName)
		// || ReportDesignConstants.PARAMETER_GROUP_ELEMENT.equals( elementName
		// )
		// || ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT.equals(
		// elementName )
		// || ReportDesignConstants.SCALAR_PARAMETER_ELEMENT.equals( elementName
		// )
		) {
			holder.insertBefore(CATEGORY_KEY_ADVANCEPROPERTY, CATEGORY_KEY_EVENTHANDLER,
					Messages.getString("ReportPageGenerator.List.EventHandler"), //$NON-NLS-1$
					HandlerPage.class);
		}

		if (ReportDesignConstants.DATA_ITEM.equals(elementName) || ReportDesignConstants.IMAGE_ITEM.equals(elementName)
				|| ReportDesignConstants.LABEL_ITEM.equals(elementName)) {
			holder.replace(CATEGORY_KEY_HYPERLINK, null, IDEHyperLinkPage.class);
		}

		return holder;
	}

	protected CategoryHolder getCategories(DesignElementHandle handle) {
		CategoryHolder holder = super.getCategories(handle);

		if (holder == null) {
			return null;
		}

		if (handle instanceof DataSourceHandle || handle instanceof DataSetHandle) {
			holder.insertBefore(CATEGORY_KEY_ADVANCEPROPERTY, CATEGORY_KEY_EVENTHANDLER,
					Messages.getString("ReportPageGenerator.List.EventHandler"), //$NON-NLS-1$
					HandlerPage.class);
		}

		return holder;
	}
}
