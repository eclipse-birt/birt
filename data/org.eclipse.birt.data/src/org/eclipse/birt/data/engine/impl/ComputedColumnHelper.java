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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetRuntime.Mode;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph.CycleFoundException;
import org.eclipse.birt.data.engine.impl.util.DirectedGraphEdge;
import org.eclipse.birt.data.engine.impl.util.GraphNode;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 * One of implementation of IResultObjectEvent interface. Used to calculate the
 * computed columns value in the time window from fetching data to do
 * grouping/sorting data.
 */

public class ComputedColumnHelper implements IResultObjectEvent {

	private ComputedColumnHelperInstance dataSetInstance;
	private ComputedColumnHelperInstance resultSetInstance;
	private ComputedColumnHelperInstance availableModeInstance;
	private ComputedColumnHelperInstance currentModel;
	private List<IComputedColumn> allCC;
	private int mode;

	private static Logger logger = Logger.getLogger(ComputedColumnHelper.class.getName());

	private boolean suppressException;

	/**
	 *
	 * @param dataSet
	 * @param dataSetCCList
	 * @param resultSetCCList
	 * @throws
	 */
	ComputedColumnHelper(DataSetRuntime dataSet, List<IComputedColumn> dataSetCCList,
			List<IComputedColumn> resultSetCCList, ScriptContext cx)
			throws DataException {
		Object[] params = { dataSet, dataSetCCList, resultSetCCList };
		logger.entering(ComputedColumnHelper.class.getName(), "ComputedColumnHelper", params);

		this.allCC = new ArrayList<>();
		this.allCC.addAll(dataSetCCList);
		this.allCC.addAll(resultSetCCList);
		this.dataSetInstance = new ComputedColumnHelperInstance(dataSet, dataSetCCList, Mode.DataSet, cx);
		this.resultSetInstance = new ComputedColumnHelperInstance(dataSet, resultSetCCList, Mode.Query, cx);

		List<IComputedColumn> availableCCList = new ArrayList<>();
		getAvailableComputedList(getComputedNameList(dataSetCCList), dataSetCCList, availableCCList);
		this.availableModeInstance = new ComputedColumnHelperInstance(dataSet, availableCCList, Mode.DataSet, cx);
		this.currentModel = this.dataSetInstance;

		this.suppressException = false;
		logger.exiting(ComputedColumnHelper.class.getName(), "ComputedColumnHelper");
	}

	// TODO: Enhance me. This is temply introduced for support aggregation on row
	// sort.
	public void suppressException(boolean suppressException) {
		this.suppressException = suppressException;
	}

	/**
	 *
	 * @return
	 */
	private ComputedColumnHelperInstance getCurrentInstance() {
		return this.currentModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.
	 * data.engine.odi.IResultObject, int)
	 */
	@Override
	public boolean process(IResultObject resultObject, int rowIndex) throws DataException {
		if (this.mode == TransformationConstants.ALL_MODEL) {
			this.dataSetInstance.process(resultObject, rowIndex);
			this.resultSetInstance.process(resultObject, rowIndex);
		} else if (this.getCurrentInstance() != null) {
			return this.getCurrentInstance().process(resultObject, rowIndex);
		}

		return true;
	}

	/**
	 * Return whether the computed column set with given model exists
	 *
	 * @param model
	 * @return
	 */
	public boolean isComputedColumnExist(int model) {
		if (model == TransformationConstants.DATA_SET_MODEL) {
			return this.dataSetInstance.getComputedColumnList().size() > 0;
		} else if (model == TransformationConstants.RESULT_SET_MODEL) {
			return this.resultSetInstance.getComputedColumnList().size() > 0;
		} else if (model == TransformationConstants.ALL_MODEL) {
			return this.allCC.size() > 0;
		}
		return false;
	}

	/**
	 * Return a list of computed column of current instance.
	 *
	 * @return
	 */
	public List<IComputedColumn> getComputedColumnList() {
		if (this.getCurrentInstance() != null) {
			return this.getCurrentInstance().getComputedColumnList();
		}
		return this.allCC;
	}

	/**
	 *
	 * @param rePrepare
	 */
	public void setRePrepare(boolean rePrepare) {
		if (this.getCurrentInstance() != null) {
			this.getCurrentInstance().setRePrepare(rePrepare);
		}
	}

