
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MyGui extends JFrame {

	private JPanel contentPane;
	private final Action action = new SwingAction();
	private final Action action_1 = new SwingAction_1();
	private Algorithm algorithm = new Algorithm();
	private JTextField txtPathToFile;
	public static JTextArea txtrDefault;
	public static boolean fileNotFound = false;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyGui frame = new MyGui();
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
	public MyGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 505, 572);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 527, 0 };
		gbl_contentPane.rowHeights = new int[] { 23, 40, 0, 328, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btnRunC = new JButton("Run C4.5");
		GridBagConstraints gbc_btnRunC = new GridBagConstraints();
		gbc_btnRunC.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRunC.insets = new Insets(0, 0, 5, 5);
		gbc_btnRunC.gridx = 0;
		gbc_btnRunC.gridy = 0;
		contentPane.add(btnRunC, gbc_btnRunC);

		txtrDefault = new JTextArea();
		txtrDefault.setText("");
		GridBagConstraints gbc_txtrDefault = new GridBagConstraints();
		gbc_txtrDefault.insets = new Insets(0, 0, 5, 5);
		gbc_txtrDefault.fill = GridBagConstraints.BOTH;
		gbc_txtrDefault.gridx = 0;
		gbc_txtrDefault.gridy = 3;
		contentPane.add(txtrDefault, gbc_txtrDefault);

		btnRunC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				algorithm.runC45();

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

				if (fileNotFound)
					txtrDefault.setText("File not found");
			}
		});

		JCheckBox chckbxOutputToFile = new JCheckBox("Output to file");
		GridBagConstraints gbc_chckbxOutputToFile = new GridBagConstraints();
		gbc_chckbxOutputToFile.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxOutputToFile.gridx = 0;
		gbc_chckbxOutputToFile.gridy = 1;
		contentPane.add(chckbxOutputToFile, gbc_chckbxOutputToFile);

		chckbxOutputToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxOutputToFile.isSelected()) {
					Algorithm.outputToFile = true;
				} else {
					Algorithm.outputToFile = false;
				}
			}
		});

		JCheckBox chckbxDebug = new JCheckBox("Debug");
		GridBagConstraints gbc_chckbxDebug = new GridBagConstraints();
		gbc_chckbxDebug.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxDebug.gridx = 0;
		gbc_chckbxDebug.gridy = 2;
		contentPane.add(chckbxDebug, gbc_chckbxDebug);

		JLabel lblPathToFile = new JLabel("Path to file");
		GridBagConstraints gbc_lblPathToFile = new GridBagConstraints();
		gbc_lblPathToFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblPathToFile.gridx = 0;
		gbc_lblPathToFile.gridy = 4;
		contentPane.add(lblPathToFile, gbc_lblPathToFile);

		txtPathToFile = new JTextField();
		txtPathToFile.setText("owls15.csv");
		GridBagConstraints gbc_txtPathToFile = new GridBagConstraints();
		gbc_txtPathToFile.insets = new Insets(0, 0, 5, 5);
		gbc_txtPathToFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPathToFile.gridx = 0;
		gbc_txtPathToFile.gridy = 5;
		contentPane.add(txtPathToFile, gbc_txtPathToFile);
		txtPathToFile.setColumns(10);

		JLabel lblPercentageSplitOf = new JLabel("Percentage split of test vs training data");
		GridBagConstraints gbc_lblPercentageSplitOf = new GridBagConstraints();
		gbc_lblPercentageSplitOf.insets = new Insets(0, 0, 5, 5);
		gbc_lblPercentageSplitOf.gridx = 0;
		gbc_lblPercentageSplitOf.gridy = 6;
		contentPane.add(lblPercentageSplitOf, gbc_lblPercentageSplitOf);

		textField = new JTextField();
		textField.setText("0.4");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 7;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);

		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				onChange();

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				onChange();

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				onChange();

			}

			public void onChange() {
				if (textField.getText() != null) {
					try {
						Algorithm.trainingSize = Double.parseDouble(textField.getText());
					} catch (NumberFormatException e) {
						txtrDefault.setText("Percentage split value not allowed");
					}
				}

			}
		});

		txtPathToFile.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {

				onChange();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				onChange();

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				onChange();

			}

			public void onChange() {
				if (txtPathToFile.getText() != null) {
					CSVLoader.pathToCSV = txtPathToFile.getText();
				}
			}
		});

		chckbxDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxDebug.isSelected()) {
					Algorithm.debug = true;
				} else if (!chckbxDebug.isSelected()) {
					Algorithm.debug = false;
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
