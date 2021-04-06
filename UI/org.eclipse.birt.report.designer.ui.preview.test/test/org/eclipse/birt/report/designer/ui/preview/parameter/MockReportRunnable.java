/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Mock report runnable.
 */

public class MockReportRunnable implements IReportRunnable {

	public DesignElementHandle getDesignHandle() {
		String fileName = "input/ParameterFactory.xml"; //$NON-NLS-1$
		ThreadResources.setLocale(ULocale.ENGLISH);

		SessionHandle sessionHandle = new DesignEngine(new DesignConfig())
				.newSessionHandle(ThreadResources.getLocale());
		try {

			ReportDesignHandle designHandle = sessionHandle.openDesign(getClass().getResource(fileName).toString());
			return designHandle;
		} catch (Exception e) {
			// Do nothing;

			return null;
		}
	}

	public IReportDesign getDesignInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	public IImage getImage(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getProperty(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getProperty(String path, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	public IReportEngine getReportEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReportName() {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap getTestConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDesignHandle(DesignElementHandle handle) {
		// TODO Auto-generated method stub

	}

}
