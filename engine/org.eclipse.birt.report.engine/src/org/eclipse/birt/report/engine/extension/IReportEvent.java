/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
