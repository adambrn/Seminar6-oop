package phonebook;

import phonebook.gui.View;
import phonebook.model.TelephoneDirectory;

public class Main {
    public static void main(String[] args) {
        TelephoneDirectory directory = new TelephoneDirectory();
        directory.addContact("Семён", "123-456");
        View view = new View(directory);
        view.run();

    }
}
