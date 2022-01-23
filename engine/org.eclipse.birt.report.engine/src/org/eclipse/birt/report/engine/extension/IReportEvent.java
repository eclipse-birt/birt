/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;

public interface IReportEvent {

	public static final int ON_PREPARE_EVENT = 1;

	public static final int ON_CREATE_EVENT = 2;

	public static final int ON_RENDER_EVENT = 3;

	public static final int ON_PAGEBREAK_EVENT = 4;

	int getEventType();

	DesignElementHandle getHandle();

	IReportEventContext getContext();
}
