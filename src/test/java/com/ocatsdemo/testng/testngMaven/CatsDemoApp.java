package com.ocatsdemo.testng.testngMaven;

import com.comcast.cats.*;
import com.comcast.cats.configuration.OCatsContext;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Properties;

public class CatsDemoApp {
    private static final String MAC_ID = "00:19:5E:C4:91:59";
    private static final String SERVER = "http://10.10.149.65/";
    private static final int KEYPRESS_INTERVAL = 1000;
    private static final int KEYPRESS_INTERVAL_SHORT = 200;

    Properties props = new Properties();

    protected ApplicationContext context;
    protected Settop settop;

    @BeforeClass
    public void beforeClass() throws AllocationException, SettopNotFoundException {
        System.setProperty( CatsProperties.SERVER_URL_PROPERTY, SERVER );
        System.setProperty( CatsProperties.SETTOP_DEFAULT_PROPERTY,  MAC_ID);

        CatsFramework framework = new CatsFramework( new OCatsContext() );
        context = framework.getContext();

        settop = framework.getSettopFactory().findSettopByHostMac(MAC_ID, true);

        Assert.assertNotNull(settop, "STB " + MAC_ID + "is null");

    }

    @Test
    public void testChannelUpDown() throws InterruptedException {
        Assert.assertTrue( settop.pressKey(RemoteCommand.EXIT), "Press Exit ");
        Thread.sleep(1000);
        Assert.assertTrue( settop.pressKey(RemoteCommand.CHDN), "Press CHDN ");
        Thread.sleep(2000);
        Assert.assertTrue( settop.pressKey(RemoteCommand.CHUP), "Press CHUP ");
        Thread.sleep(2000);
        Assert.assertTrue( settop.pressKey(RemoteCommand.EXIT), "Press Exit ");

    }

    @Test
    public void addChannelToFavorite() throws InterruptedException {

        Assert.assertTrue( settop.pressKey( RemoteCommand.EXIT ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertTrue( settop.pressKey( RemoteCommand.INFO ) );
        Thread.sleep( KEYPRESS_INTERVAL_SHORT );
        Assert.assertTrue( settop.pressKey( RemoteCommand.INFO ) );
        Thread.sleep( KEYPRESS_INTERVAL_SHORT );
        Assert.assertTrue( settop.pressKey( RemoteCommand.RIGHT ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertTrue( settop.pressKey( RemoteCommand.RIGHT ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertTrue( settop.pressKey( RemoteCommand.SELECT ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertTrue( settop.pressKey( RemoteCommand.SELECT ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertTrue( settop.pressKey( RemoteCommand.EXIT ) );

    }


    @Test
    public void testWithImageCompare() throws InterruptedException {
        Assert.assertTrue( settop.pressKey(RemoteCommand.EXIT), "Press Exit ");
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertFalse(settop.getImageCompareProvider().waitForImageRegion("src/test/resources/imagecompare/guide.xml", 5000),
                "Guide screen IS NOT visible on the UI");
        Assert.assertTrue( settop.pressKey(RemoteCommand.GUIDE), "Press Guide");
        Assert.assertTrue( settop.getImageCompareProvider().waitForImageRegion("src/test/resources/imagecompare/guide.xml", 5000),
                "Guide screen IS visible on the UI");
        ArrayList< RemoteCommand > commandList = new ArrayList< >();
        commandList.add( RemoteCommand.ONE);
        commandList.add( RemoteCommand.SIX );
        Assert.assertTrue( settop.pressKeys(commandList), "Press 16");
        Assert.assertTrue( settop.getImageCompareProvider().waitForImageRegion("src/test/resources/imagecompare/channel_16_loaded.xml", 5000),
                "Channel 16 loaded on the UI");
        Assert.assertTrue( settop.pressKey(RemoteCommand.INFO), "Press INFO");
        Assert.assertTrue( settop.getImageCompareProvider().waitForImageRegion("src/test/resources/imagecompare/channel_16_pgm_info.xml", 5000),
                "Program info of channel 16 program is visible on the UI");
        Assert.assertTrue( settop.pressKey(RemoteCommand.EXIT), "Press Exit ");
        
    }

}
