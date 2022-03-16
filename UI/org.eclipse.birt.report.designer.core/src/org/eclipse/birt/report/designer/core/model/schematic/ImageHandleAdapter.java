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

package org.eclipse.birt.report.designer.core.model.schematic;

import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement ImageHandleAdapter responds to model ImageHandle
 */
public class ImageHandleAdapter extends ReportItemtHandleAdapter {

	/**
	 * Constructor
	 *
	 * @param handle
	 */
	public ImageHandleAdapter(ImageHandle image, IModelAdapterHelper mark) {
		super(image, mark);
	}

	/**
	 * Gets the SWT image instance for given Image model
	 *
	 * @return SWT image instance
	 */
	public Image getImage() {
		ImageHandle imageHandel = getImageHandle();
		String imageSource = imageHandel.getSource();
		PropertyHandle uriPropertyHandle = imageHandel.getPropertyHandle(IImageItemModel.URI_PROP);
		ExpressionHandle expression = imageHandel.getExpressionProperty(IImageItemModel.URI_PROP);
		String url = imageHandel.getURI();
		if (uriPropertyHandle != null && uriPropertyHandle.isLocal()) {
			if (expression != null && !ExpressionType.CONSTANT.equals(expression.getType())) {
				url = removeQuoteString(url);
			}
		}
		if (DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equalsIgnoreCase(imageSource)) {
			return ImageManager.getInstance().getEmbeddedImage(imageHandel.getModuleHandle(),
					imageHandel.getImageName());
		} else if (DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equalsIgnoreCase(imageSource)) {
			if (URIUtil.isValidResourcePath(url)) {
				return ImageManager.getInstance().getImage(imageHandel.getModuleHandle(), URIUtil.getLocalPath(url));
			} else {
				return ImageManager.getInstance().getImage(imageHandel.getModuleHandle(), url);
			}

		} else if (DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase(imageSource)) {
			// bugzilla 245641
			return ImageManager.getInstance().getURIImage(imageHandel.getModuleHandle(), url);
		} else if (DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equalsIgnoreCase(imageSource)) {
			// TODO: connection database to get the data
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	private String removeQuoteString(String value) {
		if (value != null && value.length() > 1 && value.charAt(0) == '\"'
				&& value.charAt(value.length() - 1) == '\"') {
			return value.substring(1, value.length() - 1);
		}
		return value;
	}

	private ImageHandle getImageHandle() {
		return (ImageHandle) getHandle();
	}

	/**
	 * Gets size of image item. If the image size is 0, return null.
	 *
	 * @return the size of image item.
	 */
	@Override
	public Dimension getSize() {
		DimensionHandle widthHandle = getImageHandle().getWidth();
		int px = (int) DEUtil.convertoToPixel(widthHandle);

		DimensionHandle heightHandle = getImageHandle().getHeight();
		int py = (int) DEUtil.convertoToPixel(heightHandle);

		if (DEUtil.isFixLayout(getHandle())) {
			if (px == 0 && widthHandle.isSet()) {
				px = 1;
			}
			if (py == 0 && heightHandle.isSet()) {
				py = 1;
			}
		}

		if (px != 0 && py != 0) {
			return new Dimension(px, py);
		}
		return null;
	}

	/**
	 * Gets size of image item. Always returns a non-null value.
	 *
	 * @return
	 */
	public Dimension getRawSize() {
		DimensionHandle widthHandle = getImageHandle().getWidth();
		int px = (int) DEUtil.convertoToPixel(widthHandle);

		DimensionHandle heightHandle = getImageHandle().getHeight();
		int py = (int) DEUtil.convertoToPixel(heightHandle);

		if (DEUtil.isFixLayout(getHandle())) {
			if (px == 0 && widthHandle.isSet()) {
				px = 1;
			}
			if (py == 0 && heightHandle.isSet()) {
				py = 1;
			}
		}

		return new Dimension(Math.max(px, 0), Math.max(py, 0));
	}

	@Override
	public void setSize(Dimension size) throws SemanticException {
		if (size.width >= 0) {
			getImageHandle().setWidth(size.width + DesignChoiceConstants.UNITS_PX);
		}
		if (size.height >= 0) {
			getImageHandle().setHeight(size.height + DesignChoiceConstants.UNITS_PX);
		}
	}

}
