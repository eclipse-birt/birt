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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.OdaCubeHandle;
import org.eclipse.birt.report.model.api.olap.OdaDimensionHandle;
import org.eclipse.birt.report.model.api.olap.OdaHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.OdaLevelHandle;
import org.eclipse.birt.report.model.api.olap.OdaMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.OdaMeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AutoText;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.DerivedDataSet;
import org.eclipse.birt.report.model.elements.DynamicFilterParameter;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.LineItem;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.RectangleItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.elements.ScriptDataSource;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TemplateDataSet;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.Theme;
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
 * Applies logic customized to each type of report element. This is an
 * implementation of the classic visitor pattern. The application creates a
 * derived iterator, and overrides methods for the elements of interest. The
 * visitor can also walk the design tree, performing actions on each element
 * down the containment hierarchy.
 * <p>
 * By default, each method calls the method for its parent element. That is, a
 * DataItem method calls the ReportItem method which calls the DesignElement
 * method.
 */

class DesignVisitorImpl {

	/**
	 * The private internal element visitor.
	 */

	protected Forwarder forwarder = null;

	/**
	 * Constructs a <code>DesignVisitor</code>, which is not related with the
	 * specific report.
	 */

	public DesignVisitorImpl() {
		forwarder = new Forwarder();
	}

	/**
	 * Applies this visitor to the given element.
	 * 
	 * @param handle handle to the element to visit.
	 */

	public void apply(DesignElementHandle handle) {
		forwarder.setModule(handle.getModule());
		handle.getElement().apply(forwarder);
	}

	/**
	 * Visits the free form element.
	 * 
	 * @param obj the handle of the free form to traverse
	 */

