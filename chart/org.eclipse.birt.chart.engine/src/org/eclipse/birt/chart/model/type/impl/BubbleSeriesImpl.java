/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.type.impl;

import java.util.Map;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.CursorImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.DataSetImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Bubble Series</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl#getAccLineAttributes <em>Acc Line Attributes</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl#getAccOrientation <em>Acc Orientation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BubbleSeriesImpl extends ScatterSeriesImpl implements BubbleSeries
{

	/**
	 * The cached value of the '{@link #getAccLineAttributes() <em>Acc Line Attributes</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAccLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes accLineAttributes;

	/**
	 * The default value of the '{@link #getAccOrientation() <em>Acc Orientation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAccOrientation()
	 * @generated
	 * @ordered
	 */
	protected static final Orientation ACC_ORIENTATION_EDEFAULT = Orientation.HORIZONTAL_LITERAL;

	/**
	 * The cached value of the '{@link #getAccOrientation() <em>Acc Orientation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAccOrientation()
	 * @generated
	 * @ordered
	 */
	protected Orientation accOrientation = ACC_ORIENTATION_EDEFAULT;

	/**
	 * This is true if the Acc Orientation attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean accOrientationESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected BubbleSeriesImpl( )
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
		return TypePackage.Literals.BUBBLE_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public LineAttributes getAccLineAttributes( )
	{
		return accLineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAccLineAttributes(
			LineAttributes newAccLineAttributes, NotificationChain msgs )
	{
		LineAttributes oldAccLineAttributes = accLineAttributes;
		accLineAttributes = newAccLineAttributes;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES,
					oldAccLineAttributes,
					newAccLineAttributes );
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
	public void setAccLineAttributes( LineAttributes newAccLineAttributes )
	{
		if ( newAccLineAttributes != accLineAttributes )
		{
			NotificationChain msgs = null;
			if ( accLineAttributes != null )
				msgs = ( (InternalEObject) accLineAttributes ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES,
						null,
						msgs );
			if ( newAccLineAttributes != null )
				msgs = ( (InternalEObject) newAccLineAttributes ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES,
						null,
						msgs );
			msgs = basicSetAccLineAttributes( newAccLineAttributes, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES,
					newAccLineAttributes,
					newAccLineAttributes ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Orientation getAccOrientation( )
	{
		return accOrientation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setAccOrientation( Orientation newAccOrientation )
	{
		Orientation oldAccOrientation = accOrientation;
		accOrientation = newAccOrientation == null ? ACC_ORIENTATION_EDEFAULT
				: newAccOrientation;
		boolean oldAccOrientationESet = accOrientationESet;
		accOrientationESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.BUBBLE_SERIES__ACC_ORIENTATION,
					oldAccOrientation,
					accOrientation,
					!oldAccOrientationESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAccOrientation( )
	{
		Orientation oldAccOrientation = accOrientation;
		boolean oldAccOrientationESet = accOrientationESet;
		accOrientation = ACC_ORIENTATION_EDEFAULT;
		accOrientationESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					TypePackage.BUBBLE_SERIES__ACC_ORIENTATION,
					oldAccOrientation,
					ACC_ORIENTATION_EDEFAULT,
					oldAccOrientationESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAccOrientation( )
	{
		return accOrientationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, NotificationChain msgs )
	{
		switch ( featureID )
		{
			case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES :
				return basicSetAccLineAttributes( null, msgs );
		}
		return super.eInverseRemove( otherEnd, featureID, msgs );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES :
				return getAccLineAttributes( );
			case TypePackage.BUBBLE_SERIES__ACC_ORIENTATION :
				return getAccOrientation( );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES :
				setAccLineAttributes( (LineAttributes) newValue );
				return;
			case TypePackage.BUBBLE_SERIES__ACC_ORIENTATION :
				setAccOrientation( (Orientation) newValue );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES :
				setAccLineAttributes( (LineAttributes) null );
				return;
			case TypePackage.BUBBLE_SERIES__ACC_ORIENTATION :
				unsetAccOrientation( );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES :
				return accLineAttributes != null;
			case TypePackage.BUBBLE_SERIES__ACC_ORIENTATION :
				return isSetAccOrientation( );
		}
		return super.eIsSet( featureID );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (accOrientation: " ); //$NON-NLS-1$
		if ( accOrientationESet )
			result.append( accOrientation );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return
	 */
	public static final Series create( )
	{
		final BubbleSeries bs = TypeFactory.eINSTANCE.createBubbleSeries( );
		( (BubbleSeriesImpl) bs ).initialize( );
		return bs;
	}

	/**
	 * Initializes all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initialize( )
	{
		super.initialize( );

		LineAttributes la = LineAttributesImpl.create( ColorDefinitionImpl.BLACK( ),
				LineStyle.SOLID_LITERAL,
				1 );
		la.setVisible( false );

		setAccLineAttributes( la );

		Marker firstMarker = getMarkers( ).get( 0 );
		firstMarker.setType( MarkerType.CIRCLE_LITERAL );
		firstMarker.setVisible( true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "BubbleSeriesImpl.displayName" ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.model.component.Series#isSingleCache()
	 */
	public boolean isSingleCache( )
	{
		return true;
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static BubbleSeries copyInstance( BubbleSeries src )
	{
		if ( src == null )
		{
			return null;
		}

		BubbleSeriesImpl dest = new BubbleSeriesImpl( );

		if ( src.getLabel( ) != null )
		{
			dest.setLabel( LabelImpl.copyInstance( src.getLabel( ) ) );
		}

		if ( src.getDataDefinition( ) != null )
		{
			EList<Query> list = dest.getDataDefinition( );
			for ( Query element : src.getDataDefinition( ) )
			{
				list.add( QueryImpl.copyInstance( element ) );
			}
		}

		if ( src.getDataPoint( ) != null )
		{
			dest.setDataPoint( DataPointImpl.copyInstance( src.getDataPoint( ) ) );
		}

		if ( src.getDataSets( ) != null )
		{
			EMap<String, DataSet> map = dest.getDataSets( );
			for ( Map.Entry<String, DataSet> entry : src.getDataSets( )
					.entrySet( ) )
			{
				map.put( entry.getKey( ),
						DataSetImpl.copyInstance( entry.getValue( ) ) );
			}
		}

		if ( src.getTriggers( ) != null )
		{
			EList<Trigger> list = dest.getTriggers( );
			for ( Trigger element : src.getTriggers( ) )
			{
				list.add( TriggerImpl.copyInstance( element ) );
			}
		}

		if ( src.getCurveFitting( ) != null )
		{
			dest.setCurveFitting( CurveFittingImpl.copyInstance( src.getCurveFitting( ) ) );
		}

		if ( src.getCursor( ) != null )
		{
			dest.setCursor( CursorImpl.copyInstance( src.getCursor( ) ) );
		}

		if ( src.getMarkers( ) != null )
		{
			EList<Marker> list = dest.getMarkers( );
			for ( Marker element : src.getMarkers( ) )
			{
				list.add( MarkerImpl.copyInstance( element ) );
			}
		}

		if ( src.getMarker( ) != null )
		{
			dest.setMarker( MarkerImpl.copyInstance( src.getMarker( ) ) );
		}

		if ( src.getLineAttributes( ) != null )
		{
			dest.setLineAttributes( LineAttributesImpl.copyInstance( src.getLineAttributes( ) ) );
		}

		if ( src.getShadowColor( ) != null )
		{
			dest.setShadowColor( ColorDefinitionImpl.copyInstance( src.getShadowColor( ) ) );
		}

		if ( src.getAccLineAttributes( ) != null )
		{
			dest.setAccLineAttributes( LineAttributesImpl.copyInstance( src.getAccLineAttributes( ) ) );
		}

		dest.visible = src.isVisible( );
		dest.visibleESet = src.isSetVisible( );
		dest.seriesIdentifier = src.getSeriesIdentifier( );
		dest.labelPosition = src.getLabelPosition( );
		dest.labelPositionESet = src.isSetLabelPosition( );
		dest.stacked = src.isStacked( );
		dest.stackedESet = src.isSetStacked( );
		dest.translucent = src.isTranslucent( );
		dest.translucentESet = src.isSetTranslucent( );
		dest.paletteLineColor = src.isPaletteLineColor( );
		dest.paletteLineColorESet = src.isSetPaletteLineColor( );
		dest.curve = src.isCurve( );
		dest.curveESet = src.isSetCurve( );
		dest.connectMissingValue = src.isConnectMissingValue( );
		dest.connectMissingValueESet = src.isSetConnectMissingValue( );
		dest.accOrientation = src.getAccOrientation( );
		dest.accOrientationESet = src.isSetAccOrientation( );

		return dest;
	}

} // BubbleSeriesImpl
