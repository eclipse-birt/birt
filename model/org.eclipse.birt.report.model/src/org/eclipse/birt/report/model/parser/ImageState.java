/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.api.validators.ImageDataValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.SecurityUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses an image item in the design file.
 * 
 */

public class ImageState extends ReportItemState {

	/**
	 * The image item being created.
	 */

	protected ImageItem image;

	/**
	 * Constructs the image item state with the design parser handler, the container
	 * element and the container slot of the image item.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public ImageState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/**
	 * Constructs image state with the design parser handler, the container element
	 * and the container property name of the report element.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public ImageState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler, theContainer, prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement() {
		return image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		image = new ImageItem();

		initElement(attrs);
	}

	/**
	 * Check whether the source type conflicts, and set the proper source type.
	 */

	private void checkImageType() {

		int type = 0;
		Module module = handler.getModule();

		String uri = getLocalStringExpression(image, IImageItemModel.URI_PROP, module);
		if (!StringUtil.isEmpty(uri)) {
			uri = StringUtil.trimQuotes(uri);
			try {
				new URL(uri);
				setProperty(IImageItemModel.SOURCE_PROP, DesignChoiceConstants.IMAGE_REF_TYPE_URL);
			} catch (MalformedURLException e) {

				if (isFileProtocol(uri))
					setProperty(IImageItemModel.SOURCE_PROP, DesignChoiceConstants.IMAGE_REF_TYPE_FILE);
				else
					setProperty(IImageItemModel.SOURCE_PROP, DesignChoiceConstants.IMAGE_REF_TYPE_EXPR);
			}

			type++;
		}

		StructRefValue imageName = (StructRefValue) image.getLocalProperty(module, IImageItemModel.IMAGE_NAME_PROP);
		if (imageName != null) {
			setProperty(IImageItemModel.SOURCE_PROP, DesignChoiceConstants.IMAGE_REF_TYPE_EMBED);
			type++;
		}

		String typeExpr = getLocalStringExpression(image, IImageItemModel.TYPE_EXPR_PROP, module);
		String valueExpr = getLocalStringExpression(image, IImageItemModel.VALUE_EXPR_PROP, module);

		if (!StringUtil.isEmpty(typeExpr) || !StringUtil.isEmpty(valueExpr)) {
			setProperty(IImageItemModel.SOURCE_PROP, DesignChoiceConstants.IMAGE_REF_TYPE_EXPR);
			type++;
		}

		if (type > 1)
			handler.getErrorHandler().semanticError(
					new DesignParserException(DesignParserException.DESIGN_EXCEPTION_IMAGE_REF_CONFLICT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		Module module = handler.getModule();

		if (image.getLocalProperty(module, IImageItemModel.SOURCE_PROP) == null
				&& handler.versionNumber <= VersionUtil.VERSION_3_2_3)
			checkImageType();

		// check the validation of the image data.

		List<SemanticException> tmpList = ImageDataValidator.getInstance().validate(handler.module, image);
		for (int i = 0; i < tmpList.size(); i++) {
			handler.getErrorHandler().semanticWarning(tmpList.get(i));
		}

		super.end();
	}

	/**
	 * Checks whether <code>filePath</code> is a valid file on the disk.
	 * <code>filePath</code> can follow these scheme.
	 * <ul>
	 * <li>./../hello/
	 * <li>C:\\hello\..\
	 * <li>/C:/../hello/.
	 * </ul>
	 * 
	 * @param filePath the input filePath
	 * @return true if filePath exists on the disk. Otherwise false.
	 */

	private static boolean isFileProtocol(String filePath) {
		try {
			URL fileUrl = new URL(filePath);
			if (URIUtil.FILE_SCHEMA.equalsIgnoreCase(fileUrl.getProtocol()))
				return true;

			return false;
		} catch (MalformedURLException e) {
			// ignore the error since this string is not in URL format
		}
		File file = new File(filePath);

		String scheme = SecurityUtil.getFiletoURISchemaPart(file);
		if (scheme == null)
			return false;

		if (scheme.equalsIgnoreCase(URIUtil.FILE_SCHEMA)) {
			return true;
		}
		return false;
	}

	/**
	 * @param element
	 * @param propName
	 * @return
	 */

	private static String getLocalStringExpression(DesignElement element, String propName, Module root) {
		Object value = element.getLocalProperty(root, propName);
		if (!(value instanceof Expression))
			return null;

		return ((Expression) value).getStringExpression();
	}
}