/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda;

import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;

/**
 * 
 *
 */

public interface IReportParameterAdapter {

	/**
	 * Refreshes property values of the given report parameter by the given data set
	 * design. This method first copies values from ROM data set parameter to report
	 * parameter, then copies values from DataSetDesign to the report parameter.
	 * <p>
	 * When copies values from DataSetDesign, cached value in
	 * OdaDataSetHandle.designerValues are also considerred.
	 * 
	 * 
	 * @param reportParam   the report parameter
	 * @param dataSetParam  the data set parameter
	 * @param dataSetDesign the data set design
	 * @throws SemanticException if value in the data set design is invalid
	 */

	void updateLinkedReportParameter(ScalarParameterHandle reportParam, OdaDataSetParameterHandle dataSetParam,
			DataSetDesign dataSetDesign) throws SemanticException;

	/**
	 * Refreshes property values of the given ROM ODA data set parameter.
	 * 
	 * 
	 * @param reportParam  the report parameter
	 * @param dataSetParam the Oda data set parameter
	 * @throws SemanticException if value in the data set design is invalid
	 */

	void updateLinkedReportParameter(ScalarParameterHandle reportParam, OdaDataSetParameterHandle dataSetParam)
			throws SemanticException;

}