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
public interface BidiConstants {

	public final static String ORDERING_SCHEME_VISUAL = Messages
			.getString("report.bidi.properties.externalbidiformat.orderingscheme.visual"); //$NON-NLS-1$
	public final static String ORDERING_SCHEME_LOGICAL = Messages
			.getString("report.bidi.properties.externalbidiformat.orderingscheme.logical"); //$NON-NLS-1$
	public final static String TEXT_DIRECTION_LTR = Messages
			.getString("report.bidi.properties.externalbidiformat.textdirection.ltr"); //$NON-NLS-1$
	public final static String TEXT_DIRECTION_RTL = Messages
			.getString("report.bidi.properties.externalbidiformat.textdirection.rtl"); //$NON-NLS-1$
	public final static String TEXT_DIRECTION_CONTEXTLTR = Messages
			.getString("report.bidi.properties.externalbidiformat.textdirection.contextltr"); //$NON-NLS-1$
	public final static String TEXT_DIRECTION_CONTEXTRTL = Messages
			.getString("report.bidi.properties.externalbidiformat.textdirection.contextrtl"); //$NON-NLS-1$
	public final static String SHAPING_SHAPED = Messages
			.getString("report.bidi.properties.externalbidiformat.shaping.shaped"); //$NON-NLS-1$
	public final static String SHAPING_NOMINAL = Messages
			.getString("report.bidi.properties.externalbidiformat.shaping.nominal"); //$NON-NLS-1$
	public final static String NUMSHAPING_NATIONAL = Messages
			.getString("report.bidi.properties.externalbidiformat.numshaping.national"); //$NON-NLS-1$
	public final static String NUMSHAPING_NOMINAL = Messages
			.getString("report.bidi.properties.externalbidiformat.numshaping.nominal"); //$NON-NLS-1$
	public final static String NUMSHAPING_CONTEXT = Messages
			.getString("report.bidi.properties.externalbidiformat.numshaping.contextual"); //$NON-NLS-1$

	public final static String ORDERING_SCHEME_TITLE_METADATA = Messages
			.getString("report.bidi.properties.externalbidiformat.orderingscheme.metadata"); //$NON-NLS-1$
	public final static String ORDERING_SCHEME_TITLE_CONTENT = Messages
			.getString("report.bidi.properties.externalbidiformat.orderingscheme.content"); //$NON-NLS-1$

	public final static String ORDERING_SCHEME_TOOLTIP = Messages
			.getString("report.bidi.properties.externalbidiformat.orderingscheme.tooltip"); //$NON-NLS-1$
	public final static int ORDERING_SCHEME_LOGICAL_INDX = 0;
	public final static int ORDERING_SCHEME_VISUAL_INDX = 1;

	public final static String TEXT_DIRECTION_TITLE_METADATA = Messages
			.getString("report.bidi.properties.externalbidiformat.textdirection.metadata"); //$NON-NLS-1$
	public final static String TEXT_DIRECTION_TITLE_CONTENT = Messages
			.getString("report.bidi.properties.externalbidiformat.textdirection.content"); //$NON-NLS-1$

	public final static String TEXT_DIRECTION_TOOLTIP = Messages
			.getString("report.bidi.properties.externalbidiformat.textdirection.tooltip"); //$NON-NLS-1$
	public final static int TEXT_DIRECTION_LTR_INDX = 0;
	public final static int TEXT_DIRECTION_RTL_INDX = 1;
	public final static int TEXT_DIRECTION_CONTEXTLTR_INDX = 2;
	public final static int TEXT_DIRECTION_CONTEXTRTL_INDX = 3;

	public final static String SYMSWAP_TITLE_METADATA = Messages
			.getString("report.bidi.properties.externalbidiformat.symswap.metadata"); //$NON-NLS-1$
	public final static String SYMSWAP_TITLE_CONTENT = Messages
			.getString("report.bidi.properties.externalbidiformat.symswap.content"); //$NON-NLS-1$

	public final static String SYMSWAP_TOOLTIP = Messages
			.getString("report.bidi.properties.externalbidiformat.symswap.tooltip"); //$NON-NLS-1$
	public final static String SYMSWAP_TRUE = Messages
			.getString("report.bidi.properties.externalbidiformat.symswap.true"); //$NON-NLS-1$
	public final static String SYMSWAP_FALSE = Messages
			.getString("report.bidi.properties.externalbidiformat.symswap.false"); //$NON-NLS-1$
	public final static int SYMSWAP_TRUE_INDX = 0;
	public final static int SYMSWAP_FALSE_INDX = 1;

