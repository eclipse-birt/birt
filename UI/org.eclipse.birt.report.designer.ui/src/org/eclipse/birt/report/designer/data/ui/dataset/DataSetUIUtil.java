/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;

/**
 * The utility class.
 */
public final class DataSetUIUtil {
	// logger instance
	private static Logger logger = Logger.getLogger(DataSetUIUtil.class.getName());

	/**
	 * Update column cache without holding events
	 * 
	 * @param dataSetHandle
	 * @throws SemanticException
	 */
	public static void updateColumnCache(DataSetHandle dataSetHandle) throws SemanticException {
		try {
			updateColumnCache(dataSetHandle, false);
		} catch (BirtException e) {
			logger.entering(DataSetUIUtil.class.getName(), "updateColumnCache", //$NON-NLS-1$
					new Object[] { e });
		}
	}

	/**
	 * Update column cache with clean the resultset property
	 * 
	 * @param dataSetHandle
	 * @throws SemanticException
	 */
	public static void updateColumnCacheAfterCleanRs(DataSetHandle dataSetHandle) throws SemanticException {
		if (dataSetHandle.getCachedMetaDataHandle() != null
				&& dataSetHandle.getCachedMetaDataHandle().getResultSet() != null)
			dataSetHandle.getCachedMetaDataHandle().getResultSet().clearValue();
		if (dataSetHandle instanceof OdaDataSetHandle) {
			if (dataSetHandle.getPropertyHandle(OdaDataSetHandle.RESULT_SET_PROP).isLocal())
				dataSetHandle.getPropertyHandle(OdaDataSetHandle.RESULT_SET_PROP).setValue(new ArrayList());
		}
		updateColumnCache(dataSetHandle);

	}

	/**
	 * Save the column meta data to data set handle.
	 * 
	 * @param dataSetHandle
	 * @param holdEvent
	 * @throws BirtException
	 */
	public static void updateColumnCache(DataSetHandle dataSetHandle, boolean holdEvent) throws BirtException {
		DataService.getInstance().updateColumnCache(dataSetHandle, holdEvent);
	}

	public static ResourceIdentifiers createResourceIdentifiers() {
		ResourceIdentifiers ri = new ResourceIdentifiers();
		ri.setDesignResourceBaseURI(getReportDesignPath());
		ri.setApplResourceBaseURI(getBIRTResourcePath());
		return ri;
	}

	/**
	 * Gets the BIRT resource path
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	public static URI getReportDesignPath() {
		if (Utility.getReportModuleHandle() == null || Utility.getReportModuleHandle().getSystemId() == null) {
			return null;
		}
		try {
			return new URI(Utility.getReportModuleHandle().getSystemId().getPath());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Gets the report design file path
	 * 
	 * @return
	 */
	public static URI getBIRTResourcePath() {
		try {
			return new URI(encode(ReportPlugin.getDefault().getResourceFolder()));
		} catch (URISyntaxException e) {
			return null;
		}
	}

	private static String encode(String location) {
		try {
			return new File(location).toURI().toASCIIString().replace(new File("").toURI().toASCIIString(), ""); //$NON-NLS-1$//$NON-NLS-2$
		} catch (Exception e) {
			return location;
		}
	}

	/**
	 * Add this method according to GUI's requirement.This method is only for
	 * temporarily usage.
	 * 
	 * @param dataSetHandle
	 * @return
	 * @throws SemanticException
	 * @deprecated
	 */
	public static CachedMetaDataHandle getCachedMetaDataHandle(DataSetHandle dataSetHandle) throws SemanticException {
		if (!hasMetaData(dataSetHandle)) {
			try {
				updateColumnCache(dataSetHandle, true);
			} catch (BirtException e) {
				logger.entering(DataSetUIUtil.class.getName(), "updateColumnCache", //$NON-NLS-1$
						new Object[] { e });
			}
		}

		return dataSetHandle.getCachedMetaDataHandle();
	}

	/**
	 * Whether there is cached metadata in datasetHandle. The current status of
	 * datasetHandle will be processed, we won's do the refresh to retrieve the
	 * metadata. If the cached metadata handle is null or metadata handle is empty,
	 * return false.
	 * 
	 * @param dataSetHandle
	 * @return
	 */
	public static boolean hasMetaData(DataSetHandle dataSetHandle) {
		CachedMetaDataHandle metaData = dataSetHandle.getCachedMetaDataHandle();
		if (metaData == null)
			return false;
		else {
			Iterator iter = metaData.getResultSet().iterator();
			if (iter.hasNext())
				return true;
			else {
				if (dataSetHandle instanceof OdaDataSetHandle) {
					Iterator parametersIterator = ((OdaDataSetHandle) dataSetHandle).parametersIterator();
					while (parametersIterator.hasNext()) {
						Object parameter = parametersIterator.next();
						if (parameter instanceof OdaDataSetParameterHandle) {
							if (((OdaDataSetParameterHandle) parameter).isOutput()) {
								return true;
							}
						}
					}
				}
				return false;
			}

		}
	}

	/**
	 * Map oda data type to model data type.
	 * 
	 * @param modelDataType
	 * @return
	 */
	public static String toModelDataType(int modelDataType) {
		if (modelDataType == DataType.INTEGER_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER;
		else if (modelDataType == DataType.STRING_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
		else if (modelDataType == DataType.DATE_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
		else if (modelDataType == DataType.DECIMAL_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL;
		else if (modelDataType == DataType.DOUBLE_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT;
		else if (modelDataType == DataType.SQL_DATE_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATE;
		else if (modelDataType == DataType.SQL_TIME_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_TIME;
		else if (modelDataType == DataType.BOOLEAN_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN;
		else if (modelDataType == DataType.JAVA_OBJECT_TYPE)
			return DesignChoiceConstants.COLUMN_DATA_TYPE_JAVA_OBJECT;

		return DesignChoiceConstants.COLUMN_DATA_TYPE_ANY;
	}

}
