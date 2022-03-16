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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

/**
 *
 */

public class OlapQueryUtil {

	/**
	 * Valid bindings, return a list of invalid binding.
	 *
	 * @param queryDefn
	 * @param suppressException
	 * @return
	 * @throws DataException
	 */
	public static List validateBinding(ICubeQueryDefinition queryDefn, boolean suppressException) throws DataException {
		// Binding dependency cycle is a fatal error which often causes dead loop, be
		// checked first
		OlapExpressionCompiler.validateDependencyCycle(new HashSet<IBinding>(queryDefn.getBindings()));

		// invalid bindings
		Set<IBinding> result = new HashSet<>();

		Set validDimLevels = new HashSet();

		populateLevel(queryDefn, validDimLevels, ICubeQueryDefinition.COLUMN_EDGE);
		populateLevel(queryDefn, validDimLevels, ICubeQueryDefinition.ROW_EDGE);
		populateLevel(queryDefn, validDimLevels, ICubeQueryDefinition.PAGE_EDGE);

		// help to get all binding names which are referenced by a given binding
		Map<IBinding, Set<String>> bindingReferences = new HashMap<>();

		// all binding names
		Set<String> bindingNames = new HashSet<>();

		// validate one by one and prepare bindingReferences and bindingNames
		for (int i = 0; i < queryDefn.getBindings().size(); i++) {
			IBinding binding = (IBinding) queryDefn.getBindings().get(i);

			if (bindingNames.contains(binding.getBindingName())) {
				result.add(binding);
				throwException(suppressException,
						new DataException(ResourceConstants.DUPLICATED_BINDING_NAME, binding.getBindingName()));
				continue;
			}

			List<String> references = ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(),
					ExpressionUtil.DATA_INDICATOR);

			bindingReferences.put(binding, new HashSet<>(references));
			bindingNames.add(binding.getBindingName());

			Set levels = OlapExpressionCompiler.getReferencedDimLevel(binding.getExpression(), queryDefn.getBindings());
			if (levels != null && levels.size() > 0 && !validDimLevels.containsAll(levels)) {
				result.add(binding);
				throwException(suppressException, new DataException(
						ResourceConstants.INVALID_BINDING_REFER_TO_INEXIST_DIMENSION, binding.getBindingName()));
				continue;
			}

			if (binding.getAggregatOns().size() > 0) {
				if (binding.getAggrFunction() == null) {
					result.add(binding);
					throwException(suppressException, new DataException(
							ResourceConstants.INVALID_BINDING_MISSING_AGGR_FUNC, binding.getBindingName()));
					continue;
				}

				Set lvls = new HashSet();
				for (int j = 0; j < binding.getAggregatOns().size(); j++) {
					lvls.add(OlapExpressionUtil.getTargetDimLevel(binding.getAggregatOns().get(j).toString()));
				}
				if (!validDimLevels.containsAll(lvls)) {
					result.add(binding);
					throwException(suppressException, new DataException(
							ResourceConstants.INVALID_BINDING_REFER_TO_INEXIST_DIMENSION, binding.getBindingName()));
					continue;
				}
			}
		}

		// add binding names introduced from cube operations
		for (ICubeOperation co : queryDefn.getCubeOperations()) {
			for (IBinding b : co.getNewBindings()) {
				if (bindingNames.contains(b.getBindingName())) {
					result.add(b);
					throwException(suppressException,
							new DataException(ResourceConstants.DUPLICATED_BINDING_NAME, b.getBindingName()));
				}
				bindingNames.add(b.getBindingName());
			}
		}

		// Check binding references
		for (Entry<IBinding, Set<String>> entry : bindingReferences.entrySet()) {
			for (String reference : entry.getValue()) {
				if (!bindingNames.contains(reference)) {
					result.add(entry.getKey());
					throwException(suppressException,
							new DataException(ResourceConstants.REFERENCED_BINDING_NOT_EXIST, reference));
				}
			}
		}
		return new ArrayList(result);
	}

	private static void throwException(boolean suppressException, DataException e) throws DataException {
		if (!suppressException) {
			throw e;
		}
	}

	/**
	 *
	 * @param validDimLevels
	 * @param edgeType
	 */
	private static void populateLevel(ICubeQueryDefinition queryDefn, Set validDimLevels, int edgeType) {
		if (queryDefn.getEdge(edgeType) == null) {
			return;
		}
		for (int i = 0; i < queryDefn.getEdge(edgeType).getDimensions().size(); i++) {
			for (int j = 0; j < getHierarchy(queryDefn, edgeType, i).getLevels().size(); j++) {
				ILevelDefinition level = (ILevelDefinition) getHierarchy(queryDefn, edgeType, i).getLevels().get(j);
				validDimLevels.add(new DimLevel(getDimension(queryDefn, edgeType, i).getName(), level.getName()));
			}
		}
	}

	/**
	 *
	 * @param edgeType
	 * @param i
	 * @return
	 */
	private static IHierarchyDefinition getHierarchy(ICubeQueryDefinition queryDefn, int edgeType, int i) {
		return ((IHierarchyDefinition) (getDimension(queryDefn, edgeType, i)).getHierarchy().get(0));
	}

	/**
	 *
	 * @param edgeType
	 * @param i
	 * @return
	 */
	private static IDimensionDefinition getDimension(ICubeQueryDefinition queryDefn, int edgeType, int i) {
		return (IDimensionDefinition) queryDefn.getEdge(edgeType).getDimensions().get(i);
	}
}
