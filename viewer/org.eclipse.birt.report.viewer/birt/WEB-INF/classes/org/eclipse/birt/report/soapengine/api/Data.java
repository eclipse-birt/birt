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
/**
 * Data.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Data implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.Font font;
	private org.eclipse.birt.report.soapengine.api.ResultSets resultSets;
	private org.eclipse.birt.report.soapengine.api.Format format;
	private org.eclipse.birt.report.soapengine.api.TOC TOC;
	private org.eclipse.birt.report.soapengine.api.Export export;
	private org.eclipse.birt.report.soapengine.api.CascadeParameter cascadeParameter;
	private org.eclipse.birt.report.soapengine.api.Filter filter;
	private org.eclipse.birt.report.soapengine.api.FilterList filterList;
	private org.eclipse.birt.report.soapengine.api.Vector columnValues;
	private org.eclipse.birt.report.soapengine.api.Page page;
	private org.eclipse.birt.report.soapengine.api.DataSetList dataSets;
	private org.eclipse.birt.report.soapengine.api.ReportElementList reportElementList;
	private org.eclipse.birt.report.soapengine.api.ThemeList themes;
	private org.eclipse.birt.report.soapengine.api.ColumnDefinitionGroup columnDefs;
	private org.eclipse.birt.report.soapengine.api.GroupDetail groupDetail;
	private org.eclipse.birt.report.soapengine.api.CellDefinition cellDef;
	private org.eclipse.birt.report.soapengine.api.SectionDefinition sectionDef;
	private org.eclipse.birt.report.soapengine.api.ChartProperties chart;
	private java.lang.String labelText;
	private org.eclipse.birt.report.soapengine.api.FileBrowsing fileBrowsing;
	private org.eclipse.birt.report.soapengine.api.DataSourceList dataSources;
	private org.eclipse.birt.report.soapengine.api.BindingList bindingList;
	private org.eclipse.birt.report.soapengine.api.DataSet dataSetDef;
	private org.eclipse.birt.report.soapengine.api.DataSetList dataSetsToUse;
	private org.eclipse.birt.report.soapengine.api.DesignState designState;
	private org.eclipse.birt.report.soapengine.api.ToolbarState toolbarState;
	private java.lang.String redirectURL;
	private java.lang.String popupURL;
	private org.eclipse.birt.report.soapengine.api.BRDExpression BRDExpression;
	private org.eclipse.birt.report.soapengine.api.JoinMetadata joinMetadata;
	private org.eclipse.birt.report.soapengine.api.BoundDataColumnList boundDataColumnList;
	private org.eclipse.birt.report.soapengine.api.IOInfoList IOInfoList;
	private org.eclipse.birt.report.soapengine.api.TableColContextMenuState tableColContextMenu;
	private org.eclipse.birt.report.soapengine.api.TableSectionContextMenuState tableSectContextMenu;
	private org.eclipse.birt.report.soapengine.api.TableContextMenuState tableContextMenu;
	private java.lang.String confirmation;
	private org.eclipse.birt.report.soapengine.api.TableLayoutList tableLayoutList;
	private org.eclipse.birt.report.soapengine.api.AvailableOperation availableOperation;
	private org.eclipse.birt.report.soapengine.api.SortDefinitionList sortDefinitionList;

	public Data() {
	}

	public Data(org.eclipse.birt.report.soapengine.api.Font font,
			org.eclipse.birt.report.soapengine.api.ResultSets resultSets,
			org.eclipse.birt.report.soapengine.api.Format format, org.eclipse.birt.report.soapengine.api.TOC TOC,
			org.eclipse.birt.report.soapengine.api.Export export,
			org.eclipse.birt.report.soapengine.api.CascadeParameter cascadeParameter,
			org.eclipse.birt.report.soapengine.api.Filter filter,
			org.eclipse.birt.report.soapengine.api.FilterList filterList,
			org.eclipse.birt.report.soapengine.api.Vector columnValues,
			org.eclipse.birt.report.soapengine.api.Page page,
			org.eclipse.birt.report.soapengine.api.DataSetList dataSets,
			org.eclipse.birt.report.soapengine.api.ReportElementList reportElementList,
			org.eclipse.birt.report.soapengine.api.ThemeList themes,
			org.eclipse.birt.report.soapengine.api.ColumnDefinitionGroup columnDefs,
			org.eclipse.birt.report.soapengine.api.GroupDetail groupDetail,
			org.eclipse.birt.report.soapengine.api.CellDefinition cellDef,
			org.eclipse.birt.report.soapengine.api.SectionDefinition sectionDef,
			org.eclipse.birt.report.soapengine.api.ChartProperties chart, java.lang.String labelText,
			org.eclipse.birt.report.soapengine.api.FileBrowsing fileBrowsing,
			org.eclipse.birt.report.soapengine.api.DataSourceList dataSources,
			org.eclipse.birt.report.soapengine.api.BindingList bindingList,
			org.eclipse.birt.report.soapengine.api.DataSet dataSetDef,
			org.eclipse.birt.report.soapengine.api.DataSetList dataSetsToUse,
			org.eclipse.birt.report.soapengine.api.DesignState designState,
			org.eclipse.birt.report.soapengine.api.ToolbarState toolbarState, java.lang.String redirectURL,
			java.lang.String popupURL, org.eclipse.birt.report.soapengine.api.BRDExpression BRDExpression,
			org.eclipse.birt.report.soapengine.api.JoinMetadata joinMetadata,
			org.eclipse.birt.report.soapengine.api.BoundDataColumnList boundDataColumnList,
			org.eclipse.birt.report.soapengine.api.IOInfoList IOInfoList,
			org.eclipse.birt.report.soapengine.api.TableColContextMenuState tableColContextMenu,
			org.eclipse.birt.report.soapengine.api.TableSectionContextMenuState tableSectContextMenu,
			org.eclipse.birt.report.soapengine.api.TableContextMenuState tableContextMenu,
			java.lang.String confirmation, org.eclipse.birt.report.soapengine.api.TableLayoutList tableLayoutList,
			org.eclipse.birt.report.soapengine.api.AvailableOperation availableOperation,
			org.eclipse.birt.report.soapengine.api.SortDefinitionList sortDefinitionList) {
		this.font = font;
		this.resultSets = resultSets;
		this.format = format;
		this.TOC = TOC;
		this.export = export;
		this.cascadeParameter = cascadeParameter;
		this.filter = filter;
		this.filterList = filterList;
		this.columnValues = columnValues;
		this.page = page;
		this.dataSets = dataSets;
		this.reportElementList = reportElementList;
		this.themes = themes;
		this.columnDefs = columnDefs;
		this.groupDetail = groupDetail;
		this.cellDef = cellDef;
		this.sectionDef = sectionDef;
		this.chart = chart;
		this.labelText = labelText;
		this.fileBrowsing = fileBrowsing;
		this.dataSources = dataSources;
		this.bindingList = bindingList;
		this.dataSetDef = dataSetDef;
		this.dataSetsToUse = dataSetsToUse;
		this.designState = designState;
		this.toolbarState = toolbarState;
		this.redirectURL = redirectURL;
		this.popupURL = popupURL;
		this.BRDExpression = BRDExpression;
		this.joinMetadata = joinMetadata;
		this.boundDataColumnList = boundDataColumnList;
		this.IOInfoList = IOInfoList;
		this.tableColContextMenu = tableColContextMenu;
		this.tableSectContextMenu = tableSectContextMenu;
		this.tableContextMenu = tableContextMenu;
		this.confirmation = confirmation;
		this.tableLayoutList = tableLayoutList;
		this.availableOperation = availableOperation;
		this.sortDefinitionList = sortDefinitionList;
	}

	/**
	 * Gets the font value for this Data.
	 * 
	 * @return font
	 */
	public org.eclipse.birt.report.soapengine.api.Font getFont() {
		return font;
	}

	/**
	 * Sets the font value for this Data.
	 * 
	 * @param font
	 */
	public void setFont(org.eclipse.birt.report.soapengine.api.Font font) {
		this.font = font;
	}

	/**
	 * Gets the resultSets value for this Data.
	 * 
	 * @return resultSets
	 */
	public org.eclipse.birt.report.soapengine.api.ResultSets getResultSets() {
		return resultSets;
	}

	/**
	 * Sets the resultSets value for this Data.
	 * 
	 * @param resultSets
	 */
	public void setResultSets(org.eclipse.birt.report.soapengine.api.ResultSets resultSets) {
		this.resultSets = resultSets;
	}

	/**
	 * Gets the format value for this Data.
	 * 
	 * @return format
	 */
	public org.eclipse.birt.report.soapengine.api.Format getFormat() {
		return format;
	}

	/**
	 * Sets the format value for this Data.
	 * 
	 * @param format
	 */
	public void setFormat(org.eclipse.birt.report.soapengine.api.Format format) {
		this.format = format;
	}

	/**
	 * Gets the TOC value for this Data.
	 * 
	 * @return TOC
	 */
	public org.eclipse.birt.report.soapengine.api.TOC getTOC() {
		return TOC;
	}

	/**
	 * Sets the TOC value for this Data.
	 * 
	 * @param TOC
	 */
	public void setTOC(org.eclipse.birt.report.soapengine.api.TOC TOC) {
		this.TOC = TOC;
	}

	/**
	 * Gets the export value for this Data.
	 * 
	 * @return export
	 */
	public org.eclipse.birt.report.soapengine.api.Export getExport() {
		return export;
	}

	/**
	 * Sets the export value for this Data.
	 * 
	 * @param export
	 */
	public void setExport(org.eclipse.birt.report.soapengine.api.Export export) {
		this.export = export;
	}

	/**
	 * Gets the cascadeParameter value for this Data.
	 * 
	 * @return cascadeParameter
	 */
	public org.eclipse.birt.report.soapengine.api.CascadeParameter getCascadeParameter() {
		return cascadeParameter;
	}

	/**
	 * Sets the cascadeParameter value for this Data.
	 * 
	 * @param cascadeParameter
	 */
	public void setCascadeParameter(org.eclipse.birt.report.soapengine.api.CascadeParameter cascadeParameter) {
		this.cascadeParameter = cascadeParameter;
	}

	/**
	 * Gets the filter value for this Data.
	 * 
	 * @return filter
	 */
	public org.eclipse.birt.report.soapengine.api.Filter getFilter() {
		return filter;
	}

	/**
	 * Sets the filter value for this Data.
	 * 
	 * @param filter
	 */
	public void setFilter(org.eclipse.birt.report.soapengine.api.Filter filter) {
		this.filter = filter;
	}

	/**
	 * Gets the filterList value for this Data.
	 * 
	 * @return filterList
	 */
	public org.eclipse.birt.report.soapengine.api.FilterList getFilterList() {
		return filterList;
	}

	/**
	 * Sets the filterList value for this Data.
	 * 
	 * @param filterList
	 */
	public void setFilterList(org.eclipse.birt.report.soapengine.api.FilterList filterList) {
		this.filterList = filterList;
	}

	/**
	 * Gets the columnValues value for this Data.
	 * 
	 * @return columnValues
	 */
	public org.eclipse.birt.report.soapengine.api.Vector getColumnValues() {
		return columnValues;
	}

	/**
	 * Sets the columnValues value for this Data.
	 * 
	 * @param columnValues
	 */
	public void setColumnValues(org.eclipse.birt.report.soapengine.api.Vector columnValues) {
		this.columnValues = columnValues;
	}

	/**
	 * Gets the page value for this Data.
	 * 
	 * @return page
	 */
	public org.eclipse.birt.report.soapengine.api.Page getPage() {
		return page;
	}

	/**
	 * Sets the page value for this Data.
	 * 
	 * @param page
	 */
	public void setPage(org.eclipse.birt.report.soapengine.api.Page page) {
		this.page = page;
	}

	/**
	 * Gets the dataSets value for this Data.
	 * 
	 * @return dataSets
	 */
	public org.eclipse.birt.report.soapengine.api.DataSetList getDataSets() {
		return dataSets;
	}

	/**
	 * Sets the dataSets value for this Data.
	 * 
	 * @param dataSets
	 */
	public void setDataSets(org.eclipse.birt.report.soapengine.api.DataSetList dataSets) {
		this.dataSets = dataSets;
	}

	/**
	 * Gets the reportElementList value for this Data.
	 * 
	 * @return reportElementList
	 */
	public org.eclipse.birt.report.soapengine.api.ReportElementList getReportElementList() {
		return reportElementList;
	}

	/**
	 * Sets the reportElementList value for this Data.
	 * 
	 * @param reportElementList
	 */
	public void setReportElementList(org.eclipse.birt.report.soapengine.api.ReportElementList reportElementList) {
		this.reportElementList = reportElementList;
	}

	/**
	 * Gets the themes value for this Data.
	 * 
	 * @return themes
	 */
	public org.eclipse.birt.report.soapengine.api.ThemeList getThemes() {
		return themes;
	}

	/**
	 * Sets the themes value for this Data.
	 * 
	 * @param themes
	 */
	public void setThemes(org.eclipse.birt.report.soapengine.api.ThemeList themes) {
		this.themes = themes;
	}

	/**
	 * Gets the columnDefs value for this Data.
	 * 
	 * @return columnDefs
	 */
	public org.eclipse.birt.report.soapengine.api.ColumnDefinitionGroup getColumnDefs() {
		return columnDefs;
	}

	/**
	 * Sets the columnDefs value for this Data.
	 * 
	 * @param columnDefs
	 */
	public void setColumnDefs(org.eclipse.birt.report.soapengine.api.ColumnDefinitionGroup columnDefs) {
		this.columnDefs = columnDefs;
	}

	/**
	 * Gets the groupDetail value for this Data.
	 * 
	 * @return groupDetail
	 */
	public org.eclipse.birt.report.soapengine.api.GroupDetail getGroupDetail() {
		return groupDetail;
	}

	/**
	 * Sets the groupDetail value for this Data.
	 * 
	 * @param groupDetail
	 */
	public void setGroupDetail(org.eclipse.birt.report.soapengine.api.GroupDetail groupDetail) {
		this.groupDetail = groupDetail;
	}

	/**
	 * Gets the cellDef value for this Data.
	 * 
	 * @return cellDef
	 */
	public org.eclipse.birt.report.soapengine.api.CellDefinition getCellDef() {
		return cellDef;
	}

	/**
	 * Sets the cellDef value for this Data.
	 * 
	 * @param cellDef
	 */
	public void setCellDef(org.eclipse.birt.report.soapengine.api.CellDefinition cellDef) {
		this.cellDef = cellDef;
	}

	/**
	 * Gets the sectionDef value for this Data.
	 * 
	 * @return sectionDef
	 */
	public org.eclipse.birt.report.soapengine.api.SectionDefinition getSectionDef() {
		return sectionDef;
	}

	/**
	 * Sets the sectionDef value for this Data.
	 * 
	 * @param sectionDef
	 */
	public void setSectionDef(org.eclipse.birt.report.soapengine.api.SectionDefinition sectionDef) {
		this.sectionDef = sectionDef;
	}

	/**
	 * Gets the chart value for this Data.
	 * 
	 * @return chart
	 */
	public org.eclipse.birt.report.soapengine.api.ChartProperties getChart() {
		return chart;
	}

	/**
	 * Sets the chart value for this Data.
	 * 
	 * @param chart
	 */
	public void setChart(org.eclipse.birt.report.soapengine.api.ChartProperties chart) {
		this.chart = chart;
	}

	/**
	 * Gets the labelText value for this Data.
	 * 
	 * @return labelText
	 */
	public java.lang.String getLabelText() {
		return labelText;
	}

	/**
	 * Sets the labelText value for this Data.
	 * 
	 * @param labelText
	 */
	public void setLabelText(java.lang.String labelText) {
		this.labelText = labelText;
	}

	/**
	 * Gets the fileBrowsing value for this Data.
	 * 
	 * @return fileBrowsing
	 */
	public org.eclipse.birt.report.soapengine.api.FileBrowsing getFileBrowsing() {
		return fileBrowsing;
	}

	/**
	 * Sets the fileBrowsing value for this Data.
	 * 
	 * @param fileBrowsing
	 */
	public void setFileBrowsing(org.eclipse.birt.report.soapengine.api.FileBrowsing fileBrowsing) {
		this.fileBrowsing = fileBrowsing;
	}

	/**
	 * Gets the dataSources value for this Data.
	 * 
	 * @return dataSources
	 */
	public org.eclipse.birt.report.soapengine.api.DataSourceList getDataSources() {
		return dataSources;
	}

	/**
	 * Sets the dataSources value for this Data.
	 * 
	 * @param dataSources
	 */
	public void setDataSources(org.eclipse.birt.report.soapengine.api.DataSourceList dataSources) {
		this.dataSources = dataSources;
	}

	/**
	 * Gets the bindingList value for this Data.
	 * 
	 * @return bindingList
	 */
	public org.eclipse.birt.report.soapengine.api.BindingList getBindingList() {
		return bindingList;
	}

	/**
	 * Sets the bindingList value for this Data.
	 * 
	 * @param bindingList
	 */
	public void setBindingList(org.eclipse.birt.report.soapengine.api.BindingList bindingList) {
		this.bindingList = bindingList;
	}

	/**
	 * Gets the dataSetDef value for this Data.
	 * 
	 * @return dataSetDef
	 */
	public org.eclipse.birt.report.soapengine.api.DataSet getDataSetDef() {
		return dataSetDef;
	}

	/**
	 * Sets the dataSetDef value for this Data.
	 * 
	 * @param dataSetDef
	 */
	public void setDataSetDef(org.eclipse.birt.report.soapengine.api.DataSet dataSetDef) {
		this.dataSetDef = dataSetDef;
	}

	/**
	 * Gets the dataSetsToUse value for this Data.
	 * 
	 * @return dataSetsToUse
	 */
	public org.eclipse.birt.report.soapengine.api.DataSetList getDataSetsToUse() {
		return dataSetsToUse;
	}

	/**
	 * Sets the dataSetsToUse value for this Data.
	 * 
	 * @param dataSetsToUse
	 */
	public void setDataSetsToUse(org.eclipse.birt.report.soapengine.api.DataSetList dataSetsToUse) {
		this.dataSetsToUse = dataSetsToUse;
	}

	/**
	 * Gets the designState value for this Data.
	 * 
	 * @return designState
	 */
	public org.eclipse.birt.report.soapengine.api.DesignState getDesignState() {
		return designState;
	}

	/**
	 * Sets the designState value for this Data.
	 * 
	 * @param designState
	 */
	public void setDesignState(org.eclipse.birt.report.soapengine.api.DesignState designState) {
		this.designState = designState;
	}

	/**
	 * Gets the toolbarState value for this Data.
	 * 
	 * @return toolbarState
	 */
	public org.eclipse.birt.report.soapengine.api.ToolbarState getToolbarState() {
		return toolbarState;
	}

	/**
	 * Sets the toolbarState value for this Data.
	 * 
	 * @param toolbarState
	 */
	public void setToolbarState(org.eclipse.birt.report.soapengine.api.ToolbarState toolbarState) {
		this.toolbarState = toolbarState;
	}

	/**
	 * Gets the redirectURL value for this Data.
	 * 
	 * @return redirectURL
	 */
	public java.lang.String getRedirectURL() {
		return redirectURL;
	}

	/**
	 * Sets the redirectURL value for this Data.
	 * 
	 * @param redirectURL
	 */
	public void setRedirectURL(java.lang.String redirectURL) {
		this.redirectURL = redirectURL;
	}

	/**
	 * Gets the popupURL value for this Data.
	 * 
	 * @return popupURL
	 */
	public java.lang.String getPopupURL() {
		return popupURL;
	}

	/**
	 * Sets the popupURL value for this Data.
	 * 
	 * @param popupURL
	 */
	public void setPopupURL(java.lang.String popupURL) {
		this.popupURL = popupURL;
	}

	/**
	 * Gets the BRDExpression value for this Data.
	 * 
	 * @return BRDExpression
	 */
	public org.eclipse.birt.report.soapengine.api.BRDExpression getBRDExpression() {
		return BRDExpression;
	}

	/**
	 * Sets the BRDExpression value for this Data.
	 * 
	 * @param BRDExpression
	 */
	public void setBRDExpression(org.eclipse.birt.report.soapengine.api.BRDExpression BRDExpression) {
		this.BRDExpression = BRDExpression;
	}

	/**
	 * Gets the joinMetadata value for this Data.
	 * 
	 * @return joinMetadata
	 */
	public org.eclipse.birt.report.soapengine.api.JoinMetadata getJoinMetadata() {
		return joinMetadata;
	}

	/**
	 * Sets the joinMetadata value for this Data.
	 * 
	 * @param joinMetadata
	 */
	public void setJoinMetadata(org.eclipse.birt.report.soapengine.api.JoinMetadata joinMetadata) {
		this.joinMetadata = joinMetadata;
	}

	/**
	 * Gets the boundDataColumnList value for this Data.
	 * 
	 * @return boundDataColumnList
	 */
	public org.eclipse.birt.report.soapengine.api.BoundDataColumnList getBoundDataColumnList() {
		return boundDataColumnList;
	}

	/**
	 * Sets the boundDataColumnList value for this Data.
	 * 
	 * @param boundDataColumnList
	 */
	public void setBoundDataColumnList(org.eclipse.birt.report.soapengine.api.BoundDataColumnList boundDataColumnList) {
		this.boundDataColumnList = boundDataColumnList;
	}

	/**
	 * Gets the IOInfoList value for this Data.
	 * 
	 * @return IOInfoList
	 */
	public org.eclipse.birt.report.soapengine.api.IOInfoList getIOInfoList() {
		return IOInfoList;
	}

	/**
	 * Sets the IOInfoList value for this Data.
	 * 
	 * @param IOInfoList
	 */
	public void setIOInfoList(org.eclipse.birt.report.soapengine.api.IOInfoList IOInfoList) {
		this.IOInfoList = IOInfoList;
	}

	/**
	 * Gets the tableColContextMenu value for this Data.
	 * 
	 * @return tableColContextMenu
	 */
	public org.eclipse.birt.report.soapengine.api.TableColContextMenuState getTableColContextMenu() {
		return tableColContextMenu;
	}

	/**
	 * Sets the tableColContextMenu value for this Data.
	 * 
	 * @param tableColContextMenu
	 */
	public void setTableColContextMenu(
			org.eclipse.birt.report.soapengine.api.TableColContextMenuState tableColContextMenu) {
		this.tableColContextMenu = tableColContextMenu;
	}

	/**
	 * Gets the tableSectContextMenu value for this Data.
	 * 
	 * @return tableSectContextMenu
	 */
	public org.eclipse.birt.report.soapengine.api.TableSectionContextMenuState getTableSectContextMenu() {
		return tableSectContextMenu;
	}

	/**
	 * Sets the tableSectContextMenu value for this Data.
	 * 
	 * @param tableSectContextMenu
	 */
	public void setTableSectContextMenu(
			org.eclipse.birt.report.soapengine.api.TableSectionContextMenuState tableSectContextMenu) {
		this.tableSectContextMenu = tableSectContextMenu;
	}

	/**
	 * Gets the tableContextMenu value for this Data.
	 * 
	 * @return tableContextMenu
	 */
	public org.eclipse.birt.report.soapengine.api.TableContextMenuState getTableContextMenu() {
		return tableContextMenu;
	}

	/**
	 * Sets the tableContextMenu value for this Data.
	 * 
	 * @param tableContextMenu
	 */
	public void setTableContextMenu(org.eclipse.birt.report.soapengine.api.TableContextMenuState tableContextMenu) {
		this.tableContextMenu = tableContextMenu;
	}

	/**
	 * Gets the confirmation value for this Data.
	 * 
	 * @return confirmation
	 */
	public java.lang.String getConfirmation() {
		return confirmation;
	}

	/**
	 * Sets the confirmation value for this Data.
	 * 
	 * @param confirmation
	 */
	public void setConfirmation(java.lang.String confirmation) {
		this.confirmation = confirmation;
	}

	/**
	 * Gets the tableLayoutList value for this Data.
	 * 
	 * @return tableLayoutList
	 */
	public org.eclipse.birt.report.soapengine.api.TableLayoutList getTableLayoutList() {
		return tableLayoutList;
	}

	/**
	 * Sets the tableLayoutList value for this Data.
	 * 
	 * @param tableLayoutList
	 */
	public void setTableLayoutList(org.eclipse.birt.report.soapengine.api.TableLayoutList tableLayoutList) {
		this.tableLayoutList = tableLayoutList;
	}

	/**
	 * Gets the availableOperation value for this Data.
	 * 
	 * @return availableOperation
	 */
	public org.eclipse.birt.report.soapengine.api.AvailableOperation getAvailableOperation() {
		return availableOperation;
	}

	/**
	 * Sets the availableOperation value for this Data.
	 * 
	 * @param availableOperation
	 */
	public void setAvailableOperation(org.eclipse.birt.report.soapengine.api.AvailableOperation availableOperation) {
		this.availableOperation = availableOperation;
	}

	/**
	 * Gets the sortDefinitionList value for this Data.
	 * 
	 * @return sortDefinitionList
	 */
	public org.eclipse.birt.report.soapengine.api.SortDefinitionList getSortDefinitionList() {
		return sortDefinitionList;
	}

	/**
	 * Sets the sortDefinitionList value for this Data.
	 * 
	 * @param sortDefinitionList
	 */
	public void setSortDefinitionList(org.eclipse.birt.report.soapengine.api.SortDefinitionList sortDefinitionList) {
		this.sortDefinitionList = sortDefinitionList;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Data))
			return false;
		Data other = (Data) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.font == null && other.getFont() == null)
						|| (this.font != null && this.font.equals(other.getFont())))
				&& ((this.resultSets == null && other.getResultSets() == null)
						|| (this.resultSets != null && this.resultSets.equals(other.getResultSets())))
				&& ((this.format == null && other.getFormat() == null)
						|| (this.format != null && this.format.equals(other.getFormat())))
				&& ((this.TOC == null && other.getTOC() == null)
						|| (this.TOC != null && this.TOC.equals(other.getTOC())))
				&& ((this.export == null && other.getExport() == null)
						|| (this.export != null && this.export.equals(other.getExport())))
				&& ((this.cascadeParameter == null && other.getCascadeParameter() == null)
						|| (this.cascadeParameter != null && this.cascadeParameter.equals(other.getCascadeParameter())))
				&& ((this.filter == null && other.getFilter() == null)
						|| (this.filter != null && this.filter.equals(other.getFilter())))
				&& ((this.filterList == null && other.getFilterList() == null)
						|| (this.filterList != null && this.filterList.equals(other.getFilterList())))
				&& ((this.columnValues == null && other.getColumnValues() == null)
						|| (this.columnValues != null && this.columnValues.equals(other.getColumnValues())))
				&& ((this.page == null && other.getPage() == null)
						|| (this.page != null && this.page.equals(other.getPage())))
				&& ((this.dataSets == null && other.getDataSets() == null)
						|| (this.dataSets != null && this.dataSets.equals(other.getDataSets())))
				&& ((this.reportElementList == null && other.getReportElementList() == null)
						|| (this.reportElementList != null
								&& this.reportElementList.equals(other.getReportElementList())))
				&& ((this.themes == null && other.getThemes() == null)
						|| (this.themes != null && this.themes.equals(other.getThemes())))
				&& ((this.columnDefs == null && other.getColumnDefs() == null)
						|| (this.columnDefs != null && this.columnDefs.equals(other.getColumnDefs())))
				&& ((this.groupDetail == null && other.getGroupDetail() == null)
						|| (this.groupDetail != null && this.groupDetail.equals(other.getGroupDetail())))
				&& ((this.cellDef == null && other.getCellDef() == null)
						|| (this.cellDef != null && this.cellDef.equals(other.getCellDef())))
				&& ((this.sectionDef == null && other.getSectionDef() == null)
						|| (this.sectionDef != null && this.sectionDef.equals(other.getSectionDef())))
				&& ((this.chart == null && other.getChart() == null)
						|| (this.chart != null && this.chart.equals(other.getChart())))
				&& ((this.labelText == null && other.getLabelText() == null)
						|| (this.labelText != null && this.labelText.equals(other.getLabelText())))
				&& ((this.fileBrowsing == null && other.getFileBrowsing() == null)
						|| (this.fileBrowsing != null && this.fileBrowsing.equals(other.getFileBrowsing())))
				&& ((this.dataSources == null && other.getDataSources() == null)
						|| (this.dataSources != null && this.dataSources.equals(other.getDataSources())))
				&& ((this.bindingList == null && other.getBindingList() == null)
						|| (this.bindingList != null && this.bindingList.equals(other.getBindingList())))
				&& ((this.dataSetDef == null && other.getDataSetDef() == null)
						|| (this.dataSetDef != null && this.dataSetDef.equals(other.getDataSetDef())))
				&& ((this.dataSetsToUse == null && other.getDataSetsToUse() == null)
						|| (this.dataSetsToUse != null && this.dataSetsToUse.equals(other.getDataSetsToUse())))
				&& ((this.designState == null && other.getDesignState() == null)
						|| (this.designState != null && this.designState.equals(other.getDesignState())))
				&& ((this.toolbarState == null && other.getToolbarState() == null)
						|| (this.toolbarState != null && this.toolbarState.equals(other.getToolbarState())))
				&& ((this.redirectURL == null && other.getRedirectURL() == null)
						|| (this.redirectURL != null && this.redirectURL.equals(other.getRedirectURL())))
				&& ((this.popupURL == null && other.getPopupURL() == null)
						|| (this.popupURL != null && this.popupURL.equals(other.getPopupURL())))
				&& ((this.BRDExpression == null && other.getBRDExpression() == null)
						|| (this.BRDExpression != null && this.BRDExpression.equals(other.getBRDExpression())))
				&& ((this.joinMetadata == null && other.getJoinMetadata() == null)
						|| (this.joinMetadata != null && this.joinMetadata.equals(other.getJoinMetadata())))
				&& ((this.boundDataColumnList == null && other.getBoundDataColumnList() == null)
						|| (this.boundDataColumnList != null
								&& this.boundDataColumnList.equals(other.getBoundDataColumnList())))
				&& ((this.IOInfoList == null && other.getIOInfoList() == null)
						|| (this.IOInfoList != null && this.IOInfoList.equals(other.getIOInfoList())))
				&& ((this.tableColContextMenu == null && other.getTableColContextMenu() == null)
						|| (this.tableColContextMenu != null
								&& this.tableColContextMenu.equals(other.getTableColContextMenu())))
				&& ((this.tableSectContextMenu == null && other.getTableSectContextMenu() == null)
						|| (this.tableSectContextMenu != null
								&& this.tableSectContextMenu.equals(other.getTableSectContextMenu())))
				&& ((this.tableContextMenu == null && other.getTableContextMenu() == null)
						|| (this.tableContextMenu != null && this.tableContextMenu.equals(other.getTableContextMenu())))
				&& ((this.confirmation == null && other.getConfirmation() == null)
						|| (this.confirmation != null && this.confirmation.equals(other.getConfirmation())))
				&& ((this.tableLayoutList == null && other.getTableLayoutList() == null)
						|| (this.tableLayoutList != null && this.tableLayoutList.equals(other.getTableLayoutList())))
				&& ((this.availableOperation == null && other.getAvailableOperation() == null)
						|| (this.availableOperation != null
								&& this.availableOperation.equals(other.getAvailableOperation())))
				&& ((this.sortDefinitionList == null && other.getSortDefinitionList() == null)
						|| (this.sortDefinitionList != null
								&& this.sortDefinitionList.equals(other.getSortDefinitionList())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getFont() != null) {
			_hashCode += getFont().hashCode();
		}
		if (getResultSets() != null) {
			_hashCode += getResultSets().hashCode();
		}
		if (getFormat() != null) {
			_hashCode += getFormat().hashCode();
		}
		if (getTOC() != null) {
			_hashCode += getTOC().hashCode();
		}
		if (getExport() != null) {
			_hashCode += getExport().hashCode();
		}
		if (getCascadeParameter() != null) {
			_hashCode += getCascadeParameter().hashCode();
		}
		if (getFilter() != null) {
			_hashCode += getFilter().hashCode();
		}
		if (getFilterList() != null) {
			_hashCode += getFilterList().hashCode();
		}
		if (getColumnValues() != null) {
			_hashCode += getColumnValues().hashCode();
		}
		if (getPage() != null) {
			_hashCode += getPage().hashCode();
		}
		if (getDataSets() != null) {
			_hashCode += getDataSets().hashCode();
		}
		if (getReportElementList() != null) {
			_hashCode += getReportElementList().hashCode();
		}
		if (getThemes() != null) {
			_hashCode += getThemes().hashCode();
		}
		if (getColumnDefs() != null) {
			_hashCode += getColumnDefs().hashCode();
		}
		if (getGroupDetail() != null) {
			_hashCode += getGroupDetail().hashCode();
		}
		if (getCellDef() != null) {
			_hashCode += getCellDef().hashCode();
		}
		if (getSectionDef() != null) {
			_hashCode += getSectionDef().hashCode();
		}
		if (getChart() != null) {
			_hashCode += getChart().hashCode();
		}
		if (getLabelText() != null) {
			_hashCode += getLabelText().hashCode();
		}
		if (getFileBrowsing() != null) {
			_hashCode += getFileBrowsing().hashCode();
		}
		if (getDataSources() != null) {
			_hashCode += getDataSources().hashCode();
		}
		if (getBindingList() != null) {
			_hashCode += getBindingList().hashCode();
		}
		if (getDataSetDef() != null) {
			_hashCode += getDataSetDef().hashCode();
		}
		if (getDataSetsToUse() != null) {
			_hashCode += getDataSetsToUse().hashCode();
		}
		if (getDesignState() != null) {
			_hashCode += getDesignState().hashCode();
		}
		if (getToolbarState() != null) {
			_hashCode += getToolbarState().hashCode();
		}
		if (getRedirectURL() != null) {
			_hashCode += getRedirectURL().hashCode();
		}
		if (getPopupURL() != null) {
			_hashCode += getPopupURL().hashCode();
		}
		if (getBRDExpression() != null) {
			_hashCode += getBRDExpression().hashCode();
		}
		if (getJoinMetadata() != null) {
			_hashCode += getJoinMetadata().hashCode();
		}
		if (getBoundDataColumnList() != null) {
			_hashCode += getBoundDataColumnList().hashCode();
		}
		if (getIOInfoList() != null) {
			_hashCode += getIOInfoList().hashCode();
		}
		if (getTableColContextMenu() != null) {
			_hashCode += getTableColContextMenu().hashCode();
		}
		if (getTableSectContextMenu() != null) {
			_hashCode += getTableSectContextMenu().hashCode();
		}
		if (getTableContextMenu() != null) {
			_hashCode += getTableContextMenu().hashCode();
		}
		if (getConfirmation() != null) {
			_hashCode += getConfirmation().hashCode();
		}
		if (getTableLayoutList() != null) {
			_hashCode += getTableLayoutList().hashCode();
		}
		if (getAvailableOperation() != null) {
			_hashCode += getAvailableOperation().hashCode();
		}
		if (getSortDefinitionList() != null) {
			_hashCode += getSortDefinitionList().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Data.class,
			true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Data"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("font");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("resultSets");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ResultSets"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ResultSets"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("format");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("TOC");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("export");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("cascadeParameter");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CascadeParameter"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CascadeParameter"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("filter");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Filter"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Filter"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("filterList");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FilterList"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FilterList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("columnValues");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnValues"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("page");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Page"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Page"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataSets");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSets"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSetList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("reportElementList");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportElementList"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportElementList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("themes");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Themes"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ThemeList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("columnDefs");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDefs"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDefinitionGroup"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("groupDetail");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GroupDetail"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GroupDetail"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("cellDef");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CellDef"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CellDefinition"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("sectionDef");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SectionDef"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SectionDefinition"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("chart");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Chart"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChartProperties"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("labelText");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "LabelText"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("fileBrowsing");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FileBrowsing"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FileBrowsing"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataSources");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSources"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSourceList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("bindingList");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BindingList"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BindingList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataSetDef");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSetDef"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSet"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataSetsToUse");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSetsToUse"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSetList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("designState");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DesignState"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DesignState"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("toolbarState");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ToolbarState"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ToolbarState"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("redirectURL");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "RedirectURL"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("popupURL");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "PopupURL"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("BRDExpression");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BRDExpression"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BRDExpression"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("joinMetadata");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinMetadata"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinMetadata"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("boundDataColumnList");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BoundDataColumnList"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BoundDataColumnList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("IOInfoList");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOInfoList"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOInfoList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("tableColContextMenu");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableColContextMenu"));
		elemField.setXmlType(
				new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableColContextMenuState"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("tableSectContextMenu");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableSectContextMenu"));
		elemField.setXmlType(
				new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableSectionContextMenuState"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("tableContextMenu");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableContextMenu"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableContextMenuState"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("confirmation");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Confirmation"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("tableLayoutList");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableLayoutList"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableLayoutList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("availableOperation");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AvailableOperation"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AvailableOperation"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("sortDefinitionList");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDefinitionList"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDefinitionList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
