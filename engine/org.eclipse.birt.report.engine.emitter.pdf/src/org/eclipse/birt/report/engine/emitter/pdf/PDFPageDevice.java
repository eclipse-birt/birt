/*******************************************************************************
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pdf;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.util.BundleVersionUtil;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.CellArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;

import com.ibm.icu.util.ULocale;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfBoolean;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfICCBased;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfStructureElement;
import com.lowagie.text.pdf.PdfStructureTreeRoot;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.xmp.DublinCoreSchema;
import com.lowagie.text.xml.xmp.LangAlt;
import com.lowagie.text.xml.xmp.PdfA1Schema;
import com.lowagie.text.xml.xmp.PdfSchema;
import com.lowagie.text.xml.xmp.XmpBasicSchema;
import com.lowagie.text.xml.xmp.XmpSchema;
import com.lowagie.text.xml.xmp.XmpWriter;

/**
 * Definition of the PDF emitter page
 *
 * @since 3.3
 *
 */
public class PDFPageDevice implements IPageDevice {

	/** PDF possible PDF version (catalog) */
	/** PDF version 1.3 */
	private static final String PDF_VERSION_1_3 = "1.3";
	/** PDF version 1.4 */
	private static final String PDF_VERSION_1_4 = "1.4";
	/** PDF version 1.5 */
	private static final String PDF_VERSION_1_5 = "1.5";
	/** PDF version 1.6 */
	private static final String PDF_VERSION_1_6 = "1.6";
	/** PDF version 1.7 */
	private static final String PDF_VERSION_1_7 = "1.7";
	/** PDF version 2.0 */
	private static final String PDF_VERSION_2_0 = "2.0";

	/** PDFX/PDFA conformance */
	/** PDF conformance PDF Standard */
	private static final String PDF_CONFORMANCE_STANDARD = "PDF.Standard";
	/** PDF conformance PDF/X-3:2002 */
	private static final String PDF_CONFORMANCE_X32002 = "PDF.X32002";
	/** PDF conformance PDF/A-1a */
	private static final String PDF_CONFORMANCE_A1A = "PDF.A1A";
	/** PDF conformance PDFA-1b */
	private static final String PDF_CONFORMANCE_A1B = "PDF.A1B";
	/** PDF conformance PDF/A-2a */
	private static final String PDF_CONFORMANCE_A2A = "PDF.A2A";
	/** PDF conformance PDFA-2b */
	private static final String PDF_CONFORMANCE_A2B = "PDF.A2B";
	/** PDF conformance PDF/A-3a */
	private static final String PDF_CONFORMANCE_A3A = "PDF.A3A";
	/** PDF conformance PDFA-3b */
	private static final String PDF_CONFORMANCE_A3B = "PDF.A3B";
	/** PDF conformance PDF/A-3u */
	private static final String PDF_CONFORMANCE_A3U = "PDF.A3U";
	/** PDF conformance PDF/A-4f */
	private static final String PDF_CONFORMANCE_A4F = "PDF.A4F";

	/** PDF conformance PDF X-1a:2001, unsupported (TODO: CMYK of PDF.X1A2001 */

	private static final String PDF_UA_CONFORMANCE_1 = "PDF.UA-1";
	private static final String PDF_UA_CONFORMANCE_2 = "PDF.UA-2";
	private static final String PDF_UA_CONFORMANCE_NONE = "none";

	/** PDF ICC color profile */
	/** PDF ICC default color profile RGB */
	private static final String PDF_ICC_PROFILE_DEFAULT = "sRGB IEC61966-2.1";
	/** PDF ICC color profile RGB */
	private static final String PDF_ICC_COLOR_RGB = "RGB";
	/** PDF ICC color profile CMYK */
	private static final String PDF_ICC_COLOR_CMYK = "CMYK";

	/**
	 * The pdf Document object created by iText
	 */
	protected Document doc = null;

	/**
	 * The Pdf Writer
	 */
	protected PdfWriter writer = null;

	// The StructureTree defines the logical structure of the content.
	PdfStructureTreeRoot structureRoot = null;
	PdfStructureElement structureDocument = null;
	PdfStructureElement structureCurrentNode = null;

	protected IReportContext context;

	protected IReportContent report;

	protected static Logger logger = Logger.getLogger(PDFPageDevice.class.getName());

	protected PDFPage currentPage = null;

	protected HashMap<Float, PdfTemplate> templateMap = new HashMap<>();

	protected HashMap<String, PdfTemplate> imageCache = new HashMap<>();

	/**
	 * the iText and Birt engine version info.
	 */
	protected static String[] versionInfo = { BundleVersionUtil.getBundleVersion("org.eclipse.birt.report.engine") };

	protected final static int MAX_PAGE_WIDTH = 14400000; // 200 inch
	protected final static int MAX_PAGE_HEIGHT = 14400000; // 200 inch

	/** PDF emitter: user properties */

	/** PDF prepend document: names for list of files to prepend */
	private final static String PDF_PREPEND_DOCUMENTS = "PdfEmitter.PrependDocumentList";
	private final static String PREPEND_PROPERTY_NAME = "PrependList";

	/** PDF append document: names for list of files to append */
	private final static String PDF_APPEND_DOCUMENTS = "PdfEmitter.AppendDocumentList";
	private final static String APPEND_PROPERTY_NAME = "AppendList";

	/** PDF version: allowed version, 1.3 - 1.7 */
	private final static String PDF_VERSION = "PdfEmitter.Version";

	/**
	 * PDF conformance: PDF.X1A2001, PDF.X32002, PDF.A1A, PDF.A1B
	 */
	private final static String PDF_CONFORMANCE = "PdfEmitter.Conformance";

	private final static String PDF_UA_CONFORMANCE = "PdfEmitter.PDFUAConformance";

	/**
	 * PDF/A ICC color profile (default: sRGB IEC61966-2.1, standard by: HP &
	 * Microsoft)
	 */
	private final static String PDF_ICC_PROFILE_EXTERNAL_FILE = "PdfEmitter.IccProfileFile";

	/** PDF/A ICC color type CMYK or RGB (default: RGB) */
	private static final String PDF_ICC_COLOR_TYPE = "PdfEmitter.IccColorType";

	/**
	 * PDF/A with document title cause validation issue of PDF/A by openPDF 1.3.30
	 * issue based on XMP metadata "dc:title" (default: without title)
	 */
	private final static String PDFA_ADD_DOCUMENT_TITLE = "PdfEmitter.PDFA.AddDocumentTitle";

	private final static String PDFA_FALLBACK_FONT = "PdfEmitter.PDFA.FallbackFont";

	private final static String PDF_FONT_CID_SET = "PdfEmitter.IncludeCidSet";

	private final static String PDF_BACKGROUND_IMAGE_SVG_TO_RASTER = "PdfEmitter.BackGroundImageSvgToRaster";

	protected Map<String, Expression> userProperties;

	private char pdfVersion = '0';

	private int pdfConformance = PdfWriter.PDFXNONE;

	private int pdfUAConformance = PdfWriter.PDFXNONE;

	private boolean isPdfAFormat = false;

	private boolean isPdfUAFormat = false;

	private boolean addPdfADocumentTitle = false;

	private String defaultFontPdfA = null;