	public final static String SHAPING_TITLE_METADATA = Messages
			.getString("report.bidi.properties.externalbidiformat.shaping.metadata"); //$NON-NLS-1$
	public final static String SHAPING_TITLE_CONTENT = Messages
			.getString("report.bidi.properties.externalbidiformat.shaping.content"); //$NON-NLS-1$

	public final static String SHAPING_TOOLTIP = Messages
			.getString("report.bidi.properties.externalbidiformat.shaping.tooltip"); //$NON-NLS-1$
	public final static int SHAPING_SHAPED_INDX = 0;
	public final static int SHAPING_NOMINAL_INDX = 1;

	public final static String NUMSHAPING_TITLE_METADATA = Messages
			.getString("report.bidi.properties.externalbidiformat.numshaping.metadata"); //$NON-NLS-1$
	public final static String NUMSHAPING_TITLE_CONTENT = Messages
			.getString("report.bidi.properties.externalbidiformat.numshaping.content"); //$NON-NLS-1$

	public final static String NUMSHAPING_TOOLTIP = Messages
			.getString("report.bidi.properties.externalbidiformat.numshaping.tooltip"); //$NON-NLS-1$
	public final static int NUMSHAPING_NOMINAL_INDX = 0;
	public final static int NUMSHAPING_NATIONAL_INDX = 1;
	public final static int NUMSHAPING_CONTEXT_INDX = 2;
	public static final String BIDI_FORMAT_ORIENTATION = "bidiFormatOrientation"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_ORDERINGSCHEME = "bidiFormatOrderingScheme"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_SYMSWAP = "bidiFormatSymSwap"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_TEXTSHAPING = "bidiFormatTextShaping"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_NUMSHAPING = "bidiFormatNumericShaping"; //$NON-NLS-1$

	public static final String BIDI_FORMAT_CONTENT_ORIENTATION = "bidiFormatContentOrientation"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_CONTENT_ORDERINGSCHEME = "bidiFormatContentOrderingScheme"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_CONTENT_SYMSWAP = "bidiFormatContentSymSwap"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_CONTENT_TEXTSHAPING = "bidiFormatContentTextShaping"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_CONTENT_NUMSHAPING = "bidiFormatContentNumericShaping"; //$NON-NLS-1$

	public static final String BIDI_FORMAT_METADATA_ORIENTATION = "bidiFormatMetadataOrientation"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_METADATA_ORDERINGSCHEME = "bidiFormatMetadataOrderingScheme"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_METADATA_SYMSWAP = "bidiFormatMetadataSymSwap"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_METADATA_TEXTSHAPING = "bidiFormatMetadataTextShaping"; //$NON-NLS-1$
	public static final String BIDI_FORMAT_METADATA_NUMSHAPING = "bidiFormatMetadataNumericShaping"; //$NON-NLS-1$
	// bidi_acgc added start
	public final static String ARABIC_TITLE = Messages.getString("preference.bidiframe.arabictitle"); //$NON-NLS-1$
	// bidi_acgc added end

	public final static String DEFAULT_BIDI_FORMAT_STR = "ILYNN"; //$NON-NLS-1$

	public static final String TRANSFORMATION_FUNCTION_NAME = "BidiUtils.bidiTransform"; //$NON-NLS-1$

	public static final String BIDI_DIRECTION_FOR_NONBIDI_REPORT = Messages
			.getString("report.bidi.properties.nonbididirection"); //$NON-NLS-1$
	public static final String EMPTY_STR = ""; //$NON-NLS-1$
	public static final String METADATA_FORMAT_PROP_NAME = "metadataBidiFormatStr"; //$NON-NLS-1$
	public static final String CONTENT_FORMAT_PROP_NAME = "contentBidiFormatStr"; //$NON-NLS-1$
	public static final String DISABLED_METADATA_FORMAT_PROP_NAME = "disabledMetadataBidiFormatStr"; //$NON-NLS-1$
	public static final String DISABLED_CONTENT_FORMAT_PROP_NAME = "disabledContentBidiFormatStr"; //$NON-NLS-1$
}
