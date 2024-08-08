/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.device.render;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import junit.framework.TestCase;
import utility.ImageUtil;
import utility.ImageUtil.ImageCompParam;

/**
 * BaseTest is a JUnit test case that will generate image files based on a set
 * of primitive drawing events defined in drawing script files, and then compare
 * the generated files with the expected files.
 */
public class ImageOutputBaseTest extends TestCase {

	protected File file;
	protected String dirName;
	protected String filename;
	protected String workspaceDir;

	/**
	 * @param file
	 * @param dirName
	 * @param filename
	 * @param workspaceDir
	 */
	public ImageOutputBaseTest(File file, String dirName, String filename, String workspaceDir) {
		super(filename);
		this.file = file;
		this.dirName = dirName;
		this.filename = filename;
		this.workspaceDir = workspaceDir;
	}

	@Override
	public void runTest() throws Throwable {

		if (!ImageUtil.isRenderingTestApplicable()) {
			return;
		}

		Map<ImageCompParam, Integer> params = new HashMap<>();
		params.put(ImageCompParam.TOLERANCE, 4);

		// 1: verify PNG
		Png24PrimitiveGen generator2 = new Png24PrimitiveGen(
				new FileInputStream(workspaceDir + File.separator + ImageRenderTest.INDIR + dirName + File.separator
						+ filename + ImageRenderTest.DRAWEXT),
				workspaceDir + File.separator + ImageRenderTest.OUTDIR + dirName + File.separator + filename + ".png");//$NON-NLS-1$
		generator2.generate();
		generator2.flush();
		String control = workspaceDir + File.separator + ImageRenderTest.CONTROLDIR + dirName + File.separator
				+ filename + ".png";
		Image result = ImageUtil.compare(control, // $NON-NLS-1$
				workspaceDir + File.separator + ImageRenderTest.OUTDIR + dirName + File.separator + filename + ".png",
				params); // $NON-NLS-1$
		if (result != null) {
			ImageUtil.savePNG(result, control);
			fail();
		}

		// 2: verify SVG
		SvgPrimitiveGen generator3 = new SvgPrimitiveGen(
				new FileInputStream(workspaceDir + File.separator + ImageRenderTest.INDIR + dirName + File.separator
						+ filename + ImageRenderTest.DRAWEXT),
				workspaceDir + File.separator + ImageRenderTest.OUTDIR + dirName + File.separator + filename + ".svg"); //$NON-NLS-1$
		generator3.generate();
		generator3.flush();

		// convert golden and actual SVG to PNG
		String[] files = {
				workspaceDir + File.separator + ImageRenderTest.CONTROLDIR + dirName + File.separator + filename
						+ ".svg", //$NON-NLS-1$
				workspaceDir + File.separator + ImageRenderTest.OUTDIR + dirName + File.separator + filename + ".svg" //$NON-NLS-1$
		};

		for (String file : files) {
			InputStream inStream = new FileInputStream(file);
			TranscoderInput input = new TranscoderInput(inStream);
			OutputStream outStream = new FileOutputStream(file + ".png"); //$NON-NLS-1$
			TranscoderOutput output = new TranscoderOutput(outStream);
			PNGTranscoder converter = new PNGTranscoder();
			converter.transcode(input, output);
			outStream.flush();
			outStream.close();
		}
		result = ImageUtil.compare(files[0] + ".png", //$NON-NLS-1$
				files[1] + ".png"); //$NON-NLS-1$
		if (result != null) {
			ImageUtil.savePNG(result, files[0] + ".png"); //$NON-NLS-1$
			fail();
		}

//		PdfPrimitiveGen generator4 = new PdfPrimitiveGen(new FileInputStream(workspaceDir+File.separator+ImageRenderTest.INDIR+dirName+File.separator+filename+ImageRenderTest.DRAWEXT), workspaceDir+File.separator+ImageRenderTest.OUTDIR+dirName+File.separator+filename+".pdf");//$NON-NLS-1$
//		generator4.generate();
//		generator4.flush();
//		assertTrue(FileUtil.compareFiles(new FileInputStream(workspaceDir+File.separator+ImageRenderTest.OUTDIR+dirName+File.separator+filename+".pdf"), new FileInputStream(workspaceDir+File.separator+ImageRenderTest.CONTROLDIR+dirName+File.separator+filename+".pdf")));//$NON-NLS-1$//$NON-NLS-2$
	}
}
