/***********************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.bidi.utils.ui;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * @author bidi_hcg
 * 
 */
public class BidiGUIUtility
{
	private Label orderingSchemeLabel, textDirectionLabel, symSwapLabel,
			shapingLabel, numShapingLabel;
	private Combo orderingSchemeCombo, textDirectionCombo, symSwapCombo,
			shapingCombo, numShapingCombo;

	public BidiGUIUtility( )
	{
	}

	public static BidiGUIUtility INSTANCE = new BidiGUIUtility( );

	public Group addBiDiFormatFrame( Composite mainComposite,
			String biDiFormatFrameTitle, BidiFormat bidiFormat )
	{

		Group externalBiDiFormatFrame = new Group( mainComposite, SWT.NONE );
		externalBiDiFormatFrame.setLayout( new GridLayout( ) );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 2;
		data.verticalSpan = 5;
		data.verticalIndent = 5;
		externalBiDiFormatFrame.setText( biDiFormatFrameTitle );
		externalBiDiFormatFrame.setLayoutData( data );

		externalBiDiFormatFrame.setEnabled( true );

		GridData innerFrameGridData = new GridData( GridData.FILL_HORIZONTAL );
		innerFrameGridData.grabExcessHorizontalSpace = true;
		innerFrameGridData.horizontalSpan = 1;
		innerFrameGridData.horizontalAlignment = GridData.FILL;
		innerFrameGridData.verticalIndent = 5;
		innerFrameGridData.minimumWidth = SWT.DEFAULT;

		GridLayout innerFrameLayout = new GridLayout( );
		innerFrameLayout.numColumns = 2;
		innerFrameLayout.marginWidth = 5;
		innerFrameLayout.marginHeight = 10;

		externalBiDiFormatFrame.setLayout( innerFrameLayout );

		orderingSchemeLabel = new Label( externalBiDiFormatFrame, SWT.NONE );
		orderingSchemeLabel.setText( BidiConstants.ORDERING_SCHEME_TITLE );
		orderingSchemeLabel.setLayoutData( innerFrameGridData );
		orderingSchemeCombo = new Combo( externalBiDiFormatFrame, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		orderingSchemeCombo
				.setToolTipText( BidiConstants.ORDERING_SCHEME_TOOLTIP );
		orderingSchemeCombo.add( BidiConstants.ORDERING_SCHEME_LOGICAL,
				BidiConstants.ORDERING_SCHEME_LOGICAL_INDX );
		orderingSchemeCombo.add( BidiConstants.ORDERING_SCHEME_VISUAL,
				BidiConstants.ORDERING_SCHEME_VISUAL_INDX );
		orderingSchemeCombo.select( getOrderingSchemeComboIndx( bidiFormat
				.getOrderingScheme( ) ) );
		orderingSchemeCombo.setLayoutData( innerFrameGridData );

		textDirectionLabel = new Label( externalBiDiFormatFrame, SWT.NONE );
		textDirectionLabel.setText( BidiConstants.TEXT_DIRECTION_TITLE );
		textDirectionLabel.setLayoutData( innerFrameGridData );
		textDirectionCombo = new Combo( externalBiDiFormatFrame, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		textDirectionCombo
				.setToolTipText( BidiConstants.TEXT_DIRECTION_TOOLTIP );
		textDirectionCombo.add( BidiConstants.TEXT_DIRECTION_LTR,
				BidiConstants.TEXT_DIRECTION_LTR_INDX );
		textDirectionCombo.add( BidiConstants.TEXT_DIRECTION_RTL,
				BidiConstants.TEXT_DIRECTION_RTL_INDX );
		textDirectionCombo.add( BidiConstants.TEXT_DIRECTION_CONTEXTLTR,
				BidiConstants.TEXT_DIRECTION_CONTEXTLTR_INDX );
		textDirectionCombo.add( BidiConstants.TEXT_DIRECTION_CONTEXTRTL,
				BidiConstants.TEXT_DIRECTION_CONTEXTRTL_INDX );
		textDirectionCombo.select( getTextDirectionComboIndx( bidiFormat
				.getTextDirection( ) ) );
		textDirectionCombo.setLayoutData( innerFrameGridData );

		symSwapLabel = new Label( externalBiDiFormatFrame, SWT.NONE );
		symSwapLabel.setText( BidiConstants.SYMSWAP_TITLE );
		symSwapLabel.setLayoutData( innerFrameGridData );

		symSwapCombo = new Combo( externalBiDiFormatFrame, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		symSwapCombo.setToolTipText( BidiConstants.SYMSWAP_TOOLTIP );
		symSwapCombo.add( BidiConstants.SYMSWAP_TRUE,
				BidiConstants.SYMSWAP_TRUE_INDX );
		symSwapCombo.add( BidiConstants.SYMSWAP_FALSE,
				BidiConstants.SYMSWAP_FALSE_INDX );
		if ( bidiFormat.getSymSwap( ) )
		{
			symSwapCombo.select( BidiConstants.SYMSWAP_TRUE_INDX );
		}
		else
		{
			symSwapCombo.select( BidiConstants.SYMSWAP_FALSE_INDX );
		}
		symSwapCombo.setLayoutData( innerFrameGridData );

		shapingLabel = new Label( externalBiDiFormatFrame, SWT.NONE );
		shapingLabel.setText( BidiConstants.SHAPING_TITLE );
		shapingLabel.setLayoutData( innerFrameGridData );

		shapingCombo = new Combo( externalBiDiFormatFrame, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		shapingCombo.setToolTipText( BidiConstants.SHAPING_TOOLTIP );
		shapingCombo.add( BidiConstants.SHAPING_SHAPED,
				BidiConstants.SHAPING_SHAPED_INDX );
		shapingCombo.add( BidiConstants.SHAPING_NOMINAL,
				BidiConstants.SHAPING_NOMINAL_INDX );
		shapingCombo
				.select( getShapingComboIndx( bidiFormat.getTextShaping( ) ) );
		shapingCombo.setLayoutData( innerFrameGridData );

		numShapingLabel = new Label( externalBiDiFormatFrame, SWT.NONE );
		numShapingLabel.setText( BidiConstants.NUMSHAPING_TITLE );
		numShapingLabel.setLayoutData( innerFrameGridData );
		numShapingCombo = new Combo( externalBiDiFormatFrame, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		numShapingCombo.setToolTipText( BidiConstants.NUMSHAPING_TOOLTIP );
		numShapingCombo.add( BidiConstants.NUMSHAPING_NOMINAL,
				BidiConstants.NUMSHAPING_NOMINAL_INDX );
		numShapingCombo.add( BidiConstants.NUMSHAPING_NATIONAL,
				BidiConstants.NUMSHAPING_NATIONAL_INDX );
		numShapingCombo.add( BidiConstants.NUMSHAPING_CONTEXT,
				BidiConstants.NUMSHAPING_CONTEXT_INDX );
		numShapingCombo.select( getNumShapingComboIndx( bidiFormat
				.getNumeralShaping( ) ) );
		numShapingCombo.setLayoutData( innerFrameGridData );
		numShapingLabel.setEnabled( numShapingCombo.isEnabled( ) );

		return externalBiDiFormatFrame;
	}

	public static int getOrderingSchemeComboIndx( String orderingScheme )
	{
		if ( orderingScheme.equals( BidiConstants.ORDERING_SCHEME_LOGICAL ) )
			return BidiConstants.ORDERING_SCHEME_LOGICAL_INDX;
		return BidiConstants.ORDERING_SCHEME_VISUAL_INDX;
	}

	public static int getTextDirectionComboIndx( String textDirection )
	{
		if ( textDirection.equals( BidiConstants.TEXT_DIRECTION_LTR ) )
			return BidiConstants.TEXT_DIRECTION_LTR_INDX;
		if ( textDirection.equals( BidiConstants.TEXT_DIRECTION_RTL ) )
			return BidiConstants.TEXT_DIRECTION_RTL_INDX;
		if ( textDirection.equals( BidiConstants.TEXT_DIRECTION_CONTEXTLTR ) )
			return BidiConstants.TEXT_DIRECTION_CONTEXTLTR_INDX;
		return BidiConstants.TEXT_DIRECTION_CONTEXTRTL_INDX;
	}

	public static int getShapingComboIndx( String textShaping )
	{
		if ( textShaping.equals( BidiConstants.SHAPING_NOMINAL ) )
			return BidiConstants.SHAPING_NOMINAL_INDX;
		return BidiConstants.SHAPING_SHAPED_INDX;
	}

	public static int getNumShapingComboIndx( String numShaping )
	{
		if ( numShaping.equals( BidiConstants.NUMSHAPING_CONTEXT ) )
			return BidiConstants.NUMSHAPING_CONTEXT_INDX;
		if ( numShaping.equals( BidiConstants.NUMSHAPING_NATIONAL ) )
			return BidiConstants.NUMSHAPING_NATIONAL_INDX;
		return BidiConstants.NUMSHAPING_NOMINAL_INDX;
	}

	public BidiFormat getBiDiFormat( Group bidiFormatFrame )
	{
		String orderingScheme;
		String textDirection;
		String numeralShaping;
		String textShaping;
		boolean symSwap;

		Control[] controls = bidiFormatFrame.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			if ( controls[i] instanceof Combo )
			{
				if ( BidiConstants.ORDERING_SCHEME_TOOLTIP
						.equals( ( (Combo) controls[i] ).getToolTipText( ) ) )
					orderingSchemeCombo = (Combo) controls[i];
				else if ( BidiConstants.TEXT_DIRECTION_TOOLTIP
						.equals( ( (Combo) controls[i] ).getToolTipText( ) ) )
					textDirectionCombo = (Combo) controls[i];
				else if ( BidiConstants.SHAPING_TOOLTIP
						.equals( ( (Combo) controls[i] ).getToolTipText( ) ) )
					shapingCombo = (Combo) controls[i];
				else if ( BidiConstants.NUMSHAPING_TOOLTIP
						.equals( ( (Combo) controls[i] ).getToolTipText( ) ) )
					numShapingCombo = (Combo) controls[i];
				else if ( BidiConstants.SYMSWAP_TOOLTIP
						.equals( ( (Combo) controls[i] ).getToolTipText( ) ) )
					symSwapCombo = (Combo) controls[i];
			}
		}// end for loop

		switch ( orderingSchemeCombo.getSelectionIndex( ) )
		{
		case BidiConstants.ORDERING_SCHEME_LOGICAL_INDX:
			orderingScheme = BidiConstants.ORDERING_SCHEME_LOGICAL;
			break;
		case BidiConstants.ORDERING_SCHEME_VISUAL_INDX:
			orderingScheme = BidiConstants.ORDERING_SCHEME_VISUAL;
			break;
		default:
			orderingScheme = ""; // shouldn't happen
		}
		switch ( textDirectionCombo.getSelectionIndex( ) )
		{
		case BidiConstants.TEXT_DIRECTION_LTR_INDX:
			textDirection = BidiConstants.TEXT_DIRECTION_LTR;
			break;
		case BidiConstants.TEXT_DIRECTION_RTL_INDX:
			textDirection = BidiConstants.TEXT_DIRECTION_RTL;
			break;
		case BidiConstants.TEXT_DIRECTION_CONTEXTLTR_INDX:
			textDirection = BidiConstants.TEXT_DIRECTION_CONTEXTLTR;
			break;
		case BidiConstants.TEXT_DIRECTION_CONTEXTRTL_INDX:
			textDirection = BidiConstants.TEXT_DIRECTION_CONTEXTRTL;
			break;
		default:
			textDirection = ""; // shouldn't happen
		}

		symSwap = ( symSwapCombo.getSelectionIndex( ) == BidiConstants.SYMSWAP_TRUE_INDX );

		switch ( shapingCombo.getSelectionIndex( ) )
		{
		case BidiConstants.SHAPING_NOMINAL_INDX:
			textShaping = BidiConstants.SHAPING_NOMINAL;
			break;
		case BidiConstants.SHAPING_SHAPED_INDX:
			textShaping = BidiConstants.SHAPING_SHAPED;
			break;
		default:
			textShaping = ""; // shouldn't happen
			break;
		}

		switch ( numShapingCombo.getSelectionIndex( ) )
		{
		case BidiConstants.NUMSHAPING_NOMINAL_INDX:
			numeralShaping = BidiConstants.NUMSHAPING_NOMINAL;
			break;
		case BidiConstants.NUMSHAPING_NATIONAL_INDX:
			numeralShaping = BidiConstants.NUMSHAPING_NATIONAL;
			break;
		case BidiConstants.NUMSHAPING_CONTEXT_INDX:
			numeralShaping = BidiConstants.NUMSHAPING_CONTEXT;
			break;
		default:
			numeralShaping = "";// shouldn't happen
			break;
		}
		return new BidiFormat( orderingScheme, textDirection, symSwap,
				textShaping, numeralShaping );
	}

	public void performDefaults( )
	{
		orderingSchemeCombo.select( BidiConstants.ORDERING_SCHEME_LOGICAL_INDX );
		textDirectionCombo.select( BidiConstants.TEXT_DIRECTION_LTR_INDX );
		symSwapCombo.select( BidiConstants.SYMSWAP_TRUE_INDX );
		shapingCombo.select( BidiConstants.SHAPING_NOMINAL_INDX );
		numShapingCombo.select( BidiConstants.NUMSHAPING_NOMINAL_INDX );
	}

}
