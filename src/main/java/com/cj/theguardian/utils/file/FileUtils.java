package com.cj.theguardian.utils.file;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.cj.theguardian.utils.exception.UtilityException;

import mslinks.ShellLink;

public class FileUtils {

	public static String buildFilePath(String... paths) {
		// assume its a relative file path in most cases
		return buildFilePath(false, paths);
	}


	public static String convertToWindowsPath(String fullPath) {
		String path = fullPath;
		System.out.println(SystemUtils.OS_NAME);
		if (SystemUtils.IS_OS_WINDOWS && fullPath.startsWith("/")) {
			String drive = fullPath.substring(1,2);
			 path = drive.toUpperCase()+":\\"+fullPath.substring(3);
		}
		return path.replace("/","\\");
	}


	/**
	 * if OS is windows, include root includes :\ the drive you want to use as root
	 * must be appended on return eg if the parameters are (true, Documents, Work,
	 * Paystubs), this method will return :\Documents\Work\Paystubs
	 * 
	 * @param includeRoot
	 * @param paths
	 * @return
	 */
	public static String buildFilePath(boolean includeRoot, String... paths) {
		StringBuilder sb = new StringBuilder();
		if (includeRoot) {
			if (SystemUtils.IS_OS_WINDOWS) {
				sb.append(":");
			}
			sb.append(File.separator);
		}

		if (paths != null && paths.length > 0) {
			for (String path : paths) {
				sb.append(path);
				if (!path.endsWith(File.separator)) {
					sb.append(File.separator);
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	public static File getOrMakeDir(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		} else if (!dir.isDirectory()) {
			throw new UtilityException("Error making Directory: " + dir.getAbsolutePath());
		}
		return dir;
	}
	
	public static File getOrMakeDir(File parentDir, String newDirName) {
		File dir = new File(parentDir, newDirName);
		return getOrMakeDir(dir);
	}

	public static void createShortcut(File dir, File originalFile) {
		getOrMakeDir(dir);
		String shortcutName = getShortCutName(originalFile.getName());
		String shortcutPath = buildFilePath(dir.getAbsolutePath(), shortcutName);
		try {
			ShellLink.createLink(originalFile.getAbsolutePath(),shortcutPath );
		} catch (IOException e) {
			throw new UtilityException(String.format("Error creating shortcut for %s at %s!", originalFile.getAbsolutePath(), shortcutPath), e);
		}

	}

	public static String getShortCutName(String name) {
		return name + ".lnk";
	}

	/**
	 * returns the name of the directory this file is in
	 * 
	 * @param file
	 * @return
	 */
	public static String getDirName(File file) {
		if (file != null && file.getParentFile() != null) {
			return file.getParentFile().getName();
		}
		return null;
	}

	/**
	 * 
	 * find all files in this directory and all subdirectorys with this file name
	 * 
	 * @param dir
	 * @param fileName
	 * @return
	 */
	public static List<File> findAllFiles(File dir, String fileName) {
		List<File> allFiles = new LinkedList<File>();
		File file = findFile(dir, fileName);
		if (file != null) {
			allFiles.add(file);
		}
		for (File subDir : dir.listFiles()) {
			if (subDir.isDirectory()) {
				allFiles.addAll(findAllFiles(subDir, fileName));
			}
		}
		return allFiles;
	}

	public static boolean deleteFile(File dir, String fileName) {
		File file = findFile(dir, fileName);
		if (file != null && file.exists()) {
			return file.delete();
		}
		return false;
	}

	public static File findFile(File dir, String fileName) {
		File file = new File(dir, fileName);
		if (file.exists()) {
			return file;
		}
		return null;
	}

	public static void deleteExistingShortcuts(File dir, File origFile, boolean includeSubDirs) {
		String shortcut = getShortCutName(origFile.getName());
		deleteFile(dir, shortcut);
		if (includeSubDirs) {
			for (File subDir : dir.listFiles()) {
				if (subDir.isDirectory()) {
					deleteExistingShortcuts(subDir, origFile, true);
				} else {
					deleteFile(subDir, shortcut);
				}
			}
		}
	}

	public static List<String> readFileIntoLines(File file) {
		if(file.exists() && file.isDirectory()) {
			throw new IllegalArgumentException("cannot read directory");
		}
		List<String> lines = new LinkedList<>();
		if(file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String nextLine = reader.readLine();
				while (nextLine != null) {
					lines.add(nextLine);
					nextLine = reader.readLine();
				}
			} catch (IOException e) {
				throw new RuntimeException("error reading file", e);
			}
		}
		return lines;
	}

	public static void writeToFile(File file, List<String> lines, boolean append) {
		if(!append && file.exists()) {
			file.delete();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
			for(String line:lines){
				writer.append(line);
				writer.newLine();
			}
			writer.flush();
		}
		catch(IOException e) {
			throw new RuntimeException("error writing file", e);
		}
	}

	public static void copyFile(File source, File dest) {
		try {
			org.apache.commons.io.FileUtils.copyFile(source, dest);
		} catch(IOException e) {
			throw new RuntimeException("Unable to copy file", e);
		}
	}

	public static void moveFile(File src, File dest) {
		try {
			org.apache.commons.io.FileUtils.moveFile(src, dest);
		} catch(IOException e) {
			throw new RuntimeException("Unable to copy file", e);
		}
	}
}
