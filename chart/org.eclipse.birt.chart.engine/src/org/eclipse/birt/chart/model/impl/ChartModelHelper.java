/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.model.IChartModelHelper;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.emf.common.util.EList;

/**
 * Chart Model helper is default implementation of IChartModelHelper.
 */

public class ChartModelHelper implements IChartModelHelper {

	private static IChartModelHelper instance = new ChartModelHelper();

	protected ChartModelHelper() {

	}

	public static void initInstance(IChartModelHelper newInstance) {
		instance = newInstance;
	}

	public static IChartModelHelper instance() {
		return instance;
	}

	public void updateExtendedProperties(EList<ExtendedProperty> properties) {
		ExtendedProperty extendedProperty = AttributeFactory.eINSTANCE.createExtendedProperty();
		extendedProperty.setName(IDeviceRenderer.AREA_ALT_ENABLED);
		extendedProperty.setValue(Boolean.FALSE.toString());
		properties.add(extendedProperty);
	}

	public List<String> getBuiltInExtendedProperties() {
		List<String> list = new ArrayList<String>(3);
		list.add(IDeviceRenderer.AREA_ALT_ENABLED);
		return list;
	}

	public ExpressionCodec createExpressionCodec() {
		return new ExpressionCodec();
	}

}
