/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Represents a the design of an Image in the scripting environment
 */
public interface IImage extends IReportItem {

	/**
	 * Returns the image scale. The scale factor for the image given as a
	 * percentage.
	 *
	 * @return the scale of this image.
	 */

	double getScale();

	/**
	 * Returns the image size. The size must be the internal name that is one the
	 * following options defined in <code>DesignChoiceConstants</code>:
	 *
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_SIZE_SIZE_TO_IMAGE</code>
	 * <li><code>IMAGE_SIZE_SCALE_TO_ITEM</code>
	 * <li><code>IMAGE_SIZE_CLIP</code>
	 * </ul>
	 *
	 * @return the internal value of the image size.
	 */

	String getSize();

	/**
	 * Returns the alternate text of this image.
	 *
	 * @return the alternate text of the image item.
	 */

	String getAltText();

	/**
	 * Sets the alternate text of this image.
	 *
	 * @param altText the alternate text
	 * @throws ScriptException
	 */
	void setAltText(String altText) throws ScriptException;

	/**
	 * Returns the resource key of the alternate text of this image.
	 *
	 * @return the resource key of the alternate text
	 */

	String getAltTextKey();

	/**
	 * Sets the resource key of the alternate text of this image.
	 *
	 * @param altTextKey the resource key of the alternate text
	 * @throws ScriptException
	 */
	void setAltTextKey(String altTextKey) throws ScriptException;

	/**
	 * Returns the image source type. This is one of the following options defined
	 * in <code>DesignChoiceConstants</code>:
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_REF_TYPE_NONE</code>
	 * <li><code>IMAGE_REF_TYPE_URL</code>
	 * <li><code>IMAGE_REF_TYPE_FILE</code>
	 * <li><code>IMAGE_REF_TYPE_EXPR</code>
	 * <li><code>IMAGE_REF_TYPE_EMBED</code>
	 * </ul>
	 *
	 * @return the image source type.
	 *
	 */

	String getSource();

	/**
	 * Returns the image source type. This is one of the following options defined
	 * in <code>DesignChoiceConstants</code>:
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_REF_TYPE_NONE</code>
	 * <li><code>IMAGE_REF_TYPE_URL</code>
	 * <li><code>IMAGE_REF_TYPE_FILE</code>
	 * <li><code>IMAGE_REF_TYPE_EXPR</code>
	 * <li><code>IMAGE_REF_TYPE_EMBED</code>
	 * </ul>
	 *
	 * @param source the image source type.
	 * @throws ScriptException if the <code>source</code> is not one of the above.
	 *
	 */

	void setSource(String source) throws ScriptException;

	/**
	 * Returns the image URI if the image source type is
	 * <code>IMAGE_REF_TYPE_URL</code> or <code>IMAGE_REF_TYPE_FILE</code>.
	 *
	 * @return the image URI if the image source type is
	 *         <code>IMAGE_REF_TYPE_URL</code> or <code>IMAGE_REF_TYPE_FILE</code>.
	 *         Otherwise, return <code>null</code>.
	 */

	String getURI();

	/**
	 * Returns the type expression of the image item if the image source type is
	 * <code>IMAGE_REF_TYPE_EXPR</code>.
	 *
	 * @return the type expression, if the image source type is
	 *         <code>IMAGE_REF_TYPE_EXPR</code>. Otherwise, return
	 *         <code>null</code>.
	 *
	 */

	String getTypeExpression();

	/**
	 * Returns the value expression of the image if the image source type is
	 * <code>IMAGE_REF_TYPE_EXPR</code>.
	 *
	 * @return the value expression, if the image source type is
	 *         <code>IMAGE_REF_TYPE_EXPR</code>. Otherwise, return
	 *         <code>null</code>.
	 */

	String getValueExpression();

	/**
	 * Returns the embedded image name that this image refers, if the image source
	 * type is <code>IMAGE_REF_TYPE_EMBED</code>. This is not the same as
	 * {@link DesignElementHandle#getName}of this image item.
	 *
	 * @return the embedded image name, if the image source type is
	 *         <code>IMAGE_REF_TYPE_EMBED</code>. Otherwise, return
	 *         <code>null</code>.
	 */

