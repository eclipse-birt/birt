/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.DerivedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.MeasureDefinition;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter.ExpressionLocation;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;

public class CubeMeasureUtil {

	private static IModelAdapter getModelAdapter() throws BirtException {
		return new DataModelAdapter(new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION));
	}

	/**
	 * Check whether the derived measure reference is valid.
	 * 
	 * @param cubeHandle CubeHandle
	 * @throws BirtException If invalid measure reference or recursive measrue
	 *                       reference is detected.
	 */
	public static void validateDerivedMeasures(CubeHandle cubeHandle) throws BirtException {
		Map<String, IMeasureDefinition> measures = new HashMap<String, IMeasureDefinition>();
		Map<String, IDerivedMeasureDefinition> calculatedMeasures = new HashMap<String, IDerivedMeasureDefinition>();
		Map<String, MeasureHandle> mHandles = new HashMap<String, MeasureHandle>();

		populateMeasures(measures, calculatedMeasures, mHandles, cubeHandle);

		for (Map.Entry<String, IDerivedMeasureDefinition> e : calculatedMeasures.entrySet()) {
			List<String> resolving = new ArrayList<String>();
			checkDerivedMeasure(e.getValue(), resolving, measures, calculatedMeasures, mHandles);
		}
	}

	private static void populateMeasures(Map<String, IMeasureDefinition> measures,
			Map<String, IDerivedMeasureDefinition> calculatedMeasures, Map<String, MeasureHandle> measureHandles,
			CubeHandle cubeHandle) throws BirtException {
		List measureGroups = cubeHandle.getContents(CubeHandle.MEASURE_GROUPS_PROP);
		for (int i = 0; i < measureGroups.size(); i++) {
			MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get(i);
			List measureGroup = mgh.getContents(MeasureGroupHandle.MEASURES_PROP);
			for (int j = 0; j < measureGroup.size(); j++) {
				MeasureHandle mHandle = (MeasureHandle) measureGroup.get(j);
				if (measureHandles != null)
					measureHandles.put(mHandle.getName(), mHandle);
				if (mHandle.isCalculated()) {
					DerivedMeasureDefinition m = new DerivedMeasureDefinition(mHandle.getName(),
							DataAdapterUtil.adaptModelDataType(mHandle.getDataType()),
							getModelAdapter().adaptExpression((Expression) mHandle
									.getExpressionProperty(IMeasureModel.MEASURE_EXPRESSION_PROP).getValue(),
									ExpressionLocation.CUBE));
					calculatedMeasures.put(mHandle.getName(), m);
				} else {
					MeasureDefinition m = new MeasureDefinition(mHandle.getName());
					m.setAggrFunction(DataAdapterUtil.getRollUpAggregationName(mHandle.getFunction()));
					measures.put(m.getName(), m);
				}
			}
		}
	}

	private static void checkDerivedMeasure(IDerivedMeasureDefinition dmeasure, List<String> resolving,
			Map<String, IMeasureDefinition> measures, Map<String, IDerivedMeasureDefinition> calculatedMeasure,
			Map<String, MeasureHandle> mHandles) throws DataException {
		List referencedMeasures = ExpressionCompilerUtil.extractColumnExpression(dmeasure.getExpression(),
				ExpressionUtil.MEASURE_INDICATOR);
		resolving.add(dmeasure.getName());

		for (int i = 0; i < referencedMeasures.size(); i++) {
			String measureName = referencedMeasures.get(i).toString();
			if (measures.containsKey(measureName)) {
				continue;
			} else {
				if (!calculatedMeasure.containsKey(measureName)) {
					MeasureHandle measureHandle = mHandles.get(measureName);
					if (measureHandle == null)
						throw new DataException(AdapterResourceHandle.getInstance().getMessage(
								ResourceConstants.CUBE_DERIVED_MEASURE_INVALID_REF,
								new Object[] { dmeasure.getName(), measureName }));

					throw new DataException(AdapterResourceHandle.getInstance().getMessage(
							ResourceConstants.CUBE_DERIVED_MEASURE_RESOLVE_ERROR, new Object[] { resolving.get(0) }));
				}

				for (int j = 0; j < resolving.size(); j++) {
					if (measureName.equals(resolving.get(j))) {
						resolving.add(measureName);
						throw new DataException(AdapterResourceHandle.getInstance().getMessage(
								ResourceConstants.CUBE_DERIVED_MEASURE_RECURSIVE_REF,
								new Object[] { resolving.get(0), resolving.toString() }));
					}
				}

				checkDerivedMeasure(calculatedMeasure.get(measureName), resolving, measures, calculatedMeasure,
						mHandles);
			}
		}

		resolving.remove(resolving.size() - 1);
	}

	/**
	 * Get measures can be referenced by the specific derived measure.
	 * <p>
	 * This method ensures there is not recursive reference between the returned
	 * measures.
	 * 
	 * @param cubeHandle
	 * @param measureName
	 * @return A list of MeasureHandles which can be referenced by the specified
	 *         measure.
	 * @throws BirtException
	 */
	public static List<MeasureHandle> getIndependentReferences(CubeHandle cubeHandle, String measureName)
			throws BirtException {
		List<MeasureHandle> iMeasures = new ArrayList<MeasureHandle>();
		List<String> mNames = new ArrayList<String>();

		Map<String, IMeasureDefinition> measures = new HashMap<String, IMeasureDefinition>();
		Map<String, IDerivedMeasureDefinition> calculatedMeasures = new HashMap<String, IDerivedMeasureDefinition>();
		Map<String, MeasureHandle> mHandles = new HashMap<String, MeasureHandle>();
		populateMeasures(measures, calculatedMeasures, mHandles, cubeHandle);

		if (mHandles.get(measureName) != null && !mHandles.get(measureName).isCalculated()) {
			// Since the properties in MeasureHandle of the newly added measures
			// is not set correctly, Here always return all measures.
			// TODO Remove this temporary fix which does a favor for GUI while
			// GUI side set the properties correctly.
			for (Map.Entry<String, MeasureHandle> e : mHandles.entrySet()) {
				MeasureHandle handle = e.getValue();
				if (!measureName.equals(handle.getName())) {
					iMeasures.add(handle);
				}
			}

			return iMeasures;
		}

		for (Map.Entry<String, IDerivedMeasureDefinition> e : calculatedMeasures.entrySet()) {
			List<String> resolving = new ArrayList<String>();
			resolving.add(measureName);
			try {
				checkDerivedMeasure(e.getValue(), resolving, measures, calculatedMeasures, mHandles);
				mNames.add(e.getValue().getName());
			} catch (BirtException ignore) {
			}

		}

		for (String i : mNames) {
			if (i.equals(measureName))
				continue;
			iMeasures.add(mHandles.get(i));
		}

		for (Map.Entry<String, IMeasureDefinition> e : measures.entrySet()) {
			iMeasures.add(mHandles.get(e.getKey()));
		}

		return iMeasures;
	}
}
