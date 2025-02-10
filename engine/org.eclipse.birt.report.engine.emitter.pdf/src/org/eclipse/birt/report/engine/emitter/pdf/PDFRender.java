/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.pdf;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ITemplateArea;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.InlineTextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfTemplate;

/**
 * Class to create the PDF renderer
 *
 * @since 3.3
 *
 */
public class PDFRender extends PageDeviceRender {

	/**
	 * The output stream
	 */
	protected OutputStream output = null;

	protected PDFPage currentPage = null;

	protected boolean isTotalPage = false;

	protected PDFPageDevice currentPageDevice = null;

	protected HashSet<String> bookmarks = new HashSet<>();

	/**
	 * Constructor
	 *
	 * @param services emitter service
	 * @throws EngineException engine exception
	 */
	public PDFRender(IEmitterServices services) throws EngineException {
		initialize(services);
	}

	@Override
	public IPageDevice createPageDevice(String title, String author, String subject, String description,
			IReportContext context, IReportContent report) throws Exception {
		currentPageDevice = new PDFPageDevice(output, title, author, subject, description, context, report);
		return currentPageDevice;
	}

	@Override
	public String getOutputFormat() {
		return "pdf";
	}

	@Override
	protected void newPage(IContainerArea page) {
		super.newPage(page);
		currentPage = (PDFPage) pageGraphic;
	}

	@Override
	public void visitImage(IImageArea imageArea) {
		int imageX = currentX + getX(imageArea);
		int imageY = currentY + getY(imageArea);
		IHyperlinkAction hlAction = imageArea.getAction();
		if (null != hlAction) {
			currentPageDevice.openTag(PdfTag.LINK, imageArea);
		}
		super.visitImage(imageArea);
		createBookmark(imageArea, imageX, imageY);
		if (null != hlAction) {
			createHyperlink(imageArea, imageX, imageY);
			currentPageDevice.closeTag(PdfTag.LINK, imageArea);
		}
	}

	@Override
	public void visitText(ITextArea textArea) {
		IHyperlinkAction hlAction = textArea.getAction();
		if (null != hlAction) {
			currentPageDevice.openTag(PdfTag.LINK, textArea);
		}
		super.visitText(textArea);
		int x = currentX + getX(textArea);
		int y = currentY + getY(textArea);
		createBookmark(textArea, x, y);
		if (null != hlAction) {
			createHyperlink(textArea, x, y);
			currentPageDevice.closeTag(PdfTag.LINK, textArea);
		}
	}

	@Override
	public void visitAutoText(ITemplateArea templateArea) {
		super.visitAutoText(templateArea);
		int x = currentX + getX(templateArea);
		int y = currentY + getY(templateArea);
		// create template according to the page scale
		createTotalPageTemplate(x, y, getWidth(templateArea), getHeight(templateArea), scale);
	}

	@Override
	public void setTotalPage(ITextArea totalPage) {
		super.setTotalPage(totalPage);
		isTotalPage = true;
		HashMap<Float, PdfTemplate> map = ((PDFPageDevice) pageDevice).getTemplateMap();
		if (!map.isEmpty()) {
			float scaleCache = this.scale;
			for (Entry<Float, PdfTemplate> e : map.entrySet()) {
				Float s = e.getKey();
				PdfTemplate template = e.getValue();
				if (template != null) {
					this.scale = s.floatValue();
					drawText(totalPage);
				}
			}
			this.scale = scaleCache;
		}
		isTotalPage = false;
	}

	/**
	 * Closes the document.
	 *
	 * @param rc the report content.
	 */
	@Override
	public void end(IReportContent rc) {
		createTOC();
		super.end(rc);
	}

	@Override
	protected void drawContainer(IContainerArea container) {
		IHyperlinkAction hlAction = container.getAction();
		if (container instanceof InlineTextArea) {
			// A Hyperlink is created for the text already, we don't need it here.
			hlAction = null;
		}
		if (null != hlAction) {
			currentPageDevice.openTag(PdfTag.LINK, container);
		}
		super.drawContainer(container);
		int x = currentX + getX(container);
		int y = currentY + getY(container);
		createBookmark(container, x, y);
		if (hlAction != null) {
			createHyperlink(container, x, y);
			currentPageDevice.closeTag(PdfTag.LINK, container);
		}
	}

	/**
	 * Initializes the pdfEmitter.
	 *
	 * @param services the emitter services object.
	 * @throws EngineException
	 */
	private void initialize(IEmitterServices services) throws EngineException {
		this.services = services;
		// Gets the output file name from RenderOptionBase.OUTPUT_FILE_NAME.
		// It has the top preference.
		reportRunnable = services.getReportRunnable();
		if (reportRunnable != null) {
			reportDesign = (ReportDesignHandle) reportRunnable.getDesignHandle();
		}

		this.context = services.getReportContext();
		this.output = EmitterUtil.getOuputStream(services, "report.pdf");
	}

	@Override
	protected void drawTextAt(ITextArea text, int x, int y, int width, int height, TextStyle textInfo) {
		if (isTotalPage) {
			currentPage.drawTotalPage(text.getText(), x, y, width, height, textInfo, scale);
		} else {
			currentPage.drawText(text.getText(), x, y, width, height, textInfo);
		}
	}

