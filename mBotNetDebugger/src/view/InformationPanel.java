package view;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class InformationPanel extends JPanel implements PropertyChangeListener {
	JLabel labelBotName;
	JLabel labelBotNameValue;
	JLabel labelPosition;
	JLabel labelPositionValue;
	JLabel labelState;
	JLabel labelStateValue;
	JLabel labelCont;
	JLabel labelContValue;
	JLabel labelRTT;
	JLabel labelRTTValue;
	JSlider slider;

	public InformationPanel() {
		BoxLayout boxlayout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(boxlayout);

		labelBotName = new JLabel("Botname: ");
		labelBotNameValue = new JLabel("UNKNOWN");

		labelPosition = new JLabel("Position: ");
		labelPositionValue = new JLabel("UNKNOWN");

		labelState = new JLabel("State: ");
		labelStateValue = new JLabel("UNKNOWN");

		labelCont = new JLabel("Cont: ");
		labelContValue = new JLabel("UNKNOWN");

		labelRTT = new JLabel("RTT: ");
		labelRTTValue = new JLabel("UNKNOWN");

		add(labelBotName);
		add(labelBotNameValue);
		add(labelPosition);
		add(labelPositionValue);
		add(labelState);
		add(labelStateValue);
		add(labelCont);
		add(labelContValue);
		add(labelRTT);
		add(labelRTTValue);

		for (Component c : getComponents()) {
			if (c instanceof JLabel) {
				JLabel label = (JLabel) c;
				label.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 10));
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		String str = "UNKNOWN";
		if (arg0.getNewValue() != null)
			str = arg0.getNewValue().toString();
			
		switch (arg0.getPropertyName()) {
		case "countedContinuousLines":
			labelContValue.setText(str);
			break;
		case "botName":
			labelBotNameValue.setText(str);
			break;
		case "currentPositionState":
			labelStateValue.setText(str);
			break;
		case "position":
			labelPositionValue.setText(str);
			break;
		case "roundTripTime":
			labelRTTValue.setText(str + " ms");
			break;
		default:
			System.out.println("Unknown property in information panel. property=" + arg0.getPropertyName());
			break;
		}
	}
}
