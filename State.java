import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Color; 
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.util.Random;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.lang.Math.*;

abstract class State
{
    protected Board target_;
    protected GameParameters params_;
	
    public State(Board target)
    {
        target_ = target;
        params_ = target.params();
    }
	
    public void	switch_to_state()
    {
        reset();
        target_.set_etat_courant(this);
    }
    public abstract void process_event(MouseEvent e);
    public void reset(){}
    public abstract void paintComponent(Graphics g);
}

class JoueDeuxHumains extends State
{
    public JoueDeuxHumains(Board target)
    {
        super(target);
    }
    public void process_event(MouseEvent e)
    {
        Coordinate click_pos = target_.getCoordinate(e);
        
        if(target_.coordinates_are_valid(click_pos) && target_.tile(click_pos).isEmpty())
        {
            target_.tile(click_pos).setColor(target_.turnColor());
            if(target_.nbEtoiles(click_pos) == params_.starCardinal())
            {
                JOptionPane.showMessageDialog(
                    target_, 
                    (target_.tile(click_pos).getColor() == Color.RED ? "Rouge a gagné !" : "Bleu a gagné !"),
                    " Fin du game",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            target_.next_turn();
        }
    }
    
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, 10000, 10000);
        int x = 0;
        int y = 0;
        final Point mousePos = target_.getMousePosition();
		
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
        {
            for(int j = 0 ; j < params_.matrixSize() ; ++j)
            {
                g.setColor(target_.get_tile(i, j).getColor());
                if(mousePos != null
                   && ((x = mousePos.x / params_.comprehensiveTileSize()) == i)
                   && ((y = mousePos.y / params_.comprehensiveTileSize()) == j)
                   && target_.coordinates_are_valid(x,y)
                   && target_.get_tile(x, y).isEmpty())
                {
                    Color c = target_.turnColor();
                    g.setColor(new Color(Math.min(c.getRed() + 100, 255) ,Math.min(c.getGreen() + 100, 255), Math.min(c.getBlue() + 100,255), 255));
                }
                g.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize());
                if(target_.get_tile(i,j).isStarTile())
                {
                    g.setColor(Color.BLACK);
                    g.drawString("*",i * params_.comprehensiveTileSize() + params_.tileSize() / 3, j*params_.comprehensiveTileSize() + params_.tileSize() / 1);
                }
            }
        }
    }
}
	

class ColorerCase extends State
{
    public ColorerCase(Board target)
    {
        super(target);
    }
    public void process_event(MouseEvent e)
    {
        Object[] options = {"Red","Blue"};
        int n = JOptionPane.showOptionDialog(
            target_,
            "selectionnez couleur ?",
            "Question",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        Color c = n == 0 ? Color.RED : Color.BLUE; /////*******
        int x = e.getX() / (params_.tileSize() + params_.borderSize());
        int y = e.getY() / (params_.tileSize() + params_.borderSize());
        
        if(target_.coordinates_are_valid(x,y) && target_.get_tile(x, y).isEmpty())
        {
            target_.set_tile_color(x,y,c);
            if(target_.nbEtoiles(x,y) == params_.starCardinal())
            {
                JOptionPane.showMessageDialog(target_, 
                                              (target_.get_tile(x,y).getColor() == Color.RED ? "Rouge a gagné !" : "Bleu a gagné !"),
                                              " Fin du game",
                                              JOptionPane.INFORMATION_MESSAGE);
            }
            target_.next_turn();
        }
    }
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        target_.fillRect(0,0, 10000, 10000, g);
        int x = 0;
        int y = 0;
        final Point mousePos = target_.getMousePosition();
        Font font = new Font("Serif", Font.BOLD, params_.tileSize() );
        g.setFont(font);
		
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
        {
            for(int j = 0 ; j < params_.matrixSize() ; ++j)
            {
                g.setColor(target_.get_tile(i, j).getColor());
                if(mousePos != null
                   && ((x = mousePos.x / params_.comprehensiveTileSize()) == i)
                   && ((y = mousePos.y / params_.comprehensiveTileSize()) == j)
                   && target_.coordinates_are_valid(x,y)
                   && target_.get_tile(x, y).isEmpty())
                {
                    g.setColor(new Color(200,200,200,100));
                }
                g.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize());
                if(target_.get_tile(i,j).isStarTile())
                {
                    g.setColor(Color.BLACK);
                    g.drawString("*",i * params_.comprehensiveTileSize() + params_.tileSize() / 3, j*params_.comprehensiveTileSize() + params_.tileSize() / 1);
                }
            }
        }
    }
}


class AfficheComposante extends State
{
    private int x;
    private int y;
    public AfficheComposante(Board target)
    {
        super(target);
        x =  -1;
        y =  -1;
    }
    public void reset()
    {
        x = -1;
        y = -1;
    }
    public void process_event(MouseEvent e)
    {
        x = e.getX() / (params_.tileSize() + params_.borderSize());
        y = e.getY() / (params_.tileSize() + params_.borderSize());
    }
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        Color greyed = new Color(0,0,0,180);
        g.fillRect(0,0, 10000, 10000);
        final Point mousePos = target_.getMousePosition();
        Font font = new Font("Serif", Font.BOLD, params_.tileSize() );
        g.setFont(font);
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
        {
            for(int j = 0 ; j < params_.matrixSize() ; ++j)
            {
                g.setColor(target_.get_tile(i, j).getColor());
                target_.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize(), g);
                if(target_.get_tile(i,j).isStarTile())
                {
                    g.setColor(Color.BLACK);
                    g.drawString("*",i * params_.comprehensiveTileSize() + params_.tileSize() / 3, j*params_.comprehensiveTileSize() + params_.tileSize() / 1);
                }
                if(target_.coordinates_are_valid(x,y) && !target_.get_tile(i,j).memeClasse(target_.get_tile(x,y)))
                {
                    g.setColor(greyed);
                    target_.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize(), g);
                }
            }
        }
    }
}

class RelierCasesMin extends State
{
    private Coordinate origine_;
    private Coordinate destination_;
    private int clicked_;
    private int[][] tab_;
	
    public RelierCasesMin(Board target)
    {
        super(target);
        clicked_ = 0;
        tab_ = new int[params_.matrixSize()][params_.matrixSize()];
    }
	
    public void reset()
    {
        clicked_ = 0;
    }
    public void process_event(MouseEvent e)
    {
        if(clicked_ == 0)
        {
            origine_ = target_.getCoordinate(e);
            if(!target_.tile(origine_).isEmpty())
                ++clicked_;
        }
        else if(clicked_ == 1)
        {
            destination_ = target_.getCoordinate(e);
            if(target_.tile(origine_).getColor() == target_.tile(destination_).getColor())
            {
                find_path();
                clicked_ = 2;
            }
            else if(target_.tile(origine_).isEmpty())
                clicked_ = 0;
			
        }
    }
    
    private void find_path()
    {
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
        {
            for(int j = 0 ; i < params_.matrixSize() ; ++j)
            {

            }
        }
    }
    
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        Color greyed = new Color(0,0,0,180);
        g.fillRect(0,0, 10000, 10000);
        final Point mousePos = target_.getMousePosition();
        Font font = new Font("Serif", Font.BOLD, params_.tileSize() );
        g.setFont(font);
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
        {
            for(int j = 0 ; j < params_.matrixSize() ; ++j)
            {
                
            }
        }
    }
}
