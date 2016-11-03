import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;

class StateSwitchingButton extends JButton
{
	private State state_;
	private String title_;
	
	public StateSwitchingButton(String title, State state)
	{
		super(title);
		state_ = state;
		addActionListener(new ActionListener()
		{		
			public void actionPerformed(ActionEvent e)
			{
				state_.switch_to_state();
			}
		});
	}
	
	//~ public void actionPerformed(ActionEvent e)
    //~ {
		//~ state_.switch_to_state();
		//~ 
    //~ }
 

}
