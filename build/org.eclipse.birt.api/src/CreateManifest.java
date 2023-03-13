
/*******************************************************************************
 * Copyright (c) 2021 Remain Software and others. 
 * 
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Wim Jongman - Java version of API creation
 ******************************************************************************/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class CreateManifest {

	// for all jars in ./lib folder, get all packages
	// sort the package and append to manifest.mf

	static boolean isClass(String fileName) {
		return fileName.endsWith(".class");
	}

	static String getPackageName(String fileName) {
		if (isClass(fileName)) {
			// java.lang.System.out.println(fileName);
			int last = fileName.lastIndexOf('/');
			if (last != -1) {
				String pkg = fileName.substring(0, last).replaceAll("/", ".");
				System.out.println(pkg);
				return pkg;
			}
		}
		return null;
	}

	static String getJarVersion(JarFile jarFile) throws IOException {
		Manifest manifest = jarFile.getManifest();
		if (manifest != null) {
			Attributes attrs = manifest.getMainAttributes();
			if (attrs != null) {
				String version = attrs.getValue("Bundle-Version");
				if (version != null && version.matches("\\d+\\.\\d+\\.\\d+\\..*")) {
					int last = version.lastIndexOf('.');
					if (last != -1) {
						return version.substring(0, last);
					}
					return version;
				}
			}
		}
		return null;
	}

	static HashSet<String> loadPackages(String zipFileName) throws IOException {
		HashSet<String> packages = new java.util.HashSet<String>();
		JarFile zipFile = new java.util.jar.JarFile(zipFileName);
		String version = getJarVersion(zipFile);
		java.lang.System.out.println(zipFileName + ":" + version);
		Enumeration<JarEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (!entry.isDirectory()) {
				String pkgName = getPackageName(entry.getName());
				if (pkgName != null) {
					if (version != null && !pkgName.startsWith("javax.")) {
						packages.add(pkgName + ";version=\"" + version + "\"");
					} else {
						packages.add(pkgName);
					}
				}
			}
		}
		return packages;
	}

	static ArrayList<String> listJarFiles(String libFolder) {
		ArrayList<String> jarFiles = new java.util.ArrayList<String>();
		File folder = new java.io.File(libFolder);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile() && files[i].getName().endsWith(".jar")) {
				jarFiles.add(files[i].getAbsolutePath());
			}
		}
		return jarFiles;
	}

	static ArrayList<String> loadApiPackages(String libFolder) throws IOException {
		HashSet<String> packages = new java.util.HashSet<String>();
		ArrayList<String> jarFiles = listJarFiles(libFolder);
		for (int i = 0; i < jarFiles.size(); i++) {
			packages.addAll(loadPackages(jarFiles.get(i)));
		}
		ArrayList<String> sortedPackages = new java.util.ArrayList<String>();
		sortedPackages.addAll(packages);
		java.util.Collections.sort(sortedPackages);
		return sortedPackages;
	}

	static void copyFile(String source, String target) throws IOException {
		byte[] buffer = new byte[1024];
		FileInputStream input = new java.io.FileInputStream(source);
		FileOutputStream out = new java.io.FileOutputStream(target);
		int size = input.read(buffer);
		while (size > 0) {
			out.write(buffer, 0, size);
			size = input.read(buffer);
		}
		out.close();
		input.close();
	}

	static void createManifest(String source, String target, List<String> packages) throws IOException {
		StringBuilder sb = new java.lang.StringBuilder("Export-Package:");
		for (int i = 0; i < packages.size(); i++) {
			sb.append(' ');
			sb.append(packages.get(i));
			sb.append(',');
			sb.append(System.lineSeparator());
		}
		sb.setLength(sb.length() - 2);
		sb.append(System.lineSeparator());
		copyFile(source, target);
		PrintWriter manifest = new java.io.PrintWriter(
				new java.io.BufferedWriter(new java.io.FileWriter(target, true)));
		manifest.print(sb.toString());
		manifest.close();
	}

	public static void main(String[] args) throws IOException {

		String libFolder = System.getProperty("LIB_DIR");
		String template = System.getProperty("MANIFEST_TEMPLATE");
		String manifest = System.getProperty("MANIFEST_MF");

//		String libFolder = "C:\\Users\\jongw\\git\\birt\\build\\org.eclipse.birt.api\\target\\lib";
//		String template = "C:\\Users\\jongw\\git\\birt\\build\\org.eclipse.birt.api\\MANIFEST.MF.test.in";
//		String manifest = "C:\\Users\\jongw\\git\\birt\\build\\org.eclipse.birt.api\\MANIFEST.MF.test.out";

		System.out.println("load JAR from " + libFolder + " create MANIFEST to " + manifest);

		ArrayList<String> packages = loadApiPackages(libFolder);
		if (packages.isEmpty()) {
			System.err.println("failed to load API packages from " + libFolder);
		}

		createManifest(template, manifest, packages);
	}

}
