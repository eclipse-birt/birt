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