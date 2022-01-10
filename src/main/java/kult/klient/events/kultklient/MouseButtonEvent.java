package kult.klient.events.kultklient;

import kult.klient.utils.misc.input.KeyAction;
import kult.klient.events.Cancellable;

public class MouseButtonEvent extends Cancellable {
    private static final MouseButtonEvent INSTANCE = new MouseButtonEvent();

    public int button;
    public KeyAction action;

    public static MouseButtonEvent get(int button, KeyAction action) {
        INSTANCE.setCancelled(false);
        INSTANCE.button = button;
        INSTANCE.action = action;
        return INSTANCE;
    }
}
