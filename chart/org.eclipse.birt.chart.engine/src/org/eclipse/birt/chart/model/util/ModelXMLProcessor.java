/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.util;

import java.util.Map;

import org.eclipse.birt.chart.model.ModelPackage;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ModelXMLProcessor extends XMLProcessor {

	/**
	 * Public constructor to instantiate the helper. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelXMLProcessor() {
		super((EPackage.Registry.INSTANCE));
		ModelPackage.eINSTANCE.eClass();
	}

	/**
	 * Register for "*" and "xml" file extensions the ModelResourceFactoryImpl
	 * factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected Map<String, Resource.Factory> getRegistrations() {
		if (registrations == null) {
			super.getRegistrations();
			registrations.put(XML_EXTENSION, new ModelResourceFactoryImpl());
			registrations.put(STAR_EXTENSION, new ModelResourceFactoryImpl());
		}
		return registrations;
	}

} // ModelXMLProcessor
