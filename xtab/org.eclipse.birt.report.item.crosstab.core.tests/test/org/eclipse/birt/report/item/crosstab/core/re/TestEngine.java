/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.re;

import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.junit.Ignore;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 *
 */
@Ignore("XTAB API doesn't support empty XTAB now and this one can be covered by runtime SDK test")
public class TestEngine extends TestCase implements ICrosstabConstants {

	public static final String PDF_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_PDF;
	public static final String HTML_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_HTML;

	private IReportEngine engine;
	private IDesignEngine designEngine;

	private ReportDesignHandle designHandle;

	private String format = HTML_FORMAT;

	public void testEngineExtension() {
		ThreadResources.setLocale(ULocale.ENGLISH);

		if (designEngine == null) {
			designEngine = new DesignEngine(new DesignConfig());
			// MetaDataDictionary.reset( );
			// initialize the metadata.

			designEngine.getMetaData();
		}

		SessionHandle sh = designEngine.newSessionHandle(ULocale.getDefault());
		designHandle = sh.createDesign();
		ModuleHandle mh = designHandle.getModuleHandle();

		CrosstabReportItemHandle cri = createCrosstab(mh, true, true, true, 2, true, false);

		try {
			designHandle.getBody().add(cri.getModelHandle());
		} catch (ContentException | NameException e1) {
			e1.printStackTrace();
		}

		// ==================================

		IReportRunnable report = null;
		try {
			report = getEngine().openReportDesign(designHandle);
		} catch (EngineException e) {
			e.printStackTrace();
			return;
		}

		// format = PDF_FORMAT;

		IRunAndRenderTask task = getEngine().createRunAndRenderTask(report);
		HTMLRenderOption options = new HTMLRenderOption();
		options.setOutputFormat(format);
		String outputPath = "c:\\test." + format; //$NON-NLS-1$
		options.setOutputFileName(outputPath);
		options.setHtmlPagination(true);
		task.setRenderOption(options);
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setImageDirectory("./images"); //$NON-NLS-1$
		HashMap appContext = new HashMap();
		appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
		task.setAppContext(appContext);
		HashMap params = new HashMap();
		task.setParameterValues(params);
		task.validateParameters();

		// Run the report.
		try {
			task.run();

			System.out.println("Task finined successfully."); //$NON-NLS-1$
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	protected IReportEngine getEngine() {
		if (engine == null) {
			try {
				EngineConfig config = new EngineConfig();
				// config.setEngineHome( getReportEngineHome( ) );

				// Platform.startup( config );
				// IReportEngineFactory factory = (IReportEngineFactory)
				// Platform.createFactoryObject(
				// IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
				// engine = factory.createReportEngine( config );

				engine = new ReportEngine(config);

				engine.changeLogLevel(Level.WARNING);

				HTMLEmitterConfig hc = new HTMLEmitterConfig();
				HTMLCompleteImageHandler imageHandler = new HTMLCompleteImageHandler();
				hc.setImageHandler(imageHandler);
				config.setEmitterConfiguration(HTML_FORMAT, hc);
			} catch (Exception ex) {
			}
		}
		return engine;
	}

	private CrosstabReportItemHandle createCrosstab(ModuleHandle module, boolean hasColumnEdge, boolean hasRowEdge,
			boolean hasGrandTotal, int measureCount, boolean hasMeasureHeader, boolean isVerticalMeasureHeader) {
		try {
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil
					.getReportItem(CrosstabExtendedItemFactory.createCrosstabReportItem(module, null, null));

			if (isVerticalMeasureHeader) {
				crosstabItem.setMeasureDirection(MEASURE_DIRECTION_VERTICAL);
			}

			applyBorder(crosstabItem, "green");
			applyBackground(crosstabItem, "#DBE5F1");

			if (hasRowEdge) {
				if (hasGrandTotal) {
					crosstabItem.addGrandTotal(ROW_AXIS_TYPE);

					crosstabItem.getGrandTotal(ROW_AXIS_TYPE).addContent(createLabelHandle(module, "Grand Total"));

					applyBorder(crosstabItem.getGrandTotal(ROW_AXIS_TYPE), "green");
					applyBackground(crosstabItem.getGrandTotal(ROW_AXIS_TYPE), "orange");
				}

				crosstabItem.insertDimension(null, ROW_AXIS_TYPE, -1);
				DimensionViewHandle dvh = crosstabItem.getDimension(ROW_AXIS_TYPE, 0);
				dvh.insertLevel(null, -1);
				dvh.insertLevel(null, -1);

				dvh.getLevel(0).addAggregationHeader();
				dvh.getLevel(1).addAggregationHeader();

				dvh.getLevel(0).getAggregationHeader().addContent(createLabelHandle(module, "sub total"));
				dvh.getLevel(1).getAggregationHeader().addContent(createLabelHandle(module, "sub total"));

				dvh.getLevel(0).getCell().addContent(createLabelHandle(module, "LV1"));
				dvh.getLevel(1).getCell().addContent(createLabelHandle(module, "LV2"));

				// dvh.getLevel( 1 ).setPageBreakAfter( "always" );0
				// dvh.getLevel( 1 ).setPageBreakBefore(
				// "always-excluding-first" );
				// dvh.getLevel( 1 ).setPageBreakAfter(
				// DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST
				// );

				// dvh.getLevel( 0 )
				// .setAggregationHeaderLocation(
				// AGGREGATION_HEADER_LOCATION_BEFORE );
				// dvh.getLevel( 1 )
				// .setAggregationHeaderLocation(
				// AGGREGATION_HEADER_LOCATION_BEFORE );

				applyVerticalAlign(dvh.getLevel(0).getCell(), "middle");
				applyVerticalAlign(dvh.getLevel(1).getCell(), "middle");

				applyBorder(dvh.getLevel(0).getCell(), "green");
				applyBorder(dvh.getLevel(1).getCell(), "green");
				applyBorder(dvh.getLevel(0).getAggregationHeader(), "green");
				applyBackground(dvh.getLevel(0).getAggregationHeader(), "olive");
				applyTextAlign(dvh.getLevel(0).getAggregationHeader(), "center");
				applyBorder(dvh.getLevel(1).getAggregationHeader(), "green");
				applyBackground(dvh.getLevel(1).getAggregationHeader(), "olive");
				applyTextAlign(dvh.getLevel(1).getAggregationHeader(), "center");

				crosstabItem.insertDimension(null, ROW_AXIS_TYPE, -1);
				dvh = crosstabItem.getDimension(ROW_AXIS_TYPE, 1);
				dvh.insertLevel(null, -1);

				dvh.getLevel(0).getCell().addContent(createLabelHandle(module, "LV3"));

				// dvh.getLevel( 0 ).setPageBreakBefore(
				// "always-excluding-first" );
				// dvh.getLevel( 0 ).setPageBreakAfter( "always-excluding-last"
				// );

				applyTextAlign(dvh.getLevel(0).getCell(), "center");
				applyBorder(dvh.getLevel(0).getCell(), "green");
			}

			if (hasColumnEdge) {
				if (hasGrandTotal) {
					crosstabItem.addGrandTotal(COLUMN_AXIS_TYPE);

					crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE)
							.addContent(createLabelHandle(module, "Grand\r\nTotal"));

					applyBorder(crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE), "green");
					applyBackground(crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE), "orange");
				}

				crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);
				DimensionViewHandle dvh2 = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 0);
				dvh2.insertLevel(null, -1);
				dvh2.insertLevel(null, -1);
				dvh2.getLevel(0).addAggregationHeader();
				dvh2.getLevel(1).addAggregationHeader();

