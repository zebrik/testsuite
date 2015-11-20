package org.jboss.hal.testsuite.test.configuration.logging;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.category.Standalone;
import org.jboss.hal.testsuite.dmr.Dispatcher;
import org.jboss.hal.testsuite.dmr.ResourceAddress;
import org.jboss.hal.testsuite.dmr.ResourceVerifier;
import org.jboss.hal.testsuite.finder.Application;
import org.jboss.hal.testsuite.finder.FinderNames;
import org.jboss.hal.testsuite.finder.FinderNavigation;
import org.jboss.hal.testsuite.fragment.ConfigFragment;
import org.jboss.hal.testsuite.page.config.LoggingPage;
import org.jboss.hal.testsuite.page.config.StandaloneConfigEntryPoint;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

/**
 * Created by pcyprian on 14.10.15.
 */
@RunWith(Arquillian.class)
@Category(Standalone.class)
public class FileHandlerTestCase {
    private static final String FILEHANDLER = "FILE_HANDLER";

    private FinderNavigation navigation;

    private ModelNode path = new ModelNode("/subsystem=logging/file-handler=" + FILEHANDLER);
    private ResourceAddress address = new ResourceAddress(path);
    private static Dispatcher dispatcher;
    private static ResourceVerifier verifier;

    @BeforeClass
    public static void setUp() {
        dispatcher = new Dispatcher();
        verifier  = new ResourceVerifier(dispatcher);
    }

    @AfterClass
    public static void tearDown() {
        dispatcher.close();
    }

    @Drone
    private WebDriver browser;
    @Page
    private LoggingPage page;

    @Before
    public void before() {
        navigation = new FinderNavigation(browser, StandaloneConfigEntryPoint.class)
                .addAddress(FinderNames.CONFIGURATION, FinderNames.SUBSYSTEMS)
                .addAddress(FinderNames.SUBSYSTEM, "Logging");

        navigation.selectRow().invoke(FinderNames.VIEW);
        Application.waitUntilVisible();

        page.switchToHandlerTab();
        page.switchToFile();
    }

    @Test
    @InSequence(0)
    public void addFileHandler() {
        page.addFileHandler(FILEHANDLER);

        verifier.verifyResource(address, true);
    }

    @Test
    @InSequence(1)
    public void updateFileHandlerNamedFormatter() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().text("named-formatter", "PATTERN");
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "named-formatter", "PATTERN", 500);

    }

    @Test
    @InSequence(2)
    public void updateFileHandlerEncoding() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().text("encoding", "UTF-8");
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "encoding", "UTF-8", 500);

    }

    @Test
    @InSequence(3)
    public void updateFileHandlerAppend() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().checkbox("append", false);
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "append", false, 500);

    }

    @Test
    @InSequence(4)
    public void updateFileHandlerAutoflush() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().checkbox("autoflush", false);
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "autoflush", false, 500);

    }

    @Test
    @InSequence(5)
    public void disableFileHandler() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().checkbox("enabled", false);
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "enabled", false, 500);

    }

    @Test
    @InSequence(6)
    public void updateFileHandlerLevel() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().select("level", "CONFIG");
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "level" , "CONFIG", 500);
    }

    @Test
    @InSequence(7)
    public void updateFileHandlerFilterSpec() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().text("filter-spec", "match(\"JBEAP.*\")");
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "filter-spec", "match(\"JBEAP.*\")", 500);

    }

    @Test
    @InSequence(8)
    public void updateFileHandlerFormatter() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().text("formatter", "%d{HH:mm:ss,SSS}");
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "formatter", "%d{HH:mm:ss,SSS}", 500);

    }


    @Test
    @InSequence(9)
    public void updateFileHandlerToDefualtSettings() {
        page.edit();
        ConfigFragment editPanelFragment = page.getConfigFragment();

        editPanelFragment.getEditor().text("named-formatter", "");
        editPanelFragment.getEditor().text("encoding", "");
        editPanelFragment.getEditor().text("filter-spec", "");
        editPanelFragment.getEditor().text("formatter", "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n");
        editPanelFragment.getEditor().checkbox("append", true);
        editPanelFragment.getEditor().checkbox("autoflush", true);
        editPanelFragment.getEditor().checkbox("enabled", true);
        editPanelFragment.getEditor().select("level", "ALL");
        boolean finished = editPanelFragment.save();

        assertTrue("Config should be saved and closed.", finished);
        verifier.verifyAttribute(address, "named-formatter", "undefined", 500);
        verifier.verifyAttribute(address, "encoding", "undefined", 500);
        verifier.verifyAttribute(address, "filter-spec", "undefined", 500);
        verifier.verifyAttribute(address, "formatter", "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n", 500);
        verifier.verifyAttribute(address, "append", true, 500);
        verifier.verifyAttribute(address, "autoflush", true, 500);
        verifier.verifyAttribute(address, "enabled", true, 500);
        verifier.verifyAttribute(address, "level", "ALL", 500);
    }

    @Test
    @InSequence(10)
    public void removeFileHandler() {
        page.remove();

        verifier.verifyResource(address, false);
    }
}