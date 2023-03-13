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

package org.eclipse.birt.data.engine.olap.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IDimLevel;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.IQueryService;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;
import org.eclipse.birt.data.engine.olap.script.JSCubeBindingObject;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 *
 */

public class OlapExpressionUtil {

	private static Logger logger = Logger.getLogger(OlapExpressionUtil.class.getName());

	/**
	 * get the attribute reference name.
	 *
	 * @param dimName
	 * @param levelName
	 * @param attrName
	 * @return
	 */
	public static String getAttrReference(String dimName, String levelName, String attrName) {
		return dimName + '/' + levelName + '/' + attrName;
	}

	public static Set<IDimLevel> getAggregateOnLevel(IBinding targetBinding, List<IBinding> bindings,
			Set<IDimLevel> mostDetailed) throws DataException {
		Set<String> visited = new HashSet<>();
		Set<IDimLevel> valid = new HashSet<>();
		try {
			getBindings(visited, valid, targetBinding, bindings, mostDetailed);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}

		return valid;
	}

	public static Set<IDimLevel> getAggregateOnLevel(String bindingName, List<IBinding> bindings,
			Set<IDimLevel> mostDetailed) throws DataException {
		return getAggregateOnLevel(getBinding(bindingName, bindings), bindings, mostDetailed);
	}

