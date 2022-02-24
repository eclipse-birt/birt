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

package org.eclipse.birt.chart.model.util;

import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * <!-- begin-user-doc --> The <b>Resource Factory </b> associated with the
 * package. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.util.ModelResourceImpl
 */
public class ModelResourceFactoryImpl extends ResourceFactoryImpl {

	static {
		ExtendedMetaData extendedMetaData = ExtendedMetaData.INSTANCE;
		extendedMetaData.putPackage(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		extendedMetaData.putPackage(AttributePackage.eNS_URI, AttributePackage.eINSTANCE);
		extendedMetaData.putPackage(ComponentPackage.eNS_URI, ComponentPackage.eINSTANCE);
		extendedMetaData.putPackage(DataPackage.eNS_URI, DataPackage.eINSTANCE);
		extendedMetaData.putPackage(LayoutPackage.eNS_URI, LayoutPackage.eINSTANCE);
		extendedMetaData.putPackage(TypePackage.eNS_URI, TypePackage.eINSTANCE);
	}

	/**
	 * Creates an instance of the resource factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 */
	public ModelResourceFactoryImpl() {
		super();
	}

	/**
	 * Creates an instance of the resource. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Resource createResource(URI uri) {
		XMLResource result = new ModelResourceImpl(uri);
		result.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
		result.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);

		result.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

		result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
		result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

		result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
		return result;
	}

} // ModelResourceFactoryImpl
