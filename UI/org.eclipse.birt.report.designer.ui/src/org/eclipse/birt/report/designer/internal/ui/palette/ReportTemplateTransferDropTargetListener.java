
package org.eclipse.birt.report.designer.internal.ui.palette;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportCreationTool;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.DataSetColumnToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.DataSetToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.DataToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.GridToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.ImageToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.LabelToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.ListToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.ParameterToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.TableToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.TextToolExtends;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertInLayoutAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * Drag&Drop listener
 */
public class ReportTemplateTransferDropTargetListener
		extends
			TemplateTransferDropTargetListener
{

	private static final String TRANS_LABEL_CREATE_ELEMENT = Messages.getString( "ReportTemplateTransferDropTargetListener.transLabel.createElement" ); //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param viewer
	 */
	public ReportTemplateTransferDropTargetListener( EditPartViewer viewer )
	{
		super( viewer );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.TemplateTransferDropTargetListener#getFactory(java.lang.Object)
	 */
	protected CreationFactory getFactory( Object template )
	{
		if ( handleValidateDrag( template ) )
		{
			return new ReportElementFactory( template );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
	 */
	protected void handleDrop( )
	{
		Object template = TemplateTransfer.getInstance( ).getTemplate( );
		Assert.isNotNull( template );
		Assert.isTrue( handleValidateDrag( template ) );

		updateTargetRequest( );
		updateTargetEditPart( );

		AbstractToolHandleExtends preHandle = null;
		if ( template instanceof String )
		{
			if ( IReportElementConstants.REPORT_ELEMENT_IMAGE.equalsIgnoreCase( (String) template ) )
			{
				preHandle = new ImageToolExtends( );

			}
			else if ( IReportElementConstants.REPORT_ELEMENT_TABLE.equalsIgnoreCase( (String) template ) )
			{
				preHandle = new TableToolExtends( );

			}
			else if ( IReportElementConstants.REPORT_ELEMENT_TEXT.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.AUTOTEXT_PAGE.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.AUTOTEXT_DATE.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.AUTOTEXT_CREATEDON.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.AUTOTEXT_CREATEDBY.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.AUTOTEXT_FILENAME.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.AUTOTEXT_LASTPRINTED.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.AUTOTEXT_PAGEXOFY.equalsIgnoreCase( (String) template ) )
			{
				preHandle = new TextToolExtends( );
			}
			else if ( IReportElementConstants.AUTOTEXT_AUTHOR_PAGE_DATE.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.AUTOTEXT_CONFIDENTIAL_PAGE.equalsIgnoreCase( (String) template )
					|| IReportElementConstants.REPORT_ELEMENT_GRID.equalsIgnoreCase( (String) template ) )
			{
				preHandle = new GridToolExtends( );
			}
			else if ( IReportElementConstants.REPORT_ELEMENT_LABEL.equalsIgnoreCase( (String) template ) )
			{
				preHandle = new LabelToolExtends( );
			}
			else if ( IReportElementConstants.REPORT_ELEMENT_DATA.equalsIgnoreCase( (String) template ) )
			{
				preHandle = new DataToolExtends( );
			}
			else if ( IReportElementConstants.REPORT_ELEMENT_LIST.equalsIgnoreCase( (String) template ) )
			{
				preHandle = new ListToolExtends( );
			}
		}
		else if ( handleValidateInsert( template ) )
		{
			Object singleSelection = BasePaletteFactory.getSingleTransferData( template );
			if ( singleSelection instanceof DataSetHandle )
			{
				preHandle = new DataSetToolExtends( );
			}
			else if ( singleSelection instanceof DataSetItemModel )
			{
				preHandle = new DataSetColumnToolExtends( );
			}
			else if ( singleSelection instanceof ScalarParameterHandle )
			{
				preHandle = new ParameterToolExtends( );
			}
		}

		if ( preHandle != null )
		{
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getCommandStack( )
					.startTrans( TRANS_LABEL_CREATE_ELEMENT );
			preHandle.setRequest( this.getCreateRequest( ) );
			preHandle.setTargetEditPart( getTargetEditPart( ) );

			Command command = this.getCommand( );
			if ( command != null && command.canExecute( ) )
			{
				if ( !( preHandle.preHandleMouseUp( ) ) )
				{
					SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.getCommandStack( )
							.rollback( );
					return;
				}
			}
			super.handleDrop( );
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getCommandStack( )
					.commit( );
			selectAddedObject( );
		}

	}

	/**
	 * Validates drag source from palette, layout, or data view
	 * 
	 * @param dragObj
	 * @return validate result
	 */
	private boolean handleValidateDrag( Object dragObj )
	{
		return dragObj != null
				&& ( handleValidatePalette( dragObj )
						|| handleValidateOutline( dragObj ) || handleValidateInsert( dragObj ) );
	}

	private boolean handleValidatePalette( Object dragObj )
	{
		return dragObj instanceof String
				&& ( getTargetEditPart( ) == null || ReportCreationTool.handleValidatePalette( dragObj,
						getTargetEditPart( ) ) );
	}

	/**
	 * Validates drag from data view to layout
	 * 
	 * @param template
	 * @return validate result
	 */
	private boolean handleValidateInsert( Object template )
	{
		return InsertInLayoutAction.handleValidateInsert( template )
				&& ( getTargetEditPart( ) == null || InsertInLayoutAction.handleValidateInsertToLayout( template,
						getTargetEditPart( ) ) );
	}

	/**
	 * Validates drag source of outline view and drop target of layout
	 * 
	 * @return validate result
	 */
	private boolean handleValidateOutline( Object dragSource )
	{

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver( DropTargetEvent event )
	{
		super.dragOver( event );
		if ( !handleValidateDrag( TemplateTransfer.getInstance( ).getTemplate( ) ) )
		{
			event.detail = DND.DROP_NONE;
		}
	}

	/*
	 * Add the newly created object to the viewer's selected objects.
	 */
	private void selectAddedObject( )
	{
		final Object model = getCreateRequest( ).getExtendedData( )
				.get( "newObject" ); //$NON-NLS-1$
		final EditPartViewer viewer = getViewer( );
		viewer.getControl( ).setFocus( );
		ReportCreationTool.selectAddedObject( model, viewer );
	}

}