	public static Set<IDimLevel> getAggregateOnLevel(IBaseExpression expr, List<IBinding> bindings,
			Set<IDimLevel> mostDetailed) throws DataException {
		Set<String> visited = new HashSet<>();
		Set<IDimLevel> valid = new HashSet<>();
		try {
			getBindings(visited, valid, expr, bindings, mostDetailed);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
		return valid;
	}

	private static void getBindings(Set<String> visited, Set<IDimLevel> aggOn, IBaseExpression expr,
			List<IBinding> bindings, Set<IDimLevel> mostDetailed) throws DataException, CoreException {
		if (expr == null) {
			return;
		}
		List<String> currentVisitBindings = ExpressionCompilerUtil.extractColumnExpression(expr,
				ExpressionUtil.DATA_INDICATOR);
		currentVisitBindings.removeAll(visited);

		if (expr instanceof IScriptExpression) {
			aggOn.addAll(ExpressionUtil.getReferencedDimLevel(((IScriptExpression) expr).getText()));

			for (String bindingName : currentVisitBindings) {
				getBindings(visited, aggOn, getBinding(bindingName, bindings), bindings, mostDetailed);
			}
		} else if (expr instanceof IConditionalExpression) {
			IConditionalExpression cond = (IConditionalExpression) expr;
			getBindings(visited, aggOn, cond.getExpression(), bindings, mostDetailed);
			getBindings(visited, aggOn, cond.getOperand1(), bindings, mostDetailed);
			getBindings(visited, aggOn, cond.getOperand2(), bindings, mostDetailed);
		}
	}

	private static void getBindings(Set<String> visited, Set<IDimLevel> aggOn, IBinding binding,
			List<IBinding> bindings, Set<IDimLevel> mostDetailed) throws DataException, CoreException {
		if (binding == null || visited.contains(binding.getBindingName())) {
			return;
		}

		// if this binding is aggregation, return its aggregate on levels.
		if (!binding.getAggregatOns().isEmpty()) {
			for (String expr : ((List<String>) binding.getAggregatOns())) {
				aggOn.addAll(ExpressionUtil.getReferencedDimLevel(expr));
			}

			if (!CubeQueryDefinitionUtil.isRunnnigAggr(binding.getAggrFunction())) {
				return;
			}
		}

		List<String> currentVisitBindings = ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(),
				ExpressionUtil.DATA_INDICATOR);
		currentVisitBindings.removeAll(visited);

		String text = ((IScriptExpression) binding.getExpression()).getText();
		aggOn.addAll(ExpressionUtil.getReferencedDimLevel(text));

		// If a binding have no aggregate definition, and refer to measure script
		// object,
		// we should treat its aggregateOn level as that of measure.
		if (binding.getAggregatOns().isEmpty() && binding.getAggrFunction() == null) {
			List<String> result = ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(), "measure");
			if (result != null && result.size() > 0) {
				aggOn.addAll(mostDetailed);
			}
		}
		visited.add(binding.getBindingName());
		for (String bindingName : currentVisitBindings) {
			getBindings(visited, aggOn, getBinding(bindingName, bindings), bindings, mostDetailed);
		}
	}

	private static IBinding getBinding(String bindingName, List<IBinding> bindings) throws DataException {
		for (int i = 0; i < bindings.size(); i++) {
			if (bindings.get(i).getBindingName().equals(bindingName)) {
				return bindings.get(i);
			}
		}
		return null;
	}

	/**
	 *
	 * @param originalMeasureName
	 * @return
	 */
	public static String createMeasureCalculateMemeberName(String originalMeasureName) {
		return "_&$" + originalMeasureName + "$&_";
	}

	/**
	 *
	 * @param expr
	 * @return
	 */
	public static boolean isReferenceToDimLevel(String expr) {
		if (expr == null) {
			return false;
		}
		return expr.trim().matches("\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E");
	}

	/**
	 * This method is used to get the dimension,level,attributes name that reference
	 * by a level&attribute reference expression of following format:
	 * dimension["dimensionName"]["levelName"]["attributeName"].
	 *
	 * String[0] dimensionName; String[1] levelName; String[2] attributeName;
	 *
	 * @param expr
	 * @return String[]
	 * @throws DataException
	 */
	private static String[] getTargetAttribute(String expr, List bindings) throws DataException {
		if (expr == null) {
			return null;
		}
		expr = expr.trim();
		if (!(expr.matches("\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E")
				|| expr.matches("\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E"))) {
			String bindingName = getBindingName(expr);
			if (bindingName != null) {
				for (int i = 0; i < bindings.size(); i++) {
					IBinding binding = (IBinding) bindings.get(i);
					if (bindingName.equals(binding.getBindingName())) {
						if (binding.getExpression() instanceof IScriptExpression) {
							return getTargetAttribute(((IScriptExpression) binding.getExpression()).getText(),
									bindings);
						}
					}
				}
			}

			throw new DataException(ResourceConstants.INVALID_EXPRESSION, expr);
		}

		expr = expr.replaceFirst("\\Qdimension\\E", "");
		String[] result = new String[3];
		String[] candidateResult = expr.split("\\Q\"][\"\\E");
		if (candidateResult.length == 2) {
			result[0] = candidateResult[0].replaceAll("\\Q[\"\\E", "");
			result[1] = candidateResult[1].replaceAll("\\Q\"]\\E", "");
		} else {
			result[0] = candidateResult[0].replaceAll("\\Q[\"\\E", "");
			result[1] = candidateResult[1];
			result[2] = candidateResult[2].replaceAll("\\Q\"]\\E", "");
		}
		return result;
	}

	/**
	 *
	 * @param expr
	 * @return
	 * @throws DataException
	 */
	public static DimLevel getTargetDimLevel(String expr) throws DataException {
		if (expr != null && !isComplexDimensionExpr(expr)) {
			Set<DimLevel> s = OlapExpressionCompiler.getReferencedDimLevel(new ScriptExpression(expr.trim()),
					Collections.EMPTY_LIST);
			if (s != null && s.size() == 1) {
				return s.iterator().next();
			}
		}
		throw new DataException(ResourceConstants.LEVEL_NAME_NOT_FOUND, expr);
	}

	/**
	 * This method is to get the measure name that referenced by a measure reference
	 * expression.
	 *
	 * @param expr
	 * @return
	 */
	public static String getMeasure(String expr) throws DataException {
		if (expr == null) {
			return null;
		}

		String result = findMeasure(expr);
		if (result == null) {
			throw new DataException(ResourceConstants.INVALID_MEASURE_REF, expr);
		}

		return result;

	}

	/**
	 * This method is to get the dimension name with expression patter like
	 * dimension["name"] reference expression.
	 *
	 * @param expr
	 * @return
	 */
	public static String getDimensionName(String expr) throws DataException {
		if (expr == null || expr.trim().length() == 0) {
			return null;
		}
		try {
			List<String> result = ExpressionCompilerUtil.extractColumnExpression(new ScriptExpression(expr),
					"dimension");
			if (result != null && result.size() == 1) {
				return result.get(0);
			}
			return null;
		} catch (DataException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 *
	 * @param expr
	 * @return
	 */
	private static String findMeasure(String expr) {
		if ((expr == null) || !expr.trim().matches("\\Qmeasure[\"\\E.*\\Q\"]\\E")) {
			return null;
		}
		try {
			List<String> result = ExpressionCompilerUtil.extractColumnExpression(new ScriptExpression(expr), "measure");
			if (result != null && result.size() == 1) {
				return result.get(0);
			}
			return null;
		} catch (DataException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 *
	 * @param expr
	 * @return
	 */
	public static String getMeasure(IBaseExpression expr) {
		if (expr instanceof IScriptExpression) {
			return findMeasure(((IScriptExpression) expr).getText());
		}

		return null;
	}

	/**
	 * to check whether the expression directly reference to a dimension or measure
	 *
	 * @param expression
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	public static boolean isDirectRerenrence(IBaseExpression expression, List<IBinding> bindings) throws DataException {
		if (!(expression instanceof IScriptExpression)) {
			return false;
		}
		String expr = ((IScriptExpression) expression).getText();
		if (expr == null) {
			return false;
		}
		expr = expr.trim();
		if (expr.matches("\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E")
				|| expr.matches("\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E")) {
			return true;
		} else if (findMeasure(expr) != null) { // measure
			return true;
		} else if (expr.matches("\\Qdata[\"\\E.*\\Q\"]\\E"))// data binding
		{
			List result;
			try {
				result = ExpressionUtil.extractColumnExpressions(expr, "data");
				if (result.size() > 1) {
					return false;
				}
			} catch (BirtException e) {
			}
			String bindingName = getBindingName(expr);
			for (IBinding binding : bindings) {
				if (binding.getBindingName().equals(bindingName)) {
					return isDirectRerenrence(binding.getExpression(), bindings);
				}
			}
		}
		return false;
	}

	public static IBinding getDirectMeasureBinding(IBinding binding, List<IBinding> bindings) throws DataException {
		if ((binding == null) || !(binding.getExpression() instanceof IScriptExpression)) {
			return null;
		}
		String expr = ((IScriptExpression) binding.getExpression()).getText();
		if (expr == null) {
			return null;
		} else if (expr.trim().matches("\\Qmeasure[\"\\E.*\\Q\"]\\E"))// measure
		{
			return binding;
		} else if (expr.trim().matches(".*\\Qdata[\"\\E.*\\Q\"]\\E.*"))// data binding
		{
			String bindingName = getBindingName(expr);
			for (IBinding b : bindings) {
				if (b.getBindingName().equals(bindingName)) {
					return getDirectMeasureBinding(b, bindings);
				}
			}
		}
		return null;
	}

	public static IBinding getRefMeasureBinding(IBinding binding, List<IBinding> bindings) throws DataException {
		if ((binding == null) || !(binding.getExpression() instanceof IScriptExpression)) {
			return null;
		}
		String expr = ((IScriptExpression) binding.getExpression()).getText();
		if (expr == null) {
			return null;
		} else if (expr.trim().matches(".*\\Qmeasure[\"\\E.*\\Q\"]\\E.*"))// measure
		{
			return binding;
		} else if (expr.trim().matches(".*\\Qdata[\"\\E.*\\Q\"]\\E.*"))// data binding
		{
			String bindingName = getBindingName(expr);
			for (IBinding b : bindings) {
				if (b.getBindingName().equals(bindingName)) {
					return getDirectMeasureBinding(b, bindings);
				}
			}
		}
		return null;
	}

	/**
	 * Return the binding name of data["binding"]
	 *
	 * @param expr
	 * @return
	 */
	public static String getBindingName(String expr) {
		if ((expr == null) || !expr.trim().matches(".*\\Qdata[\"\\E.*\\Q\"]\\E.*")) {
			return null;
		}
		try {
			List<String> result = ExpressionCompilerUtil.extractColumnExpression(new ScriptExpression(expr), "data");
			if (result != null && result.size() == 1) {
				return result.get(0);
			}
			return null;
		} catch (DataException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 *
	 * @param level
	 * @param attribute
	 * @return
	 */
	public static String getAttributeColumnName(String level, String attribute) {
		return level + "/" + attribute;
	}

	/**
	 *
	 * @param dimentionName
	 * @param levelName
	 * @return
	 */
	public static String getQualifiedLevelName(String dimensionName, String levelName) {
		return dimensionName + "/" + levelName;
	}

	/**
	 *
	 * @param level
	 * @return
	 */
	public static String getDisplayColumnName(String level) {
		return level + "/" + "DisplayName";
	}

	/**
	 * This method returns a list of CubeAggrDefnOnMeasure instances which describes
	 * the aggregations that need to be calculated in cube query.
	 *
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	public static CubeAggrDefnOnMeasure[] getAggrDefns(List bindings) throws DataException {
		if (bindings == null || bindings.size() == 0) {
			return new CubeAggrDefnOnMeasure[0];
		}

		List<CubeAggrDefnOnMeasure> cubeAggrDefns = new ArrayList<>();
		for (Iterator it = bindings.iterator(); it.hasNext();) {
			IBinding binding = ((IBinding) it.next());
			try {
				if (binding.getAggrFunction() != null || binding.getAggregatOns().size() != 0) {
					IAggrFunction af = AggregationManager.getInstance().getAggregation(binding.getAggrFunction());
					if (af != null && af.getType() == IAggrFunction.RUNNING_AGGR) {
						throw new DataException(ResourceConstants.INVALID_AGGR_TYPE_ON_MEASURE, af.getName());
					}
					cubeAggrDefns.add(new CubeAggrDefnOnMeasure(binding.getBindingName(),
							getMeasure(binding.getExpression() == null ? null
									: ((IScriptExpression) binding.getExpression()).getText()),
							convertToDimLevel(binding.getAggregatOns()), binding.getAggrFunction(),
							binding.getTimeFunction(), convertToDimLevelAttribute(binding.getArguments(), bindings),
							binding.getFilter()));
				}
			} catch (DataException ex) {
				throw new DataException(ResourceConstants.INVALID_AGGR_BINDING_EXPRESSION, ex,
						binding.getBindingName());
			}
		}
		return cubeAggrDefns.toArray(new CubeAggrDefnOnMeasure[0]);
	}

	/**
	 * This method returns a list of CubeNestAggrDefn instances which describes the
	 * aggregations that need to be calculated in cube query.
	 *
	 * @param bindings
	 * @param basedBindings
	 * @return
	 * @throws DataException
	 */
	public static CubeNestAggrDefn[] getAggrDefnsByNestBinding(List<IBinding> bindings, IBinding[] basedBindings)
			throws DataException {
		if (bindings == null || bindings.size() == 0) {
			return new CubeNestAggrDefn[0];
		}

		List<IBinding> based = new ArrayList<>(Arrays.asList(basedBindings));

		List<CubeNestAggrDefn> cubeAggrDefns = new ArrayList<>();
		for (IBinding binding : bindings) {
			try {
				if (binding.getAggrFunction() != null) {
					if (!CubeQueryDefinitionUtil.isRunnnigAggr(binding.getAggrFunction())) {
						cubeAggrDefns.add(new CubeNestAggrDefn(binding.getBindingName(), binding.getExpression(),
								convertToDimLevel(binding.getAggregatOns()), binding.getAggrFunction(),
								convertToDimLevelAttribute(binding.getArguments(), based), binding.getFilter()));
					} else {
						cubeAggrDefns.add(new CubeRunningNestAggrDefn(binding.getBindingName(), binding.getExpression(),
								convertToDimLevel(binding.getAggregatOns()), binding.getAggrFunction(), null,
								binding.getFilter(), getFullLevelsForRunningAggregation(binding, based),
								binding.getArguments()));
					}
				}

			} catch (DataException ex) {
				throw new DataException(ResourceConstants.INVALID_AGGR_BINDING_EXPRESSION, ex,
						binding.getBindingName());
			}
			based.add(binding);
		}
		return cubeAggrDefns.toArray(new CubeNestAggrDefn[0]);
	}

	public static List getFullLevelsForRunningAggregation(IBinding binding, List<IBinding> based) throws DataException {
		List<String> referencedBindings = ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(),
				ExpressionUtil.DATA_INDICATOR);
		if (referencedBindings == null || referencedBindings.isEmpty()) {
			throw new DataException(ResourceConstants.INVALID_AGGR_BINDING_EXPRESSION, binding.getBindingName());
		}
		String reference = referencedBindings.get(0);
		for (IBinding b : based) {
			if (b.getBindingName().equals(reference) && isAggregationBinding(b)) {
				return convertToDimLevel(b.getAggregatOns());
			}
		}
		throw new DataException(ResourceConstants.INVALID_AGGR_BINDING_EXPRESSION, binding.getBindingName());
	}

	public static boolean isAggregationBinding(IBinding binding) throws DataException {
		if (binding == null) {
			return false;
		}
		return binding.getAggrFunction() != null;
	}

	public static Set<String> getDerivedMeasureNames(List<IBinding> bindings) throws DataException {
		Set<String> directRef = new HashSet<>();
		Set<String> all = new HashSet<>();
		for (IBinding binding : bindings) {
			if (isReferenceToLevelOrMeasure(binding.getExpression(), bindings)) {
				all.add(binding.getBindingName());
			}
			if (isDirectRerenrence(binding.getExpression(), bindings)) {
				directRef.add(binding.getBindingName());
			}
		}
		all.removeAll(directRef);
		return all;
	}

	private static boolean isReferenceToLevelOrMeasure(IBaseExpression expression, List<IBinding> bindings)
			throws DataException {
		String measure = OlapExpressionCompiler.getReferencedScriptObject(expression,
				ScriptConstants.MEASURE_SCRIPTABLE);
		if (measure != null) {
			return true;
		}
		String dimension = OlapExpressionCompiler.getReferencedScriptObject(expression,
				ScriptConstants.DIMENSION_SCRIPTABLE);
		if (dimension != null) {
			return true;
		}
		List bindingNames = ExpressionCompilerUtil.extractColumnExpression(expression,
				ScriptConstants.DATA_BINDING_SCRIPTABLE);
		for (int i = 0; i < bindingNames.size(); i++) {
			IBinding binding = getBinding((String) bindingNames.get(i), bindings);
			if ((binding == null) || isReferenceToLevelOrMeasure(binding.getExpression(), bindings)) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @param expr
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	public static String getReferencedDimensionName(IBaseExpression expr, List bindings) throws DataException {
		String result = OlapExpressionCompiler.getReferencedScriptObject(expr, ScriptConstants.DIMENSION_SCRIPTABLE);
		if (result == null) {
			String bindingName = OlapExpressionCompiler.getReferencedScriptObject(expr,
					ScriptConstants.DATA_BINDING_SCRIPTABLE);
			if (bindingName == null) {
				bindingName = OlapExpressionCompiler.getReferencedScriptObject(expr,
						ScriptConstants.DATA_SET_BINDING_SCRIPTABLE);
			}
			if (bindingName == null) {
				return null;
			} else {
				for (int i = 0; i < bindings.size(); i++) {
					IBinding binding = (IBinding) bindings.get(i);
					if (binding.getBindingName().equals(bindingName)) {
						return getReferencedDimensionName(binding.getExpression(), bindings);
					}
				}
			}
		}
		return result;
	}

	private static List convertToDimLevel(List dimLevelExpressions) throws DataException {
		List result = new ArrayList();
		for (int i = 0; i < dimLevelExpressions.size(); i++) {
			result.add(getTargetDimLevel(dimLevelExpressions.get(i).toString()));
		}
		return result;
	}

	private static List convertToDimLevelAttribute(List dimLevelExpressions, List bindings) throws DataException {
		List result = new ArrayList();
		for (int i = 0; i < dimLevelExpressions.size(); i++) {
			try {
				String[] attr = getTargetAttribute(((IScriptExpression) dimLevelExpressions.get(i)).getText(),
						bindings);
				result.add(attr);
			} catch (DataException e) {
			}
		}
		return result;
	}

	/**
	 *
	 * @param expr
	 * @return
	 */
	public static boolean isComplexDimensionExpr(String expr) {
		if (expr == null) {
			return false;
		}

		String exprTrimed = expr.trim().replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\t", "").replace(" ",
				"");
		try {
			boolean referToAttribute = isReferenceToAttribute(new ScriptExpression(expr), new ArrayList());

			if (referToAttribute) {
				if (exprTrimed.matches("\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E\\S+?")
						|| exprTrimed.matches("\\S+?\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E")
						|| exprTrimed.matches("\\S+?\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E\\S+?")) {
					return true;
				} else {
					return false;
				}
			} else if (exprTrimed.matches("\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E\\S+?")
					|| exprTrimed.matches("\\S+?\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E")
					|| exprTrimed.matches("\\S+?\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E\\S+?")) {
				return true;
			} else {
				return false;
			}

		} catch (DataException e) {
			return false;
		}
	}

	/**
	 *
	 * @param expr
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	public static boolean isReferenceToAttribute(IBaseExpression expr, List bindings) throws DataException {
		Set set = OlapExpressionCompiler.getReferencedDimLevel(expr, bindings);
		if (set.size() != 1) {
			return false;
		}
		for (Iterator k = set.iterator(); k.hasNext();) {
			Object obj = k.next();
			if (obj instanceof DimLevel) {
				DimLevel dimLevel = (DimLevel) obj;
				if (dimLevel.getAttrName() != null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 *
	 * @param outResults
	 * @return
	 * @throws DataException
	 */
	public static Scriptable createQueryResultsScriptable(IBaseQueryResults outResults) throws DataException {
		if (outResults instanceof ICubeQueryResults) {
			return new JSCubeBindingObject(((ICubeQueryResults) outResults).getCubeCursor());
		} else if (outResults instanceof IQueryService) {
			try {
				return ((IQueryService) outResults).getExecutorHelper().getScriptable();
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}
		return null;
	}

}
