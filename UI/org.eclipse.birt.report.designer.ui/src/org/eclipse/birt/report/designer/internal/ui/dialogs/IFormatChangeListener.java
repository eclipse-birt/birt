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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.EventListener;

/**
 * The listener for format changes.
 * 
 */

public interface IFormatChangeListener extends EventListener {

	/**
	 * Notification that a format has changed.
	 * <p>
	 * This method gets called when the observed object fires a format change event.
	 * </p>
	 * 
	 * @param event The format change event object describing which format changed
	 *              and how.
	 */

	public void formatChange(FormatChangeEvent event);
}
