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

package org.eclipse.birt.report.model.adapter.oda;

import java.util.List;

/**
 * Interface to give out all the ambiguous information when comparing data set
 * design and data set handle.
 * 
 */

public interface IAmbiguousOption {

	/**
	 * Gets all the ambiguous parameter nodes when comparing data set design and
	 * data set handle.
	 * 
	 * @return
	 */
	List<IAmbiguousParameterNode> getAmbiguousParameters();

	/**
	 * 
	 * @return
	 */
	List<IAmbiguousResultSetNode> getAmbiguousResultSets();
}
