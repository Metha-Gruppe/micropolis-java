package micropolisj.gui;

import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;

import micropolisj.engine.Micropolis;
import micropolisj.engine.MicropolisTool;
import micropolisj.research.ResearchState;

public class CheatWindow extends JFrame implements KeyEventDispatcher{
    
    private static final String MORE_MONEY_CHEAT = "gimme";
    private static final int AMOUNT_GIVEN = 5000;
    
    private static final String MORE_RESEARCH_CHEAT = "research";
    private static final int AMOUNT_RESEARCH = 1000;
    
    
    private static String lastInput;
    
    private Micropolis engine;
    
    private KeyboardFocusManager manager;
    
    private JTextField textField;
    
    private Map<MicropolisTool, JToggleButton> toolBtns;
    
    public CheatWindow(Micropolis engine, Map<MicropolisTool, JToggleButton> toolBtns) {
        super("Let's cheat!");
        this.engine = engine;
        this.toolBtns = toolBtns;
        textField = new JTextField(10);
        textField.setText(lastInput);
        add(textField);
        setLayout(new FlowLayout());
        pack();
        setLocationRelativeTo(null);
        manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                if(textField.getText().equals(MORE_MONEY_CHEAT)) {
                    engine.getPlayerInfo().budget.totalFunds += AMOUNT_GIVEN;
                    engine.fireFundsChanged();
                }
                if(textField.getText().equals(MORE_RESEARCH_CHEAT)) {
                	System.out.println(engine.getPlayerInfo().researchData.researchPoints);
                    engine.getPlayerInfo().researchData.researchPoints += AMOUNT_RESEARCH;
                    System.out.println(engine.getPlayerInfo().researchData.researchPoints);
                    engine.getPlayerInfo().researchState = ResearchState.createFromResearchData(engine, engine.getPlayerInfo().researchData, toolBtns);
                }
                lastInput = textField.getText();
                manager.removeKeyEventDispatcher(this);
                this.dispose();
                return true;
            }
        }
        return false;
    }
    
}
