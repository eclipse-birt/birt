/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.util.graphics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

/**
 * ImageLoader to load a give image file into embedded image.
 */

public class BirtImageLoader {

	private static final String IMG_PREFIX = "image/"; //$NON-NLS-1$
	private static final String ICO = "ico"; //$NON-NLS-1$
	private static final String MIME_ICO = "x-icon"; //$NON-NLS-1$
	private static final String SVG = "svg"; //$NON-NLS-1$
	private static final String MIME_SVG = "svg+xml"; //$NON-NLS-1$

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getStructure(EmbeddedImage.EMBEDDED_IMAGE_STRUCT).getMember(EmbeddedImage.TYPE_MEMBER).getAllowedChoices();

	/**
	 * load file into byte array with given file name.
	 *
	 * @param fileName
	 * @return byte array data of image file.
	 * @throws IOException
	 */
	public byte[] load(String fileName) throws IOException {
		FileInputStream file = null;
		byte data[] = null;
		try {
			file = new FileInputStream(fileName);

			if (file != null) {
				try {
					data = new byte[file.available()];
					file.read(data);
				} catch (IOException e1) {
					throw e1;
				}
			}
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (file != null) {
				file.close();
			}
		}

		return data;

	}

	/**
	 * Loads given image file into given Design file.
	 *
	 * @param handle   design file instance handle
	 * @param fileName file name of image
	 * @return
	 * @throws SemanticException
	 * @throws IOException
	 */
	public EmbeddedImage save(ModuleHandle handle, String fileName) throws SemanticException, IOException {
		return save(handle, fileName, fileName);
	}

	/**
	 * Loads given image file into given Design file.
	 *
	 * @param handle    design file instance handle
	 * @param fileName  file name of image
	 * @param imageName name of embedded image
	 *
	 * @return
	 * @throws SemanticException
	 * @throws IOException
	 */
	public EmbeddedImage save(ModuleHandle handle, String fileName, String imageName)
			throws SemanticException, IOException {
		EmbeddedImage embeddedImage = StructureFactory.createEmbeddedImage();
		embeddedImage.setType(getModelImageType(imageName));
		embeddedImage.setName(imageName);
		embeddedImage.setData(load(fileName));
		handle.addImage(embeddedImage);

		return embeddedImage;
	}

	private String getModelImageType(String imageName) {
		if (imageName.lastIndexOf(".") > -1) //$NON-NLS-1$
		{
			String suffix = imageName.substring(imageName.lastIndexOf(".") + 1) //$NON-NLS-1$
					.toLowerCase();
			String type = IMG_PREFIX + suffix;
			if (SVG.equals(suffix)) {
				type = IMG_PREFIX + MIME_SVG;
			} else if (ICO.equals(suffix)) {
				type = IMG_PREFIX + MIME_ICO;
			}
			for (IChoice choice : DATA_TYPE_CHOICE_SET.getChoices()) {
				if (choice.getValue().equals(type)) {
					return choice.getValue().toString();
				}
			}
		}
		return null;
	}
}
