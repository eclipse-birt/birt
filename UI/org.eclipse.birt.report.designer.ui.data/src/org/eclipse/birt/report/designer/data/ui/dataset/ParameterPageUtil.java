/*******************************************************************************
 * Copyright (c) 2005,2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.metadata.IChoice;

/**
 * Utility class for datasetParameterPage.
 *
 * @author Administrator
 *
 */
public final class ParameterPageUtil {

	static IChoice[] dataTypes = DEUtil.getMetaDataDictionary().getStructure(DataSetParameter.STRUCT_NAME)
			.getMember(DataSetParameter.DATA_TYPE_MEMBER).getAllowedChoices().getChoices();

	static String[] directions = { Messages.getString("label.input"), //$NON-NLS-1$
			Messages.getString("label.output"), //$NON-NLS-1$
			Messages.getString("label.inputOutput") //$NON-NLS-1$
	};

	static String[] cellLabels = { Messages.getString("dataset.editor.title.name"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.dataType"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.direction"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.defaultValue"), //$NON-NLS-1$
	};

	static String[] dialogLabels = { Messages.getString("dataset.editor.inputDialog.name"), //$NON-NLS-1$
			Messages.getString("dataset.editor.inputDialog.dataType"), //$NON-NLS-1$
			Messages.getString("dataset.editor.inputDialog.direction"), //$NON-NLS-1$
			Messages.getString("dataset.editor.inputDialog.defaultValue"), //$NON-NLS-1$
			Messages.getString("DataSetParameterPage.cell.linkToSalarParameter") //$NON-NLS-1$
	};
	static String[] odaCellLabels = { Messages.getString("dataset.editor.title.name"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.nativeName"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.dataType"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.direction"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.defaultValue"), //$NON-NLS-1$
			Messages.getString("DataSetParameterPage.editor.title.linkToSalarParameter"), //$NON-NLS-1$
			Messages.getString("DefaultNodeProvider.WarningDialog.Title"), };

	static String[] odaDialogLabels = { Messages.getString("dataset.editor.inputDialog.name"), //$NON-NLS-1$
			Messages.getString("dataset.editor.inputDialog.nativeName"), //$NON-NLS-1$
			Messages.getString("dataset.editor.inputDialog.dataType"), //$NON-NLS-1$
			Messages.getString("dataset.editor.inputDialog.direction"), //$NON-NLS-1$
			Messages.getString("dataset.editor.inputDialog.defaultValue"), //$NON-NLS-1$
			Messages.getString("DataSetParameterPage.cell.linkToSalarParameter") //$NON-NLS-1$
	};

	private static String UNLINKED_REPORT_PARAM = Messages.getString("DataSetParametersPage.reportParam.None");//$NON-NLS-1$

	private static List linkedToParamList = null;

	static boolean isJointDataSetHandle(DataSetHandle handle) {

		if (handle instanceof JointDataSetHandle) {
			return true;
		} else {
			return false;
		}
	}

	static boolean isJointOrDerivedDataSetHandle(DataSetHandle handle) {

		if (handle instanceof JointDataSetHandle || handle instanceof DerivedDataSetHandle) {
			return true;
		} else {
			return false;
		}
	}

	static boolean isOdaDataSetHandle(DataSetHandle handle) {
		if (handle instanceof OdaDataSetHandle) {
			return true;
		} else {
			return false;
		}
	}

	public static String getTypeDisplayName(String typeName) {
		for (int i = 0; i < dataTypes.length; i++) {
			if (dataTypes[i].getName().equals(typeName)) {
				return dataTypes[i].getDisplayName();
			}
		}
		return typeName;
	}

	public static String getTypeName(String typeDisplayName) {
		String name = dataTypes[0].getName();
		for (int i = 0; i < dataTypes.length; i++) {
			if (dataTypes[i].getDisplayName().equals(typeDisplayName)) {
				return dataTypes[i].getName();
			}
		}
		return name;
	}

	static String[] getDataTypeDisplayNames() {
		String[] dataTypeDisplayNames = new String[dataTypes.length];
		for (int i = 0; i < dataTypes.length; i++) {
			dataTypeDisplayNames[i] = dataTypes[i].getDisplayName();
		}
		Arrays.sort(dataTypeDisplayNames);
		return dataTypeDisplayNames;
	}

	/**
	 * Gets an array of all the available linked report parameters.
	 *
	 * @param structureHandle
	 * @return
	 */
	static String[] getLinkedReportParameterNames(OdaDataSetParameterHandle structureHandle) {
		linkedToParamList = Utility.getAllParameters();
		List nameList = new ArrayList();
		nameList.add(UNLINKED_REPORT_PARAM);

		for (int i = 0; i < linkedToParamList.size(); i++) {
			ReportElementHandle handle = (ReportElementHandle) linkedToParamList.get(i);
			if (handle instanceof ScalarParameterHandle
			// now multi-value type report parameter can also be linked with data set
			// parameter now
			// at runtime, only the first provided value is passed into data set
			/*
			 * && !( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals( (
			 * (ScalarParameterHandle) handle ).getParamType( ) ) )
			 */ ) {
				nameList.add(handle.getQualifiedName());
			}
		}
		String[] paramName = new String[nameList.size()];
		for (int i = 0; i < nameList.size(); i++) {
			paramName[i] = nameList.get(i).toString();
		}

		return paramName;
	}

	/**
	 * Gets the ScalarParameterHandle according to the given report parameter name.
	 * Furthermore, if the argument needsRefresh is true then the linked report
	 * parameter list should be refreshed every time this method is called. If
	 * needsRefresh is false, then the linked report parameter list won't be
	 * refreshed after it is initialized. This behavior is for the purpose of
	 * performance.
	 *
	 * @param name
	 * @param needsRefresh indicates whether the linked report parameter list should
	 *                     be refreshed
	 *
	 * @return
	 */
	static ScalarParameterHandle getScalarParameter(String name, boolean needsRefresh) {
		if (needsRefresh || linkedToParamList == null) {
			linkedToParamList = Utility.getAllParameters();
		}
		Object parameterObject = null;
		for (int i = 0; i < linkedToParamList.size(); i++) {
			parameterObject = linkedToParamList.get(i);
			if (parameterObject instanceof ScalarParameterHandle) {
				if (name.equals(((ScalarParameterHandle) parameterObject).getQualifiedName())) {
					return (ScalarParameterHandle) parameterObject;
				}
			}
		}
		return null;
	}

}
