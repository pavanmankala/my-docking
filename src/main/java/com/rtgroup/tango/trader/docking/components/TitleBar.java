package com.rtgroup.tango.trader.docking.components;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.rtgroup.tango.trader.docking.components.IconsStore.IconVariation;
import com.rtgroup.tango.trader.docking.util.DockUtils;


//~--- JDK imports ------------------------------------------------------------
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.geom.Path2D;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.awt.BorderLayout;
import java.awt.Font;

//~--- classes ----------------------------------------------------------------

public class TitleBar extends JPanel {
    private static final int     DIM                 = 16;
    private static final long    serialVersionUID    = 1L;
    private static Color         _COLOR_BROWN        = new Color(185, 122, 87);
    private static final Pattern PATT                = Pattern.compile("<html><B>(.*)</B></html>");
    private static Color[]       EDITOR_TRANS_COLORS = DockUtils.getGradientBrightColors(Color.lightGray, 0.01f, 0.85f);
    private static final Icon    DEFAULT_ICON        = new DocumentIcon();

    //~--- fields -------------------------------------------------------------

    private final Animator   _animator = new Animator();
    private final JPanel     _buttonPanel;
    private final JTextField _editorField;
    private final JLabel     _label;
    private boolean          isEditing;

    //~--- constructors -------------------------------------------------------

    public TitleBar(String title) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        WindowMoveListener wml = new WindowMoveListener();

        _label       = new AnimatedLabel(title, DEFAULT_ICON, SwingConstants.LEFT);
        _buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        _editorField = new JTextField(10);

        Font baseFont = _label.getFont();

        _label.setFont(new Font(baseFont.getName(), Font.BOLD, baseFont.getSize()));
        _label.setToolTipText(title);
        _label.addMouseListener(wml);
        _label.addMouseMotionListener(wml);

        _buttonPanel.setOpaque(false);
        _buttonPanel.add(TitleBarButton.getButton(IconVariation.RESTORE));
        _buttonPanel.add(TitleBarButton.getButton(IconVariation.MINIMIZE));
        _buttonPanel.add(TitleBarButton.getButton(IconVariation.MAXIMIZE));
        _buttonPanel.add(TitleBarButton.getButton(IconVariation.CLOSE));

