package ABC;

import dao.Connect_DB;

import org.jfree.chart.ChartPanel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import org.jfree.chart.ChartFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import  java.time.LocalDate;
public class statistics extends JPanel {

    private JComboBox<String> Type_JCB;

    private JTextField Day_JTF, Month_JTF, Year_JTF;
    private JButton Stat_JBT;
    private  JLabel jlb_Type, jlb_Day, jlb_Month, jlb_Year;
    private JLabel jlb_Revenue, jlb_numberInvoice, jlb_Product, jlb_MonthRevenue;

    private JTable Table;
    private DefaultTableModel  model;
    private JPanel panelChart;

    private static final Font FONT_ITEM = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_Title = new Font("SansSerif", Font.BOLD, 17);
    private static final Color COLOR = new Color(55, 111, 109);

    Connect_DB db=new Connect_DB();


    public statistics() {
              db.connect();

        // Layout chinh
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR);

        JPanel mainpanel = new JPanel(new BorderLayout(10, 10));
        mainpanel.add(filterPanel(), BorderLayout.NORTH);

        JPanel centerpanel = new JPanel(new BorderLayout(10, 10));
        centerpanel.add(SummaryPanel(), BorderLayout.NORTH);
        centerpanel.add(chartPanel(),BorderLayout.CENTER);

        mainpanel.add(centerpanel, BorderLayout.CENTER);
        mainpanel.add(TablePanel(),BorderLayout.SOUTH);
        add(mainpanel, BorderLayout.CENTER);

