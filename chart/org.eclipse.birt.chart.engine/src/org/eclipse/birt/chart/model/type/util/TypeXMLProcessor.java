/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.type.util;

import java.util.Map;

import org.eclipse.birt.chart.model.type.TypePackage;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class TypeXMLProcessor extends XMLProcessor {

	/**
	 * Public constructor to instantiate the helper. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public TypeXMLProcessor() {
		super((EPackage.Registry.INSTANCE));
		TypePackage.eINSTANCE.eClass();
	}

	/**
	 * Register for "*" and "xml" file extensions the TypeResourceFactoryImpl
	 * factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected Map<String, Resource.Factory> getRegistrations() {
		if (registrations == null) {
			super.getRegistrations();
			registrations.put(XML_EXTENSION, new TypeResourceFactoryImpl());
			registrations.put(STAR_EXTENSION, new TypeResourceFactoryImpl());
		}
		return registrations;
	}

} // TypeXMLProcessor
