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

import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.ExtendedDataSet;
import org.eclipse.birt.report.model.elements.ExtendedDataSource;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.LineItem;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.MultiLineDataItem;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.RectangleItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.elements.ScriptDataSource;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TextItem;

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

public class DesignVisitor
{

	/**
	 * The report design which this visitor visits.
	 * 
	 * @deprecated
	 */

	protected ReportDesign design = null;

	/**
	 * The private internal element visitor.
	 */

	protected Forwarder forwarder = new Forwarder( );

	/**
	 * Constructs a <code>DesignVisitor</code>, which is not related with the
	 * specific report.
	 */

	public DesignVisitor( )
	{
	}

	/**
	 * Constructs a <code>DesignVisitor</code> with the given
	 * <code>DesignElementHandle</code>.
	 * 
	 * @param handle
	 *            handle to any element in the design, typically the report
	 *            design itself
	 * @deprecated
	 */

	public DesignVisitor( DesignElementHandle handle )
	{
		design = handle.getDesign( );
	}

	/**
	 * Applies this visitor to the given element.
	 * 
	 * @param handle
	 *            handle to the element to visit.
	 */

	public void apply( DesignElementHandle handle )
	{
		forwarder.setDesign( handle.getDesign( ) );
		handle.getElement( ).apply( forwarder );
	}

	/**
	 * Visits the free form element.
	 * 
	 * @param obj
	 *            the handle of the free form to traverse
	 */

