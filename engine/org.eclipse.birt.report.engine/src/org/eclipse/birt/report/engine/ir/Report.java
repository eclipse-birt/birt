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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.parser.EngineIRTransferV213;
import org.eclipse.birt.report.engine.parser.MultiViewEngineIRVisitor;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.w3c.dom.css.CSSStyleDeclaration;

/**
 * Report is the root element of the design.
 * 
 */
public class Report {

	/**
	 * report design get from Model
	 */
	protected ReportDesignHandle reportDesign;

	/**
	 * styles used in this report
	 */
	protected ArrayList styles = new ArrayList();

	/**
	 * style-name mapping table
	 */
	protected HashMap styleTable = new HashMap();

	/**
	 * the name of Report root style
	 */
	protected String rootStyleName;

	/**
	 * queries used by this report.
	 * 
	 * @see org.eclipse.birt.report.engine.anlyzer.IQueryDefinition
	 */
	protected ArrayList queries = new ArrayList();

	/**
	 * use to find the query IDs.(query, string) pair.
	 */
	protected HashMap queryIDs = new HashMap();

	/**
	 * use to find the result MetaData.(query, ResultMetaData) pair.
	 */
	protected HashMap resultMetaData = new HashMap();

	/**
	 * Page setup this report used
	 */
	protected PageSetupDesign pageSetup = new PageSetupDesign();

	/**
	 * Report body
	 */
	protected ArrayList<ReportItemDesign> contents = new ArrayList<ReportItemDesign>();

	protected Map<String, Expression> userProperties;

	protected Map mapReportItemIDtoInstance;

	/*
	 * map report item to query
	 */
	protected HashMap mapReportItemToQuery;

	/*
	 * map query to report element handle
	 */
	protected HashMap mapQueryToReportElementHandle;

	/**
	 * css engine used in this
	 */
	protected CSSEngine cssEngine;

	private IStyle rootStyle;

	private ArrayList<PageVariableDesign> pageVariables = new ArrayList<PageVariableDesign>();
	private Expression onPageStart;
	private Expression onPageEnd;

	private String scriptLanguage = Expression.SCRIPT_JAVASCRIPT;

	private String reportLocale;

	private String javaClass;

	/**
	 * default constructor.
	 */
	public Report() {
		cssEngine = new BIRTCSSEngine();
	}

	public CSSEngine getCSSEngine() {
		return cssEngine;
	}

	/**
	 * @return Returns the javaClass.
	 */
	public String getJavaClass() {
		return javaClass;
	}

