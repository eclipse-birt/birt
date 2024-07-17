/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.utility;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterInfo;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterMakeAndModel;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;
import jakarta.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.exception.ViewerException;

/**
 * Utilities for Print Report Service
 *
 */
public class PrintUtility {

	/**
	 * URL parameter name that specifies the printer name.
	 */
	public static final String PARAM_PRINTER = "__printer"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies the printer copies.
	 */
	public static final String PARAM_PRINTER_COPIES = "__printer_copies"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies the printer collate.
	 */
	public static final String PARAM_PRINTER_COLLATE = "__printer_collate"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies the printer duplex.
	 */
	public static final String PARAM_PRINTER_DUPLEX = "__printer_duplex"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies the printer mode.
	 */
	public static final String PARAM_PRINTER_MODE = "__printer_mode"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies the printer media size.
	 */
	public static final String PARAM_PRINTER_MEDIASIZE = "__printer_pagesize"; //$NON-NLS-1$

	/**
	 * Execuate Print Job
	 *
	 * @param inputStream
	 * @param printer
	 * @throws ViewerException
	 */
	public static void execPrint(InputStream inputStream, Printer printer) throws RemoteException {
		if (inputStream == null || printer == null) {
			return;
		}

		// Create print request attribute set
		PrintRequestAttributeSet pas = new HashPrintRequestAttributeSet();

		// Copies
		if (printer.isCopiesSupported()) {
			pas.add(new Copies(printer.getCopies()));
		}

		// Collate
		if (printer.isCollateSupported()) {
			pas.add(printer.isCollate() ? SheetCollate.COLLATED : SheetCollate.UNCOLLATED);
		}

		// Duplex
		if (printer.isDuplexSupported()) {
			switch (printer.getDuplex()) {
			case Printer.DUPLEX_SIMPLEX:
				pas.add(Sides.ONE_SIDED);
				break;
			case Printer.DUPLEX_HORIZONTAL:
				pas.add(Sides.DUPLEX);
				break;
			case Printer.DUPLEX_VERTICAL:
				pas.add(Sides.TUMBLE);
				break;
			default:
				pas.add(Sides.ONE_SIDED);
			}
		}

		// Mode
		if (printer.isModeSupported()) {
			switch (printer.getMode()) {
			case Printer.MODE_MONOCHROME:
				pas.add(Chromaticity.MONOCHROME);
				break;
			case Printer.MODE_COLOR:
				pas.add(Chromaticity.COLOR);
				break;
			default:
				pas.add(Chromaticity.MONOCHROME);
			}
		}

		// Media
		if (printer.isMediaSupported() && printer.getMediaSize() != null) {
			MediaSizeName mediaSizeName = (MediaSizeName) printer.getMediaSizeNames().get(printer.getMediaSize());
			if (mediaSizeName != null) {
				pas.add(mediaSizeName);
			}
		}

		try {
			PrintService service = printer.getService();
			synchronized (service) {
				DocPrintJob job = service.createPrintJob();
				Doc doc = new SimpleDoc(inputStream, DocFlavor.INPUT_STREAM.POSTSCRIPT, null);
				job.print(doc, pas);
			}
		} catch (PrintException e) {
			AxisFault fault = new AxisFault(e.getLocalizedMessage(), e);
			fault.setFaultCode(new QName("PrintUtility.execPrint( )")); //$NON-NLS-1$
			fault.setFaultString(e.getLocalizedMessage());
			throw fault;
		}
	}

	/**
	 * Get printer settings from http request
	 *
	 * @param request
	 * @return
	 */
	public static Printer getPrinter(HttpServletRequest request) {
		String printerName = ParameterAccessor.getParameter(request, PARAM_PRINTER);
		Printer printer = null;
		if (printerName != null) {
			List printers = findPrinters();
			for (int i = 0; i < printers.size(); i++) {
				if (printerName.equalsIgnoreCase(((Printer) printers.get(i)).getName())) {
					printer = (Printer) printers.get(i);
					break;
				}
			}
		}

		if (printer == null) {
			// From default printer
			PrintService service = PrintServiceLookup.lookupDefaultPrintService();
			printer = createPrinter(service);
		}

		if (printer != null) {
			String copies = ParameterAccessor.getParameter(request, PARAM_PRINTER_COPIES);
			if (copies != null) {
				printer.setCopies(Integer.parseInt(copies));
			}

			String collate = ParameterAccessor.getParameter(request, PARAM_PRINTER_COLLATE);
			if (collate != null) {
				printer.setCollate(Boolean.parseBoolean(collate));
			}

			String duplex = ParameterAccessor.getParameter(request, PARAM_PRINTER_DUPLEX);
			if (duplex != null) {
				printer.setDuplex(Integer.parseInt(duplex));
			}

			String mode = ParameterAccessor.getParameter(request, PARAM_PRINTER_MODE);
			if (mode != null) {
				printer.setMode(Integer.parseInt(mode));
			}

			String mediaSize = ParameterAccessor.getParameter(request, PARAM_PRINTER_MEDIASIZE);
			mediaSize = ParameterAccessor.htmlDecode(mediaSize);
			if (mediaSize != null) {
				printer.setMediaSize(mediaSize);
			}
		}

		return printer;
	}

