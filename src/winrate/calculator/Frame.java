/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package winrate.calculator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.ImageIcon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;




/**
 *
 * @author Keth Dominic
 */
public class Frame extends javax.swing.JFrame {
    
    static Connection con;
    private static boolean hasData = false;
    
    
    int mouseX, mouseY;
    public static int winCount, loseCount ;
    int totalTrades;
    double winRate;
    double manWR ;
    String name;
    String nameLoad;
    String showWL, showWR, mute;
    Boolean firstLaunch = false;
    char c;
    Boolean presave = false;
    int sound;
    Statement stmt;
    ResultSet rs;
    PreparedStatement prpStmt;
    ResultSetMetaData metadata;
    int count;
    int JDSizew = 350, JDSizeh = 100 , JDLocx = 501, JDLocy = 310;
    
   
    
    
    
    /**
     * Creates new form Frame
     */
    public Frame() throws ClassNotFoundException, SQLException {
        
        initComponents();
        getConnection();
        displayData();
        preSave();

    }
    
    
    private void getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:CalcDB.db");
  
        initialize();
        
    }
    
    private void initialize() throws SQLException, ClassNotFoundException{
        if(!hasData){

            hasData = true;
            
            try{
                Statement stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("Select name FROM sqlite_master WHERE type='table' AND name='recentDB'");
                 if (!res.next()){

                     System.out.println("Creating Database");

                     Statement stmt2 = con.createStatement();
                     Statement stmt3 = con.createStatement();
                     stmt2.execute("CREATE TABLE nameDB(num INTEGER PRIMARY KEY AUTOINCREMENT, name varchar(50) UNIQUE,"
                      +"wins integer,"+"loses integer, win_rate varchar(10));");
                     System.out.println("Database 1 Created");
                     stmt3.execute("CREATE TABLE recentDB(num integer, wins integer,"+"loses integer, "
                             + "save_wl integer,save_wr varchar(10), mute varchar(50),"
                             + "JDSizew integer,JDSizeh integer,JDLocx integer,JDLocy integer);");
                     
                     firstLaunch = true;
                     System.out.println("Database 2 Created\nFirstLaunch: "+firstLaunch);
                 }
        
             }
            catch (Exception e){
                
            }
        }
    }
    public Connection getCOnnection(){
        return con;
    }
            
    public void saveData() throws ClassNotFoundException, SQLException{
        
        
        if (saveCB.isSelected()){

            if (winCount != 0 || loseCount != 0){   
            
                
                       
                           if(showWLC.isSelected()){
                               showWL = "1";
                           }
                           else{
                               showWL = "0";
                           }
                           
                           if(showWRCB.isSelected()){
                               showWR = "1";
                           }
                           else{
                               showWR = "0";
                           }
                           if(muteCB.isSelected()){
                               mute = "1";
                           }
                           else{
                               mute = "0";
                           }
                           System.out.println("Saving changes");
                           String query = ("UPDATE recentDB SET wins = '"+winCount+"', loses = '"+loseCount
                                   +"', save_wl = '"+showWL+"', save_wr = '"+showWR+"',"
                                   + "mute = '"+mute+"', JDSizew = '"+JDSizew+"',"
                                   + "JDSizeh = '"+JDSizeh+"',JDLocx = '"+JDLocx+"',JDLocy = '"+JDLocy+"' WHERE num  = 1");
                            PreparedStatement prep = con.prepareStatement(query);
                            prep.execute();
                            
                            System.out.println("Changes saved");
                       
                    
                    


                
            }else{
            }
            
            
        }
        
    }
    public void preSave() throws SQLException, SQLException{
        
        windowsBar();

        Statement stmt = con.createStatement();
                
                ResultSet rs = stmt.executeQuery("SELECT count(*) AS total FROM recentDB");
               
                    if(rs.next()){
                       int count = rs.getInt("total");

                       if (count == 0){
                           String query = "INSERT into recentDB(num, wins, loses, save_wl, save_wr, mute,"
                                   + "JDSizeh,JDSizew,JDLocx,JDLocy) VALUES"+
                            "('1','0','0','0','0','0','100','350','501','310');";
                            PreparedStatement prep = con.prepareStatement(query);
                            prep.execute();
                            
                       }
                    }
      
        if(!presave){
            presave = true;
            try{
                
               
                 
                String query = "INSERT into namesDB(num, name, wins, loses, win_rate) VALUES"+
                            "'0','haha','"+winCount+"','"+loseCount+"','"+winRate+"%')";
                con.close();

                getConnection();
               PreparedStatement prep = con.prepareStatement(query);
               prep.execute();
                }
                catch(Exception e){
            }
        }
    }
        
    
    public void saveToLoad() throws ClassNotFoundException, SQLException{

        try{
           if(String.valueOf(winRate).equals("NaN")){
               winRate = 0.00;
              
           }
            
        String query = "INSERT into nameDB(`name`, `wins`, `loses`, `win_rate`) VALUES"+
                        "('"+name+"','"+winCount+"','"+loseCount+"','"+String.valueOf(winRate)+"%')";
                        con.close();
                         
                        getConnection();
                        PreparedStatement prep = con.prepareStatement(query);
                       
                        prep.clearBatch();
                        prep.execute();

                        System.out.println("Saved to History"); 
                        
                        
        }
        catch (Exception e){
            
            JOptionPane.showMessageDialog(null, "Possible solutions:\n-Choose another file name\n-Restart the program","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println("No connection \n"+ e);
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, e);
        }
        

        
    }
    
        public void windowsBar(){
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        public void run() {
            System.out.println("In shutdown hook");
            try {
                saveMiniProp();
                saveData();
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }, "Shutdown-thread"));
        

    miniDialog.addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        System.out.println("Saving size");
        saveMiniProp();
    }
    
});
        
        }
        
        public void saveMiniProp(){
            JDSizeh = miniDialog.getSize().height;
        JDSizew = miniDialog.getSize().width;
        JDLocy = miniDialog.getLocation().y;
        JDLocx = miniDialog.getLocation().x;
        
        }
        
    
    public void displayData() throws ClassNotFoundException, SQLException{
        
      
      
        
       
            
        if (!firstLaunch){
            if (con == null){
                System.out.println("no connection");
                getConnection();
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT wins,loses,save_wl,"
                    + "save_wr,mute,JDSizeh,JDSizew,JDLocx,JDLocy FROM recentDB");
            while(rs.next()){
                System.out.println("Displaying Data");
                winTF.setText(rs.getString("wins"));
                loseTF.setText(rs.getString("loses"));
                
                winCount = Integer.parseInt(rs.getString("wins"));
                loseCount = Integer.parseInt(rs.getString("loses"));
                showWL = rs.getString("save_wl");
                showWR = rs.getString("save_wr");
                    JDSizeh = Integer.parseInt(rs.getString("JDSizeh"));
                    JDSizew = Integer.parseInt(rs.getString("JDSizew"));
                    JDLocx = Integer.parseInt(rs.getString("JDLocx"));
                    JDLocy = Integer.parseInt(rs.getString("JDLocy"));

                mute = rs.getString("mute");
                System.out.println("Data Displayed");
                
                if(showWL.equals("0")){
                    showWLC.setSelected(false);
                }
                else{
                    showWLC.setSelected(true);
                }

                if(showWR.equals("0")){
                    showWRCB.setSelected(false);
                }
                else{
                    showWRCB.setSelected(true);
                }
                if(mute.equals("0")){
                    muteCB.setSelected(false);
                }
                else{
                    muteCB.setSelected(true);
                }
   
                winRateResult();
            }
        

        
                
        }      


    }
    
    public void miniLabel(){
        if(String.valueOf(winRate).equals("NaN")){
            winRate = 0.0;
        }
        
        if(showWLC.isSelected()){
        miniWinLbl.setText("Win: "+winCount);
        miniLoseLbl.setText("Lose: "+loseCount);
        }
        else{
            miniWinLbl.setText("Win");
            miniLoseLbl.setText("Lose");
        }
        
       // jPanel1.add(miniPanel);
        if(showWRCB.isSelected()){
            miniPanel.add(wrPanel);
            wrLbl.setText("WR: "+winRate+"%");
        }
        if(!showWRCB.isSelected()){
            
            wrLbl.setText("");
            miniPanel.remove(wrPanel);
        }
    }
    
    
    public ResultSet loadData() throws ClassNotFoundException, SQLException{
        if (con == null){
            getConnection();
        }
        
        
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name FROM nameDB ORDER BY num DESC");
        return rs;
        
        
    }
    

    public ResultSet loadDataWins() throws SQLException{
        
        

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT wins FROM nameDB ORDER BY num DESC");
        
        return rs; 
    }
    
    public ResultSet loadDataLoses() throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT loses FROM nameDB ORDER BY num DESC");   
        return rs; 
    }
        
    public ResultSet loadDataWR() throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT win_rate FROM nameDB ORDER BY num DESC");   
        return rs; 
    }  
    
    
    public  void winRateResult(){
  
        totalTrades = winCount+loseCount;
        double wr = (Double.valueOf(winCount)/Double.valueOf(totalTrades))* 100;
        
        winRate = (Double.valueOf(String.format("%.2f",wr)));
        
        numTrdTF.setText(Integer.toString(totalTrades));

        
        if(showWLC.isSelected()){
            winTF.setText(Integer.toString(winCount));
            loseTF.setText(Integer.toString(loseCount));
        }
        else{
        winTF.setText("");
        loseTF.setText("");
        }
        if(showWRCB.isSelected()){
            
            if (winCount != 0 || loseCount != 0){
                wrTF.setText(String.format("%.2f",winRate)+"%");
            }
            
            
        }
        else{
            wrTF.setText("");
      }
        miniLabel();
    }
    
    
    
    public void playSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException{
        if(!muteCB.isSelected()){
            
            
            
            if(sound == 1){

                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/winrate/calculator/pics/win2.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                clip.start();
            }
            if (sound == 0){

                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/winrate/calculator/pics/fail.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                clip.start();
            }
            if (sound == 2){
                
                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/winrate/calculator/pics/minus.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                clip.start();
            }
            
        }
    }
    
    public String[][] getAllRecord(String query)
    {
               String row[][]=null;
        try
        {
            Statement stmt= con.createStatement();
            rs = stmt.executeQuery(query);
           if(rs.last())
           {
               metadata=rs.getMetaData();
               row= new String[rs.getRow()][metadata.getColumnCount()];
               rs.first();
               int r=0;
               do
               {
                 for(int col=0;col<metadata.getColumnCount();col++)
                   {
                       row[r][col]=rs.getString(col+1);
                   }
                   r++;
               }while(rs.next());
           }
        }catch(Exception e)
        {
            e.printStackTrace();
            
            
        }
        return row;

    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        miniDialog = new javax.swing.JDialog();
        miniPanel = new javax.swing.JPanel();
        winPanel = new javax.swing.JPanel();
        miniWinLbl = new javax.swing.JLabel();
        addWinBtn = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        miniLoseLbl = new javax.swing.JLabel();
        loseBtn = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        wrPanel = new javax.swing.JPanel();
        wrLbl = new javax.swing.JLabel();
        msgDialog = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgList = new javax.swing.JList<>();
        MainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        addLose = new javax.swing.JButton();
        addWin = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        showWLC = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        manualCB = new javax.swing.JCheckBox();
        wrTF = new javax.swing.JTextField();
        numTrdTF = new javax.swing.JTextField();
        winTF = new javax.swing.JTextField();
        loseTF = new javax.swing.JTextField();
        clearBtn = new javax.swing.JButton();
        showWRCB = new javax.swing.JCheckBox();
        subLose = new javax.swing.JLabel();
        subWin = new javax.swing.JLabel();
        saveCB = new javax.swing.JCheckBox();
        muteCB = new javax.swing.JCheckBox();
        jMenuBar2 = new javax.swing.JMenuBar();
        loadMenu = new javax.swing.JMenu();
        saveMenu = new javax.swing.JMenu();
        miniMenu = new javax.swing.JMenu();
        aboutBtn = new javax.swing.JMenu();
        helptBtn = new javax.swing.JMenu();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        miniDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        miniDialog.setTitle("Win Rate Calculator");
        miniDialog.setAlwaysOnTop(true);
        miniDialog.setIconImage(new ImageIcon(getClass().getResource("/winrate/calculator/pics/icons8_total_sales_25px.png")).getImage());

        miniPanel.setLayout(new java.awt.GridLayout(1, 0));

        winPanel.setLayout(new java.awt.GridBagLayout());

        miniWinLbl.setText("Win");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 11, 36);
        winPanel.add(miniWinLbl, gridBagConstraints);

        miniPanel.add(winPanel);

        addWinBtn.setText("+");
        addWinBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWinBtnActionPerformed(evt);
            }
        });
        miniPanel.add(addWinBtn);

        jButton6.setText("-");
        jButton6.setMaximumSize(new java.awt.Dimension(25, 25));
        jButton6.setMinimumSize(new java.awt.Dimension(25, 25));
        jButton6.setPreferredSize(new java.awt.Dimension(25, 25));
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        miniPanel.add(jButton6);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        miniLoseLbl.setText("Lose");
        jPanel3.add(miniLoseLbl, new java.awt.GridBagConstraints());

        miniPanel.add(jPanel3);

        loseBtn.setText("+");
        loseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loseBtnActionPerformed(evt);
            }
        });
        miniPanel.add(loseBtn);

        jButton7.setText("-");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        miniPanel.add(jButton7);

        wrPanel.setLayout(new java.awt.GridBagLayout());

        wrLbl.setText("WR");
        wrLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                wrLblMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        wrPanel.add(wrLbl, gridBagConstraints);

        miniPanel.add(wrPanel);

        javax.swing.GroupLayout miniDialogLayout = new javax.swing.GroupLayout(miniDialog.getContentPane());
        miniDialog.getContentPane().setLayout(miniDialogLayout);
        miniDialogLayout.setHorizontalGroup(
            miniDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(miniPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 290, Short.MAX_VALUE)
        );
        miniDialogLayout.setVerticalGroup(
            miniDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(miniPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(msgList);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout msgDialogLayout = new javax.swing.GroupLayout(msgDialog.getContentPane());
        msgDialog.getContentPane().setLayout(msgDialogLayout);
        msgDialogLayout.setHorizontalGroup(
            msgDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        msgDialogLayout.setVerticalGroup(
            msgDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Win Rate Calculator by sutoroberi");
        setIconImage(new ImageIcon(getClass().getResource("/winrate/calculator/pics/icons8_total_sales_25px.png")).getImage());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Win Rate Calculator");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        addLose.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        addLose.setText("+");
        addLose.setToolTipText("Add lose");
        addLose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addLose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLoseActionPerformed(evt);
            }
        });

        addWin.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        addWin.setText("+");
        addWin.setToolTipText("Add wins");
        addWin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addWin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWinActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LOSE");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("WIN");

        showWLC.setSelected(true);
        showWLC.setText("Show Wins and Loses");
        showWLC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showWLCActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Win Rate");
        jLabel4.setFocusable(false);
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Number of Trades");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Wins");
        jLabel6.setFocusable(false);
        jLabel6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Loses");
        jLabel7.setFocusable(false);
        jLabel7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        manualCB.setText("Manual Mode");
        manualCB.setToolTipText("You can also use this to calculate your win rate if you have win and lose count");
        manualCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualCBActionPerformed(evt);
            }
        });

        wrTF.setEditable(false);
        wrTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wrTFActionPerformed(evt);
            }
        });

        numTrdTF.setEditable(false);

        winTF.setEditable(false);
        winTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                winTFKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                winTFKeyTyped(evt);
            }
        });

        loseTF.setEditable(false);
        loseTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loseTFKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                loseTFKeyTyped(evt);
            }
        });

        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        showWRCB.setSelected(true);
        showWRCB.setText("Show Realtime Winrate");
        showWRCB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showWRCBMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                showWRCBMouseReleased(evt);
            }
        });
        showWRCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showWRCBActionPerformed(evt);
            }
        });

        subLose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winrate/calculator/pics/icons8_minus_25px.png"))); // NOI18N
        subLose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subLoseMouseClicked(evt);
            }
        });

        subWin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winrate/calculator/pics/icons8_minus_25px.png"))); // NOI18N
        subWin.setToolTipText("Deduct wins");
        subWin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subWinMouseClicked(evt);
            }
        });

        saveCB.setSelected(true);
        saveCB.setText("Save Inputs");
        saveCB.setToolTipText("Auto save inputs when exiting");
        saveCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCBActionPerformed(evt);
            }
        });

        muteCB.setText("Mute");
        muteCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muteCBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(addWin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(subWin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(85, 85, 85)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(addLose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(subLose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50))
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(showWLC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addComponent(showWRCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(13, 13, 13)
                .addComponent(muteCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(77, 77, 77))
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(60, 60, 60)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(40, 40, 40))
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(numTrdTF)
                .addGap(50, 50, 50)
                .addComponent(winTF)
                .addGap(20, 20, 20)
                .addComponent(loseTF)
                .addGap(40, 40, 40)
                .addComponent(wrTF)
                .addGap(40, 40, 40))
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(manualCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(151, 151, 151)
                .addComponent(saveCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(53, 53, 53))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addWin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addLose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subWin, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                            .addComponent(subLose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(20, 20, 20)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showWLC, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(showWRCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(muteCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numTrdTF, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(winTF)
                    .addComponent(loseTF)
                    .addComponent(wrTF))
                .addGap(10, 10, 10)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(manualCB, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        jMenuBar2.setBackground(new java.awt.Color(255, 255, 255));
        jMenuBar2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)));
        jMenuBar2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuBar2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jMenuBar2MouseDragged(evt);
            }
        });
        jMenuBar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuBar2MousePressed(evt);
            }
        });

        loadMenu.setBorder(null);
        loadMenu.setText("History");
        loadMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadMenuMouseClicked(evt);
            }
        });
        jMenuBar2.add(loadMenu);

        saveMenu.setBorder(null);
        saveMenu.setText("Save");
        saveMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveMenuMouseClicked(evt);
            }
        });
        jMenuBar2.add(saveMenu);

        miniMenu.setText("Mini Window");
        miniMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                miniMenuMouseClicked(evt);
            }
        });
        jMenuBar2.add(miniMenu);

        aboutBtn.setText("About");
        aboutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aboutBtnMouseClicked(evt);
            }
        });
        jMenuBar2.add(aboutBtn);

        helptBtn.setText("Help");
        helptBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                helptBtnMouseClicked(evt);
            }
        });
        jMenuBar2.add(helptBtn);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void subWinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subWinMouseClicked

            // TODO add your handling code here:
            winCount --;
            winRateResult();
            
            sound = 2;
        try {
            playSound();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_subWinMouseClicked

    private void subLoseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subLoseMouseClicked
        // TODO add your handling code here:
        loseCount --;
        winRateResult();
        
         sound = 2;
        try {
            playSound();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_subLoseMouseClicked

    private void showWRCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showWRCBActionPerformed
        // TODO add your handling code here:
        winRateResult();
        miniLabel();

    }//GEN-LAST:event_showWRCBActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // TODO add your handling code here:
        
        

        numTrdTF.setText("");
        winTF.setText("");
        loseTF.setText("");
        wrTF.setText("");

        loseCount = 0;
        winCount = 0;
        totalTrades = 0;
        winRate = 0.00;
        miniLabel();

    }//GEN-LAST:event_clearBtnActionPerformed

    private void loseTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loseTFKeyReleased
        // TODO add your handling code here:
        
        
        loseCount = Integer.parseInt(loseTF.getText());
        if(winTF.getText().equals("")){
            winCount = 0;
        }
        else{
            winCount = Integer.parseInt(winTF.getText());
        }
        
        winRateResult();
        
    }//GEN-LAST:event_loseTFKeyReleased

    private void wrTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wrTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_wrTFActionPerformed

    private void manualCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualCBActionPerformed
        
        if (manualCB.isSelected()){

            winTF.setEditable(true);
            loseTF.setEditable(true);

            addWin.setEnabled(false);
            subWin.setEnabled(false);
            addLose.setEnabled(false);
            subLose.setEnabled(false);

        }
        else{

            winTF.setEditable(false);
            loseTF.setEditable(false);

            addWin.setEnabled(true);
            subWin.setEnabled(true);
            addLose.setEnabled(true);
            subLose.setEnabled(true);
        }
    }//GEN-LAST:event_manualCBActionPerformed

    private void showWLCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showWLCActionPerformed
        // TODO add your handling code here:
        winRateResult();
        miniLabel();
    }//GEN-LAST:event_showWLCActionPerformed

    private void addWinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWinActionPerformed
        // TODO add your handling code here:
        winCount ++;
        winRateResult();
        sound = 1;
            try {

                playSound();
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addWinActionPerformed

    private void addLoseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLoseActionPerformed
        // TODO add your handling code here:
        loseCount ++;
        winRateResult();
        sound = 0;
            try {

                playSound();
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addLoseActionPerformed

    private void saveCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCBActionPerformed
        try {
            // TODO add your handling code here:
            saveData();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveCBActionPerformed

    private void jMenuBar2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuBar2MouseDragged
        // TODO add your handling code here:
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - mouseX ,y - mouseY);
    }//GEN-LAST:event_jMenuBar2MouseDragged

    private void jMenuBar2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuBar2MousePressed
        // TODO add your handling code here:
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_jMenuBar2MousePressed

    private void loadMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadMenuMouseClicked
        
        try {
            // TODO add your handling code here:
            saveData();
            LoadFrame load = new LoadFrame();
            load.setVisible(true);
            
//              LoadTable load = new LoadTable();
//              load.setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loadMenuMouseClicked

    private void winTFKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_winTFKeyTyped
        // TODO add your handling code here:
        c = evt.getKeyChar();
        if(!Character.isDigit(c)){
            evt.consume();
        }

    }//GEN-LAST:event_winTFKeyTyped

    private void winTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_winTFKeyReleased
        // TODO add your handling code here:
        winCount = Integer.parseInt(winTF.getText());
        if(loseTF.getText().equals("")){
            loseCount = 0;
        }
        else{
            loseCount = Integer.parseInt(loseTF.getText());
        }
        winRateResult();
    }//GEN-LAST:event_winTFKeyReleased

    private void loseTFKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loseTFKeyTyped
        // TODO add your handling code here:
        c = evt.getKeyChar();
        if(!Character.isDigit(c)){
            evt.consume();
        }
    }//GEN-LAST:event_loseTFKeyTyped

    private void helptBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helptBtnMouseClicked
        // TODO add your handling code here:
       // UIManager.put("OptionPane.minimumSize",new Dimension(500,500)); 
        JOptionPane.showMessageDialog(null, "-You can click mini window, note that this feature is always on top of other windows.\n"
                + "-Mini window might be located at the top left corner.\n"
                + "-To save your data to history, click the save button above\n"
                + "-If clear button is accidentally clicked, restart the program.\n\n", "Help",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_helptBtnMouseClicked

    private void saveMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMenuMouseClicked

        String userenters;

        userenters = JOptionPane.showInputDialog(null, "Enter file name", "Save as", JOptionPane.PLAIN_MESSAGE );
        name = userenters;

        if (userenters != null){

            try {
                saveToLoad();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_saveMenuMouseClicked

    private void aboutBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutBtnMouseClicked
        // TODO add your handling code here:
        AboutForm about = new AboutForm();
        about.setVisible(true);
    }//GEN-LAST:event_aboutBtnMouseClicked

    private void muteCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_muteCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_muteCBActionPerformed

    private void miniMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_miniMenuMouseClicked
        // TODO add your handling code here:
        if (JDLocx == 0 && JDLocy == 0){
            JDSizew = 350;
            JDSizeh = 100;
            JDLocx = 501; 
            JDLocy = 310;
        }
        miniDialog.setSize(JDSizew, JDSizeh);
        miniDialog.setLocation(JDLocx,JDLocy);
        miniDialog.setVisible(true);

        
        
       
        
    
                miniLabel();

        
    }//GEN-LAST:event_miniMenuMouseClicked

    private void addWinBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWinBtnActionPerformed
        // TODO add your handling code here:
        addWinActionPerformed(evt);
        
        
    }//GEN-LAST:event_addWinBtnActionPerformed

    private void loseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loseBtnActionPerformed
        addLoseActionPerformed(evt);
        
    }//GEN-LAST:event_loseBtnActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        // TODO add your handling code here:
        subWinMouseClicked(evt);
        
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        // TODO add your handling code here:
        subLoseMouseClicked(evt);
        
    }//GEN-LAST:event_jButton7MouseClicked

    private void wrLblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wrLblMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_wrLblMouseClicked

    private void showWRCBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showWRCBMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_showWRCBMouseClicked

    private void showWRCBMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showWRCBMouseReleased
        // TODO add your handling code here:
                miniLabel();

    }//GEN-LAST:event_showWRCBMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Frame frame = new Frame();
                    frame.setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainPanel;
    private javax.swing.JMenu aboutBtn;
    private javax.swing.JButton addLose;
    private javax.swing.JButton addWin;
    private javax.swing.JButton addWinBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JMenu helptBtn;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu loadMenu;
    private javax.swing.JButton loseBtn;
    private javax.swing.JTextField loseTF;
    private javax.swing.JCheckBox manualCB;
    private javax.swing.JDialog miniDialog;
    private javax.swing.JLabel miniLoseLbl;
    private javax.swing.JMenu miniMenu;
    private javax.swing.JPanel miniPanel;
    private javax.swing.JLabel miniWinLbl;
    private javax.swing.JDialog msgDialog;
    private javax.swing.JList<String> msgList;
    private javax.swing.JCheckBox muteCB;
    private javax.swing.JTextField numTrdTF;
    private javax.swing.JCheckBox saveCB;
    private javax.swing.JMenu saveMenu;
    private javax.swing.JCheckBox showWLC;
    private javax.swing.JCheckBox showWRCB;
    private javax.swing.JLabel subLose;
    private javax.swing.JLabel subWin;
    private javax.swing.JPanel winPanel;
    private javax.swing.JTextField winTF;
    private javax.swing.JLabel wrLbl;
    private javax.swing.JPanel wrPanel;
    private javax.swing.JTextField wrTF;
    // End of variables declaration//GEN-END:variables
}
