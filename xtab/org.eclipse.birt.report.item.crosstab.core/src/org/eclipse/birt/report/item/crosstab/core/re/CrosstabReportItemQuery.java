package org.eclipse.birt.report.item.crosstab.core.re;
///*******************************************************************************
// * Copyright (c) 2004 Actuate Corporation.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// *  Actuate Corporation  - initial API and implementation
// *******************************************************************************/
//
//package org.eclipse.birt.report.item.crosstab.core.re;
//
//import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
//import org.eclipse.birt.report.engine.data.IQueryContext;
//import org.eclipse.birt.report.engine.data.dte.QueryBuilderVisitor;
//import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
//import org.eclipse.birt.report.engine.ir.BandDesign;
//import org.eclipse.birt.report.engine.ir.CellDesign;
//import org.eclipse.birt.report.engine.ir.GroupDesign;
//import org.eclipse.birt.report.engine.ir.ReportItemDesign;
//import org.eclipse.birt.report.model.api.ExtendedItemHandle;
//import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
//import org.eclipse.birt.report.model.api.extension.IReportItem;
//import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
//
///**
// * CrosstabReportItemQuery
// */
//public class CrosstabReportItemQuery extends ReportItemQueryBase
//{
//
//	private CrosstabReportItemHandle crosstabItem;
//
//	public void setModelObject( ExtendedItemHandle modelHandle )
//	{
//		super.setModelObject( modelHandle );
//
//		IReportItem item = null;
//		try
//		{
//			item = modelHandle.getReportItem( );
//		}
//		catch ( ExtendedElementException e )
//		{
//			// logger.log( e );
//		}
//		if ( item == null )
//		{
//			try
//			{
//				modelHandle.loadExtendedElement( );
//				item = modelHandle.getReportItem( );
//			}
//			catch ( ExtendedElementException eeex )
//			{
//				// logger.log( eeex );
//			}
//			if ( item == null )
//			{
//				// logger.log( ILogger.ERROR,
//				// Messages.getString(
//				// "ChartReportItemPresentationImpl.log.UnableToLocateWrapper" )
//				// ); //$NON-NLS-1$
//				return;
//			}
//		}
//
//		crosstabItem = (CrosstabReportItem) item;
//	}
//
//	public boolean buildQuery( IQueryContext context )
//	{
//		if ( crosstabItem == null )
//		{
//			return false;
//		}
//
//		CrosstabItemDesign itemDesign = CrosstabIRFactory.create( exContext.getReport( ),
//				crosstabItem );
//
//		new QueryVisitor( context ).buildQuery( exContext.getReport( ),
//				itemDesign,
//				exContext );
//
//		crosstabItem.setDesign( itemDesign );
//
//		return true;
//	}
//
//	/**
//	 * QueryVisitor
//	 */
//	static class QueryVisitor extends QueryBuilderVisitor
//	{
//
//		QueryVisitor( IQueryContext queryContext )
//		{
//		}
//
//		public Object visitReportItem( ReportItemDesign item, Object value )
//		{
//			if ( item instanceof CrosstabItemDesign )
//			{
//				CrosstabItemDesign tabDesign = (CrosstabItemDesign) item;
//
//				BaseQueryDefinition query = prepareVisit( item );
//
//				if ( query != null )
//				{
//					// TODO handle expressions in column definition
//
//					pushCurrentCondition( true );
//					for ( int i = 0; i < tabDesign.getRowGroupCount( ); i++ )
//					{
//						GroupDesign group = tabDesign.getRowGroup( i );
//
//						handleRowBand( group.getHeader( ), value );
//						handleRowBand( group.getFooter( ), value );
//					}
//					popCurrentCondition( );
//
//					BandDesign detail = tabDesign.getRowDetail( );
//					if ( detail == null || detail.getContentCount( ) == 0 )
//					{
//						query.setUsesDetails( false );
//					}
//
//					pushCurrentCondition( false );
//					handleRowBand( detail, value );
//					popCurrentCondition( );
//
//				}
//
//				finishVisit( query );
//
//				return value;
//			}
//			else
//			{
//				return super.visitReportItem( item, value );
//			}
//		}
//
//		void handleRowBand( BandDesign band, Object value )
//		{
//			if ( band != null )
//			{
//				for ( int i = 0; i < band.getContentCount( ); i++ )
//				{
//					CrosstabRowDesign row = (CrosstabRowDesign) band.getContent( i );
//
//					for ( int j = 0; j < row.getCellCount( ); j++ )
//					{
//						CellDesign cell = row.getCell( j );
//						cell.accept( this, value );
//					}
//				}
//			}
//		}
//	}
//}
