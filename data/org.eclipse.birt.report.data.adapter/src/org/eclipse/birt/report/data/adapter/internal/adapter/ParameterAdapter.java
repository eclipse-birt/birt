/*
 *************************************************************************
 * Copyright (c) 2006, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.internal.adapter;

import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;

/**
 * Adapts a Model parameter definition
 */
public class ParameterAdapter extends ParameterDefinition {

	public ParameterAdapter(DataSetParameterHandle modelParam) {
		setName(modelParam.getName());
		if (modelParam.getPosition() != null)
			setPosition(modelParam.getPosition().intValue());
		if (modelParam.getNativeDataType() != null)
			setNativeType(modelParam.getNativeDataType().intValue());

		if (modelParam instanceof OdaDataSetParameterHandle) {
			setNativeName(((OdaDataSetParameterHandle) modelParam).getNativeName());
		}

		setType(org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType(modelParam.getDataType()));
		setInputMode(modelParam.isInput());
		setOutputMode(modelParam.isOutput());
		setNullable(modelParam.allowNull());
		setInputOptional(modelParam.isOptional());
		setDefaultInputValue(modelParam.getDefaultValue());
	}
}
