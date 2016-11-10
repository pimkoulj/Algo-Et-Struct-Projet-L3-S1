import java.awt.*;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.Component;
import static javax.swing.GroupLayout.Alignment.*;
import java.awt.Dimension;

public class Fenetre extends JFrame
{
    private JMenuBar menu;
    
    public Fenetre()
    {             
        this.setTitle("StarTile ep.II : the return of gelShampooing");

        GameParameters parameters = new GameParameters();
        parameters.setBorderSize(5);
        parameters.setStarCardinal(3);
        parameters.setTileSize(60);

        Board gameBoard = new Board(parameters);

        this.setLocationRelativeTo(null);  
        this.setResizable(false);        
        this.getContentPane().setBackground(Color.black); 
            

		Container pane = this;
            
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int nb_buttons = 8;
        menu = new JMenuBar();
        Dimension button_size = new Dimension(200,parameters.boardSize()/nb_buttons);
        StateSwitchingButton b1 = new StateSwitchingButton("ColorerCase", new ColorerCase(gameBoard));
        b1.setPreferredSize(button_size);
        JButton b2 = new StateSwitchingButton("AfficheComposante", new AfficheComposante(gameBoard));
        b2.setPreferredSize(button_size);
        JButton b3 = new JButton("existeCheminCases");
        b3.setPreferredSize(button_size);
        JButton b4 = new JButton("relierCasesMin");
        b4.setPreferredSize(button_size);
        JButton b5 = new JButton("nombreEtoiles");
        b5.setPreferredSize(button_size);
        JButton b6 = new JButton("afficheScores");
        b6.setPreferredSize(button_size);
        JButton b7 = new JButton("relierComposantes");
        b7.setPreferredSize(button_size);
         StateSwitchingButton b8 = new  StateSwitchingButton("joueDeuxHumains", new JoueDeuxHumains(gameBoard));
        b8.setPreferredSize(button_size);
        //~ setJMenuBar(menu);
        
        GridBagLayout layout = new GridBagLayout();
        getContentPane().setLayout(layout);
        
        GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight =  10;
		pane.add(gameBoard, c);
		
		c.gridheight =  1;
		c.gridwidth = 1;
		c.gridx = 4;
		c.gridy = 0;
		
		pane.add(b1, c);
		c.gridy = 1;
		
		pane.add(b2, c);
		c.gridy = 2;
		
		pane.add(b3, c);
		c.gridy = 3;
		
		pane.add(b4, c);
		c.gridy = 4;
		
		pane.add(b5, c);
		
		c.gridy = 5;
		pane.add(b6, c);
		
		c.gridy = 6;
		pane.add(b7, c);
		
		c.gridy = 7;
		pane.add(b8, c);

        pack();
        this.setVisible(true);
    }       

}
