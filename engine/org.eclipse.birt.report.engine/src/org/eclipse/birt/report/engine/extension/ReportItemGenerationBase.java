/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension;

import java.io.OutputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.internal.RowSet;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Implements a default generation peer that does nothing
 */
public class ReportItemGenerationBase implements IReportItemGeneration {

	protected ExtendedItemHandle modelHandle;
	protected ClassLoader appClassLoader;
	protected IReportContext context;
	protected IDataQueryDefinition[] queries;
	protected IContent content;
	protected IReportItemGenerationInfo info;

	/**
	 * Constructor that does nothing
	 */
	public ReportItemGenerationBase() {
	}

	public void init(IReportItemGenerationInfo info) {
		if (info == null) {
			throw new NullPointerException();
		}

		this.info = info;
		setModelObject(info.getModelObject());
		setApplicationClassLoader(info.getApplicationClassLoader());
		setScriptContext(info.getReportContext());
		setReportQueries(info.getReportQueries());
		setExtendedItemContent(info.getExtendedItemContent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#getSize()
	 */
	public Size getSize() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#finish()
	 */
	public void finish() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemGeneration#setModelObject
	 * (org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject(ExtendedItemHandle modelHandle) {
		this.modelHandle = modelHandle;
	}

	public void setApplicationClassLoader(ClassLoader loader) {
		this.appClassLoader = loader;
	}

	public void setScriptContext(IReportContext context) {
		this.context = context;
	}

	public void setReportQueries(IDataQueryDefinition[] queries) {
		this.queries = queries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemGeneration#onRowSets(org.
	 * eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public void onRowSets(IRowSet[] rowSets) throws BirtException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemGeneration#onRowSets(org.
	 * eclipse.birt.report.engine.extension.IBaseResultSet[])
	 */
	public void onRowSets(IBaseResultSet[] results) throws BirtException {
		if (results == null) {
			onRowSets((IRowSet[]) null);
			return;
		}

		int length = results.length;

		// test if the IBaseResultSet is a ICubeResultSet
		for (int i = 0; i < length; i++) {
			if (results[i].getType() == IBaseResultSet.CUBE_RESULTSET) {
				return;
			}
		}

		IRowSet[] rowSets = new IRowSet[length];
		for (int index = 0; index < length; index++) {
			IQueryResultSet resultSet = (IQueryResultSet) results[index];
			rowSets[index] = new RowSet(resultSet);
		}
		onRowSets(rowSets);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemGeneration#serialize(java
	 * .io.OutputStream)
	 */
	public void serialize(OutputStream ostream) throws BirtException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#
	 * needSerialization()
	 */
	public boolean needSerialization() {
		return false;
	}

	public void setExtendedItemContent(IContent content) {
		this.content = content;
	}

	public IReportItemGenerationInfo getGenerationConfig() {
		return info;
	}

}
