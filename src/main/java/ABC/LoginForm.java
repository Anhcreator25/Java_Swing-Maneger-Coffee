package ABC;

import dao.Connect_DB;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.formdev.flatlaf.FlatLightLaf;

public class LoginForm extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JCheckBox showPassword;

    Connect_DB db = new Connect_DB();

    public LoginForm() {
        db.connect();

        setTitle("Hệ thống Tèo Coffee");
        setSize(450, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainContainer.add(createLoginPanel(), "LoginCard");
        mainContainer.add(new Register(this), "RegisterCard");
         add(mainContainer);
    }

    private JPanel createLoginPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 239, 230));

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(320, 280));
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        mainPanel.add(card);

        JLabel lblTitle = new JLabel("TÈO COFFEE", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBounds(60, 10, 200, 30);
        card.add(lblTitle);

        JLabel lbl_title = new JLabel("Hệ thống quản lý quán café", JLabel.CENTER);
        lbl_title.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl_title.setForeground(Color.GRAY);
        lbl_title.setBounds(50, 40, 220, 20);
        card.add(lbl_title);

        Font font = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lblUser = new JLabel("Tài khoản");
        lblUser.setFont(font);
        lblUser.setBounds(30, 70, 100, 20);
        card.add(lblUser);

        txtUser = new JTextField();
        txtUser.setFont(font);
        txtUser.setBounds(30, 90, 260, 30);
        card.add(txtUser);

        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(font);
        lblPass.setBounds(30, 120, 100, 20);
        card.add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setFont(font);
        txtPass.setBounds(30, 140, 260, 30);
        card.add(txtPass);

        showPassword =new JCheckBox("Hiện mật khẩu");
        showPassword.setFont(font);
        showPassword.setBackground(Color.WHITE);
        showPassword.setBounds(30, 175, 260, 20);
        card.add(showPassword);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                txtPass.setEchoChar((char) 0);
            } else {
                txtPass.setEchoChar('*');
            }
        });

        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBounds(30, 201, 260, 32);
        btnLogin.setBackground(new Color(111, 78, 55));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        card.add(btnLogin);

        btnLogin.addActionListener(e -> checkLogin(txtUser.getText(), String.valueOf(txtPass.getPassword())));

        JLabel lblregister = new JLabel("<html>Bạn chưa có tài khoản? <u>Đăng Ký</u></html>", JLabel.CENTER);
        lblregister.setBounds(60, 230, 200, 25);
        lblregister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblregister.setForeground(new Color(111, 78, 55));
        lblregister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(lblregister);

        lblregister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showCard("RegisterCard");
            }
        });

        return mainPanel;
    }

    public void showCard(String name) {
        cardLayout.show(mainContainer, name);
    }

    public void checkLogin(String name, String pass) {
        String query = "SELECT * FROM account WHERE name = '" + name + "' AND pass = '" + pass + "'";
        ResultSet rs = db.getDB(query);
        try {
            if (rs.next()) {
                Save_logininfor.id=rs.getInt("id");
                Save_logininfor.name=rs.getString("name");
                Save_logininfor.pass=rs.getString("pass");

                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                 dispose();
                 new Manage().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
            } catch (Exception e) { e.printStackTrace(); }
                 new LoginForm().setVisible(true);
        });
    }
}