        Data_default();
    }

    private JPanel filterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(COLOR);

        Type_JCB = new JComboBox<>(new String[]{
                "Theo ngày",
                "Theo tháng",
                "Theo năm"
        });
        Day_JTF = new JTextField(8);
        Month_JTF = new JTextField(8);
        Year_JTF = new JTextField(8);

        jlb_Type = new JLabel("Loại");
        jlb_Type.setFont(FONT_Title);
        jlb_Type.setForeground(Color.WHITE);
        panel.add(jlb_Type);
        panel.add(Type_JCB);

        jlb_Day = new JLabel("Ngày");
        jlb_Day.setFont(FONT_Title);
        jlb_Day.setForeground(Color.WHITE);
        panel.add(jlb_Day);
        panel.add(Day_JTF);

        jlb_Month =new JLabel("Tháng");
        jlb_Month.setFont(FONT_Title);
        jlb_Month.setForeground(Color.WHITE);
        panel.add(jlb_Month);
        panel.add(Month_JTF);

        jlb_Year =new JLabel("Năm");
        jlb_Year.setFont(FONT_Title);
        jlb_Year.setForeground(Color.WHITE);
        panel.add(jlb_Year);
        panel.add(Year_JTF);

        Stat_JBT =new JButton("Thống Kê");
        Stat_JBT.setBackground(COLOR);
        Stat_JBT.setForeground(Color.white);
        Stat_JBT.setFont(FONT_ITEM);
        panel.add(Stat_JBT);

        Stat_JBT.addActionListener(e -> STAT_DATA());
        return panel;
    }

    private void STAT_DATA() {
        panelChart.removeAll();
        model.setRowCount(0);
        String loai= Type_JCB.getSelectedItem().toString().trim();

      if(loai.equals("Theo ngày")){
          Stat_Day();
          loadTbale_Day();

          panelChart.add(
                  PieChart_overtime(
                          "DAY(h.NgayLap)=" + Day_JTF.getText().trim() +
                                  " AND MONTH(h.NgayLap)=" + Month_JTF.getText().trim() +
                                  " AND YEAR(h.NgayLap)=" + Year_JTF.getText().trim()

                  )
          );
      }else{
          if(loai.equals("Theo tháng")) {
           Stat_Month();
           loadTable_Month();
           panelChart.add(ColumnChart_Month());
           panelChart.add(
                   PieChart_overtime(
                           "MONTH(h.NgayLap)="+ Month_JTF.getText().trim()+
                                   " AND YEAR(h.NgayLap)="+ Year_JTF.getText().trim()
                   )
           );

          }else{
              Stat_Year();
              loadTable_Years();
              panelChart.add(ColumnChart_Years());
              panelChart.add(
                      PieChart_overtime(
                              "YEAR(h.NgayLap)="+ Year_JTF.getText()
                      )
              );
          }
          }
panelChart.revalidate();
panelChart.repaint();
      }

    private  JPanel SummaryPanel() {
        JPanel panel1 = new JPanel(new GridLayout(1,4,15,15));
        panel1.setBackground(COLOR);
        panel1.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        jlb_Revenue =new JLabel("0VND");
        jlb_numberInvoice =new JLabel("0");
        jlb_Product =new JLabel("0");
        jlb_MonthRevenue =new JLabel("0VND");

        panel1.add(createCard("DOANH THU", jlb_Revenue));
        panel1.add(createCard("SỐ HÓA ĐƠN", jlb_numberInvoice));
        panel1.add(createCard("SẢN PHẨM BÁN RA", jlb_Product));
        panel1.add(createCard("DOANH THU THÁNG", jlb_MonthRevenue));

        return panel1;
    }

    private JPanel createCard(String title, JLabel valueLabel) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));

        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(new Color(200, 80, 80));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

      private void Stat_Day() {
        String day= Day_JTF.getText();
        String month= Month_JTF.getText();
        String year= Year_JTF.getText();
        String sql=
          "SELECT COUNT(MaHD) AS soHD, " +
                  "SUM(TongTien) AS doanhthu " +
                  "FROM hoadon " +
                  "WHERE DAY(NgayLap)=" + day +
                  " AND MONTH(NgayLap)=" + month +
                  " AND YEAR(NgayLap)=" + year;
        try {
               ResultSet rs=db.getDB(sql);
               if(rs.next()){
                   jlb_numberInvoice.setText(rs.getString("soHD"));
                   jlb_Revenue.setText(rs.getString("doanhthu"));
               }

            String sqlSP =
                    "SELECT SUM(ct.SoLuong) AS soSP " +
                            "FROM chitiethoadon ct " +
                            "JOIN hoadon h ON ct.MaHD = h.MaHD " +
                            "WHERE DAY(h.NgayLap)=" + day +
                            " AND MONTH(h.NgayLap)=" + month +
                            " AND YEAR(h.NgayLap)=" + year;

            ResultSet rsSP = db.getDB(sqlSP);

            if (rsSP.next()) {
                jlb_Product.setText(rsSP.getString("soSP"));
            } else {
                jlb_Product.setText("0");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
      }
    private void Stat_Month() {

        String Month = Month_JTF.getText().trim();
        String Year   = Year_JTF.getText().trim();

        if (Month.isEmpty() || Year.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tháng và năm");
            return;
        }

        try {

            String sqlHD =
                    "SELECT COUNT(MaHD) AS soHD, " +
                            "SUM(TongTien) AS doanhthu " +
                            "FROM hoadon " +
                            "WHERE MONTH(NgayLap)=" + Month +
                            " AND YEAR(NgayLap)=" + Year;

            ResultSet rsHD = db.getDB(sqlHD);
            if (rsHD.next()) {
                jlb_numberInvoice.setText(rsHD.getString("soHD"));
                jlb_Revenue.setText(rsHD.getString("doanhthu"));
                jlb_MonthRevenue.setText(rsHD.getString("doanhthu"));
            }

            String sqlSP =
                    "SELECT SUM(ct.SoLuong) AS soSP " +
                            "FROM chitiethoadon ct " +
                            "JOIN hoadon h ON ct.MaHD = h.MaHD " +
                            "WHERE MONTH(h.NgayLap)=" + Month +
                            " AND YEAR(h.NgayLap)=" + Year;

            ResultSet rsSP = db.getDB(sqlSP);
            if (rsSP.next()) {
                jlb_Product.setText(rsSP.getString("soSP"));
            } else {
                jlb_Product.setText("0");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Stat_Year() {

        String Year = Year_JTF.getText().trim();

        if (Year.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập năm");
            return;
        }

        try {
          
            String sqlHD =
                    "SELECT COUNT(MaHD) AS soHD, " +
                            "SUM(TongTien) AS doanhthu " +
                            "FROM hoadon " +
                            "WHERE YEAR(NgayLap)=" + Year;

            ResultSet rsHD = db.getDB(sqlHD);
            if (rsHD.next()) {
                jlb_numberInvoice.setText(rsHD.getString("soHD"));
                jlb_Revenue.setText(rsHD.getString("doanhthu"));
            }

            String sqlSP =
                    "SELECT SUM(ct.SoLuong) AS soSP " +
                            "FROM chitiethoadon ct " +
                            "JOIN hoadon h ON ct.MaHD = h.MaHD " +
                            "WHERE YEAR(h.NgayLap)=" + Year;

            ResultSet rsSP = db.getDB(sqlSP);
            if (rsSP.next()) {
                jlb_Product.setText(rsSP.getString("soSP"));
            } else {
                jlb_Product.setText("0");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTbale_Day() {
        model.setRowCount(0);

        String Day  = Day_JTF.getText().trim();
        String Month = Month_JTF.getText().trim();
        String Years   = Year_JTF.getText().trim();

        String sql =
                "SELECT MaHD, NgayLap, TongTien " +
                        "FROM hoadon " +
                        "WHERE DAY(NgayLap) = " + Day +
                        " AND MONTH(NgayLap) = " + Month +
                        " AND YEAR(NgayLap) = " + Years +
                        " ORDER BY NgayLap";

        try {
            ResultSet rs = db.getDB(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getDate("NgayLap"),
                        rs.getString("MaHD"),
                        rs.getLong("TongTien")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadTable_Month() {
        model.setRowCount(0);

        String Month = Month_JTF.getText().trim();
        String Years   = Year_JTF.getText().trim();

        String sql =
                "SELECT DAY(NgayLap) AS ngay, " +
                        "COUNT(MaHD) AS soHD, " +
                        "SUM(TongTien) AS doanhthu " +
                        "FROM hoadon " +
                        "WHERE MONTH(NgayLap) = " + Month +
                        " AND YEAR(NgayLap) = " + Years +
                        " GROUP BY DAY(NgayLap) " +
                        " ORDER BY ngay";

        try {
            ResultSet rs = db.getDB(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ngay"),
                        rs.getInt("soHD"),
                        rs.getLong("doanhthu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadTable_Years() {
        model.setRowCount(0);

        String year = Year_JTF.getText().trim();

        String sql =
                "SELECT MONTH(NgayLap) AS thang, " +
                        "COUNT(MaHD) AS soHD, " +
                        "SUM(TongTien) AS doanhthu " +
                        "FROM hoadon " +
                        "WHERE YEAR(NgayLap) = " + year +
                        " GROUP BY MONTH(NgayLap) " +
                        " ORDER BY thang";

        try {
            ResultSet rs = db.getDB(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                        "Tháng " + rs.getInt("thang"),
                        rs.getInt("soHD"),
                        rs.getLong("doanhthu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel chartPanel() {

         panelChart = new JPanel(new  GridLayout(1,2,15,0));
         panelChart.setBorder(BorderFactory.createTitledBorder(new  LineBorder(COLOR, 3, true), "BIỂU ĐỒ",0,0,FONT_Title, COLOR));

         return panelChart;
    }
    private ChartPanel ColumnChart_Month() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String thang= Month_JTF.getText().trim();
        String nam= Year_JTF.getText().trim();
        String sqlBD="SELECT DAY(NgayLap) AS ngay, SUM(TongTien) AS doanhthu " +
                "FROM hoadon " +
                "WHERE MONTH(NgayLap)=" + thang +
                " AND YEAR(NgayLap)=" + nam +
                " GROUP BY DAY(NgayLap) " +
                " ORDER BY ngay";
        try {
            ResultSet rs = db.getDB(sqlBD);
            while (rs.next()){
                dataset.addValue(rs.getLong("doanhthu"),
                        "Doanh Thu",rs.getString("ngay"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        JFreeChart chart=ChartFactory.createBarChart(
                "Doanh thu từng ngày trong tháng"+thang,
                null,
                "VND",
                dataset
        );
        //doi mau cot
        CategoryPlot plot=chart.getCategoryPlot();
        BarRenderer renderer=(BarRenderer)plot.getRenderer();
        renderer.setSeriesPaint(0,new Color(55, 111, 90, 255));

        //doi mau nen
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);
        return new ChartPanel(chart);
    }
    private ChartPanel ColumnChart_Years() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String nam = Year_JTF.getText().trim();

        String sql =
                "SELECT MONTH(NgayLap) AS thang, SUM(TongTien) AS doanhthu " +
                        "FROM hoadon " +
                        "WHERE YEAR(NgayLap)=" + nam +
                        " GROUP BY MONTH(NgayLap)";

        try {
            ResultSet rs = db.getDB(sql);
            while (rs.next()) {
                dataset.addValue(
                        rs.getLong("doanhthu"),
                        "Doanh thu",
                        "Tháng " + rs.getInt("thang")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Doanh thu từng tháng năm " + nam,
                null,
                "VNĐ",
                dataset
        );
        CategoryPlot plot1=chart.getCategoryPlot();
        BarRenderer renderer1=(BarRenderer)plot1.getRenderer();
        renderer1.setSeriesPaint(0,new Color(111, 55, 55, 255));

        //doi mau nen
        plot1.setBackgroundPaint(Color.WHITE);
        plot1.setRangeGridlinePaint(Color.BLACK);
        return new ChartPanel(chart);
    }

    private ChartPanel PieChart_overtime(String whereSQL) {
       //nguon du lieu
        DefaultPieDataset dataset = new DefaultPieDataset();

        String sql =
                "SELECT sp.TenSP, SUM(ct.SoLuong) AS soluong " +
                        "FROM chitiethoadon ct " +
                        "JOIN sanpham sp ON ct.MaSP = sp.MaSP " +
                        "JOIN hoadon h ON ct.MaHD = h.MaHD " +
                        "WHERE " + whereSQL +
                        " GROUP BY sp.TenSP";

        try {
            ResultSet rs = db.getDB(sql);
            while (rs.next()) {
                dataset.setValue(
                        rs.getString("TenSP"),
                        rs.getInt("soluong")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Tỉ lệ sản phẩm bán ra",
                dataset,
                true,
                true,
                false
        );

        return new ChartPanel(chart);
    }
    private void Data_default(){
       java.time.LocalDate Today = LocalDate.now();
           Day_JTF.setText(String.valueOf(Today.getDayOfMonth()));
           Month_JTF.setText(String.valueOf(Today.getMonthValue()));
           Year_JTF.setText(String.valueOf(Today.getYear()));

            Type_JCB.setSelectedIndex(0);
             STAT_DATA();
    }

    private JPanel TablePanel() {
        JPanel panel3 = new JPanel(new  BorderLayout());
          panel3.setPreferredSize(new Dimension(0, 250));
          panel3.setBorder(BorderFactory.createTitledBorder(new LineBorder(COLOR, 3, true), "HÓA ĐƠN", 0, 0, FONT_Title, COLOR));
          String []colum={"Ngày","Hóa Đơn","Doanh thu"};

        model=new DefaultTableModel(colum,0);
         Table =new  JTable(model);
         Table.setRowHeight(25);

         JTableHeader header1 = Table.getTableHeader();
           header1.setForeground(COLOR);
           header1.setFont(FONT_ITEM);
           panel3.add(new  JScrollPane(Table),BorderLayout.CENTER);
            return panel3;
    }
    }