				dvh2.getLevel(0).getAggregationHeader().addContent(createLabelHandle(module, "sub total"));
				dvh2.getLevel(1).getAggregationHeader().addContent(createLabelHandle(module, "sub total"));

				dvh2.getLevel(0).getCell().addContent(createLabelHandle(module, "LV1"));
				dvh2.getLevel(1).getCell().addContent(createLabelHandle(module, "LV2"));

				// dvh2.getLevel( 0 )
				// .setAggregationHeaderLocation(
				// AGGREGATION_HEADER_LOCATION_BEFORE );
				// dvh2.getLevel( 1 )
				// .setAggregationHeaderLocation(
				// AGGREGATION_HEADER_LOCATION_BEFORE );

				applyTextAlign(dvh2.getLevel(0).getCell(), "center");
				applyTextAlign(dvh2.getLevel(1).getCell(), "center");

				applyBorder(dvh2.getLevel(0).getCell(), "green");
				applyBorder(dvh2.getLevel(1).getCell(), "green");
				applyBorder(dvh2.getLevel(0).getAggregationHeader(), "green");
				applyBackground(dvh2.getLevel(0).getAggregationHeader(), "olive");
				applyTextAlign(dvh2.getLevel(0).getAggregationHeader(), "center");
				applyBorder(dvh2.getLevel(1).getAggregationHeader(), "green");
				applyBackground(dvh2.getLevel(1).getAggregationHeader(), "olive");
				applyTextAlign(dvh2.getLevel(1).getAggregationHeader(), "center");

				crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);
				DimensionViewHandle dvh3 = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 1);
				dvh3.insertLevel(null, -1);

				dvh3.getLevel(0).getCell().addContent(createLabelHandle(module, "LV3"));

				applyVerticalAlign(dvh3.getLevel(0).getCell(), "middle");
				applyTextAlign(dvh3.getLevel(0).getCell(), "center");

				applyBorder(dvh3.getLevel(0).getCell(), "green");
			}

			if (measureCount > 0) {
				crosstabItem.insertMeasure(null, -1);

				crosstabItem.getMeasure(0).getCell().addContent(createLabelHandle(module, "Measure 0"));

				applyBorder(crosstabItem.getMeasure(0).getCell(), "green");
				applyBackground(crosstabItem.getMeasure(0).getCell(), "teal");

				if (hasMeasureHeader) {
					crosstabItem.getMeasure(0).addHeader();

					crosstabItem.getMeasure(0).getHeader().addContent(createLabelHandle(module, "Header 0"));

					applyBorder(crosstabItem.getMeasure(0).getHeader(), "green");
					applyBackground(crosstabItem.getMeasure(0).getHeader(), "silver");
				}
			}

			if (measureCount > 1) {

				crosstabItem.insertMeasure(null, -1);

				crosstabItem.getMeasure(1).getCell().addContent(createLabelHandle(module, "Measure 1"));

				applyBorder(crosstabItem.getMeasure(1).getCell(), "green");
				applyBackground(crosstabItem.getMeasure(1).getCell(), "lime");

				if (hasMeasureHeader) {
					crosstabItem.getMeasure(1).addHeader();

					crosstabItem.getMeasure(1).getHeader().addContent(createLabelHandle(module, "Header 1"));

					applyBorder(crosstabItem.getMeasure(1).getHeader(), "green");
					applyBackground(crosstabItem.getMeasure(1).getHeader(), "aqua");
				}

			}

			syncAggregationCells(module, crosstabItem);

			return crosstabItem;
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void syncAggregationCells(ModuleHandle module, CrosstabReportItemHandle crosstabItem)
			throws SemanticException {
		for (int k = 0; k < crosstabItem.getMeasureCount(); k++) {
			MeasureViewHandle mv = crosstabItem.getMeasure(k);
			AggregationCellHandle handle = mv.addAggregation(null, null, null, null);

			handle.addContent(createLabelHandle(module, "Aggr"));

			applyBorder(handle, "green");
			applyTextAlign(handle, "center");
			// applyVerticalAlign( handle, "middle" );
		}
	}

	private void applyBorder(AbstractCrosstabItemHandle handle, String color) throws SemanticException {
		if (handle == null) {
			return;
		}

		StyleHandle style = handle.getModelHandle().getPrivateStyle();

		style.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
		style.setBorderRightStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
		style.setBorderTopStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
		style.setBorderBottomStyle(DesignChoiceConstants.LINE_STYLE_SOLID);

		style.getBorderLeftWidth().setStringValue("1px");
		style.getBorderRightWidth().setStringValue("1px");
		style.getBorderTopWidth().setStringValue("1px");
		style.getBorderBottomWidth().setStringValue("1px");

		style.getBorderLeftColor().setStringValue(color);
		style.getBorderRightColor().setStringValue(color);
		style.getBorderTopColor().setStringValue(color);
		style.getBorderBottomColor().setStringValue(color);
	}

	private void applyBackground(AbstractCrosstabItemHandle handle, String color) throws SemanticException {
		if (handle == null) {
			return;
		}

		StyleHandle style = handle.getModelHandle().getPrivateStyle();

		style.getBackgroundColor().setStringValue(color);
	}

	private void applyTextAlign(AbstractCrosstabItemHandle handle, String align) throws SemanticException {
		if (handle == null) {
			return;
		}

		StyleHandle style = handle.getModelHandle().getPrivateStyle();

		style.setTextAlign(align);
	}

	private void applyVerticalAlign(AbstractCrosstabItemHandle handle, String align) throws SemanticException {
		if (handle == null) {
			return;
		}

		StyleHandle style = handle.getModelHandle().getPrivateStyle();

		style.setVerticalAlign(align);
	}

	private LabelHandle createLabelHandle(ModuleHandle module, String text) throws SemanticException {
		LabelHandle label = module.getElementFactory().newLabel(null);
		label.setText(text);
		return label;
	}

}
