/**
 * Data.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Data  implements java.io.Serializable {
    private org.eclipse.birt.report.soapengine.api.Font font;

    private org.eclipse.birt.report.soapengine.api.ResultSet[] resultSets;

    private org.eclipse.birt.report.soapengine.api.Format format;

    private org.eclipse.birt.report.soapengine.api.TOC TOC;

    private org.eclipse.birt.report.soapengine.api.Export export;

    private org.eclipse.birt.report.soapengine.api.SelectionList[] cascadeParameter;

    private org.eclipse.birt.report.soapengine.api.Filter filter;

    private org.eclipse.birt.report.soapengine.api.Filter[] filterList;

    private java.lang.String[] columnValues;

    private org.eclipse.birt.report.soapengine.api.Page page;

    private org.eclipse.birt.report.soapengine.api.DataSet[] dataSets;

    private org.eclipse.birt.report.soapengine.api.ReportElement[] reportElementList;

    private org.eclipse.birt.report.soapengine.api.Theme[] themes;

    private org.eclipse.birt.report.soapengine.api.ColumnDefinition[] columnDefs;

    private org.eclipse.birt.report.soapengine.api.SectionDefinition sectionDef;

    private org.eclipse.birt.report.soapengine.api.ChartProperties chart;

    private java.lang.String labelText;

    private org.eclipse.birt.report.soapengine.api.FileBrowsing fileBrowsing;

    private org.eclipse.birt.report.soapengine.api.DataSource[] dataSources;

    private org.eclipse.birt.report.soapengine.api.Binding[] bindingList;

    private org.eclipse.birt.report.soapengine.api.DataSet dataSetDef;

    private org.eclipse.birt.report.soapengine.api.DataSet[] dataSetsToUse;

    private org.eclipse.birt.report.soapengine.api.DesignState designState;

    private org.eclipse.birt.report.soapengine.api.ToolbarState toolbarState;

    private java.lang.String redirectURL;

    private java.lang.String popupURL;

    private org.eclipse.birt.report.soapengine.api.BRDExpression BRDExpression;

    private org.eclipse.birt.report.soapengine.api.JoinMetadata joinMetadata;

    private org.eclipse.birt.report.soapengine.api.BoundDataColumn[] boundDataColumnList;

    private org.eclipse.birt.report.soapengine.api.IOFieldList[] IOInfoList;

    private org.eclipse.birt.report.soapengine.api.TableColContextMenuState tableColContextMenu;

    private org.eclipse.birt.report.soapengine.api.TableSectionContextMenuState tableSectContextMenu;

    private org.eclipse.birt.report.soapengine.api.TableContextMenuState tableContextMenu;

    private java.lang.String confirmation;

    private org.eclipse.birt.report.soapengine.api.TableLayout[] tableLayoutList;

    private org.eclipse.birt.report.soapengine.api.AvailableOperation availableOperation;

    private org.eclipse.birt.report.soapengine.api.SortDefinition[] sortDefinitionList;

    public Data() {
    }

    public Data(
           org.eclipse.birt.report.soapengine.api.Font font,
           org.eclipse.birt.report.soapengine.api.ResultSet[] resultSets,
           org.eclipse.birt.report.soapengine.api.Format format,
           org.eclipse.birt.report.soapengine.api.TOC TOC,
           org.eclipse.birt.report.soapengine.api.Export export,
           org.eclipse.birt.report.soapengine.api.SelectionList[] cascadeParameter,
           org.eclipse.birt.report.soapengine.api.Filter filter,
           org.eclipse.birt.report.soapengine.api.Filter[] filterList,
           java.lang.String[] columnValues,
           org.eclipse.birt.report.soapengine.api.Page page,
           org.eclipse.birt.report.soapengine.api.DataSet[] dataSets,
           org.eclipse.birt.report.soapengine.api.ReportElement[] reportElementList,
           org.eclipse.birt.report.soapengine.api.Theme[] themes,
           org.eclipse.birt.report.soapengine.api.ColumnDefinition[] columnDefs,
           org.eclipse.birt.report.soapengine.api.SectionDefinition sectionDef,
           org.eclipse.birt.report.soapengine.api.ChartProperties chart,
           java.lang.String labelText,
           org.eclipse.birt.report.soapengine.api.FileBrowsing fileBrowsing,
           org.eclipse.birt.report.soapengine.api.DataSource[] dataSources,
           org.eclipse.birt.report.soapengine.api.Binding[] bindingList,
           org.eclipse.birt.report.soapengine.api.DataSet dataSetDef,
           org.eclipse.birt.report.soapengine.api.DataSet[] dataSetsToUse,
           org.eclipse.birt.report.soapengine.api.DesignState designState,
           org.eclipse.birt.report.soapengine.api.ToolbarState toolbarState,
           java.lang.String redirectURL,
           java.lang.String popupURL,
           org.eclipse.birt.report.soapengine.api.BRDExpression BRDExpression,
           org.eclipse.birt.report.soapengine.api.JoinMetadata joinMetadata,
           org.eclipse.birt.report.soapengine.api.BoundDataColumn[] boundDataColumnList,
           org.eclipse.birt.report.soapengine.api.IOFieldList[] IOInfoList,
           org.eclipse.birt.report.soapengine.api.TableColContextMenuState tableColContextMenu,
           org.eclipse.birt.report.soapengine.api.TableSectionContextMenuState tableSectContextMenu,
           org.eclipse.birt.report.soapengine.api.TableContextMenuState tableContextMenu,
           java.lang.String confirmation,
           org.eclipse.birt.report.soapengine.api.TableLayout[] tableLayoutList,
           org.eclipse.birt.report.soapengine.api.AvailableOperation availableOperation,
           org.eclipse.birt.report.soapengine.api.SortDefinition[] sortDefinitionList) {
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
    public org.eclipse.birt.report.soapengine.api.ResultSet[] getResultSets() {
        return resultSets;
    }


    /**
     * Sets the resultSets value for this Data.
     * 
     * @param resultSets
     */
    public void setResultSets(org.eclipse.birt.report.soapengine.api.ResultSet[] resultSets) {
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
    public org.eclipse.birt.report.soapengine.api.SelectionList[] getCascadeParameter() {
        return cascadeParameter;
    }


    /**
     * Sets the cascadeParameter value for this Data.
     * 
     * @param cascadeParameter
     */
    public void setCascadeParameter(org.eclipse.birt.report.soapengine.api.SelectionList[] cascadeParameter) {
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
    public org.eclipse.birt.report.soapengine.api.Filter[] getFilterList() {
        return filterList;
    }


    /**
     * Sets the filterList value for this Data.
     * 
     * @param filterList
     */
    public void setFilterList(org.eclipse.birt.report.soapengine.api.Filter[] filterList) {
        this.filterList = filterList;
    }


    /**
     * Gets the columnValues value for this Data.
     * 
     * @return columnValues
     */
    public java.lang.String[] getColumnValues() {
        return columnValues;
    }


    /**
     * Sets the columnValues value for this Data.
     * 
     * @param columnValues
     */
    public void setColumnValues(java.lang.String[] columnValues) {
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
    public org.eclipse.birt.report.soapengine.api.DataSet[] getDataSets() {
        return dataSets;
    }


    /**
     * Sets the dataSets value for this Data.
     * 
     * @param dataSets
     */
    public void setDataSets(org.eclipse.birt.report.soapengine.api.DataSet[] dataSets) {
        this.dataSets = dataSets;
    }


    /**
     * Gets the reportElementList value for this Data.
     * 
     * @return reportElementList
     */
    public org.eclipse.birt.report.soapengine.api.ReportElement[] getReportElementList() {
        return reportElementList;
    }


    /**
     * Sets the reportElementList value for this Data.
     * 
     * @param reportElementList
     */
    public void setReportElementList(org.eclipse.birt.report.soapengine.api.ReportElement[] reportElementList) {
        this.reportElementList = reportElementList;
    }


    /**
     * Gets the themes value for this Data.
     * 
     * @return themes
     */
    public org.eclipse.birt.report.soapengine.api.Theme[] getThemes() {
        return themes;
    }


    /**
     * Sets the themes value for this Data.
     * 
     * @param themes
     */
    public void setThemes(org.eclipse.birt.report.soapengine.api.Theme[] themes) {
        this.themes = themes;
    }


    /**
     * Gets the columnDefs value for this Data.
     * 
     * @return columnDefs
     */
    public org.eclipse.birt.report.soapengine.api.ColumnDefinition[] getColumnDefs() {
        return columnDefs;
    }


    /**
     * Sets the columnDefs value for this Data.
     * 
     * @param columnDefs
     */
    public void setColumnDefs(org.eclipse.birt.report.soapengine.api.ColumnDefinition[] columnDefs) {
        this.columnDefs = columnDefs;
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
    public org.eclipse.birt.report.soapengine.api.DataSource[] getDataSources() {
        return dataSources;
    }


    /**
     * Sets the dataSources value for this Data.
     * 
     * @param dataSources
     */
    public void setDataSources(org.eclipse.birt.report.soapengine.api.DataSource[] dataSources) {
        this.dataSources = dataSources;
    }


    /**
     * Gets the bindingList value for this Data.
     * 
     * @return bindingList
     */
    public org.eclipse.birt.report.soapengine.api.Binding[] getBindingList() {
        return bindingList;
    }


    /**
     * Sets the bindingList value for this Data.
     * 
     * @param bindingList
     */
    public void setBindingList(org.eclipse.birt.report.soapengine.api.Binding[] bindingList) {
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
    public org.eclipse.birt.report.soapengine.api.DataSet[] getDataSetsToUse() {
        return dataSetsToUse;
    }


    /**
     * Sets the dataSetsToUse value for this Data.
     * 
     * @param dataSetsToUse
     */
    public void setDataSetsToUse(org.eclipse.birt.report.soapengine.api.DataSet[] dataSetsToUse) {
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
    public org.eclipse.birt.report.soapengine.api.BoundDataColumn[] getBoundDataColumnList() {
        return boundDataColumnList;
    }


    /**
     * Sets the boundDataColumnList value for this Data.
     * 
     * @param boundDataColumnList
     */
    public void setBoundDataColumnList(org.eclipse.birt.report.soapengine.api.BoundDataColumn[] boundDataColumnList) {
        this.boundDataColumnList = boundDataColumnList;
    }


    /**
     * Gets the IOInfoList value for this Data.
     * 
     * @return IOInfoList
     */
    public org.eclipse.birt.report.soapengine.api.IOFieldList[] getIOInfoList() {
        return IOInfoList;
    }


    /**
     * Sets the IOInfoList value for this Data.
     * 
     * @param IOInfoList
     */
    public void setIOInfoList(org.eclipse.birt.report.soapengine.api.IOFieldList[] IOInfoList) {
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
    public void setTableColContextMenu(org.eclipse.birt.report.soapengine.api.TableColContextMenuState tableColContextMenu) {
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
    public void setTableSectContextMenu(org.eclipse.birt.report.soapengine.api.TableSectionContextMenuState tableSectContextMenu) {
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
    public org.eclipse.birt.report.soapengine.api.TableLayout[] getTableLayoutList() {
        return tableLayoutList;
    }


    /**
     * Sets the tableLayoutList value for this Data.
     * 
     * @param tableLayoutList
     */
    public void setTableLayoutList(org.eclipse.birt.report.soapengine.api.TableLayout[] tableLayoutList) {
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
    public org.eclipse.birt.report.soapengine.api.SortDefinition[] getSortDefinitionList() {
        return sortDefinitionList;
    }


    /**
     * Sets the sortDefinitionList value for this Data.
     * 
     * @param sortDefinitionList
     */
    public void setSortDefinitionList(org.eclipse.birt.report.soapengine.api.SortDefinition[] sortDefinitionList) {
        this.sortDefinitionList = sortDefinitionList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Data)) return false;
        Data other = (Data) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.font==null && other.getFont()==null) || 
             (this.font!=null &&
              this.font.equals(other.getFont()))) &&
            ((this.resultSets==null && other.getResultSets()==null) || 
             (this.resultSets!=null &&
              java.util.Arrays.equals(this.resultSets, other.getResultSets()))) &&
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              this.format.equals(other.getFormat()))) &&
            ((this.TOC==null && other.getTOC()==null) || 
             (this.TOC!=null &&
              this.TOC.equals(other.getTOC()))) &&
            ((this.export==null && other.getExport()==null) || 
             (this.export!=null &&
              this.export.equals(other.getExport()))) &&
            ((this.cascadeParameter==null && other.getCascadeParameter()==null) || 
             (this.cascadeParameter!=null &&
              java.util.Arrays.equals(this.cascadeParameter, other.getCascadeParameter()))) &&
            ((this.filter==null && other.getFilter()==null) || 
             (this.filter!=null &&
              this.filter.equals(other.getFilter()))) &&
            ((this.filterList==null && other.getFilterList()==null) || 
             (this.filterList!=null &&
              java.util.Arrays.equals(this.filterList, other.getFilterList()))) &&
            ((this.columnValues==null && other.getColumnValues()==null) || 
             (this.columnValues!=null &&
              java.util.Arrays.equals(this.columnValues, other.getColumnValues()))) &&
            ((this.page==null && other.getPage()==null) || 
             (this.page!=null &&
              this.page.equals(other.getPage()))) &&
            ((this.dataSets==null && other.getDataSets()==null) || 
             (this.dataSets!=null &&
              java.util.Arrays.equals(this.dataSets, other.getDataSets()))) &&
            ((this.reportElementList==null && other.getReportElementList()==null) || 
             (this.reportElementList!=null &&
              java.util.Arrays.equals(this.reportElementList, other.getReportElementList()))) &&
            ((this.themes==null && other.getThemes()==null) || 
             (this.themes!=null &&
              java.util.Arrays.equals(this.themes, other.getThemes()))) &&
            ((this.columnDefs==null && other.getColumnDefs()==null) || 
             (this.columnDefs!=null &&
              java.util.Arrays.equals(this.columnDefs, other.getColumnDefs()))) &&
            ((this.sectionDef==null && other.getSectionDef()==null) || 
             (this.sectionDef!=null &&
              this.sectionDef.equals(other.getSectionDef()))) &&
            ((this.chart==null && other.getChart()==null) || 
             (this.chart!=null &&
              this.chart.equals(other.getChart()))) &&
            ((this.labelText==null && other.getLabelText()==null) || 
             (this.labelText!=null &&
              this.labelText.equals(other.getLabelText()))) &&
            ((this.fileBrowsing==null && other.getFileBrowsing()==null) || 
             (this.fileBrowsing!=null &&
              this.fileBrowsing.equals(other.getFileBrowsing()))) &&
            ((this.dataSources==null && other.getDataSources()==null) || 
             (this.dataSources!=null &&
              java.util.Arrays.equals(this.dataSources, other.getDataSources()))) &&
            ((this.bindingList==null && other.getBindingList()==null) || 
             (this.bindingList!=null &&
              java.util.Arrays.equals(this.bindingList, other.getBindingList()))) &&
            ((this.dataSetDef==null && other.getDataSetDef()==null) || 
             (this.dataSetDef!=null &&
              this.dataSetDef.equals(other.getDataSetDef()))) &&
            ((this.dataSetsToUse==null && other.getDataSetsToUse()==null) || 
             (this.dataSetsToUse!=null &&
              java.util.Arrays.equals(this.dataSetsToUse, other.getDataSetsToUse()))) &&
            ((this.designState==null && other.getDesignState()==null) || 
             (this.designState!=null &&
              this.designState.equals(other.getDesignState()))) &&
            ((this.toolbarState==null && other.getToolbarState()==null) || 
             (this.toolbarState!=null &&
              this.toolbarState.equals(other.getToolbarState()))) &&
            ((this.redirectURL==null && other.getRedirectURL()==null) || 
             (this.redirectURL!=null &&
              this.redirectURL.equals(other.getRedirectURL()))) &&
            ((this.popupURL==null && other.getPopupURL()==null) || 
             (this.popupURL!=null &&
              this.popupURL.equals(other.getPopupURL()))) &&
            ((this.BRDExpression==null && other.getBRDExpression()==null) || 
             (this.BRDExpression!=null &&
              this.BRDExpression.equals(other.getBRDExpression()))) &&
            ((this.joinMetadata==null && other.getJoinMetadata()==null) || 
             (this.joinMetadata!=null &&
              this.joinMetadata.equals(other.getJoinMetadata()))) &&
            ((this.boundDataColumnList==null && other.getBoundDataColumnList()==null) || 
             (this.boundDataColumnList!=null &&
              java.util.Arrays.equals(this.boundDataColumnList, other.getBoundDataColumnList()))) &&
            ((this.IOInfoList==null && other.getIOInfoList()==null) || 
             (this.IOInfoList!=null &&
              java.util.Arrays.equals(this.IOInfoList, other.getIOInfoList()))) &&
            ((this.tableColContextMenu==null && other.getTableColContextMenu()==null) || 
             (this.tableColContextMenu!=null &&
              this.tableColContextMenu.equals(other.getTableColContextMenu()))) &&
            ((this.tableSectContextMenu==null && other.getTableSectContextMenu()==null) || 
             (this.tableSectContextMenu!=null &&
              this.tableSectContextMenu.equals(other.getTableSectContextMenu()))) &&
            ((this.tableContextMenu==null && other.getTableContextMenu()==null) || 
             (this.tableContextMenu!=null &&
              this.tableContextMenu.equals(other.getTableContextMenu()))) &&
            ((this.confirmation==null && other.getConfirmation()==null) || 
             (this.confirmation!=null &&
              this.confirmation.equals(other.getConfirmation()))) &&
            ((this.tableLayoutList==null && other.getTableLayoutList()==null) || 
             (this.tableLayoutList!=null &&
              java.util.Arrays.equals(this.tableLayoutList, other.getTableLayoutList()))) &&
            ((this.availableOperation==null && other.getAvailableOperation()==null) || 
             (this.availableOperation!=null &&
              this.availableOperation.equals(other.getAvailableOperation()))) &&
            ((this.sortDefinitionList==null && other.getSortDefinitionList()==null) || 
             (this.sortDefinitionList!=null &&
              java.util.Arrays.equals(this.sortDefinitionList, other.getSortDefinitionList())));
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResultSets());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResultSets(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCascadeParameter());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCascadeParameter(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getFilter() != null) {
            _hashCode += getFilter().hashCode();
        }
        if (getFilterList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFilterList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFilterList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getColumnValues() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getColumnValues());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getColumnValues(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPage() != null) {
            _hashCode += getPage().hashCode();
        }
        if (getDataSets() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDataSets());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDataSets(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getReportElementList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReportElementList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReportElementList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getThemes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getThemes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getThemes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getColumnDefs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getColumnDefs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getColumnDefs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDataSources());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDataSources(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBindingList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBindingList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBindingList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDataSetDef() != null) {
            _hashCode += getDataSetDef().hashCode();
        }
        if (getDataSetsToUse() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDataSetsToUse());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDataSetsToUse(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBoundDataColumnList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBoundDataColumnList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIOInfoList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIOInfoList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIOInfoList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTableLayoutList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTableLayoutList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAvailableOperation() != null) {
            _hashCode += getAvailableOperation().hashCode();
        }
        if (getSortDefinitionList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSortDefinitionList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSortDefinitionList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Data.class, true);

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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ResultSet"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ResultSet"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SelectionList"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SelectionList"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Filter"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Filter"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columnValues");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Value"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSet"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSet"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportElementList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportElementList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportElement"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Element"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("themes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Themes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Theme"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Theme"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columnDefs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDefs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDefinition"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDef"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSource"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSource"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bindingList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BindingList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Binding"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Binding"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSet"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSet"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BoundDataColumn"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BoundDataColumn"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IOInfoList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOInfoList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOFieldList"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOFieldListArray"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tableColContextMenu");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableColContextMenu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableColContextMenuState"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tableSectContextMenu");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableSectContextMenu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableSectionContextMenuState"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableLayout"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableLayout"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDefinition"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDefinition"));
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
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
