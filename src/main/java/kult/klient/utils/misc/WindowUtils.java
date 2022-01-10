package kult.klient.utils.misc;

import kult.klient.utils.Version;

import static kult.klient.KultKlient.mc;

public class WindowUtils {
    public static class KultKlient {
        public static void set() {
            setIcon();
            setTitle();
        }

        public static void setIcon() {
            mc.getWindow().setIcon(WindowUtils.class.getResourceAsStream("/assets/kultklient/textures/icons/icon64.png"), WindowUtils.class.getResourceAsStream("/assets/kultklient/textures/icons/icon128.png"));
        }

        public static void setTitleLoading() {
            mc.getWindow().setTitle("KultKlient " + Version.getStylized() + " - " + mc.getVersionType() + " " + Version.getMinecraft() + " is being loaded...");
        }

        public static void setTitleLoaded() {
            mc.getWindow().setTitle("KultKlient " + Version.getStylized() + " - " + mc.getVersionType() + " " + Version.getMinecraft() + " loaded!");
        }

        public static void setTitle() {
            mc.getWindow().setTitle("KultKlient " + Version.getStylized() + " - " + mc.getVersionType() + " " + Version.getMinecraft());
        }
    }

    public static class Meteor {
        public static void set() {
            setIcon();
            setTitle();
        }

        public static void setIcon() {
            mc.getWindow().setIcon(WindowUtils.class.getResourceAsStream("/assets/kultklient/textures/icons/meteor64.png"), WindowUtils.class.getResourceAsStream("/assets/kultklient/textures/icons/meteor128.png"));
        }

        public static void setTitle() {
            mc.getWindow().setTitle("Meteor Client " + Version.getStylized() + " - " + mc.getVersionType() + " " + Version.getMinecraft());
        }
    }
}
