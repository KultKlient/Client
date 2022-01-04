package kult.legacy.klient.gui.themes.meteor.widgets;

import kult.legacy.klient.gui.WidgetScreen;
import kult.legacy.klient.gui.themes.meteor.MeteorWidget;
import kult.legacy.klient.gui.widgets.WAccount;
import kult.legacy.klient.systems.accounts.Account;
import kult.legacy.klient.utils.render.color.Color;

public class WMeteorAccount extends WAccount implements MeteorWidget {
    public WMeteorAccount(WidgetScreen screen, Account<?> account) {
        super(screen, account);
    }

    @Override
    protected Color loggedInColor() {
        return theme().loggedInColor.get();
    }

    @Override
    protected Color accountTypeColor() {
        return theme().textSecondaryColor.get();
    }
}
