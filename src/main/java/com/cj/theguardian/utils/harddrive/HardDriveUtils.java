package com.cj.theguardian.utils.harddrive;

import com.cj.theguardian.utils.file.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class HardDriveUtils {

    private static enum Context {
        PULL_RANDOM,
        SYNC;
    }
    public static void main(String[] args) {
        System.out.println("Note: if shit aint working right, check VTRoot dir and unblock in comodo");
        for(String arg : args) {
            System.out.println(arg);
        }
        Context context = Context.valueOf(args[0]);
        handleContext(context,args);
    }

    private static void handleContext(Context context, String[] args) {
        switch(context) {
            case PULL_RANDOM:
               handlePullRandom(args);
               break;
            case SYNC:
                handleSync(args);
                break;

        }
    }

    private static void handleSync(String[] args) {
        String sourceDir = FileUtils.convertToWindowsPath(args[1]);
        String destDir = FileUtils.convertToWindowsPath(args[2]);
        List<String> ignoredList = new LinkedList<>();
        for(int i = 3 ; i < args.length ; i++) {
            String[] split = args[i].split("\\|");
            for(String string : split){
                String windowsPath = FileUtils.convertToWindowsPath(string);
                if(StringUtils.isNotBlank(windowsPath)) {
                    System.out.println("should ignore " + windowsPath);
                    ignoredList.add(windowsPath);
                }
            }
        }
        SyncDirectory main = new SyncDirectory(sourceDir, destDir, ignoredList);
        main.sync();
    }

    private static void handlePullRandom(String[] args) {
        String sourceDir = args[1];
        String destDir = args[2];
        String arg3 = args[3];
        String ignoreListFile = null;
        Integer numToCopy = null;
        if(arg3.matches("[0-9]{1,3}")) {
            numToCopy = Integer.parseInt(arg3);
        } else {
            ignoreListFile = arg3;
        }
        if(args.length > 4) {
            numToCopy = Integer.parseInt(args[4]);
        }
        CopyRandomFiles main = new CopyRandomFiles(sourceDir, destDir, ignoreListFile, numToCopy);
        main.copyFiles();
    }

}
