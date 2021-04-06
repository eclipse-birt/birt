/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Hierarchy;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.Measure;
import org.eclipse.birt.report.model.elements.olap.MeasureGroup;
import org.eclipse.birt.report.model.elements.olap.OdaCube;
import org.eclipse.birt.report.model.elements.olap.OdaDimension;
import org.eclipse.birt.report.model.elements.olap.OdaHierarchy;
import org.eclipse.birt.report.model.elements.olap.OdaLevel;
import org.eclipse.birt.report.model.elements.olap.OdaMeasure;
import org.eclipse.birt.report.model.elements.olap.OdaMeasureGroup;
import org.eclipse.birt.report.model.elements.olap.TabularCube;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.elements.olap.TabularHierarchy;
import org.eclipse.birt.report.model.elements.olap.TabularLevel;
import org.eclipse.birt.report.model.elements.olap.TabularMeasure;
import org.eclipse.birt.report.model.elements.olap.TabularMeasureGroup;

/**
 * The base class for defining algorithms that traverse the design tree. The
 * derived class overrides the various <code>visitMumble</code> methods to
 * perform tasks specific to that element.
 * 
 */

class ElementVisitorImpl {

	/**
	 * Visits the free form element.
	 * 
	 * @param obj the free form to traverse
	 */

	public void visitFreeForm(FreeForm obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the data set element.
	 * 
	 * @param obj the data set to traverse
	 */

	public void visitDataSet(DataSet obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the data set element.
	 * 
	 * @param obj the data set to traverse
	 */

	public void visitSimpleDataSet(SimpleDataSet obj) {
		visitDataSet(obj);
	}

	/**
	 * Visits the script data set element.
	 * 
	 * @param obj the script data set to traverse
	 */

	public void visitScriptDataSet(ScriptDataSet obj) {
		visitSimpleDataSet(obj);
	}

	/**
	 * Visits the data source element.
	 * 
	 * @param obj the data source to traverse
	 */

	public void visitDataSource(DataSource obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the script data source element.
	 * 
	 * @param obj the script data source to traverse.
	 */

	public void visitScriptDataSource(ScriptDataSource obj) {
		visitDataSource(obj);
	}

	/**
	 * Visits the label element.
	 * 
	 * @param obj the label to traverse
	 */

	public void visitLabel(Label obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the special field element.
	 * 
	 * @param obj the label to traverse
	 */
	public void visitAutoText(AutoText obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the data item element.
	 * 
	 * @param obj the data item to traverse
	 */

	public void visitDataItem(DataItem obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the multi-line data item element.
	 * 
	 * @param obj the multi-line data item to traverse
	 */

	public void visitTextDataItem(TextDataItem obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the text item element.
	 * 
	 * @param obj the text item to traverse
	 */

	public void visitTextItem(TextItem obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the image item element.
	 * 
	 * @param obj the image item to traverse
	 */

	public void visitImage(ImageItem obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the listing item element.
	 * 
	 * @param obj the listing item to traverse
	 */

	public void visitListing(ListingElement obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the list item element.
	 * 
	 * @param obj the list item to traverse
	 */

	public void visitList(ListItem obj) {
		visitListing(obj);
	}

	/**
	 * Visits the group element,such as table group and list group.
	 * 
	 * @param obj the group element to traverse
	 */

	public void visitGroup(GroupElement obj) {
	}

	/**
	 * Visits the list group element.
	 * 
	 * @param obj the list group to traverse
	 */

	public void visitListGroup(ListGroup obj) {
		visitGroup(obj);
	}

	/**
	 * Visits the table item element.
	 * 
	 * @param obj the table item to traverse
	 */

	public void visitTable(TableItem obj) {
		visitListing(obj);
	}

	/**
	 * Visits the table group element.
	 * 
	 * @param obj the table group to traverse
	 */

	public void visitTableGroup(TableGroup obj) {
		visitGroup(obj);
	}

	/**
	 * Visits the cell element.
	 * 
	 * @param obj the cell to traverse
	 */

	public void visitCell(Cell obj) {
	}

	/**
	 * Visits the column element.
	 * 
	 * @param obj the column to traverse
	 */

	public void visitColumn(TableColumn obj) {
	}

	/**
	 * Visits the row element.
	 * 
	 * @param obj the row to traverse
	 */

	public void visitRow(TableRow obj) {
	}

	/**
	 * Visits the grid item element.
	 * 
	 * @param obj the grid item to traverse
	 */

	public void visitGrid(GridItem obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the library element.
	 * 
	 * @param obj the library to traverse
	 */

	public void visitLibrary(Library obj) {
		visitLayoutModule(obj);
	}

	/**
	 * Visits the line item element.
	 * 
	 * @param obj the line item to traverse
	 */

	public void visitLine(LineItem obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the abstract master page element.
	 * 
	 * @param obj the abstract master page to traverse
	 */

	public void visitMasterPage(MasterPage obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the graphic master page element.
	 * 
	 * @param obj the graphic master page to traverse
	 */

	public void visitGraphicMasterPage(GraphicMasterPage obj) {
		visitMasterPage(obj);
	}

	/**
	 * Visits the simple master page element.
	 * 
	 * @param obj the simple master page to traverse
	 */

	public void visitSimpleMasterPage(SimpleMasterPage obj) {
		visitMasterPage(obj);
	}

	/**
	 * Visits the parameter group element.
	 * 
	 * @param obj the parameter group to traverse
	 */

	public void visitParameterGroup(ParameterGroup obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the cascading parameter group element.
	 * 
	 * @param obj the cascading parameter group to traverse
	 */

	public void visitCascadingParameterGroup(CascadingParameterGroup obj) {
		visitParameterGroup(obj);
	}

	/**
	 * Visits the report design element.
	 * 
	 * @param obj the report design to traverse
	 */

	public void visitReportDesign(ReportDesign obj) {
		visitLayoutModule(obj);
	}

	/**
	 * Visits the scalar parameter element.
	 * 
	 * @param obj the scalar parameter to traverse
	 */

	public void visitScalarParameter(ScalarParameter obj) {
		visitAbstractScalarParameter(obj);
	}

	/**
	 * Visits DynamicFilterParameter element
	 * 
	 * @param obj the DynamicFilterParameter to traverse
	 */
	public void visitDynamicFilterParameter(DynamicFilterParameter obj) {
		visitAbstractScalarParameter(obj);
	}

	/**
	 * Visits the abstract scalar parameter element.
	 * 
	 * @param obj the abstract scalar parameter element.
	 */
	public void visitAbstractScalarParameter(AbstractScalarParameter obj) {
		visitParameter(obj);
	}

	/**
	 * Visits the style element.
	 * 
	 * @param obj the style to traverse
	 */

	public void visitStyle(Style obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the parameter element.
	 * 
	 * @param obj the parameter to traverse
	 */

	public void visitParameter(Parameter obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the report item element.
	 * 
	 * @param obj the report item to traverse
	 */

	public void visitReportItem(ReportItem obj) {
		visitStyledElement(obj);
	}

	/**
	 * Visits the styled element( element with a style ).
	 * 
	 * @param obj the styled element to traverse
	 */

	public void visitStyledElement(StyledElement obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the design element.
	 * 
	 * @param obj the design element to traverse
	 */

	public void visitDesignElement(DesignElement obj) {
	}

	/**
	 * Visits the rectangle item element.
	 * 
	 * @param obj the rectangle item to traverse
	 */

	public void visitRectangle(RectangleItem obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the extended item element.
	 * 
	 * @param obj the extended item to traverse
	 */

	public void visitExtendedItem(ExtendedItem obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the extended data source.
	 * 
	 * @param obj the element to traverse
	 */

	public void visitOdaDataSource(OdaDataSource obj) {
		visitDataSource(obj);
	}

	/**
	 * Visits the extended data set.
	 * 
	 * @param obj the element to traverse
	 */

	public void visitOdaDataSet(OdaDataSet obj) {
		visitSimpleDataSet(obj);
	}

	/**
	 * Visits the lay out module.
	 * 
	 * @param obj the module to traverse
	 */

	public void visitLayoutModule(Module obj) {
		visitModule(obj);
	}

	/**
	 * Visits the module.
	 * 
	 * @param obj the module to traverse.
	 */
	public void visitModule(Module obj) {
		visitDesignElement(obj);
	}

	public void visitAbstractTheme(AbstractTheme obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the theme.
	 * 
	 * @param obj the theme to traverse
	 */

	public void visitTheme(Theme obj) {
		visitAbstractTheme(obj);
	}

	/**
	 * Visits the report item theme.
	 * 
	 * @param obj the report item theme to traverse
	 */

	public void visitReportItemTheme(ReportItemTheme obj) {
		visitAbstractTheme(obj);
	}

	/**
	 * Visits the template parameter definition.
	 * 
	 * @param obj the template parameter definition to traverse
	 */

	public void visitTemplateParameterDefinition(TemplateParameterDefinition obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the template element.
	 * 
	 * @param obj the template element to traverse
	 */

	public void visitTemplateElement(TemplateElement obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the template report item.
	 * 
	 * @param obj the template report item to traverse
	 */

	public void visitTemplateReportItem(TemplateReportItem obj) {
		visitTemplateElement(obj);
	}

	/**
	 * Visits the template data set.
	 * 
	 * @param obj the template data set to traverse
	 */

	public void visitTemplateDataSet(TemplateDataSet obj) {
		visitTemplateElement(obj);
	}

	/**
	 * Visits the joint data set.
	 * 
	 * @param obj the joint data set to traverse
	 */

	public void visitJointDataSet(JointDataSet obj) {
		visitDataSet(obj);
	}

	/**
	 * Visits the cube element.
	 * 
	 * @param obj the cube element to traverse
	 */

	public void visitCube(Cube obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visists the dimension element.
	 * 
	 * @param obj the dimension element to traverse
	 */

	public void visitDimension(Dimension obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visists the hierarchy element.
	 * 
	 * @param obj the hierarchy element to traverse
	 */

	public void visitHierarchy(Hierarchy obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visists the level element.
	 * 
	 * @param obj the level element to traverse
	 */

	public void visitLevel(Level obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visists the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	public void visitMeasure(Measure obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the measure group element.
	 * 
	 * @param obj the measure group to traverse
	 */

	public void visitMeasureGroup(MeasureGroup obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the cube element.
	 * 
	 * @param obj the cube element to traverse
	 */

	public void visitTabularCube(TabularCube obj) {
		visitCube(obj);
	}

	/**
	 * Visists the dimension element.
	 * 
	 * @param obj the dimension element to traverse
	 */

	public void visitTabularDimension(TabularDimension obj) {
		visitDimension(obj);
	}

	/**
	 * Visists the hierarchy element.
	 * 
	 * @param obj the hierarchy element to traverse
	 */

	public void visitTabularHierarchy(TabularHierarchy obj) {
		visitHierarchy(obj);
	}

	/**
	 * Visists the level element.
	 * 
	 * @param obj the level element to traverse
	 */

	public void visitTabularLevel(TabularLevel obj) {
		visitLevel(obj);
	}

	/**
	 * Visists the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	public void visitTabularMeasure(TabularMeasure obj) {
		visitMeasure(obj);
	}

	/**
	 * Visits the measure group element.
	 * 
	 * @param obj the measure group to traverse
	 */

	public void visitTabularMeasureGroup(TabularMeasureGroup obj) {
		visitMeasureGroup(obj);
	}

	/**
	 * Visits the cube element.
	 * 
	 * @param obj the cube element to traverse
	 */

	public void visitOdaCube(OdaCube obj) {
		visitCube(obj);
	}

	/**
	 * Visists the dimension element.
	 * 
	 * @param obj the dimension element to traverse
	 */

	public void visitOdaDimension(OdaDimension obj) {
		visitDimension(obj);
	}

	/**
	 * Visists the hierarchy element.
	 * 
	 * @param obj the hierarchy element to traverse
	 */

	public void visitOdaHierarchy(OdaHierarchy obj) {
		visitHierarchy(obj);
	}

	/**
	 * Visists the level element.
	 * 
	 * @param obj the level element to traverse
	 */

	public void visitOdaLevel(OdaLevel obj) {
		visitLevel(obj);
	}

	/**
	 * Visists the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	public void visitOdaMeasure(OdaMeasure obj) {
		visitMeasure(obj);
	}

	/**
	 * Visits the measure group element.
	 * 
	 * @param obj the measure group to traverse
	 */

	public void visitOdaMeasureGroup(OdaMeasureGroup obj) {
		visitMeasureGroup(obj);
	}

	/**
	 * Visits the member value.
	 * 
	 * @param obj the member value to traverse
	 */
	public void visitMemberValue(MemberValue obj) {
		visitContentElement(obj);
	}

	/**
	 * Visits the filter condition element.
	 * 
	 * @param obj the filter condition element to traverse
	 */

	public void visitFilterConditionElement(FilterConditionElement obj) {
		visitContentElement(obj);
	}

	/**
	 * Visits the sort element.
	 * 
	 * @param obj the sort element to traverse
	 */
	public void visitSortElement(SortElement obj) {
		visitContentElement(obj);
	}

	/**
	 * Visits the multiple view element.
	 * 
	 * @param obj the multiple view element
	 */

	public void visitMultiView(MultiViews obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the variable element.
	 * 
	 * @param obj the variable element to traverse
	 */

	public void visitVariableElement(VariableElement obj) {
		visitContentElement(obj);
	}

	/**
	 * Visits the data group element.
	 * 
	 * @param obj the data group element to traverse
	 */

	public void visitDataGroup(DataGroup obj) {
		visitContentElement(obj);
	}

	/**
	 * Visits the content element.
	 * 
	 * @param obj the content element to traverse
	 */

	protected void visitContentElement(ContentElement obj) {

	}

	/**
	 * Visits the derived data set element.
	 * 
	 * @param obj the derived data set element to traverse
	 */
	public void visitDerivedDataSet(DerivedDataSet obj) {
		visitSimpleDataSet(obj);
	}
}