	/**
	 * Find all the printer resources on the server
	 *
	 * @return
	 */
	public static List findPrinters() {
		List printers = new ArrayList();

		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
		if (printServices != null) {
			for (int i = 0; i < printServices.length; i++) {
				PrintService service = printServices[i];
				printers.add(createPrinter(service));
			}
		}

		return printers;
	}

	/**
	 * Create Printer bean object from print service
	 *
	 * @param service
	 * @return
	 */
	public static Printer createPrinter(PrintService service) {
		if (service == null) {
			return null;
		}

		Printer printer = new Printer();
		printer.setName(service.getName());

		// Model attribute
		PrintServiceAttribute attr = service.getAttribute(PrinterMakeAndModel.class);
		if (attr != null) {
			printer.setModel(attr.toString());
		}

		// Status attribute
		attr = service.getAttribute(PrinterIsAcceptingJobs.class);
		if (attr == PrinterIsAcceptingJobs.ACCEPTING_JOBS) {
			printer.setStatus(Printer.STATUS_ACCEPTING_JOBS);
		} else {
			printer.setStatus(Printer.STATUS_NOT_ACCEPTING_JOBS);
		}

		// Info attribute
		attr = service.getAttribute(PrinterInfo.class);
		if (attr != null) {
			printer.setInfo(attr.toString());
		}

		// Copies attribute
		printer.setCopiesSupported(service.isAttributeCategorySupported(Copies.class));
		int copies = 0;
		Copies copiesObj = (Copies) service.getDefaultAttributeValue(Copies.class);
		if (copiesObj != null) {
			copies = Integer.parseInt(copiesObj.toString());
		}
		if (copies <= 0) {
			copies = 1;
		}

		printer.setCopies(copies);

		// Collate attribute
		boolean collateSupported = service.isAttributeCategorySupported(SheetCollate.class);
		printer.setCollateSupported(collateSupported);
		if (collateSupported) {
			SheetCollate collate = (SheetCollate) service.getDefaultAttributeValue(SheetCollate.class);
			if (collate == null) {
				collate = SheetCollate.UNCOLLATED;
			}

			if (collate == SheetCollate.COLLATED) {
				printer.setCollate(true);
			} else {
				printer.setCollate(false);
			}
		}

		// Mode attribute
		boolean modeSupported = service.isAttributeCategorySupported(Chromaticity.class);
		printer.setModeSupported(modeSupported);
		if (modeSupported) {
			Chromaticity chromaticity = (Chromaticity) service.getDefaultAttributeValue(Chromaticity.class);
			if (chromaticity == null) {
				chromaticity = Chromaticity.MONOCHROME;
			}

			if (chromaticity == Chromaticity.MONOCHROME) {
				printer.setMode(Printer.MODE_MONOCHROME);
			} else {
				printer.setMode(Printer.MODE_COLOR);
			}
		}

		// Duplex attribute
		boolean duplexSupported = service.isAttributeCategorySupported(Sides.class);
		printer.setDuplexSupported(duplexSupported);
		if (duplexSupported) {
			Sides sides = (Sides) service.getDefaultAttributeValue(Sides.class);
			if (sides == null) {
				sides = Sides.ONE_SIDED;
			}

			if (sides == Sides.ONE_SIDED) {
				printer.setDuplex(Printer.DUPLEX_SIMPLEX);
			} else if (sides == Sides.TUMBLE) {
				printer.setDuplex(Printer.DUPLEX_VERTICAL);
			} else {
				printer.setDuplex(Printer.DUPLEX_HORIZONTAL);
			}
		}

		// Media attribute
		boolean mediaSupported = service.isAttributeCategorySupported(Media.class);
		printer.setMediaSupported(mediaSupported);
		if (mediaSupported) {
			Object obj = service.getSupportedAttributeValues(Media.class, null, null);
			if (obj instanceof Media[]) {
				Media[] medias = (Media[]) obj;

				for (int j = 0; j < medias.length; j++) {
					if (medias[j] instanceof MediaSizeName) {
						printer.addMediaSizeName(medias[j].toString(), medias[j]);
					}
				}
			}

			Media media = (Media) service.getDefaultAttributeValue(Media.class);
			if (media != null) {
				if (media instanceof MediaSizeName) {
					printer.setMediaSize(media.toString());
				}
			}
		}

		printer.setService(service);

		return printer;
	}

	/**
	 * Handle back slash issue for printer name
	 *
	 * @param str
	 * @return
	 */
	public static String handleSlash(String str) {
		if (str == null) {
			return null;
		}

		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\\') {
				buf.append('\\');
				buf.append('\\');
			} else {
				buf.append(str.charAt(i));
			}
		}

		return buf.toString();
	}
}
