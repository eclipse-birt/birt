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

package org.eclipse.birt.data.engine.executor;

import org.eclipse.birt.data.engine.odaconsumer.DataTypeUtil;
import org.eclipse.birt.data.engine.odi.IParameterMetaData;

/**
 * Implementation class of the ODI IParameterMetaData interface.
 */
public class ParameterMetaData implements IParameterMetaData {
	org.eclipse.birt.data.engine.odaconsumer.ParameterMetaData m_odaMetaData;

	ParameterMetaData(org.eclipse.birt.data.engine.odaconsumer.ParameterMetaData odaMetaData) {
		assert odaMetaData != null;
		m_odaMetaData = odaMetaData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#isInputMode()
	 */
	@Override
	public Boolean isInputMode() {
		return m_odaMetaData.isInputMode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#isOutputMode()
	 */
	@Override
	public Boolean isOutputMode() {
		return m_odaMetaData.isOutputMode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#getName()
	 */
	@Override
	public String getName() {
		if (m_odaMetaData.getName() != null) {
			return m_odaMetaData.getName();
		}
		return m_odaMetaData.getNativeName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#getNativeName()
	 */
	@Override
	public String getNativeName() {
		return m_odaMetaData.getNativeName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#getPosition()
	 */
	@Override
	public int getPosition() {
		return m_odaMetaData.getPosition();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#getValueClass()
	 */
	@Override
	public Class getValueClass() {
		// maps ODA metadata java.sql.Types to class
		return DataTypeUtil.toTypeClass(m_odaMetaData.getDataType());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#isOptional()
	 */
	@Override
	public Boolean isOptional() {
		return m_odaMetaData.isOptional();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.odi.IParameterMetaData#getDefaultInputValue()
	 */
	@Override
	public String getDefaultInputValue() {
		Object o = m_odaMetaData.getDefaultValue();
		return o == null ? null : o.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#getNativeTypeName()
	 */
	@Override
	public String getNativeTypeName() {
		return m_odaMetaData.getNativeTypeName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#getScale()
	 */
	@Override
	public int getScale() {
		return m_odaMetaData.getScale();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#getPrecision()
	 */
	@Override
	public int getPrecision() {
		return m_odaMetaData.getPrecision();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IParameterMetaData#isNullable()
	 */
	@Override
	public Boolean isNullable() {
		return m_odaMetaData.isNullable();
	}

}