        _editorField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() instanceof JFrame) {
                    return;
                }
                if (isEditing) {
                    _label.setText(_animator.getOriginalLabelText());
                }

                isEditing = false;

                _editorField.setVisible(false);
            }
        });

        _editorField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isEditing) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ESCAPE :
                            _label.setText(_animator.getOriginalLabelText());
                            _editorField.setVisible(false);
                            isEditing = false;

                            break;

                        case KeyEvent.VK_ENTER :
                            _label.setText(_editorField.getText());
                            _editorField.setVisible(false);
                            isEditing = false;

                            break;
                    }
                }

                if (!isEditing) {
                    if ((_label.getText() == null) || "".equals(_label.getText().trim())) {
                        _label.setText(" -- ");
                    }

                    TitleBar.this.revalidate();
                    TitleBar.this.repaint();
                }
            }
        });

        _editorField.setVisible(false);

        add(_label);
        add(_editorField);
        add(Box.createHorizontalStrut(30));
        add(Box.createHorizontalGlue());
        add(_buttonPanel);

        addMouseListener(wml);
        addMouseMotionListener(wml);
    }

    //~--- set methods --------------------------------------------------------

    public void setTitleIcon(Icon icon) {
        if ((icon.getIconHeight() != DIM) || (icon.getIconWidth() != DIM)) {
            if (icon instanceof ImageIcon) {
                Image scaledImage = ((ImageIcon) icon).getImage().getScaledInstance(DIM, DIM, Image.SCALE_SMOOTH);

                icon = new ImageIcon(scaledImage);
            } else {
                BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
                                          BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();

                icon.paintIcon(new JLabel(), g, 0, 0);
                g.dispose();
                icon = new ImageIcon(image.getScaledInstance(DIM, DIM, Image.SCALE_SMOOTH));
            }
        }

        _label.setIcon(icon);
    }

    //~--- methods ------------------------------------------------------------

    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TitleBar  bar         = new TitleBar("Hello 1235");
        Container contentPane = frame.getContentPane();

        contentPane.add(bar, BorderLayout.PAGE_START);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D    g2d = (Graphics2D) g;

        GradientPaint gp  = new GradientPaint(0, getHeight() / 2, Color.WHITE, getWidth(), getHeight() / 2,
                                _COLOR_BROWN);

        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    //~--- inner classes ------------------------------------------------------

    private class Animator implements ActionListener {
        private int             index = 0;
        private final Rectangle rect  = new Rectangle();
        private final JLabel    _animateLabel;
        private String          _labelText;
        private final Timer     _timer;

        //~--- constructors ---------------------------------------------------

        private Animator() {
            _animateLabel = new JLabel();
            _timer        = new Timer(25, this);
        }

        //~--- methods --------------------------------------------------------

        public void start() {
            if (isEditing) {
                return;
            }

            TitleBar.this.add(_animateLabel);

            Rectangle labelBounds = _label.getBounds();

            rect.x      = labelBounds.x + DIM;
            rect.y      = labelBounds.y + 1;
            rect.width  = labelBounds.width - DIM;
            rect.height = labelBounds.height - 2;

            _animateLabel.setBounds(rect);
            _animateLabel.setOpaque(true);
            _animateLabel.setVisible(true);

            index = 0;
            _timer.start();
        }

        //~--- get methods ----------------------------------------------------

        public String getOriginalLabelText() {
            return _labelText;
        }

        //~--- methods --------------------------------------------------------

        @Override
        public void actionPerformed(ActionEvent e) {
            if (index >= EDITOR_TRANS_COLORS.length) {
                TitleBar.this.remove(_animateLabel);
                _editorField.setBounds(rect.x, rect.y, 100, rect.height);
                _labelText = _label.getText().trim();
                _labelText = (_labelText == null)
                             ? ""
                             : _labelText;

                Matcher mat = PATT.matcher(_labelText);

                if (mat.matches()) {
                    _editorField.setText(mat.group(1));
                } else {
                    _editorField.setText(_labelText);
                }

                _label.setText(null);
                _editorField.setVisible(true);
                _editorField.requestFocusInWindow();
                _timer.stop();
                TitleBar.this.repaint();
                isEditing = true;
            } else {
                _animateLabel.setBackground(EDITOR_TRANS_COLORS[index++]);
                TitleBar.this.repaint();
            }
        }
    }


    private static class DocumentIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Path2D.Float floatPath1 = new Path2D.Float(),
                         floatPath2 = new Path2D.Float();

            floatPath1.moveTo(x + 5, y + 0);
            floatPath1.lineTo(x + 12, y + 0);
            floatPath1.lineTo(x + 12, y + 15);
            floatPath1.lineTo(x + 1, y + 15);
            floatPath1.lineTo(x + 1, y + 5);
            floatPath1.closePath();

            floatPath2.moveTo(x + 5, y + 0);
            floatPath2.lineTo(x + 5, y + 5);
            floatPath2.lineTo(x + 1, y + 5);
            floatPath2.closePath();;

            g2d.setColor(Color.WHITE);
            g2d.fill(floatPath1);

            g2d.setColor(c.getBackground().darker());
            g2d.fill(floatPath2);

            g2d.setColor(Color.GRAY.darker());
            g2d.draw(floatPath1);
            g2d.draw(floatPath2);
        }

        //~--- get methods ----------------------------------------------------

        @Override
        public int getIconWidth() {
            return DIM;
        }

        @Override
        public int getIconHeight() {
            return DIM;
        }
    }


    class WindowMoveListener extends MouseAdapter {
        private Point  compOffset;
        private Window window;

        //~--- set methods ----------------------------------------------------

        void setWindow(MouseEvent e) {
            if (window == null) {
                window = SwingUtilities.getWindowAncestor((Component) e.getSource());
            }
        }

        //~--- methods --------------------------------------------------------

        @Override
        public void mouseDragged(MouseEvent e) {
            setWindow(e);

            if (compOffset != null) {
                window.setLocation(e.getXOnScreen() - compOffset.x, e.getYOnScreen() - compOffset.y);
                window.setOpacity(0.75f);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            setWindow(e);

            if (e.getButton() == MouseEvent.BUTTON1) {
                compOffset = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), window);

                if (e.getSource() == _label) {
                    _animator.start();
                }
            } else {
                compOffset = null;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setWindow(e);

            if (compOffset != null) {
                window.setOpacity(1.0f);
                compOffset = null;
            }
        }
    }
}
