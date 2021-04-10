/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.ui.impl.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.oda.pojo.querymodel.FieldSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.ColumnDefinition;
import org.eclipse.birt.data.oda.pojo.util.MethodParameterType;

public class ColumnMappingPageHelper {

	// used to make sure no duplicate name
	private Set<String> names;

	private static final String AUTO_ADJUST_NAME_REGEX = ".*_[0-9]+$"; //$NON-NLS-1$
	private static final String DEFAULT_PARAM_NAME_PREFIX = "param_"; //$NON-NLS-1$

	private List<ColumnDefinition> definitions;
	private List<IMethodParameter> currentColumnParams;

	public ColumnMappingPageHelper() {
		names = new HashSet<String>();
		definitions = new ArrayList<ColumnDefinition>();
	}

	/**
	 * 
	 * @param cd
	 * @return the ColumnDefinition added
	 */
	public ColumnDefinition addColumnDefinition(ColumnDefinition cd) {
		assert cd != null;
		String name = getDistinctName(cd.getName());
		names.add(name);
		ColumnDefinition def = cd.clone();
		def.setName(name);
		definitions.add(def);
		return def;
	}

	public ColumnDefinition[] addColumnDefinitions(ColumnDefinition[] definitions) {
		ColumnDefinition[] result = new ColumnDefinition[definitions.length];
		int index = 0;
		for (ColumnDefinition cd : definitions) {
			result[index++] = addColumnDefinition(cd);
		}
		return result;
	}

	public boolean isUniqueColumnName(String name) {
		if (name == null || name.trim().length() == 0)
			return false;

		return !names.contains(name);
	}

	public String getDistinctName(String newName) {
		String name = newName;
		while (names.contains(name)) {
			if (name.matches(AUTO_ADJUST_NAME_REGEX)) {
				int lastIndex = name.lastIndexOf('_');
				String index = name.substring(lastIndex + 1);
				name = name.substring(0, lastIndex + 1) + (Integer.parseInt(index) + 1);
			} else {
				if (name.endsWith("_")) //$NON-NLS-1$
				{
					name += "1"; //$NON-NLS-1$
				} else {
					name += "_1"; //$NON-NLS-1$
				}
			}
		}
		return name;
	}

	public List<ColumnDefinition> getColumnDefinitions() {
		return definitions;
	}

	public List<IMethodParameter> getAllParameters() {
		List params = new ArrayList();
		for (int i = 0; i < definitions.size(); i++) {
			IMappingSource[] sources = definitions.get(i).getMappingPath();
			for (int j = 0; j < sources.length; j++) {
				if (sources[j] instanceof MethodSource) {
					params.addAll(Arrays.asList(((MethodSource) sources[j]).getParameters()));
				}
			}
		}
		return params;
	}

	public void clearColumnDefinitions() {
		definitions.clear();
		names.clear();
	}

	public boolean isFirstRow(Object o) {
		if (definitions.size() > 0) {
			return definitions.get(0) == o;
		}
		return false;
	}

	public boolean isLastRow(Object o) {
		if (definitions.size() > 0) {
			return definitions.get(definitions.size() - 1) == o;
		}
		return false;
	}

	public boolean isValidParamName(VariableParameter vp, String name, String type) {
		if (vp == null || vp.getName() == null || vp.getDataType() == null)
			return false;

		List<IMethodParameter> allParams = getAllParameters();

		for (int i = 0; i < definitions.size(); i++) {
			List<VariableParameter> paramList = definitions.get(i).getVariableParameters();
			for (int k = 0; k < paramList.size(); k++) {
				if (!vp.equals(paramList.get(k)) && vp.getName().equals(paramList.get(k).getName())) {
					return vp.getDataType().equalsIgnoreCase(paramList.get(k).getDataType());
				}
			}

			for (int k = 0; k < allParams.size(); k++) {
				IMethodParameter item = allParams.get(k);
				if (item instanceof VariableParameter) {
					if (!vp.equals(item) && name.equals(((VariableParameter) item).getName()))

						return type.equals(((VariableParameter) item).getDataType());
				}
			}
		}
		return true;
	}

	public int getElementIndex(Object o) {
		return definitions.indexOf(o);
	}

	/**
	 * switch with its previous element
	 * 
	 * @param index
	 */
	public void moveColumnDefinitionUp(int index) {
		if (index >= 1 && index < definitions.size()) {
			ColumnDefinition cd = definitions.get(index);
			definitions.set(index, definitions.get(index - 1));
			definitions.set(index - 1, cd);
		}
	}

	/**
	 * switch with its next element
	 * 
	 * @param index
	 */
	public void moveColumnDefinitionDown(int index) {
		if (index >= 0 && index < definitions.size() - 1) {
			ColumnDefinition cd = definitions.get(index);
			definitions.set(index, definitions.get(index + 1));
			definitions.set(index + 1, cd);
		}
	}

	public void removeColumnDefinition(ColumnDefinition column) {
		if (definitions.remove(column)) {
			names.remove(column.getName());
		}
	}

	public ColumnDefinition setColumnDefinition(int index, ColumnDefinition cd) {
		names.remove(definitions.get(index).getName());
		String newName = getDistinctName(cd.getName());
		names.add(newName);
		ColumnDefinition newDef = cd.clone();
		newDef.setName(newName);
		definitions.set(index, newDef);
		return newDef;
	}

	public void updateParameters(ColumnDefinition cd) {

	}

	public int getColumnDefnCount() {
		return definitions.size();
	}

	public IMappingSource[] createMappingPath(List<Member> backs) {
		IMappingSource[] result = new IMappingSource[backs.size()];

		if (currentColumnParams == null)
			currentColumnParams = new ArrayList<IMethodParameter>();

		int index = 1;
		for (int i = 0; i < result.length; i++) {
			Member m = backs.get(backs.size() - 1 - i);
			if (m instanceof Field) {
				result[i] = new FieldSource(m.getName());
			} else if (m instanceof Method) {
				List<IMethodParameter> params = new ArrayList<IMethodParameter>();
				for (Class c : ((Method) m).getParameterTypes()) {
					index = getUniqueParamIndex();
					MethodParameterType pt = MethodParameterType.getInstance(c);
					VariableParameter vp = new VariableParameter(DEFAULT_PARAM_NAME_PREFIX + index, pt.getName());
					index++;
					params.add(vp);
					currentColumnParams.add(vp);
				}
				result[i] = new MethodSource(m.getName(), params.toArray(new IMethodParameter[0]));
			}
			assert false;
		}
		return result;
	}

	public int getUniqueParamIndex() {
		int index = 1;
		while (containsParameter(DEFAULT_PARAM_NAME_PREFIX + index)) {
			index++;
		}
		return index;
	}

	public boolean containsParameter(String name) {
		if (name != null) {
			for (int i = 0; i < definitions.size(); i++) {
				List<VariableParameter> paramList = definitions.get(i).getVariableParameters();
				for (int k = 0; k < paramList.size(); k++) {
					if (name.equals(paramList.get(k).getName())) {
						return true;
					}
				}
			}
			for (int i = 0; i < currentColumnParams.size(); i++) {
				IMethodParameter item = currentColumnParams.get(i);
				if ((item instanceof VariableParameter) && name.equals(((VariableParameter) item).getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public void clearParametersCache() {
		if (this.currentColumnParams != null)
			this.currentColumnParams.clear();
	}

}
