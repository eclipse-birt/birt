/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.layout.impl;

import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory </b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class LayoutFactoryImpl extends EFactoryImpl implements LayoutFactory {

	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static LayoutFactory init() {
		try {
			LayoutFactory theLayoutFactory = (LayoutFactory) EPackage.Registry.INSTANCE
					.getEFactory("http://www.birt.eclipse.org/ChartModelLayout"); //$NON-NLS-1$
			if (theLayoutFactory != null) {
				return theLayoutFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new LayoutFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public LayoutFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case LayoutPackage.BLOCK:
			return (EObject) createBlock();
		case LayoutPackage.CLIENT_AREA:
			return (EObject) createClientArea();
		case LayoutPackage.LABEL_BLOCK:
			return (EObject) createLabelBlock();
		case LayoutPackage.LEGEND:
			return (EObject) createLegend();
		case LayoutPackage.PLOT:
			return (EObject) createPlot();
		case LayoutPackage.TITLE_BLOCK:
			return (EObject) createTitleBlock();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case LayoutPackage.ELLIPSIS_TYPE:
			return createEllipsisTypeFromString(eDataType, initialValue);
		case LayoutPackage.ELLIPSIS_TYPE_OBJECT:
			return createEllipsisTypeObjectFromString(eDataType, initialValue);
		case LayoutPackage.TITLE_PERCENT_TYPE:
			return createTitlePercentTypeFromString(eDataType, initialValue);
		case LayoutPackage.TITLE_PERCENT_TYPE_OBJECT:
			return createTitlePercentTypeObjectFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case LayoutPackage.ELLIPSIS_TYPE:
			return convertEllipsisTypeToString(eDataType, instanceValue);
		case LayoutPackage.ELLIPSIS_TYPE_OBJECT:
			return convertEllipsisTypeObjectToString(eDataType, instanceValue);
		case LayoutPackage.TITLE_PERCENT_TYPE:
			return convertTitlePercentTypeToString(eDataType, instanceValue);
		case LayoutPackage.TITLE_PERCENT_TYPE_OBJECT:
			return convertTitlePercentTypeObjectToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Block createBlock() {
		BlockImpl block = new BlockImpl();
		return block;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ClientArea createClientArea() {
		ClientAreaImpl clientArea = new ClientAreaImpl();
		return clientArea;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LabelBlock createLabelBlock() {
		LabelBlockImpl labelBlock = new LabelBlockImpl();
		return labelBlock;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Legend createLegend() {
		LegendImpl legend = new LegendImpl();
		return legend;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Plot createPlot() {
		PlotImpl plot = new PlotImpl();
		return plot;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TitleBlock createTitleBlock() {
		TitleBlockImpl titleBlock = new TitleBlockImpl();
		return titleBlock;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Integer createEllipsisTypeFromString(EDataType eDataType, String initialValue) {
		return (Integer) XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INT, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertEllipsisTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Integer createEllipsisTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createEllipsisTypeFromString(LayoutPackage.Literals.ELLIPSIS_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertEllipsisTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertEllipsisTypeToString(LayoutPackage.Literals.ELLIPSIS_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Double createTitlePercentTypeFromString(EDataType eDataType, String initialValue) {
		return (Double) XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DOUBLE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertTitlePercentTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DOUBLE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Double createTitlePercentTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createTitlePercentTypeFromString(LayoutPackage.Literals.TITLE_PERCENT_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertTitlePercentTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertTitlePercentTypeToString(LayoutPackage.Literals.TITLE_PERCENT_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LayoutPackage getLayoutPackage() {
		return (LayoutPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static LayoutPackage getPackage() {
		return LayoutPackage.eINSTANCE;
	}

} // LayoutFactoryImpl
