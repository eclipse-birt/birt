/*
 *************************************************************************
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
	@Override
	public Boolean isInputMode() {
		return m_odiMetaData.isInputMode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#isOutputMode()
	 */
	@Override
	public Boolean isOutputMode() {
		return m_odiMetaData.isOutputMode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getName()
	 */
	@Override
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
	@Override
	public int getPosition() {
		return m_odiMetaData.getPosition();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getDataType()
	 */
	@Override
	public int getDataType() throws DataException {
		return DataTypeUtil.toApiDataType(m_odiMetaData.getValueClass());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getDataTypeName()
	 */
	@Override
	public String getDataTypeName() throws DataException {
		return DataType.getName(getDataType());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#isOptional()
	 */
	@Override
	public Boolean isOptional() {
		return m_odiMetaData.isOptional();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IParameterMetaData#getDefaultInputValue()
	 */
	@Override
	public String getDefaultInputValue() {
		return m_odiMetaData.getDefaultInputValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getNativeTypeName()
	 */
	@Override
	public String getNativeTypeName() {
		return m_odiMetaData.getNativeTypeName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getScale()
	 */
	@Override
	public int getScale() {
		return m_odiMetaData.getScale();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#getPrecision()
	 */
	@Override
	public int getPrecision() {
		return m_odiMetaData.getPrecision();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IParameterMetaData#isNullable()
	 */
	@Override
	public Boolean isNullable() {
		return m_odiMetaData.isNullable();
	}

}
