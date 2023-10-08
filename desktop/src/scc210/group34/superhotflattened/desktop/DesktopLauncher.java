package scc210.group34.superhotflattened.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import scc210.group34.superhotflattened.SuperHotFlattened;
import scc210.group34.superhotflattened.menu.GameManager;

import java.awt.*;

public class DesktopLauncher
{

    public static void main(String[] arg)
    {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        // maximises the window.
        config.setWindowedMode((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());

        new Lwjgl3Application(new GameManager(), config);
    }
}
