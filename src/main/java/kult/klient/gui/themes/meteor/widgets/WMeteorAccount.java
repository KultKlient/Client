package kult.klient.gui.themes.meteor.widgets;

import kult.klient.gui.WidgetScreen;
import kult.klient.gui.themes.meteor.MeteorWidget;
import kult.klient.gui.widgets.WAccount;
import kult.klient.systems.accounts.Account;
import kult.klient.utils.render.color.Color;

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
