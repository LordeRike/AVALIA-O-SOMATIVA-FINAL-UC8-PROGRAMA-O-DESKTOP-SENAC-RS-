
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager; // Gerenciador de renderização visual nativa
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;       // Manipulação de cores RGB
import java.awt.Font;        // Estilização tipográfica
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.BufferedReader; // Leitura em buffer de arquivo
import java.io.BufferedWriter; // Escrita em buffer no disco
import java.io.File;           // Referência a arquivo do sistema operacional
import java.io.FileReader;     // Conector físico de leitura
import java.io.FileWriter;     // Conector físico de gravação
import java.io.IOException; 

public class senacEcoTrash extends JFrame {

    private JTextField txtCooperado;
    private JTextField txtPeso;
    private JComboBox<String> cbMateriais;

    private DefaultTableModel modeloTabela;
    private JTable tabelaOS;
    private JLabel lblFaturamento;
    private double totalFaturamento = 0.0;

    private final String BANCO_DADOS = "ecotrash_lotes.csv";
    
    public senacEcoTrash() {
        try {
            // Ativa o tema moderno nativo do SO do computador (Windows/Linux/Mac)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Em caso de inconsistência de tema, mantém a interface padrão
        }

        setTitle("Senac EcoTrash v1.0 — Gestão de Logística Reversa");
        setSize(650, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        JMenuBar barraMenu = new JMenuBar();
        JMenu menuOperacoes = new JMenu("Operações");
        menuOperacoes.setMnemonic(KeyEvent.VK_M);

        JMenuItem itemLancar = new JMenuItem("Lançar Novo Lote...");
        itemLancar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));

