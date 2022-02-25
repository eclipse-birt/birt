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

package org.eclipse.birt.report.model.api;

import java.io.UnsupportedEncodingException;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * Represents the handle of an embedded image. The class gives the name and type
 * of the image. Used when an image element {@link ImageItem}gives a name. Each
 * embedded image has the following properties:
 *
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>an embedded image has a unique and required name, so the image item can
 * use the image name to identify an embedded image.</dd>
 *
 * <dt><strong>Type </strong></dt>
 * <dd>an embedded image has a choice and required type: bmp, gif, png or x-png.
 * </dd>
 *
 * <dt><strong>Data </strong></dt>
 * <dd>value of the image data in Base64 encoding.</dd>
 * </dl>
 *
 */

public class EmbeddedImageHandle extends StructureHandle {

	/**
	 * Constructs the handle of embedded image.
	 *
	 * @param valueHandle the value handle for embedded image list of one property
	 * @param index       the position of this embedded image in the list
	 */

	public EmbeddedImageHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the byte array of image data.
	 *
	 * @return the byte array of image data
	 */

	public byte[] getData() {
		EmbeddedImage image = (EmbeddedImage) getStructure();
		return image.getData(getModule());
	}

	/**
	 * Sets the byte array of image data.
	 *
	 * @param data the byte array to set
	 */

	public void setData(byte[] data) {
		String toSet = null;

		try {
			if (data != null) {
				toSet = new String(data, EmbeddedImage.CHARSET);
			}
		} catch (UnsupportedEncodingException e) {
			// Should not fail

			assert false;
		}

		setPropertySilently(EmbeddedImage.DATA_MEMBER, toSet);

	}

	/**
	 * Returns the embedded image name. This name is used to identify an embedded
	 * image by image item.
	 *
	 * @return the embedded image name
	 */

	public String getName() {
		return getStringProperty(EmbeddedImage.NAME_MEMBER);
	}

	/**
	 * Sets the embedded image name. This name is used to identify an embedded image
	 * by image item.
	 *
	 * @param name the embedded image name to set
	 * @throws SemanticException value required exception
	 */

	public void setName(String name) throws SemanticException {
		setProperty(EmbeddedImage.NAME_MEMBER, name);
	}

	/**
	 * Returns the image type. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li><code>IMAGE_TYPE_IMAGE_JPEG</code>
	 * <li><code>IMAGE_TYPE_IMAGE_BMP</code>
	 * <li><code>IMAGE_TYPE_IMAGE_GIF</code>
	 * <li><code>IMAGE_TYPE_IMAGE_PNG</code>
	 * <li><code>IMAGE_TYPE_IMAGE_X_PNG</code>
	 * </ul>
	 *
	 * @return the image type
	 */

	public String getType() {
		return getStringProperty(EmbeddedImage.TYPE_MEMBER);
	}

	/**
	 * Sets the image type. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li><code>IMAGE_TYPE_IMAGE_JPEG</code>
	 * <li><code>IMAGE_TYPE_IMAGE_BMP</code>
	 * <li><code>IMAGE_TYPE_IMAGE_GIF</code>
	 * <li><code>IMAGE_TYPE_IMAGE_PNG</code>
	 * <li><code>IMAGE_TYPE_IMAGE_X_PNG</code>
	 * </ul>
	 *
	 * @param type the image type to set
	 * @throws SemanticException if the image type is not in the choice list.
	 */

	public void setType(String type) throws SemanticException {
		setProperty(EmbeddedImage.TYPE_MEMBER, type);
	}

	/**
	 * Returns the qualified name of this element. The qualified name is the name of
	 * this element if this element is in module user is editing.
	 *
	 * @return the qualified name of thie element.
	 */

	public String getQualifiedName() {

		if (getName() == null) {
			return null;
		}

		Module module = getModule();
		if (module instanceof Library) {
			String namespace = ((Library) module).getNamespace();
			return StringUtil.buildQualifiedReference(namespace, getName());
		}

		return getName();
	}

	/**
	 * Checks whether <code>EmbeddedImage</code> has reference to library embedded
	 * image.
	 *
	 * @return if <code>EmbeddedImage</code> has reference to library image and the
	 *         reference is resolved return <code>true</code> else return
	 *         <code>false</code>
	 */
	public boolean isLibReference() {
		EmbeddedImage image = (EmbeddedImage) getStructure();
		StructRefValue ref = (StructRefValue) image.getLocalProperty(getModule(),
				ReferencableStructure.LIB_REFERENCE_MEMBER);

		if (ref == null || (!ref.isResolved())) {
			return false;
		}

		return true;

	}
}