	private boolean includeFontCidSet = true;

	private boolean useBackgroundImageSvg = true;

	/**
	 *
	 * Constructor to define the PDF
	 *
	 * @param output      output stream of the document
	 * @param title       title of the document
	 * @param author      author of the document
	 * @param subject     subject of the document
	 * @param description description of the document
	 * @param context     context of the document
	 * @param report      report object of the document
	 */
	public PDFPageDevice(OutputStream output, String title, String author, String subject, String description,
			IReportContext context, IReportContent report) {
		this.context = context;
		this.report = report;
		doc = new Document();
		try {
			writer = PdfWriter.getInstance(doc, new BufferedOutputStream(output));
			EngineResourceHandle handle = new EngineResourceHandle(ULocale.forLocale(context.getLocale()));

			this.userProperties = report.getDesign().getUserProperties();

			// PDF version user property based
			this.setPdfVersion();
			// PDF/A & PDF/X conformance user property based
			this.setPdfConformance();

			// PDFU/UA conformance user property based
			this.setPdfUAConformance();

			// PDF/A, set the default font of not embeddable fonts
			this.setDefaultFontPdfA();

			// PDF include font CID set stream
			this.setIncludeCidSet();

			// Validate the usage of SVG images for background images
			this.useBackgroundImageSvgToRasterImage();

			// PDF/A (A1A, A1B), avoid compression and transparency
			if (!this.isPdfAFormat) {
				writer.setFullCompression();
				writer.setRgbTransparencyBlending(true);
			}

			String creator = handle.getMessage(MessageConstants.PDF_CREATOR, versionInfo);
			doc.addCreator(creator);

			if (null != author) {
				doc.addAuthor(author);
			}
			// openPDF 1.3.30: title of PDF/A won't be set correctly,
			// issue on xmp meta data at "dc:title"
			if (!this.isPdfAFormat || this.addPdfADocumentTitle) {
				if (null != title) {
					doc.addTitle(title);
				}
			}
			if (null != subject) {
				doc.addSubject(subject);
				doc.addKeywords(subject);
			}
			if (description != null) {
				doc.addHeader("Description", description);
			}

			if (this.isPdfUAFormat) {
				String localeString = report.getDesign().getLocale();
				if (localeString == null || localeString.isEmpty()) {
					throw new BirtException("The report needs a locale property for PDF/UA!");
				}
				Locale locale = new Locale(localeString);
				String language = locale.toString();
				language = language.replace('_', '-'); // 'de_de' is invalid, it should be 'de-DE'.
				doc.setDocumentLanguage(language);
				// In order to declare the main language of the document,
				// we need to use the extraCatalog. That way we don't need to
				// modify existing OpenPDF source code.
				PdfDictionary extraCatalog = writer.getExtraCatalog();
				extraCatalog.put(PdfName.LANG, new PdfString(language, PdfObject.TEXT_UNICODE));

				writer.addViewerPreference(PdfName.DISPLAYDOCTITLE, PdfBoolean.PDFTRUE);
			}

			// Add in prepending PDF's
			// modified here. This will grab a global variable called
			// appendPDF, and take a list of strings of PDF files to
			// append to the end.
			// this is where we will test the merge
			List<InputStream> pdfs = new ArrayList<>();
			Object listObject;

			// added null check
			if (userProperties != null && userProperties.containsKey(PDFPageDevice.PREPEND_PROPERTY_NAME)) {
				listObject = userProperties.get(PDFPageDevice.PREPEND_PROPERTY_NAME);
			} else if (userProperties != null && userProperties.containsKey(PDFPageDevice.PDF_PREPEND_DOCUMENTS)) {
				listObject = userProperties.get(PDFPageDevice.PDF_PREPEND_DOCUMENTS);
			} else {
				listObject = Expression.newConstant(-1,
						(String) getReportDesignConfiguration(this.report, PDFPageDevice.PDF_PREPEND_DOCUMENTS));
			}

			if (listObject != null) {
				Expression exp = (Expression) listObject;

				Object result = context.evaluate(exp);
				// there are two options here. 1 is the user property "AppendList" is a
				// comma-seperated
				// string list. If so, check that it is a String, and split it.
				if (result instanceof String) {
					String list = (String) result;
					// check that the report variable AppendList is set, and actually has value
					if (list != null) {
						if (list.length() > 0) {
							// iterate over the list, and create a file inputstream for each file location.
							for (String s : list.split(",")) {
								// If there is an exception creating the input stream, don't stop execution.
								// Just gracefully let the user know that there was an error with the variable.
								try {
									String fileName = s.trim().replace("\\", "\\\\");
									File f = new File(fileName);
									if (f.exists()) {
										FileInputStream fis = new FileInputStream(f);
										pdfs.add(fis);
									} else {
										// get the file using context.getResource() for relative or universal paths
										URL url = context.getResource(fileName);
										InputStream is = new BufferedInputStream(url.openStream());
										pdfs.add(is);
									}
								} catch (Exception e) {
									logger.log(Level.WARNING, e.getMessage(), e);
								}
							}
						}
					}
				}

				// The other is a "Named Expression", which is basically a user property that is
				// the result of an expression instead of a string literal. This should be set
				// as an arraylist through BIRT script
				if (result instanceof ArrayList) {
					ArrayList<String> pdfList = (ArrayList<String>) result;
					for (String fileName : pdfList) {
						// If there is an exception creating the input stream, don't stop execution.
						// Just gracefully let the user know that there was an error with the variable.
						fileName = fileName.replace("\\", "\\\\");
						try {
							File f = new File(fileName);

							if (f.exists()) {
								FileInputStream fis = new FileInputStream(f);
								pdfs.add(fis);
							} else {
								// get the file using context.getResource() for relative or universal paths
								URL url = context.getResource(fileName);
								InputStream is = new BufferedInputStream(url.openStream());
								pdfs.add(is);
							}
						} catch (Exception e) {
							logger.log(Level.WARNING, e.getMessage(), e);
						}
					}
				}

				// check size of PDFs to make sure we aren't calling this on a 0 size array
				if (pdfs.size() > 0) {
					// this hasn't been initialized yet, open the doc
					if (!this.doc.isOpen()) {
						this.doc.open();
					}
					concatPDFs(pdfs, false);
				}
			}
			// End Modification
		} catch (DocumentException | BirtException be) {
			logger.log(Level.SEVERE, be.getMessage(), be);
		}
	}

	/**
	 * Initialize the attributes for the PDF tag tree structure.
	 */
	public void initStructure() {

		structureRoot = writer.getStructureTreeRoot();
		structureDocument = new PdfStructureElement(structureRoot, new PdfName("Document"));
		structureCurrentNode = structureDocument;

	}

	/**
	 * Constructor for test
	 *
	 * @param output
	 */
	public PDFPageDevice(OutputStream output) {
		doc = new Document();
		try {
			writer = PdfWriter.getInstance(doc, new BufferedOutputStream(output));
		} catch (DocumentException de) {
			logger.log(Level.SEVERE, de.getMessage(), de);
		}
	}

	/**
	 * Set pdf template
	 *
	 * @param key               the index key of the pdf template
	 * @param totalPageTemplate pdf template document
	 */
	public void setPDFTemplate(Float key, PdfTemplate totalPageTemplate) {
		templateMap.put(key, totalPageTemplate);
	}

