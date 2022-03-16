/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.sql.Types;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;

/**
 * The utility class for SQLDataSetEditorPage
 *
 */
public class SQLUtility {
	/**
	 * save the dataset design's metadata info
	 *
	 * @param design
	 */
	public static void saveDataSetDesign(DataSetDesign design, IResultSetMetaData meta, IParameterMetaData paramMeta) {
		try {
			setParameterMetaData(design, paramMeta);
			// set resultset metadata
			setResultSetMetaData(design, meta);
		} catch (OdaException e) {
			// no result set definition available, reset in dataSetDesign
			design.setResultSets(null);
		}
	}

	/**
	 * Set parameter metadata in dataset design
	 *
	 * @param design
	 * @param query
	 */
	private static void setParameterMetaData(DataSetDesign dataSetDesign, IParameterMetaData paramMeta) {
		try {
			// set parameter metadata
			mergeParameterMetaData(dataSetDesign, paramMeta);
		} catch (OdaException e) {
			// do nothing, to keep the parameter definition in dataset design
			// dataSetDesign.setParameters( null );
		}
	}

	/**
	 * solve the BIDI line problem
	 *
	 * @param lineText
	 * @return
	 */
	public static int[] getBidiLineSegments(String lineText) {
		int[] seg = null;
		if (lineText != null && lineText.length() > 0
				&& !new Bidi(lineText, Bidi.DIRECTION_LEFT_TO_RIGHT).isLeftToRight()) {
			List list = new ArrayList();

			// Punctuations will be regarded as delimiter so that different
			// splits could be rendered separately.
			Object[] splits = lineText.split("\\p{Punct}");

			// !=, <> etc. leading to "" will be filtered to meet the rule that
			// segments must not have duplicates.
			for (int i = 0; i < splits.length; i++) {
				if (!splits[i].equals("")) {
					list.add(splits[i]);
				}
			}
			splits = list.toArray();

			// first segment must be 0
			// last segment does not necessarily equal to line length
			seg = new int[splits.length + 1];
			for (int i = 0; i < splits.length; i++) {
				seg[i + 1] = lineText.indexOf((String) splits[i], seg[i]) + ((String) splits[i]).length();
			}
		}

		return seg;
	}

	/**
	 * Return pre-defined query text pattern with every element in a cell.
	 *
	 * @return pre-defined query text
	 */
	public static String getQueryPresetTextString(String extensionId) {
		String[] lines = getQueryPresetTextArray(extensionId);
		StringBuilder result = new StringBuilder();
		if (lines != null && lines.length > 0) {
			for (int i = 0; i < lines.length; i++) {
				result.append(lines[i]).append(i == lines.length - 1 ? " " : " \n"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return result.toString();
	}

	/**
	 * Return pre-defined query text pattern with every element in a cell in an
	 * Array
	 *
	 * @return pre-defined query text in an Array
	 */
	public static String[] getQueryPresetTextArray(String extensionId) {
		final String[] lines;
		if (extensionId.equals("org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet")) {
			lines = new String[] { "{call procedure-name(arg1,arg2, ...)}" };
		} else {
			lines = new String[] { "select", "from" };
		}
		return lines;
	}

	/**
	 * merge paramter meta data between dataParameter and datasetDesign's parameter.
	 *
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private static void mergeParameterMetaData(DataSetDesign dataSetDesign, IParameterMetaData md) throws OdaException {
		if (md == null || dataSetDesign == null) {
			return;
		}
		DataSetParameters dataSetParameter = DesignSessionUtil.toDataSetParametersDesign(md, ParameterMode.IN_LITERAL);

		if (dataSetParameter != null) {
			Iterator iter = dataSetParameter.getParameterDefinitions().iterator();
			while (iter.hasNext()) {
				ParameterDefinition defn = (ParameterDefinition) iter.next();
				proccessParamDefn(defn, dataSetParameter);
			}
		}
		dataSetDesign.setParameters(dataSetParameter);
	}

	/**
	 * Process the parameter definition for some special case
	 *
	 * @param defn
	 * @param parameters
	 */
	private static void proccessParamDefn(ParameterDefinition defn, DataSetParameters parameters) {
		if (defn.getAttributes().getNativeDataTypeCode() == Types.NULL) {
			defn.getAttributes().setNativeDataTypeCode(Types.CHAR);
		}
	}

	/**
	 * Set the resultset metadata in dataset design
	 *
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private static void setResultSetMetaData(DataSetDesign dataSetDesign, IResultSetMetaData md) throws OdaException {
		if (md == null || dataSetDesign == null) {
			return;
		}

		ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign(md);

		if (columns != null) {
			ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition();
			// jdbc does not support result set name
			resultSetDefn.setResultSetColumns(columns);
			// no exception; go ahead and assign to specified dataSetDesign
			dataSetDesign.setPrimaryResultSet(resultSetDefn);
			dataSetDesign.getResultSets().setDerivedMetaData(true);
		} else {
			dataSetDesign.setResultSets(null);
		}
	}
}
