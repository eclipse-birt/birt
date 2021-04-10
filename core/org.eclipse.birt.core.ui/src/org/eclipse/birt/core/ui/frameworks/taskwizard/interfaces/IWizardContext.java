/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces;

/**
 * @author Actuate Corporation
 *
 */
public interface IWizardContext {
	/**
	 * Returns a String specifying the wizard whose context this is. This
	 * information can be used by tasks that are used in multiple wizards to
	 * configure their appearance or behavior based on the wizard in which they are
	 * located.
	 * 
	 * @return String specifying the ID of the wizard with which this context is
	 *         associated.
	 */
	public String getWizardID();

	/**
	 * Determine whether the chart will be redrawn by resizing canvas. We need to
	 * disable it sometimes, e.g. when creating the UI.
	 * 
	 * @param refreshByResizing The bRefreshByResizing to set.
	 */

}
