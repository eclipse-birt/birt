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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * @author Administrator
 * 
 */
public class CrosstabSimpleComboPropertyDescriptorProvider extends
		SimpleComboPropertyDescriptorProvider {

	public CrosstabSimpleComboPropertyDescriptorProvider(String property,
			String element) {
		super(property, element);
	}

	public String[] getItems() {
		String[] items = null;
		items = super.getItems();
		if (items != null) {
			return items;
		}
		Object selecteObj = input;
		if (input instanceof List ) {
			selecteObj = ((List)input).get(0);
		}

		ExtendedItemHandle handle = (ExtendedItemHandle) selecteObj;
		if (!handle.getExtensionName().equals("Crosstab")) {
			return items;
		}

		if (ICrosstabReportItemConstants.CUBE_PROP.equals(getProperty())) {
			items = ChoiceSetFactory.getCubes();
		}

		return items;
	}

	public boolean isSpecialProperty() {
		if (ICrosstabReportItemConstants.CUBE_PROP.equals(getProperty())) {
			return true;
		} else
			return super.isSpecialProperty();

	}
	
	public String getDisplayName( )
	{
		if (ICrosstabReportItemConstants.CUBE_PROP.equals(getProperty())) {
			return Messages.getString("Element.ReportElement.Cube");
		}else
		{
			return super.getDisplayName();
		}
		
	}
	


}
