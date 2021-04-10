/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelXMLProcessor.java,v 1.1.28.1 2010/11/29 06:23:52 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model.util;

import java.util.Map;

import org.eclipse.birt.report.model.adapter.oda.model.ModelPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
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

	/**
	 * Creates and returns a new resource for saving or loading an ODA Design
	 * object.
	 * 
	 * @param uri the URI of the resource to create
	 * @return a new resource
	 * @since DTP 1.6
	 * @generated NOT
	 */

	public Resource createResource(URI uri) {
		ResourceSet resourceSet = createResourceSet();
		// Register the Design package to ensure it is available during loading.
		resourceSet.getPackageRegistry().put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);

		XMLResource resource = (XMLResource) resourceSet.createResource(uri);

		// Use the OPTION_SCHEMA_LOCATION_IMPLEMENTATION option to avoid
		// pre-registration
		// of the generated packages
		resource.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION_IMPLEMENTATION, Boolean.FALSE);
		return resource;
	}

} // ModelXMLProcessor
