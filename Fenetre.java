import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.*;

public class Fenetre extends JFrame
{
    private JMenuBar menu;
    
    public Fenetre()
    {             
        this.setTitle("StarTile ep.II : the return of gelShampooing");

        GameParameters parameters = new GameParameters();
        parameters.setBorderSize(5);

        Board gameBoard = new Board(parameters);

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
        
        //this.setSize(parameters.boardSize(), parameters.boardSize() + menu.getHeight() + getInsets().top + getInsets().bottom +25 );//todo: something with prefferedSize
        pack();
    }       

}
