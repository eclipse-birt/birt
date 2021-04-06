/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;

/**
 * Label provider class - Implements the getNodeDisplayName method for this node
 * type.
 */
public class LabelProvider extends LabelElementProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * LabelElementProvider#getDescription(org.eclipse.birt.model.api.
	 * DesignElementHandle)
	 */
	protected String getDescription(DesignElementHandle handle) {
		return getDescription(((LabelHandle) handle).getText(), true);
	}
}