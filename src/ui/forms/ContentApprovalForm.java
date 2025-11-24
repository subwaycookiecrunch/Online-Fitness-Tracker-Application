package ui.forms;

import dao.ContentDAO;
import model.FitnessContent;
import util.CustomExceptions.DatabaseException;

import javax.swing.*;
import java.awt.*;

public class ContentApprovalForm extends JDialog {
    private ContentDAO contentDAO;
    private FitnessContent content;

    public ContentApprovalForm(Frame owner, FitnessContent content) {
        super(owner, "Approve/Reject Content", true);
        this.content = content;
        this.contentDAO = new ContentDAO();

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(4, 1, 10, 10));

        add(new JLabel("Title: " + content.getTitle()));
        add(new JLabel("Description: " + content.getDescription()));

        JButton approveButton = new JButton("Approve");
        approveButton.addActionListener(e -> processContent(true));
        add(approveButton);

        JButton rejectButton = new JButton("Reject");
        rejectButton.addActionListener(e -> processContent(false));
        add(rejectButton);
    }

    private void processContent(boolean approve) {
        try {
            if (approve) {
                contentDAO.approveContent(content.getId());
                JOptionPane.showMessageDialog(this, "Content Approved!");
            } else {
                contentDAO.rejectContent(content.getId());
                JOptionPane.showMessageDialog(this, "Content Rejected!");
            }
            dispose();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
