import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class StateSwitchingButton extends JButton implements MouseListener
{
	private State state_;
	private String title_;
	
	public StateSwitchingButton(String title, State state)
	{
		super(title);
		state_ = state;
	}
	
	public void mousePressed(MouseEvent e)
    {
		//~ state_.switch_matrix_state();
    }
 
    public void mouseReleased(MouseEvent e) {
 
    }
     
    public void mouseEntered(MouseEvent e) {

    }
     
    public void mouseExited(MouseEvent e) {

    }
     
    public void mouseClicked(MouseEvent e) {

    }
}
