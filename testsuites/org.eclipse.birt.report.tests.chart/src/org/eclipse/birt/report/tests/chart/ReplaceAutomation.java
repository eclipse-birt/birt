package org.eclipse.birt.report.tests.chart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceAutomation {
	/**
	 * package name which you want do relacing.
	 */
	private final static String pckgname = "/regression/";

	/**
	 * write directory.
	 */
	private final static String writeDir = "C:\\test\\";

	/**
	 * Change 'saveAs' to 'save' and change 'compareTextFile( XX , XX )' to
	 * 'compareTextFile( XX )'
	 * 
	 * @param pckgname page name such as '/script/','/parser/.
	 * @param writeDir write directory. such as 'c:\test\".
	 * @throws Exception
	 */
	private void getClasses() throws Exception {
		// Get a File object for the package
		File directory = null;

		String path = pckgname.replace('.', '/');

		String pkgFolder = getClassFolder();
		pkgFolder = "C:\\BirtCode\\org.eclipse.birt.report.tests.chart\\src\\org\\eclipse\\birt\\report\\tests\\chart";
		directory = new File(pkgFolder, path);

		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".java")) //$NON-NLS-1$
				{
					// read file content.

					String tmpFile = pkgFolder + path + files[i];
					String writeFile = writeDir + files[i];
					System.out.println("write file is " + writeFile);
					File tmpWriteFile = new File(writeDir);
					if (!tmpWriteFile.exists()) {
						tmpWriteFile.mkdir();
					}
					File file = new File(tmpFile);
					InputStream input = new FileInputStream(file);
					// read content in file

					int available = input.available();
					byte[] bytes = new byte[available];
					byte[] b = new byte[1024];
					int count = 0;
					int number = -1;
					while ((number = input.read(b)) != -1) {
						for (int s = 0; s < number; ++s) {
							bytes[count + s] = b[s];
						}
						count = count + number;
					}

					// replace string
					String result = replaceStr(new String(bytes));

					// write back to file
					File wFile = new File(writeFile);
					if (!wFile.exists()) {
						wFile.createNewFile();
					}
					OutputStream output = new FileOutputStream(writeFile);
					output.write(result.getBytes());
				}
			}
		}

	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder() {
		String pathBase = null;

		ProtectionDomain domain = this.getClass().getProtectionDomain();
		if (domain != null) {
			CodeSource source = domain.getCodeSource();
			if (source != null) {
				URL url = source.getLocation();
				pathBase = url.getPath();

				if (pathBase.endsWith("bin/")) //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 4);
				if (pathBase.endsWith("bin")) //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 3);
			}
		}

		pathBase = pathBase + "test/";
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf(".");
		className = className.substring(0, lastDotIndex);
		className = pathBase + className.replace('.', '/');

		return className;
	}

	public static void main(String[] args) {
		ReplaceAutomation replace = new ReplaceAutomation();
		try {
			replace.getClasses();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Replace text with regex expression.
	 * 
	 * @param str
	 * @return
	 */
	private static String replaceStr(String str) {

		String outputFolder = "[\\+][\\s]*OUTPUT_FOLDER[\\s]*[\\+][\\s]*OUTPUT";
		String classFolder = "getClassFolder[\\(][\\s]*[\\)]";

		String replaceString = "genOutputFile( OUTPUT )";

		Pattern pattern = Pattern.compile(classFolder);
		Matcher matcher = pattern.matcher(str);
		str = matcher.replaceAll(replaceString);

		pattern = Pattern.compile(outputFolder);
		matcher = pattern.matcher(str);
		str = matcher.replaceAll(" ");

		System.out.println(" result is " + str);

		return str;
	}

}
