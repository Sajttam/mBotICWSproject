package view;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import controller.Controller;

public class MbotAddPanel extends JPanel {
	private static final String FILE_PATH = "bots.dat";

	private DefaultListModel<MBotListItem> listModel;
	private JList<MBotListItem> list;

	class MBotListItem {
		String name;
		String adress;

		MBotListItem(String name, String adress) {
			this.name = name;
			this.adress = adress;
		}

		public String toString() {
			return name;
		}
	}

	public MbotAddPanel(MainPanel parent, Controller controller, JPanel mBotIntefacePanel, JFrame frame)
			throws IOException {

		List<MBotListItem> listMBots = getBotsFromFile();
		listModel = new DefaultListModel<MBotListItem>();

		for (MBotListItem item : listMBots) {
			listModel.addElement(item);
		}

		list = new JList<MBotListItem>(listModel);

		list.addListSelectionListener(e -> {
			System.out.println(e.toString());
			if (e.getValueIsAdjusting()) {				
				String name = listModel.getElementAt(e.getLastIndex()).name;
				String adress = listModel.getElementAt(e.getLastIndex()).adress;
				parent.addBotInteface(name, adress, controller, mBotIntefacePanel, frame);
			}
		});

		add(list);
	}

	public List<MBotListItem> getBotsFromFile() throws IOException {
		List<MBotListItem> list = new ArrayList<MBotListItem>();

		InputStream in = new FileInputStream(FILE_PATH);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		while ((line = br.readLine()) != null) {
			String[] values = line.split("\t");
			list.add(new MBotListItem(values[0], values[1]));
		}

		return list;
	}
}
