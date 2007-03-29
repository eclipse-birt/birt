/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelResourceFactoryImpl.java,v 1.1 2006/08/10 03:33:06 rlu Exp $
 */

package org.eclipse.birt.report.model.adapter.oda.model.util;

import org.eclipse.birt.report.model.adapter.oda.model.ModelPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * <!-- begin-user-doc --> The <b>Resource Factory</b> associated with the
 * package. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.report.model.adapter.oda.model.util.ModelResourceImpl
 * @generated
 */
public class ModelResourceFactoryImpl extends ResourceFactoryImpl
{

	/**
	 * Creates an instance of the resource factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelResourceFactoryImpl( )
	{
		super( );

		ExtendedMetaData extendedMetaData = ExtendedMetaData.INSTANCE;
		extendedMetaData.putPackage( ModelPackage.eNS_URI,
				ModelPackage.eINSTANCE );
	}

	/**
	 * Creates an instance of the resource. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public Resource createResource( URI uri )
	{
		XMLResource result = new ModelResourceImpl( uri );
		result.getDefaultSaveOptions( ).put(
				XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE );
		result.getDefaultLoadOptions( ).put(
				XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE );

		result.getDefaultSaveOptions( ).put(
				XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE );

		result.getDefaultLoadOptions( ).put(
				XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE );
		result.getDefaultSaveOptions( ).put(
				XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE );

		result.getDefaultLoadOptions( ).put(
				XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE );
		return result;
	}

} // ModelResourceFactoryImpl
