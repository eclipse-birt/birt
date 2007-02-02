/**
 * <copyright>
 * </copyright>
 *
 * $Id: AttributeXMLProcessor.java,v 1.1 2006/12/28 03:49:27 anonymous Exp $
 */

package org.eclipse.birt.chart.model.attribute.util;

import java.util.Map;

import org.eclipse.birt.chart.model.attribute.AttributePackage;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AttributeXMLProcessor extends XMLProcessor
{

	/**
	 * Public constructor to instantiate the helper.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeXMLProcessor( )
	{
		super( ( EPackage.Registry.INSTANCE ) );
		AttributePackage.eINSTANCE.eClass( );
	}

	/**
	 * Register for "*" and "xml" file extensions the AttributeResourceFactoryImpl factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Map getRegistrations( )
	{
		if ( registrations == null )
		{
			super.getRegistrations( );
			registrations.put( XML_EXTENSION,
					new AttributeResourceFactoryImpl( ) );
			registrations.put( STAR_EXTENSION,
					new AttributeResourceFactoryImpl( ) );
		}
		return registrations;
	}

} //AttributeXMLProcessor
