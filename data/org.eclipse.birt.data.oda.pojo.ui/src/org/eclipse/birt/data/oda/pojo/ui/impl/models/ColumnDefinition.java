/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.querymodel.ConstantParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;
import org.eclipse.birt.data.oda.pojo.ui.util.Constants;

/**
 * 
 */

public class ColumnDefinition implements Cloneable {

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public ColumnDefinition(IMappingSource[] mappingPath, String name, OdaType type) {
		assert mappingPath != null && mappingPath.length > 0 && name != null && type != null;
		this.mappingPath = mappingPath;
		this.name = name;
		this.type = type;
	}

	private IMappingSource[] mappingPath;
	private String name;
	private OdaType type;

	public String getName() {
		return name;
	}

	public OdaType getType() {
		return type;
	}

	public List<VariableParameter> getVariableParameters() {
		List<VariableParameter> paramList = new ArrayList<VariableParameter>();
		IMappingSource[] sources = getMappingPath();
		for (int i = 0; i < sources.length; i++) {
			if (sources[i] instanceof MethodSource) {
				IMethodParameter[] params = ((MethodSource) sources[i]).getParameters();
				for (int j = 0; j < params.length; j++) {
					if (params[j] instanceof VariableParameter) {
						paramList.add((VariableParameter) params[j]);
					}
				}
			}
		}
		return paramList;
	}

	public IMappingSource[] getMappingPath() {
		return mappingPath;
	}

	public String getMappingPathText() {
		StringBuffer sb = new StringBuffer();
		for (IMappingSource m : mappingPath) {
			sb.append(m.getName());
			if (m instanceof MethodSource) {
				sb.append("("); //$NON-NLS-1$
				int i = 0;
				for (IMethodParameter p : ((MethodSource) m).getParameters()) {
					if (i > 0) {
						sb.append(Constants.METHOD_PARAM_SEPARATOR).append(" "); //$NON-NLS-1$
					}
					if (p instanceof ConstantParameter) {
						ConstantParameter cp = (ConstantParameter) p;
						if (cp.getStringValue() != null) {
							// if value string contains
							// CONSTANT_PARAM_VALUE_QUOTE, and escape char
							// before each CONSTANT_PARAM_VALUE_QUOTE
							String value = cp.getStringValue().replace(
									String.valueOf(Constants.CONSTANT_PARAM_VALUE_QUOTE),
									"" + Constants.CONSTANT_PARAM_VALUE_QUOTE_ESCAPE //$NON-NLS-1$
											+ Constants.CONSTANT_PARAM_VALUE_QUOTE);
							sb.append(Constants.CONSTANT_PARAM_VALUE_QUOTE).append(value)
									.append(Constants.CONSTANT_PARAM_VALUE_QUOTE)
									.append(Constants.PARAM_TYPE_SEPARATOR);
						}
						sb.append(cp.getDataType());
					} else if (p instanceof VariableParameter) {
						VariableParameter vp = (VariableParameter) p;
						sb.append(vp.getName()).append(Constants.PARAM_TYPE_SEPARATOR).append(vp.getDataType());
						if (vp.getStringValue() != null && vp.getStringValue().trim().length() > 0) {
							sb.append(Constants.PARAM_TYPE_SEPARATOR).append(Constants.CONSTANT_PARAM_VALUE_QUOTE)
									.append(vp.getStringValue()).append(Constants.CONSTANT_PARAM_VALUE_QUOTE);
						}
					}
					i++;
				}
				sb.append(")"); //$NON-NLS-1$
			}
			sb.append(Constants.METHOD_OR_FIELD_SEPARATOR);
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ColumnDefinition clone() {
		try {
			return (ColumnDefinition) super.clone();
		} catch (CloneNotSupportedException e) {
			// never happens
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(mappingPath);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnDefinition other = (ColumnDefinition) obj;
		if (!Arrays.equals(mappingPath, other.mappingPath))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
