/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
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
