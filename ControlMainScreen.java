/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GUI.ChangeFontFrame;
import GUI.FindScreen;
import GUI.MainScreen;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */
public class ControlMainScreen {

    private final MainScreen main;
    private String saved = "";
    private final FindScreen findFrame = new FindScreen();
    private final ChangeFontFrame changeFontFrame = new ChangeFontFrame();
    boolean checkUndo = true;

    public ControlMainScreen(MainScreen main) {
        this.main = main;
        main.text.getDocument().addUndoableEditListener(main.undoManager);
        findFrame.setJTextArea(main.text);

    }
    
// If select nothing, you can't copy or cut 
    public String getClipboardContent() {
        String data = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(null);
        boolean hasTrans = (content != null)
                && content.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTrans) {
            try {
                data = (String) content.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
            }
        }
        return data;
    }

    public void checker() throws UnsupportedFlavorException, IOException {

        if (!getClipboardContent().equals("")) {
            main.btnPaste.setEnabled(true);
            System.out.println("null");
        } else {
            main.btnPaste.setEnabled(false);
        }
        if (main.text.getSelectedText() != null) {
            main.btnCopy.setEnabled(true);
            main.btnCut.setEnabled(true);
        } else {
            main.btnCopy.setEnabled(false);
            main.btnCut.setEnabled(false);
        }
        if (main.text.getText().equalsIgnoreCase("")) {
            main.btnSelectAll.setEnabled(false);
        } else {
            main.btnSelectAll.setEnabled(true);
        }
        if (main.undoManager.canUndo()) {
            main.btnUndo.setEnabled(true);
        } else {
            main.btnUndo.setEnabled(false);
        }
        if (main.undoManager.canRedo()) {
            main.btnRedo.setEnabled(true);
        } else {
            main.btnRedo.setEnabled(false);
        }

    }

    // Display box to answer save or not
    private boolean navigator() {
        checkUndo = false;
        int answer = JOptionPane.showConfirmDialog(main, "File has not been saved. Do you want to save it?");
        boolean temp = false;
        
        switch (answer) {
            case JOptionPane.YES_OPTION:
                temp = saveNotepad();
                break;
            case JOptionPane.NO_OPTION:
                temp = true;
                break;
            case JOptionPane.CANCEL_OPTION:
                temp = false;
                break;
            default:
                break;
        }
        
        return temp;
    }
    // Clear all method

    private void makeNew() {
        main.text.setText("");
        checkUndo = false;
        saved = "";
        //main.setFile(null);
        main.setTitle("Notepad");

    }

    // new Menu
    public void newNotepad() {
        if (!saved.equals(main.text.getText())) {
            // Chua Save thi hoi xem co muon Save khong?
            if (navigator()) {
                checkUndo = false;
                makeNew();
                main.undoManager.die();
            }           
        } else {
            // Neu da luu roi thi NEW thoi
            makeNew();
            checkUndo = false;
        }       
    }

    private void open() {
        // Ask user
        FileDialog fDialog = new FileDialog(main, "Open ...", FileDialog.LOAD);
        fDialog.setVisible(true);

        // Get the file name chosen by the user
        String name = fDialog.getFile();

        // If user canceled file selection, return without doing anything.
        if (name == null) {
            return;
        }

        // Open file
        String fileName = fDialog.getDirectory() + name;
        File file = new File(fileName);

        StringBuilder textBuffer = new StringBuilder();
        try {
            // Try to create a file reader from the chosen file.
            FileReader reader;
            reader = new FileReader(fileName);
            // Try to read from the file one line at a time.
            try (BufferedReader bReader = new BufferedReader(reader)) {
                // Try to read from the file one line at a time.
                String textLine = bReader.readLine();
                while (textLine != null) {
                    textBuffer.append(textLine).append('\n');
                    textLine = bReader.readLine();
                }
            }
            reader.close();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(main, "Error reading file: " + ioe.toString());
            return;
        }
        // When opened
        rename(name);   // reset frame title
        main.text.setText(textBuffer.toString());
        saved = main.text.getText();
        main.setFile(file);
    }

    // Open file
    public void openNotepad() {
        if (!saved.equals(main.text.getText())) {
            // Chua Save thi hoi xem co muon SAVE khong?
            if (navigator()) {
                open();
            }
        } else {
            // Neu da SAVE roi thi OPEN thoi
            open();
        }
    }

    // Save File
    public boolean saveNotepad() {
        if (main.getFile() == null) {
            return saveAs();
        } else {
            save(main.getFile());
            return true;
        }
    }

    // Save As
    public void saveAsNotepad() {
        saveAs();
    }

    // Save to file file
    private void save(File file) {
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(main.text.getText());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(main, ex.getMessage());
            return;
        }
        // after save
        saved = main.text.getText();
        rename(file.getName());
        main.setFile(file);
    }

    private boolean saveAs() {
        FileDialog fDialog = new FileDialog(main, "Save", FileDialog.SAVE);
        fDialog.setVisible(true);
        if (fDialog.getFile() != null) {
            String path = fDialog.getDirectory() + fDialog.getFile();
            System.out.println(path);
            File file = new File(path);
            save(file);
            return true;
        }
        return false;
    }

    // Rename
    private void rename(String name) {
        main.setTitle(name + " - MainScreen");
    }

    // Exit
    public void exit() {
        if (saved.equalsIgnoreCase(main.text.getText())) {
            // Saved -> exit
            System.exit(0);
        } else {
            if (navigator()) {
                System.exit(0);
            }
        }

    }

    // Select All
    public void selectAll() {
        main.text.selectAll();
    }

    // Cut
    public void cut() {
        main.text.cut();
    }

    // Copy
    public void copy() {
        main.text.copy();
    }

    // Paste
    public void paste() {
        main.text.paste();
    }

    // Undo
    public void undo() {
        
        if (main.text.getText().equals("")) {
            checkUndo = false;
            makeNew();
            main.undoManager.die();
            
        }else{
            main.undoManager.undo();
        }
    }

    // Redo
    public void redo() {
        main.undoManager.redo();
    }

    // Find 
    public void find() {
        findFrame.txtFind.setText("");
        findFrame.setVisible(true);
        findFrame.setTitle("Find");
        findFrame.replaceButton.setVisible(false);
        findFrame.replaceAllButton.setVisible(false);
        findFrame.txtReplace.setVisible(false);
        findFrame.replaceLabel.setVisible(false);
    }

    // Replace
    public void replace() {
        findFrame.txtFind.setText("");
        findFrame.setVisible(true);
        findFrame.replaceButton.setVisible(true);
        findFrame.replaceAllButton.setVisible(true);
        findFrame.txtReplace.setVisible(true);
        findFrame.replaceLabel.setVisible(true);
        findFrame.setTitle("Replace");
    }

    // Change font
    public void changeFont() {
        changeFontFrame.setTextarea(main.text);
        changeFontFrame.setVisible(true);
    }

}
