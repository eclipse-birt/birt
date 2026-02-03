package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * AvailableOperation - simplified DTO, SOAP-free.
 */
@XmlRootElement(name = "AvailableOperation")
@XmlAccessorType(XmlAccessType.FIELD)
public class AvailableOperation {
	@XmlElement(name = "SaveView")
	private Boolean saveView;
	@XmlElement(name = "ApplyView")
	private Boolean applyView;
	@XmlElement(name = "Print")
	private Boolean print;
	@XmlElement(name = "Export")
	private Boolean export;
	@XmlElement(name = "Toc")
	private Boolean toc;
	@XmlElement(name = "Undo")
	private Boolean undo;
	@XmlElement(name = "Redo")
	private Boolean redo;
	@XmlElement(name = "SortAsc")
	private Boolean sortAsc;
	@XmlElement(name = "SortDsc")
	private Boolean sortDsc;
	@XmlElement(name = "AdvancedSort")
	private Boolean advancedSort;
	@XmlElement(name = "AddGroup")
	private Boolean addGroup;
	@XmlElement(name = "DeleteGroup")
	private Boolean deleteGroup;
	@XmlElement(name = "HideColumn")
	private Boolean hideColumn;
	@XmlElement(name = "ShowColumns")
	private Boolean showColumns;
	@XmlElement(name = "ReorderColumns")
	private Boolean reorderColumns;
	@XmlElement(name = "Filter")
	private Boolean filter;
	@XmlElement(name = "Calculation")
	private Boolean calculation;
	@XmlElement(name = "Aggregation")
	private Boolean aggregation;
	@XmlElement(name = "ChangeFont")
	private Boolean changeFont;
	@XmlElement(name = "Format")
	private Boolean format;
	@XmlElement(name = "Text")
	private Boolean text;
	@XmlElement(name = "AlignLeft")
	private Boolean alignLeft;
	@XmlElement(name = "AlignCenter")
	private Boolean alignCenter;
	@XmlElement(name = "AlignRight")
	private Boolean alignRight;

	// Konštruktory
	public AvailableOperation() {
	}

	// Get/Set metódy
	public Boolean getSaveView() {
		return saveView;
	}

	public void setSaveView(Boolean saveView) {
		this.saveView = saveView;
	}

	public Boolean getApplyView() {
		return applyView;
	}

	public void setApplyView(Boolean applyView) {
		this.applyView = applyView;
	}

	public Boolean getPrint() {
		return print;
	}

	public void setPrint(Boolean print) {
		this.print = print;
	}

	public Boolean getExport() {
		return export;
	}

	public void setExport(Boolean export) {
		this.export = export;
	}

	public Boolean getToc() {
		return toc;
	}

	public void setToc(Boolean toc) {
		this.toc = toc;
	}

	public Boolean getUndo() {
		return undo;
	}

	public void setUndo(Boolean undo) {
		this.undo = undo;
	}

	public Boolean getRedo() {
		return redo;
	}

	public void setRedo(Boolean redo) {
		this.redo = redo;
	}

	public Boolean getSortAsc() {
		return sortAsc;
	}

	public void setSortAsc(Boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public Boolean getSortDsc() {
		return sortDsc;
	}

	public void setSortDsc(Boolean sortDsc) {
		this.sortDsc = sortDsc;
	}

	public Boolean getAdvancedSort() {
		return advancedSort;
	}

	public void setAdvancedSort(Boolean advancedSort) {
		this.advancedSort = advancedSort;
	}

	public Boolean getAddGroup() {
		return addGroup;
	}

	public void setAddGroup(Boolean addGroup) {
		this.addGroup = addGroup;
	}

	public Boolean getDeleteGroup() {
		return deleteGroup;
	}

	public void setDeleteGroup(Boolean deleteGroup) {
		this.deleteGroup = deleteGroup;
	}

	public Boolean getHideColumn() {
		return hideColumn;
	}

	public void setHideColumn(Boolean hideColumn) {
		this.hideColumn = hideColumn;
	}

	public Boolean getShowColumns() {
		return showColumns;
	}

	public void setShowColumns(Boolean showColumns) {
		this.showColumns = showColumns;
	}

	public Boolean getReorderColumns() {
		return reorderColumns;
	}

	public void setReorderColumns(Boolean reorderColumns) {
		this.reorderColumns = reorderColumns;
	}

	public Boolean getFilter() {
		return filter;
	}

	public void setFilter(Boolean filter) {
		this.filter = filter;
	}

	public Boolean getCalculation() {
		return calculation;
	}

	public void setCalculation(Boolean calculation) {
		this.calculation = calculation;
	}

	public Boolean getAggregation() {
		return aggregation;
	}

	public void setAggregation(Boolean aggregation) {
		this.aggregation = aggregation;
	}

	public Boolean getChangeFont() {
		return changeFont;
	}

	public void setChangeFont(Boolean changeFont) {
		this.changeFont = changeFont;
	}

	public Boolean getFormat() {
		return format;
	}

	public void setFormat(Boolean format) {
		this.format = format;
	}

	public Boolean getText() {
		return text;
	}

	public void setText(Boolean text) {
		this.text = text;
	}

	public Boolean getAlignLeft() {
		return alignLeft;
	}

	public void setAlignLeft(Boolean alignLeft) {
		this.alignLeft = alignLeft;
	}

	public Boolean getAlignCenter() {
		return alignCenter;
	}

	public void setAlignCenter(Boolean alignCenter) {
		this.alignCenter = alignCenter;
	}

	public Boolean getAlignRight() {
		return alignRight;
	}

	public void setAlignRight(Boolean alignRight) {
		this.alignRight = alignRight;
	}
}
