import java.awt.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.management.openmbean.InvalidKeyException;
import javax.swing.*;
import java.awt.event.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.security.*;
import java.io.*;
import javax.crypto.*;


class MainScreen {
    JFrame frame;
    JLabel image = new JLabel(new ImageIcon("xxxxxxxxxxxx"));
    JLabel text = new JLabel("PASSWORD MANAGER");
    JProgressBar progressBar = new JProgressBar();

    //JLabel message = new JLabel();
    MainScreen() {
        createGUI();
        addImage();
        addText();
        addProgressBar();
        runProgBar();
    }

    public void createGUI() {
        frame = new JFrame();
        frame.getContentPane().setLayout(null);
        frame.setUndecorated(true);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(0XFF8787));
        frame.setVisible(true);
    }

    public void addImage() {
        image.setSize(400,200);
        frame.add(image);
    }

    public void addText() {
        text.setFont(new Font("MV Boli", Font.BOLD, 20));
        text.setBounds(80, 200, 400, 30);
        text.setForeground(Color.black);
        frame.add(text);
    }

    public void addProgressBar() {
        progressBar.setBounds(100, 280, 200, 30);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.black);
        progressBar.setForeground(new Color(0X38E54D));
        progressBar.setValue(0);
        frame.add(progressBar);
    }

    public void runProgBar() {
        for (int i = 0; i <= 100; i++) {
            try{
                Thread.sleep(40);
                progressBar.setValue(i);
                if (i == 100) {
                    frame.dispose();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class HashtablePassword implements hashTableMap {
    private Entry[] entries;
    private final float loadFactor;
    private int size, used;
    private final Entry NIL = new Entry(null, null);
    private static class Entry {
        Object key, value;
        Entry(Object k, Object v)
        {
            key = k;
            value = v;
        }
    }

    public HashtablePassword(int capacity, float loadFactor, int useProbe) {
        entries = new Entry[capacity];
        this.loadFactor = loadFactor;
    }

    public int hash (Object key) {
        return (key.hashCode() & 0X7FFFFFFF) % entries.length;
    }

    private int nextProbe (int h, int i) {
        return (h + i) % entries.length;
    }

    private void rehash() {
        Entry[] oldEntries = entries;
        entries = new Entry[2*entries.length+1];
        for (Entry entry: oldEntries) {
            if (entry == NIL || entry == null) {
                continue;
            }
            int h = hash(entry.key);
            for (int x = 0; x < entries.length; x++) {
                int j = nextProbe(h, x);
                if (entries[j] == null) {
                    entries[j] = entry;
                    break;
                }
            }
            used = size;
        }
    }

    @Override
    public Object get_acc(Object account) {
        return null;
    }

    @Override
    public int add_acc(Object account, Object password) {
        if (used > (loadFactor * entries.length))rehash();
        int h = hash(account);
        for (int i = 0; i < entries.length; i++) {
            int j = (h+i) % entries.length;
            Entry entry = entries[j];
            if (entry == null) {
                entries[j] = new Entry(account, password);
                ++size;
                ++used;
                return h;
            }
            if (entry == NIL)
                continue;
            if (entry.key.equals(account)) {
                Object oldValue = entry.value;
                entries[j].value = password;
                return (int) oldValue;
            }
        }
        return h;
    }

    @Override
    public Object remove_acc(Object account) {
        return null;
    }

    @Override
    public Object get_Acc(Object Account) {
        int h = hash(Account);
        for(int i = 0; i < entries.length; i++){
            int j = nextProbe(h , i);
            Entry entry = entries[j];
            if(entry == null)
                break;
            if(entry == NIL)
                continue;
            if(entry.key.equals(Account))
                return entry.value;
        }
        return null;
    }
    @Override
    public Object remove_Acc(Object Account) {
        int h = hash(Account);
        for(int i = 0; i < entries.length; i++){
            int j = nextProbe(h,i);
            Entry entry = entries[j];
            if(entry == NIL)
                continue;
            if(entry.key.equals(Account)){
                Object Value = entry.value;
                entries[j] = NIL;
                size--;
                return Value;
            }
        }
        return null;
    }
}

class CryptoUtil {
    Cipher ecipher, dcipher;
    byte[] salt = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    int count = 19;

    public CryptoUtil() {

    }

//    public String encrypt(String secretKey, String plainText)
//        throws NoSuchAlgorithmException,
//            InvalidKeySpecException,
//            NoSuchPaddingException,
//            InvalidKeyException,
//            InvalidAlgorithmParameterException,
//            UnsupportedEncodingException,
//            IllegalBlockSizeException,
//            BadPaddingException, java.security.InvalidKeyException {
//        byte[] keyBytes = secretKey.getBytes("UTF-8");
//        SecretKey keySpec = new SecretKeySpec(keyBytes, "AES");
//
//        SecureRandom random = new SecureRandom();
//        byte[] iv = new byte[16];
//        random.nextBytes(iv);
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
//        byte[] plaintextBytes = plainText.getBytes("UTF-8");
//        byte[] ciphertextBytes = cipher.doFinal(plaintextBytes);
//        byte[] combinedBytes = new byte[iv.length + ciphertextBytes.length];
//        System.arraycopy(iv, 0, combinedBytes, 0, iv.length);
//        System.arraycopy(ciphertextBytes, 0, combinedBytes, iv.length, ciphertextBytes.length);
//        String encryptedText = Base64.getEncoder().encodeToString(combinedBytes);
//        return encryptedText;
//    }
//
//    public String decrypt(String secretKey, String encryptedText)
//            throws NoSuchAlgorithmException,
//            InvalidKeySpecException,
//            NoSuchPaddingException,
//            InvalidKeyException,
//            InvalidAlgorithmParameterException,
//            UnsupportedEncodingException,
//            IllegalBlockSizeException,
//            BadPaddingException, java.security.InvalidKeyException {
//        byte[] keyBytes = secretKey.getBytes("UTF-8");
//        SecretKey keySpec = new SecretKeySpec(keyBytes, "AES");
//
//        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
//        byte[] iv = new byte[16];
//        System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);
//        Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        dcipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
//
//        byte[] decrypedBytes = dcipher.doFinal(encryptedBytes, iv.length, encryptedBytes.length - iv.length);
//
//        String plainText = new String(decrypedBytes, "UTF-8");
//
//        return plainText;
//    }

    public String encrypt(String secretKey, String plainText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException, java.security.InvalidKeyException {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, count);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5andDES").generateSecret(keySpec);
        AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(salt, count);

        ecipher = Cipher.getInstance(key.getAlgorithm());
        ecipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        String charSet = "UTF-8";
        byte[] in = plainText.getBytes(charSet);
        byte[] out = ecipher.doFinal(in);
        String encStr = new String(Base64.getEncoder().encode(out));
        return encStr;
    }

    public String decrypt(String secretKey, String encryptedText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException, java.security.InvalidKeyException {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, count);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5andDES").generateSecret(keySpec);
        AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(salt, count);

        dcipher = Cipher.getInstance(key.getAlgorithm());
        dcipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte[] enc = Base64.getDecoder().decode(encryptedText);
        byte[] utf8 = dcipher.doFinal(enc);
        String charSet = "UTF-8";
        String plainStr = new String(utf8, charSet);
        return plainStr;
    }
}

class PasswordGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String smallCaps = caps.toLowerCase();
    private static final String numeric = "1234567890";
    private static final String specialChar = "~!@#$%^&*()_+{}|:;?[]<>=";
    private static final String dic = caps + smallCaps + numeric + specialChar;

    public String generatePassword(int len) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            password.append(dic.charAt(index));
        }
        return password.toString();
    }
}

interface hashTableMap {
    Object get_acc(Object account);
    int add_acc(Object account, Object password);
    Object remove_acc(Object account);

    Object get_Acc(Object Account);

    Object remove_Acc(Object Account);
}

class PasswordManager implements ActionListener {
    HashtablePassword data = new HashtablePassword(15, 0.5F, 0);

    JFrame frame;
    JFrame frame2;
    JLabel background;
    Container cont1, cont2;
    JLabel lAcc, lPass;
    JTextArea encryptPasswordArea, generatePasswordArea, searchPasswordArea;
    JButton passwordGeneBtn, passwordStoreBtn, passwordSearchBtn, accAddBtn, passwordDelBtn;
    JTextField tAcc, tPass;
    JButton addNoteBtn;
    JLabel addNoteLabel;
    JTextArea tNote;
    JButton addNote;
    JFrame cont3;

    ArrayList<String> notes = new ArrayList<>();

    @Override
    public void actionPerformed(ActionEvent e) { }

    //Frame settings
    public static void FrameGUI(JFrame frame){
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
    }


    //container settings
    public static void ContainerGUI(Container conn){
        conn.setVisible(true);
        conn.setBackground(Color.getHSBColor(20.4f, 10.5f, 12.9f));
        conn.setLayout(null);
    }


    // buttons settings
    public void GUIButtonsSetting(JButton btn){
        btn.setBackground(new Color(0XFB2576));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        btn.setFocusable(false);
        Cursor crs = new Cursor(Cursor.HAND_CURSOR);
        btn.setCursor(crs);
        Font fn = new Font("MV Boli", Font.BOLD, 15);
        btn.setFont(fn);
    }

    public void StoringGUI()
    {
        frame2 = new JFrame("Store your passwords");
        frame2.setBounds(1400, 300, 800, 500);
        frame2.setSize(400,400);
        FrameGUI(frame2);
        cont2 = frame2.getContentPane();
        ContainerGUI(cont2);
        Font fn = new Font("MV Boli", Font.BOLD, 20);

        //Account textFiled and label
        lAcc = new JLabel("ACCOUNT NAME");
        lAcc.setBounds(90, 23, 380, 20);
        lAcc.setFont(fn);
        cont2.add(lAcc);

        tAcc = new JTextField();
        tAcc.setBounds(90,70,200,50);
        tAcc.setFont(fn);
        tAcc.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        tAcc.setForeground(Color.DARK_GRAY);
        cont2.add(tAcc);

        //Account password textField and label
        lPass = new JLabel("ACCOUNT PASSWORD");
        lPass.setBounds(90, 160, 380, 20);
        lPass.setFont(fn);
        cont2.add(lPass);

        tPass = new JTextField();
        tPass.setBounds(90,200,200,50);
        tPass.setFont(fn);
        tPass.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        tPass.setForeground(Color.DARK_GRAY);
        cont2.add(tPass);

        accAddBtn = new JButton("STORE");
        accAddBtn.setBounds(120, 290, 150, 50);
        cont2.add(accAddBtn);
        GUIButtonsSetting(accAddBtn);
    }

    //for password generator and encryption
    public void textArea(String Pass,JTextArea TA){
        TA.setText(Pass);
        Font fn = new Font("MV Boli", Font.BOLD, 20);
        TA.setWrapStyleWord(true);
        TA.setLineWrap(true);
        TA.setCaretPosition(0);
        TA.setEditable(false);
        TA.setFont(fn);

    }

    PasswordManager() {

        frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,650);
        frame.setResizable(false);
        ImageIcon img = new ImageIcon("background.png");
        background = new JLabel("",img,JLabel.CENTER);
        background.setBounds(0,0,400,650);
        background.setVisible(true);
        frame.add(background);

        FrameGUI(frame);

        cont1 = frame.getContentPane();
        ContainerGUI(cont1);

        //Generator buttons settings
        passwordGeneBtn = new JButton("GENERATE PASSWORD");
        passwordGeneBtn.setBounds(90, 20, 220, 40);
        cont1.add(passwordGeneBtn);
        GUIButtonsSetting(passwordGeneBtn);

        //generating password
        passwordGeneBtn.addActionListener(e -> {
                    if(passwordGeneBtn ==e.getSource())
                    {
                        try{
                            int len = Integer.parseInt(JOptionPane.showInputDialog("Enter the password length"));
                            if(len>4)
                            {
                                //  password generator class reference
                                PasswordGenerator pass = new PasswordGenerator();
                                String passwd = pass.generatePassword(len);
                                generatePasswordArea = new JTextArea(5,4);
                                textArea(passwd,generatePasswordArea);
                                JOptionPane.showMessageDialog(cont1,new JScrollPane(generatePasswordArea),"Copy your password",JOptionPane.INFORMATION_MESSAGE);

                            }
                            else JOptionPane.showMessageDialog (cont1,"Password length must be greater than 8!","Invalid Input Error",JOptionPane.WARNING_MESSAGE);

                        }
                        catch(Exception ex){JOptionPane.showMessageDialog(cont1,"Write something","EXIT!",JOptionPane.ERROR_MESSAGE);}
                    }
                }
        );

        // add a encryption button and action
        JButton EncryptBtn = new JButton("ENCRYPT Text");
        EncryptBtn.setBounds(90, 90, 220, 40);
        cont1.add(EncryptBtn);
        GUIButtonsSetting(EncryptBtn);
        EncryptBtn.addActionListener(e -> {
                    if(EncryptBtn ==e.getSource())
                    {
                        try{
                            String text = JOptionPane.showInputDialog("Enter the text to encrypt");
                            String secretKey = JOptionPane.showInputDialog("Enter the secret key");
                            if(text.length()>0 && secretKey.length()>0)
                            {
                                //  password generator class reference
                                CryptoUtil pass1 = new CryptoUtil();
                                String passwd = pass1.encrypt(secretKey, text); // encrypting the text
                                generatePasswordArea = new JTextArea(5,4); // text area for the encrypted text
                                textArea(passwd,generatePasswordArea); // setting the text area
                                JOptionPane.showMessageDialog(cont1,new JScrollPane(generatePasswordArea),"Copy your password",JOptionPane.INFORMATION_MESSAGE); // showing the encrypted text

                            }
                            else JOptionPane.showMessageDialog (cont1,"Write something","Invalid Input Error",JOptionPane.WARNING_MESSAGE);

                        }
                        catch(Exception ex){JOptionPane.showMessageDialog(cont1,"Write something","EXIT!",JOptionPane.ERROR_MESSAGE);}
                    }
                }
        );

        // add a decryption button and action
        JButton DecryptBtn = new JButton("DECRYPT Text");
        DecryptBtn.setBounds(90, 160, 220, 40);
        cont1.add(DecryptBtn);
        GUIButtonsSetting(DecryptBtn);
        DecryptBtn.addActionListener(e -> {
                    if(DecryptBtn ==e.getSource())
                    {
                        try{
                            String text = JOptionPane.showInputDialog("Enter the text to decrypt"); // getting the encrypted text
                            String secretKey = JOptionPane.showInputDialog("Enter the secret key"); // getting the secret key
                            if(text.length()>0 && secretKey.length()>0) // checking if the text and secret key is not empty
                            {
                                //  password generator class reference
                                CryptoUtil pass1 = new CryptoUtil(); // creating a object of the CryptoUtil class
                                String passwd = pass1.decrypt(secretKey, text); // decrypting the text
                                generatePasswordArea = new JTextArea(5,4); // text area for the decrypted text
                                textArea(passwd,generatePasswordArea); // setting the text area
                                JOptionPane.showMessageDialog(cont1,new JScrollPane(generatePasswordArea),"Decrypted text",JOptionPane.INFORMATION_MESSAGE); // showing the decrypted text

                            }
                            else JOptionPane.showMessageDialog (cont1,"Password length must be greater than 8!","Invalid Input Error",JOptionPane.WARNING_MESSAGE);

                        }
                        catch(Exception ex){JOptionPane.showMessageDialog(cont1,"Write something","EXIT!",JOptionPane.ERROR_MESSAGE);}
                    }
                }
        );

        //storing password using hashtable
        passwordStoreBtn = new JButton("STORE PASSWORD");
        passwordStoreBtn.setBounds(90, 230, 220, 40);
        cont1.add(passwordStoreBtn);
        GUIButtonsSetting(passwordStoreBtn);
        //Store password action
        passwordStoreBtn.addActionListener(e -> {
                    if(passwordStoreBtn ==e.getSource())
                    {
                        try{
                            StoringGUI();
                            // action on the Store btn
                            accAddBtn.addActionListener(e4 -> {
                                        if (accAddBtn == e4.getSource()) {
                                            String account_name = tAcc.getText(); // getting the account name
                                            String acc_pass = tPass.getText(); // getting the password
                                            if (account_name.isEmpty() && acc_pass.isEmpty()) {
                                                JOptionPane.showMessageDialog(cont2,"unable to store your password!","ERROR",JOptionPane.ERROR_MESSAGE);
                                            }
                                            else{
                                                //calling put method of the hashtablePassword class
                                                data.add_acc(account_name,acc_pass); // adding the account name and password to the hashtable
                                                JOptionPane.showMessageDialog(cont2, "Account added Successfully !");
                                                tAcc.setText(null);
                                                tPass.setText(null);
                                            }
                                        }
                                    }
                            );
                        }
                        catch(Exception ex) {JOptionPane.showMessageDialog(cont2,"Write something","EXIT",JOptionPane.ERROR_MESSAGE);}
                    }
                }
        );

        //searching password
        passwordSearchBtn = new JButton("SEARCH PASSWORD");
        GUIButtonsSetting(passwordSearchBtn);
        passwordSearchBtn.setBounds(90, 300, 220, 40);
        cont1.add(passwordSearchBtn);
        passwordSearchBtn.addActionListener(e ->{
                    if (passwordSearchBtn ==e.getSource()){
                        try{
                            String acc_name = JOptionPane.showInputDialog("Enter your Account Name"); // getting the account name
                            if (!acc_name.isBlank()) { // checking if the account name is not empty
                                Object pass = data.get_Acc(acc_name.toLowerCase()); // getting the password of the account name
                                if(pass!=null) { // checking if the password is not null
                                    searchPasswordArea = new JTextArea(4,5); // text area for the password
                                    textArea(String.valueOf(pass), searchPasswordArea); // setting the text area
                                    JOptionPane.showMessageDialog(cont1, new JScrollPane(searchPasswordArea), "Copy your password", JOptionPane.INFORMATION_MESSAGE);
                                }
                                else JOptionPane.showMessageDialog(cont1, "Account not Found!");
                            }
                        }
                        catch (Exception ex){
                            JOptionPane.showMessageDialog(cont1,"Write something","EXIT",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        );

        // deleting password
        passwordDelBtn = new JButton("DELETE PASSWORD");
        GUIButtonsSetting(passwordDelBtn);
        passwordDelBtn.setBounds(90, 370, 220, 40);
        cont1.add(passwordDelBtn);
        passwordDelBtn.addActionListener(e -> {
                    if (passwordDelBtn == e.getSource()) {
                        try {
                            String acc_name = JOptionPane.showInputDialog("Enter the Account Name"); // getting the account name
                            if (!acc_name.isBlank()) {
                                data.remove_Acc(acc_name.toLowerCase()); // removing the account name and password from the hashtable
                                JOptionPane.showMessageDialog(cont1, "Delete successfully!"); // showing the message
                            }
                            else JOptionPane.showMessageDialog(cont1, "Account not found!", "INFO", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(cont1, "Write something", "EXIT", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
        );

        addNoteBtn = new JButton("ADD NOTE");
        GUIButtonsSetting(addNoteBtn);
        addNoteBtn.setBounds(90, 440, 220, 40);
        cont1.add(addNoteBtn);
        addNoteBtn.addActionListener(e -> {
                    if (addNoteBtn == e.getSource()) {
                        try {
                            NoteGUI();
                            // action on the add note btn
                            addNote.addActionListener(e4 -> {
                                if (addNote == e4.getSource()) {
                                    String note = tNote.getText(); // getting the note
                                    if (note.isEmpty()) {
                                        JOptionPane.showMessageDialog(cont3, "unable to store your note!", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        //calling put method of the hashtablePassword class
                                        notes.add(note); // adding the note to the arraylist
                                        JOptionPane.showMessageDialog(cont3, "Note added Successfully !");
                                        cont3.setVisible(false);
                                        tNote.setText(null);
                                    }
                                }
                            });
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(cont3, "Write something", "EXIT", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        );

        //get all notes
        JButton getNoteBtn = new JButton("GET NOTE");
        GUIButtonsSetting(getNoteBtn);
        getNoteBtn.setBounds(90, 510, 220, 40);
        cont1.add(getNoteBtn);
        getNoteBtn.addActionListener(e -> {
                    if (getNoteBtn == e.getSource()) {
                        try {
                            String allNotes = notes.get(notes.size() - 1); // getting the last note added
                            if (allNotes.isEmpty()) { // checking if the note is empty or not
                                JOptionPane.showMessageDialog(cont1, "No note found!", "INFO", JOptionPane.INFORMATION_MESSAGE); // showing the message
                            } else {
                                searchPasswordArea = new JTextArea(4, 5); // text area for the note
                                textArea(allNotes, searchPasswordArea); // setting the text area
                                JOptionPane.showMessageDialog(cont1, new JScrollPane(searchPasswordArea), "Get your notes", JOptionPane.INFORMATION_MESSAGE); // showing the message
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(cont1, "Add a note before trying to retrive", "EXIT", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        );

    }

    // method for setting the buttons and GUI for adding notes
    private void NoteGUI() {

        cont3 = new JFrame("Add Note");
        cont3.setSize(500, 500);
        cont3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cont3.setLocationRelativeTo(null);
        cont3.setLayout(null);
        cont3.setVisible(true);
        cont3.setResizable(false);

        //add note label
        addNoteLabel = new JLabel("Add Note");
        addNoteLabel.setBounds(200, 20, 100, 30);
        cont3.add(addNoteLabel);

        //add note text area
        tNote = new JTextArea(10, 10);
        tNote.setBounds(100, 60, 300, 300);
        cont3.add(tNote);

        //add note button
        addNote = new JButton("ADD NOTE");
        GUIButtonsSetting(addNote);
        addNote.setBounds(140, 380, 220, 30);
        cont3.add(addNote);
    }
}

public class PasswordManagerApp {
    public static void main (String [] args) {
        new MainScreen();
        try {
            new PasswordManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
