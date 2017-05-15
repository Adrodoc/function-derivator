package de.adrodoc55.math.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import de.adrodoc55.math.term.RationalNumber;
import de.adrodoc55.math.term.Term;
import de.adrodoc55.math.term.Variable;
import de.adrodoc55.parsing.SyntaxException;

public class FunktionsFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int MAX_ABLEITUNGEN = 5;

	private List<TermListener> listener = new ArrayList<TermListener>();
	private Term term = new RationalNumber(0);
	private Term output = new RationalNumber(0);

	private JPanel inputPanel;
	private JLabel inputLabel;
	private JTextField inputTextField;

	private Plotter plotter;

	private JPanel outputPanel;

	private JLabel outputDescriptionLabel;
	private JLabel outputLabel;
	private JCheckBox outputCheckBox;

	private JLabel anzahlAbleitungenLabel;
	private JComboBox<Integer> anzahlAbleitungenComboBox;

	private final Term[] ableitungen = new Term[MAX_ABLEITUNGEN];
	{
		for (int x = 0; x < ableitungen.length; x++) {
			ableitungen[x] = new RationalNumber(0);
		}
	}
	private final Variable[] ableitungsVariablen = new Variable[MAX_ABLEITUNGEN];
	private final JPanel[] ableitungsPanels = new JPanel[MAX_ABLEITUNGEN];
	private final JLabel[] ableitungsDescriptionLabels = new JLabel[MAX_ABLEITUNGEN];
	private final JLabel[] ableitungsLabels = new JLabel[MAX_ABLEITUNGEN];
	private final JCheckBox[] ableitungsCheckBoxs = new JCheckBox[MAX_ABLEITUNGEN];
	private final JPanel[] ableitungsVariablePanels = new JPanel[MAX_ABLEITUNGEN];
	private final JLabel[] ableitungsVariableDescriptionLabels = new JLabel[MAX_ABLEITUNGEN];
	private final JTextField[] ableitungsVariableTextFields = new JTextField[MAX_ABLEITUNGEN];

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			throw new RuntimeException(ex);
		}
		FunktionsFrame frame = new FunktionsFrame();
		frame.setVisible(true);
	}

	public FunktionsFrame() {
		super("Funktions Optimierer");
		init();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}

	private void init() {
		JPanel contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);
		contentPane.add(getOutputPanel(), BorderLayout.WEST);
		contentPane.add(getInputPanel(), BorderLayout.NORTH);
		contentPane.add(getPlotter(), BorderLayout.CENTER);
	}

	private void addTermListener(TermListener al) {
		listener.add(al);
	}

	private void fireTermChangedEvent(Term newTerm) {
		// getPlotter().getFunctions().clear();
		for (TermListener al : listener) {
			TermChangedEvent e = new TermChangedEvent();
			// e.setOldTerm(oldTerm);
			e.setNewTerm(newTerm);
			al.termChanged(e);
		}
		getPlotter().repaint();
	}

	private JPanel getInputPanel() {
		if (inputPanel == null) {
			inputPanel = new JPanel();
			inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			inputPanel.add(getInputLabel());
			inputPanel.add(getInputTextField());
		}
		return inputPanel;
	}

	private JLabel getInputLabel() {
		if (inputLabel == null) {
			inputLabel = new JLabel("Gib einen Term ein");
		}
		return inputLabel;
	}

	private JTextField getInputTextField() {
		if (inputTextField == null) {
			inputTextField = new JTextField(40);
			inputTextField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						term = Term.parse(inputTextField.getText());
					} catch (SyntaxException ex) {
						JOptionPane warning = new JOptionPane(ex.getMessage(),
								JOptionPane.ERROR_MESSAGE,
								JOptionPane.DEFAULT_OPTION);
						JDialog errorDialog = warning.createDialog(FunktionsFrame.this, "Fehlerhafter Term");
						errorDialog.setVisible(true);
					}
					fireTermChangedEvent(term);
				}
			});
		}
		return inputTextField;
	}

	private Plotter getPlotter() {
		if (plotter == null) {
			plotter = new Plotter();
			plotter.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		return plotter;
	}

	private JPanel getOutputPanel() {
		if (outputPanel == null) {
			outputPanel = new JPanel();
			outputPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null,
					null, null, null));
			GridBagLayout gbl_outputPanel = new GridBagLayout();
			// gbl_outputPanel.columnWidths = new int[] {};
			// gbl_outputPanel.rowHeights = new int[] {};
			gbl_outputPanel.columnWeights = new double[3];
			gbl_outputPanel.rowWeights = new double[MAX_ABLEITUNGEN + 3];
			gbl_outputPanel.rowWeights[MAX_ABLEITUNGEN + 2] = Double.MIN_VALUE;
			// gbl_outputPanel.columnWeights = new double[] { Double.MIN_VALUE
			// };
			// gbl_outputPanel.rowWeights = new double[] { 0.0, 0.0 };
			outputPanel.setLayout(gbl_outputPanel);
			GridBagConstraints gbc_outputDescriptionLabel = new GridBagConstraints();
			gbc_outputDescriptionLabel.insets = new Insets(1, 1, 1, 1);
			gbc_outputDescriptionLabel.anchor = GridBagConstraints.WEST;
			gbc_outputDescriptionLabel.gridx = 0;
			gbc_outputDescriptionLabel.gridy = 0;
			outputPanel.add(getOutputDescriptionLabel(),
					gbc_outputDescriptionLabel);
			GridBagConstraints gbc_outputLabel = new GridBagConstraints();
			gbc_outputLabel.insets = new Insets(1, 1, 1, 1);
			gbc_outputLabel.fill = GridBagConstraints.BOTH;
			gbc_outputLabel.gridx = 1;
			gbc_outputLabel.gridy = 0;
			outputPanel.add(getOutputLabel(), gbc_outputLabel);
			GridBagConstraints gbc_outputCheckBox = new GridBagConstraints();
			gbc_outputCheckBox.insets = new Insets(1, 1, 1, 1);
			gbc_outputCheckBox.gridx = 2;
			gbc_outputCheckBox.gridy = 0;
			outputPanel.add(getOutputCheckBox(), gbc_outputCheckBox);
			GridBagConstraints gbc_anzahlAbleitungenLabel = new GridBagConstraints();
			gbc_anzahlAbleitungenLabel.gridwidth = 2;
			gbc_anzahlAbleitungenLabel.insets = new Insets(1, 1, 1, 1);
			gbc_anzahlAbleitungenLabel.anchor = GridBagConstraints.WEST;
			gbc_anzahlAbleitungenLabel.gridx = 0;
			gbc_anzahlAbleitungenLabel.gridy = 1;
			outputPanel.add(getAnzahlAbleitungenLabel(),
					gbc_anzahlAbleitungenLabel);
			GridBagConstraints gbc_anzahlAbleitungenComboBox = new GridBagConstraints();
			gbc_anzahlAbleitungenComboBox.insets = new Insets(1, 1, 1, 1);
			gbc_anzahlAbleitungenComboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_anzahlAbleitungenComboBox.gridx = 2;
			gbc_anzahlAbleitungenComboBox.gridy = 1;
			outputPanel.add(getAnzahlAbleitungenComboBox(),
					gbc_anzahlAbleitungenComboBox);

			addAbleitungen();

			GridBagConstraints gbc_formatPanel = new GridBagConstraints();
			gbc_formatPanel.gridwidth = 3;
			gbc_anzahlAbleitungenLabel.gridwidth = 2;
			gbc_formatPanel.insets = new Insets(1, 1, 1, 1);
			gbc_formatPanel.gridx = 0;
			gbc_formatPanel.gridy = MAX_ABLEITUNGEN + 2;
			outputPanel.add(new JPanel(), gbc_formatPanel);
		}
		return outputPanel;
	}

	private JLabel getOutputDescriptionLabel() {
		if (outputDescriptionLabel == null) {
			outputDescriptionLabel = new JLabel("Einfachste Form:");
		}
		return outputDescriptionLabel;
	}

	private JLabel getOutputLabel() {
		if (outputLabel == null) {
			outputLabel = new JLabel();
			addTermListener(new TermListener() {
				@Override
				public void termChanged(TermChangedEvent e) {
					Term oldOutput = output;
					output = e.getNewTerm().toSimplestForm();
					String text = output.toString();
					// Klammern abschneiden
					if (text.startsWith("(") && text.endsWith(")")) {
						text = text.substring(1, text.length() - 1);
					}
					outputLabel.setText(text);
					List<Term> functions = getPlotter().getFunctions();
					functions.remove(oldOutput);
					if (getOutputCheckBox().isSelected()) {
						functions.add(output);
					}
				}
			});
		}
		return outputLabel;
	}

	private JCheckBox getOutputCheckBox() {
		if (outputCheckBox == null) {
			outputCheckBox = new JCheckBox();
			outputCheckBox.setSelected(true);
			outputCheckBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<Term> functions = getPlotter().getFunctions();
					functions.remove(output);
					if (outputCheckBox.isSelected()) {
						functions.add(output);
					}
					getPlotter().repaint();
				}
			});
		}
		return outputCheckBox;
	}

	private JLabel getAnzahlAbleitungenLabel() {
		if (anzahlAbleitungenLabel == null) {
			anzahlAbleitungenLabel = new JLabel("Anzahl Ableitungen");
		}
		return anzahlAbleitungenLabel;
	}

	private JComboBox<Integer> getAnzahlAbleitungenComboBox() {
		if (anzahlAbleitungenComboBox == null) {
			anzahlAbleitungenComboBox = new JComboBox<Integer>();
			for (int x = 1; x <= MAX_ABLEITUNGEN; x++) {
				anzahlAbleitungenComboBox.addItem(x);
			}
			anzahlAbleitungenComboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = anzahlAbleitungenComboBox.getSelectedIndex();
					for (int x = 0; x <= index; x++) {
						getAbleitungsPanel(x).setVisible(true);
						getAbleitungsPanel(x).repaint();
					}
					for (int x = index + 1; x < MAX_ABLEITUNGEN; x++) {
						getAbleitungsPanel(x).setVisible(false);
						getAbleitungsPanel(x).repaint();

						JCheckBox ableitungsCheckBox = getAbleitungsCheckBox(x);
						if (ableitungsCheckBox.isSelected()) {
							ableitungsCheckBox.doClick();
						}
					}
				}
			});
		}
		return anzahlAbleitungenComboBox;
	}

	private void addAbleitungen() {
		for (int y = 0; y < MAX_ABLEITUNGEN; y++) {
			GridBagConstraints gbc_ableitungsPanel = new GridBagConstraints();
			gbc_ableitungsPanel.insets = new Insets(1, 1, 1, 1);
			gbc_ableitungsPanel.gridwidth = 3;
			gbc_ableitungsPanel.fill = GridBagConstraints.BOTH;
			gbc_ableitungsPanel.gridx = 0;
			gbc_ableitungsPanel.gridy = y + 2;
			JPanel ableitungsPanel = getAbleitungsPanel(y);
			if (y > getAnzahlAbleitungenComboBox().getSelectedIndex()) {
				ableitungsPanel.setVisible(false);
			}
			getOutputPanel().add(ableitungsPanel, gbc_ableitungsPanel);
		}
	}

	private JPanel getAbleitungsPanel(int i) {
		if (ableitungsPanels[i] == null) {
			ableitungsPanels[i] = new JPanel();
			ableitungsPanels[i].setBorder(new LineBorder(SystemColor.scrollbar,
					1, true));
			ableitungsPanels[i].setLayout(new BorderLayout(1, 1));
			ableitungsPanels[i].add(getAbleitungsDescriptionLabel(i),
					BorderLayout.WEST);
			ableitungsPanels[i].add(getAbleitungsLabel(i), BorderLayout.CENTER);
			ableitungsPanels[i]
					.add(getAbleitungsCheckBox(i), BorderLayout.EAST);
			ableitungsPanels[i].add(getAbleitungsVariablePanel(i),
					BorderLayout.SOUTH);
		}
		return ableitungsPanels[i];
	}

	private JLabel getAbleitungsDescriptionLabel(int i) {
		if (ableitungsDescriptionLabels[i] == null) {
			ableitungsDescriptionLabels[i] = new JLabel(i + 1 + ". Ableitung:");
		}
		return ableitungsDescriptionLabels[i];
	}

	private JLabel getAbleitungsLabel(int i) {
		if (ableitungsLabels[i] == null) {
			ableitungsLabels[i] = new JLabel();
			ableitungsLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			addTermListener(new TermListener() {
				@Override
				public void termChanged(TermChangedEvent e) {
					Term oldAbleitung = ableitungen[i];
					Term input;
					if (i == 0) {
						input = e.getNewTerm().toSimplestForm();
					} else {
						input = ableitungen[i - 1];
					}
					ableitungen[i] = input
							.getDerivation(ableitungsVariablen[i]);
					// for (int x = 0; x < i; x++) {
					// ableitungen[i] = ableitungen[i]
					// .getDerivation(ableitungsVariablen[i]);
					// }
					String text = ableitungen[i].toString();
					// Klammern abschneiden
					if (text.startsWith("(") && text.endsWith(")")) {
						text = text.substring(1, text.length() - 1);
					}
					ableitungsLabels[i].setText(text);
					List<Term> functions = getPlotter().getFunctions();
					functions.remove(oldAbleitung);
					if (getAbleitungsCheckBox(i).isSelected()) {
						functions.add(ableitungen[i]);
					}
				}
			});
		}
		return ableitungsLabels[i];
	}

	private JCheckBox getAbleitungsCheckBox(int i) {
		if (ableitungsCheckBoxs[i] == null) {
			ableitungsCheckBoxs[i] = new JCheckBox();
			ableitungsCheckBoxs[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<Term> functions = getPlotter().getFunctions();
					if (ableitungsCheckBoxs[i].isSelected()) {
						functions.add(ableitungen[i]);
					} else {
						functions.remove(ableitungen[i]);
					}
					getPlotter().repaint();
				}
			});
		}
		return ableitungsCheckBoxs[i];
	}

	private JPanel getAbleitungsVariablePanel(int i) {
		if (ableitungsVariablePanels[i] == null) {
			ableitungsVariablePanels[i] = new JPanel();
			FlowLayout flowLayout = (FlowLayout) ableitungsVariablePanels[i]
					.getLayout();
			flowLayout.setVgap(1);
			flowLayout.setHgap(1);
			ableitungsVariablePanels[i]
					.add(getAbleitungsVariableDescriptionLabel(i));
			ableitungsVariablePanels[i].add(getAbleitungsVariableTextField(i));
		}
		return ableitungsVariablePanels[i];
	}

	private JLabel getAbleitungsVariableDescriptionLabel(int i) {
		if (ableitungsVariableDescriptionLabels[i] == null) {
			ableitungsVariableDescriptionLabels[i] = new JLabel(
					"Ableitungs Variable:");
		}
		return ableitungsVariableDescriptionLabels[i];
	}

	private JTextField getAbleitungsVariableTextField(int i) {
		if (ableitungsVariableTextFields[i] == null) {
			ableitungsVariableTextFields[i] = new JTextField("x");
			ableitungsVariablen[i] = new Variable("x");
			ableitungsVariableTextFields[i].setColumns(2);
			ableitungsVariableTextFields[i]
					.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String text = ableitungsVariableTextFields[i]
									.getText();
							ableitungsVariablen[i] = new Variable(text);
							fireTermChangedEvent(term);
						}
					});
		}
		return ableitungsVariableTextFields[i];
	}
}
