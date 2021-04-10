/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.layout.util;

import java.util.Map;

import org.eclipse.birt.chart.model.layout.LayoutPackage;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class LayoutXMLProcessor extends XMLProcessor {

	/**
	 * Public constructor to instantiate the helper. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public LayoutXMLProcessor() {
		super((EPackage.Registry.INSTANCE));
		LayoutPackage.eINSTANCE.eClass();
	}

	/**
	 * Register for "*" and "xml" file extensions the LayoutResourceFactoryImpl
	 * factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected Map<String, Resource.Factory> getRegistrations() {
		if (registrations == null) {
			super.getRegistrations();
			registrations.put(XML_EXTENSION, new LayoutResourceFactoryImpl());
			registrations.put(STAR_EXTENSION, new LayoutResourceFactoryImpl());
		}
		return registrations;
	}

} // LayoutXMLProcessor
