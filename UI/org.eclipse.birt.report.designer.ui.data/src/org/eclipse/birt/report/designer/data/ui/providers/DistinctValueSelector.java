/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.impl.DataModelAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.AppContextPopulator;
import org.eclipse.birt.report.designer.data.ui.dataset.AppContextResourceReleaser;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetPreviewer;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetPreviewer.PreviewType;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.data.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;

/**
 * Utility class to fetch all available value for filter use.
 *
 */
public class DistinctValueSelector {
	/**
	 * private constructor
	 */
	private DistinctValueSelector() {
	}

	/**
	 * Used in the filter select value dialog in dataset editor
	 *
	 * @param expression
	 * @param dataSetHandle
	 * @param binding
	 * @param useDataSetFilter
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueList(Expression expression, DataSetHandle dataSetHandle, boolean useDataSetFilter)
			throws BirtException {
		return getSelectValueList1(expression, dataSetHandle.getRoot(), dataSetHandle, useDataSetFilter,
				DataEngineFlowMode.NORMAL);
	}

	public static List getSelectValueList(Expression expression, DataSetHandle dataSetHandle,
			DataEngineFlowMode flowMode) throws BirtException {

		return getSelectValueList1(expression, dataSetHandle.getRoot(), dataSetHandle, false, flowMode);
	}

	private static List getSelectValueList1(Expression expression, ModuleHandle moduleHandle,
			DataSetHandle dataSetHandle, boolean useDataSetFilter, DataEngineFlowMode flowMode) throws BirtException {
		ScriptExpression expr = null;
		DataSetHandle targetHandle = dataSetHandle;
		Map appContext = new HashMap();
		DataSetPreviewer previewer = null;

		try {
			ModuleHandle targetModuleHandle = moduleHandle;
			if (!useDataSetFilter) {
				targetModuleHandle = ((Module) moduleHandle.copy()).getModuleHandle();
				SlotHandle dataSets = targetModuleHandle.getDataSets();
				for (int i = 0; i < dataSets.getCount(); i++) {
					if (dataSetHandle.getName().equals(dataSets.get(i).getName())) {
						targetHandle = (DataSetHandle) dataSets.get(i);
						targetHandle.clearProperty(IDataSetModel.FILTER_PROP);
						break;
					}
				}
			}
			previewer = new DataSetPreviewer(targetHandle, 0, PreviewType.RESULTSET);

			DataModelAdapter adapter = new DataModelAdapter(
					new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION, targetModuleHandle));
			expr = adapter.adaptExpression(expression);

			boolean startsWithRow = ExpressionUtility.isColumnExpression(expr.getText(), true);
			boolean startsWithDataSetRow = ExpressionUtility.isColumnExpression(expr.getText(), false);
			if (!startsWithRow && !startsWithDataSetRow) {
				throw new DataException(
						Messages.getString("SelectValueDialog.messages.info.invalidSelectVauleExpression")); //$NON-NLS-1$
			}

			String dataSetColumnName = null;
			if (startsWithDataSetRow) {
				dataSetColumnName = ExpressionUtil.getColumnName(expr.getText());
			} else {
				dataSetColumnName = ExpressionUtil.getColumnBindingName(expr.getText());
			}

			ResourceIdentifiers identifiers = new ResourceIdentifiers();
			String resouceIDs = ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS;
			identifiers.setApplResourceBaseURI(DTPUtil.getInstance().getBIRTResourcePath());
			identifiers.setDesignResourceBaseURI(DTPUtil.getInstance().getReportDesignPath());
			appContext.put(resouceIDs, identifiers);

			AppContextPopulator.populateApplicationContext(targetHandle, appContext);
			previewer.open(appContext, getEngineConfig(targetHandle.getModuleHandle()), flowMode);
			IResultIterator itr = previewer.preview();

			Set visitedValues = new HashSet();
			Object value = null;

			while (itr.next()) {
				// default is to return 10000 distinct value
				if (visitedValues.size() > 10000) {
					break;
				}
				value = itr.getValue(dataSetColumnName);
				if (value != null && !visitedValues.contains(value)) {
					visitedValues.add(value);
				}
			}

			if (visitedValues.isEmpty()) {
				return Collections.EMPTY_LIST;
			}

			return new ArrayList(visitedValues);
		} finally {
			AppContextResourceReleaser.release(appContext);
			if (previewer != null) {
				previewer.close();
			}
		}
	}

	public static List getSelectValueList(Expression expression, ModuleHandle moduleHandle, DataSetHandle dataSetHandle,
			boolean useDataSetFilter) throws BirtException {
		return getSelectValueList1(expression, moduleHandle, dataSetHandle, useDataSetFilter,
				DataEngineFlowMode.NORMAL);
	}

	private static EngineConfig getEngineConfig(ModuleHandle handle) {
		EngineConfig ec = new EngineConfig();
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = handle.getClass().getClassLoader();
		}
		ClassLoader customClassLoader = DataSetProvider.getCustomScriptClassLoader(parent, handle);
		ec.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, customClassLoader);
		return ec;
	}

	/**
	 * Used in filter select value dialog in layout with group definition.
	 *
	 * @param expression
	 * @param dataSetHandle
	 * @param binding          The iterator of ComputedColumnHandle
	 * @param groupIterator    The iterator of GroupHandle
	 * @param useDataSetFilter
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueFromBinding(Expression expression, DataSetHandle dataSetHandle, Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter) throws BirtException {
		return getSelectValueFromBinding1(expression, dataSetHandle, dataSetHandle.getModuleHandle(), binding,
				groupIterator, useDataSetFilter);
	}

	private static List getSelectValueFromBinding1(Expression expression, DataSetHandle dataSetHandle,
			ModuleHandle moduleHandle, Iterator binding, Iterator groupIterator, boolean useDataSetFilter)
			throws BirtException {
		String columnName;
		List bindingList = new ArrayList();

		if (binding != null && binding.hasNext()) {
			while (binding.hasNext()) {
				bindingList.add(binding.next());
			}
		}
		ComputedColumn handle = new ComputedColumn();
		columnName = "TEMP_" + expression.getStringExpression();
		handle.setExpressionProperty(ComputedColumn.EXPRESSION_MEMBER, expression);
		handle.setName(columnName);
		bindingList.add(handle);

		Collection result = null;
		DataRequestSession session = null;
		if (dataSetHandle != null && (moduleHandle instanceof ReportDesignHandle)) {
			EngineConfig config = new EngineConfig();

			ReportDesignHandle copy = (ReportDesignHandle) (moduleHandle.copy().getHandle(null));

			config.setProperty(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
					DataSetProvider.getCustomScriptClassLoader(Thread.currentThread().getContextClassLoader(), copy));

			// clear filter in gorup
			if (groupIterator != null) {
				List clearedGroup = new ArrayList();
				while (groupIterator.hasNext()) {
					GroupHandle groupHandle = (GroupHandle) groupIterator.next();
					if (groupHandle.filtersIterator().hasNext()) {
						GroupHandle copyHandle = (GroupHandle) groupHandle.copy().getHandle(copy.getModule());
						copyHandle.setProperty(IGroupElementModel.FILTER_PROP, Collections.EMPTY_LIST);
						clearedGroup.add(copyHandle);
					} else {
						clearedGroup.add(groupHandle);
					}
				}
				groupIterator = clearedGroup.iterator();
			}

			ReportEngine engine = (ReportEngine) new ReportEngineFactory().createReportEngine(config);

			DummyEngineTask engineTask = new DummyEngineTask(engine,
					new ReportEngineHelper(engine).openReportDesign((ReportDesignHandle) copy), copy, dataSetHandle);

			AppContextPopulator.populateApplicationContext(dataSetHandle, engineTask.getAppContext());

			session = engineTask.getDataSession();

			engineTask.run();
			result = session.getColumnValueSet(dataSetHandle,
					dataSetHandle.getPropertyHandle(DataSetHandle.PARAMETERS_PROP).iterator(), bindingList.iterator(),
					groupIterator, columnName, useDataSetFilter, null);

			engineTask.close();
			engine.destroy();
		} else if (dataSetHandle != null) {
			session = DataRequestSession.newSession(new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION,
					dataSetHandle.getModuleHandle()));

			AppContextPopulator.populateApplicationContext(dataSetHandle, session);

			result = session.getColumnValueSet(dataSetHandle,
					dataSetHandle.getPropertyHandle(DataSetHandle.PARAMETERS_PROP).iterator(), bindingList.iterator(),
					null, columnName, useDataSetFilter, null);
			session.shutdown();
		}

		assert result != null;

		if (result == null) {
			throw new BirtException(Messages.getString("SelectValueDialog.messages.error.selectValueNotSupported"));
		}
		if (result.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		Object resultProtoType = result.iterator().next();
		if (resultProtoType instanceof IBlob || resultProtoType instanceof byte[]) {
			return Collections.EMPTY_LIST;
		}

		// remove the null value in list
		result.remove(null);
		return new ArrayList(result);
	}

	public static List getSelectValueFromBinding(Expression expression, ModuleHandle moduleHandle,
			DataSetHandle dataSetHandle, Iterator binding, Iterator groupIterator, boolean useDataSetFilter)
			throws BirtException {
		return getSelectValueFromBinding1(expression, dataSetHandle, moduleHandle, binding, groupIterator,
				useDataSetFilter);
	}

}
