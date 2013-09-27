package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sf.tweety.beliefdynamics.BaseRevisionOperator;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;

/**
 * The default view for a revision compare. This class is responsible to create the hierarchy of
 * widgets and delegates its inputs to the RevisionComparePresenter. Everytime an update occurs
 * the output of the revision is updated.
 * This class is based in spirit on the RevisionCompareView in the beliefdynamics.gui package.
 * 
 * @author Sebastian Homann
 * @author Tim Janus
 */
public class SimpleRevisionCompareView extends JPanel implements PropertyChangeListener {
	
	/** kill warning */
	private static final long serialVersionUID = 5699544277473453367L;

	@SuppressWarnings("rawtypes")
	protected JComboBox cbOperatorLeft;
	
	@SuppressWarnings("rawtypes")
	protected JComboBox cbOperatorRight;

	protected JButton btnAddLeft;
	
	protected JButton btnAddRight;
	
	protected JButton btnRunRevision;
	
	protected JTextArea txtResultLeft;
	
	protected JTextArea txtResultRight;
	
	protected JTextArea txtBeliefBase;
	
	protected JTextArea txtNewBeliefs;
	
	protected JList<AnswerSet> lstLeftAnswerSets;
	
	protected JList<AnswerSet> lstRightAnswerSets;
	
	/** Default Ctor: Creates the view */
	public SimpleRevisionCompareView() {
		this.setLayout(new BorderLayout());		
		this.add(guiGetOperatorControls(), BorderLayout.NORTH);
		
		// input
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerPanel.add(guiGetInput());
		centerPanel.add(guiGetRevisionResult());
		centerPanel.add(guiGetAnswerSetView());
		this.add(centerPanel, BorderLayout.CENTER);
		
		this.add(guiGetButtons(), BorderLayout.SOUTH);
	}
	
	private JPanel guiGetAnswerSetView() {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());

		JPanel lblPanel = new JPanel();
		lblPanel.setLayout(new GridLayout(1,2,5,5));
		lblPanel.add(new JLabel("Resulting answer sets"));
		lblPanel.add(new JLabel("Resulting answer sets"));
		result.add(lblPanel, BorderLayout.NORTH);
		
