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
	public int getStartRow();

	/**
	 * @return the max row number
	 */
	public int getMaxRow();

}
