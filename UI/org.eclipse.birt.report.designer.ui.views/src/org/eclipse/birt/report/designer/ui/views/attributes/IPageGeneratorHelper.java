/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/
package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.List;

/**
 * 
 */

public interface IPageGeneratorHelper {

	public String[] createTabItems(List input);

	public TabPage buildTabContent(String tabKey);

}
