package Russificator.First;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.LayoutStyle;

/*
 * Java ResourceBundle tutorial
 *
 * This program uses a ResourceBundle in a
 * Java Swing application.
 *
 * Author: Jan Bodnar
 * Website: zetcode.com
 * Last modified: August 2016
 */
public class ChangerLanguage extends JFrame {

  private ResourceBundle bundle;
  private JLabel lbl;
  private JMenu langMenu;
  private JRadioButtonMenuItem skMenuItem;
  private JRadioButtonMenuItem huMenuItem;

  public ChangerLanguage() {

    initUI();
  }

  private void initUI() {

    createMenuBar();

    lbl = new JLabel();

    updateLanguage(new Locale("ru", "RU"));

    createLayout(lbl);
    pack();

    setTitle(bundle.getString("name"));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private void updateLanguage(Locale locale) {

    //Меняем язык в стандартных SWING-компонентах на нужный нам, который мы установили
    // в настройках в папке resources
    bundle = ResourceBundle.getBundle("Russificator.First.resources.MyResources", locale);
    langMenu.setText(bundle.getString("lang_menu"));
    skMenuItem.setText(bundle.getString("lang_ru"));
    huMenuItem.setText(bundle.getString("lang_hu"));
    lbl.setText(bundle.getString("description"));
    setTitle(bundle.getString("name"));
    pack();
  }

  private void createMenuBar() {

    JMenuBar menubar = new JMenuBar();

    langMenu = new JMenu();
    langMenu.setMnemonic(KeyEvent.VK_F);

    ButtonGroup btnGroup = new ButtonGroup();

    skMenuItem = new JRadioButtonMenuItem("Русский", true);
    btnGroup.add(skMenuItem);

    skMenuItem.addActionListener((ActionEvent e) -> {
      updateLanguage(new Locale("ru", "RU"));
    });

    langMenu.add(skMenuItem);

    huMenuItem = new JRadioButtonMenuItem("Hungarian");
    btnGroup.add(huMenuItem);

    huMenuItem.addActionListener((ActionEvent e) -> {
      updateLanguage(new Locale("hu", "HU"));
    });

    langMenu.add(huMenuItem);

    menubar.add(langMenu);

    setJMenuBar(menubar);
  }

  private void createLayout(JComponent... arg) {

    Container pane = getContentPane();
    GroupLayout gl = new GroupLayout(pane);
    pane.setLayout(gl);

    gl.setAutoCreateContainerGaps(true);

    gl.setHorizontalGroup(gl.createParallelGroup()
        .addComponent(arg[0])
    );

    gl.setVerticalGroup(gl.createSequentialGroup()
        .addComponent(arg[0])
        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
    );
  }

  public static void main(String[] args) {

    EventQueue.invokeLater(() -> {
      ChangerLanguage ex = new ChangerLanguage();
      ex.setSize(500, 500);
      ex.setVisible(true);
    });
  }
}
