package drok.missilecommand;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

public class LevelCreator extends JFrame {
	private static final long serialVersionUID = 1L;

	public LevelCreator() {	
		setTitle("Level Creator");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);	
		Object[] options = {"Planet", "Level/Moon"};
		int answer = JOptionPane.showOptionDialog(this,
		"Planet or moon?",
		"Level creator",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,
		options,
		options[1]);
		if(answer == 0) {
			add(getPlanetPanel());
		} else {
			add(getLevelPanel());
		}
		pack();
	}
	
	private JPanel getPlanetPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 90));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		final JTextField jtf = new JTextField("Name");
		panel.add(jtf);
		
		final JSpinner jspx = new JSpinner();
		jspx.setValue((int) (Math.random() * 2048));
		panel.add(jspx);
		
		final JSpinner jspy = new JSpinner();
		jspy.setValue((int) (Math.random() * 2048));
		panel.add(jspy);
		
		JButton createBtn = new JButton("Create!");
		createBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent ae) {
				FileOutputStream fos = null;
				try {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("name", jtf.getText());
					jsonObj.put("x", jspx.getValue());
					jsonObj.put("y", jspy.getValue());
					fos = new FileOutputStream("res/data/planets/" + jtf.getText() + ".misscommplanet");
					fos.write(jsonObj.toJSONString().getBytes());
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		panel.add(createBtn);
		return panel;
	}
	
	private JPanel getLevelPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 400));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		final JTextField name = new JTextField("Name");
		panel.add(name);
		final JTextField planetName = new JTextField("Planet name");
		panel.add(planetName);
		
		final JTextField debrisName = new JTextField("Debris");
		panel.add(debrisName);
		
		final JSpinner debrisAmount = new JSpinner();
		debrisAmount.setValue(1);
		panel.add(debrisAmount);
		
		final JList list = new JList();
		list.setModel(new DefaultListModel());
		
		JButton addBtn = new JButton("Add debris");
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				((DefaultListModel) list.getModel()).addElement(debrisName.getText() + " " + debrisAmount.getValue());
			}
		});
		panel.add(addBtn);

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(300, 200));
		panel.add(scrollPane);
		
		JButton removeBtn = new JButton("Remove selected");
		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(list.getSelectedIndex() != -1) {
					((DefaultListModel) list.getModel()).remove(list.getSelectedIndex());
				}
			}
		});
		panel.add(removeBtn);
		
		JButton createBtn = new JButton("Create!");
		createBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("name", name.getText());
					jsonObj.put("planetName", planetName.getText());
					
					Map<String, Integer> map = new HashMap<String, Integer>();
					for(int i = 0; i < list.getModel().getSize(); i++) {
						String str = (String) list.getModel().getElementAt(i);
						String[] strArr = str.split(" ");
						map.put(strArr[0], Integer.valueOf(strArr[1]));
					}
					jsonObj.put("spawnables", map);
					jsonObj.put("spawnTime", new Integer(500));
					jsonObj.put("missiles", new Integer(20));
					
					FileOutputStream fos = new FileOutputStream("res/data/levels/" + name.getText() + ".misscommlvl");
					fos.write(jsonObj.toJSONString().getBytes());
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		panel.add(createBtn);
		
		return panel;
	}
	
	public static void main(String[] args) throws Exception {
		new LevelCreator();
	}
}
