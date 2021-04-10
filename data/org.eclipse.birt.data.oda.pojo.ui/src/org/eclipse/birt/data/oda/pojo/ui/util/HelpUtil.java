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

package org.eclipse.birt.data.oda.pojo.ui.util;

import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class HelpUtil {

	public static final String PREFIX = "org.eclipse.birt.cshelp" + "."; //$NON-NLS-1$ //$NON-NLS-2$

	public static final String CONEXT_ID_DATASOURCE_POJO = PREFIX + "Wizard_POJO_Datasource_ID";//$NON-NLS-1$

	public static final String CONEXT_ID_DATASET_POJO_PROPS = PREFIX + "Wizard_POJO_Dataset_Props_ID";//$NON-NLS-1$

	public static final String CONEXT_ID_DATASET_POJO_COLUMN_MAPPING = PREFIX + "Wizard_POJO_Dataset_ColumnMapping_ID";//$NON-NLS-1$

	public static final String CONEXT_ID_DATASET_POJO_COLUMN_MAPPING_DIALOG = PREFIX
			+ "Dialog_POJO_Dataset_ColumnMapping_ID";//$NON-NLS-1$

	public static final String CONEXT_ID_DATASET_POJO_CLASS_INPUT_DIALOG = PREFIX + "Dialog_POJO_Dataset_ClassInput_ID";//$NON-NLS-1$

	public static final String CONEXT_ID_DATASET_SHOW_SAMPLE_DATA_DIALOG = PREFIX
			+ "Dialog_POJO_Dataset_ShowSampleData_ID";//$NON-NLS-1$

	public static final String CONEXT_ID_DATASET_POJO_METHOD_PARAMETER = PREFIX
			+ "Wizard_POJO_Dataset_MethodParameter_ID";//$NON-NLS-1$

	/**
	 * Set context-sensitive help
	 * 
	 * @param control
	 * @param contextId
	 */
	public static void setSystemHelp(Control control, String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(control, contextId);
	}

}
