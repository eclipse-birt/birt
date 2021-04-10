
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

/**
 * This utility class provides util methods that are used by Data Engine's
 * supporing to PLS features.
 */

public final class PLSUtil {
	/**
	 * Determine whether a query is PLSEnabled.
	 * 
	 * @param queryDefn
	 * @return
	 */
	public static boolean isPLSEnabled(IQueryDefinition queryDefn) {
		return queryDefn.getQueryExecutionHints() != null
				&& queryDefn.getQueryExecutionHints().getTargetGroupInstances().size() > 0;
	}

	/**
	 * Indicates whether need to update the data set data in report document.
	 * 
	 * @param queryDefn
	 * @param manager
	 * @return
	 * @throws DataException
	 */
	public static boolean needUpdateDataSet(IQueryDefinition queryDefn, StreamManager manager) throws DataException {
		assert queryDefn != null;
		assert queryDefn.getQueryExecutionHints() != null;
		if (queryDefn.getQueryExecutionHints().getTargetGroupInstances().size() == 0)
			return false;
		PLSInfo plsInfo = readPLSInfo(manager);
		int currentRequestedGroupLevel = getOutmostPlsGroupLevel(queryDefn);
		return plsInfo.groupLevel == null || plsInfo.groupLevel < currentRequestedGroupLevel;
	}

	public static boolean isRowIdSaved(StreamManager manager) {
		return readPLSInfo(manager).rowIdSaved;
	}

	private static PLSInfo readPLSInfo(StreamManager manager) {
		PLSInfo plsInfo = new PLSInfo(null, false);
		RAInputStream in = null;
		try {
			if (manager.hasInStream(DataEngineContext.PLS_GROUPLEVEL_STREAM, StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE))
				in = manager.getInStream(DataEngineContext.PLS_GROUPLEVEL_STREAM, StreamManager.ROOT_STREAM,
						StreamManager.BASE_SCOPE);
			if (in != null) {
				plsInfo.groupLevel = IOUtil.readInt(in);
				try {
					plsInfo.rowIdSaved = IOUtil.readBool(in);
				} catch (IOException e) {
					// This item might not exist if the stream is old version
				}
			}
		} catch (Exception e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return plsInfo;
	}

	static class PLSInfo {
		public Integer groupLevel;
		public boolean rowIdSaved;

		public PLSInfo(Integer groupLevel, boolean rowIdSaved) {
			this.groupLevel = groupLevel;
			this.rowIdSaved = rowIdSaved;
		}
	}

	/**
	 * 
	 * @param queryDefn
	 * @return
	 */
	public static int getOutmostPlsGroupLevel(IQueryDefinition queryDefn) {
		assert queryDefn != null;
		assert queryDefn.getQueryExecutionHints() != null;
		int currentRequestedGroupLevel = 0;
		for (IGroupInstanceInfo info : queryDefn.getQueryExecutionHints().getTargetGroupInstances()) {
			currentRequestedGroupLevel = info.getGroupLevel() > currentRequestedGroupLevel ? info.getGroupLevel()
					: currentRequestedGroupLevel;
		}
		return currentRequestedGroupLevel;
	}

	/**
	 * 
	 * @param query
	 * @param targetGroups
	 * @return
	 */
	private static List<String> getReCalGroupNames(IQueryDefinition query, List<IGroupInstanceInfo> targetGroups) {
		int groupLevel = Integer.MAX_VALUE;
		for (IGroupInstanceInfo instance : targetGroups) {
			if (groupLevel > instance.getGroupLevel())
				groupLevel = instance.getGroupLevel();
		}

		List groups = query.getGroups();
		List<String> reCalGroups = new ArrayList<String>();
		for (int i = groupLevel - 1; i < groups.size(); i++) {
			reCalGroups.add(((IGroupDefinition) groups.get(i)).getName());
		}
		return reCalGroups;
	}

	/**
	 * Construct a binding's representative in ResultClass.
	 * 
	 * @param originalBindingName
	 * @return
	 */
	public static String constructNonReCalBindingDataSetName(String originalBindingName) {
		return "${RE_CAL:" + originalBindingName + "}$";
	}

	/**
	 * The binding expression should have been processed in
	 * PreparedIVDataSourceQuery.
	 * 
	 * @param binding
	 * @return
	 */
	public static boolean isPLSProcessedBinding(IBinding binding) {
		try {
			if (binding.getExpression() instanceof IScriptExpression) {
				String columnName = ExpressionUtil
						.getColumnName(((IScriptExpression) binding.getExpression()).getText());

				if (columnName != null && columnName.startsWith("${RE_CAL:"))
					return true;
			}
		} catch (BirtException e) {
			// Igonre
		}
		return false;

	}

	/**
	 * Prepare the binding for a query definition.
	 * 
	 * @param queryDefn
	 * @return
	 * @throws DataException
	 */
	public static IQueryDefinition populateBindings(IQueryDefinition queryDefn) throws DataException {
		try {

			List<String> reCalGroupNames = getReCalGroupNames(queryDefn,
					queryDefn.getQueryExecutionHints().getTargetGroupInstances());
			Iterator<IBinding> bindingIt = queryDefn.getBindings().values().iterator();
			while (bindingIt.hasNext()) {
				IBinding binding = bindingIt.next();

				if (binding.getAggregatOns().size() == 0
						|| !reCalGroupNames.contains(binding.getAggregatOns().get(0))) {
					if (binding.getExpression() instanceof IScriptExpression && binding.getAggrFunction() == null) {
						String text = ((IScriptExpression) binding.getExpression()).getText();
						if (ExpressionUtil.getColumnName(text) != null
								|| ExpressionUtil.getColumnBindingName(text) == null)
							continue;
						// If refer to an aggr binding that need to be
						// recalculated, we need also recalculate this binding.
						if (!referToRecAggrBinding(queryDefn, reCalGroupNames, text))
							continue;
					}

					String expr = ExpressionUtil.createJSDataSetRowExpression(
							constructNonReCalBindingDataSetName(binding.getBindingName()));
					binding.setExpression(new ScriptExpression(expr));
					binding.getAggregatOns().clear();
					binding.setAggrFunction(null);
				}
			}

			return queryDefn;
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/**
	 * 
	 * @param queryDefn
	 * @param reCalGroupNames
	 * @param exprText
	 * @param currentBinding
	 * @return
	 * @throws BirtException
	 */
	private static boolean referToRecAggrBinding(IQueryDefinition queryDefn, List<String> reCalGroupNames,
			String exprText) throws BirtException {
		List<IColumnBinding> columnBindings = (List<IColumnBinding>) ExpressionUtil.extractColumnExpressions(exprText,
				ExpressionUtil.ROW_INDICATOR);
		if (columnBindings != null) {
			for (IColumnBinding cb : columnBindings) {
				IBinding usedBinding = (IBinding) queryDefn.getBindings().get(cb.getResultSetColumnName());
				if (usedBinding.getAggrFunction() != null && usedBinding.getAggregatOns().size() > 0
						&& reCalGroupNames.contains(usedBinding.getAggregatOns().get(0))) {
					return true;
				}

				if (usedBinding.getExpression() instanceof IScriptExpression) {
					String text = ((IScriptExpression) usedBinding.getExpression()).getText();
					if (referToRecAggrBinding(queryDefn, reCalGroupNames, text)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
