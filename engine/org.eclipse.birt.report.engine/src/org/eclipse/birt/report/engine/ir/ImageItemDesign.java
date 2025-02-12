/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

/**
 * Image Item definition.
 *
 */
public class ImageItemDesign extends ReportItemDesign {

	/**
	 * the image source is defined by a URI
	 */
	public final static int IMAGE_URI = 0;
	/**
	 * the image source is defined by name
	 */
	public final static int IMAGE_NAME = 1;
	/**
	 * the image source is defined by expression.
	 */
	public final static int IMAGE_EXPRESSION = 2;

	/**
	 * the image source is defined by a FILE
	 */
	public final static int IMAGE_FILE = 3;

	/**
	 * image source type
	 */
	protected int imageSource;

	/**
	 * image uri, used if source type URI
	 */
	protected Expression imageUri;
	/**
	 * image name, used if source type NAME
	 */
	protected Expression imageName;
	/**
	 * image expression, used if source type EXPR
	 */
	protected Expression imageExpression;
	/**
	 * image type, used if source type EXPR.
	 */
	protected Expression imageFormat;
	/**
	 * help text
	 */
	protected String helpText;
	/**
	 * help text resource key
	 */
	protected String helpTextKey;

	protected boolean fitToContainer;

	/**
	 * indicates whether to scale the image proportionally.
	 */
	protected boolean proportionalScale;

	/**
	 * Constructor
	 */
	public ImageItemDesign() {
		this.imageSource = IMAGE_URI;
	}

	@Override
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitImageItem(this, value);
	}

	/**
	 * get the type of the image source.
	 *
	 * Image can be defined by expression, uri and name.
	 *
	 * @return type of the image source.
	 */
	public int getImageSource() {
		return this.imageSource;
	}

	/**
	 * @param imageExpr The imageExpr to set.
	 * @param imageType the image type.
	 */
	public void setImageExpression(Expression imageExpr, Expression imageType) {
		this.imageSource = IMAGE_EXPRESSION;
		this.imageExpression = imageExpr;
		this.imageFormat = imageType;
	}

	/**
	 * @return Returns the imageExpr.
	 */
	public Expression getImageExpression() {
		if (this.imageSource == IMAGE_EXPRESSION) {
			return imageExpression;
		}
		return null;
	}

	/**
	 * @return Returns the imageType.
	 */
	public Expression getImageFormat() {
		if (this.imageSource == IMAGE_EXPRESSION) {
			return imageFormat;
		}
		return null;
	}

	/**
	 * @param imageName The imageName to set.
	 */
	public void setImageName(Expression imageName) {
		this.imageSource = IMAGE_NAME;
		this.imageName = imageName;
	}

	/**
	 * @return Returns the imageName.
	 */
	public Expression getImageName() {
		if (imageSource == IMAGE_NAME) {
			return imageName;
		}
		return null;
	}

	/**
	 * @param imageUri The imageUri to set.
	 */
	public void setImageUri(Expression imageUri) {
		this.imageSource = IMAGE_URI;
		this.imageUri = imageUri;
	}

	/**
	 * @return Returns the imageUri.
	 */
	public Expression getImageUri() {
		if (imageSource == IMAGE_URI || imageSource == IMAGE_FILE) {
			return imageUri;
		}
		return null;
	}

	/**
	 * Set image file
	 *
	 * @param file file URI
	 */
	public void setImageFile(Expression file) {
		imageSource = IMAGE_FILE;
		imageUri = file;
	}

	/**
	 * set the help info.
	 *
	 * @param key  resource key
	 * @param text text content
	 */
	public void setHelpText(String key, String text) {
		this.helpTextKey = key;
		this.helpText = text;
	}

	/**
	 * get the help text property.
	 *
	 * @return help text
	 */
	public String getHelpText() {
		return this.helpText;
	}

	/**
	 * get the help text resource key property.
	 *
	 * @return resource key of the help text
	 */
	public String getHelpTextKey() {
		return this.helpTextKey;
	}

	/**
	 * get the FitToContainer property.
	 *
	 * @return the FitToContainer property.
	 */
	public boolean isFitToContainer() {
		return fitToContainer;
	}

	/**
	 * set the FitToContainer property.
	 *
	 * @param fitToContainer
	 */
	public void setFitToContainer(boolean fitToContainer) {
		this.fitToContainer = fitToContainer;
	}

	/**
	 * @return the proportionalScale
	 */
	public boolean isProportionalScale() {
		return proportionalScale;
	}

	/**
	 * @param proportionalScale the proportionalScale to set
	 */
	public void setProportionalScale(boolean proportionalScale) {
		this.proportionalScale = proportionalScale;
	}

}
