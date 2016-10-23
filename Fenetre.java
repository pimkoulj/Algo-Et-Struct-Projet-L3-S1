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
        parameters.setTileSize(50);

        Board gameBoard = new Board(parameters);

        this.setLocationRelativeTo(null);  
        this.setResizable(false);        
        this.getContentPane().setBackground(Color.black); 
            

        //Instanciation d'un objet JPanel;
	
        //On prévient notre JFrame que notre JPanel sera son content pane
        //~ this.setContentPane(background); 
          
        //~ this.setContentPane(gameBoard);  
		Container pane = this;
            
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menu = new JMenuBar();
        Dimension button_size = new Dimension(200,70);
        JButton b1 = new JButton("ColorerCase");
        b1.setPreferredSize(button_size);
        JButton b2 = new JButton("afficheComposante");
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
        JButton b8 = new JButton("joueDeuxHumains");
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
        //~ layout.setAutoCreateGaps(true);
        //~ layout.setAutoCreateContainerGaps(true);
        /*GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
			.addComponent(gameBoard)
			.addGroup(layout.createParallelGroup(LEADING)
				.addComponent(b1)
				.addComponent(b2)
				.addComponent(b3)));
			
			  
        layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(LEADING)
				.addComponent(gameBoard)
				.addGroup(layout.createSequentialGroup()
					.addComponent(b1)
					.addComponent(b2)
					.addComponent(b3))));
        */
        //this.setSize(parameters.boardSize(), parameters.boardSize() + menu.getHeight() + getInsets().top + getInsets().bottom +25 );//todo: something with prefferedSize
        pack();
        this.setVisible(true);
    }       

}
