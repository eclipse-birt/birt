/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;

public class ReportEngineCreator {
	/**
	 * create an engine instance
	 * 
	 * @param config
	 * @return
	 * @throws BirtException
	 */
	public static IReportEngine createReportEngine(EngineConfig config) throws BirtException {
		return new ReportEngineFactory().createReportEngine(config);
	}
}
