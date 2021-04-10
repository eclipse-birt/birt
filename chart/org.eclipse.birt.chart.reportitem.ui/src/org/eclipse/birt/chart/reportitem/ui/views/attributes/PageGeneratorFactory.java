/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.attributes;

import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.core.runtime.IAdapterFactory;

public class PageGeneratorFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return new ChartPageGenerator();
	}

	public Class[] getAdapterList() {
		return new Class[] { IPageGenerator.class };
	}

}
