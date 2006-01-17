/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.api.script.element.IDesignElement;
import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableBandContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Cell;
import org.eclipse.birt.report.engine.script.internal.element.DataItem;
import org.eclipse.birt.report.engine.script.internal.element.Grid;
import org.eclipse.birt.report.engine.script.internal.element.Image;
import org.eclipse.birt.report.engine.script.internal.element.Label;
import org.eclipse.birt.report.engine.script.internal.element.List;
import org.eclipse.birt.report.engine.script.internal.element.ReportDesign;
import org.eclipse.birt.report.engine.script.internal.element.ReportElement;
import org.eclipse.birt.report.engine.script.internal.element.Row;
import org.eclipse.birt.report.engine.script.internal.element.Table;
import org.eclipse.birt.report.engine.script.internal.element.DynamicText;
import org.eclipse.birt.report.engine.script.internal.element.TextItem;
import org.eclipse.birt.report.engine.script.internal.instance.CellInstance;
import org.eclipse.birt.report.engine.script.internal.instance.DataItemInstance;
import org.eclipse.birt.report.engine.script.internal.instance.GridInstance;
import org.eclipse.birt.report.engine.script.internal.instance.ImageInstance;
import org.eclipse.birt.report.engine.script.internal.instance.LabelInstance;
import org.eclipse.birt.report.engine.script.internal.instance.ListInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RowInstance;
import org.eclipse.birt.report.engine.script.internal.instance.TableInstance;
import org.eclipse.birt.report.engine.script.internal.instance.TextItemInstance;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

public class ElementUtil
{

	public static IReportElementInstance getInstance( IElement element,
			ExecutionContext context )
	{
		if ( element == null )
			return null;

		// No row data available, fromGrid doesn't matter
		if ( element instanceof CellContent )
			return new CellInstance( ( CellContent ) element, null, context, false );

		if ( element instanceof DataContent )
			return new DataItemInstance( ( DataContent ) element, context );

		if ( element instanceof ImageContent )
			return new ImageInstance( ( ImageContent ) element, context );

		if ( element instanceof LabelContent )
			return new LabelInstance( ( LabelContent ) element, context );

		if ( element instanceof ContainerContent )
			return new ListInstance( ( ContainerContent ) element, context );

		// No row data available
		if ( element instanceof RowContent )
			return new RowInstance( ( RowContent ) element, null, context );

		if ( element instanceof TableContent )
		{
			Object genBy = ( ( TableContent ) element ).getGenerateBy( );
			if ( genBy instanceof TableItemDesign )
				return new TableInstance( ( TableContent ) element, context );
			else if ( genBy instanceof GridItemDesign )
				return new GridInstance( ( TableContent ) element, context );
		}

		if ( element instanceof TextContent )
			return new TextItemInstance( ( TextContent ) element, context );

		if ( element instanceof ForeignContent )
		{
			ForeignContent fc = ( ForeignContent ) element;
			if ( IForeignContent.HTML_TYPE.equals( fc.getRawType( ) )
					|| IForeignContent.TEXT_TYPE.equals( fc.getRawType( ) )
					|| IForeignContent.TEMPLATE_TYPE.equals( fc.getRawType( ) ) )
				return new TextItemInstance( fc, context );
		}
		if ( element instanceof TableBandContent )
		{
			return getInstance( element.getParent( ), context );
		}

		return null;
	}

	public static IDesignElement getElement( DesignElementHandle element )
	{
		if ( element == null )
			return null;
		if ( element instanceof ReportDesignHandle )
			return new ReportDesign( ( ReportDesignHandle ) element );

		if ( !( element instanceof ReportElementHandle ) )
			return null;

		if ( element instanceof CellHandle )
			return new Cell( ( CellHandle ) element );

		if ( element instanceof DataItemHandle )
			return new DataItem( ( DataItemHandle ) element );

		if ( element instanceof GridHandle )
			return new Grid( ( GridHandle ) element );

		if ( element instanceof ImageHandle )
			return new Image( ( ImageHandle ) element );

		if ( element instanceof LabelHandle )
			return new Label( ( LabelHandle ) element );

		if ( element instanceof ListHandle )
			return new List( ( ListHandle ) element );

		if ( element instanceof RowHandle )
			return new Row( ( RowHandle ) element );

		if ( element instanceof TableHandle )
			return new Table( ( TableHandle ) element );

		if ( element instanceof TextDataHandle )
			return new DynamicText( ( TextDataHandle ) element );

		if ( element instanceof TextItemHandle )
			return new TextItem( ( TextItemHandle ) element );

		return new ReportElement( ( ReportElementHandle ) element );

	}

}
