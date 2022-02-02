/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.soapengine.processor;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.soapengine.api.ReportIdType;
import org.eclipse.birt.report.soapengine.processor.IProcessorFactory;

/**
 * Processor factory class.
 */
public class BaseProcessorFactory implements IProcessorFactory {

	/**
	 * Processor factory instance.
	 */
	protected static IProcessorFactory instance = null;

	/**
	 * Get processor factory instance.
	 * 
	 * @return
	 * @throws MarshalException
	 * @throws ValidationException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static synchronized IProcessorFactory getInstance() throws AxisFault {
		if (instance != null) {
			return instance;
		}

		try {
			instance = (IProcessorFactory) Class.forName("com.actuate.common.processor.ProcessorFactory").newInstance(); //$NON-NLS-1$
		} catch (Exception e) {
			instance = null;
		}

		if (instance == null) {
			instance = new BaseProcessorFactory();
		}

		instance.init();

		return instance;
	}

	/**
	 * Initializes the ERNI config manager instance. Read ERNI_Config.xml
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws ValidationException
	 * @throws MarshalException
	 * @throws FileNotFoundException
	 */
	public void init() throws AxisFault {
	}

	public IComponentProcessor createProcessor(String category, ReportIdType component) {
		if (component != null) {
			if (ReportIdType._Document.equalsIgnoreCase(component.getValue())) {
				return new BirtDocumentProcessor();
			} else if (ReportIdType._Table.equalsIgnoreCase(component.getValue())) {
				return new BirtTableProcessor();
			}
		}

		return null;
	}
}
