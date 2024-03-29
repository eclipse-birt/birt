/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
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

	@Override
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

	@Override
	public IReportDesign getDesignInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage getImage(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProperty(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProperty(String path, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IReportEngine getReportEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReportName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap getTestConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDesignHandle(DesignElementHandle handle) {
		// TODO Auto-generated method stub

	}

}
