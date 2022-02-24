/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.ui.wizards;

public interface IChangeListener {

	/**
	 * Called with false when the listener is first attached to the model, and
	 * called with true every time the model's state changes.
	 */
	void update(boolean changed);
}
