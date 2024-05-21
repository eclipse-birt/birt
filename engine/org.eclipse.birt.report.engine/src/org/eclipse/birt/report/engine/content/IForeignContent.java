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
	String HTML_TYPE = "text/html";

	/** the content is an string which contains plain text */
	String TEXT_TYPE = "text/plaintext";
	/**
	 * the content is an template. In this condition the raw value should be a
	 * Object[2], the first object is the template text, the second object is the
	 * value map.
	 */

	String TEMPLATE_TYPE = "text/template";
	/** the content is the output of a extenal item, it is byte[] */

	String EXTERNAL_TYPE = "object/external";
	/** the content is an image content which define the image */

	String IMAGE_TYPE = "binary/image";

	/** the content is unkown */
	String UNKNOWN_TYPE = "binary/unknown";

	/** the object value */
	String VALUE_TYPE = "binary/value";

	/**
	 * Get the original format of the object. such as: "text/html", "text/rtf",
	 * "xml/svg" etc.
	 *
	 * @return type of the content
	 */
	String getRawType();

	/**
	 * Set the raw type
	 *
	 * @param type raw type of content
	 */
	void setRawType(String type);

	/**
	 * Get the raw content key
	 *
	 * @return the raw content key
	 */
	String getRawKey();

	/**
	 * Set the raw content key
	 *
	 * @param rawKey raw content key
	 */
	void setRawKey(String rawKey);

	/**
	 * the orignal content describe in raw format.
	 *
	 * @return Returns the content. Caller knows how to cast this object
	 */
	Object getRawValue();

	/**
	 * Set the raw content value
	 *
	 * @param value raw content value
	 */
	void setRawValue(Object value);

	/**
	 * @return Returns the altText.
	 */
	@Override
	String getAltText();

	@Override
	String getAltTextKey();

	@Override
	void setAltText(String altText);

	@Override
	void setAltTextKey(String key);

	/**
	 * Set the use of JTidy for document generation
	 *
	 * @param jTidy JTidy usage
	 */
	void setJTidy(boolean jTidy);

	/**
	 * Is JTidy to be used
	 *
	 * @return is JTidy to be used
	 */
	boolean isJTidy();
}
