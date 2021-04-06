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

package org.eclipse.birt.chart.reportitem;

import java.io.OutputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IReportItemGenerationInfo;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Proxy class of generation for Chart. It will delegate to proper
 * implementation class according to Chart's context.
 */

public class ChartReportItemGenerationProxy implements IReportItemGeneration {

	private IReportItemGeneration impl;
	private IReportItemGenerationInfo info;

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

	private IReportItemGeneration createImpl(ExtendedItemHandle modelHandle) {
//		DesignElementHandle handle = modelHandle.getContainer( );
//		if ( handle instanceof ExtendedItemHandle )
//		{
//			String exName = ( (ExtendedItemHandle) handle ).getExtensionName( );
//			if ( ICrosstabConstants.CROSSTAB_CELL_EXTENSION_NAME.equals( exName ) )
//			{
//				// If chart is in cross tab cell, use specific impl
//				return new ChartCrosstabItemGenerationImpl( );
//			}
//		}
		return new ChartReportItemGenerationImpl();
	}

	public boolean needSerialization() {
		assert impl != null;
		return impl.needSerialization();
	}

	public void onRowSets(IRowSet[] rowSets) throws BirtException {
		assert impl != null;
		impl.onRowSets(rowSets);
	}

	public void serialize(OutputStream ostream) throws BirtException {
		assert impl != null;
		impl.serialize(ostream);
	}

	public void setModelObject(ExtendedItemHandle modelHandle) {
		impl = createImpl(modelHandle);
		assert impl != null;
		impl.setModelObject(modelHandle);
	}

	public void finish() {
		assert impl != null;
		impl.finish();
	}

	public Size getSize() {
		assert impl != null;
		return impl.getSize();
	}

	public void setApplicationClassLoader(ClassLoader loader) {
		assert impl != null;
		impl.setApplicationClassLoader(loader);
	}

	public void setReportQueries(IDataQueryDefinition[] queries) {
		assert impl != null;
		impl.setReportQueries(queries);
	}

	public void setScriptContext(IReportContext context) {
		assert impl != null;
		impl.setScriptContext(context);
	}

	public void onRowSets(IBaseResultSet[] results) throws BirtException {
		assert impl != null;
		impl.onRowSets(results);

	}

	public void setExtendedItemContent(IContent content) {
		assert impl != null;
		impl.setExtendedItemContent(content);

	}

	public IReportItemGenerationInfo getGenerationConfig() {
		return info;
	}

}
