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
package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.LabelDirectEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportElementNonResizablePolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.LabelCellEditorLocator;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.LabelEditManager;
import org.eclipse.birt.report.designer.internal.ui.util.ElementBuilderFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class PlaceHolderEditPart extends ReportElementEditPart
{

	private DesignElementHandle copiedHandle;

	public PlaceHolderEditPart( Object model )
	{
		super( model );
	}

	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE,
				new ReportComponentEditPolicy( ) );
		installEditPolicy( EditPolicy.DIRECT_EDIT_ROLE,
				new LabelDirectEditPolicy( ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		StyleHandle style = ( (DesignElementHandle) getDefaultHandle( ) )
				.getPrivateStyle( );

		//TODO:fixed the bug 191874
		//( (LabelFigure) getFigure( ) ).setFont( getFont( ) ); 
		( (LabelFigure) getFigure( ) ).setImage( getImage( ) );
		( (LabelFigure) getFigure( ) ).setAlignment( PositionConstants.WEST );
		( (LabelFigure) getFigure( ) ).setDirection( getTextDirection( getDefaultHandle( )) ); // bidi_hcg
		( (LabelFigure) getFigure( ) ).setText( getTemplateModel( )
				.getDisplayDescription( ) );
		( (LabelFigure) getFigure( ) )
				.setTextAlign( DesignChoiceConstants.TEXT_ALIGN_CENTER );
		( (LabelFigure) getFigure( ) )
				.setForegroundColor( ReportColorConstants.ShadowLineColor );
		( (LabelFigure) getFigure( ) ).setDisplay( style.getDisplay( ) );

		getFigure( ).setBorder( new LineBorder( 1 ) );
	}

	/**
	 * Get the current font family.
	 * 
	 * @return The current font family
	 */
	protected Font getFont( )
	{
		return getFont( (ReportItemHandle) getDefaultHandle( ) );
	}

	private Image getImage( )
	{
		if ( getDefaultHandle( ) instanceof LabelHandle )
		{
			return ReportPlatformUIImages
					.getImage( IReportGraphicConstants.ICON_ELEMENT_LABEL );
		}
		if ( getDefaultHandle( ) instanceof TextItemHandle )
		{
			return ReportPlatformUIImages
					.getImage( IReportGraphicConstants.ICON_ELEMENT_TEXT );
		}
		if ( getDefaultHandle( ) instanceof DataItemHandle )
		{
			return ReportPlatformUIImages
					.getImage( IReportGraphicConstants.ICON_ELEMENT_DATA );
		}
		if ( getDefaultHandle( ) instanceof TextDataHandle )
		{
			return ReportPlatformUIImages
					.getImage( IReportGraphicConstants.ICON_ELEMENT_TEXTDATA );
		}
		if ( getDefaultHandle( ) instanceof ImageHandle )
		{
			return ReportPlatformUIImages
					.getImage( IReportGraphicConstants.ICON_ELEMENT_IMAGE );
		}
		if ( getDefaultHandle( ) instanceof TableHandle )
		{
			return ReportPlatformUIImages
					.getImage( IReportGraphicConstants.ICON_ELEMENT_TABLE );
		}
		if ( getDefaultHandle( ) instanceof GridHandle )
		{
			return ReportPlatformUIImages
					.getImage( IReportGraphicConstants.ICON_ELEMENT_GRID );
		}
		if ( getDefaultHandle( ) instanceof ListHandle )
		{
			return ReportPlatformUIImages
					.getImage( IReportGraphicConstants.ICON_ELEMENT_LIST );
		}
		if ( getDefaultHandle( ) instanceof ExtendedItemHandle )
		{
			return ReportPlatformUIImages.getImage( getDefaultHandle( ) );
		}

		return null;
	}

	protected IFigure createFigure( )
	{
		LabelFigure label = new LabelFigure( );
		return label;
	}

	/**
	 * Perform director edit on label
	 */
	public void performDirectEdit( )
	{
		if ( getModel( ) instanceof TemplateElementHandle )
		{
			Object builder = null;
			TemplateElementHandle handle = getTemplateModel( );
			copiedHandle = handle.copyDefaultElement( ).getHandle(
					handle.getModule( ) );

			getCommandStack( )
					.startTrans(
							Messages
									.getString( "PlaceHolderEditPart.Trans.TransferToElement" ) );//$NON-NLS-1$

			if ( handle.getDefaultElement( ) instanceof LabelHandle )
			{

				builder = new LabelEditManager( this, TextCellEditor.class,
						new LabelCellEditorLocator( (Figure) getFigure( ) ) );
				( (LabelEditManager) builder ).setModel( (Object) copiedHandle );

				( (LabelEditManager) builder ).show( );
			}
			else
			{
				if(! (copiedHandle instanceof DataItemHandle))
				{					
					builder = ElementBuilderFactory.getInstance( ).createBuilder(
						copiedHandle );
				}
				if ( builder == null )
				{
					performTransfer( );
					getCommandStack( ).commit( );
					return;
				}

				if ( ( (Dialog) builder ).open( ) == Dialog.OK )
				{
					if ( builder instanceof ExpressionBuilder )
					{
						try
						{

							if ( copiedHandle instanceof TextDataHandle )
							{
								( (TextDataHandle) copiedHandle )
										.setValueExpr( ( (ExpressionBuilder) builder )
												.getResult( ) );

							}
//							if ( copiedHandle instanceof DataItemHandle )
//							{
//								( (DataItemHandle) copiedHandle )
//										.setValueExpr( ( (ExpressionBuilder) builder )
//												.getResult( ) );
//							}
						}
						catch ( SemanticException e )
						{
							getCommandStack( ).rollback( );
							return;
						}

					}
					performTransfer( );
					getCommandStack( ).commit( );
					return;
				}
				else
				{
					getCommandStack( ).rollback( );
					return;
				}
			}
		}
	}

	private CommandStack getCommandStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	public Object getCopiedModel( )
	{
		return copiedHandle;
	}

	public void performTransfer( )
	{
		if ( getModel( ) instanceof TemplateElementHandle )
		{
			try
			{
				( (TemplateReportItemHandle) getModel( ) )
						.transformToReportItem( (ReportItemHandle) copiedHandle );
				
//				ProviderFactory.createProvider( copiedHandle )
//				.performRequest( copiedHandle,
//						new Request( IRequestConstants.REQUST_REVERT_TO_REPORTITEM ) );
			}
			catch ( SemanticException e )
			{
				SessionHandleAdapter.getInstance( ).getCommandStack( )
						.rollbackAll( );
			}
			catch ( Exception e )
			{
				SessionHandleAdapter.getInstance( ).getCommandStack( )
				.rollbackAll( );
			}
			finally
			{
				copiedHandle = null;
			}
		}
	}

	public void perfrormLabelEdit( boolean changed )
	{
		if ( changed )
		{
			performTransfer( );
			getCommandStack( ).commit( );
		}
		else
		{
			getCommandStack( ).rollbackAll( );
		}
	}

	private DesignElementHandle getDefaultHandle( )
	{
		return getTemplateModel( ).getDefaultElement( );
	}

	private TemplateElementHandle getTemplateModel( )
	{
		return (TemplateElementHandle) getModel( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#getResizePolice(org.eclipse.gef.EditPolicy)
	 */
	public EditPolicy getResizePolice(EditPolicy parentPolice)
	{
		return new ReportElementNonResizablePolicy( );
	}
}
