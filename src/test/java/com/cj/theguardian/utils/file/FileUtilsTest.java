package com.cj.theguardian.utils.file;

import org.jetbrains.annotations.TestOnly;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileUtilsTest {

    public static void main(String[] args) {
       File file = new File("E:\\Other\\Porn\\scripts\\alreadyUsed.txt");
       List<String> lines =  FileUtils.readFileIntoLines(file);
       lines.forEach(l -> System.out.println(l));
       FileUtils.writeToFile(file, Arrays.asList("hello","conor","this","is","weird"), false);
    }


}
