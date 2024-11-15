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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;

/**
 * Report Item
 *
 */
abstract public class ReportItemDesign extends StyledElementDesign {

	/**
	 * x position
	 */
	protected DimensionType x;
	/**
	 * y position
	 */
	protected DimensionType y;
	/**
	 * width
	 */
	protected DimensionType width;
	/**
	 * height
	 */
	protected DimensionType height;

	/**
	 * book-mark associated with this element.
	 */
	protected Expression bookmark;
	/**
	 * TOC expression
	 */
	protected Expression toc;

	/**
	 * scripted called while on created
	 */
	protected Expression onCreate;

	/**
	 * script called while on render
	 */
	protected Expression onRender;

	/**
	 * script called while on render
	 */
	protected Expression onPageBreak;

	/**
	 * Visibility property.
	 */
	protected VisibilityDesign visibility;

	/**
	 * Action associated with this DataItem.
	 */
	protected ActionDesign action;

	/**
	 * query used to create the data set.
	 */
	transient protected IDataQueryDefinition[] queries;

	/**
	 * if the item use cached result or not.
	 */
	protected boolean useCachedResult = false;

	/**
	 * Expression styles.
	 */
	protected Map<Integer, Expression> expressionStyles;

	protected Object extensionData;

	/**
	 * Text associated with this image, used for default locale.
	 */
	protected Expression altText;

	/**
	 * Text Resource Key used for altText localization.
	 */
	protected String altTextKey;

	/**
	 * Constructor
	 */
	public ReportItemDesign() {
	}

	/**
	 * @return Returns the height.
	 */
	public DimensionType getHeight() {
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setHeight(DimensionType height) {
		this.height = height;
	}

	/**
	 * @return Returns the width.
	 */
	public DimensionType getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(DimensionType width) {
		this.width = width;
	}

	/**
	 * @return Returns the x.
	 */
	public DimensionType getX() {
		return x;
	}

	/**
	 * @param x The x to set.
	 */
	public void setX(DimensionType x) {
		this.x = x;
	}

	/**
	 * @return Returns the y.
	 */
	public DimensionType getY() {
		return y;
	}

	/**
	 * @param y The y to set.
	 */
	public void setY(DimensionType y) {
		this.y = y;
	}

	/**
	 * accept a visitor. see visit pattern.
	 *
	 * @param visitor report item visitor
	 * @param value
	 * @return Return the accepted object
	 */
	abstract public Object accept(IReportItemVisitor visitor, Object value);

	/**
	 * Get the TOC
	 *
	 * @return Return the TOC
	 */
	public Expression getTOC() {
		return toc;
	}

	/**
	 * Set the TOC
	 *
	 * @param expr expression of TOC
	 */
	public void setTOC(Expression expr) {
		this.toc = expr;
	}

	/**
	 * Get the bookmarks
	 *
	 * @return Returns the bookmark.
	 */
	public Expression getBookmark() {
		return bookmark;
	}

	/**
	 * Set the bookmarks
	 *
	 * @param bookmark The book-mark to set.
	 */
	public void setBookmark(Expression bookmark) {
		this.bookmark = bookmark;
	}

	/**
	 * Get the queries
	 *
	 * @return Returns the queries.
	 */
	public IDataQueryDefinition[] getQueries() {
		return queries;
	}

	/**
	 * Set the queries
	 *
	 * @param queries The queries to set.
	 */
	public void setQueries(IDataQueryDefinition[] queries) {
		this.queries = queries;
	}

	/**
	 * @return Returns the query.
	 */
	public IDataQueryDefinition getQuery() {
		if (queries != null && queries.length > 0) {
			return queries[0];
		}
		return null;
	}

	/**
	 * @param query The query to set.
	 */
	public void setQueries(IBaseQueryDefinition query) {
		this.queries = new IBaseQueryDefinition[] { query };
	}

	/**
	 * @return Returns the onCreate.
	 */
	public Expression getOnCreate() {
		return onCreate;
	}

	/**
	 * The onCreate script to set
	 *
	 * @param expr expression of onCreate
	 */
	public void setOnCreate(Expression expr) {
		onCreate = expr;
	}

	/**
	 * Get the onRender script
	 *
	 * @return Returns the onRender script
	 */
	public Expression getOnRender() {
		return onRender;
	}

	/**
	 * Set the onPageBreak script
	 *
	 * @param expr expression of onPageBreak
	 */
	public void setOnPageBreak(Expression expr) {
		onPageBreak = expr;
	}

	/**
	 * @return Returns the onPageBreak.
	 */
	public Expression getOnPageBreak() {
		return onPageBreak;
	}

	/**
	 * Set the onRender script
	 *
	 * @param expr expression of onRender
	 */
	public void setOnRender(Expression expr) {
		onRender = expr;
	}

	/**
	 * @return Returns the visibility.
	 */
	public VisibilityDesign getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility The visibility to set.
	 */
	public void setVisibility(VisibilityDesign visibility) {
		this.visibility = visibility;
	}

	/**
	 * @return Returns the action.
	 */
	public ActionDesign getAction() {
		return action;
	}

	/**
	 * @param action The action to set.
	 */
	public void setAction(ActionDesign action) {
		this.action = action;
	}

	/**
	 * Set the use cache result
	 *
	 * @param useCachedResult use cache
	 */
	public void setUseCachedResult(boolean useCachedResult) {
		this.useCachedResult = useCachedResult;
	}

	/**
	 * Is use cached result active
	 *
	 * @return Return the flag of cache result usage
	 */
	public boolean useCachedResult() {
		return useCachedResult;
	}

	/**
	 * Set expression style
	 *
	 * @param propertyIndex property index
	 * @param value         expression of the style
	 */
	public void setExpressionStyle(int propertyIndex, Expression value) {
		if (expressionStyles == null) {
			expressionStyles = new HashMap<>();
		}
		expressionStyles.put(propertyIndex, value);
	}

	/**
	 * Get the expression style
	 *
	 * @return Return the expression style
	 */
	public Map<Integer, Expression> getExpressionStyles() {
		return expressionStyles;
	}

	/**
	 * Set expression data
	 *
	 * @param extensionData expression data
	 */
	public void setExtensionData(Object extensionData) {
		this.extensionData = extensionData;
	}

	/**
	 * Get the expression data
	 *
	 * @return Return the expression data
	 */
	public Object getExtensionData() {
		return this.extensionData;
	}

	/**
	 * Get the alternative text
	 *
	 * @return Return the alternative text
	 */
	public Expression getAltText() {
		return altText;
	}

	/**
	 * Set the alternative text
	 *
	 * @param altText alternative text
	 */
	public void setAltText(Expression altText) {
		this.altText = altText;
	}

	/**
	 * @return Returns the altTextKey.
	 */
	public String getAltTextKey() {
		return altTextKey;
	}

	/**
	 * Set the alternative text key
	 *
	 * @param altTextKey alternative text key
	 */
	public void setAltTextKey(String altTextKey) {
		this.altTextKey = altTextKey;
	}

}
