package ABC;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class Manage extends JFrame {

    private JPanel mainPanel;

    private static final String SALE = "SALES";
    private static final String PRODUCT = "PRODUCTS";
    private static final String INVOICE = "INVOICES";
    private static final String STATISTICS = "STATISTICS";
    private static final String SETTINGS = "SETTINGS";

    private Map<String, JButton> navButtons = new HashMap<>();


    // Su mau sac co ban va chinh do mau
    private static final Color COLOR = new Color(46, 125, 110);
    private static final Color COLOR_DEFAULT = Color.WHITE;
    private static final Font FONT = new Font("Segoe UI", Font.BOLD, 16);

    public Manage() {
        setTitle("TEO COFFE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener( new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Confirm_Exit();
            }
        });
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setLayout(new BorderLayout());

        // I,thanh dieu huong doc
        JPanel NavigationPanel = createNavigation();
        add(NavigationPanel, BorderLayout.WEST);

        // II, panel noi dung chinh
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        //  them cac noi dung panel
        Menu menu = new Menu();
        products sp = new products(menu);
        mainPanel.add(menu, SALE);
        mainPanel.add(sp, PRODUCT);

        mainPanel.add(new Invoice(), INVOICE);
        mainPanel.add(new statistics(), STATISTICS);
        mainPanel.add(new Setting(), SETTINGS);

        showPanel(SALE);
        updateActiveButton(SALE);

        setVisible(true);
    }

    // tao thanh dieu huong
    private JPanel createNavigation() {
        JPanel panel = new JPanel();

             panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
              panel.setBackground(COLOR);
               panel.setPreferredSize(new Dimension(200, 0));




        // 1. Logo
        ImageIcon icon = new ImageIcon(
                getClass().getClassLoader().getResource("coffee-shop.png"));
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JLabel logo = new JLabel("TÈO COFFEE", icon, JLabel.CENTER);
        logo.setHorizontalTextPosition(JLabel.CENTER);//can ngang so vs icon
        logo.setVerticalTextPosition(JLabel.BOTTOM);//can duoi so voi icon
        logo.setIconTextGap(10);//k/c giua chung
        logo.setFont(new Font("\"Montserrat\"", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(90, 0, 50, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logo);

        // 2. create nut
        createNavButton("BÁN HÀNG", SALE, panel);
        createNavButton("SẢN PHẨM", PRODUCT, panel);
        createNavButton("HÓA ĐƠN", INVOICE, panel);
        createNavButton("THỐNG KÊ", STATISTICS, panel);
        createNavButton("CÀI ĐẶT", SETTINGS, panel);


        return panel;
    }
    //tao nut dieu huong
    private void createNavButton(String text, String panelname, JPanel container) {
        JButton button = new JButton(text);
        button.setFont(FONT);
        button.setForeground(COLOR_DEFAULT);
        button.setBackground(COLOR);
        button.setFocusPainted(false);


        button.setMaximumSize(new Dimension(180, 60));
        button.setMinimumSize(new Dimension(180, 60));
        button.setPreferredSize(new Dimension(180, 60));

        // Can giua nut so voi panel chua no
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.CENTER);



        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(panelname);
                updateActiveButton(panelname);
            }
        });

        // khoang cach giua cac nut
        container.add(Box.createRigidArea(new Dimension(0, 25)));
        container.add(button);

        navButtons.put(panelname, button);
    }

    // cap nhat mau sac cho nut dang chon
    private void updateActiveButton(String activePanelName) {

        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            JButton button = entry.getValue();
            button.setBackground(COLOR);
            button.setForeground(COLOR_DEFAULT);

        }
        JButton activeButton = navButtons.get(activePanelName);
        if (activeButton != null) {
            activeButton.setBackground(Color.WHITE);
            activeButton.setForeground(COLOR);
        }
    }
    // chuyen doi panel
    private void showPanel(String panelname) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, panelname);
    }

    private void Confirm_Exit(){
        int Result=JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn thoát hệ thống không?",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE

        );
        if(Result==JOptionPane.YES_OPTION){
            this.dispose();

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Manage();
        });
    }
}