	protected void visitFreeForm( FreeFormHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the data set element.
	 * 
	 * @param obj
	 *            the handle of the data set to traverse
	 */

	protected void visitDataSet( DataSetHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the script data set element.
	 * 
	 * @param obj
	 *            the handle of the script data set to traverse
	 */

	protected void visitScriptDataSet( ScriptDataSetHandle obj )
	{
		visitDataSet( obj );
	}

	/**
	 * Visits the data source element.
	 * 
	 * @param obj
	 *            the handle of data source to traverse
	 */

	protected void visitDataSource( DataSourceHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the label element.
	 * 
	 * @param obj
	 *            the handle of the label to traverse
	 */

	protected void visitLabel( LabelHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the data element.
	 * 
	 * @param obj
	 *            the handle of the data to traverse
	 */

	protected void visitDataItem( DataItemHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the text element.
	 * 
	 * @param obj
	 *            the handle of the text to traverse
	 */

	protected void visitTextItem( TextItemHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the image element.
	 * 
	 * @param obj
	 *            the handle of the image to traverse
	 */

	protected void visitImage( ImageHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the list element.
	 * 
	 * @param obj
	 *            the handle of the list to traverse
	 */

	protected void visitList( ListHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the group element.
	 * 
	 * @param obj
	 *            the handle of the group to traverse
	 */

	protected void visitGroup( GroupHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the list group element.
	 * 
	 * @param obj
	 *            the handle of the list group to traverse
	 */

	protected void visitListGroup( ListGroupHandle obj )
	{
		visitGroup( obj );
	}

	/**
	 * Visits the table element.
	 * 
	 * @param obj
	 *            the handle of the table to traverse
	 */

	protected void visitTable( TableHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the table group element.
	 * 
	 * @param obj
	 *            the handle of the table group to traverse
	 */

	protected void visitTableGroup( TableGroupHandle obj )
	{
		visitGroup( obj );
	}

	/**
	 * Visits the cell element.
	 * 
	 * @param obj
	 *            the handle of the cell to traverse
	 */

	protected void visitCell( CellHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the column element.
	 * 
	 * @param obj
	 *            the handle of the column to traverse
	 */

	protected void visitColumn( ColumnHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the row element.
	 * 
	 * @param obj
	 *            the handle of the row to traverse
	 */

	protected void visitRow( RowHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the grid element.
	 * 
	 * @param obj
	 *            the handle of the grid to traverse
	 */

	protected void visitGrid( GridHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the line element.
	 * 
	 * @param obj
	 *            the handle of the line to traverse
	 */

	protected void visitLine( LineHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the master page element.
	 * 
	 * @param obj
	 *            the handle of the master page to traverse
	 */

	protected void visitMasterPage( MasterPageHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the parameter group element.
	 * 
	 * @param obj
	 *            the handle of the parameter group to traverse
	 */

	protected void visitParameterGroup( ParameterGroupHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the report design element.
	 * 
	 * @param obj
	 *            the handle of the report design to traverse
	 */

	protected void visitReportDesign( ReportDesignHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the scalar parameter element.
	 * 
	 * @param obj
	 *            the handle of the scalar parameter to traverse
	 */

	protected void visitScalarParameter( ScalarParameterHandle obj )
	{
		visitParameter( obj );
	}

	/**
	 * Visits the style element.
	 * 
	 * @param obj
	 *            the handle of the style to traverse
	 */

	protected void visitStyle( StyleHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the parameter element.
	 * 
	 * @param obj
	 *            the handle of the parameter to traverse
	 */

	protected void visitParameter( ParameterHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the report item.
	 * 
	 * @param obj
	 *            the handle of report item to traverse
	 */

	protected void visitReportItem( ReportItemHandle obj )
	{
		visitStyledElement( obj );
	}

	/**
	 * Visits the styled element.
	 * 
	 * @param obj
	 *            the handle of styled element to traverse
	 */

	protected void visitStyledElement( ReportItemHandle obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the design element.
	 * 
	 * @param obj
	 *            the handle of design element to traverse
	 */

	protected void visitDesignElement( DesignElementHandle obj )
	{
	}

	/**
	 * Visits the rectangle element.
	 * 
	 * @param obj
	 *            the handle of rectangle to traverse
	 */

	protected void visitRectangle( RectangleHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the content of the slot.
	 * 
	 * @param slot
	 *            the handle of a slot to traverse
	 */

	protected void visitContents( SlotHandle slot )
	{
		forwarder.visitContents( slot.getSlot( ) );
	}

	/**
	 * Visits the multi-line data element.
	 * 
	 * @param obj
	 *            the handle of a multi-line data to traverse
	 */

	protected void visitMultiLineDataItem( MultiLineDataHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the extended element.
	 * 
	 * @param obj
	 *            the handle of an extended element to traverse
	 */

	protected void visitExtendedItem( ExtendedItemHandle obj )
	{
		visitReportItem( obj );
	}

	/**
	 * Visits the extended element.
	 * 
	 * @param obj
	 *            the handle of an extended element to traverse
	 */

	protected void visitScriptDataSource( ScriptDataSourceHandle obj )
	{
		visitDataSource( obj );
	}

	/**
	 * Visits the graphic master page element.
	 * 
	 * @param obj
	 *            the handle of a graphic master page to traverse
	 */

	protected void visitGraphicMasterPage( GraphicMasterPageHandle obj )
	{
		visitMasterPage( obj );
	}

	/**
	 * Visits the simple master page element.
	 * 
	 * @param obj
	 *            the handle of a simple master page to traverse
	 */

	protected void visitSimpleMasterPage( SimpleMasterPageHandle obj )
	{
		visitMasterPage( obj );
	}

	/**
	 * Visits the extended data source element.
	 * 
	 * @param obj
	 *            the handle of a extended data source to traverse
	 */

	protected void visitExtendedDataSource( ExtendedDataSourceHandle obj )
	{
		visitDataSource( obj );
	}

	/**
	 * Visits the extended data set element.
	 * 
	 * @param obj
	 *            the extended data set to traverse
	 */

	protected void visitExtendedDataSet( ExtendedDataSetHandle obj )
	{
		visitDataSet( obj );
	}

	/**
	 * A class forward the visit of an element to its handle.
	 *  
	 */

	protected class Forwarder extends ElementVisitor
	{

		/**
		 * The report design which this visitor visits.
		 */

		ReportDesign design = null;

		/**
		 * Visits the free form element.
		 * 
		 * @param obj
		 *            the free form to traverse
		 */

		public void visitFreeForm( FreeForm obj )
		{
			DesignVisitor.this.visitFreeForm( obj.handle( design ) );
		}

		/**
		 * The report design which this visitor visits.
		 * 
		 * @param design
		 *            report design
		 */
		
		void setDesign( ReportDesign design )
		{
			this.design = design;
		}

		/**
		 * Visits the label element.
		 * 
		 * @param obj
		 *            the label to traverse
		 */

		public void visitLabel( Label obj )
		{
			DesignVisitor.this.visitLabel( obj.handle( design ) );
		}

		/**
		 * Visits the data element.
		 * 
		 * @param obj
		 *            the data element to traverse
		 */

		public void visitDataItem( DataItem obj )
		{
			DesignVisitor.this.visitDataItem( obj.handle( design ) );
		}

		/**
		 * Visits the text element.
		 * 
		 * @param obj
		 *            the text to traverse
		 */

		public void visitTextItem( TextItem obj )
		{
			DesignVisitor.this.visitTextItem( obj.handle( design ) );
		}

		/**
		 * Visits the image element.
		 * 
		 * @param obj
		 *            the image to traverse
		 */

		public void visitImage( ImageItem obj )
		{
			DesignVisitor.this.visitImage( obj.handle( design ) );
		}

		/**
		 * Visits the list element.
		 * 
		 * @param obj
		 *            the list to traverse
		 */

		public void visitList( ListItem obj )
		{
			DesignVisitor.this.visitList( obj.handle( design ) );
		}

		/**
		 * Visits the list group element.
		 * 
		 * @param obj
		 *            the list group to traverse
		 */

		public void visitListGroup( ListGroup obj )
		{
			DesignVisitor.this.visitListGroup( obj.handle( design ) );
		}

		/**
		 * Visits the table element.
		 * 
		 * @param obj
		 *            the table to traverse
		 */

		public void visitTable( TableItem obj )
		{
			DesignVisitor.this.visitTable( obj.handle( design ) );
		}

		/**
		 * Visits the table group element.
		 * 
		 * @param obj
		 *            the table group to traverse
		 */

		public void visitTableGroup( TableGroup obj )
		{
			DesignVisitor.this.visitTableGroup( obj.handle( design ) );
		}

		/**
		 * Visits the cell element.
		 * 
		 * @param obj
		 *            the cell to traverse
		 */

		public void visitCell( Cell obj )
		{
			DesignVisitor.this.visitCell( obj.handle( design ) );
		}

		/**
		 * Visits the column element.
		 * 
		 * @param obj
		 *            the column to traverse
		 */

		public void visitColumn( TableColumn obj )
		{
			DesignVisitor.this.visitColumn( obj.handle( design ) );
		}

		/**
		 * Visits the row element.
		 * 
		 * @param obj
		 *            the row to traverse
		 */

		public void visitRow( TableRow obj )
		{
			DesignVisitor.this.visitRow( obj.handle( design ) );
		}

		/**
		 * Visits the grid element.
		 * 
		 * @param obj
		 *            the grid to traverse
		 */

		public void visitGrid( GridItem obj )
		{
			DesignVisitor.this.visitGrid( obj.handle( design ) );
		}

		/**
		 * Visits the line element.
		 * 
		 * @param obj
		 *            the line to traverse
		 */

		public void visitLine( LineItem obj )
		{
			DesignVisitor.this.visitLine( obj.handle( design ) );
		}

		/**
		 * Visits the parameter group element.
		 * 
		 * @param obj
		 *            the parameter group to traverse
		 */

		public void visitParameterGroup( ParameterGroup obj )
		{
			DesignVisitor.this.visitParameterGroup( obj.handle( design ) );
		}

		/**
		 * Visits the report design.
		 * 
		 * @param obj
		 *            the report design to traverse
		 */

		public void visitReportDesign( ReportDesign obj )
		{
			DesignVisitor.this.visitReportDesign( obj.handle( ) );
		}

		/**
		 * Visits the scalar parameter.
		 * 
		 * @param obj
		 *            the scalar parameter to traverse
		 */

		public void visitScalarParameter( ScalarParameter obj )
		{
			DesignVisitor.this.visitScalarParameter( obj.handle( design ) );
		}

		/**
		 * Visits the style element.
		 * 
		 * @param obj
		 *            the style to traverse
		 */

		public void visitStyle( Style obj )
		{
			DesignVisitor.this.visitStyle( obj.handle( design ) );
		}

		/**
		 * Visits the rectangle element.
		 * 
		 * @param obj
		 *            the rectangle to traverse
		 */

		public void visitRectangle( RectangleItem obj )
		{
			DesignVisitor.this.visitRectangle( obj.handle( design ) );
		}

		/**
		 * Visits the multiline data item.
		 * 
		 * @param obj
		 *            the multiline data to traverse
		 */

		public void visitMultiLineDataItem( MultiLineDataItem obj )
		{
			DesignVisitor.this.visitMultiLineDataItem( obj.handle( design ) );
		}

		/**
		 * Visits the extended item.
		 * 
		 * @param obj
		 *            the extended item to traverse
		 */

		public void visitExtendedItem( ExtendedItem obj )
		{
			DesignVisitor.this.visitExtendedItem( obj.handle( design ) );
		}

		/**
		 * Visits the script data source element.
		 * 
		 * @param obj
		 *            the script data source to traverse
		 */

		public void visitScriptDataSource( ScriptDataSource obj )
		{
			DesignVisitor.this.visitScriptDataSource( obj.handle( design ) );
		}

		/**
		 * Visits the graphic master page element.
		 * 
		 * @param obj
		 *            the graphic master page to traverse
		 */

		public void visitGraphicMasterPage( GraphicMasterPage obj )
		{
			DesignVisitor.this.visitGraphicMasterPage( obj.handle( design ) );
		}

		/**
		 * Visits the simple master page element.
		 * 
		 * @param obj
		 *            the simple master page to traverse
		 */

		public void visitSimpleMasterPage( SimpleMasterPage obj )
		{
			DesignVisitor.this.visitSimpleMasterPage( obj.handle( design ) );
		}

		/**
		 * Visits the extended data source element.
		 * 
		 * @param obj
		 *            the extended data source to traverse
		 */

		public void visitExtendedDataSource( ExtendedDataSource obj )
		{
			DesignVisitor.this.visitExtendedDataSource( obj.handle( design ) );
		}

		/**
		 * Visits the script data set element.
		 * 
		 * @param obj
		 *            the script data set to traverse
		 */

		public void visitScriptDataSet( ScriptDataSet obj )
		{
			DesignVisitor.this.visitScriptDataSet( obj.handle( design ) );
		}

		/**
		 * Visits the extended data set element.
		 * 
		 * @param obj
		 *            the extended data set to traverse
		 */

		public void visitExtendedDataSet( ExtendedDataSet obj )
		{
			DesignVisitor.this.visitExtendedDataSet( obj.handle( design ) );
		}
	}
}