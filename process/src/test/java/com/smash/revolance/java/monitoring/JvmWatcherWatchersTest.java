package com.smash.revolance.java.monitoring;

import com.smash.revolance.commons.Series;
import com.smash.revolance.process.monitoring.JvmMonitoring;
import com.smash.revolance.process.monitoring.JvmWatcher;
import com.smash.revolance.process.monitoring.jvm.Jvm;
import com.smash.revolance.process.monitoring.jvm.Jvms;
import com.smash.revolance.process.monitoring.jvm.filter.By;
import com.smash.revolance.process.monitoring.jvm.filter.JvmSearchCriteria;
import com.smash.revolance.process.monitoring.utils.CmdlineHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ebour on 17/11/13.
 */
public class JvmWatcherWatchersTest
{
    private final static String VMNAME = "jvm-monitoring-materials-memory-consumer-1.0.0-SNAPSHOT.jar";
    private static CmdlineHelper cmdline;

    @BeforeClass
    public static void beforeTests() throws Exception
    {
        cmdline = launchMemoryConsumer();
    }

    @AfterClass
    public static void afterTests()
    {
        if(cmdline != null)
            cmdline.kill();
    }

    @Test
    public void watcherShouldHandleNewJvm() throws Exception
    {
        JvmWatcher memoryConsumer = monitorMemoryConsumer(VMNAME);
        waitAndStopMemoryConsumer(4, memoryConsumer);

        Series metrics = memoryConsumer.getMetrics();
        assertThat(metrics.getDates(0).isEmpty(), is(false));
    }

    private void waitAndStopMemoryConsumer(long duration, JvmWatcher memoryConsumer) throws Exception
    {
        new Thread(memoryConsumer).start();
        Thread.sleep(duration*1000);

        cmdline.kill(); // Stop the memory consumer
    }

    private JvmWatcher monitorMemoryConsumer(String vmName) throws Exception
    {
        JvmMonitoring watchers = new JvmMonitoring(vmName);
        watchers.update();

        List<Jvm> jvmWatchers = Jvms.find(watchers.listJvms(), new JvmSearchCriteria(By.VMNAME, vmName));
        Jvm memoryConsumer = jvmWatchers.get(0);

        assertThat(memoryConsumer.getName(), equalTo(VMNAME));
        assertThat(memoryConsumer.getOptions().containsKey("-Xmx512M"), is(true));
        JvmWatcher watcher = watchers.getWatcher(memoryConsumer);


        return watcher;
    }

    private static CmdlineHelper launchMemoryConsumer() throws Exception
    {
        // Start the memory gobbler
        CmdlineHelper cmdline = new CmdlineHelper();
        cmdline.dir(new File(new File("").getAbsoluteFile(), "target/materials"));

        cmdline.cmd("java", "-Xmx512M", "-jar", VMNAME);
        cmdline.exec().waitInMs(1000).withoutErrors();

        return cmdline;
    }

}
