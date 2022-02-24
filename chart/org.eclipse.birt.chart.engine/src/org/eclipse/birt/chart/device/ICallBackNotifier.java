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

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.model.attribute.CallBackValue;

/**
 * This interface adds the callback ability to the existing IUpdateNotifer
 * interface, any listener who want to perform a callback must implement this
 * instead of the original IUpdateNotifer.
 */
public interface ICallBackNotifier extends IUpdateNotifier {

	/**
	 * Invokes the callBack.
	 * 
	 * @param event  CallBack event object.
	 * @param source CallBack source object.
	 * @param value  CallBack value.
	 */
	void callback(Object event, Object source, CallBackValue value);
}
