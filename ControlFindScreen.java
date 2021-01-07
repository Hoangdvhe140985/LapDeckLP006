/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GUI.FindScreen;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author pc
 */
public class ControlFindScreen {

    private final FindScreen findFrame;

    private JTextArea findText;
    private int index = 0;
    public int lastIndex;

    public ControlFindScreen(FindScreen findFrame) {
        this.findFrame = findFrame;

    }

    public void setJTextArea(JTextArea jTextArea) {
        this.findText = jTextArea;
    }

    public void cancel() {
        findFrame.setVisible(false);
        findFrame.setFind("");
        index = 0;
    }

    int findNext() {
        boolean RadioFind = findFrame.radio();
        boolean check = findFrame.checkbox();
        String s1 = findText.getText();
        String s2 = findFrame.getFind();
        lastIndex = findText.getCaretPosition();    //variable to save findText's position in find
        int selStart = findText.getSelectionStart();    //varialbes to specify the findText's position
        int selEnd = findText.getSelectionEnd();        //
        if (RadioFind) {
            if (selStart != selEnd) {
                lastIndex = selEnd - s2.length() - 1;
            }
            if (!check) {   //If matchCase == false
                lastIndex = s1.toUpperCase().lastIndexOf(s2.toUpperCase(), lastIndex);
            } else { //If matchCase == true
                lastIndex = s1.lastIndexOf(s2, lastIndex);
            }
        } else {
            if (selStart != selEnd) {
                lastIndex = selStart + 1;
            }
            if (!check) { //If matchCase == false
                lastIndex = s1.toUpperCase().indexOf(s2.toUpperCase(), lastIndex);
            } else {    //If matchCase == true
                lastIndex = s1.indexOf(s2, lastIndex);
            }
        }

        return lastIndex;

    }

    public void findNextWithSelection() {
        if (findFrame.txtFind.getText().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(findFrame, "Please enter find");
            return;
        }
        int idx = findNext();
        if (idx != -1) {    //if catch end or start of text -> finish
            findText.setSelectionStart(idx); //color the specified text on Notepad
            findText.setSelectionEnd(idx + findFrame.getFind().length());
        } else {    //if no more findText to display -> showMessage to finish
            JOptionPane.showMessageDialog(findFrame,
                    "Finished find" + " \"" + findFrame.getFind() + "\"",
                    "Find", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void replaceNext() {
        boolean check = findFrame.checkbox();

        // if nothing is selectd
        if (findText.getSelectionStart() == findText.getSelectionEnd()) {
            findNextWithSelection();
            return;
        }

        String searchText = findFrame.getFind();
        String temp = findText.getSelectedText();	//get selected text

        //check if the selected text matches the search text then do replacement
        if ((check && temp.equals(searchText))
                || (!check && temp.equalsIgnoreCase(searchText))) {
            findText.replaceSelection(findFrame.getReplace());
        }

        findNextWithSelection();
    }

    //Replace all text in textArea
    public void replaceAll() {
        String find = findFrame.getFind();
        String replace = findFrame.getReplace();
        findFrame.getRadioUp().setSelected(true);
        while(findText.getText().contains(find)){
            findText.replaceRange(replace, findNext(), findNext()+find.length());
        }
        //findText.setText(replaceTemp);
    }

}
