/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.api.IParameterMetaData;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 */
public class ParameterMetaData implements IParameterMetaData {
	org.eclipse.birt.data.engine.odi.IParameterMetaData m_odiMetaData;

	protected static Logger logger = Logger.getLogger(ParameterMetaData.class.getName());

	ParameterMetaData(org.eclipse.birt.data.engine.odi.IParameterMetaData odiMetaData) {
		logger.entering(ParameterMetaData.class.getName(), "ParameterMetaData", odiMetaData);
		assert odiMetaData != null;
		m_odiMetaData = odiMetaData;
		logger.exiting(ParameterMetaData.class.getName(), "ParameterMetaData");
		logger.log(Level.FINER, "ParameterMetaData starts up");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#isInputMode()
	 */
	public Boolean isInputMode() {
		return m_odiMetaData.isInputMode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#isOutputMode()
	 */
	public Boolean isOutputMode() {
		return m_odiMetaData.isOutputMode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getName()
	 */
	public String getName() {
		return m_odiMetaData.getName();
	}

	/**
	 * Returns the parameter's native name
	 * 
	 * @return
	 */
	public String getNativeName() {
		return m_odiMetaData.getNativeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getPosition()
	 */
	public int getPosition() {
		return m_odiMetaData.getPosition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getDataType()
	 */
	public int getDataType() throws DataException {
		return DataTypeUtil.toApiDataType(m_odiMetaData.getValueClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getDataTypeName()
	 */
	public String getDataTypeName() throws DataException {
		return DataType.getName(getDataType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#isOptional()
	 */
	public Boolean isOptional() {
		return m_odiMetaData.isOptional();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IParameterMetaData#getDefaultInputValue()
	 */
	public String getDefaultInputValue() {
		return m_odiMetaData.getDefaultInputValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getNativeTypeName()
	 */
	public String getNativeTypeName() {
		return m_odiMetaData.getNativeTypeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getScale()
	 */
	public int getScale() {
		return m_odiMetaData.getScale();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getPrecision()
	 */
	public int getPrecision() {
		return m_odiMetaData.getPrecision();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#isNullable()
	 */
	public Boolean isNullable() {
		return m_odiMetaData.isNullable();
	}

}
