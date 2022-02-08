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

package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * This policy is a copy policy for pasting, which means, after copying, the
 * original object is deeply cloned, and the target object can be pasted to
 * every where.
 */

public class FlattenCopyPolicy extends CopyForPastePolicy {

	private final static FlattenCopyPolicy instance = new FlattenCopyPolicy();

	/**
	 * Auxiliary function helps to clear display name and display name id.
	 * 
	 * @param e the design element need to clear display name information.
	 */

	protected void clearDisplayName(DesignElement e) {
		// Need not clear display name when flatten design
	}

	public static FlattenCopyPolicy getInstance() {
		return instance;
	}

}
