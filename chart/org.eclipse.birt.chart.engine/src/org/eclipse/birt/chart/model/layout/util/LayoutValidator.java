/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.layout.util;

import java.util.Map;

import org.eclipse.birt.chart.model.layout.*;

import org.eclipse.emf.common.util.DiagnosticChain;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage
 * @generated
 */
public class LayoutValidator extends EObjectValidator
{

	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final LayoutValidator INSTANCE = new LayoutValidator( );

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "org.eclipse.birt.chart.model.layout"; //$NON-NLS-1$

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XMLTypeValidator xmlTypeValidator;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LayoutValidator( )
	{
		super( );
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EPackage getEPackage( )
	{
		return LayoutPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected boolean validate( int classifierID, Object value,
			DiagnosticChain diagnostics, Map context )
	{
		switch ( classifierID )
		{
			case LayoutPackage.BLOCK :
				return validateBlock( (Block) value, diagnostics, context );
			case LayoutPackage.CLIENT_AREA :
				return validateClientArea( (ClientArea) value,
						diagnostics,
						context );
			case LayoutPackage.LABEL_BLOCK :
				return validateLabelBlock( (LabelBlock) value,
						diagnostics,
						context );
			case LayoutPackage.LEGEND :
				return validateLegend( (Legend) value, diagnostics, context );
			case LayoutPackage.PLOT :
				return validatePlot( (Plot) value, diagnostics, context );
			case LayoutPackage.TITLE_BLOCK :
				return validateTitleBlock( (TitleBlock) value,
						diagnostics,
						context );
			case LayoutPackage.TITLE_PERCENT_TYPE :
				return validateTitlePercentType( ( (Double) value ).doubleValue( ),
						diagnostics,
						context );
			case LayoutPackage.TITLE_PERCENT_TYPE_OBJECT :
				return validateTitlePercentTypeObject( (Double) value,
						diagnostics,
						context );
			default :
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlock( Block block, DiagnosticChain diagnostics,
			Map context )
	{
		return validate_EveryDefaultConstraint( block, diagnostics, context );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateClientArea( ClientArea clientArea,
			DiagnosticChain diagnostics, Map context )
	{
		return validate_EveryDefaultConstraint( clientArea,
				diagnostics,
				context );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLabelBlock( LabelBlock labelBlock,
			DiagnosticChain diagnostics, Map context )
	{
		return validate_EveryDefaultConstraint( labelBlock,
				diagnostics,
				context );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLegend( Legend legend, DiagnosticChain diagnostics,
			Map context )
	{
		return validate_EveryDefaultConstraint( legend, diagnostics, context );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePlot( Plot plot, DiagnosticChain diagnostics,
			Map context )
	{
		return validate_EveryDefaultConstraint( plot, diagnostics, context );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTitleBlock( TitleBlock titleBlock,
			DiagnosticChain diagnostics, Map context )
	{
		return validate_EveryDefaultConstraint( titleBlock,
				diagnostics,
				context );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTitlePercentType( double titlePercentType,
			DiagnosticChain diagnostics, Map context )
	{
		boolean result = validateTitlePercentType_Min( titlePercentType,
				diagnostics,
				context );
		if ( result || diagnostics != null )
			result &= validateTitlePercentType_Max( titlePercentType,
					diagnostics,
					context );
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateTitlePercentType_Min
	 */
	public static final double TITLE_PERCENT_TYPE__MIN__VALUE = 0.0;

	/**
	 * Validates the Min constraint of '<em>Title Percent Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTitlePercentType_Min( double titlePercentType,
			DiagnosticChain diagnostics, Map context )
	{
		boolean result = titlePercentType >= TITLE_PERCENT_TYPE__MIN__VALUE;
		if ( !result && diagnostics != null )
			reportMinViolation( LayoutPackage.Literals.TITLE_PERCENT_TYPE,
					new Double( titlePercentType ),
					new Double( TITLE_PERCENT_TYPE__MIN__VALUE ),
					true,
					diagnostics,
					context );
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateTitlePercentType_Max
	 */
	public static final double TITLE_PERCENT_TYPE__MAX__VALUE = 1.0;

	/**
	 * Validates the Max constraint of '<em>Title Percent Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTitlePercentType_Max( double titlePercentType,
			DiagnosticChain diagnostics, Map context )
	{
		boolean result = titlePercentType <= TITLE_PERCENT_TYPE__MAX__VALUE;
		if ( !result && diagnostics != null )
			reportMaxViolation( LayoutPackage.Literals.TITLE_PERCENT_TYPE,
					new Double( titlePercentType ),
					new Double( TITLE_PERCENT_TYPE__MAX__VALUE ),
					true,
					diagnostics,
					context );
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTitlePercentTypeObject(
			Double titlePercentTypeObject, DiagnosticChain diagnostics,
			Map context )
	{
		boolean result = validateTitlePercentType_Min( titlePercentTypeObject.doubleValue( ),
				diagnostics,
				context );
		if ( result || diagnostics != null )
			result &= validateTitlePercentType_Max( titlePercentTypeObject.doubleValue( ),
					diagnostics,
					context );
		return result;
	}

} //LayoutValidator
