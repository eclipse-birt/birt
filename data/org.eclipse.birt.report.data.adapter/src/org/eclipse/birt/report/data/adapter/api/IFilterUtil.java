/*******************************************************************************
 * Copyright (c) 2004,2012 Actuate Corporation.
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

import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.DataSetHandle;

/**
 * The filter util interface definition that is used for filter related
 * optimization job.
 */
public interface IFilterUtil {
	public Map<String, List<String>> populatedNonPushdownableFilterOperators(DataSetHandle dataSet, int filterType)
			throws BirtException;
}
