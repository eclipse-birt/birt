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

package org.eclipse.birt.report.data.bidi.utils.core;

import org.eclipse.birt.report.data.bidi.utils.i18n.Messages;


/**
 * @author fira@il.ibm.com (bidi_hcg)
 * 
 */
public interface BidiConstants
{
	public final static String ORDERING_SCHEME_VISUAL = Messages
			.getString( "report.bidi.properties.externalbidiformat.orderingscheme.visual" );
	public final static String ORDERING_SCHEME_LOGICAL = Messages
			.getString( "report.bidi.properties.externalbidiformat.orderingscheme.logical" );
	public final static String TEXT_DIRECTION_LTR = Messages
			.getString( "report.bidi.properties.externalbidiformat.textdirection.ltr" );
	public final static String TEXT_DIRECTION_RTL = Messages
			.getString( "report.bidi.properties.externalbidiformat.textdirection.rtl" );
	public final static String TEXT_DIRECTION_CONTEXTLTR = Messages
			.getString( "report.bidi.properties.externalbidiformat.textdirection.contextltr" );
	public final static String TEXT_DIRECTION_CONTEXTRTL = Messages
			.getString( "report.bidi.properties.externalbidiformat.textdirection.contextrtl" );
	public final static String SHAPING_SHAPED = Messages
			.getString( "report.bidi.properties.externalbidiformat.shaping.shaped" );
	public final static String SHAPING_NOMINAL = Messages
			.getString( "report.bidi.properties.externalbidiformat.shaping.nominal" );
	public final static String NUMSHAPING_NATIONAL = Messages
			.getString( "report.bidi.properties.externalbidiformat.numshaping.national" );
	public final static String NUMSHAPING_NOMINAL = Messages
			.getString( "report.bidi.properties.externalbidiformat.numshaping.nominal" );
	public final static String NUMSHAPING_CONTEXT = Messages
			.getString( "report.bidi.properties.externalbidiformat.numshaping.contextual" );
	public final static String ORDERING_SCHEME_TITLE = Messages
			.getString( "report.bidi.properties.externalbidiformat.orderingscheme" );
	public final static String ORDERING_SCHEME_TOOLTIP = Messages
			.getString( "report.bidi.properties.externalbidiformat.orderingscheme.tooltip" );
	public final static int ORDERING_SCHEME_LOGICAL_INDX = 0;
	public final static int ORDERING_SCHEME_VISUAL_INDX = 1;
	public final static String TEXT_DIRECTION_TITLE = Messages
			.getString( "report.bidi.properties.externalbidiformat.textdirection" );
	public final static String TEXT_DIRECTION_TOOLTIP = Messages
			.getString( "report.bidi.properties.externalbidiformat.textdirection.tooltip" );
	public final static int TEXT_DIRECTION_LTR_INDX = 0;
	public final static int TEXT_DIRECTION_RTL_INDX = 1;
	public final static int TEXT_DIRECTION_CONTEXTLTR_INDX = 2;
	public final static int TEXT_DIRECTION_CONTEXTRTL_INDX = 3;
	public final static String SYMSWAP_TITLE = Messages
			.getString( "report.bidi.properties.externalbidiformat.symswap" );
	public final static String SYMSWAP_TOOLTIP = Messages
			.getString( "report.bidi.properties.externalbidiformat.symswap.tooltip" );
	public final static String SYMSWAP_TRUE = Messages
			.getString( "report.bidi.properties.externalbidiformat.symswap.true" );
	public final static String SYMSWAP_FALSE = Messages
			.getString( "report.bidi.properties.externalbidiformat.symswap.false" );
	public final static int SYMSWAP_TRUE_INDX = 0;
	public final static int SYMSWAP_FALSE_INDX = 1;
	public final static String SHAPING_TITLE = Messages
			.getString( "report.bidi.properties.externalbidiformat.shaping" );
	public final static String SHAPING_TOOLTIP = Messages
			.getString( "report.bidi.properties.externalbidiformat.shaping.tooltip" );
	public final static int SHAPING_SHAPED_INDX = 0;
	public final static int SHAPING_NOMINAL_INDX = 1;
	public final static String NUMSHAPING_TITLE = Messages
			.getString( "report.bidi.properties.externalbidiformat.numshaping" );
	public final static String NUMSHAPING_TOOLTIP = Messages
			.getString( "report.bidi.properties.externalbidiformat.numshaping.tooltip" );
	public final static int NUMSHAPING_NOMINAL_INDX = 0;
	public final static int NUMSHAPING_NATIONAL_INDX = 1;
	public final static int NUMSHAPING_CONTEXT_INDX = 2;
	public static final String BIDI_FORMAT_ORIENTATION = "bidiFormatOrientation";
	public static final String BIDI_FORMAT_ORDERINGSCHEME = "bidiFormatOrderingScheme";
	public static final String BIDI_FORMAT_SYMSWAP = "bidiFormatSymSwap";
	public static final String BIDI_FORMAT_TEXTSHAPING = "bidiFormatTextShaping";
	public static final String BIDI_FORMAT_NUMSHAPING = "bidiFormatNumericShaping";

	public static final String BIDI_FORMAT_CONTENT_ORIENTATION = "bidiFormatContentOrientation";
	public static final String BIDI_FORMAT_CONTENT_ORDERINGSCHEME = "bidiFormatContentOrderingScheme";
	public static final String BIDI_FORMAT_CONTENT_SYMSWAP = "bidiFormatContentSymSwap";
	public static final String BIDI_FORMAT_CONTENT_TEXTSHAPING = "bidiFormatContentTextShaping";
	public static final String BIDI_FORMAT_CONTENT_NUMSHAPING = "bidiFormatContentNumericShaping";

	public static final String BIDI_FORMAT_METADATA_ORIENTATION = "bidiFormatMetadataOrientation";
	public static final String BIDI_FORMAT_METADATA_ORDERINGSCHEME = "bidiFormatMetadataOrderingScheme";
	public static final String BIDI_FORMAT_METADATA_SYMSWAP = "bidiFormatMetadataSymSwap";
	public static final String BIDI_FORMAT_METADATA_TEXTSHAPING = "bidiFormatMetadataTextShaping";
	public static final String BIDI_FORMAT_METADATA_NUMSHAPING = "bidiFormatMetadataNumericShaping";

	public final static String DEFAULT_BIDI_FORMAT_STR = "ILYNN";

	public static final String TRANSFORMATION_FUNCTION_NAME = "BidiUtils.bidiTransform";

	public static final String BIDI_DIRECTION_FOR_NONBIDI_REPORT = Messages
			.getString( "report.bidi.properties.nonbididirection" );

	public static final String METADATA_FORMAT_PROP_NAME = "metadataBidiFormatStr";
	public static final String CONTENT_FORMAT_PROP_NAME = "contentBidiFormatStr";
}
