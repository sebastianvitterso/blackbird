package main.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileIO {

	/**
	 * Imports all PDF files in specified folder.
	 */
	public static List<File> importPDFs(File folder) {
		FilenameFilter filter = (dir, filename) -> filename.endsWith(".pdf");
    	return new ArrayList<>(Arrays.asList(folder.listFiles(filter)));
	}
}
