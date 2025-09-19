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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Checks unused bound columns for the specified element.
 */

abstract class UnusedBoundColumnsMgrImpl extends BoundColumnsMgr {

	protected Set<String> boundColumnNames = new HashSet<>();

	protected DesignElement element;

	protected Module module;

	/**
	 * @param element
	 */

	public UnusedBoundColumnsMgrImpl(DesignElementHandle element) {
		super();
		this.element = element.getElement();
		this.module = element.getModule();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.writer.BoundColumnsMgr#
	 * handleBoundsForParamBinding
	 * (org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.Module, java.lang.String)
	 */

	@Override
	protected void handleBoundsForParamBinding(DesignElement element, Module module, String propValue) {
		if (propValue == null) {
			return;
		}

		List<IColumnBinding> newExprs = null;

		try {
			newExprs = ExpressionUtil.extractColumnExpressions(propValue);
		} catch (BirtException e) {
			newExprs = null;
		}

		if (newExprs != null && newExprs.size() > 0) {
			for (int i = 0; i < newExprs.size(); i++) {
				IColumnBinding column = newExprs.get(i);
				boundColumnNames.add(column.getResultSetColumnName());
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.writer.BoundColumnsMgr#handleBoundsForValue
	 * (org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.Module, java.lang.String)
	 */

	@Override
	protected void handleBoundsForValue(DesignElement element, Module module, String propValue) {
		if (propValue == null) {
			return;
		}

		List newExprs = null;

		try {
			newExprs = ExpressionUtil.extractColumnExpressions(propValue);
		} catch (BirtException e) {
			newExprs = null;
		}

		if (newExprs != null) {
			for (int i = 0; i < newExprs.size(); i++) {
				IColumnBinding column = (IColumnBinding) newExprs.get(i);
				boundColumnNames.add(column.getResultSetColumnName());
			}
		}
	}

	/**
	 * Removes unused bound columns from the given element.
	 *
	 * @param elementHandle the element
	 * @throws SemanticException if bound column property is locked.
	 */

	public static void removedUnusedBoundColumns(DesignElementHandle elementHandle) throws SemanticException {
		if (elementHandle == null
				|| !(elementHandle instanceof ReportItemHandle || elementHandle instanceof ScalarParameterHandle)) {
			return;
		}

		UnusedBoundColumnsMgr mgr = new UnusedBoundColumnsMgr(elementHandle);

		Module module = elementHandle.getModule();

		if (elementHandle instanceof ListingHandle) {
			mgr.dealDataContainerReportItem((ListingElement) mgr.element, module);
		} else if (elementHandle instanceof ReportItemHandle) {
			mgr.dealNonDataContainerReportItem((ReportItem) mgr.element, module);
		} else if (elementHandle instanceof ScalarParameterHandle) {
			mgr.dealScalarParameter((ScalarParameter) mgr.element, module);
		}

		mgr.removeUnusedColumns();
	}

	/**
	 * Removed unused bound columns from the element.
	 *
	 * @throws SemanticException
	 */

	void removeUnusedColumns() throws SemanticException {
		String propName = null;

		if (element instanceof ReportItem) {
			propName = IInternalReportItemModel.BOUND_DATA_COLUMNS_PROP;
		} else if (element instanceof ScalarParameter) {
			propName = IScalarParameterModel.BOUND_DATA_COLUMNS_PROP;
		} else {
			return;
		}

		List currentList = (List) element.getLocalProperty(module, propName);
		if (currentList == null || currentList.isEmpty()) {
			return;
		}

		List<ComputedColumn> unusedList = new ArrayList<>();
		for (int i = 0; i < currentList.size(); i++) {
			ComputedColumn column = (ComputedColumn) currentList.get(i);
			if (!boundColumnNames.contains(column.getName())) {
				unusedList.add(column);
			}
		}

		PropertyHandle propHandle = element.getHandle(module).getPropertyHandle(propName);

		ActivityStack cmdStack = module.getActivityStack();
		cmdStack.startTrans(ModelMessages.getMessage(MessageConstants.REMOVE_ITEM_MESSAGE));
		try {
			for (int i = 0; i < unusedList.size(); i++) {
				propHandle.removeItem(unusedList.get(i));
			}
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}
		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.writer.BoundColumnsMgr#dealData(org.eclipse
	 * .birt.report.model.elements.DataItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */

	@Override
	public void dealData(DataItem element, Module module) {
		super.dealData(element, module);

		// add the result set column to the bound columns. Both name and value
		// are the value of resultSetColumn.

		String value = (String) element.getLocalProperty(module, IDataItemModel.RESULT_SET_COLUMN_PROP);
		if (value == null) {
			return;
		}

		boundColumnNames.add(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealReportItem(org
	 * .eclipse.birt.report.model.elements.ReportItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */

	@Override
	protected void dealReportItem(ReportItem element, Module module) {
		super.dealReportItem(element, module);

		List<ComputedColumn> columnBindings = (List<ComputedColumn>) element.getLocalProperty(module,
				IInternalReportItemModel.BOUND_DATA_COLUMNS_PROP);
		if (columnBindings != null) {
			for (int i = 0; i < columnBindings.size(); i++) {
				ComputedColumn paramValue = columnBindings.get(i);
				handleBoundsForExpression(element, module,
						paramValue.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER));

				List<AggregationArgument> args = (List<AggregationArgument>) paramValue.getLocalProperty(module,
						ComputedColumn.ARGUMENTS_MEMBER);
				if (args == null) {
					continue;
				}

				for (int j = 0; j < args.size(); j++) {
					AggregationArgument arg = args.get(j);
					handleBoundsForValue(element, module, arg.getValue());
				}

			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealScalarParameter
	 * (org.eclipse.birt.report.model.elements.ScalarParameter,
	 * org.eclipse.birt.report.model.core.Module)
	 */

	@Override
	protected void dealScalarParameter(ScalarParameter element, Module module) {
		super.dealScalarParameter(element, module);

		List columnBindings = (List) element.getLocalProperty(module, IScalarParameterModel.BOUND_DATA_COLUMNS_PROP);
		if (columnBindings != null) {
			for (int i = 0; i < columnBindings.size(); i++) {
				ComputedColumn paramValue = (ComputedColumn) columnBindings.get(i);
				handleBoundsForValue(element, module, paramValue.getExpression());

				List<AggregationArgument> args = (List<AggregationArgument>) paramValue.getLocalProperty(module,
						ComputedColumn.ARGUMENTS_MEMBER);
				if (args == null) {
					continue;
				}

				for (int j = 0; j < args.size(); j++) {
					AggregationArgument arg = args.get(j);
					handleBoundsForValue(element, module, arg.getValue());
				}
			}
		}
	}
}
