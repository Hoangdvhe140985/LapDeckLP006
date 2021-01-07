/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GUI.ChangeFontFrame;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ControlChangeFont {

    ChangeFontFrame changeFontFrame;
    JTextArea changeArea = new JTextArea();
    Font font;
    String[] arrstyle = {"Plain", "Bold", "Italic", "Bold and Italic"};
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] arrfont = ge.getAvailableFontFamilyNames();
    ArrayList arrsize = new ArrayList();

    public ControlChangeFont(ChangeFontFrame changeFontFrame) {
        this.changeFontFrame = changeFontFrame;
        loadFont();
        loadSize();
        loadStyle();

        changeFontFrame.setSampleTAFont(font);
    }

    //load Font
    public final void loadFont() {
        for (String s : arrfont) {
            changeFontFrame.addFontList(s);
        }
    }

    //load Style
    public final void loadStyle() {
        for (String s : arrstyle) {
            changeFontFrame.addStyleList(s);
        }
    }

    //load Size
    public final void loadSize() {
        for (int i = 8; i <= 40; i++) {
            changeFontFrame.addSizeList(i);
            arrsize.add(i + "");
        }
    }

    //Pass jtextarea's detail from main frame to ChangeFontGUI
    public void setTextArea(JTextArea textarea) {
        this.changeArea = textarea;
        font = textarea.getFont();
        changeFontFrame.setFontText(font.getName());
        changeFontFrame.setStyleText(arrstyle[font.getStyle()]);
        changeFontFrame.setSizeText(font.getSize());
        changeFontFrame.SelectFontList(getindexoffontlist(font.getName()));
        changeFontFrame.SelectStyleList(font.getStyle());
        changeFontFrame.SelectSizeList(getindexofsizelist(font.getSize()));
    }

    public int getindexoffontlist(String s) {
        int index = 0;
        for (String i : arrfont) {
            if (i.equals(s)) {
                return index;
            }
            index++;
        }
        return 0;
    }

    public int getindexofsizelist(int i) {
        return arrsize.indexOf(i + "");
    }

    public boolean checkSize() {
        try {
            int i = Integer.valueOf(changeFontFrame.SizeText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //Message false if input size isn't integer, else set main frame text area font like this
    public void Change() {
        if (checkSize()) {
            ChangeSize(Integer.valueOf(changeFontFrame.SizeText()));
            changeArea.setFont(font);
            changeFontFrame.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(changeFontFrame, "Size must be integer");
        }
    }
    // change font

    public void ChangeSize(int i) {
        changeFontFrame.setSizeText(i);
        font = new Font(font.getName(), font.getStyle(), i);
        changeFontFrame.setSampleTAFont(font);
        changeFontFrame.setSizeText(i);
        changeFontFrame.SelectSizeList(getindexofsizelist(i));
    }

    //Back to main frame
    public void ClickCancel() {
        changeFontFrame.setVisible(false);
    }

    public void ChangeFont(String name) {

        font = new Font(name, font.getStyle(), font.getSize());
        changeFontFrame.setSampleTAFont(font);
        changeFontFrame.setFontText(name);

    }

    public void ChangeStyle(int i) {
        font = new Font(font.getName(), i, font.getSize());
        changeFontFrame.setSampleTAFont(font);
        changeFontFrame.setStyleText(arrstyle[i]);
    }

}
