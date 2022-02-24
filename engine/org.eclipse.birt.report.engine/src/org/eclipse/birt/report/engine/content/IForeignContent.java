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

package org.eclipse.birt.report.engine.content;

/**
 * 
 * The content of foreign object is not handle by report engine.
 * 
 * It is the object's responsibility to convert itself to standard content type.
 * 
 * Such as object described in HTML/RTF or other format.
 * 
 * For foreign object in "text/html" format, the PDF writer output the standard
 * content while the HTML writer output the raw value directly.
 * 
 */
public interface IForeignContent extends IContent {

	/** the content is an string which contains HTML content */
	public final String HTML_TYPE = "text/html";
	/** the content is an string which contains plain text */
	public final String TEXT_TYPE = "text/plaintext";
	/**
	 * the content is an template. In this condition the raw value should be a
	 * Object[2], the first object is the template text, the second object is the
	 * value map.
	 */
	public final String TEMPLATE_TYPE = "text/template";
	/** the content is the output of a extenal item, it is byte[] */
	public final String EXTERNAL_TYPE = "object/external";
	/** the content is an image content which define the image */
	public final String IMAGE_TYPE = "binary/image";
	/** the content is unkown */
	public final String UNKNOWN_TYPE = "binary/unknown";
	/** the object value */
	public final String VALUE_TYPE = "binary/value";

	/**
	 * the orginal format of the object. such as: "text/html", "text/rtf", "xml/svg"
	 * etc.
	 * 
	 * @return type of the content
	 */
	public String getRawType();

	void setRawType(String type);

	public String getRawKey();

	void setRawKey(String rawKey);

	/**
	 * the orignal content describe in raw format.
	 * 
	 * @return Returns the content. Caller knows how to cast this object
	 */
	public Object getRawValue();

	void setRawValue(Object value);

	/**
	 * @return Returns the altText.
	 */
	public String getAltText();

	public String getAltTextKey();

	public void setAltText(String altText);

	public void setAltTextKey(String key);

	public void setJTidy(boolean jTidy);

	public boolean isJTidy();
}
