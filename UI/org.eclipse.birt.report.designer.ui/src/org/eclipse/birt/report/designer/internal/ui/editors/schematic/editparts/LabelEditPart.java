/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.LabelDirectEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.LabelCellEditorLocator;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.LabelEditManager;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;

/**
 * Provides support for label edit parts.
 *  
 */
public class LabelEditPart extends ReportElementEditPart
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.facade.IModelAdaptHelper#markDirty(boolean)
	 */
	private static final String ELEMENT_DEFAULT_TEXT = Messages.getString( "LabelEditPart.Figure.Default" );//$NON-NLS-1$

	public void markDirty( boolean bool )
	{
		super.markDirty( bool );

		// refresh label to adopt container's changes.
		if ( bool )
		{
			refreshVisuals( );
		}
	}

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public LabelEditPart( Object model )
	{
		super( model );
	}

	private DirectEditManager manager;

	protected AccessibleEditPart createAccessible( )
	{
		return new AccessibleGraphicalEditPart( ) {

			public void getValue( AccessibleControlEvent e )
			{
				//TODO: need to create LabelHandleAdapter
				e.result = ( (Label) getModel( ) ).getStringProperty( SessionHandleAdapter.getInstance( )
						.getReportDesign( ),
						Label.TEXT_ID_PROP );
			}

			public void getName( AccessibleEvent e )
			{
				e.result = ( (Label) getModel( ) ).getStringProperty( SessionHandleAdapter.getInstance( )
						.getReportDesign( ),
						Label.TEXT_ID_PROP );
			}
		};
	}

	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE,
				new ReportComponentEditPolicy( ) );

		installEditPolicy( EditPolicy.DIRECT_EDIT_ROLE,
				new LabelDirectEditPolicy( ) );
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
		if ( manager == null )
			manager = new LabelEditManager( this,
					TextCellEditor.class,
					new LabelCellEditorLocator( (Figure) getFigure( ) ) );
		manager.show( );
	}

	/**
	 * perform edit directly when the request is the corresponding type.
	 */
	public void performRequest( Request request )
	{
		if ( request.getType( ) == RequestConstants.REQ_OPEN )
		{
			performDirectEdit( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#notify(org.eclipse.birt.model.core.DesignElement,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle arg0, NotificationEvent arg1 )
	{
		markDirty( true );
		refreshVisuals( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		StyleHandle style = ( (DesignElementHandle) getModel( ) ).getPrivateStyle( );

		( (LabelFigure) getFigure( ) ).setText( getText( ) );
		( (LabelFigure) getFigure( ) ).setFont( getFont( ) );

		( (LabelFigure) getFigure( ) ).setTextUnderline( style.getTextUnderline( ) );
		( (LabelFigure) getFigure( ) ).setTextLineThrough( style.getTextLineThrough( ) );
		( (LabelFigure) getFigure( ) ).setTextOverline( style.getTextOverline( ) );
		( (LabelFigure) getFigure( ) ).setTextAlign( style.getTextAlign( ) );
		( (LabelFigure) getFigure( ) ).setVerticalAlign( style.getVerticalAlign( ) );

		( (LabelFigure) getFigure( ) ).setDisplay( style.getDisplay( ) );

		( (AbstractGraphicalEditPart) getParent( ) ).setLayoutConstraint( this,
				getFigure( ),
				getConstraint( ) );
		( (LabelFigure) getFigure( ) ).setForegroundColor( ColorManager.getColor( getForegroundColor( (ReportItemHandle) getModel( ) ) ) );
		refreshBorder( (DesignElementHandle) getModel( ), new LineBorder( ) );

		if ( !hasText( ) )
			( (LabelFigure) getFigure( ) ).setForegroundColor( ColorConstants.lightGray );
		else
			( (LabelFigure) getFigure( ) ).setForegroundColor( ColorManager.getColor( getForegroundColor( (ReportItemHandle) getModel( ) ) ) );

		refreshBackground( (DesignElementHandle) getModel( ) );
	}

	protected boolean hasText( )
	{
		if ( StringUtil.isBlank( ( (LabelHandle) getModel( ) ).getDisplayText( ) ) )
		{
			return false;
		}

		return true;
	}

	/**
	 * Get the text shown on label.
	 * 
	 * @return The text shown on label
	 */
	protected String getText( )
	{
		String text = ( (LabelHandle) getModel( ) ).getDisplayText( );
		if ( text == null )
		{
			text = ELEMENT_DEFAULT_TEXT;
		}
		return text;
	}

	/**
	 * @return The constraint
	 */
	protected Object getConstraint( )
	{
		ReportItemHandle handle = (ReportItemHandle) getModel( );
		ReportItemConstraint constraint = new ReportItemConstraint( );

		constraint.setDisplay( handle.getPrivateStyle( ).getDisplay( ) );
		return constraint;
	}

}