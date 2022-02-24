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

import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;

/**
 * Interface to provide the one ambiguous parameter node. It defines the oda
 * result set column handle and a list of <code>IAmbiguousAttribute</code>.
 *
 */
public interface IAmbiguousResultSetNode {

	/**
	 * Gets the oda result set column handle that has ambiguous information compared
	 * with that is in data set design.
	 *
	 * @return
	 */
	OdaResultSetColumnHandle getOdaResultSetColumnHandle();

	/**
	 * Gets a list of ambiguous attributes in the given oda result set column
	 * handle.
	 *
	 * @return
	 */
	List<IAmbiguousAttribute> getAmbiguousAttributes();
}
