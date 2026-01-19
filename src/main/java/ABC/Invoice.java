package ABC;
import dao.Connect_DB;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.BorderLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;


public class Invoice extends JPanel {

    private JPanel BigPanel;
    private JButton Search_JBT, Refesh_JBT;
    private JLabel Totalmoney_JLB, Discount_JLB, TotalAmount_JLB;
    private  JButton Print_JBT, Cancel_JBT;
    private DefaultTableModel Model, Model_LTC;
    private JTable Table, Table_CT;
    private JTextField JTF_Search;


    private static final Font FONT_ITEM = new Font("SansSerif", Font.BOLD, 12);
    private static final Color COLOR = new Color(55, 111, 109);

        Connect_DB db=new Connect_DB();

    public Invoice() {

        db.connect();
        //panel chinh
        setLayout(new BorderLayout());

        BigPanel = new JPanel(new BorderLayout(10, 10));

            BigPanel.add(PanelNorth(), BorderLayout.NORTH);
            BigPanel.add(PanelCenter(), BorderLayout.CENTER);
            BigPanel.add(PanelSouth(), BorderLayout.SOUTH);

             add(BigPanel, BorderLayout.CENTER);

             Load_Invoice();
             addEvent();

    }

    private JPanel PanelNorth() {
        JPanel Panel_1 = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
              Panel_1.setBackground(COLOR);

        JLabel JLB_Mahoadon = new JLabel("Mã HĐ");
        JLB_Mahoadon.setFont(FONT_ITEM);
        JLB_Mahoadon.setForeground(Color.WHITE);


        JTextField maHD_JTF = new JTextField(20);
        JTF_Search = maHD_JTF;

        Search_JBT = new JButton("Tìm Kiếm");
        Search_JBT.setFont(FONT_ITEM);
        Search_JBT.setForeground(Color.WHITE);
        Search_JBT.setBackground(COLOR);

        Refesh_JBT = new JButton(" Cập Nhật");
        Refesh_JBT.setFont(FONT_ITEM);
        Refesh_JBT.setForeground( Color.WHITE);
        Refesh_JBT.setBackground(COLOR);

        Panel_1.add(JLB_Mahoadon);
        Panel_1.add(maHD_JTF);
        Panel_1.add(Search_JBT);
        Panel_1.add(Refesh_JBT);

        return Panel_1;

    }

