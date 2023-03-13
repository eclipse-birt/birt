/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.api.data;

import java.io.IOException;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

import com.ibm.icu.util.ULocale;

/**
 * Presents a bar chart with mulitple Y series, which could be acheived in the
 * report designer as follows: Chart Builder -> Data -> Y Axis -> Set: Series
 * Grouping Key
 *
 */
public class GroupOnYAxis {

	/**
	 * execute application
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new GroupOnYAxis().groupKey();

	}

	private IDesignEngine getDesignEngine() {

		DesignConfig config = new DesignConfig();
		try {
			Platform.startup(config);
		} catch (BirtException e) {
			e.printStackTrace();
		}

		Object factory = Platform.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);

		return ((IDesignEngineFactory) factory).createDesignEngine(config);
	}

	/**
	 * Get the chart instance from the design file and add series grouping key.
	 *
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	void groupKey() {
		SessionHandle sessionHandle = getDesignEngine().newSessionHandle((ULocale) null);
		ReportDesignHandle designHandle = null;

		String path = "src/org/eclipse/birt/chart/examples/api/data/";//$NON-NLS-1$

		try {
			designHandle = sessionHandle.openDesign(path + "NonGroupOnYAxis.rptdesign");//$NON-NLS-1$
			ExtendedItemHandle eih = (ExtendedItemHandle) designHandle.getBody().getContents().get(0);
			Chart cm = (Chart) eih.getReportItem().getProperty("chart.instance"); //$NON-NLS-1$
			cm.getTitle().getLabel().getCaption().setValue("Group On Y Axis");//$NON-NLS-1$

			Axis axisBase = ((ChartWithAxes) cm).getAxes().get(0); // X-Axis
			Axis axisOrth = axisBase.getAssociatedAxes().get(0); // Y-Axis
			SeriesDefinition sdY = axisOrth.getSeriesDefinitions().get(0); // Y-Series

			SeriesDefinition sdGroup = SeriesDefinitionImpl.create();
			Query query = QueryImpl.create("row[\"Month\"]");//$NON-NLS-1$
			sdGroup.setQuery(query);

			axisOrth.getSeriesDefinitions().clear(); // Clear the original
			// Y-Series (sdY)
			axisOrth.getSeriesDefinitions().add(0, sdGroup);
			sdGroup.getSeries().add(sdY.getSeries().get(0));
			designHandle.saveAs(path + "GroupOnYAxis.rptdesign");//$NON-NLS-1$
		} catch (DesignFileException | ExtendedElementException | IOException e) {
			e.printStackTrace();
		} finally {
			Platform.shutdown();
		}

	}

}
