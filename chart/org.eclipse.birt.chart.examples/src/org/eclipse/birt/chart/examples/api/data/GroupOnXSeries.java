/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.api.data;

import java.io.IOException;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
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
 * Presents a bar chart with grouping on X series, which could be acheived in
 * the report designer as follows: Chart Builder -> Data -> X Series -> Set Dat
 * Sorting / Tick Grouping Enabled
 */
public class GroupOnXSeries {

	/**
	 * execute application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new GroupOnXSeries().groupSeries();

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
	 * Get the chart instance from the design file and group X series of the chart.
	 * 
	 * return An instance of the simulated runtime chart model (containing filled
	 * datasets)
	 */
	void groupSeries() {
		SessionHandle sessionHandle = getDesignEngine().newSessionHandle((ULocale) null);
		ReportDesignHandle designHandle = null;

		String path = "src/org/eclipse/birt/chart/examples/api/data/";//$NON-NLS-1$

		try {
			designHandle = sessionHandle.openDesign(path + "NonGroupOnXSeries.rptdesign");//$NON-NLS-1$
			ExtendedItemHandle eih = (ExtendedItemHandle) designHandle.getBody().getContents().get(0);
			Chart cm = (Chart) eih.getReportItem().getProperty("chart.instance"); //$NON-NLS-1$
			cm.getTitle().getLabel().getCaption().setValue("Group On X Series");//$NON-NLS-1$

			SeriesDefinition sdX = (((ChartWithAxes) cm).getAxes().get(0)).getSeriesDefinitions().get(0);

			sdX.setSorting(SortOption.ASCENDING_LITERAL);
			sdX.getGrouping().setEnabled(true);
			sdX.getGrouping().setAggregateExpression("Sum");//$NON-NLS-1$
			sdX.getGrouping().setGroupType(DataType.NUMERIC_LITERAL);
			sdX.getGrouping().setGroupingInterval(1);
			designHandle.saveAs(path + "GroupOnXSeries.rptdesign");//$NON-NLS-1$
		} catch (DesignFileException e) {
			e.printStackTrace();
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Platform.shutdown();
		}

	}

}