	private PdfAnnotation createHyperlink(IArea area, int x, int y) {
		PdfAnnotation annotation = null;
		IHyperlinkAction hlAction = area.getAction();
		if (null != hlAction) {
			try {
				String systemId = reportRunnable == null ? null : reportRunnable.getReportName();
				int width = getWidth(area);
				int height = getHeight(area);
				String bookmark = hlAction.getBookmark();
				String targetWindow = hlAction.getTargetWindow();
				int type = hlAction.getType();
				Action act = new Action(systemId, hlAction);
				String link = null;
				IHTMLActionHandler actionHandler = null;
				Object ac = services.getOption(IRenderOption.ACTION_HANDLER);
				if (ac instanceof IHTMLActionHandler) {
					actionHandler = (IHTMLActionHandler) ac;
				}
				if (actionHandler != null) {
					link = actionHandler.getURL(act, context);
				} else {
					link = hlAction.getHyperlink();
				}
				switch (type) {
				case IHyperlinkAction.ACTION_BOOKMARK:
					annotation = currentPage.createHyperlink(link, bookmark, targetWindow, type, x, y, width, height);
					break;

				case IHyperlinkAction.ACTION_HYPERLINK:
					annotation = currentPage.createHyperlink(link, null, targetWindow, type, x, y, width, height);
					break;

				case IHyperlinkAction.ACTION_DRILLTHROUGH:
					annotation = currentPage.createHyperlink(link, null, targetWindow, type, x, y, width, height);
					break;
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			if (currentPageDevice.isTagged()) {
				PdfArray children;
				PdfObject childObject = currentPageDevice.structureCurrentNode.get(PdfName.K);
				// The PdfName K means "kids" in this context.
				if (childObject == null) {
					children = new PdfArray();
					currentPageDevice.structureCurrentNode.put(PdfName.K, children);
				} else {
					children = new PdfArray();
					children.add(childObject);
					currentPageDevice.structureCurrentNode.put(PdfName.K, children);
				}
				PdfDictionary objr = new PdfDictionary(PdfName.OBJR);
				PdfIndirectReference annotationRef = annotation.getIndirectReference();
				objr.put(PdfName.OBJ, annotationRef);
				objr.put(PdfName.PG, currentPageDevice.writer.getCurrentPage());
				children.add(objr);
				// The link should contain a /Contents key, because it is required by PDF/UA-1.
				// However, according to the PDF/UA Best Practice Guide, many or most current
				// generation AT do not process this key and relaxation of the /Contents key
				// requirement is anticipated in PDF/UA-2.
				// If the area has a tooltip, we use that for the /Contents, otherwise we do
				// not generate the /Contents entry.
				String tooltip = hlAction.getTooltip();
				if (tooltip != null) {
					annotation.put(PdfName.CONTENTS, new PdfString(tooltip));
				}
				PdfIndirectReference linkref = currentPageDevice.structureCurrentNode.getReference();
				int key = currentPageDevice.structureRoot.addExistingObject(linkref);
				annotation.put(PdfName.STRUCTPARENT, new PdfNumber(key));
				if (currentPageDevice.isPdfAFormat()) {
					// See PDF specification Table 165 - Annotation flags
					// and PDF/A-3 specification rules 6.3.2-1 and 6.3.2-2
					annotation.put(PdfName.F, new PdfNumber(4));
				}
			}

		}
		return annotation;
	}

	@SuppressWarnings("unchecked")
	protected void createBookmark(IArea area, int x, int y) {
		String bookmark = area.getBookmark();
		if (null != bookmark) {
			int height = getHeight(area);
			int width = getWidth(area);
			currentPage.createBookmark(bookmark, x, y, width, height);
			bookmarks.add(bookmark);

			// Make the bookmark available in the appContext.
			// Note that the y value goes downwards, that means y=0 is the top of the page.
			// This is different from PDF.
			// The x,y,width,height values are all integers, measured in thousandth points.
			// That means a value of 1000 is 1mm.
			// An A4 page has a width of ~ 595275 and a height of ~ 841890.
			// The page numbers start at 1.

			@SuppressWarnings("rawtypes")
			Map appContext = context.getAppContext();

			Map<String, BookmarkInfo> bookmarksInContext = (Map<String, BookmarkInfo>) appContext.get("Bookmarks"); //$NON-NLS-1$
			if (bookmarksInContext == null) {
				bookmarksInContext = new HashMap<String, BookmarkInfo>();
				appContext.put("Bookmarks", bookmarksInContext); //$NON-NLS-1$
			}
			final int pageNumber = this.currentPageDevice.writer.getCurrentPageNumber();
			BookmarkInfo bm = new BookmarkInfo(bookmark, pageNumber, x, y, width, height);
			bookmarksInContext.put(bookmark, bm);

			// Note: We could use a similar approach to export the TOC.
			// (see TOCHandler.java, PDFPageDevice.java and other methods here).
			// This could possibly be used for
			// generating section-wise page-numbering based on the TOC.

		}
	}

	@Override
	protected void visitChildren(IContainerArea container) {
		String tagType = null;
		if (currentPageDevice.isTagged()) {
			if (container.getChildrenCount() > 0) {
				tagType = container.getTagType();
				if (tagType != null) {
					currentPageDevice.openTag(tagType, container);
				}
			}
		}
		super.visitChildren(container);
		if (tagType != null) {
			currentPageDevice.closeTag(tagType, container);
		}
	}

	private void createTOC() {
		currentPageDevice.createTOC(bookmarks);
	}

	private void createTotalPageTemplate(int x, int y, int width, int height, float scale) {
		currentPage.createTotalPageTemplate(x, y, width, height, scale);
	}

	@Override
	public void drawTableBorder(TableArea table) {
		boolean tagged = currentPageDevice.isTagged();
		if (tagged) {
			PdfDictionary properties = new PdfDictionary();
			properties.put(new PdfName("Type"), new PdfName("Background"));
			currentPage.beginArtifact(properties);
		}
		super.drawTableBorder(table);
		if (tagged) {
			currentPage.endArtifact();
		}
	}

}
