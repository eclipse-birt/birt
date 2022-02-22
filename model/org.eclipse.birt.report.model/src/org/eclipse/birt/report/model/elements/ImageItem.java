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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportItemModel;

/**
 * This class represents an image item. Reports often display images in various
 * formats: corporate logos, product images, icons, and so on. Popular image
 * formats are JPEG for photographs, and GIF, PNG and SVG for computer-generated
 * graphics such as logos.
 * <p>
 * The image item provides an image in one of a number of formats. The image can
 * come from a number of sources. The image item can be sized to the image (in
 * which case the height and width attributes are ignored), or the image can be
 * sized or clipped to fit the item. Images are always scaled proportionately.
 * <p>
 * The image item is similar to the Image report element in RDL, the image
 * control in AFC and the picture item in Crystal. Key features include:
 * <li>Store the image as part of the report design, obtain the image at run
 * time, or reference the image at view time.</li>
 * <li>Allow the image to come from a BLOB field in the query.</li>
 * <li>Associate a hyperlink with the image.</li>
 * <li>Support line images. This is an image that is repeated to form a line.
 * For example, a slice of an image can repeat to form a custom border along the
 * top or side of a report. Allow repetition across or down.</li>
 * <li>The image element should provide a number of options for handling images
 * that are larger or smaller than the size of the element itself.</li>
 * <li>Clip the image to fit the element. Provide options of clipping all four
 * edges so the image remains centered in the element, or clipping the bottom
 * and left edges.</li>
 * <li>Expand or shrink the element to fit the image size. This is the default,
 * and works well for reports that use the power-assist form of layout (see
 * below.)</li>
 * <li>Adjust the element size to fit the image.</li>
 * <p>
 *
 */

public class ImageItem extends ReportItem implements IImageItemModel {

	/**
	 * Default constructor.
	 */

	public ImageItem() {
	}

	/**
	 * Constructs the image item with an optional name.
	 *
	 * @param theName the name of this image item, which is optional.
	 */

	public ImageItem(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitImage(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.IMAGE_ITEM;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public ImageHandle handle(Module module) {
		if (handle == null) {
			handle = new ImageHandle(module, this);
		}
		return (ImageHandle) handle;
	}

	/**
	 * Returns the image scale.
	 *
	 * @param design the report design instance
	 * @return the image scale value, which should be between 0 and 1.0.
	 */

	public double getImageScale(ReportDesign design) {
		return getFloatProperty(design, SCALE_PROP);
	}

	/**
	 * Returns the alternate text of the image.
	 *
	 * @param design the report design instance
	 * @return the alternate text
	 */

	public String getAltText(ReportDesign design) {
		Expression expr = (Expression) getProperty(design, IInternalReportItemModel.ALTTEXT_PROP);
		if (expr != null) {
			return expr.getStringExpression();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getDisplayLabel(org.eclipse.
	 * birt.report.model.elements.ReportDesign, int)
	 */

	@Override
	public String getDisplayLabel(Module module, int level) {
		StringBuilder displayLabel = new StringBuilder().append(super.getDisplayLabel(module, level));
		if (level == IDesignElementModel.FULL_LABEL) {
			displayLabel.append("("); //$NON-NLS-1$

			String sourceType = getStringProperty(module, SOURCE_PROP);

			if (DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equalsIgnoreCase(sourceType)
					|| DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase(sourceType)) {
				displayLabel.append(limitStringLength(getStringProperty(module, URI_PROP)));
			} else if (DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equalsIgnoreCase(sourceType)) {
				displayLabel.append(limitStringLength(getStringProperty(module, IMAGE_NAME_PROP)));
			} else if (DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equalsIgnoreCase(sourceType)) {
				displayLabel.append(limitStringLength(getStringProperty(module, VALUE_EXPR_PROP)));
			}

			displayLabel.append(")"); //$NON-NLS-1$
		}
		return displayLabel.toString();
	}
}
