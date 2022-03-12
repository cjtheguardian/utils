package com.cj.theguardian.utils.harddrive;

import com.cj.theguardian.utils.file.FileUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
        this.sourceRoot = sourceRoot;
        this.destRoot = destRoot;
        this.archiveRoot = getArchivedRoot(destRoot);
        this.doNotSync = doNotSync;

        init();
        System.out.println("Source root: " + sourceRoot.getAbsolutePath());
        System.out.println("Dest root: " + destRoot.getAbsolutePath());
        System.out.println("doNotSync: ");
        if (doNotSync != null) {
            for (File file : doNotSync) {
                System.out.println("----" + file.getAbsolutePath());
            }
        }
    }

    private File getArchivedRoot(File destRoot) {
        File archived = new File(destRoot, "archive");
        return new File(archived, new SimpleDateFormat("yyyy_MM_dd").format(new Date()));
    }

    private void init() {
        destRoot.mkdirs();
        if (!sourceRoot.exists()) {
            throw new IllegalArgumentException("Non existent source " + sourceRoot.getAbsolutePath());
        }
        validateDestNotInSource();
    }

    private void validateDestNotInSource() {
        File parent = destRoot;
        while (parent != null) {
            if (sourceRoot.equals(parent)) {
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
        for (String error : errors) {
            System.err.println(error);
        }
    }

    private void sync(File sourceRoot, File destRoot, File archiveRoot) {
        List<SyncFile> filesToSync = prepareFileSyncs(sourceRoot, destRoot, archiveRoot);
        long totalSize = filesToSync.stream().mapToLong(f -> f.source.length()).sum();
        long totalFiles = filesToSync.stream().count();
        BigDecimal totalGb = BigDecimal.valueOf(totalSize).divide(BigDecimal.valueOf(1000000000), 2, RoundingMode.HALF_UP);
        System.out.println("Found " + totalFiles + " comprising of ~" + totalGb + " GB");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
        long totalSynced = 0;
        long filesSynced = 0;
        for (SyncFile syncFile : filesToSync) {
            syncFile.syncFile();
            totalSynced += syncFile.source.length();
            filesSynced++;
            BigDecimal percentageFilesSynced = percentage(filesSynced, totalFiles);
            BigDecimal percentageDataSynced = percentage(totalSynced, totalSize);
            System.out.println(String.format("status: synced %s/%s files (%s%%) , and %s/%s bytes (%s%%)",
                    filesSynced, totalFiles, percentageFilesSynced,
                    totalSynced, totalSize, percentageDataSynced));
        }
    }

    private BigDecimal percentage(long top, long bottom) {
        return BigDecimal.valueOf(top).divide(BigDecimal.valueOf(bottom), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100).setScale(0, RoundingMode.HALF_UP));
    }

    private List<SyncFile> prepareFileSyncs(File source, File dest, File archive) {
        List<SyncFile> syncFiles = new ArrayList<>();
        try {
            if (source.isDirectory()) {
                File[] sourceFiles = source.listFiles();
                if (sourceFiles == null) {
                    errors.add("Error listing file for " + source.getAbsolutePath());
                } else {
                    for (File sourceFile : sourceFiles) {
                        if (doNotSync.contains(sourceFile)) {
                            System.out.println("Not syncing " + sourceFile.getAbsolutePath());
                        } else {
                            syncFiles.addAll(prepareFileSyncs(sourceFile, new File(dest, sourceFile.getName()), new File(archive, sourceFile.getName())));
                        }
                    }
                }
            } else {
                if (!dest.exists() || source.length() != dest.length()) {
                    syncFiles.add(new SyncFile(source, dest, archive));
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing " + source.getAbsolutePath() + " : " + dest.getAbsolutePath());
            throw e;
        }
        return syncFiles;
    }

    private static class SyncFile {
        private File source;
        private File dest;
        private File archive;

        private SyncFile(File source, File dest, File archive) {
            this.source = source;
            this.dest = dest;
            this.archive = archive;
        }

        private void syncFile() {
            if (dest.exists()) {
                System.out.println("WARNING: source file already exists at destination, but may be different size, archiving existing" + source.getAbsolutePath());
                FileUtils.moveFile(dest, archive);
            } else {
                dest.getParentFile().mkdirs();
            }
            System.out.println("Copying file " + source.getAbsolutePath());
            FileUtils.copyFile(source, dest);

        }
    }
}
