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

package org.eclipse.birt.report.designer.ui.views.attributes;

/**
 * Instances of ColumnPageGenerator take change of creating attribute page
 * correspond to TableColumn element.
 */
public class ColumnPageGenerator extends BasePageGenerator {

	protected void createTabItems() {
		createTabItem(MAPTITLE, ATTRIBUTESTITLE);
		createTabItem(HIGHLIGHTSTITLE, MAPTITLE);
	}
}