	/**
	 *
	 * @param model
	 */
	public void setModel(int model) {
		this.mode = model;
		if (model == TransformationConstants.DATA_SET_MODEL) {
			this.currentModel = this.dataSetInstance;
		} else if (model == TransformationConstants.RESULT_SET_MODEL) {
			this.currentModel = this.resultSetInstance;
		} else if (model == TransformationConstants.PRE_CALCULATE_MODEL) {
			this.currentModel = this.availableModeInstance;
		} else {
			this.currentModel = null;
		}
	}

	/**
	 *
	 * @param dataSetCCList
	 * @return
	 */
	private List<String> getComputedNameList(List<IComputedColumn> dataSetCCList) {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < dataSetCCList.size(); i++) {
			IComputedColumn column = dataSetCCList.get(i);
			result.add(column.getName());
		}
		return result;
	}

	public void removeAvailableComputedColumn(IComputedColumn ccol) {
		getComputedColumnList().remove(ccol);
		if (this.getCurrentInstance() == null) {
			this.dataSetInstance.remove(ccol.getName());
		}
	}

	/**
	 *
	 * @param dataSetCCList
	 * @return
	 * @throws DataException
	 */
	private void getAvailableComputedList(List<String> refernceNameList, List<IComputedColumn> dataSetCCList,
			List<IComputedColumn> result)
			throws DataException {
		try {
			for (int i = 0; i < dataSetCCList.size(); i++) {
				IComputedColumn column = dataSetCCList.get(i);
				if (!refernceNameList.contains(column.getName())) {
					continue;
				}

				if (ExpressionCompilerUtil.hasAggregationInExpr(column.getExpression())
						|| column.getAggregateFunction() != null) {
				} else {
					List<IColumnBinding> referedList = ExpressionUtil
							.extractColumnExpressions(((IScriptExpression) column.getExpression()).getText());
					if (referedList.size() == 0) {
						result.add(column);
					} else {
						List<String> newList = new ArrayList<>();
						for (int j = 0; j < referedList.size(); j++) {
							IColumnBinding binding = referedList.get(j);
							String name = binding.getResultSetColumnName();
							newList.add(name);
						}
						if (!hasAggregation(newList, dataSetCCList)) {
							result.add(column);
						}
					}
				}
			}
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/**
	 *
	 * @param nameList
	 * @param dataSetCCList
	 * @return
	 * @throws DataException
	 */
	private boolean hasAggregation(List<String> nameList, List<IComputedColumn> dataSetCCList) throws DataException {
		try {
			for (int k = 0; k < nameList.size(); k++) {
				IComputedColumn column = null;
				for (int i = 0; i < dataSetCCList.size(); i++) {
					column = dataSetCCList.get(i);
					if (column.getName() != null && column.getName().equals(nameList.get(k))) {
						break;
					}
					column = null;
				}
				if (column != null) {
					if (ExpressionCompilerUtil.hasAggregationInExpr(column.getExpression())
							|| column.getAggregateFunction() != null) {
						return true;
					}
					List<IColumnBinding> referedList = ExpressionUtil
							.extractColumnExpressions(((IScriptExpression) column.getExpression()).getText());
					List<String> newList = new ArrayList<>();
					for (int j = 0; j < referedList.size(); j++) {
						IColumnBinding binding = referedList.get(j);
						String name = binding.getResultSetColumnName();
						newList.add(name);
					}
					return hasAggregation(newList, dataSetCCList);
				}
				continue;
			}
			return false;
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	private class ComputedColumnHelperInstance {

		private DataSetRuntime dataSet;

		private Mode mode;

		// computed column list passed from external caller
		private List<IComputedColumn> ccList;
		private List<String> removedCCName;

		// computed column array which will be evaluated
		private IComputedColumn[] computedColumn;

		// save such computed columns whose expression is just like dataSetRow["xxx"]
		private Map<String, String> columnReferenceMap = new HashMap<>();

		// computed column position index array
		private int[] columnIndexArray;

		// prepared flag
		private boolean isPrepared;
		private ScriptContext cx;
		// protected static Logger logger = Logger.getLogger(
		// ComputedColumnHelper.class.getName( ) );

		public ComputedColumnHelperInstance(DataSetRuntime dataSet, List<IComputedColumn> computedColumns, Mode mode,
				ScriptContext cx)
				throws DataException {
			// Do not change the assignment of array
			// TODO enhance.
			this.ccList = new ArrayList<>();
			this.removedCCName = new ArrayList<>();
			for (int i = 0; i < computedColumns.size(); i++) {
				this.ccList.add(computedColumns.get(i));
			}
			this.isPrepared = false;
			this.dataSet = dataSet;
			this.mode = mode;
			this.cx = cx.newContext(this.dataSet.getScriptScope());
		}

		void remove(String colName) {
			this.removedCCName.add(colName);
		}

		public List<IComputedColumn> getComputedColumnList() {
			return this.ccList;
		}

		public boolean isRemoved(String colName) {
			for (int i = 0; i < removedCCName.size(); i++) {
				if (colName.equals(removedCCName.get(i))) {
					return true;
				}
			}
			return false;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.
		 * data.engine.odi.IResultObject)
		 */
		public boolean process(IResultObject resultObject, int rowIndex) throws DataException {
			logger.entering(ComputedColumnHelper.class.getName(), "process");
			assert resultObject != null;

			IResultClass resultClass = resultObject.getResultClass();
			if (!isPrepared) {
				prepare(resultClass);
			}

			// check if no computed columns are found as custom fields in the result
			// set
			if (computedColumn.length == 0) {
				logger.exiting(ComputedColumnHelper.class.getName(), "process");
				return true; // done
			}

			IResultIterator cachedIterator = dataSet.getResultSet();
			// bind new object to row script object
			dataSet.setRowObject(resultObject, true);
			dataSet.setCurrentRowIndex(rowIndex);
			Mode temp = dataSet.getMode();
			dataSet.setMode(this.mode);
			// now assign the computed value to each of its projected computed
			// columns
			try {
				// iterate through each projected computed column,
				// and assign it the computed value
				for (int i = 0; i < computedColumn.length; i++) {
					if (isAggrComputedColumn(computedColumn[i]) || isRemoved(computedColumn[i].getName())) {
						continue;
					}

					if (computedColumn[i].getExpression() != null) {
						Object value = null;
						try {
							String columnName = columnReferenceMap.get(computedColumn[i].getName());
							if (columnName != null) {
								if (resultObject != null) {
									// for these computed columns whose expression is just like dataSetRow["xxx"]
									// fetch value just from result set directly rather than Rhino
									value = resultObject.getFieldValue(columnName);
								}
							} else if (computedColumn[i].getExpression().getHandle() != null
									&& computedColumn[i].getExpression().getHandle() instanceof CompiledExpression) {
								value = ExprEvaluateUtil.evaluateCompiledExpression(
										(CompiledExpression) computedColumn[i].getExpression().getHandle(),
										resultObject, rowIndex, dataSet.getScriptScope(), cx);
							} else {
								IScriptExpression expr = (IScriptExpression) computedColumn[i].getExpression();
								String exprText = expr.getText();
								if (exprText != null) {
									if (expr.getHandle() == null) {
										expr.setHandle(cx.compile(expr.getScriptId(), null, 0, exprText));
									}
									if (expr.getHandle() != null && expr.getHandle() instanceof CompiledExpression) {
										value = ExprEvaluateUtil.evaluateCompiledExpression(
												(CompiledExpression) expr.getHandle(), resultObject, rowIndex,
												dataSet.getScriptScope(), cx);
									} else {
										value = ScriptEvalUtil.evalExpr(expr, cx, ScriptExpression.defaultID, 0);
									}
								}
							}
							if (computedColumn[i] instanceof GroupComputedColumn) {
								try {
									value = ((GroupComputedColumn) computedColumn[i]).calculate(value);
								} catch (BirtException e) {
									throw DataException.wrap(e);
								}
							}

							value = DataTypeUtil.convert(value, resultClass.getFieldValueClass(columnIndexArray[i]));
						} catch (BirtException e) {
							if (!suppressException) {
								String fieldName = resultClass.getFieldName(columnIndexArray[i]);
								// Exception from System computed column for Sort, Group or Filter
								if (fieldName != null && fieldName.startsWith("_{$TEMP_")) {
									throw new DataException(ResourceConstants.WRONG_SYSTEM_COMPUTED_COLUMN, e);
								}
								// Exception from "Any" type
								if (resultClass.wasAnyType(columnIndexArray[i])) {
									throw new DataException(ResourceConstants.POSSIBLE_MIXED_DATA_TYPE_IN_COLUMN, e);
								}

								// All other exceptions
								throw new DataException(ResourceConstants.FAIL_RETRIEVE_VALUE_COMPUTED_COLUMN, e,
										resultClass.getFieldName(columnIndexArray[i]));
							}
						}

						resultObject.setCustomFieldValue(columnIndexArray[i], value);
					} else {
						throw new DataException(ResourceConstants.EXPR_INVALID_COMPUTED_COLUMN,
								resultObject.getResultClass().getFieldName(columnIndexArray[i]));
					}
				}
			} finally {
				dataSet.setMode(temp);
			}
			logger.exiting(ComputedColumnHelper.class.getName(), "process");
			if (cachedIterator != null) {
				this.dataSet.setResultSet(cachedIterator, true);
			}
			return true;
		}

		private boolean isAggrComputedColumn(IComputedColumn cc) {
			return cc.getAggregateFunction() != null;
		}

		/**
		 * Indicate the ComputedColumnHelper to reprepare.
		 *
		 * @param rePrepare
		 */
		public void setRePrepare(boolean rePrepare) {
			this.isPrepared = !rePrepare;
		}

		/**
		 * Convert ccList to projComputedColumns, only prepare once.
		 */
		private void prepare(IResultClass resultClass) throws DataException {
			assert resultClass != null;

			// identify those computed columns that are projected
			// in the result set by checking the result metadata
			List<Integer> cmptList = new ArrayList<>();
			Map<String, IComputedColumn> nameToComptCol = new HashMap<>();
			for (int i = 0; i < ccList.size(); i++) {
				IComputedColumn cmptdColumn = ccList.get(i);

				int cmptdColumnIdx = resultClass.getFieldIndex(cmptdColumn.getName());
				// check if given field name is found in result set metadata, and
				// is indeed declared as a custom field
				if (cmptdColumnIdx >= 1 && resultClass.isCustomField(cmptdColumnIdx)) {
					cmptList.add(Integer.valueOf(i));
					nameToComptCol.put(cmptdColumn.getName(), cmptdColumn);
				}
				// else computed column is not projected, skip to next computed
				// column
			}

			int size = cmptList.size();
			columnIndexArray = new int[size];
			computedColumn = new IComputedColumn[size];
			int cmptColPos = 0;
			Set<DirectedGraphEdge> edges = new HashSet<>();
			for (int i = 0; i < size; i++) {
				int pos = cmptList.get(i).intValue();
				IComputedColumn cmptdColumn = ccList.get(pos);
				List<String> referencedBindings = ExpressionCompilerUtil
						.extractColumnExpression(cmptdColumn.getExpression(), ExpressionUtil.ROW_INDICATOR);

				boolean existReference = false;
				for (String name : referencedBindings) {
					if (nameToComptCol.containsKey(name)) {
						edges.add(new DirectedGraphEdge(new GraphNode(cmptdColumn.getName()), new GraphNode(name)));
						existReference = true;
					}
				}
				if (!existReference) {
					computedColumn[cmptColPos] = cmptdColumn;
					columnIndexArray[cmptColPos] = resultClass.getFieldIndex(cmptdColumn.getName());
					cmptColPos++;
				}
			}

			GraphNode[] nodes = null;
			try {
				nodes = new DirectedGraph(edges).flattenNodesByDependency();
			} catch (CycleFoundException e) {
				throw new DataException(ResourceConstants.COMPUTED_COLUMN_CYCLE, e.getNode().getValue());
			}
			for (GraphNode node : nodes) {
				String name = (String) node.getValue();
				boolean isAdded = false;
				for (int i = 0; i < cmptColPos; i++) {
					if (name.equals(computedColumn[i].getName())) {
						isAdded = true;
						break;
					}
				}
				if (isAdded) {
					continue;
				}
				IComputedColumn cmptdColumn = nameToComptCol.get(name);
				computedColumn[cmptColPos] = cmptdColumn;
				columnIndexArray[cmptColPos] = resultClass.getFieldIndex(cmptdColumn.getName());
				cmptColPos++;
			}

			// find out computed columns whose expression is just like dataSetRow["xxx"]
			columnReferenceMap.clear();
			for (IComputedColumn cc : computedColumn) {
				String exprText = null;
				if (cc.getExpression() instanceof IScriptExpression) {
					exprText = ((IScriptExpression) cc.getExpression()).getText();
				}
				if (exprText == null) {
					continue;
				}
				String columnName = null;
				try {
					columnName = ExpressionUtil.getColumnName(exprText);
				} catch (BirtException e) {
					throw DataException.wrap(e);
				}
				if (columnName != null) {
					// if it's expression is just like dataSetRow["xxx"]
					columnReferenceMap.put(cc.getName(), columnName);
				}

			}
			isPrepared = true;
		}
	}
}
