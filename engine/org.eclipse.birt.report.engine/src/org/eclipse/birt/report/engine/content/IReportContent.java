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
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.ir.Report;

import com.ibm.icu.util.ULocale;

/**
 * The object represents the report content as a whole.
 *
 */
public interface IReportContent {
	/**
	 * get the report design used to create this report content.
	 *
	 * @return the report design.
	 */
	Report getDesign();

	/**
	 * get the error list which occurs in the generation.
	 *
	 * @return error list.
	 */
	List getErrors();

	/**
	 * get the TOC structure constructed in the generation.
	 *
	 * @return the TOC structure.
	 * @deprecated This method shoule be substituted by:<br>
	 *             &nbsp;&nbsp;&nbsp;&nbsp;
	 *             <code>getTOCTree( format, locale ).getTOCTree( );</code>
	 */
	@Deprecated
	TOCNode getTOC();

	/**
	 * Gets the toc tree of this report content.
	 *
	 * @return the TOC Tree
	 */
	ITOCTree getTOCTree(String format, ULocale locale);

	/**
	 * return root content
	 *
	 * @return
	 */
	IContent getRoot();

	long getTotalPage();

	/**
	 * The page content in the report content. If the page is not exist, return
	 * NULL.
	 *
	 * @param pageNumber page number
	 * @return the page content object.
	 */
	IPageContent getPageContent(long pageNumber);

	/**
	 * return the content in this report.
	 *
	 * @param id content id
	 * @return the content object
	 */
	IContent getContent(InstanceID id);

	/**
	 * find the named style.
	 *
	 * @param styleClass style name.
	 * @return style named with the name, null if not exists.
	 */
	IStyle findStyle(String styleClass);

	/**
	 * create a anction content. The action content can only be use in this report
	 * content.
	 *
	 * @return the action content.
	 */
	IHyperlinkAction createActionContent();

	/**
	 * create a style. the style can only be used in this report.
	 *
	 * @return style created.
	 */
	IStyle createStyle();

	/**
	 * create a cell content. the content can only be used in this report.
	 *
	 * @return cell content.
	 */
	ICellContent createCellContent();

	/**
	 * create a container content. the content can only be used in this report.
	 *
	 * @return the container content.
	 */
	IContainerContent createContainerContent();

	/**
	 * create a page content. the page content can only be used in this report.
	 *
	 * @return the page content.
	 */
	IPageContent createPageContent();

	/**
	 * create an table content. the table content can only be used in this report.
	 *
	 * @return the table.
	 */
	ITableContent createTableContent();

	ITableGroupContent createTableGroupContent();

	ITableBandContent createTableBandContent();

	IListContent createListContent();

	IListGroupContent createListGroupContent();

	IListBandContent createListBandContent();

	/**
	 * create the row content. the row can only be used in this report.
	 *
	 * @return the row content.
	 */
	IRowContent createRowContent();

	/**
	 * create the text content. the text can only be used in this report.
	 *
	 * @return the text content.
	 */
	ITextContent createTextContent();

	/**
	 * create the text content, copy the properties from the template content. the
	 * text can only be used in this report.
	 *
	 * @param content the content template.
	 * @return the text content.
	 */
	ITextContent createTextContent(IContent conent);

	/**
	 * create the foreign content. the foreign content can only be used in this
	 * report.
	 *
	 * @return the foreign content
	 */
	IForeignContent createForeignContent();

	/**
	 * create the image content. the image can only be used in this report.
	 *
	 * @return the image content.
	 */
	IImageContent createImageContent();

	/**
	 * create the image content, copy the properties from the template content. the
	 * image can only be used in this report.
	 *
	 * @param content the content template.
	 * @return the image content.
	 */
	IImageContent createImageContent(IContent conent);

	/**
	 * create the label content. the label can only be used in this report.
	 *
	 * @return the label content.
	 */
	ILabelContent createLabelContent();

	/**
	 * create the auto text content. the auto text can only be used in this report.
	 *
	 * @return the auto text content.
	 */
	IAutoTextContent createAutoTextContent();

	/**
	 * create the label content, copy the properties from the template content. the
	 * label can only be used in this report.
	 *
	 * @param content the content template.
	 * @return the label content.
	 */
	ILabelContent createLabelContent(IContent conent);

	/**
	 * create the data content. the data can only be used in this report.
	 *
	 * @return the data content.
	 */
	IDataContent createDataContent();

	/**
	 * create the data content, copy the properties from the template content. the
	 * data can only be used in this report.
	 *
	 * @param content the content template.
	 * @return the data content.
	 */
	IDataContent createDataContent(IContent conent);

	String getACL();

	void setACL(String acl);

	/**
	 *
	 * @return the ReportContext
	 */
	IReportContext getReportContext();

	Map<String, Object> getUserProperties();

	Map<String, Object> getExtensions();

	void setExtensions(Map<String, Object> properties);

	void writeContent(DataOutputStream out) throws IOException;

	void readContent(DataInputStream in, ClassLoader loader) throws IOException;

	String getTitle();

	void setTitle(String title);
}
