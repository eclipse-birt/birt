/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.strategy.GroupPropSearchStrategy;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * The utility to recover listing/group properties if data binding reference
 * elements has different numbers of groups.
 * 
 */

public class RecoverDataGroupUtil
{

	/**
	 * @param listing
	 * @param tmpHandler
	 * 
	 */

	static void checkListingGroup( ListingElement listing,
			ModuleParserHandler tmpHandler )
	{
		ElementRefValue refValue = (ElementRefValue) listing.getLocalProperty(
				tmpHandler.module, IReportItemModel.DATA_BINDING_REF_PROP );

		assert refValue != null;

		tmpHandler.addUnresolveListingElement( listing );

		if ( !refValue.isResolved( ) )
		{
			return;
		}

		DesignElement targetElement = refValue.getElement( );
		if ( targetElement.getDefn( ) != listing.getDefn( ) )
			return;

		int elementGroupCount = listing.getGroups( ).size( );
		int targetGroupCount = ( (ListingElement) targetElement ).getGroups( )
				.size( );

		if ( elementGroupCount != targetGroupCount )
		{
			// throw exception and clears the data binding reference

			recoverListingElement( listing, (ListingElement) targetElement,
					tmpHandler );
			tmpHandler
					.getErrorHandler( )
					.semanticWarning(
							new SemanticError(
									listing,
									new String[]{listing.getIdentifier( ),
											targetElement.getIdentifier( )},
									SemanticError.DESIGN_EXCEPTION_INCONSISTENT_DATA_GROUP,
									SemanticError.WARNING ) );
		}
	}

	/**
	 * 
	 */

	private static void recoverListingElement( ListingElement listing,
			ListingElement targetElement, ModuleParserHandler tmpHandler )
	{
		recoverReferredReportItem( listing, targetElement, tmpHandler );
		List listingGroups = listing.getGroups( );
		List targetGroups = targetElement.getGroups( );

		int size = Math.min( listingGroups.size( ), targetGroups.size( ) );
		for ( int i = 0; i < size; i++ )
		{
			recoverReferredReportItem( (GroupElement) listingGroups.get( i ),
					(GroupElement) targetGroups.get( i ), tmpHandler );
		}

		listing.setProperty( IReportItemModel.DATA_BINDING_REF_PROP, null );
	}

	/**
	 * 
	 */

	private static void recoverReferredReportItem( DesignElement source,
			DesignElement targetElement, ModuleParserHandler tmpHandler )
	{
		List propDefns = targetElement.getDefn( ).getProperties( );
		for ( int i = 0; i < propDefns.size( ); i++ )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) propDefns
					.get( i );
			if ( propDefn.isStyleProperty( ) )
				continue;

			String propName = propDefn.getName( );
			if ( targetElement instanceof ListingElement )
			{
				if ( !ReportItemPropSearchStrategy
						.isDataBindingProperty( propName ) )
					continue;

			}
			else if ( targetElement instanceof GroupElement )
			{
				if ( !GroupPropSearchStrategy.isDataBindingProperty( propName ) )
					continue;
			}

			source.setProperty( propName, targetElement.getStrategy( )
					.getPropertyExceptRomDefault( tmpHandler.module,
							targetElement, propDefn ) );
		}
	}
}
