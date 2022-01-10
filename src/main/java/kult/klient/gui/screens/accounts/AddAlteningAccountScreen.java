package kult.klient.gui.screens.accounts;

import kult.klient.gui.widgets.containers.WTable;
import kult.klient.gui.widgets.input.WTextBox;
import kult.klient.gui.GuiTheme;
import kult.klient.systems.accounts.types.TheAlteningAccount;

public class AddAlteningAccountScreen extends AddAccountScreen {
    public AddAlteningAccountScreen(GuiTheme theme, AccountsScreen parent) {
        super(theme, "Add The Altening Account", parent);
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).widget();

        // Token
        t.add(theme.label("Token: "));
        WTextBox token = t.add(theme.textBox("")).minWidth(400).expandX().widget();
        token.setFocused(true);
        t.row();

        // Add
        add = t.add(theme.button("Add")).expandX().widget();
        add.action = () -> {
            if (!token.get().isEmpty()) AccountsScreen.addAccount(this, parent, new TheAlteningAccount(token.get()));
        };

        enterAction = add.action;
    }
}
