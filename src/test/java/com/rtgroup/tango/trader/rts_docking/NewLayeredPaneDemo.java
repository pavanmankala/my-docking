package com.rtgroup.tango.trader.rts_docking;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

//~--- classes ----------------------------------------------------------------

public class NewLayeredPaneDemo {
    private final static Color
        BORDER_COLOR        = new Color(57, 105, 138, 126),
        FILL_COLOR          = new Color(161, 180, 195, 126);
    static JComponent comp1 = new JComponent() {
        protected void paintComponent(Graphics g) {
            g = g.create();
            Graphics2D g2d  = (Graphics2D) g;
            Dimension  d    = getSize();

            g.setColor(FILL_COLOR);
            g.fillRect(0, 0, d.width, d.height);

            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(BORDER_COLOR);
            g2d.drawRect(0, 0, d.width, d.height);
            System.out.println(g.getClipBounds());
            g.dispose();
        }
    };

    //~--- methods ------------------------------------------------------------

    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    break;
                }
            }

            UIDefaults defaults = UIManager.getDefaults();

            defaults.put("ScrollBar.maximumThumbSize", new Dimension(10000, 10000));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLayeredPane layeredPane  = new JLayeredPane();
        final JFrame window       = new JFrame();
        String[]     layerStrings = { "Yellow (0)", "Magenta (1)", "Cyan (2)", "Red (3)", "Green (4)" };
        Color[]      layerColors  = { Color.yellow, Color.magenta, Color.cyan, Color.red, Color.green };

        Point        origin       = new Point(10, 20);
        int          offset       = 35;


        JComponent base = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(new JTextArea("Hello world", 100, 100)),
                new JScrollPane(new JTextArea("Hello world", 100, 100)));
        base.setBounds(0, 0, 700, 300);
        layeredPane.add(base, new Integer(-1));
        for (int i = 0; i < layerStrings.length; i++) {
            JLabel label = createColoredLabel(layerStrings[i], layerColors[i], origin);

            layeredPane.add(label, new Integer(i));
            origin.x += offset;
            origin.y += offset;
        }

        comp1.setOpaque(false);
        comp1.setBounds(origin.x, origin.y, 160, 140);

        layeredPane.add(comp1, new Integer(100));

        window.getContentPane().add(new JLabel("Hello World"), BorderLayout.PAGE_START);
        window.getContentPane().add(layeredPane, BorderLayout.CENTER);
        window.setSize(700, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private static JLabel createColoredLabel(String text, Color color, Point origin) {
        JLabel label = new JLabel(text);

        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 126));
        label.setForeground(Color.black);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setBounds(origin.x, origin.y, 140, 140);

        return label;
    }
}
