/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Multiple Fill</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl#getFills <em>Fills</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultipleFillImpl extends FillImpl implements MultipleFill
{

	/**
	 * The cached value of the '{@link #getFills() <em>Fills</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFills()
	 * @generated
	 * @ordered
	 */
	protected EList<Fill> fills;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected MultipleFillImpl( )
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
		return AttributePackage.Literals.MULTIPLE_FILL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Fill> getFills( )
	{
		if ( fills == null )
		{
			fills = new EObjectContainmentEList<Fill>( Fill.class,
					this,
					AttributePackage.MULTIPLE_FILL__FILLS );
		}
		return fills;
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
			case AttributePackage.MULTIPLE_FILL__FILLS :
				return ( (InternalEList<?>) getFills( ) ).basicRemove( otherEnd,
						msgs );
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
			case AttributePackage.MULTIPLE_FILL__FILLS :
				return getFills( );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case AttributePackage.MULTIPLE_FILL__FILLS :
				getFills( ).clear( );
				getFills( ).addAll( (Collection<? extends Fill>) newValue );
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
			case AttributePackage.MULTIPLE_FILL__FILLS :
				getFills( ).clear( );
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
			case AttributePackage.MULTIPLE_FILL__FILLS :
				return fills != null && !fills.isEmpty( );
		}
		return super.eIsSet( featureID );
	}

	/**
	 * Manually written.
	 * 
	 * @return
	 */
	public static MultipleFill create( )
	{
		return AttributeFactory.eINSTANCE.createMultipleFill( );
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static MultipleFill copyInstance( MultipleFill src )
	{
		if ( src == null )
		{
			return null;
		}

		MultipleFillImpl dest = new MultipleFillImpl( );

		if ( src.getFills( ) != null )
		{
			EList<Fill> list = dest.getFills( );
			for ( Fill element : src.getFills( ) )
			{
				list.add( FillImpl.copyInstance( element ) );
			}
		}

		dest.type = src.getType( );
		dest.typeESet = src.isSetType( );

		return dest;
	}

} // MultipleFillImpl
