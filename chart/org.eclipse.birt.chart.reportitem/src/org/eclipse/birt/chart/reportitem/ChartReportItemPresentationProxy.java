/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem;

import java.io.InputStream;
import java.util.Locale;

import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentationInfo;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Proxy class of presentation for Chart. It will delegate to proper
 * implementation class according to Chart's context.
 */

public class ChartReportItemPresentationProxy implements IReportItemPresentation {

	private IReportItemPresentation impl;
	private IReportItemPresentationInfo info;

	@Override
	public void init(IReportItemPresentationInfo info) {
		if (info == null) {
			throw new NullPointerException();
		}

		this.info = info;
		impl = createImpl(info);
		impl.init(info);
	}

	protected IReportItemPresentation createImpl(IReportItemPresentationInfo info) {
		ExtendedItemHandle modelHandle = info.getModelObject();
		if (ChartCubeUtil.isInXTabMeasureCell(modelHandle)) {
			// // If chart is in cross tab cell, use specific impl
			if (ChartCubeUtil.isPlotChart(modelHandle)) {
				return new ChartReportItemPresentationPlotImpl();
			} else if (ChartCubeUtil.isAxisChart(modelHandle)) {
				return new ChartReportItemPresentationAxisImpl();
			}
		}
		return ChartReportItemUtil.instanceReportItemPresentation(modelHandle, info);
	}

	@Override
	public void deserialize(InputStream istream) {
		assert impl != null;
		impl.deserialize(istream);
	}

	@Override
	public void finish() {
		assert impl != null;
		impl.finish();
	}

	@Override
	public String getImageMIMEType() {
		assert impl != null;
		return impl.getImageMIMEType();
	}

	@Override
	public int getOutputType() {
		assert impl != null;
		return impl.getOutputType();
	}

	@Override
	public Size getSize() {
		assert impl != null;
		return impl.getSize();
	}

	@Override
	public Object onRowSets(IBaseResultSet[] results) throws BirtException {
		assert impl != null;
		return impl.onRowSets(results);
	}

	@Override
	public IReportItemPresentationInfo getPresentationConfig() {
		return info;
	}

	// Follows deprecated methods. Empty implementation
	@Override
	@SuppressWarnings("deprecation")
	public Object onRowSets(IRowSet[] rowSets) throws BirtException {
		assert false;
		return null;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void setActionHandler(IHTMLActionHandler ah) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setApplicationClassLoader(ClassLoader loader) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setDynamicStyle(IStyle style) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setExtendedItemContent(IContent content) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setLocale(Locale locale) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setModelObject(ExtendedItemHandle modelHandle) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setOutputFormat(String outputFormat) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setReportQueries(IDataQueryDefinition[] queries) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setResolution(int dpi) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setScriptContext(IReportContext context) {
		assert false;

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setSupportedImageFormats(String supportedImageFormats) {
		assert false;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemPresentation#isCacheable(
	 * )
	 */
	@Override
	public boolean isCacheable() {
		return impl.isCacheable();
	}
}
