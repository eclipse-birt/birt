/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import java.util.List;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.api.ExportedColumn;
import org.eclipse.birt.report.service.api.ExportedResultSet;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.Column;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.ResultSet;
import org.eclipse.birt.report.soapengine.api.ResultSets;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Abstract action handler for ExportData event.
 * <P>
 * In current implementation, export data should be from a document object.
 * <p>
 * Support two export types:
 * <ol>
 * <li>Export all the data</li>
 * <li>Use InstanceID to export data of a selected instance data object</li>
 * </ol>
 */
public abstract class AbstractQueryExportActionHandler extends AbstractBaseActionHandler {

	/*
	 * Existed document file
	 */
	protected String __docName;

	/**
	 * Abstract method to handle update response object.
	 *
	 * @param resultSets
	 */
	protected abstract void handleUpdate(ResultSets resultSets);

	/**
	 * Check if document file exists.
	 *
	 * @throws Exception
	 */
	abstract protected void __checkDocumentExists() throws Exception;

	/**
	 * Constructor.
	 *
	 * @param context
	 * @param operation
	 * @param response
	 */
	public AbstractQueryExportActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * Action handler entry point.
	 *
	 * @throws Exception
	 */
	@Override
	protected void __execute() throws Exception {
		BaseAttributeBean attrBean = (BaseAttributeBean) context.getBean();
		__docName = attrBean.getReportDocumentName();
		__checkDocumentExists();

		List exportedResultSets;
		String instanceID = operation.getTarget().getId();

		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, context.getRequest());

		if (instanceID.equals("Document")) { //$NON-NLS-1$
			exportedResultSets = getReportService().getResultSetsMetadata(__docName, options);
		} else { // $NON-NLS-1$
			exportedResultSets = getReportService().getResultSetsMetadata(__docName, instanceID, options);
		}

		if (exportedResultSets == null) {
			// No result sets available
			AxisFault fault = new AxisFault();
			fault.setFaultReason(
					BirtResources.getMessage(ResourceConstants.REPORT_SERVICE_EXCEPTION_EXTRACT_DATA_NO_RESULT_SET));
			throw fault;
		}

		ResultSet[] resultSetArray = getResultSetArray(exportedResultSets);
		ResultSets resultSets = new ResultSets();
		resultSets.setResultSet(resultSetArray);
		handleUpdate(resultSets);
	}

	/**
	 * Prepare returned result set.
	 *
	 * @param exportedResultSets
	 * @return ResultSet[]
	 */
	private ResultSet[] getResultSetArray(List exportedResultSets) {
		assert exportedResultSets != null;

		ResultSet[] rsArray = new ResultSet[exportedResultSets.size()];
		for (int i = 0; i < exportedResultSets.size(); i++) {
			ExportedResultSet rs = (ExportedResultSet) exportedResultSets.get(i);
			List columns = rs.getColumns();
			Column[] colArray = new Column[columns.size()];
			for (int j = 0; j < columns.size(); j++) {
				ExportedColumn col = (ExportedColumn) columns.get(j);
				colArray[j] = new Column(ParameterAccessor.htmlEncode(col.getName()), col.getLabel(),
						col.getVisibility());
			}
			rsArray[i] = new ResultSet(rs.getQueryName(), colArray);
		}

		return rsArray;
	}
}
