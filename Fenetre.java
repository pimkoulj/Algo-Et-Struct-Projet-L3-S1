import java.awt.Color; 

import javax.swing.JFrame;

import javax.swing.*;

 

public class Fenetre extends JFrame {
	
	JMenuBar menu;

  public Fenetre(){             

    this.setTitle("Ma première fenêtre Java");

    this.setSize(Constante.MATRIX_SIZE * Constante.TILE_SIZE + 2 * Constante.MATRIX_SIZE, Constante.MATRIX_SIZE * Constante.TILE_SIZE + 5 * Constante.MATRIX_SIZE);

    this.setLocationRelativeTo(null);               

 

    //Instanciation d'un objet JPanel;
	
    Matrix matrix = new Matrix();
    //On prévient notre JFrame que notre JPanel sera son content pane
    //~ this.setContentPane(background);   
    this.setContentPane(matrix);  
            
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    
    menu = new JMenuBar();
    JButton b1 = new JButton("Bonjour");
    menu.add(b1);
    setJMenuBar(menu);
  }       

}
