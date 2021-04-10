/*******************************************************************************
 * Copyright (c) 2004,2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.IFilterUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;

public class FilterUtil implements IFilterUtil {

	/*
	 * 
	 */
	public Map<String, List<String>> populatedNonPushdownableFilterOperators(DataSetHandle dataSet, int filterType)
			throws BirtException {
		throw new UnsupportedOperationException();
	}
}
