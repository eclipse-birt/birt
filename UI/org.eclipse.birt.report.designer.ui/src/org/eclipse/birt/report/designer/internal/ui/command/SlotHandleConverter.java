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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.core.commands.AbstractParameterValueConverter;
import org.eclipse.core.commands.ParameterValueConversionException;

/**
 *
 */

public class SlotHandleConverter extends AbstractParameterValueConverter {

	public SlotHandleConverter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object convertToObject(String parameterValue) throws ParameterValueConversionException {
		String elementId = parameterValue.substring(0, parameterValue.indexOf("#")); //$NON-NLS-1$
		String slotId = parameterValue.substring(parameterValue.indexOf("#") + 1);
		return SessionHandleAdapter.getInstance().getReportDesignHandle().getElementByID(Long.parseLong(elementId))
				.getSlot(Integer.parseInt(slotId));
	}

	@Override
	public String convertToString(Object parameterValue) throws ParameterValueConversionException {

		return ((SlotHandle) parameterValue).getElement().getID() + "#" //$NON-NLS-1$
				+ ((SlotHandle) parameterValue).getSlotID();
	}

}