    private JPanel PanelCenter() {
        JPanel Panel_2 = new JPanel(new BorderLayout(10,10));
        Panel_2.setBackground(COLOR);
        //panel trai
        JPanel Panel_Left = new JPanel(new BorderLayout());

           String[] colHD={"Mã HĐ", "Ngày Tạo","Tổng Tiền"};
             Model =new DefaultTableModel(colHD,0);
             Table =new JTable(Model);
        Table.setRowHeight(28);
        JTableHeader header = Table.getTableHeader();
        header.setForeground(COLOR);
        header.setFont(FONT_ITEM);

        //gan nut click chuot
        Table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row= Table.getSelectedRow();
                if(row==-1)return;
                int MaHD=Integer.parseInt(Table.getValueAt(row,0).toString());

                Load_detailInvoice(MaHD);

          //hienthi
                int tong = Caculate_Totalmoney();
                int giam = 0;
                int thanh = tong - giam;

                Totalmoney_JLB.setText("Tổng tiền: " + tong);
                Discount_JLB.setText("Giảm giá: " + giam);
                TotalAmount_JLB.setText("Thành tiền: " + thanh);
            }
        });

        Panel_Left.add(new JScrollPane(Table), BorderLayout.CENTER);
                Panel_Left.setPreferredSize(new Dimension(400, 0));
        Panel_Left.setBorder(BorderFactory.createTitledBorder(new LineBorder(COLOR,3,true),"DANH SÁCH HÓA ĐƠN ",0,0,FONT_ITEM,COLOR));

                  Panel_2.add(Panel_Left,BorderLayout.WEST);


        //panel phai
        JPanel Panel_Right = new JPanel(new BorderLayout());

         String[]colhoadonct={"Mã HĐ","Tên Món","Số Lượng","Đơn Giá","Thành Tiền"};
           Model_LTC =new DefaultTableModel(colhoadonct,0);
           Table_CT =new JTable(Model_LTC);
           Table_CT.setRowHeight(28);
        JTableHeader header1 = Table_CT.getTableHeader();
        header1.setForeground(COLOR);
        header1.setFont(FONT_ITEM);

                   Panel_Right.add(new JScrollPane(Table_CT), BorderLayout.CENTER);
                      Panel_Right.setBorder(BorderFactory.createTitledBorder(new LineBorder(COLOR,3,true),"HÓA ĐƠN CHI TIẾT ",0,0,FONT_ITEM,COLOR));


                    Panel_2.add(Panel_Right,BorderLayout.CENTER);

        return Panel_2;
    }

    private JPanel PanelSouth() {
        JPanel Panel_3 = new JPanel(new BorderLayout(10,10));
        Panel_3.setBackground(COLOR);

// panel tien
        JPanel Panel_money = new JPanel(new GridLayout(3,2,10,10));
        Panel_money.setBackground(COLOR);

        Totalmoney_JLB = new JLabel("Tổng tiền:");
        Totalmoney_JLB.setFont(FONT_ITEM);
        Totalmoney_JLB.setForeground(Color.WHITE);


        Discount_JLB = new JLabel("Giảm Giá:");
        Discount_JLB.setFont(FONT_ITEM);
        Discount_JLB.setForeground(Color.WHITE);


        TotalAmount_JLB = new JLabel("Thành Tiền:");
        TotalAmount_JLB.setFont(FONT_ITEM);
        TotalAmount_JLB.setForeground(Color.WHITE);


        Panel_money.add(Totalmoney_JLB);
        Panel_money.add(Discount_JLB);
        Panel_money.add(TotalAmount_JLB);


          Panel_3.add(Panel_money, BorderLayout.WEST);

   //panel nut
        JPanel Panel_Button=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Panel_Button.setBackground(COLOR);

        Print_JBT = new JButton("In Hóa Đơn");
        Print_JBT.setFont(FONT_ITEM);
        Print_JBT.setBackground(COLOR);
        Print_JBT.setForeground(Color.WHITE);

        Cancel_JBT = new JButton("Hủy Đơn");
        Cancel_JBT.setFont(FONT_ITEM);
        Cancel_JBT.setBackground(COLOR);
        Cancel_JBT.setForeground(Color.WHITE);

            Panel_Button.add(Print_JBT);
            Panel_Button.add(Cancel_JBT);

                Panel_3.add(Panel_Button,BorderLayout.EAST);

        return Panel_3;
    }