	String getImageName();

	/**
	 * Sets the embedded image name that this image refers, if the image source type
	 * is <code>IMAGE_REF_TYPE_EMBED</code>. The reference type is automatically set
	 * in this method. This is not the same as
	 * {@link DesignElementHandle#setName( String )}.
	 *
	 * @param name the embedded image name
	 * @throws ScriptException if the property is locked.
	 */

	void setImageName(String name) throws ScriptException;

	/**
	 * @deprecated Sets the image uri property. The source type is automatically set
	 *             in this method. Whether <code>IMAGE_REF_TYPE_FILE</code> or
	 *             <code>IMAGE_REF_TYPE_URL</code> depends on the uri to set.
	 *
	 * @param uri the uri to be set.
	 * @throws ScriptException if the property is locked.
	 */

	@Deprecated
	void setURI(String uri) throws ScriptException;

	/**
	 * Sets the image scale property. The scale factor for the image given as a
	 * percentage. The default is 100%.
	 *
	 * @param scale the scale value to be set.
	 * @throws ScriptException if the property is locked.
	 */

	void setScale(double scale) throws ScriptException;

	/**
	 * Sets the image size property. The input value is one of the followings
	 * defined in <code>DesignChoiceConstants</code>:
	 *
	 * <p>
	 * <ul>
	 * <li><code>IMAGE_SIZE_SIZE_TO_IMAGE</code>
	 * <li><code>IMAGE_SIZE_SCALE_TO_ITEM</code>
	 * <li><code>IMAGE_SIZE_CLIP</code>
	 * </ul>
	 *
	 * @param size the size value to be set.
	 * @throws ScriptException if the input size is not one of the above, or if the
	 *                         property is locked.
	 */

	void setSize(String size) throws ScriptException;

	/**
	 * Sets the type expression value. The source type is automatically set to
	 * <code>IMAGE_REF_TYPE_EXPR</code>.
	 *
	 * @param value the type expression value.
	 * @throws ScriptException if the property is locked.
	 */

	void setTypeExpression(String value) throws ScriptException;

	/**
	 * Sets the value expression value. The source type is automatically set to
	 * <code>IMAGE_REF_TYPE_EXPR</code>.
	 *
	 * @param value the value expression.
	 * @throws ScriptException if the property is locked.
	 */

	void setValueExpression(String value) throws ScriptException;

	/**
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 *
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the image.
	 * @see ActionHandle
	 */

	IAction getAction();

	/**
	 * Returns the help text of this image item.
	 *
	 * @return the help text
	 */

	String getHelpText();

	/**
	 * Sets the help text of this image item.
	 *
	 * @param helpText the help text
	 *
	 * @throws ScriptException if the property is locked.
	 */

	void setHelpText(String helpText) throws ScriptException;

	/**
	 * Returns the resource key of the help text of this image item.
	 *
	 * @return the resource key of the help text
	 */

	String getHelpTextKey();

	/**
	 * Sets the resource key of help text of this image item.
	 *
	 * @param helpTextKey the help text
	 *
	 * @throws ScriptException if the property is locked.
	 */

	void setHelpTextKey(String helpTextKey) throws ScriptException;

	/**
	 * Sets the image url. The source type is <code>IMAGE_REF_TYPE_URL</code>, and
	 * will automatically set in this method.
	 *
	 * @param url
	 * @throws ScriptException
	 */
	void setURL(String url) throws ScriptException;

	/**
	 * Gets the image url, if the source type is not <code>IMAGE_REF_TYPE_URL</code>
	 * return null.
	 *
	 * @return image url.
	 */
	String getURL();

	/**
	 * Sets the image file. The source type is <code>IMAGE_REF_TYPE_FILE</code>, and
	 * will automatically set in this method.
	 *
	 * @param file
	 * @throws ScriptException
	 */
	void setFile(String file) throws ScriptException;

	/**
	 * Returns the image file, if the source type is not
	 * <code>IMAGE_REF_TYPE_FILE</code> return null.
	 *
	 * @return image file.
	 */
	String getFile();

	/**
	 * Adds the action structure to this data item.
	 *
	 * @param action action structure
	 */
	void addAction(IAction action);

}
