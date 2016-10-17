import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.*;

public class Fenetre extends JFrame
{
    private JMenuBar menu;
    
    public Fenetre()
    {             
        this.setTitle("StarTile ep.II : the return of the gelShampooing");

        Board gameBoard = new Board();
        this.setSize(gameBoard.params().matrixSize() * gameBoard.params().tileSize() + 2 * gameBoard.params().matrixSize(), gameBoard.params().matrixSize() * gameBoard.params().tileSize() + 5 * gameBoard.params().matrixSize());

        this.setLocationRelativeTo(null);               

        //Instanciation d'un objet JPanel;
	
        //On prévient notre JFrame que notre JPanel sera son content pane
        //~ this.setContentPane(background);   
        this.setContentPane(gameBoard);  
            
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    
        menu = new JMenuBar();
        JButton b1 = new JButton("Bonjour");
        menu.add(b1);
        setJMenuBar(menu);
    }       

}