	/**
	 * Get all pdf templates
	 *
	 * @return Return all pdf templates
	 */
	public HashMap<Float, PdfTemplate> getTemplateMap() {
		return templateMap;
	}

	/**
	 * Get the pdf template
	 *
	 * @param key index of the pdf template
	 * @return Return the index based pdf template
	 */
	public PdfTemplate getPDFTemplate(Float key) {
		return templateMap.get(key);
	}

	/**
	 * Check if a template was used
	 *
	 * @param key key of the template
	 * @return Return the result of the template check
	 */
	public boolean hasTemplate(Float key) {
		return templateMap.containsKey(key);
	}

	/**
	 * Get the image cache
	 *
	 * @return Return the image cache
	 */
	public HashMap<String, PdfTemplate> getImageCache() {
		return imageCache;
	}

	@Override
	public void close() throws Exception {
		if (!doc.isOpen()) {
			// to ensure we create a PDF file
			doc.open();
		}

		// modified here. This will grab a global variable called
		// appendPDF, and take a list of strings of PDF files to
		// append to the end.
		// this is where we will test the merge
		List<InputStream> pdfs = new ArrayList<>();

		Map<String, Expression> userProperties = report.getDesign().getUserProperties();
		Object listObject;

		// added null check
		if (userProperties != null && userProperties.containsKey(PDFPageDevice.APPEND_PROPERTY_NAME)) {
			listObject = userProperties.get(PDFPageDevice.APPEND_PROPERTY_NAME);
		} else if (userProperties != null && userProperties.containsKey(PDFPageDevice.PDF_APPEND_DOCUMENTS)) {
			listObject = userProperties.get(PDFPageDevice.PDF_APPEND_DOCUMENTS);
		} else {
			listObject = Expression.newConstant(-1,
					(String) getReportDesignConfiguration(this.report, PDFPageDevice.PDF_APPEND_DOCUMENTS));
		}

		if (listObject != null) {
			Expression exp = (Expression) listObject;

			Object result = context.evaluate(exp);
			// 2 options to append pdf
			// option 1: is the user property "AppendList" is a comma-seperated string list
			// If so, check that it is a String, and split it.
			if (result instanceof String) {
				String list = (String) result;
				// check that the report variable AppendList is set, and actually has value
				if (list != null) {
					if (list.length() > 0) {
						// iterate over the list, and create a fileinputstream for each file location.
						for (String s : list.split(",")) {
							// If there is an exception creating the input stream, don't stop execution.
							// Just gracefully let the user know that there was an error with the variable.
							try {
								String fileName = s.trim().replace("\\", "\\\\");
								File f = new File(fileName);
								if (f.exists()) {
									FileInputStream fis = new FileInputStream(f);
									pdfs.add(fis);
								} else {
									// get the file using context.getResource() for relative or universal paths
									URL url = context.getResource(fileName);
									InputStream is = new BufferedInputStream(url.openStream());
									pdfs.add(is);
								}
							} catch (Exception e) {
								logger.log(Level.WARNING, e.getMessage(), e);
							}
						}
					}
				}
			}

			// option 2: "Named Expression", which is basically a user property that is the
			// result of an expression instead of a string literal. This should be set as an
			// arraylist through BIRT script
			if (result instanceof ArrayList) {
				ArrayList<String> pdfList = (ArrayList<String>) result;

				for (String fileName : pdfList) {
					// If there is an exception creating the input stream, don't stop execution.
					// Just gracefully let the user know that there was an error with the variable.
					fileName = fileName.replace("\\", "\\\\");
					try {
						File f = new File(fileName);
						if (f.exists()) {
							FileInputStream fis = new FileInputStream(f);
							pdfs.add(fis);
						} else {
							// get the file using context.getResource() for relative or universal paths
							URL url = context.getResource(fileName);
							InputStream is = new BufferedInputStream(url.openStream());
							pdfs.add(is);
						}
					} catch (Exception e) {
						logger.log(Level.WARNING, e.getMessage(), e);
					}
				}
			}
			// check size of PDFs to make sure we aren't calling this on a 0 size array
			if (pdfs.size() > 0) {
				concatPDFs(pdfs, false);
			}
		}

		if (this.isPdfAFormat || this.isPdfUAFormat) {
			// PDF/A: set color profile and metadata
			this.setPdfIccXmp();
		}

		writer.setPageEmpty(false);
		if (this.isPdfUAFormat) {
			writer.setTabs(PdfName.S);
		}
		if (doc.isOpen()) {
			doc.close();
		}
	}

	@Override
	public IPage newPage(int width, int height, Color backgroundColor) {
		int w = Math.min(width, MAX_PAGE_WIDTH);
		int h = Math.min(height, MAX_PAGE_HEIGHT);
		currentPage = createPDFPage(w, h);
		currentPage.drawBackgroundColor(backgroundColor, 0, 0, w, h);
		return currentPage;
	}

	protected PDFPage createPDFPage(int pageWidth, int pageHeight) {
		return new PDFPage(pageWidth, pageHeight, doc, writer, this);
	}

	/**
	 * Create the TOC of the pdf document
	 *
	 * @param bookmarks bookmarks of the TOC
	 */
	public void createTOC(Set<String> bookmarks) {
		// we needn't create the TOC if there is no page in the PDF file.
		// the doc is opened only if the user invokes newPage.
		if (!doc.isOpen()) {
			return;
		}
		if (bookmarks.isEmpty()) {
			writer.setViewerPreferences(PdfWriter.PageModeUseNone);
			return;
		}
		ULocale ulocale = null;
		Locale locale = context.getLocale();
		if (locale == null) {
			ulocale = ULocale.getDefault();
		} else {
			ulocale = ULocale.forLocale(locale);
		}
		// Before closing the document, we need to create TOC.
		ITOCTree tocTree = report.getTOCTree("pdf", //$NON-NLS-1$
				ulocale);
		if (tocTree == null) {
			writer.setViewerPreferences(PdfWriter.PageModeUseNone);
		} else {
			TOCNode rootNode = tocTree.getRoot();
			if (rootNode == null || rootNode.getChildren().isEmpty()) {
				writer.setViewerPreferences(PdfWriter.PageModeUseNone);
			} else {
				writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
				TOCHandler tocHandler = new TOCHandler(rootNode, writer.getDirectContent().getRootOutline(), bookmarks);
				tocHandler.createTOC();
			}
		}
	}

	protected TOCHandler createTOCHandler(TOCNode root, PdfOutline outline, Set<String> bookmarks) {
		return new TOCHandler(root, outline, bookmarks);
	}

	/**
	 * Patched PDF to Combine PDF Files
	 *
	 * Given a list of PDF Files When a user wants to append PDf files to a PDF
	 * emitter output Then Append the PDF files to the output stream or output file
	 *
	 * @param streamOfPDFFiles
	 * @param paginate
	 */
	public void concatPDFs(List<InputStream> streamOfPDFFiles, boolean paginate) {

		Document document = doc;
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);

				int n = pdfReader.getNumberOfPages();