	/**
	 * @param javaClass The javaClass to set.
	 */
	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}

	/**
	 * return the map from report item to query
	 * 
	 * @return the map from report item to query
	 */
	public HashMap getReportItemToQueryMap() {
		if (mapReportItemToQuery == null) {
			mapReportItemToQuery = new HashMap();
		}
		return mapReportItemToQuery;
	}

	/**
	 * set query to report item
	 * 
	 * @param reportItem the report item
	 * @param query      query definition
	 */
	public void setQueryToReportHandle(ReportElementHandle handle, IDataQueryDefinition[] queries) {
		if (mapQueryToReportElementHandle == null) {
			mapQueryToReportElementHandle = new HashMap();
		}
		mapQueryToReportElementHandle.put(handle, queries);
	}

	/**
	 * get query by report item
	 * 
	 * @param reportItem the report item
	 * @param query      query definition
	 */
	public IDataQueryDefinition[] getQueryByReportHandle(ReportElementHandle handle) {
		if (mapQueryToReportElementHandle != null) {
			return (IDataQueryDefinition[]) mapQueryToReportElementHandle.get(handle);
		}
		return null;
	}

	/**
	 * set report item id to report item instance
	 * 
	 * @param id      the report item component id
	 * @param rptItem the report item
	 */
	public void setReportItemInstanceID(long id, ReportElementDesign rptElement) {
		if (mapReportItemIDtoInstance == null) {
			mapReportItemIDtoInstance = new HashMap();
		}
		mapReportItemIDtoInstance.put(Long.valueOf(id), rptElement);
	}

	/**
	 * return the report item with the specific component ID
	 * 
	 * @param id the component id
	 * @return the report item instance
	 */
	public ReportElementDesign getReportItemByID(long id) {
		assert mapReportItemIDtoInstance != null;
		return (ReportElementDesign) mapReportItemIDtoInstance.get(Long.valueOf(id));
	}

	/**
	 * return the named expression defined on the report
	 * 
	 * @return
	 */
	public Map<String, Expression> getUserProperties() {
		return userProperties;
	}

	public void setUserProperties(Map<String, Expression> userProperties) {
		this.userProperties = userProperties;
	}

	/**
	 * set the report's page setup
	 * 
	 * @param pageSetup page setup
	 */
	public void setPageSetup(PageSetupDesign pageSetup) {
		this.pageSetup = pageSetup;
	}

	/**
	 * get the report's page setup
	 * 
	 * @return page setup of this report
	 */
	public PageSetupDesign getPageSetup() {
		return this.pageSetup;
	}

	public Collection<ReportItemDesign> getContents() {
		return contents;
	}

	/**
	 * get contents count in a report.
	 * 
	 * @return content count
	 */
	public int getContentCount() {
		return this.contents.size();
	}

	/**
	 * get content at index.
	 * 
	 * @param index content index
	 * @return content
	 */
	public ReportItemDesign getContent(int index) {
		assert (index >= 0 && index < this.contents.size());
		return (ReportItemDesign) this.contents.get(index);
	}

	/**
	 * add content in to report body.
	 * 
	 * @param item content to be added.
	 */
	public void addContent(ReportItemDesign item) {
		this.contents.add(item);
	}

	/**
	 * get the style.
	 * 
	 * @param index style index
	 * @return style
	 */
	public Map getStyles() {
		return styleTable;
	}

	/**
	 * add a style definition into the report.
	 * 
	 * @param style style definition.
	 */
	public void addStyle(String name, CSSStyleDeclaration style) {
		assert (style != null);
		this.styles.add(style);
		this.styleTable.put(name, style);
	}

	/**
	 * Finds the style in the report.
	 * 
	 * @param name The name of the style.
	 * @return The corresponding <code>StyleDesign</code> object.
	 */
	public IStyle findStyle(String name) {
		if (name == null) {
			return null;
		}
		return (IStyle) this.styleTable.get(name);
	}

	/**
	 * Finds a master page with given name.
	 * 
	 * @param name The name of the master page to locate.
	 * @return A <code>MasterPageDesign</code> object that describes the master
	 *         page, or <code>null</code> if no master page of the given name is
	 *         found.
	 */
	public MasterPageDesign findMasterPage(String name) {
		assert (name != null);
		return this.pageSetup.findMasterPage(name);
	}

	/**
	 * @return Returns the reportDesign.
	 */
	public ReportDesignHandle getReportDesign() {
		return reportDesign;
	}

	/**
	 * @param reportDesign The reportDesign to set.
	 */
	public void setReportDesign(ReportDesignHandle reportDesign) {
		this.reportDesign = reportDesign;
	}

	/**
	 * get queries used in this report.
	 * 
	 * @see org.eclipse.birt.report.engine.analysis.IReportQuery
	 * @return the list of the query
	 */
	public ArrayList getQueries() {
		return this.queries;
	}

	public HashMap getQueryIDs() {
		return this.queryIDs;
	}

	public HashMap getResultMetaData() {
		return this.resultMetaData;
	}

	/**
	 * the name of Report root style
	 */
	public String getRootStyleName() {
		return rootStyleName;
	}

	public void setRootStyleName(String rootStyleName) {
		this.rootStyleName = rootStyleName;
	}

	public List getErrors() {
		return this.reportDesign.getErrorList();
	}

	public ReportItemDesign findDesign(ReportElementHandle handle) {
		ReportElementDesign elementDesign = getReportItemByID(handle.getID());
		if (elementDesign != null) {
			return (ReportItemDesign) elementDesign;
		} else {
			return new MultiViewEngineIRVisitor(reportDesign).translate(handle, this);
		}
	}

	/**
	 * the BIRT vesion used to generate the IR.
	 */
	protected String version;

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void updateVersion(String version) {
		if (version == null) {
			return;
		}

		if (version.equals(this.version)) {
			return;
		}

		if (version.equals(ReportDocumentConstants.BIRT_ENGINE_VERSION_2_1_3)) {
			new EngineIRTransferV213(reportDesign, this).transfer();
			return;
		}

	}

	public Collection<PageVariableDesign> getPageVariables() {
		return pageVariables;
	}

	public Expression getOnPageStart() {
		return onPageStart;
	}

	public void setOnPageStart(Expression onPageStart) {
		this.onPageStart = onPageStart;
	}

	public Expression getOnPageEnd() {
		return onPageEnd;
	}

	public void setOnPageEnd(Expression onPageEnd) {
		this.onPageEnd = onPageEnd;
	}

	public String getScriptLanguage() {
		return scriptLanguage;
	}

	public void setScriptLanguage(String defaultScript) {
		this.scriptLanguage = defaultScript;
	}

	public String getLocale() {
		return reportLocale;
	}

	public void setLocale(String loc) {
		reportLocale = loc;
	}
}
