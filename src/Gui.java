
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class Gui extends JFrame {

	private JPanel contentPane;
	private final Action action = new SwingAction();
	private final Action action_1 = new SwingAction_1();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 552, 572);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 527, 0 };
		gbl_contentPane.rowHeights = new int[] { 23, 40, 328, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btnNewButton = new JButton("Run C4.5");
		btnNewButton.setAction(action);

		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		contentPane.add(btnNewButton, gbc_btnNewButton);

		JCheckBox chckbxOutputResultsTo = new JCheckBox("Output results to file");
		chckbxOutputResultsTo.setAction(action_1);
		GridBagConstraints gbc_chckbxOutputResultsTo = new GridBagConstraints();
		gbc_chckbxOutputResultsTo.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxOutputResultsTo.gridx = 0;
		gbc_chckbxOutputResultsTo.gridy = 1;
		contentPane.add(chckbxOutputResultsTo, gbc_chckbxOutputResultsTo);

		JTextArea txtrDefault = new JTextArea();
		txtrDefault.setText("default");
		GridBagConstraints gbc_txtrDefault = new GridBagConstraints();
		gbc_txtrDefault.fill = GridBagConstraints.BOTH;
		gbc_txtrDefault.gridx = 0;
		gbc_txtrDefault.gridy = 2;
		contentPane.add(txtrDefault, gbc_txtrDefault);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Algorithm.runC45();

				List<Result> results = new ArrayList<>();
				results = Algorithm.results;
				String resultStr = "";
				for (Result result : results) {
					resultStr += (result.toString() + "\n");
				}
				resultStr += ("Average Classification Accuracy = "
						+ Algorithm.getAverageClassificationAccuracy(results));

				if (Algorithm.outputToFile) {
					resultStr += "\n Results file generated...";
				}
				txtrDefault.setText(resultStr);
			}
		});

		chckbxOutputResultsTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxOutputResultsTo.isSelected()) {
					Algorithm.outputToFile = true;
				} else {
					Algorithm.outputToFile = false;
				}
			}
		});
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "SwingAction_1");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}
}
