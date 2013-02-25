/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.model.api.ReportItemHandle;


public interface IExtendedDataXtabAdapter
{
	public boolean isExtendedDataColumn(Object element);
	
	public boolean setExtendedData(ReportItemHandle element, Object object);
	
	public String getExtendedDataName(ReportItemHandle element);
	
	public boolean contains(Object parent, Object child);
	
	public Object[] getSupportedTypes(Object element, Object parent);
	
	public boolean hasDataSource(Object element);

}