        JMenuItem itemSair = new JMenuItem("Sair do Sistema");
        itemSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));

        menuOperacoes.add(itemLancar);
        menuOperacoes.addSeparator();
        menuOperacoes.add(itemSair);

        barraMenu.add(menuOperacoes);

        setJMenuBar(barraMenu);

        JPanel pnlNorte = new JPanel();
        pnlNorte.setBackground(new Color(10,200,55));

        JLabel lblTitulo = new JLabel("Senac EcoTrash v1.0 — Gestão de Logística Reversa");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        pnlNorte.add(lblTitulo);
        add(pnlNorte, BorderLayout.NORTH);

        JPanel pnlCentro = new JPanel(new GridLayout(3, 2, 10, 8));
        pnlCentro.setBackground(new Color(192,192,192));

        JLabel lblCooperado = new JLabel("Nome do Cooperado/Doador: ");
        lblCooperado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCooperado = new JTextField();
        txtCooperado.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblMaterial = new JLabel("Tipo de Material: ");
        lblMaterial.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        String[] materiais = {"Plástico (R$ 2,50/Kg)","Papel / Papelão (R$ 1,50/Kg)","Metal / Alumínio (R$ 6,00/Kg)","Vidro (R$ 0,80/Kg)"};
        cbMateriais = new JComboBox<>(materiais);
        cbMateriais.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblPeso = new JLabel("Peso Coletado (Kg): ");
        lblPeso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPeso = new JTextField();
        txtPeso.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pnlCentro.add(lblCooperado); pnlCentro.add(txtCooperado);
        pnlCentro.add(lblMaterial); pnlCentro.add(cbMateriais);
        pnlCentro.add(lblPeso); pnlCentro.add(txtPeso);

        JPanel pnlNorteIntegrado = new JPanel(new BorderLayout(5,5));
        pnlNorteIntegrado.add(pnlNorte, BorderLayout.NORTH);
        pnlNorteIntegrado.add(pnlCentro, BorderLayout.CENTER);

        add(pnlNorteIntegrado, BorderLayout.NORTH);

        String[] colunas = {"Cooperado/Doador","Tipo de Material","Peso Coletado (Kg)","Valor Total (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaOS = new JTable(modeloTabela);
        tabelaOS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaOS.setRowHeight(22);

        add(new JScrollPane(tabelaOS), BorderLayout.CENTER);

        JPanel pnlSul = new JPanel(new BorderLayout(10,10));
        pnlSul.setBackground(new Color(192,192,192));

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlBotoes.setBackground(new Color(195,195,195));

        JButton btnConfirmar = new JButton("Confirmar Lote");
        btnConfirmar.setMnemonic(KeyEvent.VK_C);
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirmar.setBackground(new Color(0, 255, 69));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setOpaque(true);
        btnConfirmar.setBorderPainted(false);

        JButton btnRemover = new JButton("Remover Lote Selecionado");
        btnRemover.setMnemonic(KeyEvent.VK_R);
        btnRemover.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRemover.setBackground(new Color(255, 0, 69));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setOpaque(true);
        btnRemover.setBorderPainted(false);

        pnlBotoes.add(btnConfirmar);
        pnlBotoes.add(btnRemover);

        lblFaturamento = new JLabel("Total de Reembolso: R$ 0,00 ");
        lblFaturamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFaturamento.setForeground(new Color(13, 71, 161));

        pnlSul.add(pnlBotoes, BorderLayout.WEST);
        pnlSul.add(lblFaturamento, BorderLayout.EAST);
        
        add(pnlSul, BorderLayout.SOUTH);

        carregarDados();
        txtCooperado.requestFocus();

        btnConfirmar.addActionListener(e -> ConfirmarLote());
        btnRemover.addActionListener(e -> RemoverLote());

        itemLancar.addActionListener(e -> txtCooperado.requestFocus());
        itemSair.addActionListener(e -> System.exit(0));
        
    }

    private void ConfirmarLote(){
        String coop = txtCooperado.getText().trim();
        String material = (String) cbMateriais.getSelectedItem();
        String pesoTexto = txtPeso.getText().trim().replace(",",".");

        if  (coop.isEmpty() || pesoTexto.isEmpty()){
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (coop.contains(";")) {
            JOptionPane.showMessageDialog(this, "O nome do cooperado não pode conter o caractere ';'!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;            
        }

        try {
            double peso = Double.parseDouble(pesoTexto);
            double valorUnitario = 0.0;
            double valorTotal = 0.0;

            switch (material) {
                case "Plástico (R$ 2,50/Kg)":
                    valorUnitario = 2.50;
                    break;
                case "Papel / Papelão (R$ 1,50/Kg)":
                    valorUnitario = 1.50;
                    break;
                case "Metal / Alumínio (R$ 6,00/Kg)":
                    valorUnitario = 6.00;
                    break;
                case "Vidro (R$ 0,80/Kg)":
                    valorUnitario = 0.80;
                    break;
            }

            if (peso > 50.0) {
                double desc = (peso * valorUnitario) * 0.1;
                valorTotal = peso * valorUnitario - desc;

            } else {
                valorTotal = peso * valorUnitario;
            }

            

            modeloTabela.addRow(new Object[]{coop, material,  peso, String.format("%.2f", valorTotal)});
            gravarNovaLinha(coop, material, peso, valorTotal);

            txtCooperado.setText("");
            txtPeso.setText("");
            txtCooperado.requestFocus();

            JOptionPane.showMessageDialog(this,"Lote Cadastrado com Sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Peso inválido! Digite um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarDados() {
        File arq = new File(BANCO_DADOS);
        if (!arq.exists()) return ;

        try (FileReader fr = new FileReader(arq);
            BufferedReader br = new BufferedReader(fr)){
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] dados = linha.split(";");

                    if (dados.length == 4) {
                        double val = Double.parseDouble(dados[3]);

                        Object[] linhaTabela = {dados[0], dados[1],dados[2], String.format("R$ %.2f",val)};
                        modeloTabela.addRow(linhaTabela);

                        totalFaturamento += val;                        
                    }
                }

            lblFaturamento.setText(String.format("Total de Reembolso: R$ %.2f", totalFaturamento));
            
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar banco de dados de disco: " + ex.getMessage(), "Erro I/O", JOptionPane.ERROR_MESSAGE);
        }

    }
    // é só um teste pra ver se foi no git

    private void gravarNovaLinha(String coop, String material,double peso, double valorTotal) {
        File arq = new File(BANCO_DADOS);

        try (FileWriter fw = new FileWriter(arq, true);
            BufferedWriter bw = new BufferedWriter(fw)){

                bw.write(coop + ";" + material + ";" + peso + ";" + valorTotal);
                bw.newLine();

                //sincronizarTabelaComDisco();
                recalcularFaturamento();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar registro no HD: " + ex.getMessage(), "Erro I/O", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    private void RemoverLote() {
        int linhaSel = tabelaOS.getSelectedRow();
        if (linhaSel == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma Linha na tabela para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover esta Linha.?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            modeloTabela.removeRow(linhaSel);

            sincronizarTabelaComDisco();
            recalcularFaturamento();

            JOptionPane.showMessageDialog(this, "Linha removida e base sincronizada!");
        }
    }

    private void sincronizarTabelaComDisco(){
        File arq = new File(BANCO_DADOS);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arq, false))) {
            for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                String coop = (String) modeloTabela.getValueAt(i, 0);
                String material = (String) modeloTabela.getValueAt(i, 1);
                double peso = (double) modeloTabela.getValueAt(i, 2);
                String valorTotal = modeloTabela.getValueAt(i, 3).toString().replace("R$", "").replace(",", ".").trim();

                bw.write(coop + ";" + material + ";" + peso + ";" + valorTotal);
                bw.newLine();
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao sincronizar dados no HD: " + ex.getMessage(), "Erro I/O", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void recalcularFaturamento() {
        totalFaturamento = 0.0;
        for (int i = 0; i < modeloTabela.getRowCount(); i++) {
            String valorTotal = modeloTabela.getValueAt(i, 2).toString().replace("R$", "").replace(",", ".").trim();
            totalFaturamento += Double.parseDouble(valorTotal);
        }
        lblFaturamento.setText(String.format("Total Reembolsado: R$ %.2f  ", totalFaturamento));
    }

    public static void main(String[] args) {
        new senacEcoTrash().setVisible(true);
    }

    
}