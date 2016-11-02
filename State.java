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
	public abstract void mousePressed(MouseEvent e);
	public void reset(){}
	public abstract void paintComponent(Graphics g);
}

class JoueDeuxHumains extends State
{
	public JoueDeuxHumains(Board target)
	{
		super(target);
	}
    public void mousePressed(MouseEvent e)
    {
        Color c = target_.turnColor(); /////*******
        int x = e.getX() / (params_.tileSize() + params_.borderSize());
        int y = e.getY() / (params_.tileSize() + params_.borderSize());
        
        if(target_.valid_coordinates(x,y) && target_.get_tile(x, y).isEmpty())
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
				&& target_.valid_coordinates(x,y)
				&& target_.get_tile(x, y).isEmpty())
				{
					Color c = target_.turnColor();
                    g.setColor(new Color(Math.min(c.getRed() + 100, 255) ,Math.min(c.getGreen() + 100, 255), Math.min(c.getBlue() + 100,255), 255));
				}
                target_.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize(), g);
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
    public void mousePressed(MouseEvent e)
    {
		Object[] options = {"Red",
							"Blue"};
		int n = JOptionPane.showOptionDialog(target_,
		"What color would you like to color this tile ?",
		"Question",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,
		options,
		options[0]);
        Color c = n == 0 ? Color.RED : Color.BLUE; /////*******
        int x = e.getX() / (params_.tileSize() + params_.borderSize());
        int y = e.getY() / (params_.tileSize() + params_.borderSize());
        
        if(target_.valid_coordinates(x,y) && target_.get_tile(x, y).isEmpty())
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
				&& target_.valid_coordinates(x,y)
				&& target_.get_tile(x, y).isEmpty())
				{
                    g.setColor(new Color(200,200,200,100));
				}
                target_.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize(), g);
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
    public void mousePressed(MouseEvent e)
    {
		x = e.getX() / (params_.tileSize() + params_.borderSize());
		y = e.getY() / (params_.tileSize() + params_.borderSize());
    }
	public void paintComponent(Graphics g)
	{
        g.setColor(Color.BLACK);
        Color greyed = new Color(0,0,0,80);
        target_.fillRect(0,0, 10000, 10000, g);
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
				if(target_.valid_coordinates(x,y) && !target_.get_tile(i,j).memeClasse(target_.get_tile(x,y)))
				{
					g.setColor(greyed);
					target_.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize(), g);
				}
			}
		}
	}
}
