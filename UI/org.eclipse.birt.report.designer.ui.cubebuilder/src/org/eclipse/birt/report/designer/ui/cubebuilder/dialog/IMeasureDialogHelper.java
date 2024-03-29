/*******************************************************************************
 * Cmpyright (c) 2013 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.Map;

import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeMeasureExpressionProvider;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 *
 */

public interface IMeasureDialogHelper {

	CubeMeasureExpressionProvider getExpressionProvider(MeasureHandle handle);

	IAggrFunction[] getAggregationFunctions(MeasureHandle handle);

	ComputedColumnHandle[] getBindings(MeasureHandle handle);

	MeasureHandle createMeasure(String name) throws SemanticException;

	boolean hasFilter(MeasureHandle handle);

	Expression getFilter(MeasureHandle handle);

	void setFilter(MeasureHandle handle, Expression expr) throws SemanticException;

	Map<String, Expression> getArguments(MeasureHandle handle);

	void setArguments(MeasureHandle measure, Map<String, Expression> arguments) throws SemanticException;

	boolean hideSecurityPart();

	boolean hideHyperLinkPart();

	boolean hideFormatPart();

	boolean hideAlignmentPart();
}
