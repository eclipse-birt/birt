/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id: LayoutValidator.java,v 1.3 2009/05/08 06:15:18 ywang1 Exp $
 */

package org.eclipse.birt.chart.model.layout.util;

import java.util.Map;

import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc --> The <b>Validator</b> for the model. <!-- end-user-doc
 * -->
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage
 * @generated
 */
public class LayoutValidator extends EObjectValidator {

	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final LayoutValidator INSTANCE = new LayoutValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource()
	 * source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode()
	 * codes} from this package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "org.eclipse.birt.chart.model.layout"; //$NON-NLS-1$

	/**
	 * A constant with a fixed name that can be used as the base value for
	 * additional hand written constants. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for
	 * additional hand written constants in a derived class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	protected XMLTypeValidator xmlTypeValidator;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	public LayoutValidator() {
		super();
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
		return LayoutPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		switch (classifierID) {
		case LayoutPackage.BLOCK:
			return validateBlock((Block) value, diagnostics, context);
		case LayoutPackage.CLIENT_AREA:
			return validateClientArea((ClientArea) value, diagnostics, context);
		case LayoutPackage.LABEL_BLOCK:
			return validateLabelBlock((LabelBlock) value, diagnostics, context);
		case LayoutPackage.LEGEND:
			return validateLegend((Legend) value, diagnostics, context);
		case LayoutPackage.PLOT:
			return validatePlot((Plot) value, diagnostics, context);
		case LayoutPackage.TITLE_BLOCK:
			return validateTitleBlock((TitleBlock) value, diagnostics, context);
		case LayoutPackage.ELLIPSIS_TYPE:
			return validateEllipsisType((Integer) value, diagnostics, context);
		case LayoutPackage.ELLIPSIS_TYPE_OBJECT:
			return validateEllipsisTypeObject((Integer) value, diagnostics, context);
		case LayoutPackage.TITLE_PERCENT_TYPE:
			return validateTitlePercentType((Double) value, diagnostics, context);
		case LayoutPackage.TITLE_PERCENT_TYPE_OBJECT:
			return validateTitlePercentTypeObject((Double) value, diagnostics, context);
		default:
			return true;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateBlock(Block block, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) block, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateClientArea(ClientArea clientArea, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) clientArea, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateLabelBlock(LabelBlock labelBlock, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) labelBlock, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateLegend(Legend legend, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) legend, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validatePlot(Plot plot, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) plot, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateTitleBlock(TitleBlock titleBlock, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) titleBlock, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateEllipsisType(int ellipsisType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateEllipsisType_Min(ellipsisType, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @see #validateEllipsisType_Min
	 */
	public static final int ELLIPSIS_TYPE__MIN__VALUE = 0;

	/**
	 * Validates the Min constraint of '<em>Ellipsis Type</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateEllipsisType_Min(int ellipsisType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = ellipsisType >= ELLIPSIS_TYPE__MIN__VALUE;
		if (!result && diagnostics != null) {
			reportMinViolation(LayoutPackage.Literals.ELLIPSIS_TYPE, ellipsisType, ELLIPSIS_TYPE__MIN__VALUE, true,
					diagnostics, context);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateEllipsisTypeObject(Integer ellipsisTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = validateEllipsisType_Min(ellipsisTypeObject, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateTitlePercentType(double titlePercentType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = validateTitlePercentType_Min(titlePercentType, diagnostics, context);
		if (result || diagnostics != null) {
			result &= validateTitlePercentType_Max(titlePercentType, diagnostics, context);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @see #validateTitlePercentType_Min
	 */
	public static final double TITLE_PERCENT_TYPE__MIN__VALUE = 0.0;

	/**
	 * Validates the Min constraint of '<em>Title Percent Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateTitlePercentType_Min(double titlePercentType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = titlePercentType >= TITLE_PERCENT_TYPE__MIN__VALUE;
		if (!result && diagnostics != null) {
			reportMinViolation(LayoutPackage.Literals.TITLE_PERCENT_TYPE, titlePercentType,
					TITLE_PERCENT_TYPE__MIN__VALUE, true, diagnostics, context);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @see #validateTitlePercentType_Max
	 */
	public static final double TITLE_PERCENT_TYPE__MAX__VALUE = 1.0;

	/**
	 * Validates the Max constraint of '<em>Title Percent Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateTitlePercentType_Max(double titlePercentType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = titlePercentType <= TITLE_PERCENT_TYPE__MAX__VALUE;
		if (!result && diagnostics != null) {
			reportMaxViolation(LayoutPackage.Literals.TITLE_PERCENT_TYPE, titlePercentType,
					TITLE_PERCENT_TYPE__MAX__VALUE, true, diagnostics, context);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateTitlePercentTypeObject(Double titlePercentTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = validateTitlePercentType_Min(titlePercentTypeObject, diagnostics, context);
		if (result || diagnostics != null) {
			result &= validateTitlePercentType_Max(titlePercentTypeObject, diagnostics, context);
		}
		return result;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this
	 * validator's diagnostics. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this
		// validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} // LayoutValidator
