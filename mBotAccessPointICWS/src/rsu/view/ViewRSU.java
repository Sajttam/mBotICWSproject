package rsu.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import mV2IL.model.TraficLightStates;
import rsu.algorithms.QueueAlg;
import rsu.algorithms.TrafficLightAlg;
import rsu.server.VehicleInIntersection;

public class ViewRSU extends JPanel implements PropertyChangeListener {

	class QueuePanel extends JPanel {
		private JLabel labelHeading;
		private DefaultListModel<String> listCars;

		QueuePanel(String heading, Boolean isActive) {
			setLayout(new BorderLayout());
			labelHeading = new JLabel();
			updateHeading(heading, isActive);
			listCars = new DefaultListModel<String>();
			JList<String> list = new JList<String>(listCars);

			add(labelHeading, BorderLayout.NORTH);
			add(list, BorderLayout.CENTER);
		}

		void addListItem(String itemName) {
			listCars.addElement(itemName);
			add(new JList<String>(listCars), BorderLayout.CENTER);
		}

		void removeListItem(String itemName) {
			listCars.removeElement(itemName);
			add(new JList<String>(listCars), BorderLayout.CENTER);
		}

		void updateElement(String itemName) {
			if (!listCars.contains(itemName)) {
				System.out.println("(view)1: Adding to queue");
				addListItem(itemName);
			} else {
				removeListItem(itemName);
				System.out.println("(view)2: Removing queue");
			}

		}

		void updateHeading(String text, Boolean isActive) {
			if (isActive) {
				labelHeading.setText(text + "(OPEN)");
				setBackground(Color.GREEN);
			} else {
				labelHeading.setText(text + "(CLOSED)");
				setBackground(Color.RED);
			}
		}
	}

	private QueuePanel queueSouthNorth;
	private QueuePanel queueWestEast;

	private static final String SN_TEXT = "South & North";
	private static final String WE_TEXT = "West & East";

	public ViewRSU() {
		queueSouthNorth = new QueuePanel(SN_TEXT, true);
		queueWestEast = new QueuePanel(WE_TEXT, false);

		setLayout(new BorderLayout());

		add(queueSouthNorth, BorderLayout.WEST);
		add(queueWestEast, BorderLayout.EAST);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName().equals(TrafficLightAlg.EVENT_NAME)) {
			TraficLightStates tl = (TraficLightStates) arg0.getNewValue();
			switch (tl) {
			case ALL_RED:
				queueSouthNorth.updateHeading(SN_TEXT, false);
				queueWestEast.updateHeading(WE_TEXT, false);
				break;
			case EAST_WEST_GREEN:
				queueSouthNorth.updateHeading(SN_TEXT, false);
				queueWestEast.updateHeading(WE_TEXT, true);
				break;
			case SOUTH_NORTH_GREEN:
				queueSouthNorth.updateHeading(SN_TEXT, true);
				queueWestEast.updateHeading(WE_TEXT, false);
				break;
			default:
				break;
			}
		} else if (arg0.getPropertyName().equals(QueueAlg.EVENT_NAME)) {
			VehicleInIntersection car;
			
			if (arg0.getNewValue() == null) {
				car = (VehicleInIntersection) arg0.getOldValue();
			}
			else {
				car = (VehicleInIntersection) arg0.getNewValue();
			}
			
			
			switch (car.getPositionState()) {
			case POSITION_STATE_ENTER_EAST:
			case POSITION_STATE_ENTER_WEST:
				queueWestEast.updateElement(car.getName());
				repaint();
				break;
			case POSITION_STATE_ENTER_NORTH:
			case POSITION_STATE_ENTER_SOUTH:
				queueSouthNorth.updateElement(car.getName());
				repaint();
				break;
			default:
				break;
			}
		}
	}
}
