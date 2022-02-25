/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.data.adapter.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;

/**
	 *
	 */

public class SecurityListener {

	public SecurityListener(DataRequestSessionImpl session) {

	}

	public void start(TabularCubeHandle cubeHandle) throws BirtException {
	}

	public void process(String dimName, DataSetIterator iterator) throws BirtException {

	}

	public void process(DimensionHandle dimHandle) throws BirtException {

	}

	public List<IFilterDefinition> populateSecurityFilter(String dimName, Map appContext) throws BirtException {
		return null;
	}

	public void end() throws BirtException {
	}
}
