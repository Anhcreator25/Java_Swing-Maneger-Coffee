package ABC;

import dao.Connect_DB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class products extends JPanel {

    private static final Color COLOR = new Color(46, 125, 110);
    private static final Font FONT_ITEM = new Font("SansSerif", Font.BOLD, 12);
    private Menu menu;

    JTextField Code_JTF, Name_JTF, Price_JTF;
    JLabel Code_Product, Name_Product, Type_Product, Price_Product, Status_Product;
    JComboBox<String> Type_JCB, Status_JCB;
    JTable JTB_Table;
    DefaultTableModel Model;

    Connect_DB db = new Connect_DB();
    Connection con = db.connect();

    public products(Menu menu) {
        this.menu = menu;

        setLayout(new BorderLayout(10, 10));

        add(Form_fill(), BorderLayout.WEST);
        add(Infor_Table(), BorderLayout.CENTER);
        add(Button_Product(), BorderLayout.SOUTH);

        loadTable();
    }


    private JPanel Form_fill() {
        JPanel Layout = new JPanel(new GridLayout(5, 2, 5, 5));
        Layout.setBackground(COLOR);

        Layout.setPreferredSize(new Dimension(250, 0));

        Layout.setBorder(new EmptyBorder(10, 10, 10, 10));

        Code_JTF = new JTextField();
        Code_JTF.setEditable(false);// kh cho phep chỉnh sưa mã

        Name_JTF = new JTextField();

        Price_JTF = new JTextField();
        Type_JCB = new JComboBox<>(new String[]{
                "Trà Sữa", "Trà", "Cà Phê", "Khác"
        });
        Status_JCB = new JComboBox<>(new String[]{
                "Đang bán", "Ngừng Bán"
        });

        Code_Product = new JLabel("Mã Sản Phẩm");
        Code_Product.setFont(FONT_ITEM);
        Code_Product.setForeground(Color.WHITE);
        Layout.add(Code_Product);
        Layout.add(Code_JTF);

        Name_Product =new JLabel("Tên Sản Phẩm");
        Name_Product.setFont(FONT_ITEM);
        Name_Product.setForeground(Color.WHITE);
        Layout.add(Name_Product);
        Layout.add(Name_JTF);

        Type_Product =new JLabel("Loại");
        Type_Product.setFont(FONT_ITEM);
        Type_Product.setForeground(Color.WHITE);
        Layout.add(Type_Product);
        Layout.add(Type_JCB);

        Price_Product =new JLabel("Gía Thành");
        Price_Product.setFont(FONT_ITEM);
        Price_Product.setForeground(Color.WHITE);
        Layout.add(Price_Product);
        Layout.add(Price_JTF);

       Status_Product =new JLabel("Trạng Thái");
       Status_Product.setFont(FONT_ITEM);
       Status_Product.setForeground(Color.WHITE);
       Layout.add(Status_Product);
        Layout.add(Status_JCB);

        return Layout;
    }

    private JPanel Infor_Table() {
        JPanel Table = new JPanel(new BorderLayout());
        Table.setBackground(COLOR);
        Model = new DefaultTableModel(
                new String[]{"Mã", "Tên", "Danh Mục", "Giá Thành", "Trạng Thái"}, 0
        );
        Table.setBorder(BorderFactory.createLineBorder(COLOR,3));

        JTB_Table = new JTable(Model);
        JTB_Table.setRowHeight(30);

        JTB_Table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = JTB_Table.getSelectedRow();
                Code_JTF.setText(Model.getValueAt(row, 0).toString());
                Name_JTF.setText(Model.getValueAt(row, 1).toString());
                Type_JCB.setSelectedItem(Model.getValueAt(row, 2).toString());
                Price_JTF.setText(Model.getValueAt(row, 3).toString());
                Status_JCB.setSelectedItem(Model.getValueAt(row, 4).toString());
            }
        });

        Table.add(new JScrollPane(JTB_Table));
        JTableHeader header = JTB_Table.getTableHeader();
        header.setForeground(COLOR);
        header.setFont(FONT_ITEM);

        return Table;
    }

    private JPanel Button_Product() {
        JPanel Button_Panel = new JPanel();

        JButton Add_JBT = new JButton("Thêm");
        Add_JBT.setForeground(Color.WHITE);
        Add_JBT.setBackground(COLOR);
        Add_JBT.setFont(FONT_ITEM);

        JButton Edit_JBT = new JButton("Sửa");
        Edit_JBT.setBackground(COLOR);
        Edit_JBT.setForeground(Color.WHITE);
        Edit_JBT.setFont(FONT_ITEM);

        JButton Clear_JBT = new JButton("Ngừng Bán");
        Clear_JBT.setBackground(COLOR);
        Clear_JBT.setForeground(Color.WHITE);
        Clear_JBT.setFont(FONT_ITEM);

        JButton Refesh_JBT = new JButton("Làm mới");
        Refesh_JBT.setBackground(COLOR);
        Refesh_JBT.setForeground(Color.WHITE);
        Refesh_JBT.setFont(FONT_ITEM);

        Add_JBT.addActionListener(e -> ADD());
        Edit_JBT.addActionListener(e -> EDIT());
        Clear_JBT.addActionListener(e -> Stop_Sell());
        Refesh_JBT.addActionListener(e -> CLEAR());

        Button_Panel.add(Add_JBT);
        Button_Panel.add(Edit_JBT);
        Button_Panel.add(Clear_JBT);
        Button_Panel.add(Refesh_JBT);

        return Button_Panel;
    }

    void loadTable() {
        try {
            Model.setRowCount(0);


String sql="select MaSP, TenSP, DanhMuc, Gia, TrangThai from sanpham";
            ResultSet rs =db.getDB(sql);
            while (rs.next()) {
                int tt=rs.getInt("TrangThai");
                String trangthaitx=(tt==1)? "Đang bán" : "Ngừng bán";
                Model.addRow(new Object[]{
                        rs.getInt("MaSP"),
                        rs.getString("TenSP"),
                        rs.getString("DanhMuc"),
                        rs.getDouble("Gia"),
                        rs.getString("TrangThai")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


    void ADD() {
        try {


            String sql = "INSERT INTO sanpham(TenSP, DanhMuc, Gia, TrangThai) VALUES (?,?,?,1)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, Name_JTF.getText());
            ps.setString(2, Type_JCB.getSelectedItem().toString());
            ps.setDouble(3, Double.parseDouble(Price_JTF.getText()));
            ps.executeUpdate();

            loadTable();
            menu.reloadMenu();
            CLEAR();
            JOptionPane.showMessageDialog(this, "Thêm thành công");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm");
        }
    }


    void EDIT() {
        try {
            int tt = Status_JCB.getSelectedItem().toString().equals("Đang bán") ? 1 : 0;

            String sql = "UPDATE sanpham SET TenSP=?, DanhMuc=?, Gia=?, TrangThai=? WHERE MaSP=?";
            PreparedStatement ps = con.prepareStatement(sql);
           ps.setString(1, Name_JTF.getText());
           ps.setString(2, Type_JCB.getSelectedItem().toString());
           ps.setDouble(3,Double.parseDouble(Price_JTF.getText()));//chuyen doi sang kieu double
           ps.setInt(4,tt);
           ps.setInt(5,Integer.parseInt(Code_JTF.getText()));
           ps.executeUpdate();
            loadTable();
            menu.reloadMenu();
            JOptionPane.showMessageDialog(this, "Sửa thành công");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa");
        }
    }


    void Stop_Sell() {
        try {
            String sql = "UPDATE sanpham SET TrangThai=0 WHERE MaSP=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(Code_JTF.getText()));
            ps.executeUpdate();

            loadTable();
            menu.reloadMenu();
            JOptionPane.showMessageDialog(this, "Đã ngừng bán");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi ngừng bán");
        }
    }


    void CLEAR() {
        Code_JTF.setText("");
        Name_JTF.setText("");
        Price_JTF.setText("");
        Type_JCB.setSelectedIndex(0);
        Status_JCB.setSelectedIndex(0);
    }
}
