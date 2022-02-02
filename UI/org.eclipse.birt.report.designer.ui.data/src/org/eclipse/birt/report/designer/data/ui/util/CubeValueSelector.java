/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.designer.data.ui.util;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DimensionLevel;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;

public class CubeValueSelector {
	public static Iterator getMemberValueIterator(DataRequestSession session, TabularCubeHandle cubeHandle,
			String dataBindingExpr, ICubeQueryDefinition queryDefn) throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, dataBindingExpr, queryDefn);
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, CubeHandle cubeHandle,
			String dataBindingExpr, ICubeQueryDefinition queryDefn) throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, dataBindingExpr, queryDefn, null);
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, CubeHandle cubeHandle, String targetLevel,
			DimensionLevel[] dimensionLevels, Object[] values) throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, targetLevel, dimensionLevels, values,
				null);
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, TabularCubeHandle cubeHandle,
			String targetLevel, DimensionLevel[] dimensionLevels, Object[] values) throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, targetLevel, dimensionLevels, values,
				null);
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, TabularCubeHandle cubeHandle,
			String targetLevel, ILevelDefinition[] higherLevelDefns, Object[] values) throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, targetLevel, higherLevelDefns, values,
				null);
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, CubeHandle cubeHandle,
			String dataBindingExpr, ICubeQueryDefinition queryDefn, Map appContext) throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, dataBindingExpr, queryDefn,
				session.getDataSessionContext().getAppContext());
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, TabularCubeHandle cubeHandle,
			String dataBindingExpr, ICubeQueryDefinition queryDefn, Map appContext) throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, dataBindingExpr, queryDefn, appContext);
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, CubeHandle cubeHandle, String targetLevel,
			DimensionLevel[] dimensionLevels, Object[] values, Map appContext) throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, targetLevel, dimensionLevels, values,
				appContext);
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, TabularCubeHandle cubeHandle,
			String targetLevel, DimensionLevel[] dimensionLevels, Object[] values, Map appContext)
			throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, targetLevel, dimensionLevels, values,
				appContext);
	}

	public static Iterator getMemberValueIterator(DataRequestSession session, TabularCubeHandle cubeHandle,
			String targetLevel, ILevelDefinition[] higherLevelDefns, Object[] values, Map appContext)
			throws BirtException {
		return session.getCubeQueryUtil().getMemberValueIterator(cubeHandle, targetLevel, higherLevelDefns, values,
				appContext);

	}
}
