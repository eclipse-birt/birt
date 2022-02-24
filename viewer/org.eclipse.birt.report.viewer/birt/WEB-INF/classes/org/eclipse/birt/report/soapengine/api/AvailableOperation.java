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
 * AvailableOperation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class AvailableOperation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.Boolean saveView;
	private java.lang.Boolean applyView;
	private java.lang.Boolean print;
	private java.lang.Boolean export;
	private java.lang.Boolean toc;
	private java.lang.Boolean undo;
	private java.lang.Boolean redo;
	private java.lang.Boolean sortAsc;
	private java.lang.Boolean sortDsc;
	private java.lang.Boolean advancedSort;
	private java.lang.Boolean addGroup;
	private java.lang.Boolean deleteGroup;
	private java.lang.Boolean hideColumn;
	private java.lang.Boolean showColumns;
	private java.lang.Boolean reorderColumns;
	private java.lang.Boolean filter;
	private java.lang.Boolean calculation;
	private java.lang.Boolean aggregation;
	private java.lang.Boolean changeFont;
	private java.lang.Boolean format;
	private java.lang.Boolean text;
	private java.lang.Boolean alignLeft;
	private java.lang.Boolean alignCenter;
	private java.lang.Boolean alignRight;

	public AvailableOperation() {
	}

	public AvailableOperation(java.lang.Boolean saveView, java.lang.Boolean applyView, java.lang.Boolean print,
			java.lang.Boolean export, java.lang.Boolean toc, java.lang.Boolean undo, java.lang.Boolean redo,
			java.lang.Boolean sortAsc, java.lang.Boolean sortDsc, java.lang.Boolean advancedSort,
			java.lang.Boolean addGroup, java.lang.Boolean deleteGroup, java.lang.Boolean hideColumn,
			java.lang.Boolean showColumns, java.lang.Boolean reorderColumns, java.lang.Boolean filter,
			java.lang.Boolean calculation, java.lang.Boolean aggregation, java.lang.Boolean changeFont,
			java.lang.Boolean format, java.lang.Boolean text, java.lang.Boolean alignLeft,
			java.lang.Boolean alignCenter, java.lang.Boolean alignRight) {
		this.saveView = saveView;
		this.applyView = applyView;
		this.print = print;
		this.export = export;
		this.toc = toc;
		this.undo = undo;
		this.redo = redo;
		this.sortAsc = sortAsc;
		this.sortDsc = sortDsc;
		this.advancedSort = advancedSort;
		this.addGroup = addGroup;
		this.deleteGroup = deleteGroup;
		this.hideColumn = hideColumn;
		this.showColumns = showColumns;
		this.reorderColumns = reorderColumns;
		this.filter = filter;
		this.calculation = calculation;
		this.aggregation = aggregation;
		this.changeFont = changeFont;
		this.format = format;
		this.text = text;
		this.alignLeft = alignLeft;
		this.alignCenter = alignCenter;
		this.alignRight = alignRight;
	}

	/**
	 * Gets the saveView value for this AvailableOperation.
	 * 
	 * @return saveView
	 */
	public java.lang.Boolean getSaveView() {
		return saveView;
	}

	/**
	 * Sets the saveView value for this AvailableOperation.
	 * 
	 * @param saveView
	 */
	public void setSaveView(java.lang.Boolean saveView) {
		this.saveView = saveView;
	}

	/**
	 * Gets the applyView value for this AvailableOperation.
	 * 
	 * @return applyView
	 */
	public java.lang.Boolean getApplyView() {
		return applyView;
	}

	/**
	 * Sets the applyView value for this AvailableOperation.
	 * 
	 * @param applyView
	 */
	public void setApplyView(java.lang.Boolean applyView) {
		this.applyView = applyView;
	}

	/**
	 * Gets the print value for this AvailableOperation.
	 * 
	 * @return print
	 */
	public java.lang.Boolean getPrint() {
		return print;
	}

	/**
	 * Sets the print value for this AvailableOperation.
	 * 
	 * @param print
	 */
	public void setPrint(java.lang.Boolean print) {
		this.print = print;
	}

	/**
	 * Gets the export value for this AvailableOperation.
	 * 
	 * @return export
	 */
	public java.lang.Boolean getExport() {
		return export;
	}

	/**
	 * Sets the export value for this AvailableOperation.
	 * 
	 * @param export
	 */
	public void setExport(java.lang.Boolean export) {
		this.export = export;
	}

	/**
	 * Gets the toc value for this AvailableOperation.
	 * 
	 * @return toc
	 */
	public java.lang.Boolean getToc() {
		return toc;
	}

	/**
	 * Sets the toc value for this AvailableOperation.
	 * 
	 * @param toc
	 */
	public void setToc(java.lang.Boolean toc) {
		this.toc = toc;
	}

	/**
	 * Gets the undo value for this AvailableOperation.
	 * 
	 * @return undo
	 */
	public java.lang.Boolean getUndo() {
		return undo;
	}

	/**
	 * Sets the undo value for this AvailableOperation.
	 * 
	 * @param undo
	 */
	public void setUndo(java.lang.Boolean undo) {
		this.undo = undo;
	}

	/**
	 * Gets the redo value for this AvailableOperation.
	 * 
	 * @return redo
	 */
	public java.lang.Boolean getRedo() {
		return redo;
	}

	/**
	 * Sets the redo value for this AvailableOperation.
	 * 
	 * @param redo
	 */
	public void setRedo(java.lang.Boolean redo) {
		this.redo = redo;
	}

	/**
	 * Gets the sortAsc value for this AvailableOperation.
	 * 
	 * @return sortAsc
	 */
	public java.lang.Boolean getSortAsc() {
		return sortAsc;
	}

	/**
	 * Sets the sortAsc value for this AvailableOperation.
	 * 
	 * @param sortAsc
	 */
	public void setSortAsc(java.lang.Boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	/**
	 * Gets the sortDsc value for this AvailableOperation.
	 * 
	 * @return sortDsc
	 */
	public java.lang.Boolean getSortDsc() {
		return sortDsc;
	}

	/**
	 * Sets the sortDsc value for this AvailableOperation.
	 * 
	 * @param sortDsc
	 */
	public void setSortDsc(java.lang.Boolean sortDsc) {
		this.sortDsc = sortDsc;
	}

	/**
	 * Gets the advancedSort value for this AvailableOperation.
	 * 
	 * @return advancedSort
	 */
	public java.lang.Boolean getAdvancedSort() {
		return advancedSort;
	}

	/**
	 * Sets the advancedSort value for this AvailableOperation.
	 * 
	 * @param advancedSort
	 */
	public void setAdvancedSort(java.lang.Boolean advancedSort) {
		this.advancedSort = advancedSort;
	}

	/**
	 * Gets the addGroup value for this AvailableOperation.
	 * 
	 * @return addGroup
	 */
	public java.lang.Boolean getAddGroup() {
		return addGroup;
	}

	/**
	 * Sets the addGroup value for this AvailableOperation.
	 * 
	 * @param addGroup
	 */
	public void setAddGroup(java.lang.Boolean addGroup) {
		this.addGroup = addGroup;
	}

	/**
	 * Gets the deleteGroup value for this AvailableOperation.
	 * 
	 * @return deleteGroup
	 */
	public java.lang.Boolean getDeleteGroup() {
		return deleteGroup;
	}

	/**
	 * Sets the deleteGroup value for this AvailableOperation.
	 * 
	 * @param deleteGroup
	 */
	public void setDeleteGroup(java.lang.Boolean deleteGroup) {
		this.deleteGroup = deleteGroup;
	}

	/**
	 * Gets the hideColumn value for this AvailableOperation.
	 * 
	 * @return hideColumn
	 */
	public java.lang.Boolean getHideColumn() {
		return hideColumn;
	}

	/**
	 * Sets the hideColumn value for this AvailableOperation.
	 * 
	 * @param hideColumn
	 */
	public void setHideColumn(java.lang.Boolean hideColumn) {
		this.hideColumn = hideColumn;
	}

	/**
	 * Gets the showColumns value for this AvailableOperation.
	 * 
	 * @return showColumns
	 */
	public java.lang.Boolean getShowColumns() {
		return showColumns;
	}

	/**
	 * Sets the showColumns value for this AvailableOperation.
	 * 
	 * @param showColumns
	 */
	public void setShowColumns(java.lang.Boolean showColumns) {
		this.showColumns = showColumns;
	}

	/**
	 * Gets the reorderColumns value for this AvailableOperation.
	 * 
	 * @return reorderColumns
	 */
	public java.lang.Boolean getReorderColumns() {
		return reorderColumns;
	}

	/**
	 * Sets the reorderColumns value for this AvailableOperation.
	 * 
	 * @param reorderColumns
	 */
	public void setReorderColumns(java.lang.Boolean reorderColumns) {
		this.reorderColumns = reorderColumns;
	}

	/**
	 * Gets the filter value for this AvailableOperation.
	 * 
	 * @return filter
	 */
	public java.lang.Boolean getFilter() {
		return filter;
	}

	/**
	 * Sets the filter value for this AvailableOperation.
	 * 
	 * @param filter
	 */
	public void setFilter(java.lang.Boolean filter) {
		this.filter = filter;
	}

	/**
	 * Gets the calculation value for this AvailableOperation.
	 * 
	 * @return calculation
	 */
	public java.lang.Boolean getCalculation() {
		return calculation;
	}

	/**
	 * Sets the calculation value for this AvailableOperation.
	 * 
	 * @param calculation
	 */
	public void setCalculation(java.lang.Boolean calculation) {
		this.calculation = calculation;
	}

	/**
	 * Gets the aggregation value for this AvailableOperation.
	 * 
	 * @return aggregation
	 */
	public java.lang.Boolean getAggregation() {
		return aggregation;
	}

	/**
	 * Sets the aggregation value for this AvailableOperation.
	 * 
	 * @param aggregation
	 */
	public void setAggregation(java.lang.Boolean aggregation) {
		this.aggregation = aggregation;
	}

	/**
	 * Gets the changeFont value for this AvailableOperation.
	 * 
	 * @return changeFont
	 */
	public java.lang.Boolean getChangeFont() {
		return changeFont;
	}

	/**
	 * Sets the changeFont value for this AvailableOperation.
	 * 
	 * @param changeFont
	 */
	public void setChangeFont(java.lang.Boolean changeFont) {
		this.changeFont = changeFont;
	}

	/**
	 * Gets the format value for this AvailableOperation.
	 * 
	 * @return format
	 */
	public java.lang.Boolean getFormat() {
		return format;
	}

	/**
	 * Sets the format value for this AvailableOperation.
	 * 
	 * @param format
	 */
	public void setFormat(java.lang.Boolean format) {
		this.format = format;
	}

	/**
	 * Gets the text value for this AvailableOperation.
	 * 
	 * @return text
	 */
	public java.lang.Boolean getText() {
		return text;
	}

	/**
	 * Sets the text value for this AvailableOperation.
	 * 
	 * @param text
	 */
	public void setText(java.lang.Boolean text) {
		this.text = text;
	}

	/**
	 * Gets the alignLeft value for this AvailableOperation.
	 * 
	 * @return alignLeft
	 */
	public java.lang.Boolean getAlignLeft() {
		return alignLeft;
	}

	/**
	 * Sets the alignLeft value for this AvailableOperation.
	 * 
	 * @param alignLeft
	 */
	public void setAlignLeft(java.lang.Boolean alignLeft) {
		this.alignLeft = alignLeft;
	}

	/**
	 * Gets the alignCenter value for this AvailableOperation.
	 * 
	 * @return alignCenter
	 */
	public java.lang.Boolean getAlignCenter() {
		return alignCenter;
	}

	/**
	 * Sets the alignCenter value for this AvailableOperation.
	 * 
	 * @param alignCenter
	 */
	public void setAlignCenter(java.lang.Boolean alignCenter) {
		this.alignCenter = alignCenter;
	}

	/**
	 * Gets the alignRight value for this AvailableOperation.
	 * 
	 * @return alignRight
	 */
	public java.lang.Boolean getAlignRight() {
		return alignRight;
	}

	/**
	 * Sets the alignRight value for this AvailableOperation.
	 * 
	 * @param alignRight
	 */
	public void setAlignRight(java.lang.Boolean alignRight) {
		this.alignRight = alignRight;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof AvailableOperation))
			return false;
		AvailableOperation other = (AvailableOperation) obj;
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
				&& ((this.saveView == null && other.getSaveView() == null)
						|| (this.saveView != null && this.saveView.equals(other.getSaveView())))
				&& ((this.applyView == null && other.getApplyView() == null)
						|| (this.applyView != null && this.applyView.equals(other.getApplyView())))
				&& ((this.print == null && other.getPrint() == null)
						|| (this.print != null && this.print.equals(other.getPrint())))
				&& ((this.export == null && other.getExport() == null)
						|| (this.export != null && this.export.equals(other.getExport())))
				&& ((this.toc == null && other.getToc() == null)
						|| (this.toc != null && this.toc.equals(other.getToc())))
				&& ((this.undo == null && other.getUndo() == null)
						|| (this.undo != null && this.undo.equals(other.getUndo())))
				&& ((this.redo == null && other.getRedo() == null)
						|| (this.redo != null && this.redo.equals(other.getRedo())))
				&& ((this.sortAsc == null && other.getSortAsc() == null)
						|| (this.sortAsc != null && this.sortAsc.equals(other.getSortAsc())))
				&& ((this.sortDsc == null && other.getSortDsc() == null)
						|| (this.sortDsc != null && this.sortDsc.equals(other.getSortDsc())))
				&& ((this.advancedSort == null && other.getAdvancedSort() == null)
						|| (this.advancedSort != null && this.advancedSort.equals(other.getAdvancedSort())))
				&& ((this.addGroup == null && other.getAddGroup() == null)
						|| (this.addGroup != null && this.addGroup.equals(other.getAddGroup())))
				&& ((this.deleteGroup == null && other.getDeleteGroup() == null)
						|| (this.deleteGroup != null && this.deleteGroup.equals(other.getDeleteGroup())))
				&& ((this.hideColumn == null && other.getHideColumn() == null)
						|| (this.hideColumn != null && this.hideColumn.equals(other.getHideColumn())))
				&& ((this.showColumns == null && other.getShowColumns() == null)
						|| (this.showColumns != null && this.showColumns.equals(other.getShowColumns())))
				&& ((this.reorderColumns == null && other.getReorderColumns() == null)
						|| (this.reorderColumns != null && this.reorderColumns.equals(other.getReorderColumns())))
				&& ((this.filter == null && other.getFilter() == null)
						|| (this.filter != null && this.filter.equals(other.getFilter())))
				&& ((this.calculation == null && other.getCalculation() == null)
						|| (this.calculation != null && this.calculation.equals(other.getCalculation())))
				&& ((this.aggregation == null && other.getAggregation() == null)
						|| (this.aggregation != null && this.aggregation.equals(other.getAggregation())))
				&& ((this.changeFont == null && other.getChangeFont() == null)
						|| (this.changeFont != null && this.changeFont.equals(other.getChangeFont())))
				&& ((this.format == null && other.getFormat() == null)
						|| (this.format != null && this.format.equals(other.getFormat())))
				&& ((this.text == null && other.getText() == null)
						|| (this.text != null && this.text.equals(other.getText())))
				&& ((this.alignLeft == null && other.getAlignLeft() == null)
						|| (this.alignLeft != null && this.alignLeft.equals(other.getAlignLeft())))
				&& ((this.alignCenter == null && other.getAlignCenter() == null)
						|| (this.alignCenter != null && this.alignCenter.equals(other.getAlignCenter())))
				&& ((this.alignRight == null && other.getAlignRight() == null)
						|| (this.alignRight != null && this.alignRight.equals(other.getAlignRight())));
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
		if (getSaveView() != null) {
			_hashCode += getSaveView().hashCode();
		}
		if (getApplyView() != null) {
			_hashCode += getApplyView().hashCode();
		}
		if (getPrint() != null) {
			_hashCode += getPrint().hashCode();
		}
		if (getExport() != null) {
			_hashCode += getExport().hashCode();
		}
		if (getToc() != null) {
			_hashCode += getToc().hashCode();
		}
		if (getUndo() != null) {
			_hashCode += getUndo().hashCode();
		}
		if (getRedo() != null) {
			_hashCode += getRedo().hashCode();
		}
		if (getSortAsc() != null) {
			_hashCode += getSortAsc().hashCode();
		}
		if (getSortDsc() != null) {
			_hashCode += getSortDsc().hashCode();
		}
		if (getAdvancedSort() != null) {
			_hashCode += getAdvancedSort().hashCode();
		}
		if (getAddGroup() != null) {
			_hashCode += getAddGroup().hashCode();
		}
		if (getDeleteGroup() != null) {
			_hashCode += getDeleteGroup().hashCode();
		}
		if (getHideColumn() != null) {
			_hashCode += getHideColumn().hashCode();
		}
		if (getShowColumns() != null) {
			_hashCode += getShowColumns().hashCode();
		}
		if (getReorderColumns() != null) {
			_hashCode += getReorderColumns().hashCode();
		}
		if (getFilter() != null) {
			_hashCode += getFilter().hashCode();
		}
		if (getCalculation() != null) {
			_hashCode += getCalculation().hashCode();
		}
		if (getAggregation() != null) {
			_hashCode += getAggregation().hashCode();
		}
		if (getChangeFont() != null) {
			_hashCode += getChangeFont().hashCode();
		}
		if (getFormat() != null) {
			_hashCode += getFormat().hashCode();
		}
		if (getText() != null) {
			_hashCode += getText().hashCode();
		}
		if (getAlignLeft() != null) {
			_hashCode += getAlignLeft().hashCode();
		}
		if (getAlignCenter() != null) {
			_hashCode += getAlignCenter().hashCode();
		}
		if (getAlignRight() != null) {
			_hashCode += getAlignRight().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			AvailableOperation.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AvailableOperation"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("saveView");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SaveView"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("applyView");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ApplyView"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("print");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Print"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("export");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("toc");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Toc"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("undo");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Undo"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("redo");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Redo"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("sortAsc");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortAsc"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("sortDsc");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDsc"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("advancedSort");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AdvancedSort"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("addGroup");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AddGroup"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("deleteGroup");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DeleteGroup"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("hideColumn");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "HideColumn"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("showColumns");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ShowColumns"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("reorderColumns");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReorderColumns"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("filter");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Filter"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("calculation");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Calculation"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("aggregation");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Aggregation"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("changeFont");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChangeFont"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("format");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("text");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Text"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("alignLeft");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AlignLeft"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("alignCenter");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AlignCenter"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("alignRight");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AlignRight"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
