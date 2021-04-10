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

package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;

import org.eclipse.birt.report.engine.api.InstanceID;

public interface IReportletDocument extends IInternalReportDocument {

	boolean isReporltetDocument() throws IOException;

	String getReportletBookmark() throws IOException;

	InstanceID getReportletInstanceID() throws IOException;
}
