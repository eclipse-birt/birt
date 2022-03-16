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

package org.eclipse.birt.report.data.adapter.api;

/**
 * Interface to get the resultset request info: start row index, return max row
 * etc. This interface is to cooperate with the method of
 * {@link DataRequestSession#getColumnValueSet(org.eclipse.birt.report.model.api.DataSetHandle, java.util.Iterator, java.util.Iterator, String, IRequestInfo)}.
 */
public interface IRequestInfo {

	/**
	 * @return the start row index, 0-based
	 */
	int getStartRow();

	/**
	 * @return the max row number
	 */
	int getMaxRow();

}