	protected void visitFreeForm(FreeFormHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits auto text element
	 * 
	 * @param obj
	 * 
	 */

	protected void visitAutoText(AutoTextHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the data set element.
	 * 
	 * @param obj the handle of the data set to traverse
	 */

	protected void visitDataSet(DataSetHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the simple data set element.
	 * 
	 * @param obj the handle of the simple data set to traverse
	 */

	protected void visitSimpleDataSet(DataSetHandle obj) {
		visitDataSet(obj);
	}

	/**
	 * Visits the script data set element.
	 * 
	 * @param obj the handle of the script data set to traverse
	 */

	protected void visitScriptDataSet(ScriptDataSetHandle obj) {
		visitSimpleDataSet(obj);
	}

	/**
	 * Visits the joint data set element.
	 * 
	 * @param obj the handle of the joint data set to traverse
	 */

	protected void visitJointDataSet(JointDataSetHandle obj) {
		visitSimpleDataSet(obj);
	}

	/**
	 * Visits the data source element.
	 * 
	 * @param obj the handle of data source to traverse
	 */

	protected void visitDataSource(DataSourceHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the label element.
	 * 
	 * @param obj the handle of the label to traverse
	 */

	protected void visitLabel(LabelHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the data element.
	 * 
	 * @param obj the handle of the data to traverse
	 */

	protected void visitDataItem(DataItemHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the text element.
	 * 
	 * @param obj the handle of the text to traverse
	 */

	protected void visitTextItem(TextItemHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the image element.
	 * 
	 * @param obj the handle of the image to traverse
	 */

	protected void visitImage(ImageHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the list element.
	 * 
	 * @param obj the handle of the list to traverse
	 */

	protected void visitList(ListHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the group element.
	 * 
	 * @param obj the handle of the group to traverse
	 */

	protected void visitGroup(GroupHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the list group element.
	 * 
	 * @param obj the handle of the list group to traverse
	 */

	protected void visitListGroup(ListGroupHandle obj) {
		visitGroup(obj);
	}

	/**
	 * Visits the table element.
	 * 
	 * @param obj the handle of the table to traverse
	 */

	protected void visitTable(TableHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the table group element.
	 * 
	 * @param obj the handle of the table group to traverse
	 */

	protected void visitTableGroup(TableGroupHandle obj) {
		visitGroup(obj);
	}

	/**
	 * Visits the cell element.
	 * 
	 * @param obj the handle of the cell to traverse
	 */

	protected void visitCell(CellHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the column element.
	 * 
	 * @param obj the handle of the column to traverse
	 */

	protected void visitColumn(ColumnHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the row element.
	 * 
	 * @param obj the handle of the row to traverse
	 */

	protected void visitRow(RowHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the grid element.
	 * 
	 * @param obj the handle of the grid to traverse
	 */

	protected void visitGrid(GridHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the line element.
	 * 
	 * @param obj the handle of the line to traverse
	 */

	protected void visitLine(LineHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the master page element.
	 * 
	 * @param obj the handle of the master page to traverse
	 */

	protected void visitMasterPage(MasterPageHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the parameter group element.
	 * 
	 * @param obj the handle of the parameter group to traverse
	 */

	protected void visitParameterGroup(ParameterGroupHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the cascading parameter group element.
	 * 
	 * @param obj the handle of the cascading parameter group to traverse
	 */
	protected void visitCascadingParameterGroup(CascadingParameterGroupHandle obj) {
		visitParameterGroup(obj);
	}

	/**
	 * Visits the module element
	 * 
	 * @param obj the handle of the module to traverse
	 */

	protected void visitModule(ModuleHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the library element.
	 * 
	 * @param obj the handle of the library to traverse
	 */

	protected void visitLibrary(LibraryHandle obj) {
		visitModule(obj);
	}

	/**
	 * Visits the report design element.
	 * 
	 * @param obj the handle of the report design to traverse
	 */

	protected void visitReportDesign(ReportDesignHandle obj) {
		visitModule(obj);
	}

	/**
	 * Visits the scalar parameter element.
	 * 
	 * @param obj the handle of the scalar parameter to traverse
	 */

	protected void visitScalarParameter(ScalarParameterHandle obj) {
		visitParameter(obj);
	}

	/**
	 * Visits the dynamic filter parameter element.
	 * 
	 * @param obj the handle of the dynamic filter parameter to traverse.
	 */
	protected void visitDynamicFilterParameter(DynamicFilterParameterHandle obj) {
		visitParameter(obj);
	}

	/**
	 * Visits the style element.
	 * 
	 * @param obj the handle of the style to traverse
	 */

	protected void visitStyle(StyleHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the parameter element.
	 * 
	 * @param obj the handle of the parameter to traverse
	 */

	protected void visitParameter(ParameterHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the report item.
	 * 
	 * @param obj the handle of report item to traverse
	 */

	protected void visitReportItem(ReportItemHandle obj) {
		visitStyledElement(obj);
	}

	/**
	 * Visits the styled element.
	 * 
	 * @param obj the handle of styled element to traverse
	 */

	protected void visitStyledElement(ReportItemHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the design element.
	 * 
	 * @param obj the handle of design element to traverse
	 */

	protected void visitDesignElement(DesignElementHandle obj) {
	}

	/**
	 * Visits the rectangle element.
	 * 
	 * @param obj the handle of rectangle to traverse
	 */

	protected void visitRectangle(RectangleHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the content of the slot.
	 * 
	 * @param slot the handle of a slot to traverse
	 */

	protected void visitContents(SlotHandle slot) {
		forwarder.visitContents(slot.getModule(), new ContainerContext(slot.getElement(), slot.getSlotID()));
	}

	/**
	 * Visits the content of this property.
	 * 
	 * @param obj      the container element where the contents reside
	 * @param propName name of the property where the contents reside
	 */
	protected void visitContents(DesignElementHandle obj, String propName) {
		forwarder.visitContents(obj.getModule(), new ContainerContext(obj.getElement(), propName));
	}

	/**
	 * Visits the multi-line data element.
	 * 
	 * @param obj the handle of a multi-line data to traverse
	 */

	protected void visitTextDataItem(TextDataHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the extended element.
	 * 
	 * @param obj the handle of an extended element to traverse
	 */

	protected void visitExtendedItem(ExtendedItemHandle obj) {
		visitReportItem(obj);
	}

	/**
	 * Visits the extended element.
	 * 
	 * @param obj the handle of an extended element to traverse
	 */

	protected void visitScriptDataSource(ScriptDataSourceHandle obj) {
		visitDataSource(obj);
	}

	/**
	 * Visits the graphic master page element.
	 * 
	 * @param obj the handle of a graphic master page to traverse
	 */

	protected void visitGraphicMasterPage(GraphicMasterPageHandle obj) {
		visitMasterPage(obj);
	}

	/**
	 * Visits the simple master page element.
	 * 
	 * @param obj the handle of a simple master page to traverse
	 */

	protected void visitSimpleMasterPage(SimpleMasterPageHandle obj) {
		visitMasterPage(obj);
	}

	/**
	 * Visits the extended data source element.
	 * 
	 * @param obj the handle of a extended data source to traverse
	 */

	protected void visitExtendedDataSource(OdaDataSourceHandle obj) {
		visitDataSource(obj);
	}

	/**
	 * Visits the extended data set element.
	 * 
	 * @param obj the extended data set to traverse
	 */

	protected void visitExtendedDataSet(OdaDataSetHandle obj) {
		visitDataSet(obj);
	}

	/**
	 * Visits the theme element.
	 * 
	 * @param obj the theme to traverse
	 */

	protected void visitTheme(ThemeHandle obj) {
		visitAbstractTheme(obj);
	}

	/**
	 * Visits the template parameter definition.
	 * 
	 * @param obj the template parameter definition to traverse
	 */

	protected void visitTemplateParameterDefinition(TemplateParameterDefinitionHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the template element.
	 * 
	 * @param obj the template element to traverse
	 */

	protected void visitTemplateElement(TemplateElementHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the template report item.
	 * 
	 * @param obj the template report item to traverse
	 */

	protected void visitTemplateReportItem(TemplateReportItemHandle obj) {
		visitTemplateElement(obj);
	}

	/**
	 * Visits the template data set.
	 * 
	 * @param obj the template data set to traverse
	 */

	protected void visitTemplateDataSet(TemplateDataSetHandle obj) {
		visitTemplateElement(obj);
	}

	/**
	 * Visits the cube element.
	 * 
	 * @param obj the cube element to traverse
	 */

	protected void visitTabularCube(TabularCubeHandle obj) {
		visitCube(obj);
	}

	/**
	 * Visits the dimension element.
	 * 
	 * @param obj the dimension element to traverse
	 */

	protected void visitTabularDimension(TabularDimensionHandle obj) {
		visitDimension(obj);
	}

	/**
	 * Visits the hierarchy element.
	 * 
	 * @param obj the hierarchy element to traverse
	 */

	protected void visitTabularHierarchy(TabularHierarchyHandle obj) {
		visitHierarchy(obj);
	}

	/**
	 * Visits the level element.
	 * 
	 * @param obj the level element to traverse
	 */

	protected void visitTabularLevel(TabularLevelHandle obj) {
		visitLevel(obj);
	}

	/**
	 * Visits the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	protected void visitTabularMeasure(TabularMeasureHandle obj) {
		visitMeasure(obj);
	}

	/**
	 * Visits the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	protected void visitTabularMeasureGroup(TabularMeasureGroupHandle obj) {
		visitMeasureGroup(obj);
	}

	/**
	 * Visits the cube element.
	 * 
	 * @param obj the cube element to traverse
	 */

	protected void visitOdaCube(OdaCubeHandle obj) {
		visitCube(obj);
	}

	/**
	 * Visits the dimension element.
	 * 
	 * @param obj the dimension element to traverse
	 */

	protected void visitOdaDimension(OdaDimensionHandle obj) {
		visitDimension(obj);
	}

	/**
	 * Visits the hierarchy element.
	 * 
	 * @param obj the hierarchy element to traverse
	 */

	protected void visitOdaHierarchy(OdaHierarchyHandle obj) {
		visitHierarchy(obj);
	}

	/**
	 * Visits the level element.
	 * 
	 * @param obj the level element to traverse
	 */

	protected void visitOdaLevel(OdaLevelHandle obj) {
		visitLevel(obj);
	}

	/**
	 * Visits the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	protected void visitOdaMeasure(OdaMeasureHandle obj) {
		visitMeasure(obj);
	}

	/**
	 * Visits the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	protected void visitOdaMeasureGroup(OdaMeasureGroupHandle obj) {
		visitMeasureGroup(obj);
	}

	/**
	 * Visits the cube element.
	 * 
	 * @param obj the cube element to traverse
	 */

	protected void visitCube(CubeHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the dimension element.
	 * 
	 * @param obj the dimension element to traverse
	 */

	protected void visitDimension(DimensionHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the hierarchy element.
	 * 
	 * @param obj the hierarchy element to traverse
	 */

	protected void visitHierarchy(HierarchyHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the level element.
	 * 
	 * @param obj the level element to traverse
	 */

	protected void visitLevel(LevelHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	protected void visitMeasure(MeasureHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the measure element.
	 * 
	 * @param obj the measure element to traverse
	 */

	protected void visitMeasureGroup(MeasureGroupHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * Visits the derived data set element.
	 * 
	 * @param obj the derived data set element to traverse
	 */
	protected void visitDerivedDataSet(DerivedDataSetHandle obj) {
		visitDataSet(obj);
	}

	/**
	 * Visits the report item theme element.
	 * 
	 * @param obj the report item theme to traverse
	 */
	protected void visitReportItemTheme(ReportItemThemeHandle obj) {
		visitAbstractTheme(obj);
	}

	/**
	 * Visits the abstract theme element.
	 * 
	 * @param obj the abstract theme to traverse
	 */
	protected void visitAbstractTheme(AbstractThemeHandle obj) {
		visitDesignElement(obj);
	}

	/**
	 * A class forward the visit of an element to its handle.
	 * 
	 */

	protected class Forwarder extends ElementVisitor {

		/**
		 * The module which this visitor visits.
		 */

		protected Module module = null;

		/**
		 * Visits the free form element.
		 * 
		 * @param obj the free form to traverse
		 */

		public void visitFreeForm(FreeForm obj) {
			DesignVisitorImpl.this.visitFreeForm(obj.handle(module));
		}

		/**
		 * The module which this visitor visits.
		 * 
		 * @param module the module
		 */

		void setModule(Module module) {
			this.module = module;
		}

		/**
		 * Visits the label element.
		 * 
		 * @param obj the label to traverse
		 */

		public void visitLabel(Label obj) {
			DesignVisitorImpl.this.visitLabel(obj.handle(module));
		}

		/**
		 * Visits the auto text element
		 * 
		 * @param obj auto text to traverse
		 */

		public void visitAutoText(AutoText obj) {
			DesignVisitorImpl.this.visitAutoText(obj.handle(module));
		}

		/**
		 * Visits the data element.
		 * 
		 * @param obj the data element to traverse
		 */

		public void visitDataItem(DataItem obj) {
			DesignVisitorImpl.this.visitDataItem(obj.handle(module));
		}

		/**
		 * Visits the text element.
		 * 
		 * @param obj the text to traverse
		 */

		public void visitTextItem(TextItem obj) {
			DesignVisitorImpl.this.visitTextItem(obj.handle(module));
		}

		/**
		 * Visits the image element.
		 * 
		 * @param obj the image to traverse
		 */

		public void visitImage(ImageItem obj) {
			DesignVisitorImpl.this.visitImage(obj.handle(module));
		}

		/**
		 * Visits the list element.
		 * 
		 * @param obj the list to traverse
		 */

		public void visitList(ListItem obj) {
			DesignVisitorImpl.this.visitList(obj.handle(module));
		}

		/**
		 * Visits the list group element.
		 * 
		 * @param obj the list group to traverse
		 */

		public void visitListGroup(ListGroup obj) {
			DesignVisitorImpl.this.visitListGroup(obj.handle(module));
		}

		/**
		 * Visits the table element.
		 * 
		 * @param obj the table to traverse
		 */

		public void visitTable(TableItem obj) {
			DesignVisitorImpl.this.visitTable(obj.handle(module));
		}

		/**
		 * Visits the table group element.
		 * 
		 * @param obj the table group to traverse
		 */

		public void visitTableGroup(TableGroup obj) {
			DesignVisitorImpl.this.visitTableGroup(obj.handle(module));
		}

		/**
		 * Visits the cell element.
		 * 
		 * @param obj the cell to traverse
		 */

		public void visitCell(Cell obj) {
			DesignVisitorImpl.this.visitCell(obj.handle(module));
		}

		/**
		 * Visits the column element.
		 * 
		 * @param obj the column to traverse
		 */

		public void visitColumn(TableColumn obj) {
			DesignVisitorImpl.this.visitColumn(obj.handle(module));
		}

		/**
		 * Visits the row element.
		 * 
		 * @param obj the row to traverse
		 */

		public void visitRow(TableRow obj) {
			DesignVisitorImpl.this.visitRow(obj.handle(module));
		}

		/**
		 * Visits the grid element.
		 * 
		 * @param obj the grid to traverse
		 */

		public void visitGrid(GridItem obj) {
			DesignVisitorImpl.this.visitGrid(obj.handle(module));
		}

		/**
		 * Visits the line element.
		 * 
		 * @param obj the line to traverse
		 */

		public void visitLine(LineItem obj) {
			DesignVisitorImpl.this.visitLine(obj.handle(module));
		}

		/**
		 * Visits the parameter group element.
		 * 
		 * @param obj the parameter group to traverse
		 */

		public void visitParameterGroup(ParameterGroup obj) {
			DesignVisitorImpl.this.visitParameterGroup(obj.handle(module));
		}

		/**
		 * Visits the cascading parameter group element.
		 * 
		 * @param obj the cascading parameter group to traverse
		 */

		public void visitCascadingParameterGroup(CascadingParameterGroup obj) {
			DesignVisitorImpl.this.visitCascadingParameterGroup((CascadingParameterGroupHandle) obj.handle(module));
		}

		/**
		 * Visits the library.
		 * 
		 * @param obj the library to traverse
		 */

		public void visitLibrary(Library obj) {
			DesignVisitorImpl.this.visitLibrary(obj.handle());
		}

		/**
		 * Visits the report design.
		 * 
		 * @param obj the report design to traverse
		 */

		public void visitReportDesign(ReportDesign obj) {
			DesignVisitorImpl.this.visitReportDesign(obj.handle());
		}

		/**
		 * Visits the scalar parameter.
		 * 
		 * @param obj the scalar parameter to traverse
		 */

		public void visitScalarParameter(ScalarParameter obj) {
			DesignVisitorImpl.this.visitScalarParameter(obj.handle(module));
		}

		/**
		 * Visits the dynamic filter parameter.
		 * 
		 * @param obj the dynamic filter parameter to traverse
		 */
		public void visitDynamicFilterParameter(DynamicFilterParameter obj) {
			DesignVisitorImpl.this.visitDynamicFilterParameter(obj.handle(module));
		}

		/**
		 * Visits the style element.
		 * 
		 * @param obj the style to traverse
		 */

		public void visitStyle(Style obj) {
			DesignVisitorImpl.this.visitStyle(obj.handle(module));
		}

		/**
		 * Visits the rectangle element.
		 * 
		 * @param obj the rectangle to traverse
		 */

		public void visitRectangle(RectangleItem obj) {
			DesignVisitorImpl.this.visitRectangle(obj.handle(module));
		}

		/**
		 * Visits the multiline data item.
		 * 
		 * @param obj the multiline data to traverse
		 */

		public void visitTextDataItem(TextDataItem obj) {
			DesignVisitorImpl.this.visitTextDataItem(obj.handle(module));
		}

		/**
		 * Visits the extended item.
		 * 
		 * @param obj the extended item to traverse
		 */

		public void visitExtendedItem(ExtendedItem obj) {
			DesignVisitorImpl.this.visitExtendedItem(obj.handle(module));
		}

		/**
		 * Visits the script data source element.
		 * 
		 * @param obj the script data source to traverse
		 */

		public void visitScriptDataSource(ScriptDataSource obj) {
			DesignVisitorImpl.this.visitScriptDataSource(obj.handle(module));
		}

		/**
		 * Visits the graphic master page element.
		 * 
		 * @param obj the graphic master page to traverse
		 */

		public void visitGraphicMasterPage(GraphicMasterPage obj) {
			DesignVisitorImpl.this.visitGraphicMasterPage(obj.handle(module));
		}

		/**
		 * Visits the simple master page element.
		 * 
		 * @param obj the simple master page to traverse
		 */

		public void visitSimpleMasterPage(SimpleMasterPage obj) {
			DesignVisitorImpl.this.visitSimpleMasterPage(obj.handle(module));
		}

		/**
		 * Visits the extended data source element.
		 * 
		 * @param obj the extended data source to traverse
		 */

		public void visitOdaDataSource(OdaDataSource obj) {
			DesignVisitorImpl.this.visitExtendedDataSource(obj.handle(module));
		}

		/**
		 * Visits the script data set element.
		 * 
		 * @param obj the script data set to traverse
		 */

		public void visitScriptDataSet(ScriptDataSet obj) {
			DesignVisitorImpl.this.visitScriptDataSet(obj.handle(module));
		}

		/**
		 * Visits the extended data set element.
		 * 
		 * @param obj the extended data set to traverse
		 */

		public void visitOdaDataSet(OdaDataSet obj) {
			DesignVisitorImpl.this.visitExtendedDataSet(obj.handle(module));
		}

		/**
		 * Visits the joint data set element.
		 * 
		 * @param obj the handle of the joint data set to traverse
		 */

		public void visitJointDataSet(JointDataSet obj) {
			DesignVisitorImpl.this.visitJointDataSet(obj.handle(module));
		}

		/**
		 * Visits the theme element.
		 * 
		 * @param obj the theme to traverse
		 */

		public void visitTheme(Theme obj) {
			DesignVisitorImpl.this.visitTheme(obj.handle(module));
		}

		/**
		 * Visits the template parameter definition.
		 * 
		 * @param obj the template parameter definition to traverse
		 */

		public void visitTemplateParameterDefinition(TemplateParameterDefinition obj) {
			DesignVisitorImpl.this.visitTemplateParameterDefinition(obj.handle(module));
		}

		/**
		 * Visits the template report item.
		 * 
		 * @param obj the template report item to traverse
		 */

		public void visitTemplateReportItem(TemplateReportItem obj) {
			DesignVisitorImpl.this.visitTemplateReportItem(obj.handle(module));
		}

		/**
		 * Visits the template data set.
		 * 
		 * @param obj the template data set to traverse
		 */

		public void visitTemplateDataSet(TemplateDataSet obj) {
			DesignVisitorImpl.this.visitTemplateDataSet(obj.handle(module));
		}

		/**
		 * Visits the cube element.
		 * 
		 * @param obj the cube element
		 */

		public void visitTabularCube(TabularCube obj) {
			DesignVisitorImpl.this.visitTabularCube(obj.handle(module));
		}

		/**
		 * Visits the dimension element.
		 * 
		 * @param obj the dimension element
		 */

		public void visitTabularDimension(TabularDimension obj) {
			DesignVisitorImpl.this.visitTabularDimension(obj.handle(module));
		}

		/**
		 * Visits the hierarchy element.
		 * 
		 * @param obj the hierarchy element
		 */

		public void visitTabularHierarchy(TabularHierarchy obj) {
			DesignVisitorImpl.this.visitTabularHierarchy(obj.handle(module));
		}

		/**
		 * Visits the level element.
		 * 
		 * @param obj the level element
		 */

		public void visitTabularLevel(TabularLevel obj) {
			DesignVisitorImpl.this.visitTabularLevel(obj.handle(module));
		}

		/**
		 * Visits the measure element.
		 * 
		 * @param obj the measure element
		 */

		public void visitTabularMeasure(TabularMeasure obj) {
			DesignVisitorImpl.this.visitTabularMeasure(obj.handle(module));
		}

		/**
		 * Visits the measure group.
		 * 
		 * @param obj the measure group
		 */
		public void visitTabularMeasureGroup(TabularMeasureGroup obj) {
			DesignVisitorImpl.this.visitTabularMeasureGroup(obj.handle(module));
		}

		/**
		 * Visits the cube element.
		 * 
		 * @param obj the cube element
		 */

		public void visitOdaCube(OdaCube obj) {
			DesignVisitorImpl.this.visitOdaCube(obj.handle(module));
		}

		/**
		 * Visits the dimension element.
		 * 
		 * @param obj the dimension element
		 */

		public void visitOdaDimension(OdaDimension obj) {
			DesignVisitorImpl.this.visitOdaDimension(obj.handle(module));
		}

		/**
		 * Visits the hierarchy element.
		 * 
		 * @param obj the hierarchy element
		 */

		public void visitOdaHierarchy(OdaHierarchy obj) {
			DesignVisitorImpl.this.visitOdaHierarchy(obj.handle(module));
		}

		/**
		 * Visits the level element.
		 * 
		 * @param obj the level element
		 */

		public void visitOdaLevel(OdaLevel obj) {
			DesignVisitorImpl.this.visitOdaLevel(obj.handle(module));
		}

		/**
		 * Visits the measure element.
		 * 
		 * @param obj the measure element
		 */

		public void visitOdaMeasure(OdaMeasure obj) {
			DesignVisitorImpl.this.visitOdaMeasure(obj.handle(module));
		}

		/**
		 * Visits the measure group.
		 * 
		 * @param obj the measure group
		 */
		public void visitOdaMeasureGroup(OdaMeasureGroup obj) {
			DesignVisitorImpl.this.visitOdaMeasureGroup(obj.handle(module));
		}

		/**
		 * Visits the derived data set.
		 * 
		 * @param obj the derived data set
		 */
		public void visitDerivedDataSet(DerivedDataSet obj) {
			DesignVisitorImpl.this.visitDerivedDataSet(obj.handle(module));
		}

		/**
		 * Visits the report item theme.
		 * 
		 * @param obj the report item theme to traverse
		 */
		public void visitReportItemTheme(ReportItemTheme obj) {
			DesignVisitorImpl.this.visitReportItemTheme(obj.handle(module));
		}
	}
}