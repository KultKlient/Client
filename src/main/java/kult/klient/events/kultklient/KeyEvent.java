package kult.klient.events.kultklient;

import kult.klient.utils.misc.input.KeyAction;
import kult.klient.events.Cancellable;

public class KeyEvent extends Cancellable {
    private static final KeyEvent INSTANCE = new KeyEvent();

    public int key, modifiers;
    public KeyAction action;

    public static KeyEvent get(int key, int modifiers, KeyAction action) {
        INSTANCE.setCancelled(false);
        INSTANCE.key = key;
        INSTANCE.modifiers = modifiers;
        INSTANCE.action = action;
        return INSTANCE;
    }
}
