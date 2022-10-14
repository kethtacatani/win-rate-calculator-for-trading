/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package winrate.calculator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keth Dominic
 */
public class WinRateCalculator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            Frame calc = new Frame();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WinRateCalculator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(WinRateCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
