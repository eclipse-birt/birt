/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.palette;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.dialogs.TableOptionBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.TableOptionDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.ExtendedElementToolExtends;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.SelectVariableDialog;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;

/**
 * is the base class of Palette factory. Creates default tools here.
 */
public class BasePaletteFactory
{

	private static final String POINTER_SELECT_LABEL = Messages.getString( "BasePaletteFactory.categoryName.PointerSelect" );//$NON-NLS-1$
	private static final String RECTANGEL_SELECT_LABEL = Messages.getString( "BasePaletteFactory.categoryName.RectangleSelect" );//$NON-NLS-1$
	private static final String TOOL_TIP_POINTER_SELECT = Messages.getString( "BasePaletteFactory.toolTip.PointerSelect" );//$NON-NLS-1$
	private static final String TOOL_TIP_RECTANGLE_SELECT = Messages.getString( "BasePaletteFactory.toolTip.RectangleSelect" );//$NON-NLS-1$
	private static final String PALETTE_GROUP_TEXT = Messages.getString( "BasePaletteFactory.Group.Items" ); //$NON-NLS-1$
	private static final String AUTOTEXT_LABEL_PAGE_X_OF_Y = Messages.getString( "BasePaletteFactory.AutoTextLabel.PageXofY" ); //$NON-NLS-1$
	private static final String AUTOTEXT_LABEL_PAGE_COUNT = Messages.getString( "BasePaletteFactory.AutoTextLabel.PageCount" ); //$NON-NLS-1$
	private static final String AUTOTEXT_LABEL_LAST_PRINTED = Messages.getString( "BasePaletteFactory.AutoTextLabel.LastPrinted" ); //$NON-NLS-1$
	private static final String AUTOTEXT_LABEL_FILENAME = Messages.getString( "BasePaletteFactory.AutoTextLabel.Filename" ); //$NON-NLS-1$
	private static final String AUTOTEXT_LABEL_CREATE_BY = Messages.getString( "BasePaletteFactory.AutoTextLabel.CreatedBy" ); //$NON-NLS-1$
	private static final String AUTOTEXT_LABEL_CREATE_ON = Messages.getString( "BasePaletteFactory.AutoTextLabel.CreatedOn" ); //$NON-NLS-1$
	private static final String AUTOTEXT_LABEL_PAGE = Messages.getString( "BasePaletteFactory.AutoTextLabel.Page" ); //$NON-NLS-1$
	private static final String AUTOTEXT_TEXT_CONFIDENTIAL = Messages.getString( "BasePaletteFactory.AutoText.Confidential" ); //$NON-NLS-1$

	protected static PaletteRoot createPalette( )
	{
		PaletteRoot root = new PaletteRoot( );
		root.add( createControlGroup( root ) );
		return root;
	}

	/**
	 * Creates default tools category, which include selection and marquee tools
	 * 
	 * @param root
	 *            the root
	 * @return PaletteContainer contains default tools
	 */
	protected static PaletteContainer createControlGroup( PaletteRoot root )
	{
		PaletteGroup controlGroup = new PaletteGroup( PALETTE_GROUP_TEXT );

		List<ToolEntry> entries = new ArrayList<ToolEntry>( );

		ToolEntry tool = new PanningSelectionToolEntry( POINTER_SELECT_LABEL,
				TOOL_TIP_POINTER_SELECT );
		entries.add( tool );
		root.setDefaultEntry( tool );

		tool = new MarqueeToolEntry( RECTANGEL_SELECT_LABEL,
				TOOL_TIP_RECTANGLE_SELECT );
		entries.add( tool );

		controlGroup.addAll( entries );
		return controlGroup;
	}

