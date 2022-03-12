package com.cj.theguardian.utils.hardrive;

import com.cj.theguardian.utils.harddrive.SyncDirectory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class SyncDirectoryTest {

    private File source = new File("E:\\Test\\SubTest");
    private File dest = new File("E:\\Test2\\SubTest2");
    private File shouldNotBeSynced = new File("E:\\Test\\SubTest\\shouldNotBeSynced");

    @Before
    public void removeAllFromDest() {
       // dest.delete();
    }

    @Test
    public void testValidSource() {
        SyncDirectory sync = new SyncDirectory(source, dest, Arrays.asList(shouldNotBeSynced));
       // sync.sync();
    }

    @Test
    public void testValidSource2() {
        File source = new File("E:\\Test\\SubTest");
        File dest = new File("E:\\Test2\\SubTest\\someSubClass\\someOtherSubtest");

        SyncDirectory sync = new SyncDirectory(source, dest, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSource() {
        File source = new File("E:\\Test\\SubTest");
        File dest = new File("E:\\Test\\SubTest");

        SyncDirectory sync = new SyncDirectory(source, dest, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSourceRoot() {
        File source = new File("E:\\");
        File dest = new File("E:\\");

        SyncDirectory sync = new SyncDirectory(source, dest, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSource2() {
        File source = new File("E:\\Test\\SubTest");
        File dest = new File("E:\\Test\\SubTest\\someSubClass");

        SyncDirectory sync = new SyncDirectory(source, dest, null);
    }
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSource3() {
        File source = new File("E:\\Test\\SubTest");
        File dest = new File("E:\\Test\\SubTest\\someSubClass\\someOtherSubtest");

        SyncDirectory sync = new SyncDirectory(source, dest, null);
    }
}
