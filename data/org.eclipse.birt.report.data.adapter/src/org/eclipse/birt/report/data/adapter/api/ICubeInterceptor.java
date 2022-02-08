/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * Interceptor for one type of cube
 *
 */
public interface ICubeInterceptor {

	void preDefineCube(DataSessionContext appContext, CubeHandle handle) throws BirtException;

	boolean needDefineCube() throws BirtException;
}
