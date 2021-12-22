package kultklient.legacy.client.gui.screens.accounts;

import kultklient.legacy.client.gui.GuiTheme;
import kultklient.legacy.client.gui.WindowScreen;
import kultklient.legacy.client.gui.widgets.WAccount;
import kultklient.legacy.client.gui.widgets.containers.WContainer;
import kultklient.legacy.client.gui.widgets.containers.WHorizontalList;
import kultklient.legacy.client.gui.widgets.pressable.WButton;
import kultklient.legacy.client.systems.accounts.Account;
import kultklient.legacy.client.systems.accounts.Accounts;
import kultklient.legacy.client.systems.accounts.MicrosoftLogin;
import kultklient.legacy.client.systems.accounts.types.MicrosoftAccount;
import kultklient.legacy.client.utils.misc.NbtUtils;
import kultklient.legacy.client.utils.network.KultKlientExecutor;
import org.jetbrains.annotations.Nullable;

import static kultklient.legacy.client.KultKlientLegacy.mc;

public class AccountsScreen extends WindowScreen {
    public AccountsScreen(GuiTheme theme) {
        super(theme, "Accounts");
    }

    @Override
    public void initWidgets() {
        // Accounts
        for (Account<?> account : Accounts.get()) {
            WAccount wAccount = add(theme.account(this, account)).expandX().widget();
            wAccount.refreshScreenAction = this::reload;
        }

        // Add account
        WHorizontalList l = add(theme.horizontalList()).expandX().widget();

        addButton(l, "Cracked", () -> mc.setScreen(new AddCrackedAccountScreen(theme, this)));
        addButton(l, "Premium", () -> mc.setScreen(new AddPremiumAccountScreen(theme, this)));
        addButton(l, "Microsoft", () -> {
            locked = true;

            MicrosoftLogin.getRefreshToken(refreshToken -> {
                locked = false;

                if (refreshToken != null) {
                    MicrosoftAccount account = new MicrosoftAccount(refreshToken);
                    addAccount(null, this, account);
                }
            });
        });
        addButton(l, "The Altening", () -> mc.setScreen(new AddAlteningAccountScreen(theme, this)));
    }

    private void addButton(WContainer c, String text, Runnable action) {
        WButton button = c.add(theme.button(text)).expandX().widget();
        button.action = action;
    }

    public static void addAccount(@Nullable AddAccountScreen screen, AccountsScreen parent, Account<?> account) {
        if (screen != null) screen.locked = true;

        KultKlientExecutor.execute(() -> {
            if (account.fetchInfo() && account.fetchHead()) {
                Accounts.get().add(account);
                if (account.login()) Accounts.get().save();

                if (screen != null) {
                    screen.locked = false;
                    screen.onClose();
                }

                parent.reload();

                return;
            }

            if (screen != null) screen.locked = false;
        });
    }

    @Override
    public boolean toClipboard() {
        return NbtUtils.toClipboard(Accounts.get());
    }

    @Override
    public boolean fromClipboard() {
        return NbtUtils.fromClipboard(Accounts.get());
    }
}
