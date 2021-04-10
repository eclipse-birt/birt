/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.soapengine.processor;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.soapengine.api.ReportIdType;
import org.eclipse.birt.report.soapengine.processor.IComponentProcessor;

public interface IProcessorFactory {
	abstract public void init() throws AxisFault;

	abstract public IComponentProcessor createProcessor(String category, ReportIdType component);
}
