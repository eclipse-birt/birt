/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview;

/**
 * ISecurityConstants
 */
public interface IPreviewConstants {

	String SID = "TransientSecurityID"; //$NON-NLS-1$
	String DSID = "TransientDataSecurityID"; //$NON-NLS-1$

	String MAX_DATASET_ROWS = "MaxDataSetRows"; //$NON-NLS-1$
	String MAX_CUBE_ROW_LEVELS = "MaxCubeRowLevels"; //$NON-NLS-1$
	String MAX_CUBE_COLUMN_LEVELS = "MaxCubeColumnLevels"; //$NON-NLS-1$
	String MAX_DATA_MODEL_MEMORY_SIZE = "MaxLinkedDataModelMemorySize"; //$NON-NLS-1$

	String REPORT_PREVIEW_OPTIONS = "ReportPreviewOptions"; //$NON-NLS-1$

	String REPORT_FILE_PATH = "ReportFilePath"; //$NON-NLS-1$
}
