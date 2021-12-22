package kultklient.legacy.client.gui.themes.kultklient.widgets;

import kultklient.legacy.client.gui.WidgetScreen;
import kultklient.legacy.client.gui.themes.kultklient.KultKlientWidget;
import kultklient.legacy.client.gui.widgets.WAccount;
import kultklient.legacy.client.systems.accounts.Account;
import kultklient.legacy.client.utils.render.color.Color;

public class WKultKlientAccount extends WAccount implements KultKlientWidget {
    public WKultKlientAccount(WidgetScreen screen, Account<?> account) {
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
