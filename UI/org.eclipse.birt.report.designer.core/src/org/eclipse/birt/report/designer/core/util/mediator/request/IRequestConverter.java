/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.core.util.mediator.request;

import java.util.List;

/**
 * Convert the selection list to model list
 * 
 */
public interface IRequestConverter {

	/**
	 * Convert the selection list to model list
	 * 
	 * @param list
	 * @return
	 */
	List convertSelectionToModelLisr(List list);
}