				totalPages += n;
			}
			// Create a writer for the outputstream
			PdfWriter writer = this.writer;

			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					pageOfCurrentReaderPDF++;
					currentPageNumber++;

					// note: page size has to be set before new page created. current page is
					// already initialized
					Rectangle sourcePageSize = pdfReader.getPageSize(pageOfCurrentReaderPDF);
					document.setPageSize(sourcePageSize);

					document.newPage();

					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);

					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages,
								520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the PDF version based on the user property
	 *
	 * @throws BirtException
	 */
	private void setPdfVersion() throws BirtException {
		String userPdfVersion;
		// PDF version based on user property
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDF_VERSION)) {
			userPdfVersion = this.userProperties.get(PDFPageDevice.PDF_VERSION).toString();
		} else {
			userPdfVersion = (String) getReportDesignConfiguration(this.report, PDFPageDevice.PDF_VERSION);
		}
		this.setPdfVersion(userPdfVersion);
	}

	/**
	 * Set the PDF version
	 *
	 * @param version key to set the PDF version (e.g. 1.7)
	 * @throws BirtException
	 */
	public void setPdfVersion(String version) throws BirtException {
		switch (version) {
		case PDFPageDevice.PDF_VERSION_1_3:
			this.pdfVersion = PdfWriter.VERSION_1_3;
			break;
		case PDFPageDevice.PDF_VERSION_1_4:
			this.pdfVersion = PdfWriter.VERSION_1_3;
			break;
		case PDFPageDevice.PDF_VERSION_1_5:
			this.pdfVersion = PdfWriter.VERSION_1_3;
			break;
		case PDFPageDevice.PDF_VERSION_1_6:
			this.pdfVersion = PdfWriter.VERSION_1_3;
			break;
		case PDFPageDevice.PDF_VERSION_1_7:
			this.pdfVersion = PdfWriter.VERSION_1_7;
			break;
		case PDFPageDevice.PDF_VERSION_2_0:
			// this.pdfVersion = PdfWriter.VERSION_2_0;
			// OpenPDF does not yet support creation of PDF 2.0
			throw new BirtException("OpenPDF does not yet support creation of PDF 2.0");
		}
		// version only set if the PDF version exists
		if (this.pdfVersion != '0') {
			writer.setAtLeastPdfVersion(this.pdfVersion);
		}
	}

	/**
	 * Set the PDF/UA conformance based on the user property
	 */
	private void setPdfUAConformance() {
		String userPdfUAConformance;
		// PDF version based on user property
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDF_UA_CONFORMANCE)) {
			userPdfUAConformance = this.userProperties.get(PDFPageDevice.PDF_UA_CONFORMANCE).toString().toUpperCase();
		} else {
			userPdfUAConformance = (String) getReportDesignConfiguration(this.report, PDFPageDevice.PDF_UA_CONFORMANCE);
		}
		this.setPdfUAConformance(userPdfUAConformance);
	}

	/**
	 * Set the PDF version
	 *
	 * @param conformance key to set the PDF/UA version (e.g. PDF/UA-1)
	 */
	public void setPdfUAConformance(String conformance) {
		switch (conformance) {
		case PDFPageDevice.PDF_UA_CONFORMANCE_1:
			this.pdfUAConformance = 1;
			this.isPdfUAFormat = true;
			writer.setTagged();
			break;
		case PDFPageDevice.PDF_UA_CONFORMANCE_2:
			this.pdfUAConformance = 2;
			this.isPdfUAFormat = true;
			writer.setTagged();
			break;
		}
	}

	/**
	 * Get the PDF conformance
	 *
	 * @return Return the PDF/UA conformance (e.g. PDF.UA-1)
	 */
	public String getPdfUAConformance() {
		switch (this.pdfUAConformance) {
		case 1:
			return PDFPageDevice.PDF_UA_CONFORMANCE_1;
		case 2:
			return PDFPageDevice.PDF_UA_CONFORMANCE_2;
		default:
			return PDFPageDevice.PDF_UA_CONFORMANCE_NONE;
		}
	}

	/**
	 * Get the PDF version
	 *
	 * @return Return the PDF version (e.g. 1.7)
	 */
	public String getPdfVersion() {
		switch (this.pdfVersion) {
		case PdfWriter.VERSION_1_3:
			return PDFPageDevice.PDF_VERSION_1_3;
		case PdfWriter.VERSION_1_4:
			return PDFPageDevice.PDF_VERSION_1_4;
		case PdfWriter.VERSION_1_5:
			return PDFPageDevice.PDF_VERSION_1_5;
		case PdfWriter.VERSION_1_6:
			return PDFPageDevice.PDF_VERSION_1_6;
		case PdfWriter.VERSION_1_7:
			return PDFPageDevice.PDF_VERSION_1_7;
		default:
			return PDFPageDevice.PDF_VERSION_1_5;
		}
	}

	/**
	 * Set the PDF conformance user property based
	 */
	private void setPdfConformance() {
		String userPdfConformance;

		// PDFA & PDFX conformance, based on user property
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDF_CONFORMANCE)) {
			userPdfConformance = this.userProperties.get(PDFPageDevice.PDF_CONFORMANCE).toString().toUpperCase();
		} else {
			userPdfConformance = (String) getReportDesignConfiguration(this.report, PDFPageDevice.PDF_CONFORMANCE);
		}

		switch (userPdfConformance) {
		case PDFPageDevice.PDF_CONFORMANCE_X32002:
			this.pdfConformance = PdfWriter.PDFX32002;
			this.isPdfAFormat = false;
			break;
		case PDFPageDevice.PDF_CONFORMANCE_A1A:
			this.pdfConformance = PdfWriter.PDFA1A;
			this.isPdfAFormat = true;
			break;
		case PDFPageDevice.PDF_CONFORMANCE_A1B:
			this.pdfConformance = PdfWriter.PDFA1B;
			this.isPdfAFormat = true;
			break;
		case PDFPageDevice.PDF_CONFORMANCE_A2A:
			this.pdfConformance = PDFPageDevice.PDFA2A;
			this.isPdfAFormat = true;
			break;
		case PDFPageDevice.PDF_CONFORMANCE_A2B:
			this.pdfConformance = PDFPageDevice.PDFA2B;
			this.isPdfAFormat = true;
			break;
		case PDFPageDevice.PDF_CONFORMANCE_A3A:
			this.pdfConformance = PDFPageDevice.PDFA3A;
			this.isPdfAFormat = true;
			break;
		case PDFPageDevice.PDF_CONFORMANCE_A3B:
			this.pdfConformance = PDFPageDevice.PDFA3B;
			this.isPdfAFormat = true;
			break;
		case PDFPageDevice.PDF_CONFORMANCE_A3U:
			this.pdfConformance = PDFPageDevice.PDFA3U;
			this.isPdfAFormat = true;
			break;
		case PDFPageDevice.PDF_CONFORMANCE_A4F:
			this.pdfConformance = PDFPageDevice.PDFA4F;
			this.isPdfAFormat = true;
			break;
		default:
			this.pdfConformance = PdfWriter.PDFXNONE;
			this.isPdfAFormat = false;
			break;
		}
		this.setPdfConformance(this.pdfConformance);

		// PDFA: overwrite to get the document title independent of the openPDF
		// issue of PDF/A-conformance
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDFA_ADD_DOCUMENT_TITLE)) {
			this.addPdfADocumentTitle = Boolean
					.parseBoolean(this.userProperties.get(PDFPageDevice.PDFA_ADD_DOCUMENT_TITLE).toString());
		} else {
			this.addPdfADocumentTitle = Boolean.parseBoolean(
					(String) getReportDesignConfiguration(this.report, PDFPageDevice.PDFA_ADD_DOCUMENT_TITLE));
		}
	}

	/**
	 * Set the PDF conformance
	 *
	 * @param pdfConformance conformance of the PDF document
	 */
	public void setPdfConformance(int pdfConformance) {
		writer.setPDFXConformance(pdfConformance);
		switch (pdfConformance) {
		case PdfWriter.PDFA1A:
		case PDFA2A:
		case PDFA3A:
			writer.setTagged();
			break;
		default:
			// do nothing
		}
	}

	// FIXME The existence of these constants is a workaround for the fact that
	// OpenPDF does not yet support PDF/A-2 or newer.
	private static final int PDFA2A = 5;
	private static final int PDFA2B = 6;
	private static final int PDFA3A = 7;
	private static final int PDFA3B = 8;
	private static final int PDFA3U = 9;
	private static final int PDFA4F = 10;

	/**
	 * Get the PDF conformance
	 *
	 * @return Return the PDF conformance (e.g. PDF.A1A)
	 */
	public String getPdfConformance() {
		switch (this.pdfConformance) {
		case PdfWriter.PDFX32002:
			return PDFPageDevice.PDF_CONFORMANCE_X32002;
		case PdfWriter.PDFA1A:
			return PDFPageDevice.PDF_CONFORMANCE_A1A;
		case PdfWriter.PDFA1B:
			return PDFPageDevice.PDF_CONFORMANCE_A1B;
		case PDFPageDevice.PDFA2A:
			return PDFPageDevice.PDF_CONFORMANCE_A2A;
		case PDFPageDevice.PDFA2B:
			return PDFPageDevice.PDF_CONFORMANCE_A2B;
		case PDFPageDevice.PDFA3A:
			return PDFPageDevice.PDF_CONFORMANCE_A3A;
		case PDFPageDevice.PDFA3B:
			return PDFPageDevice.PDF_CONFORMANCE_A3B;
		case PDFPageDevice.PDFA3U:
			return PDFPageDevice.PDF_CONFORMANCE_A3U;
		case PDFPageDevice.PDFA4F:
			return PDFPageDevice.PDF_CONFORMANCE_A4F;
		default:
			return PDFPageDevice.PDF_CONFORMANCE_STANDARD;
		}
	}

	/**
	 * Check if PDF/A format
	 *
	 * @return Return the check result of PDF/A format
	 */
	public boolean isPdfAFormat() {
		return this.isPdfAFormat;
	}

	/**
	 * Check if PDF/UA conformance is specified
	 *
	 * @return Return if PDF/UA conformance is specified.
	 */
	public boolean isPDFUAFormat() {
		return this.isPdfUAFormat;
	}

	/**
	 * PDF XMP schema for declaring that the PDF is PDF/UA conforming.
	 *
	 * @since 4.18
	 *
	 */
	private static class PDFUASchema extends XmpSchema {

		private static final long serialVersionUID = -6990512370284803429L;

		public PDFUASchema() {
			super("xmlns:pdfuaid=\"http://www.aiim.org/pdfua/ns/id/\"");
		}

		/**
		 * This is what declares the document to be PDF/UA-1, so it must be called.
		 *
		 * @param version the PDF/UA version. Valid values are 1 or 2.
		 */
		public void addPdfUAId(int version) {
			setProperty("pdfuaid:part", String.valueOf(version));
		}

	}

	/**
	 * PDF XMP schema for declaring that the PDF is PDF/A conforming.
	 *
	 * Since the document can be PDF/UA conforming at the same time, the PDF/A
	 * specification requires that this is other schema is also described here.
	 *
	 * @since 4.18
	 *
	 */
	private static class PDFAExtensionSchema extends XmpSchema {

		private static final long serialVersionUID = 6654512771721220538L;

		public PDFAExtensionSchema() {
			super("xmlns:pdfaExtension=\"http://www.aiim.org/pdfa/ns/extension/\" xmlns:pdfaProperty=\"http://www.aiim.org/pdfa/ns/property#\" xmlns:pdfaSchema=\"http://www.aiim.org/pdfa/ns/schema#\"");
		}

		public String toString() {
			return "            <pdfaExtension:schemas>\r\n" + "                <rdf:Bag>\r\n"
					+ "                    <rdf:li rdf:parseType=\"Resource\">\r\n"
					+ "                        <pdfaSchema:namespaceURI>http://www.aiim.org/pdfua/ns/id/</pdfaSchema:namespaceURI>\r\n"
					+ "                        <pdfaSchema:prefix>pdfuaid</pdfaSchema:prefix>\r\n"
					+ "                        <pdfaSchema:schema>PDF/UA identification schema</pdfaSchema:schema>\r\n"
					+ "                        <pdfaSchema:property>\r\n" + "                            <rdf:Seq>\r\n"
					+ "                                <rdf:li rdf:parseType=\"Resource\">\r\n"
					+ "                                    <pdfaProperty:category>internal</pdfaProperty:category>\r\n"
					+ "                                    <pdfaProperty:description>PDF/UA version identifier</pdfaProperty:description>\r\n"
					+ "                                    <pdfaProperty:name>part</pdfaProperty:name>\r\n"
					+ "                                    <pdfaProperty:valueType>Integer</pdfaProperty:valueType>\r\n"
					+ "                                </rdf:li>\r\n"
					+ "                                <rdf:li rdf:parseType=\"Resource\">\r\n"
					+ "                                    <pdfaProperty:category>internal</pdfaProperty:category>\r\n"
					+ "                                    <pdfaProperty:description>PDF/UA amendment identifier</pdfaProperty:description>\r\n"
					+ "                                    <pdfaProperty:name>amd</pdfaProperty:name>\r\n"
					+ "                                    <pdfaProperty:valueType>Text</pdfaProperty:valueType>\r\n"
					+ "\r\n" + "                                </rdf:li>\r\n"
					+ "                                <rdf:li rdf:parseType=\"Resource\">\r\n"
					+ "                                    <pdfaProperty:category>internal</pdfaProperty:category>\r\n"
					+ "                                    <pdfaProperty:description>PDF/UA corrigenda identifier</pdfaProperty:description>\r\n"
					+ "                                    <pdfaProperty:name>corr</pdfaProperty:name>\r\n"
					+ "                                    <pdfaProperty:valueType>Text</pdfaProperty:valueType>\r\n"
					+ "                                </rdf:li>\r\n" + "                            </rdf:Seq>\r\n"
					+ "                        </pdfaSchema:property>\r\n" + "                    </rdf:li>\r\n"
					+ "                </rdf:Bag>\r\n" + "            </pdfaExtension:schemas>\r\n" + "";
		}
	}

	/**
	 * Create the XML for the XMPMetadata.
	 *
	 * We use the same method from {@link PdfWriter} as a template and add what is
	 * needed for PDF/UA.
	 *
	 * @return an XmpMetadata byte array
	 */
	private byte[] createXmpMetadataBytes() {
		PdfDictionary info = writer.getInfo();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			// We could declare the document to be PDF/A conformant.
			// Note: PDF/A is something completely different from PDF/UA.
			// But the same document can be PDF/A conformant and PDF/UA conformant.
			// Not tested.
			int PdfXConformance = writer.getPDFXConformance();
			XmpWriter xmp = new XmpWriter(baos, "UTF-8", 4);
			DublinCoreSchema dc = new DublinCoreSchema();
			PdfSchema p = new PdfSchema();
			XmpBasicSchema basic = new XmpBasicSchema();

			// Use the properties from the PDF info to define some XMPMetadata properties.
			PdfName key;
			PdfObject obj;
			for (PdfName pdfName : info.getKeys()) {
				key = pdfName;
				obj = info.get(key);
				if (obj == null)
					continue;
				if (PdfName.TITLE.equals(key)) {
					// The XMPMetadata allows defining the title for different languages.
					// We add the title in the default language.
					LangAlt langAlt = new LangAlt(((PdfString) obj).toUnicodeString());
					// Example: How to add a translation of the title in a different language.
					// langAlt.addLanguage("de_DE", "Das ist der Titel für deutsche Leser");
					dc.setProperty(DublinCoreSchema.TITLE, langAlt);
				}
				if (PdfName.AUTHOR.equals(key)) {
					dc.addAuthor(((PdfString) obj).toUnicodeString());
				}
				if (PdfName.SUBJECT.equals(key)) {
					dc.addSubject(((PdfString) obj).toUnicodeString());
					dc.addDescription(((PdfString) obj).toUnicodeString());
				}
				if (PdfName.KEYWORDS.equals(key)) {
					p.addKeywords(((PdfString) obj).toUnicodeString());
				}
				if (PdfName.CREATOR.equals(key)) {
					basic.addCreatorTool(((PdfString) obj).toUnicodeString());
				}
				if (PdfName.PRODUCER.equals(key)) {
					p.addProducer(((PdfString) obj).toUnicodeString());
				}
				if (PdfName.CREATIONDATE.equals(key)) {
					basic.addCreateDate(((PdfDate) obj).getW3CDate());
				}
				if (PdfName.MODDATE.equals(key)) {
					basic.addModDate(((PdfDate) obj).getW3CDate());
				}
			}

			if (dc.size() > 0)
				xmp.addRdfDescription(dc);
			if (p.size() > 0)
				xmp.addRdfDescription(p);
			if (basic.size() > 0)
				xmp.addRdfDescription(basic);

			if (this.isPDFUAFormat()) {
				// Declare the document to be PDF/UA conforming.
				PDFUASchema ua = new PDFUASchema();
				ua.addPdfUAId(this.pdfUAConformance);
				xmp.addRdfDescription(ua);
			}

			// Declare the document to be PDF/A conforming, if requested by the developer.
			if (isPdfAFormat) {

				// If the document is also PDF/UA conforming, we need to add a description of
				// that schema as an PDF/A extension.
				if (isPDFUAFormat()) {
					PDFAExtensionSchema pdfaext = new PDFAExtensionSchema();
					xmp.addRdfDescription(pdfaext);
				}

				PdfA1Schema a1 = new PdfA1Schema();
				switch (PdfXConformance) {
				case PdfWriter.PDFA1A:
				case PdfWriter.PDFA1B:
					a1.addPart("1");
					break;
				case PDFA2A:
				case PDFA2B:
					a1.addPart("2");
					break;
				case PDFA3A:
				case PDFA3B:
				case PDFA3U:
					a1.addPart("3");
					break;
				case PDFA4F:
					a1.addPart("4");
					break;
				}
				switch (PdfXConformance) {
				case PdfWriter.PDFA1A:
				case PDFA2A:
				case PDFA3A:
					a1.addConformance("A");
					break;
				case PdfWriter.PDFA1B:
				case PDFA2B:
				case PDFA3B:
					a1.addConformance("B");
					break;
				case PDFA4F:
					a1.addConformance("F");
				}
				xmp.addRdfDescription(a1);
			}

			xmp.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * Set the PDF ICC color profile and the XMP meta data
	 */
	private void setPdfIccXmp() {

		// PDF/A: set ICC color profile and XMP metadata
		try {
			// PDF ICC standard color profile
			PdfDictionary outi = new PdfDictionary(PdfName.OUTPUTINTENT);
			outi.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString(PDFPageDevice.PDF_ICC_PROFILE_DEFAULT));
			outi.put(PdfName.INFO, new PdfString(PDFPageDevice.PDF_ICC_PROFILE_DEFAULT));
			outi.put(PdfName.S, PdfName.GTS_PDFA1);

			// PDF ICC color profile
			boolean iccProfileExternal = false;
			ICC_Profile iccProfile = null;
			File iccFile = null;
			String fullFileNameIcc = "";
			if (this.userProperties != null
					&& userProperties.containsKey(PDFPageDevice.PDF_ICC_PROFILE_EXTERNAL_FILE)) {
				fullFileNameIcc = userProperties.get(PDFPageDevice.PDF_ICC_PROFILE_EXTERNAL_FILE).toString();
			} else {
				fullFileNameIcc = ((String) getReportDesignConfiguration(this.report,
						PDFPageDevice.PDF_ICC_PROFILE_EXTERNAL_FILE));
			}
			if (fullFileNameIcc != null) {
				fullFileNameIcc = fullFileNameIcc.trim();
			}
			if (fullFileNameIcc != null && fullFileNameIcc.length() > 0) {
				try {
					iccFile = new File(fullFileNameIcc);
					if (!iccFile.exists()) {
						// get the file using context.getResource() for relative or universal paths
						URL url = context.getResource(fullFileNameIcc);
						iccFile = new File(url.toURI());
					}
					iccProfileExternal = true;
				} catch (Exception e) {
					logger.log(Level.WARNING, e.getMessage(), e);
				}
			}

			// PDF color RGB / CMYK
			int colorSpace = ColorSpace.CS_sRGB;
			String iccColorType = PDFPageDevice.PDF_ICC_COLOR_RGB;
			if (this.userProperties != null && userProperties.containsKey(PDFPageDevice.PDF_ICC_COLOR_TYPE)) {
				iccColorType = userProperties.get(PDFPageDevice.PDF_ICC_COLOR_TYPE).toString().toUpperCase().trim();
			} else {
				iccColorType = (String) getReportDesignConfiguration(this.report, PDFPageDevice.PDF_ICC_COLOR_TYPE);
			}
			if (iccColorType.equals(PDFPageDevice.PDF_ICC_COLOR_CMYK)) {
				colorSpace = ColorSpace.TYPE_CMYK;
			}
			if (iccProfileExternal) {
				iccProfile = ICC_Profile.getInstance(iccFile.getAbsolutePath());
			} else {
				iccProfile = ICC_Profile.getInstance(colorSpace);
			}
			PdfICCBased iccPdf = new PdfICCBased(iccProfile);
			iccPdf.remove(PdfName.ALTERNATE);
			outi.put(PdfName.DESTOUTPUTPROFILE, writer.addToBody(iccPdf).getIndirectReference());
			writer.getExtraCatalog().put(PdfName.OUTPUTINTENTS, new PdfArray(outi));

		} catch (Exception icce) {
			logger.log(Level.WARNING, icce.getMessage(), icce);
		}

		try {
			// PDF create the XMP metadata based on the document information
			byte[] xmpMetadata = this.createXmpMetadataBytes();
			writer.setXmpMetadata(xmpMetadata);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	/**
	 * Set the default font of PDF/A user property based
	 */
	private void setDefaultFontPdfA() {
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDFA_FALLBACK_FONT)) {
			this.defaultFontPdfA = this.userProperties.get(PDFPageDevice.PDFA_FALLBACK_FONT).toString();
		} else {
			this.defaultFontPdfA = (String) getReportDesignConfiguration(this.report, PDFPageDevice.PDFA_FALLBACK_FONT);
		}
	}

	/**
	 * Set the default font of PDF/A
	 *
	 * @param defaultFont default font of PDF/A
	 */
	public void setDefaultFontPdfA(String defaultFont) {
		this.defaultFontPdfA = defaultFont.replace("\\", "\\\\");
	}

	/**
	 * Get the default font of PDF/A
	 *
	 * @return Return the default font of PDF/A
	 */
	public String getDefaultFontPdfA() {
		return this.defaultFontPdfA;
	}

	/**
	 * Set the including of a font CIDSet stream the document. When set to true, a
	 * CIDSet stream will be included in the document. When set to false, no CIDSet
	 * stream will be included.
	 */
	private void setIncludeCidSet() {
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDF_FONT_CID_SET))
			this.includeFontCidSet = Boolean
					.parseBoolean(this.userProperties.get(PDFPageDevice.PDF_FONT_CID_SET).toString());
		else
			this.includeFontCidSet = Boolean
					.parseBoolean((String) getReportDesignConfiguration(this.report, PDFPageDevice.PDF_FONT_CID_SET));

	}

	/**
	 * SVG background images are use native or converted to raster images
	 */
	private void useBackgroundImageSvgToRasterImage() {
		if (this.userProperties != null
				&& this.userProperties.containsKey(PDFPageDevice.PDF_BACKGROUND_IMAGE_SVG_TO_RASTER))
			this.useBackgroundImageSvg = !Boolean
					.parseBoolean(this.userProperties.get(PDFPageDevice.PDF_BACKGROUND_IMAGE_SVG_TO_RASTER).toString());
	}

	/**
	 * Get the usage of SVG background images
	 *
	 * @return the usage of SVG background images
	 */
	public boolean useBackgroundImageSvg() {
		return this.useBackgroundImageSvg;
	}

	/**
	 * Set the including of a font CIDSet stream the document
	 *
	 * @param includeFontCidSet include CIDSet stream of a font to the document
	 */
	public void setIncludeCidSet(boolean includeFontCidSet) {
		this.includeFontCidSet = includeFontCidSet;
	}

	/**
	 * Get the instruction to include CIDSet stream of fonts
	 *
	 * @return the CIDSet shall be included
	 */
	public boolean isIncludeCidSet() {
		return this.includeFontCidSet;
	}

	/*
	 * Read the configuration from the report design if no user property is set
	 */
	private Object getReportDesignConfiguration(IReportContent reportContent, String name) {
		Object value = null;

		if (name.equalsIgnoreCase(PDFPageDevice.PDF_VERSION)) {
			value = reportContent.getDesign().getReportDesign().getPdfVersion();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDF_CONFORMANCE)) {
			value = reportContent.getDesign().getReportDesign().getPdfConformance();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDF_UA_CONFORMANCE)) {
			value = reportContent.getDesign().getReportDesign().getPdfUAConformance();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDF_ICC_COLOR_TYPE)) {
			value = reportContent.getDesign().getReportDesign().getPdfIccColorType();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDF_ICC_PROFILE_EXTERNAL_FILE)) {
			value = reportContent.getDesign().getReportDesign().getPdfIccColorProfileExternal();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDF_PREPEND_DOCUMENTS)) {
			value = reportContent.getDesign().getReportDesign().getPdfDocumentsPrepend();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDF_APPEND_DOCUMENTS)) {
			value = reportContent.getDesign().getReportDesign().getPdfDocumentsAppend();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDFA_FALLBACK_FONT)) {
			value = reportContent.getDesign().getReportDesign().getPdfAFontFallback();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDF_FONT_CID_SET)) {
			value = reportContent.getDesign().getReportDesign().getPdfFontCidEmbed();

		} else if (name.equalsIgnoreCase(PDFPageDevice.PDFA_ADD_DOCUMENT_TITLE)) {
			value = reportContent.getDesign().getReportDesign().getPdfAEmbedTitle();
		}
		return value;
	}

	/**
	 * Open a tag in the tag tree structure.
	 *
	 * Basically this means: Create a new child node for structureCurrentNode and
	 * let structureCurrentNode point to this child node. But several edge cases
	 * need special handling. For example, when we are in an artifact, we don't want
	 * to open a new tag. And containers need special handling for page-breaking to
	 * avoid the creation of unnecessary tags, e.g. a table that spans two pages
	 * must still a single table in the tag tree.
	 *
	 * If the PDF emitter is not configured to create tagged PDF, then this method
	 * is a no-op.
	 *
	 * @param tagType the tag type. Note that we use some special tag types AUTO,
	 *                PAGE_HEADER and PAGE_FOOTER which are not actually PDF tags,
	 *                but plcaeholders which trigger special handling here.
	 * @param area    the area for which we create a tag.
	 */
	public void openTag(String tagType, IArea area) {
		if (!writer.isTagged() || tagType == null) {
			return;
		}
		PdfDictionary properties = null;
		switch (tagType) {
		case PdfTag.AUTO:
			System.err.println("TODO: auto TagType found for area: " + area);
			break;
		case PdfTag.PAGE_HEADER:
			properties = new PdfDictionary();
			properties.put(PdfNames.TYPE, PdfNames.PAGINATION);
			properties.put(PdfNames.SUBTYPE, PdfNames.HEADER);
			currentPage.beginArtifact(properties);
			break;
		case PdfTag.PAGE_FOOTER:
			properties = new PdfDictionary();
			properties.put(PdfNames.TYPE, PdfNames.PAGINATION);
			properties.put(PdfNames.SUBTYPE, PdfNames.FOOTER);
			currentPage.beginArtifact(properties);
			break;
		default:
			if (area instanceof ContainerArea && ((ContainerArea) area).isArtifact()) {
				properties = new PdfDictionary();
				// FIXME: It is not clear if Pagination or Page is the appropriate type for
				// repeated header rows.
				properties.put(PdfNames.TYPE, PdfNames.PAGINATION);
				currentPage.beginArtifact(properties);
			} else if (currentPage.isInArtifact()) {
				// Do not open a tag inside artifacts.
			} else {
				if (area instanceof ContainerArea) {
					final ContainerArea container = (ContainerArea) area;
					if (container.isFirstPart()) {
						if (PdfTag.TR.equals(tagType)) {
							beforeOpenTableSectionTag(container);
						}
						structureCurrentNode = new PdfStructureElement(structureCurrentNode, new PdfName(tagType));
						try {
							container.setStructureElement(structureCurrentNode);
						} catch (BirtException be) {
							be.printStackTrace();
							structureCurrentNode = new PdfStructureElement(structureCurrentNode, new PdfName(tagType));
						}
					} else {
						structureCurrentNode = container.getFirstPart().getStructureElement();
						PdfName restored = structureCurrentNode.getAsName(PdfName.S);
						if (PdfName.TABLE.equals(restored)) {
							// Also restore the table section, e.g. TBody.
							PdfArray children = structureCurrentNode.getAsArray(PdfName.K); // K means "kids" in this
																							// context
							if (children != null && children.size() > 0) {
								structureCurrentNode = (PdfStructureElement) children.getAsDict(children.size() - 1);
							}
						}
					}
				} else {
					structureCurrentNode = new PdfStructureElement(structureCurrentNode, new PdfName(tagType));
				}
				if (PdfTag.FIGURE.equals(tagType)) {
					addFigureAttributes();
				}
				if (PdfTag.TD.equals(tagType) || PdfTag.TH.equals(tagType)) {
					addTableCellAttributes(tagType, area);
				}
			}
		}
	}

	/**
	 * Open a tag for the table section THead, TBody, TFoot when necessary.
	 *
	 * When opening a row, we want to open e.g. a TBody tag if it is the first row
	 * of the table body. If we are still in the wrong section, we have to close
	 * that section tag first before opening the new section tag.
	 *
	 * @param row the RowArea.
	 */
	private void beforeOpenTableSectionTag(final ContainerArea row) {
		PdfName currentTag = structureCurrentNode.getAsName(PdfName.S);
		RowContent rowContent = (RowContent) row.getContent();
		PdfName inject = null;
		boolean closeSection = false;
		switch (rowContent.getBand().getBandType()) {
		case IBandContent.BAND_HEADER:
			// THead
			if (!currentTag.equals(PdfName.THEAD)) {
				inject = PdfName.THEAD;
			}
			break;
		case IBandContent.BAND_FOOTER:
			// TFoot
			if (!currentTag.equals(PdfName.TFOOT)) {
				inject = PdfName.TFOOT;
				closeSection = !currentTag.equals(PdfName.TABLE);
			}
			break;
		default:
			// TBody
			if (!currentTag.equals(PdfName.TBODY)) {
				inject = PdfName.TBODY;
				closeSection = !currentTag.equals(PdfName.TABLE);
			}
		}
		if (closeSection) {
			structureCurrentNode = (PdfStructureElement) structureCurrentNode.getParent();
		}
		if (inject != null) {
			structureCurrentNode = new PdfStructureElement(structureCurrentNode, inject);
		}
	}

	/**
	 * Add attributes for a table cell.
	 *
	 * They are necessary to connect TD cells with their corresponding TH cells.
	 */
	private void addTableCellAttributes(String tagType, IArea area) {
		if (area instanceof CellArea) {
			CellArea cellArea = (CellArea) area;
			int rowspan = cellArea.getRowSpan();
			int colspan = cellArea.getColSpan();
			String scope = ((CellContent) (cellArea.getContent())).getScope();
			String bookmark = cellArea.getBookmark();
			if (bookmark != null) {
				structureCurrentNode.put(PdfName.ID, new PdfString(bookmark));
			}

			String headers = ((CellContent) (cellArea.getContent())).getHeaders();
			if (rowspan != 1 || colspan != 1 || scope != null || headers != null) {
				PdfDictionary attributes = structureCurrentNode.getAsDict(PdfName.A);
				if (attributes == null) {
					attributes = new PdfDictionary();
					attributes.put(PdfName.O, PdfName.TABLE);
					structureCurrentNode.put(PdfName.A, attributes);
				}
				if (rowspan != 1) {
					attributes.put(PdfNames.ROWSPAN, new PdfNumber(rowspan));
				}
				if (colspan != 1) {
					attributes.put(PdfNames.COLSPAN, new PdfNumber(colspan));
				}
				if (scope != null && PdfTag.TH.equals(tagType)) {
					attributes.put(PdfNames.SCOPE, pdfScope((scope)));
				}
				if (headers != null) {
					attributes.put(PdfNames.HEADERS, commaSeparatedToPdfByteStringArray((headers)));
				}
			}
		}
	}

	/**
	 * Add necessary attributes for figure tags.
	 *
	 * Top-Level figure elements must have a placement attribute.
	 */
	private void addFigureAttributes() {
		if (PdfName.DOCUMENT.equals(structureCurrentNode.getParent().get(PdfName.S))) {
			PdfDictionary attributes = structureCurrentNode.getAsDict(PdfName.A);
			if (attributes == null) {
				attributes = new PdfDictionary();
				structureCurrentNode.put(PdfName.A, attributes);
			}
			attributes.put(PdfNames.PLACEMENT, PdfNames.BLOCK);
			attributes.put(PdfName.O, PdfNames.LAYOUT);
		}
	}

	/**
	 * Split a comma-separated string into a PDF bytestring array. Note that blanks
	 * are not stripped and empty values are allowed.
	 *
	 * @param cav
	 * @return A PDF bytestring array
	 */
	private PdfArray commaSeparatedToPdfByteStringArray(String csv) {
		String[] arr = csv.split(",");
		PdfArray pdfArr = new PdfArray();
		for (String s : arr)
			pdfArr.add(new PdfString(s));
		return pdfArr;
	}

	/**
	 * @param scope scope as set in the rptdesign file.
	 * @return scope as needed for PDF "/Table" attribute "/Scope".
	 */
	private PdfName pdfScope(String scope) {
		if (scope == null) {
			return null;
		}
		if ("col".equals(scope)) {
			return PdfNames.COLUMN;
		}
		if ("row".equals(scope)) {
			return PdfNames.ROW;
		}
		logger.warning("Unsupported scope: " + scope);
		return null;
	}

	/**
	 * This is the opposite of openTag.
	 *
	 * Every tag that has been opened must be closed exactly once.
	 *
	 *
	 * If the PDF emitter is not configured to create tagged PDF, then this method
	 * is a no-op.
	 *
	 * @param tagType must be the same as in the call to openTag.
	 * @param area    must be the same as in the call to openTag.
	 */
	public void closeTag(String tagType, IArea area) {
		if (!writer.isTagged() || tagType == null) {
			return;
		}
		if ("pageHeader".equals(tagType)) {
			currentPage.endArtifact();
		} else if ("pageFooter".equals(tagType)) {
			currentPage.endArtifact();
		} else if (area instanceof ContainerArea && ((ContainerArea) area).isArtifact()) {
			currentPage.endArtifact();
		} else if (currentPage.isInArtifact()) {
			// do nothing
		} else {
			if (PdfTag.TABLE.equals(tagType)) {
				PdfName currentTag = structureCurrentNode.getAsName(PdfName.S);
				if (!currentTag.equals(PdfNames.TR)) {
					// Close the THead/TBody/TFoot tag also
					structureCurrentNode = (PdfStructureElement) structureCurrentNode.getParent();
				}
			}
			structureCurrentNode = (PdfStructureElement) structureCurrentNode.getParent();
		}
	}

	/**
	 * @return Is the writer is expected to create tagged PDF or not?
	 */
	public boolean isTagged() {
		return writer.isTagged();
	}
}
