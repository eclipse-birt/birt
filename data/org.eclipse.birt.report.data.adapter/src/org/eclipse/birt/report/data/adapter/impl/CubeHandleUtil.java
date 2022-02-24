
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

import java.util.Date;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;

/**
 * 
 */

public class CubeHandleUtil {
	public static void defineCube(DataEngineImpl dataEngine, CubeHandle handle, Map appContext) throws BirtException {
	}

	public static boolean isTimeDimension(DimensionHandle dimension) {
		return false;
	}

	public static Date getStartTime(DimensionHandle dimension) {
		return null;
	}

	public static Date getEndTime(DimensionHandle dimension) {
		return null;
	}

}
