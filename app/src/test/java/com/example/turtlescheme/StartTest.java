package com.example.turtlescheme;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class StartTest
{
    @Test
    public void testCheckAppStart()
    {
        String className = "com.example.turtlescheme.MainActivity";  // Change here
        MainActivity mainActivity= null;
        try
        {
            mainActivity= (MainActivity) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        // First start
        int oldVersion = -1;
        int newVersion = 1;
        assert mainActivity != null;
        assertEquals("Expected result", MainActivity.AppStart.FIRST_TIME, mainActivity.checkAppStart(newVersion, oldVersion));

        // First start this version
        oldVersion = 1;
        newVersion = 2;
        assertEquals("Expected result", MainActivity.AppStart.FIRST_TIME_VERSION, mainActivity.checkAppStart(newVersion, oldVersion));

        // Normal start
        oldVersion = 2;
        newVersion = 2;
        assertEquals("Expected result", MainActivity.AppStart.NORMAL, mainActivity.checkAppStart(newVersion, oldVersion));
    }
}
