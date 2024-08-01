/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import java.io.File;
import java.io.FileInputStream;
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
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.util.BundleVersionUtil;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;

import com.ibm.icu.util.ULocale;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfICCBased;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

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

	/** PDFX/PDFA conformance */
	/** PDF conformance PDF Standard */
	private static final String PDF_CONFORMANCE_STANDARD = "PDF.Standard";
	/** PDF conformance PDF X-3:2002 */
	private static final String PDF_CONFORMANCE_X32002 = "PDF.X32002";
	/** PDF conformance PDF A1A */
	private static final String PDF_CONFORMANCE_A1A = "PDF.A1A";
	/** PDF conformance PDF A1B */
	private static final String PDF_CONFORMANCE_A1B = "PDF.A1B";
	/** PDF conformance PDF X-1a:2001, unsupported (TODO: CMYK of PDF.X1A2001 */

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

	protected Map<String, Expression> userProperties;

	private char pdfVersion = '0';

	private int pdfConformance = PdfWriter.PDFXNONE;

	private boolean isPdfAFormat = false;

	private boolean addPdfADocumentTitle = false;

	private String defaultFontPdfA = null;

	private boolean includeFontCidSet = true;

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
			// PDF/A, set the default font of not embeddable fonts
			this.setDefaultFontPdfA();
			// PDF include font CID set stream
			this.setIncludeCidSet();

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

			// Add in prepending PDF's
			// modified here. This will grab a global variable called
			// appendPDF, and take a list of strings of PDF files to
			// append to the end.
			// this is where we will test the merge
			List<InputStream> pdfs = new ArrayList<>();

			// added null check
			if (userProperties != null) {
				Object listObject = userProperties.get(PDFPageDevice.PREPEND_PROPERTY_NAME);
				if (userProperties.containsKey(PDFPageDevice.PDF_PREPEND_DOCUMENTS)) {
					listObject = userProperties.get(PDFPageDevice.PDF_PREPEND_DOCUMENTS);
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
								// iterate over the list, and create a fileinputstream for each file location.
								for (String s : list.split(",")) {
									// If there is an exception creating the input stream, don't stop execution.
									// Just graceffully let the user know that there was an error with the variable.
									try {
										String fileName = s.trim();

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
					// the result
					// of an expression instead of a string literal. This should be set as an
					// arraylist through
					// BIRT script
					if (result instanceof ArrayList) {
						ArrayList<String> pdfList = (ArrayList<String>) result;

						for (String fileName : pdfList) {
							// If there is an exception creating the input stream, don't stop execution.
							// Just graceffully let the user know that there was an error with the variable.
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
			}
			// End Modification
		} catch (DocumentException | BirtException be) {
			logger.log(Level.SEVERE, be.getMessage(), be);
		}
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
	 * @param key               the inex key of the pdf template
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

		// added null check
		if (userProperties != null) {
			Object listObject = userProperties.get(PDFPageDevice.APPEND_PROPERTY_NAME);
			if (userProperties.containsKey(PDFPageDevice.PDF_APPEND_DOCUMENTS)) {
				listObject = userProperties.get(PDFPageDevice.PDF_APPEND_DOCUMENTS);
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
								// Just graceffully let the user know that there was an error with the variable.
								try {
									String fileName = s.trim();

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
				// arraylist through
				// BIRT script
				if (result instanceof ArrayList) {
					ArrayList<String> pdfList = (ArrayList<String>) result;

					for (String fileName : pdfList) {
						// If there is an exception creating the input stream, don't stop execution.
						// Just graceffully let the user know that there was an error with the variable.
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
		}

		if (this.isPdfAFormat) {
			// PDF/A: set color profile and metadata
			this.setPdfIccXmp();
		}

		writer.setPageEmpty(false);
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
	 */
	private void setPdfVersion() {
		// PDF version based on user property
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDF_VERSION)) {
			String userPdfVersion = this.userProperties.get(PDFPageDevice.PDF_VERSION).toString();
			this.setPdfVersion(userPdfVersion);
		}
	}

	/**
	 * Set the PDF version
	 *
	 * @param version key to set the PDF version (e.g. 1.7)
	 */
	public void setPdfVersion(String version) {
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
		}
		// version only set if the PDF version exists
		if (this.pdfVersion != '0') {
			writer.setAtLeastPdfVersion(this.pdfVersion);
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
		// PDFA & PDFX conformance, based on user property
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDF_CONFORMANCE)) {
			String userPdfConformance = this.userProperties.get(PDFPageDevice.PDF_CONFORMANCE).toString().toUpperCase();
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
			default:
				this.pdfConformance = PdfWriter.PDFXNONE;
				this.isPdfAFormat = false;
				break;
			}
			this.setPdfConformance(this.pdfConformance);
		}

		// PDFA: overwrite to get the document title independent of the openPDF
		// issue of PDF/A-conformance
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDFA_ADD_DOCUMENT_TITLE)) {
			String pdfaUseTitleOverwrite = this.userProperties.get(PDFPageDevice.PDFA_ADD_DOCUMENT_TITLE).toString()
					.toLowerCase();
			if (pdfaUseTitleOverwrite.equals("true")) {
				this.addPdfADocumentTitle = true;
			}
		}
	}

	/**
	 * Set the PDF conformance
	 *
	 * @param pdfConformance conformance of the PDF document
	 */
	public void setPdfConformance(int pdfConformance) {
		writer.setPDFXConformance(pdfConformance);
		writer.setTagged();
	}

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
	 * Set the PDF icc color profile and the XMP meta data
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
				fullFileNameIcc = userProperties.get(PDFPageDevice.PDF_ICC_PROFILE_EXTERNAL_FILE).toString().trim();
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
				if (iccColorType.equals(PDFPageDevice.PDF_ICC_COLOR_CMYK)) {
					colorSpace = ColorSpace.TYPE_CMYK;
				}
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
			// PDF create the xmp metaddata based on the document information
			writer.createXmpMetadata();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	/**
	 * Set the default font of PDF/A user property based
	 */
	private void setDefaultFontPdfA() {
		if (this.userProperties != null && this.userProperties.containsKey(PDFPageDevice.PDFA_FALLBACK_FONT)) {
			String defaultFont = this.userProperties.get(PDFPageDevice.PDFA_FALLBACK_FONT).toString();
			this.defaultFontPdfA = defaultFont;
		}
	}

	/**
	 * Set the default font of PDF/A
	 *
	 * @param defaultFont default font of PDF/A
	 */
	public void setDefaultFontPdfA(String defaultFont) {
		this.defaultFontPdfA = defaultFont;
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
}
