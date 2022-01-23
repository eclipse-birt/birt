/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * object created by report generator.
 * 
 * the content of report document.
 * 
 */
public interface IContent extends IElement, CSSStylableElement, IStyledElement {
	final static int SERIALIZE_CONTENT = -1;
	final static int REPORT_CONTENT = 0;
	final static int CELL_CONTENT = 1;
	final static int CONTAINER_CONTENT = 2;
	final static int DATA_CONTENT = 3;
	final static int FOREIGN_CONTENT = 4;
	final static int IMAGE_CONTENT = 5;
	final static int LABEL_CONTENT = 6;
	final static int PAGE_CONTENT = 7;
	final static int ROW_CONTENT = 8;
	final static int TABLE_BAND_CONTENT = 9;
	final static int TABLE_CONTENT = 10;
	final static int TEXT_CONTENT = 11;
	final static int AUTOTEXT_CONTENT = 12;
	final static int LIST_CONTENT = 13;
	final static int LIST_BAND_CONTENT = 14;
	final static int GROUP_CONTENT = 15;
	final static int LIST_GROUP_CONTENT = 16;
	final static int TABLE_GROUP_CONTENT = 17;

	/**
	 * the content type, must be one of the predefines.
	 * 
	 * @return
	 */
	int getContentType();

	/**
	 * get the instance id of the content. the instance id is the unique id of the
	 * content.
	 * 
	 * @return
	 */
	InstanceID getInstanceID();

	/**
	 * set the instace id of the content. the instance id can only be set by the
	 * content generator.
	 * 
	 * @param id
	 */
	void setInstanceID(InstanceID id);

	static final int DOCUMENT_EXTENSION = 0;
	static final int LAYOUT_EXTENSION = 1;

	Object getExtension(int extension);

	/**
	 * Set the extension of the content. Only 2 extension supported so far
	 * <li>0: document extension</li>
	 * <li>1: layout extension</li>
	 * 
	 * @param extension extension type
	 * @param value     extension value
	 * @throws ArrayIndexOutOfBoundsException if index is outof range {0,1}
	 */
	void setExtension(int extension, Object value);

	/**
	 * return the report which contains/create this content.
	 * 
	 * @return
	 */
	IReportContent getReportContent();

	/**
	 * set the report content.
	 * 
	 * @param report
	 */
	void setReportContent(IReportContent report);

	/**
	 * unique id of the content.
	 * 
	 * @return
	 */
	String getName();

	void setName(String name);

	/**
	 * the design object which create this content.
	 * 
	 * @return
	 */
	Object getGenerateBy();

	void setGenerateBy(Object generateBy);

	/**
	 * @return inline style
	 */
	IStyle getInlineStyle();

	void setInlineStyle(IStyle style);

	/**
	 * use visitor to process the object.
	 * 
	 * @param visitor
	 * @throws BirtException
	 */
	Object accept(IContentVisitor visitor, Object value) throws BirtException;

	/**
	 * @return the bookmark value
	 */
	String getBookmark();

	void setBookmark(String bookmark);

	/**
	 * @return hyperlink actions
	 */
	IHyperlinkAction getHyperlinkAction();

	void setHyperlinkAction(IHyperlinkAction hyperlink);

	/**
	 * @return Returns the altText.
	 */
	String getAltText();

	void setAltText(String altText);

	/**
	 * @return Returns the altText key.
	 */
	String getAltTextKey();

	void setAltTextKey(String altTextKey);

	/**
	 * @return Returns the helpText.
	 */
	String getHelpText();

	void setHelpText(String help);

	/**
	 * sepcified value, the actual height is defined in IBounds
	 * 
	 * @return the height of the report item
	 */
	DimensionType getHeight();

	void setHeight(DimensionType height);

	/**
	 * specified value, the real value is defined in IBounds
	 * 
	 * @return the width of the report item
	 */
	DimensionType getWidth();

	void setWidth(DimensionType width);

	/**
	 * specified value, the real value is defined in IBounds
	 * 
	 * @return the x position of the report item.
	 */
	DimensionType getX();

	void setX(DimensionType x);

	/**
	 * specified value, the real value is defined in IBounds
	 * 
	 * @return Returns the y position of the report item.
	 */
	DimensionType getY();

	void setY(DimensionType y);

	Object getTOC();

	void setTOC(Object toc);

	void writeContent(DataOutputStream out) throws IOException;

	void readContent(DataInputStream in, ClassLoader loader) throws IOException;

	IContent cloneContent(boolean isDeep);

	boolean isRTL();

	boolean isDirectionRTL();

	String getACL();

	void setACL(String acl);

	IBaseResultSet getResultSet();

	boolean isLastChild();

	void setLastChild(boolean isLastChild);

	boolean hasChildren();

	void setHasChildren(boolean hasChildren);

	Map<String, Object> getUserProperties();

	void setUserProperties(Map<String, Object> values);

	Map<String, Object> getExtensions();

	void setExtensions(Map<String, Object> values);
}