		JPanel txtPanel = new JPanel();
		txtPanel.setLayout(new GridLayout(1,2, 5, 5));
		lstLeftAnswerSets = new JList<AnswerSet>();
		lstLeftAnswerSets.setPreferredSize(new Dimension(10, 20));
		lstRightAnswerSets = new JList<AnswerSet>();
		lstRightAnswerSets.setPreferredSize(new Dimension(10, 20));
		txtPanel.add(lstLeftAnswerSets);
		txtPanel.add(lstRightAnswerSets);
		result.add(new JScrollPane(txtPanel), BorderLayout.CENTER);
		result.setPreferredSize(new Dimension(20, 80));
		return result;
	}
	
	private JPanel guiGetRevisionResult() {
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout());

		JPanel lblPanel = new JPanel();
		lblPanel.setLayout(new GridLayout(1,2,5,5));
		lblPanel.add(new JLabel("Left revision result"));
		lblPanel.add(new JLabel("Right revision result"));
		parentPanel.add(lblPanel, BorderLayout.NORTH);
		
		JPanel txtPanel = new JPanel();
		txtPanel.setLayout(new GridLayout(1,2, 5, 5));
		txtResultLeft = new JTextArea(10, 20);
		txtResultLeft.setEditable(false);
		txtPanel.add(txtResultLeft);
		
		txtResultRight = new JTextArea(10, 20);
		txtResultRight.setEditable(false);
		txtPanel.add(txtResultRight);
		
		parentPanel.add(new JScrollPane(txtPanel), BorderLayout.CENTER);
		return parentPanel;
	}
	
	private JPanel guiGetInput() {
		JPanel actPanel = new JPanel();
		actPanel.setLayout(new BorderLayout());
		JPanel lblPanel = new JPanel();
		lblPanel.setLayout(new GridLayout(1,2,5,5));
		lblPanel.add(new JLabel("Beliefbase"));
		lblPanel.add(new JLabel("New beliefs"));
		actPanel.add(lblPanel, BorderLayout.NORTH);
		
		JPanel txtPanel = new JPanel();
		txtPanel.setLayout(new GridLayout(1,2, 5, 5));
		
		txtBeliefBase = new JTextArea(10, 40);
		txtBeliefBase.setEditable(true);
		txtPanel.add(txtBeliefBase);
		
		txtNewBeliefs = new JTextArea(10, 40);
		txtNewBeliefs.setEditable(true);		
		txtPanel.add(txtNewBeliefs);
		
		actPanel.add(new JScrollPane(txtPanel), BorderLayout.CENTER);
		
		return actPanel;
	}
	
	@SuppressWarnings("rawtypes")
	private JPanel guiGetOperatorControls() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));
		result.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		result.add(new JLabel("Left revision operator:"));
		result.add(Box.createRigidArea(new Dimension(10, 0)));
		cbOperatorLeft = new JComboBox();
		result.add(cbOperatorLeft);
		
		result.add(Box.createRigidArea(new Dimension(10, 0)));
		result.add(new JLabel("Right revision operator:"));
		result.add(Box.createRigidArea(new Dimension(10, 0)));
		cbOperatorRight = new JComboBox();
		result.add(cbOperatorRight);
		return result;
	}
	
	private JPanel guiGetButtons() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));
		result.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		btnAddLeft = new JButton("Load beliefBase");
		btnAddRight = new JButton("Load new beliefs");
		btnRunRevision = new JButton("Run Revision");
		btnRunRevision.setMnemonic(KeyEvent.VK_R);

		result.add(btnAddLeft);
		result.add(Box.createRigidArea(new Dimension(10, 0)));
		result.add(btnAddRight);
		result.add(Box.createHorizontalGlue());
		result.add(btnRunRevision);
		return result;
	}

	/**
	 * Reacts to property change events to keep the view up to date. The presenter is
	 * responsible to register the view at the correct data-model.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("leftOperator")) {
			cbOperatorLeft.setSelectedItem(evt.getNewValue());
		} else if(evt.getPropertyName().equals("rightOperator")) {
			cbOperatorRight.setSelectedItem(evt.getNewValue());
		} else if(evt.getPropertyName().equals("beliefBase")) {
			PropertyChangeEvent ipce = (PropertyChangeEvent)evt;
			String newValue = (String) ipce.getNewValue();
			
			if(newValue == null) {
				txtBeliefBase.setText("");
			} else {
				txtBeliefBase.setText(newValue);
			}
		} else if(evt.getPropertyName().equals("newbeliefs")) {
			PropertyChangeEvent ipce = (PropertyChangeEvent)evt;
			String newValue = (String) ipce.getNewValue();
			
			if(newValue == null) {
				txtNewBeliefs.setText("");
			} else {
				txtNewBeliefs.setText(newValue);
			}
		} else if(evt.getPropertyName() == "selectableOperators") {
			if(evt.getOldValue() == null) {
				BaseRevisionOperator<?> op = (BaseRevisionOperator<?>)evt.getNewValue();
				cbOperatorLeft.addItem(op);
				cbOperatorRight.addItem(op);
			} else if(evt.getNewValue() == null) {
				BaseRevisionOperator<?> op = (BaseRevisionOperator<?>)evt.getOldValue();
				cbOperatorLeft.removeItem(op);
				cbOperatorRight.removeItem(op);
			}
		} else if(evt.getPropertyName() == "leftresult") {
			Collection<?> result = (Collection<?>)evt.getNewValue();
			String newResult = "";
			for(Object entry : result) {
				newResult += entry + "\n";
			}
			txtResultLeft.setText(newResult);
		} else if(evt.getPropertyName() == "rightresult") {
			Collection<?> result = (Collection<?>)evt.getNewValue();
			String newResult = "";
			for(Object entry : result) {
				newResult += entry + "\n";
			}
			txtResultRight.setText(newResult);
		} else if(evt.getPropertyName() == "error") {
			JOptionPane.showMessageDialog(this, evt.getNewValue(), "Error", JOptionPane.ERROR_MESSAGE);
		} else if(evt.getPropertyName() == "leftASL") {
			AnswerSetList asl = (AnswerSetList) evt.getNewValue();
			DefaultListModel<AnswerSet> model = new DefaultListModel<AnswerSet>();
			for(AnswerSet as : asl) {
				model.addElement(as);
			}
			lstLeftAnswerSets.setModel(model);
		} else if(evt.getPropertyName() == "rightASL") {
			AnswerSetList asl = (AnswerSetList) evt.getNewValue();
			DefaultListModel<AnswerSet> model = new DefaultListModel<AnswerSet>();
			for(AnswerSet as : asl) {
				model.addElement(as);
			}
			lstRightAnswerSets.setModel(model);
		}
	}
}