package phonebook.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TelephoneDirectory {
    private ArrayList<Contact> contacts;

    public TelephoneDirectory() {
        contacts = new ArrayList<Contact>();
    }

    public void addContact(String name, String phoneNumber) {
        contacts.add(new Contact(name, phoneNumber));
        logAction("add", name, phoneNumber);
    }
    

    public void removeContact(String name) {
        for (Contact entry : contacts) {
            if (entry.getName().equals(name)) {
                contacts.remove(entry);
                logAction("remove", name, "");
                break;
            }
        }
    }

    public ArrayList<Contact> searchContact(String searchString) {
        ArrayList<Contact> result = new ArrayList<Contact>();
        for (Contact entry : contacts) {
            if (entry.getName().toLowerCase().contains(searchString.toLowerCase())
                    || entry.getPhoneNumber().toLowerCase().contains(searchString.toLowerCase())) {
                result.add(entry);
            }
        }
        return result;
    }

    public Contact getContact(String name, String phone) {
        for (Contact entry : contacts) {
            if (entry.getName().equals(name) && entry.getPhoneNumber().equals(phone)) {
                return entry;
            }
        }
        return null;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void export(String fileName, String declamer) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "UTF8"))) {
                for (Contact record : contacts) {
                    writer.write(record.getName() + declamer + record.getPhoneNumber() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importContacts(File file, String declamer) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] contactData = line.split(declamer);
                if (contactData.length >= 2) {
                    addContact(contactData[0], contactData[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logAction(String action, String name, String phone) {
        String fileName = "telephone_directory_log.txt";
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(    
            new FileOutputStream(fileName, true), "UTF8"))) {
            String logMessage = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : " + action + " : "
                    + name + " : " + phone + "\n";
            bw.write(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
