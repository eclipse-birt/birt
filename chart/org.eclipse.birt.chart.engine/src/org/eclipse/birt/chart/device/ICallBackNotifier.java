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
