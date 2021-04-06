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

package org.eclipse.birt.doc.romdoc;

public class RomDoc {
	/**
	 * 
	 * @param args using args[0] to specify the rom doc output folder, using args[1]
	 *             to specify the template doc folder.
	 */

	public static void main(String[] args) {
		Generator generator = new Generator();

		if (args.length > 0) {
			// Output folder is specified

			generator.setOutputDir(args[0]);

			if (args.length > 1)
				generator.setTemplateDir(args[1]);
		}

		try {
			generator.generate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