//hoadontong
    private void Load_Invoice() {

        Model.setRowCount(0);

     try{
          ResultSet rs= db.getDB("select MaHD,NgayLap,TongTien from hoadon");

            while(rs.next()){
                Object[]row={
                        rs.getInt("MaHD"),
                        rs.getTimestamp("NgayLap"),
                        rs.getInt("TongTien")
                };
                Model.addRow(row);
      }

    }
     catch(Exception e){
         e.printStackTrace();
     }
    }

    //hoadonchitiet
    public void Load_detailInvoice(int maHD) {

        Model_LTC.setRowCount(0);

        try {
            ResultSet rs = db.getDB("SELECT ct.MaHD, sp.TenSP, ct.SoLuong, ct.DonGia, ct.ThanhTien " +
                    "FROM chitiethoadon ct " +
                    "JOIN sanpham sp ON ct.MaSP = sp.MaSP " +
                    "WHERE ct.MaHD = " + maHD);
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("MaHD"),
                        rs.getString("TenSP"),
                        rs.getInt("SoLuong"),
                        rs.getInt("DonGia"),
                        rs.getInt("ThanhTien")
                };
                Model_LTC.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int Caculate_Totalmoney() {
        int tong = 0;
        for (int i = 0; i < Model_LTC.getRowCount(); i++) {
            tong += Integer.parseInt(
                    Model_LTC.getValueAt(i, 4).toString()
            );
        }
        return tong;
    }


    private void addEvent() {
        //timkiem
     Search_JBT.addActionListener(e->{
         String ma= JTF_Search.getText().trim();
         if(ma.isEmpty()){
             JOptionPane.showMessageDialog(this,"Vui lòng nhập mã hóa đơn");
             return;
         }
         Model.setRowCount(0);
         String query2="select MaHD,NgayLap,TongTien from hoadon where MaHD="+ma;
         try{
             ResultSet rs = db.getDB(query2);
             while(rs.next()){
                 Object[] row={
                         rs.getInt("MaHD"),
                         rs.getTimestamp("NgayLap"),
                         rs.getInt("TongTien")
                 };
                 Model.addRow(row);
             }
         }catch (Exception ex){
             ex.printStackTrace();
         }
     });
//capnhat
     Refesh_JBT.addActionListener(e->{
         JTF_Search.setText("");
         Model.setRowCount(0);
         Model_LTC.setRowCount(0);
         Load_Invoice();

         Totalmoney_JLB.setText("Tổng tiền:");
         Discount_JLB.setText("Giảm giá:");
         TotalAmount_JLB.setText("Thành tiền:");
     });

        Print_JBT.addActionListener(e -> Print_Invoice_pdf());
        Cancel_JBT.addActionListener(e -> Cancel_Invoice());
    }

    //in hoa don
    private void Print_Invoice_pdf() {
        try {
            int row = Table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn hóa đơn trước khi in");
                return;
            }

            int maHD = Integer.parseInt(Table.getValueAt(row, 0).toString());

            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontVN = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream("HoaDon_" + maHD + ".pdf"));

            document.open();

            document.add(new com.itextpdf.text.Phrase("HÓA ĐƠN",fontVN));
            document.add(new Paragraph(" "));
            document.add(new com.itextpdf.text.Phrase("Mã hóa đơn: " + maHD,fontVN));
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.addCell(new com.itextpdf.text.Phrase("Sản Phẩm",fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Số lượng",fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Đơn giá",fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Thành tiền",fontVN));

            ResultSet rs = db.getDB("select sp.TenSP, hdct.SoLuong, hdct.DonGia, hdct.ThanhTien " +
                    "from chitiethoadon hdct " +
                    "join sanpham sp on hdct.MaSP = sp.MaSP " +
                    "where hdct.MaHD = " + maHD);
            int tong = 0;
            while (rs.next()) {
                pdfTable.addCell(new com.itextpdf.text.Phrase(rs.getString("TenSP"), fontVN));
                pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf(rs.getInt("SoLuong")), fontVN));
                pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf(rs.getInt("DonGia")), fontVN));
                pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf(rs.getInt("ThanhTien")), fontVN));
                tong += rs.getInt("ThanhTien");
                System.out.println(rs.getString("TenSP"));
            }

            document.add(pdfTable);
            document.add(new com.itextpdf.text.Phrase("TỔNG TIỀN: " + tong,fontVN));

            document.close();

            JOptionPane.showMessageDialog(this, "Da tao hoa don PDF");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //xoahoadon
    private void Cancel_Invoice() {
        int row= Table.getSelectedRow();
        if(row==-1){
            JOptionPane.showMessageDialog(this,"Bạn chưa chọn hóa đơn");
            return;
        }
        int maHd=Integer.parseInt(Table.getValueAt(row, 0).toString());
        int confim=JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc hủy hóa đơn"+maHd+"?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if(confim!=JOptionPane.YES_OPTION){
            return;
        }
        try{
            db.executeDB("delete from chitiethoadon where MaHd="+maHd);
            db.executeDB("delete from hoadon where MaHD="+maHd);

            JOptionPane.showMessageDialog(this,"Đã hủy thành công");
                Model_LTC.setRowCount(0);
                Load_Invoice();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

