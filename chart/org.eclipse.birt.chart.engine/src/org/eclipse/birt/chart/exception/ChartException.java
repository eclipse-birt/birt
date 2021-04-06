/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.exception;

import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Exception class for Chart framework.
 */
public class ChartException extends BirtException {

	private static final long serialVersionUID = 1L;

	private static final int MAX_VALUE = 30;

	/**
	 * Exception type for Data format error.
	 */
	public static final int DATA_FORMAT = 0;
	/**
	 * Exception type for Data set error.
	 */
	public static final int DATA_SET = 1;
	/**
	 * Exception type for Does not fit error.
	 */
	public static final int DOES_NOT_FIT = 2;
	/**
	 * Exception type for chart Generation error.
	 */
	public static final int GENERATION = 3;
	/**
	 * Exception type for Image loading error.
	 */
	public static final int IMAGE_LOADING = 4;
	/**
	 * Exception type for Missing stacked entry error.
	 */
	public static final int MISSING_STACKED_ENTRY = 5;
	/**
	 * Exception type for Not found error.
	 */
	public static final int NOT_FOUND = 6;
	/**
	 * Exception type for Out of range error.
	 */
	public static final int OUT_OF_RANGE = 7;
	/**
	 * Exception type for Out of SYnc error.
	 */
	public static final int OUT_OF_SYNC = 8;
	/**
	 * Exception type for Overlap error.
	 */
	public static final int OVERLAP = 9;
	/**
	 * Exception type for PlugIn error.
	 */
	public static final int PLUGIN = 10;
	/**
	 * Exception type for Rendering error.
	 */
	public static final int RENDERING = 11;
	/**
	 * Exception type for Script error.
	 */
	public static final int SCRIPT = 12;
	/**
	 * Exception type for Undefined value error.
	 */
	public static final int UNDEFINED_VALUE = 13;
	/**
	 * Exception type for Unsupported feature error.
	 */
	public static final int UNSUPPORTED_FEATURE = 14;
	/**
	 * Exception type for Computation error.
	 */
	public static final int COMPUTATION = 15;
	/**
	 * Exception type for ZeroDataset error.
	 */
	public static final int ZERO_DATASET = 16;
	/**
	 * Exception type for NullDataset error.
	 */
	public static final int NULL_DATASET = 17;
	/**
	 * Exception type for InvalidImageSize error.
	 */
	public static final int INVALID_IMAGE_SIZE = 18;
	/**
	 * Exception type for Data binding error
	 */
	public static final int DATA_BINDING = 19;
	/**
	 * Exception type for all null values error.
	 */
	public static final int ALL_NULL_DATASET = 20;

	public static final int INVALID_DATA_TYPE = 21;
	/**
	 * Exception type for Validation error.
	 */
	public static final int VALIDATION = MAX_VALUE;

	final int type;

	/**
	 * The constructor.
	 * 
	 * @deprecated use {@link #ChartException(String, int, Throwable)} instead.
	 */
	public ChartException(final int type, Throwable cause) {
		super(getResourceKey(cause), getArguments(cause), getResourceBundle(cause), cause);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 * 
	 * @deprecated use {@link #ChartException(String, int, String)} instead.
	 */
	public ChartException(final int type, String errorMsg) {
		super(errorMsg, null);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 * 
	 * @deprecated use {@link #ChartException(String, int, String, ResourceBundle)}
	 *             instead.
	 */
	public ChartException(final int type, String sResourceKey, ResourceBundle rb) {
		super(sResourceKey, rb);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 * 
	 * @deprecated use
	 *             {@link #ChartException(String, int, String, Object[], ResourceBundle)}
	 *             instead.
	 */
	public ChartException(final int type, String sResourceKey, Object[] oaArgs, ResourceBundle rb) {
		super(sResourceKey, oaArgs, rb);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 * 
	 * @deprecated use
	 *             {@link #ChartException(String, int, String, ResourceBundle, Throwable)}
	 *             instead.
	 */
	public ChartException(final int type, String sResourceKey, ResourceBundle rb, Throwable thCause) {
		super(sResourceKey, rb);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 * 
	 * @deprecated use
	 *             ({@link #ChartException(String, int, String, Object[], ResourceBundle, Throwable)}
	 *             instead.
	 */
	public ChartException(final int type, String sResourceKey, Object[] oaArgs, ResourceBundle rb, Throwable thCause) {
		super(sResourceKey, oaArgs, rb, thCause);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 */
	public ChartException(String pluginId, final int type, Throwable cause) {
		super(pluginId, getResourceKey(cause), getArguments(cause), getResourceBundle(cause), cause);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 */
	public ChartException(String pluginId, final int type, String errorMsg) {
		super(pluginId, errorMsg, null);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 */
	public ChartException(String pluginId, final int type, String sResourceKey, ResourceBundle rb) {
		super(pluginId, sResourceKey, rb);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 */
	public ChartException(String pluginId, final int type, String sResourceKey, Object[] oaArgs, ResourceBundle rb) {
		super(pluginId, sResourceKey, oaArgs, rb);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 */
	public ChartException(String pluginId, final int type, String sResourceKey, ResourceBundle rb, Throwable thCause) {
		super(pluginId, sResourceKey, rb);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * The constructor.
	 */
	public ChartException(String pluginId, final int type, String sResourceKey, Object[] oaArgs, ResourceBundle rb,
			Throwable thCause) {
		super(pluginId, sResourceKey, oaArgs, rb, thCause);

		if (type < 0 || type > MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	private static String getResourceKey(Throwable cause) {
		if (cause instanceof ChartException) {
			return ((ChartException) cause).sResourceKey;
		} else {
			return cause.getLocalizedMessage();
		}
	}

	private static ResourceBundle getResourceBundle(Throwable cause) {
		if (cause instanceof ChartException) {
			return ((ChartException) cause).rb;
		} else {
			return null;
		}
	}

	private static Object[] getArguments(Throwable cause) {
		if (cause instanceof ChartException) {
			return ((ChartException) cause).oaMessageArguments;
		} else {
			return null;
		}
	}

	/**
	 * Returns the type of this Chart exception.
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}
}