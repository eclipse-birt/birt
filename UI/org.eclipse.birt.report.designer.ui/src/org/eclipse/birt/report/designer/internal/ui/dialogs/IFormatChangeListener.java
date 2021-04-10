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