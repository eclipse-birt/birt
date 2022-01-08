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
	 * @param visitor
	 */
	abstract public Object accept(IReportItemVisitor visitor, Object value);

	public Expression getTOC() {
		return toc;
	}

	public void setTOC(Expression expr) {
		this.toc = expr;
	}

	/**
	 * @return Returns the boo-kmark.
	 */
	public Expression getBookmark() {
		return bookmark;
	}

	/**
	 * @param bookmark The book-mark to set.
	 */
	public void setBookmark(Expression bookmark) {
		this.bookmark = bookmark;
	}

	/**
	 * @return Returns the queries.
	 */
	public IDataQueryDefinition[] getQueries() {
		return queries;
	}

	/**
	 * @param query The queries to set.
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
	 * @param onCreate The onCreate to set.
	 */
	public void setOnCreate(Expression expr) {
		onCreate = expr;
	}

	/**
	 * @return Returns the onRender.
	 */
	public Expression getOnRender() {
		return onRender;
	}

	/**
	 * @param onPageBreak The onPageBreak to set.
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
	 * @param onRender The onRender to set.
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

	public void setUseCachedResult(boolean useCachedResult) {
		this.useCachedResult = useCachedResult;
	}

	public boolean useCachedResult() {
		return useCachedResult;
	}

	public void setExpressionStyle(int propertyIndex, Expression value) {
		if (expressionStyles == null) {
			expressionStyles = new HashMap<Integer, Expression>();
		}
		expressionStyles.put(propertyIndex, value);
	}

	public Map<Integer, Expression> getExpressionStyles() {
		return expressionStyles;
	}

	public void setExtensionData(Object extensionData) {
		this.extensionData = extensionData;
	}

	public Object getExtensionData() {
		return this.extensionData;
	}

	public Expression getAltText() {
		return altText;
	}

	public void setAltText(Expression altText) {
		this.altText = altText;
	}

	/**
	 * @return Returns the altTextKey.
	 */
	public String getAltTextKey() {
		return altTextKey;
	}

	public void setAltTextKey(String altTextKey) {
		this.altTextKey = altTextKey;
	}

}
