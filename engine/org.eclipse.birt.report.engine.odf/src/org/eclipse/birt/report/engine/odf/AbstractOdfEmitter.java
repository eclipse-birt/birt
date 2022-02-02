/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.odf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.odf.pkg.ImageEntry;
import org.eclipse.birt.report.engine.odf.pkg.Package;
import org.eclipse.birt.report.engine.odf.style.StyleBuilder;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.writer.ContentWriter;
import org.eclipse.birt.report.engine.odf.writer.MetaWriter;
import org.eclipse.birt.report.engine.odf.writer.StylesWriter;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.ULocale;

public abstract class AbstractOdfEmitter extends ContentEmitterAdapter implements OdfConstants {
	protected static Logger logger = Logger.getLogger(AbstractOdfEmitter.class.getName());

	protected static final Set<Integer> NON_INHERITY_STYLES;

	protected IEmitterServices service = null;

	protected OutputStream out = null;

	protected Package pkg;

	protected ByteArrayOutputStream bodyOut = null;

	protected ByteArrayOutputStream masterPageOut = null;

	protected ContentEmitterVisitor contentVisitor;

	protected AbstractOdfEmitterContext context = null;

	protected IReportContext reportContext;

	protected IReportContent reportContent;

	protected IReportRunnable reportRunnable;

	protected IHTMLActionHandler actionHandler;

	protected int tableCount;

	protected AbstractOdfEmitter() {
		contentVisitor = new ContentEmitterVisitor(this);
	}

	public void initialize(IEmitterServices service) throws EngineException {
		this.service = service;

		this.context = createContext();
		if (service != null) {
			this.reportRunnable = service.getReportRunnable();
			this.actionHandler = (IHTMLActionHandler) service.getOption(RenderOption.ACTION_HANDLER);

			String tempFileDir = service.getReportEngine().getConfig().getTempDir();
			context.setTempFileDir(service.getReportEngine().getConfig().getTempDir());

			// TODO: use temp file for document body
			this.bodyOut = new ByteArrayOutputStream();
			this.masterPageOut = new ByteArrayOutputStream();

			this.out = EmitterUtil.getOuputStream(service, "report." //$NON-NLS-1$
					+ getOutputFormat());
			pkg = Package.createInstance(out, tempFileDir, getRootMime());
			context.setPackage(pkg);
			this.reportContext = service.getReportContext();
		}

		ULocale locale = null;
		if (reportContext != null) {
			locale = ULocale.forLocale(reportContext.getLocale());
		}
		if (locale == null) {
			locale = ULocale.getDefault();
		}
		context.setLocale(locale);
	}

	@Override
	public void start(IReportContent report) throws BirtException {
		super.start(report);
		Object dpi = report.getReportContext().getRenderOption().getOption(IRenderOption.RENDER_DPI);
		int renderDpi = 0;
		if (dpi != null && dpi instanceof Integer) {
			renderDpi = ((Integer) dpi).intValue();
		}
		int reportDpi = PropertyUtil.getRenderDpi(report, renderDpi);
		context.setReportDpi(reportDpi);
		this.reportContent = report;
	}

	public void end(IReportContent report) throws BirtException {
		save();
	}

