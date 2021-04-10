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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * 
 */
public class ContentExceptionFactory {

	/**
	 * Creates a ContentException from the given options.
	 * 
	 * @param containerInfor
	 * @param content
	 * @param errorCode
	 * @return
	 */
	public static ContentException createContentException(ContainerContext containerInfor, DesignElement content,
			String errorCode) {
		if (containerInfor == null)
			return null;
		if (!StringUtil.isBlank(containerInfor.getPropertyName()))
			return new ContentException(containerInfor.getElement(), containerInfor.getPropertyName(), content,
					errorCode);
		return new ContentException(containerInfor.getElement(), containerInfor.getSlotID(), content, errorCode);
	}

	/**
	 * 
	 * @param containerInfor
	 * @param errorCode
	 * @return
	 */
	public static ContentException createContentException(ContainerContext containerInfor, String errorCode) {
		if (containerInfor == null)
			return null;
		if (!StringUtil.isBlank(containerInfor.getPropertyName()))
			return new ContentException(containerInfor.getElement(), containerInfor.getPropertyName(), errorCode);
		return new ContentException(containerInfor.getElement(), containerInfor.getSlotID(), errorCode);
	}

}
