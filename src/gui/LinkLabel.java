package gui;

import config.LanguageManager;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class LinkLabel extends javax.swing.JLabel implements java.awt.event.MouseListener {
    
    private String url;
    
    public LinkLabel(String text) {
        super(text);
        init(text);
    }
    
    public void setURL(String url) {
        this.url = url;
        this.setToolTipText(LanguageManager.getValue("Open XXX in your browser").replace("XXX", url));
    }
    
    private void init(String url) {
        setURL(url);
        this.addMouseListener(this);
        this.setForeground(Color.BLUE);
    }
    
    @Override
    public void mouseClicked(MouseEvent arg0) {
        browse();
    }
    
    @Override
    public void mouseEntered(MouseEvent arg0) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    @Override
    public void mouseExited(MouseEvent arg0) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    @Override
    public void mousePressed(MouseEvent arg0) {
    }
    
    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
    
    private void browse() {
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new java.net.URI(url));
                    return;
                } catch (java.io.IOException | java.net.URISyntaxException e) {
                    System.err.println("Unable to open browser.\n" + e.getMessage());
                }
            }
        }
        
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (osName.startsWith("Mac OS")) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                java.lang.reflect.Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else {
                //check for $BROWSER
                java.util.Map<String, String> env = System.getenv();
                if (env.get("BROWSER") != null) {
                    Runtime.getRuntime().exec(env.get("BROWSER") + " " + url);
                    return;
                }

                //check for common browsers
                String[] browsers = {"firefox", "iceweasel", "chrome", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                        break;
                    }
                }
                if (browser == null) {
                    throw new RuntimeException("Couldn't find any browser...");
                } else {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InterruptedException | RuntimeException e) {
            javax.swing.JOptionPane.showMessageDialog(null, LanguageManager.getValue("Unable to open browser.\\nPlease browse for yourself:\\n") + url);
        }
    }
}
