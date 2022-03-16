/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

abstract public class DesignRunnable implements IReportRunnable {

	/**
	 * reference to report engine
	 */
	protected IReportEngine engine;

	/**
	 * the report
	 */
	protected DesignElementHandle designHandle;

	/**
	 * constructor
	 *
	 * @param report reference to report
	 */
	public DesignRunnable(IReportEngine engine, DesignElementHandle designHandle) {
		this.engine = engine;
		this.designHandle = designHandle;
	}

	@Override
	public Object getProperty(String propertyName) {
		FactoryPropertyHandle handle = getDesignHandle().getFactoryPropertyHandle(propertyName);
		if (handle != null) {
			return handle.getStringValue();
		}
		return null;
	}

	@Override
	public Object getProperty(String path, String propertyName) {
		return null;
	}

	@Override
	public DesignElementHandle getDesignHandle() {
		return designHandle;
	}

	@Override
	public IReportEngine getReportEngine() {
		return engine;
	}

	@Override
	public String getReportName() {
		ModuleHandle moduleHandle = getModuleHandle();
		return moduleHandle.getFileName();
	}

	@Override
	public HashMap getTestConfig() {
		ModuleHandle moduleHandle = getModuleHandle();
		HashMap configs = new HashMap();
		Iterator iter = moduleHandle.configVariablesIterator();
		if (iter != null) {
			while (iter.hasNext()) {
				ConfigVariableHandle handle = (ConfigVariableHandle) iter.next();
				String name = handle.getName();
				String value = handle.getValue();
				configs.put(name, value);
			}
		}
		return configs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getImage(java.lang
	 * .String)
	 */
	@Override
	public IImage getImage(String name) {
		ModuleHandle moduleHandle = getModuleHandle();
		EmbeddedImage embeddedImage = moduleHandle.findImage(name);

		if (embeddedImage != null) {
			Image image = new Image(embeddedImage.getData(moduleHandle.getModule()), name);
			image.setReportRunnable(this);

			return image;
		}

		return null;
	}

	protected ModuleHandle getModuleHandle() {
		return (ModuleHandle) designHandle;
	}
}
