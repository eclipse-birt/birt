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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;

/**
 * The utility class which provides many static methods used in Model.
 */

public class ModelUtilBase {

	protected static boolean needSkipProperty(DesignElementHandle elementHandle, String propName) {

		if (IStyledElementModel.STYLE_PROP.equals(propName) || IDesignElementModel.EXTENDS_PROP.equals(propName)
				|| IDesignElementModel.USER_PROPERTIES_PROP.equals(propName)
				|| IExtendedItemModel.EXTENSION_NAME_PROP.equals(propName)
				|| IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP.equals(propName)
				|| IDesignElementModel.VIEW_ACTION_PROP.equals(propName)
				|| IModuleModel.LIBRARIES_PROP.equals(propName))
			return true;
		else if (elementHandle instanceof ExtendedItemHandle
				&& IOdaExtendableElementModel.EXTENSION_ID_PROP.equals(propName))
			return true;
		else
			return false;
	}
}
