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
package org.eclipse.birt.data.engine.executor.cache;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.binding.FeaturesTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;


/**
 *
 */
public class CacheFeaturesTest extends FeaturesTest
{

	protected Map getAppContext()
	{
		Map appContext = new HashMap();
		appContext.put( "birt.data.engine.test.memcachesize", "20000" );
		return appContext;
	}
}
