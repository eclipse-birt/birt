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

import org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI;

/**
 * An attribute page Presents some attributes of a DE element.
 */
public abstract class TabPage implements IPropertyTabUI {

	public void refresh() {
	}

	public String getTabDisplayName() {
		return null;
	}

}
