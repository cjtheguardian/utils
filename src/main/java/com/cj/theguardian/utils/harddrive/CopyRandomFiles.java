package com.cj.theguardian.utils.harddrive;

import com.cj.theguardian.utils.file.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.cj.theguardian.utils.file.FileUtils.convertToWindowsPath;

public class CopyRandomFiles {

    private static final int DEFAULT_COPY_COUNT = 20;
    private File sourceDir;
    private File destDir;
    private File ignoredFileList;
    private int numberToCopy;

    public CopyRandomFiles(File sourceDir, File destDir, File ignoredFileList, Integer numToCopy) {
        this.sourceDir = sourceDir;
        this.destDir = destDir;
        this.ignoredFileList = ignoredFileList;
        this.numberToCopy = numToCopy == null ? DEFAULT_COPY_COUNT : numToCopy;
        validateOrCreateDirectory(sourceDir);
        validateOrCreateDirectory(destDir);
    }


    public CopyRandomFiles(String sourceDirPath, String destDirPath, String ignoredFilesTextPath, Integer numToCopy) {
        this(new File(sourceDirPath),  new File(destDirPath),StringUtils.isNotBlank(ignoredFilesTextPath) ? new File(ignoredFilesTextPath) : null, numToCopy);
    }

    private void validateOrCreateDirectory(File directory) {
        if(directory.exists()) {
            if(!directory.isDirectory()) {
                throw new IllegalArgumentException("Invalid directory: " + directory.getAbsolutePath());
            }
        } else {
            directory.mkdirs();
        }
    }

    private List<String> getIgnoredFilesFromText() {
        List<String> ignoredFileNames;
        if(ignoredFileList != null && ignoredFileList.exists()) {
            ignoredFileNames = FileUtils.readFileIntoLines(ignoredFileList);
        } else {
            ignoredFileNames = new LinkedList<>();
        }
        return ignoredFileNames;
    }

    public void copyFiles() {

        List<String> allFilenames = Arrays.stream(sourceDir.listFiles()).map(f -> f.getName()).collect(Collectors.toList());
        System.out.println("All filenames size: "+allFilenames.size());
        List<String> ignoredFilenames = getIgnoredFilesFromText();
        System.out.println("Ignored filenames size: "+ignoredFilenames.size());
        allFilenames.removeAll(ignoredFilenames);

        System.out.println("Considering files size: " + allFilenames.size());

        Collections.shuffle(allFilenames);
        Random random = new Random(new Date().getTime());

        List<String> filenamesToPull = new LinkedList<>();
        if(allFilenames.isEmpty()) {
            throw new RuntimeException("no files available");
        }
        for(int i =0;i<numberToCopy;i++) {
            if(i >= allFilenames.size()) {
                break;
            }
            int position = random.nextInt(allFilenames.size());
            filenamesToPull.add(allFilenames.get(position));
        }
        System.out.println("downloading files:");
        for (String file : filenamesToPull) {
            System.out.println("downloading "+file);
            File source = new File(sourceDir, file);
            File dest = new File(destDir, file);
            if(!dest.exists()) {
                FileUtils.copyFile(source, dest);
            }
            System.out.println("finished " + file);
        }

        if(ignoredFileList != null) {
            FileUtils.writeToFile(ignoredFileList, filenamesToPull, true);
        }
    }

}
