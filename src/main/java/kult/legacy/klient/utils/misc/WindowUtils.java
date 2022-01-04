package kult.legacy.klient.utils.misc;

import kult.legacy.klient.utils.Version;

import static kult.legacy.klient.KultKlientLegacy.mc;

public class WindowUtils {
    public static class KultKlient {
        public static void set() {
            setIcon();
            setTitle();
        }

        public static void setIcon() {
            mc.getWindow().setIcon(WindowUtils.class.getResourceAsStream("/assets/kultklientlegacy/textures/icons/icon64.png"), WindowUtils.class.getResourceAsStream("/assets/kultklientlegacy/textures/icons/icon128.png"));
        }

        public static void setTitleLoading() {
            mc.getWindow().setTitle("KultKlient Legacy " + Version.getStylized() + " - " + mc.getVersionType() + " " + Version.getMinecraft() + " is being loaded...");
        }

        public static void setTitleLoaded() {
            mc.getWindow().setTitle("KultKlient Legacy " + Version.getStylized() + " - " + mc.getVersionType() + " " + Version.getMinecraft() + " loaded!");
        }

        public static void setTitle() {
            mc.getWindow().setTitle("KultKlient Legacy " + Version.getStylized() + " - " + mc.getVersionType() + " " + Version.getMinecraft());
        }
    }

    public static class Meteor {
        public static void set() {
            setIcon();
            setTitle();
        }

        public static void setIcon() {
            mc.getWindow().setIcon(WindowUtils.class.getResourceAsStream("/assets/kultklientlegacy/textures/icons/meteor64.png"), WindowUtils.class.getResourceAsStream("/assets/kultklientlegacy/textures/icons/meteor128.png"));
        }

        public static void setTitle() {
            mc.getWindow().setTitle("Meteor Client " + Version.getStylized() + " - " + mc.getVersionType() + " " + Version.getMinecraft());
        }
    }
}
