/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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
