/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.factory;

import java.net.URL;

/**
 * Provides a way to find a resource in resource folder. If chart engine runs
 * within BIRT, ChartReportItemImpl has implemented this interface by default,
 * which reuses the model's mechanism. Otherwise the user should implement it.
 */
public interface IResourceFinder {
	URL findResource(String fileName);
}
