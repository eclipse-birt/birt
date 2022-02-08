/**
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseTransform;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

public class QueryDefinitionUtil {
	private QueryDefinitionUtil() {
	};

	/**
	 * Get all accessible bindings from a query definition. If <code>qd</code> is a
	 * sub query definition, returns bindings defined onto itself and all
	 * not-aggregation bindings from its ancestors. If <code>qd</code> is not a sub
	 * query definition, just returns its bindings defined onto itself
	 * 
	 * @param qd
	 * @return
	 * @throws DataException
	 */
	public static Map<String, IBinding> getAccessibleBindings(IBaseQueryDefinition qd) throws DataException {
		if (qd == null) {
			return null;
		}
		Map<String, IBinding> result = new HashMap<String, IBinding>(qd.getBindings());
		IBaseQueryDefinition parent = null;
		if (qd instanceof SubqueryDefinition) {
			parent = qd.getParentQuery();
		}
		while (parent != null) {
			Map parentBindings = parent.getBindings();
			Map<String, Boolean> aggrInfo = parseAggregations(parentBindings);
			Iterator it = parentBindings.keySet().iterator();
			while (it.hasNext()) {
				String name = (String) it.next();

				if (!aggrInfo.get(name)) {
					// not an aggregation
					IBinding b = (IBinding) parentBindings.get(name);
					if (!result.containsKey(name)) {
						result.put(name, b);
					}
				}
			}
			if (parent instanceof SubqueryDefinition) {
				parent = parent.getParentQuery();
			} else {
				break;
			}
		}
		return result;
	}

	/**
	 * Each binding is parsed to see if it's an aggregation. The parsed result saved
	 * in a Map<String, Boolean> map
	 * 
	 * @param input: all bindings
	 * @return
	 * @throws DataException
	 */
	public static Map<String, Boolean> parseAggregations(Map<String, IBinding> input) throws DataException {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		for (Entry<String, IBinding> entry : input.entrySet()) {
			IBinding binding = entry.getValue();

			// transition bindings during parsing, used to check if cycle exists
			Set<String> transitions = new HashSet<String>();
			transitions.add(binding.getBindingName());

			parseAggregation(binding, input, result, transitions);
		}
		return result;
	}

	/**
	 * 
	 * @param binding:     the binding to be parsed
	 * @param allBindings: all bindings
	 * @param checked:     already parsed result
	 * @param transitions: names of transitions bindings during parsing, used to
	 *                     check if cycle exists
	 * @return true if <code>binding</code> is an aggregation; false otherwise
	 * @throws DataException
	 */
	private static boolean parseAggregation(IBinding binding, Map<String, IBinding> allBindings,
			Map<String, Boolean> checked, Set<String> transitions) throws DataException {
		String name = binding.getBindingName();
		if (checked.containsKey(name)) {
			// already checked
			return checked.get(name);
		}
		IBaseExpression expr = binding.getExpression();

		// check if it's an old style aggregation
		if (expr instanceof IScriptExpression) {
			if (ExpressionUtil.hasAggregation(((IScriptExpression) expr).getText())) {
				checked.put(name, true);
				return true;
			}
		}

		// check if itself is an aggregation
		if (binding.getAggrFunction() != null) {
			checked.put(name, true);
			return true;
		}

		// check if it refers to some aggregation bindings
		List<String> referencedBindings = ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(),
				ExpressionUtil.ROW_INDICATOR);
		for (String reference : referencedBindings) {
			if (transitions.contains(reference)) {
				throw new DataException(ResourceConstants.COLUMN_BINDING_CYCLE, reference);
			}
			IBinding b = allBindings.get(reference);
			if (b != null) {
				Set<String> newTransitions = new HashSet<String>(transitions);
				newTransitions.add(reference);
				boolean isAggr = parseAggregation(b, allBindings, checked, newTransitions);
				if (isAggr) {
					checked.put(name, true);
					return true;
				}
			}
		}

		// not an aggregation
		checked.put(name, false);
		return false;
	}

	/**
	 * 
	 * @param subQueryName
	 * @return
	 * @throws DataException
	 */
	public static ISubqueryDefinition findSubQueryDefinition(String subQueryName, IBaseQueryDefinition queryDefn)
			throws DataException {
		if (queryDefn == null)
			return null;
		Collection subQueries = queryDefn.getSubqueries();
		ISubqueryDefinition subQueryDefn = null;
		// search from subQueries list
		if (subQueries != null && !subQueries.isEmpty()) {
			Iterator subQueriesIter = subQueries.iterator();
			while (subQueriesIter.hasNext()) {
				ISubqueryDefinition qd = (ISubqueryDefinition) subQueriesIter.next();
				if (qd.getName().equals(subQueryName)) {
					return qd;
				} else {
					subQueryDefn = findSubQueryDefinition(subQueryName, qd);
				}
			}
		}

		// search from groups' subQueries list
		if (subQueryDefn == null && queryDefn.getGroups() != null) {
			List group = queryDefn.getGroups();
			for (int i = 0; i < group.size(); i++) {
				Collection groupSubQueries = ((IBaseTransform) group.get(i)).getSubqueries();
				if (groupSubQueries != null && !groupSubQueries.isEmpty()) {
					Iterator subQueriesIter = groupSubQueries.iterator();
					while (subQueriesIter.hasNext()) {
						ISubqueryDefinition qd = (ISubqueryDefinition) subQueriesIter.next();
						if (qd.getName().equals(subQueryName)) {
							return qd;
						} else {
							subQueryDefn = findSubQueryDefinition(subQueryName, qd);
							if (subQueryDefn != null)
								return subQueryDefn;
						}
					}
				}
			}
		}
		return subQueryDefn;
	}
}
