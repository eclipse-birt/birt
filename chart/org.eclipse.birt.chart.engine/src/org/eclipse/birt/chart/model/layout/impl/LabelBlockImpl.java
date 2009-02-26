/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.layout.impl;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.CursorImpl;
import org.eclipse.birt.chart.model.attribute.impl.FillImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Label Block</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LabelBlockImpl extends BlockImpl implements LabelBlock
{

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected Label label;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected LabelBlockImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass( )
	{
		return LayoutPackage.Literals.LABEL_BLOCK;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Label getLabel( )
	{
		return label;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLabel( Label newLabel,
			NotificationChain msgs )
	{
		Label oldLabel = label;
		label = newLabel;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					LayoutPackage.LABEL_BLOCK__LABEL,
					oldLabel,
					newLabel );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLabel( Label newLabel )
	{
		if ( newLabel != label )
		{
			NotificationChain msgs = null;
			if ( label != null )
				msgs = ( (InternalEObject) label ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- LayoutPackage.LABEL_BLOCK__LABEL,
						null,
						msgs );
			if ( newLabel != null )
				msgs = ( (InternalEObject) newLabel ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- LayoutPackage.LABEL_BLOCK__LABEL,
						null,
						msgs );
			msgs = basicSetLabel( newLabel, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					LayoutPackage.LABEL_BLOCK__LABEL,
					newLabel,
					newLabel ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, NotificationChain msgs )
	{
		switch ( featureID )
		{
			case LayoutPackage.LABEL_BLOCK__LABEL :
				return basicSetLabel( null, msgs );
		}
		return super.eInverseRemove( otherEnd, featureID, msgs );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case LayoutPackage.LABEL_BLOCK__LABEL :
				return getLabel( );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case LayoutPackage.LABEL_BLOCK__LABEL :
				setLabel( (Label) newValue );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case LayoutPackage.LABEL_BLOCK__LABEL :
				setLabel( (Label) null );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case LayoutPackage.LABEL_BLOCK__LABEL :
				return label != null;
		}
		return super.eIsSet( featureID );
	}

	/**
	 * A convenience method to create an initialized 'LabelBlock' instance
	 * 
	 * @return
	 */
	public static Block create( )
	{
		final LabelBlock lb = LayoutFactory.eINSTANCE.createLabelBlock( );
		( (LabelBlockImpl) lb ).initialize( );
		return lb;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected void initialize( )
	{
		super.initialize( );
		setLabel( LabelImpl.create( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.layout.Block#getPreferredSize(org.eclipse.birt.chart.device.IDisplayServer,
	 *      org.eclipse.birt.chart.model.Chart,
	 *      org.eclipse.birt.chart.factory.RunTimeContext)
	 */
	public final Size getPreferredSize( IDisplayServer xs, Chart cm,
			RunTimeContext rtc ) throws ChartException
	{
		BoundingBox bb = computeBox( xs, rtc );

		final Size sz = SizeImpl.create( bb.getWidth( ), bb.getHeight( ) );
		sz.scale( 72d / xs.getDpiResolution( ) );
		final Insets ins = getInsets( );
		sz.setHeight( sz.getHeight( ) + ins.getTop( ) + ins.getBottom( ) );
		sz.setWidth( sz.getWidth( ) + ins.getLeft( ) + ins.getRight( ) );
		return sz;
	}

	protected BoundingBox computeBox( IDisplayServer xs, RunTimeContext rtc )
			throws ChartException
	{
		Label la = LabelImpl.copyInstance( getLabel( ) );
		final String sPreviousValue = getLabel( ).getCaption( ).getValue( );
		la.getCaption( ).setValue( rtc.externalizedMessage( sPreviousValue ) );
		try
		{
			return Methods.computeBox( xs, IConstants.TOP, la, 0, 0 );
		}
		catch ( IllegalArgumentException uiex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					uiex );
		}
	}

	private static LabelBlock copyInstanceThis( LabelBlock src )
	{
		if ( src == null )
		{
			return null;
		}

		LabelBlockImpl dest = new LabelBlockImpl( );

		if ( src.getChildren( ) != null )
		{
			EList<Block> list = dest.getChildren( );
			for ( Block element : src.getChildren( ) )
			{
				list.add( BlockImpl.copyInstance( element ) );
			}
		}

		if ( src.getBounds( ) != null )
		{
			dest.setBounds( BoundsImpl.copyInstance( src.getBounds( ) ) );
		}

		if ( src.getInsets( ) != null )
		{
			dest.setInsets( InsetsImpl.copyInstance( src.getInsets( ) ) );
		}

		if ( src.getMinSize( ) != null )
		{
			dest.setMinSize( SizeImpl.copyInstance( src.getMinSize( ) ) );
		}

		if ( src.getOutline( ) != null )
		{
			dest.setOutline( LineAttributesImpl.copyInstance( src.getOutline( ) ) );
		}

		if ( src.getBackground( ) != null )
		{
			dest.setBackground( FillImpl.copyInstance( src.getBackground( ) ) );
		}

		if ( src.getTriggers( ) != null )
		{
			EList<Trigger> list = dest.getTriggers( );
			for ( Trigger element : src.getTriggers( ) )
			{
				list.add( TriggerImpl.copyInstance( element ) );
			}
		}

		if ( src.getCursor( ) != null )
		{
			dest.setCursor( CursorImpl.copyInstance( src.getCursor( ) ) );
		}

		if ( src.getLabel( ) != null )
		{
			dest.setLabel( LabelImpl.copyInstance( src.getLabel( ) ) );
		}

		dest.anchor = src.getAnchor( );
		dest.anchorESet = src.isSetAnchor( );
		dest.stretch = src.getStretch( );
		dest.stretchESet = src.isSetStretch( );
		dest.row = src.getRow( );
		dest.rowESet = src.isSetRow( );
		dest.column = src.getColumn( );
		dest.columnESet = src.isSetColumn( );
		dest.rowspan = src.getRowspan( );
		dest.rowspanESet = src.isSetRowspan( );
		dest.columnspan = src.getColumnspan( );
		dest.columnspanESet = src.isSetColumnspan( );
		dest.visible = src.isVisible( );
		dest.visibleESet = src.isSetVisible( );
		dest.widthHint = src.getWidthHint( );
		dest.widthHintESet = src.isSetWidthHint( );
		dest.heightHint = src.getHeightHint( );
		dest.heightHintESet = src.isSetHeightHint( );

		return dest;
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static LabelBlock copyInstance( LabelBlock src )
	{
		if ( src == null )
		{
			return null;
		}

		if ( src instanceof TitleBlock )
		{
			return TitleBlockImpl.copyInstance( (TitleBlock) src );
		}
		else
		{
			return copyInstanceThis( src );
		}
	}

} // LabelBlockImpl
