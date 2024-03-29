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

package org.eclipse.birt.chart.ui.integrate;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartUIHelper;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.core.exception.BirtException;

/**
 * Default implementation or base class of UI helper interface.
 */

public class ChartUIHelperBase implements IChartUIHelper {

	@Override
	public boolean isDefaultTitleSupported() {
		return false;
	}

	@Override
	public String getDefaultTitle(ChartWizardContext context) {
		return ""; //$NON-NLS-1$
	}

	@Override
	public void updateDefaultTitle(Chart cm, Object extendedItem) {
		// Do nothing
	}

	@Override
	public boolean canCombine(IChartType type, ChartWizardContext context) {
		return type.canCombine();
	}

	@Override
	public boolean useDataSetRow(Object reportItem, String expression) throws BirtException {
		/*
		 * Default implementation is a bit simple, this behavior is copied from
		 * org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent.
		 * enableAggEditor( String expression ) prior to the refactoring that yielded
		 * this method. According to the javadoc it should be more comprehensive though.
		 */
		return expression.startsWith("data"); //$NON-NLS-1$
	}
}
