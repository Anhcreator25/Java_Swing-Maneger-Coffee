package ABC;
import dao.Connect_DB;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class Menu extends JPanel {
    private static final DecimalFormat FMT = new DecimalFormat("#,##0");


    private final Map<String, Integer> drinkPrice = new LinkedHashMap<>();
    private final Map<String, String> drinkCategory = new LinkedHashMap<>();
    private final Map<String, Integer> drinkID = new LinkedHashMap<>(); // Lưu MaSP



    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JComboBox<String> categoryCB;
    private JPanel gridMenu;

    private static final Color COLOR = new Color(46, 125, 110);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);
    private static final Font FONT_ITEM = new Font("SansSerif", Font.BOLD, 12);

    Connect_DB db=new Connect_DB();
    Connection con = db.connect();

    public Menu() {


        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR);

        String[] categories = {"Tất cả", "Trà Sữa", "Trà", "Cà Phê", "Khác"};
        categoryCB = new JComboBox<>(categories);
        categoryCB.setFont(new Font("SansSerif", Font.BOLD, 14));
        categoryCB.addActionListener(e -> renderMenu());
           add(categoryCB, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(COLOR);
        mainPanel.add(createMenuPanel());
        mainPanel.add(createInvoicePanel());

           add(mainPanel, BorderLayout.CENTER);

        loadMenuFromDB();
        renderMenu();
    }

    private void loadMenuFromDB() {
        try {
            ResultSet rs = db.getDB("SELECT MaSP, TenSP, DanhMuc, Gia FROM sanpham WHERE TrangThai=1");
            while (rs.next()) {
                int id = rs.getInt("MaSP");
                String ten = rs.getString("TenSP");
                String loai = rs.getString("DanhMuc");
                int gia = rs.getInt("Gia");

                drinkID.put(ten, id);
                drinkPrice.put(ten, gia);
                drinkCategory.put(ten, loai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createMenuPanel() {
        gridMenu = new JPanel(new GridLayout(0, 3, 10, 10));
        gridMenu.setBackground(COLOR);
        gridMenu.setBorder(new EmptyBorder(20, 20, 20, 20));
        JScrollPane scroll = new JScrollPane(gridMenu);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COLOR);

        JPanel container = new JPanel(new BorderLayout());
        container.add(scroll);
        return container;
    }

    private void renderMenu() {
        gridMenu.removeAll();
        String selected = categoryCB.getSelectedItem().toString();

        for (var item : drinkPrice.entrySet()) {
            String name = item.getKey();
            int price = item.getValue();
            String loai = drinkCategory.get(name);

            if (!selected.equals("Tất cả") && !selected.equals(loai)) continue;

            JButton btn = new JButton("<html><center>" + name + "<br><b>" + FMT.format(price) + "đ</b></center></html>");
            btn.setFont(FONT_ITEM);
            btn.setPreferredSize(new Dimension(140, 110));
            btn.addActionListener(e -> updateOrder(name, price));
            gridMenu.add(btn);
        }
        // thay đổi khung va ve lai khi update sp
             gridMenu.revalidate();
             gridMenu.repaint();
    }

    private JPanel createInvoicePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(new LineBorder(COLOR, 3, true), "HÓA ĐƠN", 0, 0, FONT_TITLE, COLOR));

        String[] cols = {"Tên món", "Số lượng", "Giá", "Thành tiền", "Xóa"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (table.columnAtPoint(e.getPoint()) == 4) {
                    int qty = (int) tableModel.getValueAt(row, 1);
                    if (qty > 1) {
                        qty--;
                        tableModel.setValueAt(qty, row, 1);
                        tableModel.setValueAt((int)tableModel.getValueAt(row, 2) * qty, row, 3);
                    } else {
                        tableModel.removeRow(row);
                    }
                    updateTotalMoney();
                }
            }
        });
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JTableHeader header = table.getTableHeader();
        header.setForeground(COLOR);
        header.setFont(FONT_ITEM);


        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

              JButton btnThanhToan = new JButton("Thanh Toán");
              btnThanhToan.setBackground(COLOR);
              btnThanhToan.setForeground(Color.WHITE);
              btnThanhToan.setFont(FONT_ITEM);
              btnThanhToan.addActionListener(e -> addinfordata());

              JButton btnXoa = new JButton("Xóa Hóa Đơn");
              btnXoa.setBackground(COLOR);
              btnXoa.setForeground(Color.WHITE);
              btnXoa.setFont(FONT_ITEM);
              btnXoa.addActionListener(e -> Clear_infor());

                  buttonPanel.add(btnThanhToan);
                  buttonPanel.add(btnXoa);

        totalLabel = new JLabel("Tổng tiền: 0 VND", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        totalLabel.setForeground(Color.RED);

        bottom.add(buttonPanel, BorderLayout.WEST);
        bottom.add(totalLabel, BorderLayout.EAST);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void updateOrder(String name, int price) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(name)) {
                int qty = (int) tableModel.getValueAt(i, 1) + 1;
                tableModel.setValueAt(qty, i, 1);
                tableModel.setValueAt(qty * price, i, 3);
                updateTotalMoney();
                return;
            }
        }
        tableModel.addRow(new Object[]{name, 1, price, price, "Xóa"});
        updateTotalMoney();
    }

    private void updateTotalMoney() {
        int total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            total += (int) tableModel.getValueAt(i, 3);
        }
             totalLabel.setText("Tổng tiền: " + FMT.format(total) + " VND");
    }


    private void addinfordata() {

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn trống!");
            return;
        }
        try {
            con.setAutoCommit(false);//chặn lưu tudong

            // save hoadon
            int tongTien = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                tongTien += (int) tableModel.getValueAt(i, 3);
            }
               //hoadontong
            String sqlHD = "INSERT INTO hoadon(NgayLap, TongTien) VALUES (NOW(), ?)";
            PreparedStatement psHD = con.prepareStatement(sqlHD, Statement.RETURN_GENERATED_KEYS);
            psHD.setInt(1, tongTien);
            psHD.executeUpdate();

            ResultSet rs = psHD.getGeneratedKeys();
            int maHD = 0;
            if(rs.next()) {
                maHD = rs.getInt(1);
                System.out.println("Lấy mã hóa đơn thành công: " + maHD);
            }else{
                throw new SQLException();
            }

            //hoadonchitiet
            String sqlCT = "INSERT INTO chitiethoadon(MaHD, MaSP, SoLuong, DonGia, ThanhTien) VALUES (?,?,?,?,?)";
            PreparedStatement psCT = con.prepareStatement(sqlCT);

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String tenMon = tableModel.getValueAt(i, 0).toString();
                int maSP = drinkID.get(tenMon); // Lấy mã số từ Map

                psCT.setInt(1, maHD);
                psCT.setInt(2, maSP);
                psCT.setInt(3, (int) tableModel.getValueAt(i, 1));
                psCT.setInt(4, (int) tableModel.getValueAt(i, 2));
                psCT.setInt(5, (int) tableModel.getValueAt(i, 3));
                psCT.executeUpdate();
            }
              con.commit();
            JOptionPane.showMessageDialog(this, "Thanh toán thành công Mã HĐ: " + maHD);
            Clear_infor();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi thanh toán!");
        }
    }

    private void Clear_infor() {
        tableModel.setRowCount(0);
        updateTotalMoney();
    }

    public void reloadMenu() {
        // delete dlieu cũ
        drinkID.clear();
        drinkPrice.clear();
        drinkCategory.clear();

        //load lai tu dtb
        loadMenuFromDB();
        // ve lai menu
        renderMenu();
    }
}