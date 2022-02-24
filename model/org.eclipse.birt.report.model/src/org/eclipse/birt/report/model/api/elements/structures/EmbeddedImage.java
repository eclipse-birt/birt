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

package org.eclipse.birt.report.model.api.elements.structures;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.elements.ImageItem;

/**
 * Represents an embedded image. The class gives the name and type of the image.
 * Used when an image element {@link ImageItem}gives a name. Each embedded image
 * has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>an embedded image has a unique and required name, so the image item can
 * use the image name to identify an embedded image.</dd>
 * 
 * <dt><strong>Type </strong></dt>
 * <dd>an embedded image has a choice and required type: bmp, gif, png or
 * x-png.</dd>
 * 
 * <dt><strong>Data </strong></dt>
 * <dd>value of the image data in Base64 encoding.</dd>
 * </dl>
 * 
 */

public class EmbeddedImage extends ReferencableStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String EMBEDDED_IMAGE_STRUCT = "EmbeddedImage"; //$NON-NLS-1$

	/**
	 * Name of the "name" property.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the "type" property. It can be:
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_TYPE_IMAGE_BMP</code>
	 * <li><code>IMAGE_TYPE_IMAGE_GIF</code>
	 * <li><code>IMAGE_TYPE_IMAGE_PNG</code>
	 * <li><code>IMAGE_TYPE_IMAGE_X_PNG</code>
	 * </ul>
	 */

	public static final String TYPE_MEMBER = "type"; //$NON-NLS-1$

	/**
	 * Name of the "data" property.
	 */

	public static final String DATA_MEMBER = "data"; //$NON-NLS-1$

	/**
	 * Encoding mode for the data member.
	 */

	public static final String CHARSET = "8859_1"; //$NON-NLS-1$

	/**
	 * Value of the name property.
	 */

	protected String name = null;

	/**
	 * Value of the expression property.
	 */

	protected String type = null;

	/**
	 * String of the image data in 8859_1 encoding.
	 */

	protected String data = null;

	/**
	 * Default constructor.
	 * 
	 */

	public EmbeddedImage() {
	}

	/**
	 * Constructs the image with the given name. The type of the image is set to the
	 * default value <code>DesignChoiceConstants.IMAGE_TYPE_AUTO</code>.
	 * 
	 * @param name name of the image
	 */

	public EmbeddedImage(String name) {
		this.name = name;
	}

	/**
	 * Constructs the image with the required name and type.
	 * 
	 * @param name name of the image
	 * @param type type of the image
	 */

	public EmbeddedImage(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return EMBEDDED_IMAGE_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (NAME_MEMBER.equals(propName))
			return name;
		else if (TYPE_MEMBER.equals(propName))
			return type;
		else if (DATA_MEMBER.equals(propName))
			return data;

		return super.getIntrinsicProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.
	 * String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (NAME_MEMBER.equalsIgnoreCase(propName))
			name = (String) value;
		else if (TYPE_MEMBER.equalsIgnoreCase(propName))
			type = (String) value;
		else if (DATA_MEMBER.equalsIgnoreCase(propName)) {
			// for data member, the verified value is a string.

			data = (String) value;
		} else {
			super.setIntrinsicProperty(propName, value);
		}

	}

	/**
	 * Returns name of the image.
	 * 
	 * @return name of the image
	 */

	public String getName() {
		return (String) getProperty(null, NAME_MEMBER);
	}

	/**
	 * Sets the name of the image.
	 * 
	 * @param name the name to set
	 */

	public void setName(String name) {
		setProperty(NAME_MEMBER, name);
	}

	/**
	 * Returns type value this node represents. It can be:
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_TYPE_IMAGE_JPEG</code>
	 * <li><code>IMAGE_TYPE_IMAGE_BMP</code>
	 * <li><code>IMAGE_TYPE_IMAGE_GIF</code>
	 * <li><code>IMAGE_TYPE_IMAGE_PNG</code>
	 * <li><code>IMAGE_TYPE_IMAGE_X_PNG</code>
	 * </ul>
	 * 
	 * @param module the module of this structure
	 * 
	 * @return the type value
	 */

	public String getType(Module module) {
		return (String) getProperty(module, TYPE_MEMBER);
	}

	/**
	 * Sets the type of the image. The allowed values are defined in
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
	 * @param type the type to set
	 */

	public void setType(String type) {
		setProperty(TYPE_MEMBER, type);
	}

	/**
	 * Returns the image data in Base64 encoding.
	 * 
	 * @param module the module of this structure
	 * 
	 * @return the image data
	 */

	public byte[] getData(Module module) {
		String dataValue = (String) getProperty(module, DATA_MEMBER);

		if (dataValue == null)
			return null;

		try {
			return dataValue.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			assert false;
		}

		return null;
	}

	/**
	 * Sets the data of the image.
	 * 
	 * @param data the image data to set
	 */

	public void setData(byte[] data) {
		// ignore the empty data
		if (data == null)
			return;

		try {
			this.data = new String(data, CHARSET);
			// setProperty( DATA_MEMBER, new String( data, CHARSET ) );
		} catch (UnsupportedEncodingException e) {
			this.data = null;
			assert false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new EmbeddedImageHandle(valueHandle, index);
	}

	/*
	 * Validates this structure. The following are the rules: <ul><li> The image
	 * name is required. <li> The image data is required. </ul>
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt.report
	 * .model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate(Module module, DesignElement element) {
		List list = super.validate(module, element);

		if (StringUtil.isBlank(name)) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), name,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}
		if (getData(module) == null) {
			list.add(new PropertyValueException(element, getDefn().getMember(DATA_MEMBER), data,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferencableStructure#
	 * isReferencableProperty(java.lang.String)
	 */
	public boolean isReferencableProperty(String memberName) {
		return NAME_MEMBER.equalsIgnoreCase(memberName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getReferencableProperty()
	 */

	public String getReferencableProperty() {
		return name;
	}
}