	/**
	 * Provides element building support for table element.
	 */
	public static class TableToolExtends extends AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			CreateRequest request = getRequest( );
			if ( IReportElementConstants.REPORT_ELEMENT_TABLE.equalsIgnoreCase( (String) request.getNewObjectType( ) ) )
			{
				TableOptionBindingDialog dialog = new TableOptionBindingDialog( UIUtil.getDefaultShell( ) );

				if ( dialog.open( ) == Window.OK )
				{
					Object[] result = (Object[]) dialog.getResult( );

					Object[] data = (Object[]) result[0];

					boolean isSummaryTable = data.length > 2
							&& data[2] != null
							&& ( (Boolean) data[2] ).booleanValue( );

					Object[] datasetInfo = (Object[]) result[1];

					int columnCount = ( (Integer) data[1] ).intValue( );
					int bindingCount = 0;

					if ( datasetInfo != null
							&& datasetInfo[1] instanceof Object[] )
					{
						bindingCount = ( (Object[]) datasetInfo[1] ).length;

						if ( bindingCount > 0 )
						{
							columnCount = bindingCount;
						}
					}

					TableHandle table = DesignElementFactory.getInstance( )
							.newTableItem( null,
									columnCount,
									1,
									isSummaryTable ? 0
											: ( (Integer) data[0] ).intValue( ),
									1 );
					InsertInLayoutUtil.setInitWidth( table );

					if ( datasetInfo != null && datasetInfo[0] != null )
					{
						try
						{
							DataSetHandle dataSet = (DataSetHandle) datasetInfo[0];
							// if ( dataSet != null )
							// {
							( (ReportItemHandle) table ).setDataSet( dataSet );
							// }
							// else
							// {
							// new LinkedDataSetAdapter( ).setLinkedDataModel(
							// table,
							// datasetInfo[0].toString( ) );
							// }
								( (ReportItemHandle) table ).setDataSet(dataSet);
							DataSetColumnBindingsFormHandleProvider provider = new DataSetColumnBindingsFormHandleProvider( );
							provider.setBindingObject( table );

							if ( datasetInfo[1] instanceof Object[] )
							{
								Object[] selectedColumns = (Object[]) datasetInfo[1];
								provider.generateBindingColumns( selectedColumns );

								if ( bindingCount > 0 )
								{
									ResultSetColumnHandle[] columns = new ResultSetColumnHandle[bindingCount];
									for ( int i = 0; i < selectedColumns.length; i++ )
									{
										columns[i] = (ResultSetColumnHandle) selectedColumns[i];
									}

									InsertInLayoutUtil.insertToCell( dataSet,
											table,
											table.getHeader( ),
											columns,
											true );

									if ( !isSummaryTable )
									{
										InsertInLayoutUtil.insertToCell( dataSet,
												table,
												table.getDetail( ),
												columns,
												false );
									}
								}
							}
						}
						catch ( Exception e )
						{
							ExceptionHandler.handle( e );
						}
					}

					if ( isSummaryTable )
					{
						try
						{
							table.setIsSummaryTable( ( (Boolean) data[2] ).booleanValue( ) );
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
						}
					}
					setModel( table );
					return super.preHandleMouseUp( );
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * Provides element building support for Data element.
	 */
	public static class DataToolExtends extends AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			CreateRequest request = getRequest( );
			if ( IReportElementConstants.REPORT_ELEMENT_DATA.equalsIgnoreCase( (String) request.getNewObjectType( ) ) )
			{
				// DataItemHandle dataHandle = SessionHandleAdapter.getInstance(
				// )
				// .getReportDesignHandle( )
				// .getElementFactory( )
				// .newDataItem( null );
				DataItemHandle dataHandle = DesignElementFactory.getInstance( )
						.newDataItem( null );
				setModel( dataHandle );
				// disable this dialog
				// dialog will pop-up after image is create
				// see ReportCreationTool.selectAddedObject()
				// BindingColumnDialog dialog = new BindingColumnDialog( true );
				// dialog.setInput( dataHandle );
				// if ( dialog.open( ) == Window.OK )
				// {
				return super.preHandleMouseUp( );
				// }
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			// TODO Auto-generated method stub
			return false;
		}
	}

	/**
	 * Provides element building support for Image element.
	 */
	public static class ImageToolExtends extends AbstractToolHandleExtends
	{

		// private List dataSetList = new ArrayList( );

		/*
		 * get target design element handle
		 */
		private DesignElementHandle getDesignElementHandle( )
		{

			Object model = getTargetEditPart( ).getModel( );
			DesignElementHandle desginElementHandle = null;
			if ( model instanceof DesignElementHandle )
			{
				desginElementHandle = (DesignElementHandle) ( model );
			}
			else if ( model instanceof ListBandProxy )
			{
				desginElementHandle = ( (ListBandProxy) ( model ) ).getSlotHandle( )
						.getElementHandle( );

			}

			return desginElementHandle;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			// CreateRequest request = getRequest( );
			//
			// DesignElementHandle desginElementHandle = getDesignElementHandle(
			// );
			// dataSetList = DEUtil.getDataSetList( desginElementHandle );
			//
			// if (
			// IReportElementConstants.REPORT_ELEMENT_IMAGE.equalsIgnoreCase(
			// (String) request.getNewObjectType( ) ) )
			// {
			// // Open the builder for new image
			// ImageBuilder dialog = new ImageBuilder( UIUtil.getDefaultShell(
			// ),
			// ImageBuilder.DLG_TITLE_NEW,
			// dataSetList );
			// if ( Window.OK == dialog.open( ) )
			// {
			// setModel( dialog.getResult( ) );
			//
			// // If the dialog popup, mouse up event will not be called
			// // automatically, call it explicit
			// return super.preHandleMouseUp( );
			// }
			// }
			// return false;
			ImageHandle dataHandle = DesignElementFactory.getInstance( )
					.newImage( null );
			setModel( dataHandle );
			return super.preHandleMouseUp( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			// TODO Auto-generated method stub
			return false;
		}
	}

	public static class AutoTextToolExtends extends AbstractToolHandleExtends
	{

		public boolean preHandleMouseUp( )
		{
			CreateRequest request = getRequest( );

			AutoTextHandle autoTextItemHandle = DesignElementFactory.getInstance( )
					.newAutoText( null );
			try
			{
				if ( IReportElementConstants.AUTOTEXT_PAGE.equalsIgnoreCase( (String) request.getNewObjectType( ) ) )
				{
					autoTextItemHandle.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER );
				}
				else if ( IReportElementConstants.AUTOTEXT_TOTAL_PAGE_COUNT.equalsIgnoreCase( (String) request.getNewObjectType( ) ) )
				{
					autoTextItemHandle.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE );
				}

			}
			catch ( SemanticException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace( );
			}
			setModel( autoTextItemHandle );
			return super.preHandleMouseUp( );

		} /*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */

		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	public static class VariableToolExtends extends AbstractToolHandleExtends
	{

		public boolean preHandleMouseUp( )
		{
			CreateRequest request = getRequest( );
			if ( IReportElementConstants.AUTOTEXT_VARIABLE.equalsIgnoreCase( (String) request.getNewObjectType( ) ) )
			{
				ModuleHandle reportHandle = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( );
				if ( reportHandle instanceof ReportDesignHandle )
				{
					SelectVariableDialog dialog = new SelectVariableDialog( (ReportDesignHandle) SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( ) );
					if ( dialog.open( ) == Dialog.OK )
					{
						AutoTextHandle autoTextItemHandle = DesignElementFactory.getInstance( )
								.newAutoText( null );
						try
						{
							autoTextItemHandle.setPageVariable( (String) dialog.getResult( ) );
							autoTextItemHandle.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_PAGE_VARIABLE );
							setModel( autoTextItemHandle );
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
						}
						return super.preHandleMouseUp( );
					}
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * Provides element building support for text element.
	 */
	public static class TextToolExtends extends AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */

		private static final String DEFAULT_AUTHOR = Messages.getString( "TextExtendsTools.Message.DefaultAuthor" ); //$NON-NLS-1$		

		public boolean preHandleMouseUp( )
		{
			String type = (String) getRequest( ).getNewObjectType( );
			String text = null;
			ModuleHandle reportHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );

			// TextItemHandle textItemHandle = SessionHandleAdapter.getInstance(
			// )
			// .getReportDesignHandle( )
			// .getElementFactory( )
			// .newTextItem( null );
			TextItemHandle textItemHandle = DesignElementFactory.getInstance( )
					.newTextItem( null );
			try
			{
				// if ( IReportElementConstants.AUTOTEXT_PAGE.equalsIgnoreCase(
				// type ) )
				// {
				// text = AUTOTEXT_LABEL_PAGE
				// + "<value-of>pageNumber</value-of>"; //$NON-NLS-1$
				// textItemHandle.setContentType(
				// DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
				//
				// }else
				if ( IReportElementConstants.AUTOTEXT_DATE.equalsIgnoreCase( type ) )
				{
					text = "<value-of>new Date()</value-of>"; //$NON-NLS-1$
					textItemHandle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
				}
				else if ( IReportElementConstants.AUTOTEXT_CREATEDON.equalsIgnoreCase( type ) )
				{
					text = AUTOTEXT_LABEL_CREATE_ON
							+ "<value-of>new Date()</value-of>"; //$NON-NLS-1$
					textItemHandle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
				}
				else if ( IReportElementConstants.AUTOTEXT_CREATEDBY.equalsIgnoreCase( type ) )
				{
					text = AUTOTEXT_LABEL_CREATE_BY;
					String author = reportHandle.getAuthor( );
					if ( author != null )
					{
						text += author;
					}
					else
					{
						String pluginVersion = ReportPlugin.getDefault( )
								.getBundle( )
								.getHeaders( )
								.get( org.osgi.framework.Constants.BUNDLE_VERSION );
						text += DEFAULT_AUTHOR + " " //$NON-NLS-1$
								+ pluginVersion;
					}
				}
				else if ( IReportElementConstants.AUTOTEXT_FILENAME.equalsIgnoreCase( type ) )
				{
					text = AUTOTEXT_LABEL_FILENAME + reportHandle.getFileName( );
				}
				else if ( IReportElementConstants.AUTOTEXT_LASTPRINTED.equalsIgnoreCase( type ) )
				{
					text = AUTOTEXT_LABEL_LAST_PRINTED
							+ "<value-of>new Date()</value-of>"; //$NON-NLS-1$
					textItemHandle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
				}
				// else if (
				// IReportElementConstants.AUTOTEXT_TOTAL_PAGE_COUNT.equalsIgnoreCase(
				// type ) )
				// {
				// text = AUTOTEXT_LABEL_PAGE_COUNT
				// + "<value-of>pageNumber</value-of>"
				// + " of"
				// + "<value-of>pageNumber</value-of>";
				// }
				// else if (
				// IReportElementConstants.AUTOTEXT_PAGEXOFY.equalsIgnoreCase(
				// type ) )
				// {
				// text = AUTOTEXT_LABEL_PAGE_X_OF_Y
				// + "Page "
				// + "<value-of>pageNumber</value-of>"
				// + " of"
				// + "<value-of>pageNumber</value-of>";
				// }
				else if ( !IReportElementConstants.REPORT_ELEMENT_TEXT.equalsIgnoreCase( type ) )
				{
					return false;
				}

				if ( text != null )
				{
					textItemHandle.setContent( text );
				}
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}

			setModel( textItemHandle );
			return super.preHandleMouseUp( );
		} /*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */

		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * Provides element building support for label element.
	 */
	public static class LabelToolExtends extends AbstractToolHandleExtends
	{

		public boolean preHandleMouseUp( )
		{
			CreateRequest request = getRequest( );
			if ( IReportElementConstants.REPORT_ELEMENT_LABEL.equalsIgnoreCase( (String) request.getNewObjectType( ) ) )
			{
				// LabelHandle labelItemHandle =
				// SessionHandleAdapter.getInstance( )
				// .getReportDesignHandle( )
				// .getElementFactory( )
				// .newLabel( null );

				LabelHandle labelItemHandle = DesignElementFactory.getInstance( )
						.newLabel( null );

				setModel( labelItemHandle );
				return super.preHandleMouseUp( );

			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * Provides element building support for TextData element.
	 */
	public static class TextDataToolExtends extends AbstractToolHandleExtends
	{

		public boolean preHandleMouseUp( )
		{
			CreateRequest request = getRequest( );
			if ( IReportElementConstants.REPORT_ELEMENT_TEXTDATA.equalsIgnoreCase( (String) request.getNewObjectType( ) ) )
			{
				// LabelHandle labelItemHandle =
				// SessionHandleAdapter.getInstance( )
				// .getReportDesignHandle( )
				// .getElementFactory( )
				// .newLabel( null );

				TextDataHandle textItemHandle = DesignElementFactory.getInstance( )
						.newTextData( null );

				try
				{
					textItemHandle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
					return false;
				}
				setModel( textItemHandle );

				return super.preHandleMouseUp( );

			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * Provides element building support for List element.
	 */
	public static class ListToolExtends extends AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			CreateRequest request = getRequest( );
			if ( IReportElementConstants.REPORT_ELEMENT_LIST.equalsIgnoreCase( (String) request.getNewObjectType( ) ) )
			{
				// ListHandle list = SessionHandleAdapter.getInstance( )
				// .getReportDesignHandle( )
				// .getElementFactory( )
				// .newList( null );

				ListHandle list = DesignElementFactory.getInstance( )
						.newList( null );

				setModel( list );
				return super.preHandleMouseUp( );
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * Provides element building support for grid element.
	 */
	public static class GridToolExtends extends AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			ModuleHandle reportDesignHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			// ElementFactory factory = reportDesignHandle.getElementFactory( );
			DesignElementFactory factory = DesignElementFactory.getInstance( reportDesignHandle );
			String type = (String) getRequest( ).getNewObjectType( );
			GridHandle grid = null;

			if ( IReportElementConstants.AUTOTEXT_PAGEXOFY.equals( type ) )
			{
				grid = factory.newGridItem( null, 3, 1 );
				try
				{
					List cellList = ( (RowHandle) grid.getRows( ).get( 0 ) ).getCells( )
							.getContents( );

					AutoTextHandle autoTextHandle = factory.newAutoText( null );

					autoTextHandle.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER );

					( (CellHandle) cellList.get( 0 ) ).getContent( )
							.add( autoTextHandle );

					TextItemHandle textHandle = factory.newTextItem( null );
					textHandle.setContent( "/" ); //$NON-NLS-1$
					textHandle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_PLAIN );
					( (CellHandle) cellList.get( 1 ) ).getContent( )
							.add( textHandle );

					autoTextHandle = factory.newAutoText( null );
					autoTextHandle.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE );
					( (CellHandle) cellList.get( 2 ) ).getContent( )
							.add( autoTextHandle );

				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			else if ( IReportElementConstants.AUTOTEXT_AUTHOR_PAGE_DATE.equals( type ) )
			{
				grid = factory.newGridItem( null, 3, 1 );
				try
				{
					List cellList = ( (RowHandle) grid.getRows( ).get( 0 ) ).getCells( )
							.getContents( );

					TextItemHandle textHandle = factory.newTextItem( null );
					String text = AUTOTEXT_LABEL_CREATE_BY;
					if ( reportDesignHandle.getAuthor( ) != null )
					{
						text += reportDesignHandle.getAuthor( );
					}
					textHandle.setContent( text );
					( (CellHandle) cellList.get( 0 ) ).getContent( )
							.add( textHandle );

					AutoTextHandle autoTextHandle = factory.newAutoText( null );
					autoTextHandle.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER );
					( (CellHandle) cellList.get( 1 ) ).getContent( )
							.add( autoTextHandle );

					textHandle = factory.newTextItem( null );
					textHandle.setContent( "<value-of>new Date()</value-of>" ); //$NON-NLS-1$
					textHandle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
					( (CellHandle) cellList.get( 2 ) ).getContent( )
							.add( textHandle );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			else if ( IReportElementConstants.AUTOTEXT_CONFIDENTIAL_PAGE.equals( type ) )
			{
				grid = factory.newGridItem( null, 2, 1 );
				try
				{
					List cellList = ( (RowHandle) grid.getRows( ).get( 0 ) ).getCells( )
							.getContents( );

					TextItemHandle textHandle = factory.newTextItem( null );
					textHandle.setContent( AUTOTEXT_TEXT_CONFIDENTIAL );
					textHandle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
					( (CellHandle) cellList.get( 0 ) ).getContent( )
							.add( textHandle );

					AutoTextHandle autoTextHandle = factory.newAutoText( null );
					autoTextHandle.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER );
					( (CellHandle) cellList.get( 1 ) ).getContent( )
							.add( autoTextHandle );

				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			else if ( IReportElementConstants.REPORT_ELEMENT_GRID.equals( type ) )
			{
				TableOptionDialog dlg = new TableOptionDialog( UIUtil.getDefaultShell( ),
						false );
				if ( dlg.open( ) == Window.OK
						&& dlg.getResult( ) instanceof Object[] )
				{
					Object[] data = (Object[]) dlg.getResult( );
					grid = factory.newGridItem( null,
							( (Integer) data[1] ).intValue( ),
							( (Integer) data[0] ).intValue( ) );
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
			InsertInLayoutUtil.setInitWidth( grid );
			setModel( grid );
			return super.preHandleMouseUp( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * Provides element building support for data set.
	 */
	public static class DataSetToolExtends extends AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			if ( getRequest( ).getNewObjectType( ) instanceof DataSetHandle )
			{

				try
				{

					// add extended dataset element.
					Object newObj = getRequest( ).getNewObject( );
					if ( newObj instanceof Object[]
							&& ( (Object[]) newObj ).length > 0 )
					{
						newObj = ( (Object[]) newObj )[0];
					}
					DesignElementHandle elementHandle = (DesignElementHandle) newObj;
					ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( );
					// element comes from library and not to itself.
					if ( elementHandle.getRoot( ) instanceof LibraryHandle
							&& elementHandle.getRoot( ) != moduleHandle )
					{
						LibraryHandle library = (LibraryHandle) elementHandle.getRoot( );
						try
						{
							if ( UIUtil.includeLibrary( moduleHandle, library ) )
							{
								elementHandle = moduleHandle.getElementFactory( )
										.newElementFrom( elementHandle,
												elementHandle.getName( ) );
								moduleHandle.addElement( elementHandle,
										moduleHandle.getDataSets( ).getSlotID( ) );
							}
						}
						catch ( Exception e )
						{
							ExceptionHandler.handle( e );
						}
					}
					Object newHandle = InsertInLayoutUtil.performInsert( elementHandle,
							getTargetEditPart( ) );
					if ( newHandle == null )
						return false;
					setModel( newHandle );

					return super.preHandleMouseUp( );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * for cross tab Provides element building support for data set.
	 */
	public static class DimensionHandleToolExtends extends
			AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			if ( getRequest( ).getNewObjectType( ) instanceof DimensionHandle )
			{

				// try
				// {

				// add extended dataset element.
				Object newObj = getRequest( ).getNewObject( );
				if ( newObj instanceof Object[]
						&& ( (Object[]) newObj ).length > 0 )
				{
					newObj = ( (Object[]) newObj )[0];
				}
				DesignElementHandle elementHandle = (DesignElementHandle) newObj;
				// ModuleHandle moduleHandle = SessionHandleAdapter.getInstance(
				// )
				// .getReportDesignHandle( );
				// // element comes from library and not to itself.
				//
				// Object newHandle = InsertInLayoutUtil.performInsert(
				// elementHandle,
				// getTargetEditPart( ) );
				if ( elementHandle == null )
					return false;
				setModel( elementHandle );

				return super.preHandleMouseUp( );
				// }
				// catch ( SemanticException e )
				// {
				// ExceptionHandler.handle( e );
				// }
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	public static class MeasureHandleToolExtends extends
			AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			if ( getRequest( ).getNewObjectType( ) instanceof MeasureHandle )
			{

				// try
				// {

				// add extended dataset element.
				Object newObj = getRequest( ).getNewObject( );
				if ( newObj instanceof Object[]
						&& ( (Object[]) newObj ).length > 0 )
				{
					newObj = ( (Object[]) newObj )[0];
				}
				DesignElementHandle elementHandle = (DesignElementHandle) newObj;
				// ModuleHandle moduleHandle = SessionHandleAdapter.getInstance(
				// )
				// .getReportDesignHandle( );
				// // element comes from library and not to itself.
				//
				// Object newHandle = InsertInLayoutUtil.performInsert(
				// elementHandle,
				// getTargetEditPart( ) );
				if ( elementHandle == null )
					return false;
				setModel( elementHandle );

				return super.preHandleMouseUp( );
				// }
				// catch ( SemanticException e )
				// {
				// ExceptionHandler.handle( e );
				// }
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	/**
	 * Provides element building support for data set column.
	 */
	public static class DataSetColumnToolExtends extends
			AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			if ( getRequest( ).getNewObjectType( ) instanceof DataSetItemModel
					|| getRequest( ).getNewObjectType( ) instanceof ResultSetColumnHandle )
			{
				try
				{
					Object newHandle = InsertInLayoutUtil.performInsert( getRequest( ).getNewObject( ),
							getTargetEditPart( ) );
					if ( newHandle == null )
						return false;

					setModel( newHandle );
					return super.preHandleMouseUp( );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}

	}

	/**
	 * Provides element building support for parameter.
	 */
	public static class ParameterToolExtends extends AbstractToolHandleExtends
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * IToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseUp( )
		{
			if ( getRequest( ).getNewObjectType( ) instanceof ScalarParameterHandle )
			{
				try
				{
					Object newObj = getRequest( ).getNewObject( );
					if ( newObj instanceof Object[] )
					{
						Object[] newObjs = (Object[]) newObj;
						newObj = UIUtil.getInsertPamaterElements( newObjs );
					}

					Object newHandle = InsertInLayoutUtil.performInsert( newObj,
							getTargetEditPart( ) );
					if ( newHandle == null )
						return false;

					setModel( newHandle );

					return super.preHandleMouseUp( );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.tools.
		 * AbstractToolHandleExtends#preHandleMouseDown()
		 */
		public boolean preHandleMouseDown( )
		{
			return false;
		}
	}

	public static AbstractToolHandleExtends getAbstractToolHandleExtendsFromPaletteName(
			Object str )
	{
		if ( !( str instanceof String ) )
		{
			throw new Error( "Don't support the other type expect String" ); //$NON-NLS-1$
		}
		String template = (String) str;
		AbstractToolHandleExtends preHandle = null;
		if ( IReportElementConstants.REPORT_ELEMENT_IMAGE.equalsIgnoreCase( template ) )
		{
			preHandle = new ImageToolExtends( );

		}
		else if ( IReportElementConstants.REPORT_ELEMENT_TABLE.equalsIgnoreCase( template ) )
		{
			preHandle = new TableToolExtends( );

		}
		else if ( IReportElementConstants.REPORT_ELEMENT_TEXT.equalsIgnoreCase( template )
				|| IReportElementConstants.AUTOTEXT_DATE.equalsIgnoreCase( template )
				|| IReportElementConstants.AUTOTEXT_CREATEDON.equalsIgnoreCase( template )
				|| IReportElementConstants.AUTOTEXT_CREATEDBY.equalsIgnoreCase( template )
				|| IReportElementConstants.AUTOTEXT_FILENAME.equalsIgnoreCase( template )
				|| IReportElementConstants.AUTOTEXT_LASTPRINTED.equalsIgnoreCase( template )

		)
		{
			preHandle = new TextToolExtends( );
		}
		else if ( IReportElementConstants.AUTOTEXT_PAGE.equalsIgnoreCase( template )
				|| IReportElementConstants.AUTOTEXT_TOTAL_PAGE_COUNT.equalsIgnoreCase( template ) )
		{
			preHandle = new AutoTextToolExtends( );
		}
		else if ( IReportElementConstants.REPORT_ELEMENT_TEXTDATA.equalsIgnoreCase( template ) )
		{
			preHandle = new TextDataToolExtends( );
		}
		else if ( IReportElementConstants.AUTOTEXT_AUTHOR_PAGE_DATE.equalsIgnoreCase( template )
				|| IReportElementConstants.AUTOTEXT_CONFIDENTIAL_PAGE.equalsIgnoreCase( template )
				|| IReportElementConstants.REPORT_ELEMENT_GRID.equalsIgnoreCase( template )
				|| IReportElementConstants.AUTOTEXT_PAGEXOFY.equalsIgnoreCase( template ) )
		{
			preHandle = new GridToolExtends( );
		}
		else if ( IReportElementConstants.REPORT_ELEMENT_LABEL.equalsIgnoreCase( template ) )
		{
			preHandle = new LabelToolExtends( );
		}
		else if ( IReportElementConstants.REPORT_ELEMENT_DATA.equalsIgnoreCase( template ) )
		{
			preHandle = new DataToolExtends( );
		}
		else if ( IReportElementConstants.REPORT_ELEMENT_LIST.equalsIgnoreCase( template ) )
		{
			preHandle = new ListToolExtends( );
		}
		else if ( ( template ).startsWith( IReportElementConstants.REPORT_ELEMENT_EXTENDED ) )
		{
			String extensionName = template.substring( IReportElementConstants.REPORT_ELEMENT_EXTENDED.length( ) );
			preHandle = new ExtendedElementToolExtends( extensionName );
		}
		else if ( IReportElementConstants.AUTOTEXT_VARIABLE.equalsIgnoreCase( template ) )
		{
			preHandle = new VariableToolExtends( );
		}

		if ( preHandle == null )
		{
			throw new Error( "Don't find the AbstractToolHandleExtends" ); //$NON-NLS-1$
		}
		return preHandle;
	}
}