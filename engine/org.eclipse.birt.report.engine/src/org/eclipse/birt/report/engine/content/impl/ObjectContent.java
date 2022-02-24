/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ObjectContent extends ImageContent {
	/*
	 * Attributes described in W3C recommendation. declare (declare) #IMPLIED --
	 * declare but don't instantiate flag -- classid %URI; #IMPLIED -- identifies an
	 * implementation -- codebase %URI; #IMPLIED -- base URI for classid, data,
	 * archive-- data %URI; #IMPLIED -- reference to object's data -- type
	 * %ContentType; #IMPLIED -- content type for data -- codetype %ContentType;
	 * #IMPLIED -- content type for code -- archive CDATA #IMPLIED --
	 * space-separated list of URIs -- standby %Text; #IMPLIED -- message to show
	 * while loading -- height %Length; #IMPLIED -- override height -- width
	 * %Length; #IMPLIED -- override width -- usemap %URI; #IMPLIED -- use
	 * client-side image map -- name CDATA #IMPLIED -- submit as part of form --
	 * tabindex NUMBER #IMPLIED -- position in tabbing order --
	 */

	private HashMap<String, String> params = new HashMap<String, String>();

	ObjectContent(ReportContent report) {
		super(report);
	}

	public void addParam(String name, String value) {
		if (null != name) {
			params.put(name, value);
		}
	}

	public HashMap<String, String> getParamters() {
		return params;
	}

	public String getParamValueByName(String name) {
		return params.get(name);
	}

	public void readContent(DataInputStream in, ClassLoader loader) throws IOException {
		throw new IOException("Unsupported operation: Object content can not be serialized");
	}

	public void writeContent(DataOutputStream out) throws IOException {
		throw new IOException("Unsupported operation: Object content can not be serialized");
	}
}
