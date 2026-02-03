package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Data implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Font")
	private Font font;
	@XmlElement(name = "ResultSets")
	private ResultSets resultSets;
	@XmlElement(name = "Format")
	private Format format;
	@XmlElement(name = "TOC")
	private TOC TOC;
	@XmlElement(name = "Export")
	private Export export;
	@XmlElement(name = "CascadeParameter")
	private CascadeParameter cascadeParameter;
	@XmlElement(name = "Filter")
	private Filter filter;
	@XmlElement(name = "FilterList")
	private FilterList filterList;
	@XmlElement(name = "ColumnValues")
	private Vector columnValues;
	@XmlElement(name = "Page")
	private Page page;
	@XmlElement(name = "DataSets")
	private DataSetList dataSets;
	@XmlElement(name = "ReportElementList")
	private ReportElementList reportElementList;
	@XmlElement(name = "Themes")
	private ThemeList themes;
	@XmlElement(name = "ColumnDefs")
	private ColumnDefinitionGroup columnDefs;
	@XmlElement(name = "GroupDetail")
	private GroupDetail groupDetail;
	@XmlElement(name = "CellDef")
	private CellDefinition cellDef;
	@XmlElement(name = "SectionDef")
	private SectionDefinition sectionDef;
	@XmlElement(name = "Chart")
	private ChartProperties chart;
	@XmlElement(name = "LabelText")
	private String labelText;
	@XmlElement(name = "FileBrowsing")
	private FileBrowsing fileBrowsing;
	@XmlElement(name = "DataSources")
	private DataSourceList dataSources;
	@XmlElement(name = "BindingList")
	private BindingList bindingList;
	@XmlElement(name = "DataSetDef")
	private DataSet dataSetDef;
	@XmlElement(name = "DataSetsToUse")
	private DataSetList dataSetsToUse;
	@XmlElement(name = "DesignState")
	private DesignState designState;
	@XmlElement(name = "ToolbarState")
	private ToolbarState toolbarState;
	@XmlElement(name = "RedirectURL")
	private String redirectURL;
	@XmlElement(name = "PopupURL")
	private String popupURL;
	@XmlElement(name = "BRDExpression")
	private BRDExpression BRDExpression;
	@XmlElement(name = "JoinMetadata")
	private JoinMetadata joinMetadata;
	@XmlElement(name = "BoundDataColumnList")
	private BoundDataColumnList boundDataColumnList;
	@XmlElement(name = "IOInfoList")
	private IOInfoList IOInfoList;
	@XmlElement(name = "TableColContextMenu")
	private TableColContextMenuState tableColContextMenu;
	@XmlElement(name = "TableSectContextMenu")
	private TableSectionContextMenuState tableSectContextMenu;
	@XmlElement(name = "TableContextMenu")
	private TableContextMenuState tableContextMenu;
	@XmlElement(name = "Confirmation")
	private String confirmation;
	@XmlElement(name = "TableLayoutList")
	private TableLayoutList tableLayoutList;
	@XmlElement(name = "AvailableOperation")
	private AvailableOperation availableOperation;
	@XmlElement(name = "SortDefinitionList")
	private SortDefinitionList sortDefinitionList;

	public Data() {
	}

	// Generický konštruktor s parametrami môžeš zachovať, alebo vynechať pre
	// jednoduchší POJO
	public Data(Font font, ResultSets resultSets, Format format, TOC TOC, Export export,
			CascadeParameter cascadeParameter, Filter filter, FilterList filterList, Vector columnValues, Page page,
			DataSetList dataSets, ReportElementList reportElementList, ThemeList themes,
			ColumnDefinitionGroup columnDefs, GroupDetail groupDetail, CellDefinition cellDef,
			SectionDefinition sectionDef, ChartProperties chart, String labelText, FileBrowsing fileBrowsing,
			DataSourceList dataSources, BindingList bindingList, DataSet dataSetDef, DataSetList dataSetsToUse,
			DesignState designState, ToolbarState toolbarState, String redirectURL, String popupURL,
			BRDExpression BRDExpression, JoinMetadata joinMetadata, BoundDataColumnList boundDataColumnList,
			IOInfoList IOInfoList, TableColContextMenuState tableColContextMenu,
			TableSectionContextMenuState tableSectContextMenu, TableContextMenuState tableContextMenu,
			String confirmation, TableLayoutList tableLayoutList, AvailableOperation availableOperation,
			SortDefinitionList sortDefinitionList) {
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

	// Getter / Setter metódy pre všetky polia
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public ResultSets getResultSets() {
		return resultSets;
	}

	public void setResultSets(ResultSets resultSets) {
		this.resultSets = resultSets;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public TOC getTOC() {
		return TOC;
	}

	public void setTOC(TOC TOC) {
		this.TOC = TOC;
	}

	public Export getExport() {
		return export;
	}

	public void setExport(Export export) {
		this.export = export;
	}

	public CascadeParameter getCascadeParameter() {
		return cascadeParameter;
	}

	public void setCascadeParameter(CascadeParameter cascadeParameter) {
		this.cascadeParameter = cascadeParameter;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public FilterList getFilterList() {
		return filterList;
	}

	public void setFilterList(FilterList filterList) {
		this.filterList = filterList;
	}

	public Vector getColumnValues() {
		return columnValues;
	}

	public void setColumnValues(Vector columnValues) {
		this.columnValues = columnValues;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public DataSetList getDataSets() {
		return dataSets;
	}

	public void setDataSets(DataSetList dataSets) {
		this.dataSets = dataSets;
	}

	public ReportElementList getReportElementList() {
		return reportElementList;
	}

	public void setReportElementList(ReportElementList reportElementList) {
		this.reportElementList = reportElementList;
	}

	public ThemeList getThemes() {
		return themes;
	}

	public void setThemes(ThemeList themes) {
		this.themes = themes;
	}

	public ColumnDefinitionGroup getColumnDefs() {
		return columnDefs;
	}

	public void setColumnDefs(ColumnDefinitionGroup columnDefs) {
		this.columnDefs = columnDefs;
	}

	public GroupDetail getGroupDetail() {
		return groupDetail;
	}

	public void setGroupDetail(GroupDetail groupDetail) {
		this.groupDetail = groupDetail;
	}

	public CellDefinition getCellDef() {
		return cellDef;
	}

	public void setCellDef(CellDefinition cellDef) {
		this.cellDef = cellDef;
	}

	public SectionDefinition getSectionDef() {
		return sectionDef;
	}

	public void setSectionDef(SectionDefinition sectionDef) {
		this.sectionDef = sectionDef;
	}

	public ChartProperties getChart() {
		return chart;
	}

	public void setChart(ChartProperties chart) {
		this.chart = chart;
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public FileBrowsing getFileBrowsing() {
		return fileBrowsing;
	}

	public void setFileBrowsing(FileBrowsing fileBrowsing) {
		this.fileBrowsing = fileBrowsing;
	}

	public DataSourceList getDataSources() {
		return dataSources;
	}

	public void setDataSources(DataSourceList dataSources) {
		this.dataSources = dataSources;
	}

	public BindingList getBindingList() {
		return bindingList;
	}

	public void setBindingList(BindingList bindingList) {
		this.bindingList = bindingList;
	}

	public DataSet getDataSetDef() {
		return dataSetDef;
	}

	public void setDataSetDef(DataSet dataSetDef) {
		this.dataSetDef = dataSetDef;
	}

	public DataSetList getDataSetsToUse() {
		return dataSetsToUse;
	}

	public void setDataSetsToUse(DataSetList dataSetsToUse) {
		this.dataSetsToUse = dataSetsToUse;
	}

	public DesignState getDesignState() {
		return designState;
	}

	public void setDesignState(DesignState designState) {
		this.designState = designState;
	}

	public ToolbarState getToolbarState() {
		return toolbarState;
	}

	public void setToolbarState(ToolbarState toolbarState) {
		this.toolbarState = toolbarState;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getPopupURL() {
		return popupURL;
	}

	public void setPopupURL(String popupURL) {
		this.popupURL = popupURL;
	}

	public BRDExpression getBRDExpression() {
		return BRDExpression;
	}

	public void setBRDExpression(BRDExpression BRDExpression) {
		this.BRDExpression = BRDExpression;
	}

	public JoinMetadata getJoinMetadata() {
		return joinMetadata;
	}

	public void setJoinMetadata(JoinMetadata joinMetadata) {
		this.joinMetadata = joinMetadata;
	}

	public BoundDataColumnList getBoundDataColumnList() {
		return boundDataColumnList;
	}

	public void setBoundDataColumnList(BoundDataColumnList boundDataColumnList) {
		this.boundDataColumnList = boundDataColumnList;
	}

	public IOInfoList getIOInfoList() {
		return IOInfoList;
	}

	public void setIOInfoList(IOInfoList IOInfoList) {
		this.IOInfoList = IOInfoList;
	}

	public TableColContextMenuState getTableColContextMenu() {
		return tableColContextMenu;
	}

	public void setTableColContextMenu(TableColContextMenuState tableColContextMenu) {
		this.tableColContextMenu = tableColContextMenu;
	}

	public TableSectionContextMenuState getTableSectContextMenu() {
		return tableSectContextMenu;
	}

	public void setTableSectContextMenu(TableSectionContextMenuState tableSectContextMenu) {
		this.tableSectContextMenu = tableSectContextMenu;
	}

	public TableContextMenuState getTableContextMenu() {
		return tableContextMenu;
	}

	public void setTableContextMenu(TableContextMenuState tableContextMenu) {
		this.tableContextMenu = tableContextMenu;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

	public TableLayoutList getTableLayoutList() {
		return tableLayoutList;
	}

	public void setTableLayoutList(TableLayoutList tableLayoutList) {
		this.tableLayoutList = tableLayoutList;
	}

	public AvailableOperation getAvailableOperation() {
		return availableOperation;
	}

	public void setAvailableOperation(AvailableOperation availableOperation) {
		this.availableOperation = availableOperation;
	}

	public SortDefinitionList getSortDefinitionList() {
		return sortDefinitionList;
	}

	public void setSortDefinitionList(SortDefinitionList sortDefinitionList) {
		this.sortDefinitionList = sortDefinitionList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Data))
			return false;
		Data data = (Data) o;
		return Objects.equals(font, data.font) && Objects.equals(resultSets, data.resultSets)
				&& Objects.equals(format, data.format) && Objects.equals(TOC, data.TOC)
				&& Objects.equals(export, data.export) && Objects.equals(cascadeParameter, data.cascadeParameter)
				&& Objects.equals(filter, data.filter) && Objects.equals(filterList, data.filterList)
				&& Objects.equals(columnValues, data.columnValues) && Objects.equals(page, data.page)
				&& Objects.equals(dataSets, data.dataSets) && Objects.equals(reportElementList, data.reportElementList)
				&& Objects.equals(themes, data.themes) && Objects.equals(columnDefs, data.columnDefs)
				&& Objects.equals(groupDetail, data.groupDetail) && Objects.equals(cellDef, data.cellDef)
				&& Objects.equals(sectionDef, data.sectionDef) && Objects.equals(chart, data.chart)
				&& Objects.equals(labelText, data.labelText) && Objects.equals(fileBrowsing, data.fileBrowsing)
				&& Objects.equals(dataSources, data.dataSources) && Objects.equals(bindingList, data.bindingList)
				&& Objects.equals(dataSetDef, data.dataSetDef) && Objects.equals(dataSetsToUse, data.dataSetsToUse)
				&& Objects.equals(designState, data.designState) && Objects.equals(toolbarState, data.toolbarState)
				&& Objects.equals(redirectURL, data.redirectURL) && Objects.equals(popupURL, data.popupURL)
				&& Objects.equals(BRDExpression, data.BRDExpression) && Objects.equals(joinMetadata, data.joinMetadata)
				&& Objects.equals(boundDataColumnList, data.boundDataColumnList)
				&& Objects.equals(IOInfoList, data.IOInfoList)
				&& Objects.equals(tableColContextMenu, data.tableColContextMenu)
				&& Objects.equals(tableSectContextMenu, data.tableSectContextMenu)
				&& Objects.equals(tableContextMenu, data.tableContextMenu)
				&& Objects.equals(confirmation, data.confirmation)
				&& Objects.equals(tableLayoutList, data.tableLayoutList)
				&& Objects.equals(availableOperation, data.availableOperation)
				&& Objects.equals(sortDefinitionList, data.sortDefinitionList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(font, resultSets, format, TOC, export, cascadeParameter, filter, filterList, columnValues,
				page, dataSets, reportElementList, themes, columnDefs, groupDetail, cellDef, sectionDef, chart,
				labelText, fileBrowsing, dataSources, bindingList, dataSetDef, dataSetsToUse, designState, toolbarState,
				redirectURL, popupURL, BRDExpression, joinMetadata, boundDataColumnList, IOInfoList,
				tableColContextMenu, tableSectContextMenu, tableContextMenu, confirmation, tableLayoutList,
				availableOperation, sortDefinitionList);
	}
}
