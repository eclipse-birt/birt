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

package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Base class for copy policies. Each copy policy has its own implement
 * strategy.
 */

abstract public class CopyPolicy {

	/**
	 * Each copy policy has its own implement strategy. A common interface for each
	 * kind of copy policy, which implements a specific copy strategy.
	 * 
	 * @param from the original data object
	 * @param to   the target data object
	 */
	abstract public void execute(DesignElement from, DesignElement to);
}
