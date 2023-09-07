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
import org.eclipse.swt.graphics.Rectangle;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement ImageHandleAdapter responds to model ImageHandle
 */
public class ImageHandleAdapter extends ReportItemtHandleAdapter {

	private Rectangle imageFigureSize = null;

	/**
	 * Constructor
	 *
	 * @param image image handle
	 * @param mark  model adapter helper
	 */
	public ImageHandleAdapter(ImageHandle image, IModelAdapterHelper mark) {
		super(image, mark);
	}

	/**
	 * Set the image figure size
	 *
	 * @param imageFigureSize size of the image figure (width & height)
	 */
	public void setImageFigureDimension(Rectangle imageFigureSize) {
		this.imageFigureSize = imageFigureSize;
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
			}
			return ImageManager.getInstance().getImage(imageHandel.getModuleHandle(), url);

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
	 * @return the size of image item (unit px)
	 */
	@Override
	public Dimension getSize() {
		return evaluateSize(false);
	}

	/**
	 * Gets size of image item. Always returns a non-null value.
	 *
	 * @return Return the size of the image item (unit px)
	 */
	public Dimension getRawSize() {
		return evaluateSize(true);
	}

	/**
	 * Evaluate the size of the image based on px
	 *
	 * @param getRawSize return the real raw size
	 * @return Return the requested size of the image (unit px)
	 */
	private Dimension evaluateSize(boolean getRawSize) {
		int px = 0;
		int py = 0;

		DimensionHandle widthHandle = this.getImageHandle().getWidth();
		if (this.imageFigureSize != null && DesignChoiceConstants.UNITS_PERCENTAGE.equals(widthHandle.getUnits())) {
			px = (int) DEUtil.convertToPixel(widthHandle, this.imageFigureSize.width, DesignChoiceConstants.UNITS_PX);
		} else {
			px = (int) DEUtil.convertoToPixel(widthHandle);
		}
		DimensionHandle heightHandle = this.getImageHandle().getHeight();
		if (this.imageFigureSize != null && DesignChoiceConstants.UNITS_PERCENTAGE.equals(heightHandle.getUnits())) {
			py = (int) DEUtil.convertToPixel(heightHandle, this.imageFigureSize.height, DesignChoiceConstants.UNITS_PX);
		} else {
			py = (int) DEUtil.convertoToPixel(heightHandle);
		}

		if (DEUtil.isFixLayout(getHandle())) {
			if (px == 0 && widthHandle.isSet()) {
				px = 1;
			}
			if (py == 0 && heightHandle.isSet()) {
				py = 1;
			}
		}

		// proportional scale of the image size
		if (this.getImageHandle().isProportionalScale()) {
			py = px;
		}

		// return the real raw size
		if (getRawSize) {
			return new Dimension(Math.max(px, 0), Math.max(py, 0));
		}

		// return only if size is given (if not return null)
		if (px != 0 && py != 0) {
			return new Dimension(px, py);
		}
		return null;
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
