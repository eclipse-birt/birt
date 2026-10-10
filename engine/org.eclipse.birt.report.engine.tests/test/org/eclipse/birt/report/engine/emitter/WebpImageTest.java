/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.openpdf.text.pdf.PRIndirectReference;
import org.openpdf.text.pdf.PdfDictionary;
import org.openpdf.text.pdf.PdfName;
import org.openpdf.text.pdf.PdfObject;
import org.openpdf.text.pdf.PdfReader;
import org.openpdf.text.pdf.PdfStream;

/**
 * Renders a design with an embedded WebP image and a WebP grid background to
 * output formats that cannot hold WebP, which get the images as PNG.
 */
public class WebpImageTest extends EngineCase {

	private static final String DESIGN = "org/eclipse/birt/report/engine/emitter/webp.rptdesign"; //$NON-NLS-1$

	private byte[] renderAs(String format) throws EngineException, IOException {
		IRunAndRenderTask task = createRunAndRenderTask(DESIGN);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			RenderOption options = new RenderOption();
			options.setOutputStream(out);
			options.setOutputFormat(format);
			task.setRenderOption(options);
			task.run();
			assertTrue(task.getErrors().toString(), task.getErrors().isEmpty());
			return out.toByteArray();
		} finally {
			task.close();
		}
	}

	public void testPdf() throws Exception {
		PdfReader reader = new PdfReader(renderAs("pdf")); //$NON-NLS-1$
		try {
			Set<Integer> images = new HashSet<>();
			for (int page = 1; page <= reader.getNumberOfPages(); page++) {
				collectImages(reader.getPageN(page).getAsDict(PdfName.RESOURCES), images);
			}
			// the image item and the grid background
			assertEquals(2, images.size());
		} finally {
			reader.close();
		}
	}

	private static void collectImages(PdfDictionary resources, Set<Integer> images) {
		PdfDictionary xobjects = resources == null ? null : resources.getAsDict(PdfName.XOBJECT);
		if (xobjects == null) {
			return;
		}
		for (PdfName name : xobjects.getKeys()) {
			PdfObject reference = xobjects.get(name);
			PdfObject object = PdfReader.getPdfObject(reference);
			if (!(object instanceof PdfStream)) {
				continue;
			}
			PdfStream stream = (PdfStream) object;
			if (PdfName.IMAGE.equals(stream.getAsName(PdfName.SUBTYPE))) {
				images.add(((PRIndirectReference) reference).getNumber());
			} else if (PdfName.FORM.equals(stream.getAsName(PdfName.SUBTYPE))) {
				collectImages(stream.getAsDict(PdfName.RESOURCES), images);
			}
		}
	}

	public void testDocx() throws Exception {
		List<String> media = new ArrayList<>();
		try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(renderAs("docx")))) { //$NON-NLS-1$
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				if (entry.getName().contains("media/")) { //$NON-NLS-1$
					media.add(entry.getName());
				}
			}
		}
		assertFalse(media.isEmpty());
		for (String name : media) {
			assertTrue(name, name.toLowerCase().endsWith(".png")); //$NON-NLS-1$
		}
	}
}
