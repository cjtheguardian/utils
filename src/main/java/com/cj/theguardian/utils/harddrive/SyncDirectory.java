package com.cj.theguardian.utils.harddrive;

import com.cj.theguardian.utils.file.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SyncDirectory {

    private File destRoot;
    private File sourceRoot;
    private File archiveRoot;
    private List<File> doNotSync;
    private List<String> errors = new LinkedList<>();

    public SyncDirectory(String source, String dest, List<String> doNotSync) {
        this(new File(source), new File(dest), doNotSync != null ? doNotSync.stream().map(s -> new File(s)).collect(Collectors.toList()) : new LinkedList<>());
    }

    public SyncDirectory(File sourceRoot, File destRoot, List<File> doNotSync) {
        this.sourceRoot=sourceRoot;
        this.destRoot = destRoot;
        this.archiveRoot = getArchivedRoot(destRoot);
        this.doNotSync = doNotSync;

        init();
        System.out.println("Source root: " + sourceRoot.getAbsolutePath());
        System.out.println("Dest root: " + destRoot.getAbsolutePath());
        System.out.println("doNotSync: " );
        if(doNotSync != null) {
            for (File file : doNotSync) {
                System.out.println("----" + file.getAbsolutePath());
            }
        }
    }

    private File getArchivedRoot(File destRoot) {
        File archived = new File(destRoot, "archive");
        return new File(archived,new SimpleDateFormat("yyyy_MM_dd").format(new Date()));
    }

    private void init() {
        destRoot.mkdirs();
        if(!sourceRoot.exists()) {
            throw new IllegalArgumentException("Non existent source " + sourceRoot.getAbsolutePath());
        }
        validateDestNotInSource();
    }

    private void validateDestNotInSource() {
        File parent = destRoot;
        while(parent != null) {
            if(sourceRoot.equals(parent)) {
                throw new IllegalArgumentException("Cannot sync a to a destination that exists inside the source");
            }
            parent = parent.getParentFile();
        }
    }

    public void sync() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {

        }
        sync(sourceRoot, destRoot, archiveRoot);
        for(String error : errors) {
            System.err.println(error);
        }
    }

    private void sync(File source, File dest, File archive) {
        try {
            if (source.isDirectory()) {
                dest.mkdirs();

                File[] sourceFiles = source.listFiles();
                if(sourceFiles == null) {
                    errors.add("Error listing file for "+source.getAbsolutePath());
                } else {
                    for (File sourceFile : sourceFiles) {
                        if (doNotSync.contains(sourceFile)) {
                            System.out.println("Not syncing " + sourceFile.getAbsolutePath());
                        } else {
                            sync(sourceFile, new File(dest, sourceFile.getName()), new File(archive, sourceFile.getName()));
                        }
                    }
                }
            } else {
                boolean copyFile = true;
                if (dest.exists()) {
                    copyFile = false;
                    if (source.length() != dest.length()) {
                        System.out.println("WARNING: source file already exists at destination, but is different size, archiving existing" + source.getAbsolutePath());
                        FileUtils.moveFile(dest, archive);
                        copyFile = true;
                    }
                }
                if (copyFile) {
                    System.out.println("Copying file " + source.getAbsolutePath());
                    FileUtils.copyFile(source, dest);
                } else {
                    //System.out.println("ignoring file " + source.getAbsolutePath());
                }
            }
        }catch(Exception e) {
            System.err.println("Error processing " + source.getAbsolutePath() +" : " + dest.getAbsolutePath());
            throw e;
        }
    }

}
