package ABC;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import dao.Connect_DB;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class Setting extends JPanel {
    private static final Font FONT_Title = new Font("SansSerif", Font.BOLD, 17);
    private static final Font FONT = new Font("SansSerif", Font.BOLD, 15);
    private static final Color COLOR = new Color(55, 111, 109);
    private JPanel Main_panel;
    //Phan quan li tai khoan
    private JLabel ID_User, Name_Login, Position_Work;
    private JTextField ID_JTF, Name_JTF, Position_JTF;
    private JLabel jlb_Pass1, jlb_Pass2, jlb_Pass3;
    private JPasswordField JPF_Pass1, JPF_Pass2, JPF_Pass3;
    private JButton Update_Pass;
    private JLabel Mode_UI;
    private JRadioButton Light_Mode, Dark_Mode;

     Connect_DB db=new Connect_DB();
     Connection con=db.connect();

    public Setting() {

        setLayout(new BorderLayout(10,10));
        setBackground(COLOR);
        Main_panel = new JPanel(new  BorderLayout());
        Main_panel.add(Account_manager(),BorderLayout.NORTH);
        Main_panel.add(Edit_UI(),BorderLayout.CENTER);
        add(Main_panel,BorderLayout.CENTER);

        Load_data();

    }
    private JPanel Account_manager(){
        JPanel Panelmain=new JPanel(new GridLayout(1,2));
            Panelmain.setPreferredSize(new Dimension(80,500));
            Panelmain.setBorder(BorderFactory.createTitledBorder(new LineBorder(COLOR,3,true),"Quản lí tài khoản ",0,0,FONT_Title,COLOR));

            // phan tai khoan ben trai
             JPanel panel_Account=new JPanel();
                panel_Account.setLayout(new BoxLayout(panel_Account,BoxLayout.Y_AXIS));//can doc
                panel_Account.setBorder(BorderFactory.createEmptyBorder(50,20,50,20));

              Dimension sizeLbl=new Dimension(150,20);

        JPanel Row_id=new JPanel(new FlowLayout(FlowLayout.LEFT));
              ID_User =new JLabel("ID người dùng:");
              ID_User.setPreferredSize(sizeLbl);
              ID_User.setFont(FONT);
              ID_User.setForeground(COLOR);

              ID_JTF =new JTextField();
              ID_JTF.setPreferredSize(new Dimension(150,30));
              ID_JTF.setEditable(false);
                Row_id.add(ID_User);
                Row_id.add(ID_JTF);

        JPanel Row_Name=new JPanel(new FlowLayout(FlowLayout.LEFT));
              Name_Login =new JLabel("Tên đăng nhập:");
              Name_Login.setPreferredSize(sizeLbl);
              Name_Login.setFont(FONT);
              Name_Login.setForeground(COLOR);

              Name_JTF =new JTextField();
              Name_JTF.setPreferredSize(new Dimension(150,30));
              Name_JTF.setEditable(false);
                Row_Name.add(Name_Login);
                Row_Name.add(Name_JTF);

        JPanel Row_Position=new JPanel(new FlowLayout(FlowLayout.LEFT));
               Position_Work =new JLabel("Chức vụ:");
               Position_Work.setPreferredSize(sizeLbl);
               Position_Work.setFont(FONT);
               Position_Work.setForeground(COLOR);

               Position_JTF =new JTextField("    Người quản lí");
               Position_JTF.setPreferredSize(new Dimension(150,30));
               Position_JTF.setEditable(false);
                 Row_Position.add(Position_Work);
                 Row_Position.add(Position_JTF);

                 panel_Account.add(Row_id);
                panel_Account.add(Row_Name);
                panel_Account.add(Row_Position);
                Panelmain.add(panel_Account);

                 //phan mat khau ben phai
                JPanel Panel_MK=new JPanel();
                Panel_MK.setLayout(new BoxLayout(Panel_MK,BoxLayout.Y_AXIS));
                Panel_MK.setBorder(BorderFactory.createEmptyBorder(50,20,50,20));

                JPanel Row_pass1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                 jlb_Pass1 =new JLabel("Mật khẩu hiện tại:");
                 jlb_Pass1.setPreferredSize(sizeLbl);
                 jlb_Pass1.setFont(FONT);
                 jlb_Pass1.setForeground(COLOR);

                 JPF_Pass1 =new JPasswordField();
                 JPF_Pass1.setPreferredSize(new Dimension(180,30));
                 JPF_Pass1.putClientProperty(FlatClientProperties.STYLE, "" + "showRevealButton:true;");// button hien thi mk
                  Row_pass1.add(jlb_Pass1);
                  Row_pass1.add(JPF_Pass1);

                JPanel Row_pass2=new JPanel(new FlowLayout(FlowLayout.LEFT));
                jlb_Pass2 =new JLabel("Mật khẩu mới:");
                jlb_Pass2.setPreferredSize(sizeLbl);
                jlb_Pass2.setFont(FONT);
                jlb_Pass2.setForeground(COLOR);

                JPF_Pass2 =new JPasswordField();
                JPF_Pass2.setPreferredSize(new Dimension(180,30));
                JPF_Pass2.putClientProperty(FlatClientProperties.STYLE, ""+"showRevealButton:true;");
                  Row_pass2.add(jlb_Pass2);
                  Row_pass2.add(JPF_Pass2);

               JPanel Row_pass3=new JPanel(new FlowLayout(FlowLayout.LEFT));
               jlb_Pass3 =new JLabel("Xác nhận mật khẩu:");
               jlb_Pass3.setPreferredSize(sizeLbl);
               jlb_Pass3.setFont(FONT);
               jlb_Pass3.setForeground(COLOR);

               JPF_Pass3 =new JPasswordField();
               JPF_Pass3.setPreferredSize(new Dimension(180,30));
               JPF_Pass3.putClientProperty(FlatClientProperties.STYLE, ""+"showRevealButton:true;");
                 Row_pass3.add(jlb_Pass3);
                 Row_pass3.add(JPF_Pass3);

                JPanel Updata_password=new JPanel(new FlowLayout(FlowLayout.LEFT));
               Update_Pass =new JButton("CẬP NHẬT ");
               Update_Pass.setPreferredSize(new Dimension(180,30));
               Update_Pass.setFont(FONT);
               Update_Pass.setForeground(Color.WHITE);
               Update_Pass.setBackground(COLOR);
                 Updata_password.add(Box.createHorizontalStrut((int) sizeLbl.getWidth()));//tao mot khoang trong bang voi dong tren
                    Update_Pass.addActionListener(e->{
                     String Pass_current= JPF_Pass1.getText();
                     String Pass_new= JPF_Pass2.getText();
                     String Pass_confirm= JPF_Pass3.getText();

                     if(Pass_new.isEmpty()||Pass_confirm.isEmpty()){
                         JOptionPane.showMessageDialog(this,"Vui lòng nhập mật khẩu mới");
                     }
                     if(Pass_current.equals(Pass_new)){
                         JOptionPane.showMessageDialog(this," Mật khẩu đã tồn tại");
                         return;
                     }
                     if(!Pass_new.equals(Pass_confirm)){
                         JOptionPane.showMessageDialog(this,"Mật khẩu xác nhận không khớp");
                     }
                        try {
                            String sql = "UPDATE account SET pass =? WHERE id = ?";
                            PreparedStatement ps=con.prepareStatement(sql);
                            ps.setString(1,Pass_new);
                            ps.setInt(2,Save_logininfor.id);
                            int Result=ps.executeUpdate();
                            if (Result > 0) {
                                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                                Save_logininfor.pass=Pass_new;
                                JPF_Pass2.setText("");
                                JPF_Pass3.setText("");
                                JPF_Pass1.setText(Pass_new);
                            } else {
                                JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                            }
                        }catch (Exception E){
                            E.printStackTrace();
                        }
                    });


                 Updata_password.add(Update_Pass);

                 Panel_MK.add(Row_pass1);
                 Panel_MK.add(Row_pass2);
                 Panel_MK.add(Row_pass3);
                 Panel_MK.add(Updata_password);

                 Panelmain.add(Panel_MK);


          return Panelmain;
    }
    private void Load_data(){

        ID_JTF.setText(Save_logininfor.id + "");
        Name_JTF.setText(Save_logininfor.name);
        JPF_Pass1.setText(Save_logininfor.pass);
    }
    private JPanel Edit_UI(){
        JPanel Panle_Main1=new JPanel();
              Panle_Main1.setLayout(new BoxLayout(Panle_Main1,BoxLayout.Y_AXIS));
              Panle_Main1.setPreferredSize(new Dimension(80,260));
              Panle_Main1.setBorder(BorderFactory.createTitledBorder(new LineBorder(COLOR,3,true),"Điều chỉnh  ",0,0,FONT_Title,COLOR));

       JPanel Panel_1=new JPanel(new FlowLayout(FlowLayout.LEFT,20,20));
              Panel_1.setPreferredSize(new Dimension(80,200));
           Mode_UI =new JLabel("Chế độ giao diện:");
           Mode_UI.setFont(FONT);
           Mode_UI.setForeground(COLOR);
              Panel_1.add(Mode_UI);

           Light_Mode = new JRadioButton("Sáng");
           Light_Mode.setFont(FONT);
           Light_Mode.setForeground(COLOR);
           Light_Mode.setSelected(true);
              Panel_1.add(Light_Mode);

           Dark_Mode =new JRadioButton("Tối");
           Dark_Mode.setFont(FONT);
           Dark_Mode.setForeground(COLOR);
              Panel_1.add(Dark_Mode);
           Panle_Main1.add(Panel_1);

            ButtonGroup buttonGroup=new ButtonGroup();
            buttonGroup.add(Light_Mode);
            buttonGroup.add(Dark_Mode);

            Light_Mode.addActionListener(e->{
                try{
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    Update_Color();
                }catch(Exception E){
                    E.printStackTrace();
                }
            });
            Dark_Mode.addActionListener(e->{
                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    Update_Color();
                }catch(Exception E){
                    E.printStackTrace();
                }
            });
          JPanel Panel_2=new JPanel(new FlowLayout(FlowLayout.LEFT,20,20));
            JButton Log_outJBT=new JButton("ĐĂNG XUẤT");
              Log_outJBT.setFont(FONT);
              Log_outJBT.setForeground(Color.white);
              Log_outJBT.setBackground(COLOR);
                   Panel_2.add(Log_outJBT);
                   Panle_Main1.add(Panel_2);

              Log_outJBT.addActionListener(e->{
                  int Confirm=JOptionPane.showConfirmDialog(null,"Bạn có chắc chắn muốn thoát");
                   if(Confirm==JOptionPane.YES_OPTION){
                      //tim cua so chua nút này và đóng nó
                      Window window=SwingUtilities.getWindowAncestor(Log_outJBT);
                      if(window!=null){
                          window.dispose();
                      }
                      new LoginForm().setVisible(true);
                  }
              });
           return Panle_Main1;
    }
    //cap nhat color
     private void Update_Color(){
       for(Window w:Window.getWindows()){
           SwingUtilities.updateComponentTreeUI(w);
       }
     }

}