	private void save() {
		try {
			// output stream for real content
			ContentWriter docContentWriter = new ContentWriter(
					pkg.addEntry(FILE_CONTENT, CONTENT_TYPE_XML).getOutputStream(), context.getReportDpi());
			docContentWriter.write(context.getStyleManager().getStyles(),
					new ByteArrayInputStream(bodyOut.toByteArray()));

			StylesWriter stylesWriter = new StylesWriter(pkg.addEntry(FILE_STYLES, CONTENT_TYPE_XML).getOutputStream(),
					context.getReportDpi());

			// write the styles.xml file
			// including the global styles
			stylesWriter.start();
			stylesWriter.writeStyles(context.getGlobalStyleManager().getStyles());
			stylesWriter.writeMasterPage(new ByteArrayInputStream(masterPageOut.toByteArray()));
			stylesWriter.end();

			pkg.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	protected abstract AbstractOdfEmitterContext createContext();

	protected void writeMetaProperties(IReportContent reportContent) throws IOException, BirtException {
		String creator = null;
		String title = null;
//		String comments = null;;
		String subject = null;
		String description = null;
		if (reportContent != null) {
			ReportDesignHandle designHandle = reportContent.getDesign().getReportDesign();
			creator = designHandle.getAuthor();
			title = reportContent.getTitle();
//			comments = designHandle.getComments( );
			subject = designHandle.getSubject();
			description = designHandle.getDescription();
		}
		MetaWriter writer = new MetaWriter(pkg.addEntry(FILE_META, CONTENT_TYPE_XML).getOutputStream());
		writer.start();
		writer.writeMeta(creator, title, description, subject);
		writer.end();
	}

	protected StyleEntry[] getColStyles(double[] cols) {
		StyleEntry[] styles = new StyleEntry[cols.length];

		StyleEntry previousStyle = null;
		for (int i = 0; i < cols.length; i++) {
			StyleEntry style = null;
			if (previousStyle != null && previousStyle.getDoubleProperty(StyleConstant.WIDTH) != null
					&& (Math.abs(previousStyle.getDoubleProperty(StyleConstant.WIDTH) - cols[i]) < 0.0001)) {
				// reuse same previous style
				style = previousStyle;
			} else {
				style = StyleBuilder.createEmptyStyleEntry(StyleEntry.TYPE_TABLE_COLUMN);
				style.setProperty(StyleConstant.WIDTH, cols[i]);
				context.addStyle(getTableStylePrefix(), style);
				previousStyle = style;
			}

			styles[i] = style;
		}
		return styles;
	}

	/**
	 * Return the MIME-type of the package.
	 */
	protected abstract String getRootMime();

	protected String getTableStylePrefix() {
		return "Table" + tableCount + "."; //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Replace the background image URI with an embedded image entry URI
	 * 
	 * @param style
	 */
	protected void processBackgroundImageStyle(StyleEntry style) {
		if (style == null) {
			return;
		}

		String imageUri = EmitterUtil.getBackgroundImageUrl(style.getStyle(),
				reportContent.getDesign().getReportDesign(), reportContext.getAppContext());
		if (imageUri != null) {
			try {
				ImageEntry entry = context.getImageManager().addImage(imageUri, null, null);
				style.setProperty(StyleConstant.BACKGROUND_IMAGE_URL, entry.getUri());
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			}
		}

	}

	/**
	 * @param page
	 */
	protected StyleEntry makePageLayoutStyle(IPageContent page) {
		StyleEntry pageLayout = StyleBuilder.createStyleEntry(page.getComputedStyle(), StyleConstant.TYPE_PAGE_LAYOUT);
		pageLayout.setProperty(StyleConstant.WIDTH, page.getPageWidth());
		pageLayout.setProperty(StyleConstant.HEIGHT, page.getPageHeight());

		pageLayout.setProperty(StyleConstant.FOOTER_HEIGHT, page.getFooterHeight());
		pageLayout.setProperty(StyleConstant.HEADER_HEIGHT, page.getHeaderHeight());

		pageLayout.setProperty(StyleConstant.MARGIN_TOP, page.getMarginTop());
		pageLayout.setProperty(StyleConstant.MARGIN_BOTTOM, page.getMarginBottom());

		pageLayout.setProperty(StyleConstant.MARGIN_LEFT, page.getMarginLeft());
		pageLayout.setProperty(StyleConstant.MARGIN_RIGHT, page.getMarginRight());

		pageLayout.setProperty(StyleConstant.PAGE_ORIENTATION, page.getOrientation());

		IStyle style = page.getComputedStyle();
		pageLayout.setProperty(StyleConstant.BACKGROUND_IMAGE_WIDTH, style.getBackgroundWidth());
		pageLayout.setProperty(StyleConstant.BACKGROUND_IMAGE_HEIGHT, style.getBackgroundHeight());
		pageLayout.setProperty(StyleConstant.BACKGROUND_IMAGE_LEFT, style.getBackgroundPositionX());
		pageLayout.setProperty(StyleConstant.BACKGROUND_IMAGE_TOP, style.getBackgroundPositionY());
		pageLayout.setProperty(StyleConstant.BACKGROUND_IMAGE_REPEAT, style.getBackgroundRepeat());

		processBackgroundImageStyle(pageLayout);
		context.addGlobalStyle(pageLayout);
		return pageLayout;
	}

	static {
		Set<Integer> nonInherityStyles = new HashSet<Integer>();

		nonInherityStyles.add(IStyle.STYLE_BORDER_BOTTOM_COLOR);
		nonInherityStyles.add(IStyle.STYLE_BORDER_BOTTOM_STYLE);
		nonInherityStyles.add(IStyle.STYLE_BORDER_BOTTOM_WIDTH);
		nonInherityStyles.add(IStyle.STYLE_BORDER_TOP_COLOR);
		nonInherityStyles.add(IStyle.STYLE_BORDER_TOP_STYLE);
		nonInherityStyles.add(IStyle.STYLE_BORDER_TOP_WIDTH);
		nonInherityStyles.add(IStyle.STYLE_BORDER_LEFT_COLOR);
		nonInherityStyles.add(IStyle.STYLE_BORDER_LEFT_STYLE);
		nonInherityStyles.add(IStyle.STYLE_BORDER_LEFT_WIDTH);
		nonInherityStyles.add(IStyle.STYLE_BORDER_RIGHT_COLOR);
		nonInherityStyles.add(IStyle.STYLE_BORDER_RIGHT_STYLE);
		nonInherityStyles.add(IStyle.STYLE_BORDER_RIGHT_WIDTH);

		// TODO: don't inherit background image, so the original background
		// style must be applied on the table/row
		/*
		 * nonInherityStyles.add( IStyle.STYLE_BACKGROUND_IMAGE );
		 * nonInherityStyles.add( IStyle.STYLE_BACKGROUND_ATTACHMENT );
		 * nonInherityStyles.add( IStyle.STYLE_BACKGROUND_HEIGHT );
		 * nonInherityStyles.add( IStyle.STYLE_BACKGROUND_WIDTH );
		 * nonInherityStyles.add( IStyle.STYLE_BACKGROUND_POSITION_X );
		 * nonInherityStyles.add( IStyle.STYLE_BACKGROUND_POSITION_Y );
		 * nonInherityStyles.add( IStyle.STYLE_BACKGROUND_REPEAT );
		 */
		NON_INHERITY_STYLES = Collections.unmodifiableSet(nonInherityStyles);
	}
}
