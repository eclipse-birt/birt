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

package org.eclipse.birt.report.designer.core.commands;

import java.io.UnsupportedEncodingException;

import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

/**
 * @author xzhang
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PasteStructureCommandTest extends CmdBaseTestCase {

	private static final String testImageName = "Test Image";

	private EmbeddedImage embeddedImg = null;

	protected void tearDown() throws SemanticException {
		super.tearDown();
		embeddedImg = null;
	}

	private void createEmbeddedImage() {
		embeddedImg = new EmbeddedImage(testImageName);

		try {
			embeddedImg.setData("data".getBytes(EmbeddedImage.CHARSET));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("embeddedImg.setData failed");
		}
	}

	public void testPasteStructure() {

		EmbeddedImageNode embeddedImgNode = new EmbeddedImageNode(getReportDesignHandle());
		createEmbeddedImage();

		PasteStructureCommand pasteStrucCmd = new PasteStructureCommand((IStructure) embeddedImg, embeddedImgNode);
		assertTrue(pasteStrucCmd.canExecute());
		pasteStrucCmd.execute();

		EmbeddedImage find = getReportDesignHandle().findImage(testImageName);
		assertNotNull(find);

	}

	public void testPasteStructure2() {

		createEmbeddedImage();

		PasteStructureCommand pasteStrucCmd = new PasteStructureCommand((IStructure) embeddedImg,
				getReportDesignHandle());
		assertTrue(pasteStrucCmd.canExecute());
		pasteStrucCmd.execute();

		EmbeddedImage find = getReportDesignHandle().findImage(testImageName);
		assertNotNull(find);
	}

}
