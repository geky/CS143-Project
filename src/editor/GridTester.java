package editor;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import math.Vector3f;
import actor.Actor;


public class GridTester {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GridTester window = new GridTester();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GridTester() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setLayout(null);
		
		GridPacker grids = new GridPacker();
		frame.add(grids);
		
        Map<Actor,Color> actormap = new HashMap<Actor,Color>();
        
        Actor ship = new actor.ship.types.Fighter();
        ship.setPosition(new Vector3f(50,20,-20));
        ship.setSize(10);
        actormap.put(ship,Color.RED);
        
        Actor ship2 = new actor.ship.types.Fighter();
        ship2.setPosition(new Vector3f(10,-5,20));
        ship2.setSize(10);
        actormap.put(ship2,Color.BLUE);
        
        grids.updateActors(actormap);
		
		frame.setBounds(300, 300, 900, 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
