package org.eclipse.birt.report.engine.api;

import java.util.Date;

import junit.framework.TestCase;

import com.ibm.icu.util.ULocale;

public class EngineExceptionTest extends TestCase {

	public void testLocalizedEngineException() {
		Date date = new Date();

		EngineException.setULocale(ULocale.CHINA);
		EngineException.setULocale(ULocale.CHINA);

		EngineException cnEx = new EngineException("date:{0}", date);

		System.out.println(cnEx.getLocalizedMessage());

		EngineException.setULocale(ULocale.ENGLISH);
		EngineException enEx = new EngineException("date:{0}", date);

		System.out.println(enEx.getLocalizedMessage());
	}

}
