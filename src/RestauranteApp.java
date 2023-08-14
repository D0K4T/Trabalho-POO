import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RestauranteApp {
    private Map<String, CardapioDia> cardapioSemanal = new HashMap<>();
    private DefaultListModel<CardapioItem> cardapioListModel = new DefaultListModel<>();

    private JFrame frame;
    private JTextField nomeField;
    private JTextField descricaoField;
    private JList<CardapioItem> cardapioList;
    private JComboBox<String> diaComboBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RestauranteApp app = new RestauranteApp();
            app.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Restaurante Universitário");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        diaComboBox = new JComboBox<>(new String[]{"Segunda", "Terça", "Quarta", "Quinta", "Sexta"});
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(diaComboBox, constraints);

        nomeField = new JTextField();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(new JLabel("Nome da comida:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(nomeField, constraints);

        descricaoField = new JTextField();
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Descrição:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(descricaoField, constraints);

        JButton adicionarButton = new JButton("Adicionar");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(adicionarButton, constraints);

        JButton removerButton = new JButton("Remover");
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(removerButton, constraints);

        JButton salvarButton = new JButton("Salvar Cardápio");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(salvarButton, constraints);

        JButton carregarButton = new JButton("Carregar Cardápio");
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(carregarButton, constraints);

        cardapioList = new JList<>(cardapioListModel);
        JScrollPane scrollPane = new JScrollPane(cardapioList);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, constraints);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String descricao = descricaoField.getText();
                String dia = (String) diaComboBox.getSelectedItem();

                if (!nome.isEmpty() && !descricao.isEmpty()) {
                    CardapioItem item = new CardapioItem(nome, descricao);
                    cardapioSemanal.get(dia).adicionarItem(item);
                    atualizarListaDoCardapio(dia);
                    nomeField.setText("");
                    descricaoField.setText("");
                }
            }
        });

        removerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cardapioList.getSelectedIndex();
                if (selectedIndex != -1) {
                    CardapioItem selectedItem = cardapioListModel.get(selectedIndex);
                    String dia = (String) diaComboBox.getSelectedItem();
                    cardapioSemanal.get(dia).removerItem(selectedItem);
                    atualizarListaDoCardapio(dia);
                }
            }
        });

        diaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String diaSelecionado = (String) diaComboBox.getSelectedItem();
                atualizarListaDoCardapio(diaSelecionado);
            }
        });

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    salvarCardapio(filePath);
                }
            }
        });

        carregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    carregarCardapio(filePath);
                }
            }
        });

        // Inicializar o cardápio semanal para cada dia da semana
        for (int i = 0; i < diaComboBox.getItemCount(); i++) {
            cardapioSemanal.put((String) diaComboBox.getItemAt(i), new CardapioDia());
        }
    }

    private void atualizarListaDoCardapio(String dia) {
        cardapioListModel.clear();
        cardapioListModel.addAll(cardapioSemanal.get(dia).getItens());
    }

    private void salvarCardapio(String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(cardapioSemanal);
            JOptionPane.showMessageDialog(frame, "Cardápio salvo com sucesso!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao salvar o cardápio:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCardapio(String filePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            cardapioSemanal = (Map<String, CardapioDia>) inputStream.readObject();
            JOptionPane.showMessageDialog(frame, "Cardápio carregado com sucesso!");
            String diaSelecionado = (String) diaComboBox.getSelectedItem();
            atualizarListaDoCardapio(diaSelecionado);
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar o cardápio:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

