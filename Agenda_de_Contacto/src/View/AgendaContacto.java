package View;

import Controller.ContactoDB;
import Model.Contacto;
import com.mysql.cj.jdbc.Blob;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class AgendaContacto extends javax.swing.JFrame {

    ContactoDB db = new ContactoDB();
    Contacto contacto = new Contacto();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    private FileInputStream fis = null;
    private int n;

    public AgendaContacto() throws SQLException {
        initComponents();
        con = db.getConexao();
        jTableContacto.setModel(tabelaContacto());
        ButtonGroup group = new ButtonGroup();
        group.add(jRadioButtonFeminino);
        group.add(jRadioButtonMasculino);
    }

    private void salvarContacto() throws SQLException, FileNotFoundException {
        contacto.setNome(this.jTextFieldNome.getText());
        contacto.setApelido(this.jTextFieldApelido.getText());
        contacto.setTelefone1(this.jTextFieldTelefone1.getText());
        contacto.setTelefone2(this.jTextFieldTelefone2.getText());
        contacto.setEndereco1(this.jTextFieldEndereco1.getText());
        contacto.setEndereco2(this.jTextFieldEndereco2.getText());
        contacto.setEmail(this.jTextFieldEmail.getText());

        contacto.setCategoria(this.jComboBoxCategoria.getSelectedItem().toString());
        if (jRadioButtonFeminino.isSelected()) {
            contacto.setGenero(jRadioButtonFeminino.getText());
        }
        if (jRadioButtonMasculino.isSelected()) {
            contacto.setGenero(jRadioButtonMasculino.getText());
        }
        //c.setGenero(group.getSelection().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String addDate = dateFormat.format(jDateChooserDataNascimento.getDate());
        contacto.setDataNascimento(addDate);
        //Carregar imagem ao DB
        contacto.setFileInputStream(fis);
        contacto.setTamanho(n);
        db.salvar(contacto);
    }

    private boolean camposVazios() {
        if (this.jTextFieldNome.getText().equals("Nome")) {
            JOptionPane.showMessageDialog(rootPane, "Digite o nome");
            jTextFieldNome.requestFocus();
            return false;
        }
        if (jTextFieldApelido.getText().equals("Apelido")) {
            JOptionPane.showMessageDialog(rootPane, "Digite o Apelido");
            jTextFieldApelido.requestFocus();
            return false;
        }
        if (jTextFieldTelefone1.getText().equals("Telefone 1")) {
            JOptionPane.showMessageDialog(rootPane, "Digite o telefone 1");
            jTextFieldTelefone1.requestFocus();
            return false;
        }
        if (jTextFieldTelefone2.getText().equals("Telefone 2")) {
            JOptionPane.showMessageDialog(rootPane, "Digite o telefone 2");
            jTextFieldTelefone2.requestFocus();
            return false;
        }
        if (jTextFieldEndereco1.getText().equals("Endereco 1")) {
            JOptionPane.showMessageDialog(rootPane, "Digite o endereco 1");
            jTextFieldEndereco1.requestFocus();
            return false;
        }
        if (jTextFieldEndereco2.getText().equals("Endereco 2")) {
            JOptionPane.showMessageDialog(rootPane, "Digite o endereco 2");
            jTextFieldEndereco2.requestFocus();
            return false;
        }
        if (jTextFieldEmail.getText().equals("Email")) {
            JOptionPane.showMessageDialog(rootPane, "Digite o email");
            jTextFieldEmail.requestFocus();
            return false;
        }

        if (jComboBoxCategoria.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Escolhe uma categoria");
            jComboBoxCategoria.requestFocus();
            return false;
        }

        if (this.jDateChooserDataNascimento.getDate() == null) {
            JOptionPane.showMessageDialog(rootPane, "Escolhe uma data de nascimento..!");
            jDateChooserDataNascimento.requestFocus();
            return false;
        }
        return true;
    }

    private void limparCampos() {
        this.jTextFieldNome.setText("Nome");
        this.jTextFieldApelido.setText("Apelido");
        this.jTextFieldTelefone1.setText("Telefone 1");
        this.jTextFieldTelefone2.setText("Telefone 2");
        this.jTextFieldEndereco1.setText("Endereco 1");
        this.jTextFieldEndereco2.setText("Endereco 2");
        this.jTextFieldEmail.setText("Email");
        this.jComboBoxCategoria.setSelectedIndex(0);
        this.jDateChooserDataNascimento.setDate(null);
        this.jLabelImagem.setIcon(null);
        this.jTextFieldPesquisar.setText("Pesquisar");
    }

    private boolean validarCampos() {
        String movitel86 = "86";
        String movitel87 = "87";
        String vodacom84 = "84";
        String vodacom85 = "85";
        String mcel82 = "82";
        String mcel83 = "83";
        if (!this.jTextFieldNome.getText().matches("[a-zA-Z_ ]+")) {
            JOptionPane.showMessageDialog(null, "Digite um nome valido!", "Validacao do Nome", 2);
            jTextFieldNome.requestFocus();
            return false;
        }
        if (!this.jTextFieldApelido.getText().matches("[a-zA-Z_ ]+")) {
            JOptionPane.showMessageDialog(null, "Digite um apelido valido!", "Validacao do Apelido", 2);
            jTextFieldApelido.requestFocus();
            return false;
        }

        if (this.jComboBoxCategoria.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Selecione uma categoria!", "Validacao de Tipo de Contacto", 2);
            return false;
        }
        if (!(this.jRadioButtonFeminino.isSelected() || this.jRadioButtonMasculino.isSelected())) {
            JOptionPane.showMessageDialog(null, "Escolhe um genero!");
            return false;
        }
        if (this.jLabelImagem.getIcon() == null) {
            JOptionPane.showMessageDialog(null, "Escolhe uma foto");
            return false;
        }
        if (!((this.jTextFieldTelefone1.getText().startsWith(mcel82)
                || this.jTextFieldTelefone1.getText().startsWith(mcel83)
                || this.jTextFieldTelefone1.getText().startsWith(vodacom84)
                || this.jTextFieldTelefone1.getText().startsWith(vodacom85)
                || this.jTextFieldTelefone1.getText().startsWith(movitel86)
                || this.jTextFieldTelefone1.getText().startsWith(movitel87))
                && (this.jTextFieldTelefone1.getText().length() == 9))) {
            JOptionPane.showMessageDialog(null, "Digite um contacto valido", "Validacao do Contacto", 2);
            this.jTextFieldTelefone1.requestFocus();
            return false;
        }
        if (!((this.jTextFieldTelefone2.getText().startsWith(mcel82)
                || this.jTextFieldTelefone2.getText().startsWith(mcel83)
                || this.jTextFieldTelefone2.getText().startsWith(vodacom84)
                || this.jTextFieldTelefone2.getText().startsWith(vodacom85)
                || this.jTextFieldTelefone2.getText().startsWith(movitel86)
                || this.jTextFieldTelefone2.getText().startsWith(movitel87))
                && (this.jTextFieldTelefone2.getText().length() == 9))) {
            JOptionPane.showMessageDialog(null, "Digite um contacto valido", "Validacao do Contacto", 2);
            this.jTextFieldTelefone2.requestFocus();
            return false;
        }
        if (!jTextFieldEmail.getText().matches(EMAIL_PATTERN)) {
            JOptionPane.showMessageDialog(null, "Digite um email valido", "Validacao de email", 2);
            this.jTextFieldEmail.requestFocus();
            return false;
        }
        return true;
    }
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private DefaultTableModel tabelaContacto() throws SQLException {
        ArrayList<Contacto> model = (ArrayList<Contacto>) db.ListarContacto();
        int linhas = model.size();
        Object objecto[][] = new Object[linhas][30];
        if (linhas > 0) {
            for (int i = 0; i < linhas; i++) {
                Contacto c = model.get(i);
                objecto[i][0] = c.getID();
                objecto[i][1] = c.getNome();
                objecto[i][2] = c.getApelido();
                objecto[i][3] = c.getTelefone1();
                objecto[i][4] = c.getEndereco1();
                objecto[i][5] = c.getEmail();
                objecto[i][6] = c.getCategoria();
                objecto[i][7] = c.getGenero();
                objecto[i][8] = c.getDataNascimento();
            }
        }
        Object[] colunas = new Object[]{"ID", "NOME", "APELIDO", "TELEFONE 1", "ENDERECO 1", "EMAIL", "CATEGORIA", "GENERO", "DT NASCIMENTO"};
        return new DefaultTableModel(objecto, colunas);
    }

    private DefaultTableModel tabelaContactoPesquisa() throws SQLException {
        ArrayList<Contacto> model = db.pesquisarContacto(this.jTextFieldPesquisar.getText());
        int linhas = model.size();
        Object objecto[][] = new Object[linhas][30];
        if (linhas > 0) {
            for (int i = 0; i < linhas; i++) {
                Contacto c = model.get(i);
                objecto[i][0] = c.getID();
                objecto[i][1] = c.getNome();
                objecto[i][2] = c.getApelido();
                objecto[i][3] = c.getTelefone1();
                objecto[i][4] = c.getEndereco1();
                objecto[i][5] = c.getEmail();
                objecto[i][6] = c.getCategoria();
                objecto[i][7] = c.getGenero();
                objecto[i][8] = c.getDataNascimento();
            }
        }
        Object[] colunas = new Object[]{"ID", "NOME", "APELIDO", "TELEFONE 1", "ENDERECO 1", "EMAIL", "CATEGORIA", "GENERO", "DT NASCIMENTO"};
        return new DefaultTableModel(objecto, colunas);
    }

    private boolean dadosContacto() throws FileNotFoundException, IOException {
        int id = (int) jTableContacto.getValueAt(jTableContacto.getSelectedRow(), 0);
        try {
            String sql = "SELECT nome, apelido, telefone1, telefone2, endereco1, endereco2, email,"
                    + "categoria, genero, data_nascimento, foto FROM "
                    + "contacto "
                    + "WHERE id=" + id;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jTextFieldNome.setText(rs.getString("nome"));
                jTextFieldApelido.setText(rs.getString("apelido"));
                jTextFieldTelefone1.setText(rs.getString("telefone1"));
                jTextFieldTelefone2.setText(rs.getString("telefone2"));
                jTextFieldEndereco1.setText(rs.getString("endereco1"));
                jTextFieldEndereco2.setText(rs.getString("endereco2"));
                jTextFieldEmail.setText(rs.getString("email"));
                String genero = rs.getString("genero");
                if (genero.equals("Feminino")) {
                    jRadioButtonFeminino.setSelected(true);
                }
                if (genero.equals("Masculino")) {
                    jRadioButtonMasculino.setSelected(true);
                }
                Image foto = ImageIO.read(rs.getBlob("foto").getBinaryStream()).getScaledInstance(jLabelImagem.getWidth(),
                        jLabelImagem.getHeight(), Image.SCALE_SMOOTH);
                jLabelImagem.setIcon(new ImageIcon(foto));
                jLabelImagem.updateUI();
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelExcluir = new javax.swing.JLabel();
        jLabelSalvar = new javax.swing.JLabel();
        jLabelEditar = new javax.swing.JLabel();
        jLabelLog_Off = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTextFieldNome = new javax.swing.JTextField();
        jTextFieldEndereco1 = new javax.swing.JTextField();
        jTextFieldEndereco2 = new javax.swing.JTextField();
        jTextFieldApelido = new javax.swing.JTextField();
        jTextFieldTelefone1 = new javax.swing.JTextField();
        jTextFieldTelefone2 = new javax.swing.JTextField();
        jComboBoxCategoria = new javax.swing.JComboBox<>();
        jPanelGenero = new javax.swing.JPanel();
        jRadioButtonFeminino = new javax.swing.JRadioButton();
        jRadioButtonMasculino = new javax.swing.JRadioButton();
        jPanelFoto = new javax.swing.JPanel();
        jLabelImagem = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jPanelDTNascimento = new javax.swing.JPanel();
        jDateChooserDataNascimento = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableContacto = new javax.swing.JTable();
        jLabelPrint = new javax.swing.JLabel();
        jTextFieldPesquisar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AGENDA DE CONTACTO");
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelExcluir.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabelExcluir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/delete.png"))); // NOI18N
        jLabelExcluir.setText("EXCLUIR");
        jLabelExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelExcluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabelExcluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelExcluirMouseClicked(evt);
            }
        });

        jLabelSalvar.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabelSalvar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/salvar.png"))); // NOI18N
        jLabelSalvar.setText("SALVAR");
        jLabelSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabelSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSalvarMouseClicked(evt);
            }
        });

        jLabelEditar.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabelEditar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/alterar.png"))); // NOI18N
        jLabelEditar.setText("EDITAR");
        jLabelEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabelEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelEditarMouseClicked(evt);
            }
        });

        jLabelLog_Off.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabelLog_Off.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLog_Off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/sair.png"))); // NOI18N
        jLabelLog_Off.setText("LOG-OFF");
        jLabelLog_Off.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelLog_Off.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelLog_Off.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabelLog_Off.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelLog_OffMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-contact-96.png"))); // NOI18N
        jLabel1.setText("Novo");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelSalvar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelExcluir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelLog_Off, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jLabelEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabelExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabelLog_Off, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Cancel.png"))); // NOI18N
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 204));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("AGENDA TELEFONICA");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextFieldNome.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jTextFieldNome.setText("Nome");
        jTextFieldNome.setToolTipText("Nome");
        jTextFieldNome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldNomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextFieldNomeMouseExited(evt);
            }
        });
        jTextFieldNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNomeKeyTyped(evt);
            }
        });

        jTextFieldEndereco1.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jTextFieldEndereco1.setText("Endereco 1");
        jTextFieldEndereco1.setToolTipText("Endereco 1");
        jTextFieldEndereco1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldEndereco1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextFieldEndereco1MouseExited(evt);
            }
        });

        jTextFieldEndereco2.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jTextFieldEndereco2.setText("Endereco 2");
        jTextFieldEndereco2.setToolTipText("Endereco 2");
        jTextFieldEndereco2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldEndereco2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextFieldEndereco2MouseExited(evt);
            }
        });

        jTextFieldApelido.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jTextFieldApelido.setText("Apelido");
        jTextFieldApelido.setToolTipText("Apelido");
        jTextFieldApelido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldApelidoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextFieldApelidoMouseExited(evt);
            }
        });
        jTextFieldApelido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldApelidoKeyTyped(evt);
            }
        });

        jTextFieldTelefone1.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jTextFieldTelefone1.setText("Telefone 1");
        jTextFieldTelefone1.setToolTipText("Telefone 1");
        jTextFieldTelefone1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldTelefone1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextFieldTelefone1MouseExited(evt);
            }
        });
        jTextFieldTelefone1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldTelefone1KeyTyped(evt);
            }
        });

        jTextFieldTelefone2.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jTextFieldTelefone2.setText("Telefone 2");
        jTextFieldTelefone2.setToolTipText("Telefone 2");
        jTextFieldTelefone2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldTelefone2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextFieldTelefone2MouseExited(evt);
            }
        });
        jTextFieldTelefone2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldTelefone2KeyTyped(evt);
            }
        });

        jComboBoxCategoria.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jComboBoxCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<<Escolhe a categoria>>", "Amigo", "Colega", "Conhecido", "Docente", "Familia", "Outro" }));
        jComboBoxCategoria.setToolTipText("Categoria");

        jPanelGenero.setBackground(new java.awt.Color(255, 255, 255));
        jPanelGenero.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Genero", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Consolas", 0, 18))); // NOI18N
        jPanelGenero.setToolTipText("Genero");
        jPanelGenero.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N

        jRadioButtonFeminino.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jRadioButtonFeminino.setText("Feminino");

        jRadioButtonMasculino.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jRadioButtonMasculino.setText("Masculino");

        javax.swing.GroupLayout jPanelGeneroLayout = new javax.swing.GroupLayout(jPanelGenero);
        jPanelGenero.setLayout(jPanelGeneroLayout);
        jPanelGeneroLayout.setHorizontalGroup(
            jPanelGeneroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeneroLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanelGeneroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonMasculino)
                    .addComponent(jRadioButtonFeminino))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelGeneroLayout.setVerticalGroup(
            jPanelGeneroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeneroLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jRadioButtonFeminino)
                .addGap(10, 10, 10)
                .addComponent(jRadioButtonMasculino)
                .addGap(10, 10, 10))
        );

        jPanelFoto.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFoto.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Foto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Consolas", 0, 18))); // NOI18N
        jPanelFoto.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N

        jLabelImagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelImagemMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelFotoLayout = new javax.swing.GroupLayout(jPanelFoto);
        jPanelFoto.setLayout(jPanelFotoLayout);
        jPanelFotoLayout.setHorizontalGroup(
            jPanelFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFotoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelImagem, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelFotoLayout.setVerticalGroup(
            jPanelFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFotoLayout.createSequentialGroup()
                .addComponent(jLabelImagem, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTextFieldEmail.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jTextFieldEmail.setText("Email");
        jTextFieldEmail.setToolTipText("Email");
        jTextFieldEmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldEmailMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextFieldEmailMouseExited(evt);
            }
        });

        jPanelDTNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDTNascimento.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Data de Nascimento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Consolas", 0, 18))); // NOI18N
        jPanelDTNascimento.setToolTipText("Data Nascimento");

        jDateChooserDataNascimento.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanelDTNascimentoLayout = new javax.swing.GroupLayout(jPanelDTNascimento);
        jPanelDTNascimento.setLayout(jPanelDTNascimentoLayout);
        jPanelDTNascimentoLayout.setHorizontalGroup(
            jPanelDTNascimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDTNascimentoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jDateChooserDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanelDTNascimentoLayout.setVerticalGroup(
            jPanelDTNascimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDTNascimentoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jDateChooserDataNascimento, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/contactologin.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jComboBoxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldTelefone2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldApelido, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldEndereco1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldEndereco2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jTextFieldEmail))
                .addGap(29, 29, 29)
                .addComponent(jPanelFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelDTNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldApelido, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldEndereco1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldTelefone2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldEndereco2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel7))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(jComboBoxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanelGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDTNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTableContacto.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jTableContacto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableContacto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableContactoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableContacto);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabelPrint.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/printer.png"))); // NOI18N
        jLabelPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPrintMouseClicked(evt);
            }
        });

        jTextFieldPesquisar.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jTextFieldPesquisar.setText("Pesquisar");
        jTextFieldPesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldPesquisarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextFieldPesquisarMouseExited(evt);
            }
        });
        jTextFieldPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPesquisarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPesquisarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelPrint)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jTextFieldPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        if (evt != null)
            System.exit(0);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jTextFieldNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNomeKeyTyped
        String caracteres = "`~!@#$%^&*()_+={}[]|';:/?.>,<*-";
        if (caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNomeKeyTyped

    private void jTextFieldApelidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldApelidoKeyTyped
        String caracteres = "`~!@#$%^&*()_+={}[]|';:/?.>,<*-";
        if (caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldApelidoKeyTyped

    private void jTextFieldPesquisarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPesquisarKeyTyped
        String caracteres = "`~!@#$%^&*()_+={}[]|';:/?.>,<*-";
        if (caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPesquisarKeyTyped

    private void jTextFieldTelefone1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldTelefone1KeyTyped
        String caracteres = "`~!@#$%^&*()_+={}[]|';:/?.>,<*-";
        if (caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldTelefone1KeyTyped

    private void jTextFieldTelefone2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldTelefone2KeyTyped
        String caracteres = "`~!@#$%^&*()_+={}[]|';:/?.>,<*-";
        if (caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldTelefone2KeyTyped

    private void jTextFieldNomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldNomeMouseEntered
        if (evt != null) {
            if (this.jTextFieldNome.getText().equals("Nome")) {
                this.jTextFieldNome.setText("");
            }
        }
    }//GEN-LAST:event_jTextFieldNomeMouseEntered

    private void jTextFieldNomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldNomeMouseExited
        if (evt != null) {
            if (this.jTextFieldNome.getText().isEmpty()) {
                this.jTextFieldNome.setText("Nome");
            }
        }
    }//GEN-LAST:event_jTextFieldNomeMouseExited

    private void jTextFieldApelidoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldApelidoMouseEntered
        if (evt != null) {
            if (this.jTextFieldApelido.getText().equals("Apelido")) {
                this.jTextFieldApelido.setText("");
            }
        }
    }//GEN-LAST:event_jTextFieldApelidoMouseEntered

    private void jTextFieldApelidoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldApelidoMouseExited
        if (evt != null) {
            if (this.jTextFieldApelido.getText().isEmpty()) {
                this.jTextFieldApelido.setText("Apelido");
            }
        }
    }//GEN-LAST:event_jTextFieldApelidoMouseExited

    private void jTextFieldTelefone1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldTelefone1MouseEntered
        if (evt != null) {
            if (this.jTextFieldTelefone1.getText().equals("Telefone 1")) {
                this.jTextFieldTelefone1.setText("");
            }
        }
    }//GEN-LAST:event_jTextFieldTelefone1MouseEntered

    private void jTextFieldTelefone1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldTelefone1MouseExited
        if (evt != null) {
            if (this.jTextFieldTelefone1.getText().isEmpty()) {
                this.jTextFieldTelefone1.setText("Telefone 1");
            }
        }
    }//GEN-LAST:event_jTextFieldTelefone1MouseExited

    private void jTextFieldTelefone2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldTelefone2MouseEntered
        if (evt != null) {
            if (this.jTextFieldTelefone2.getText().equals("Telefone 2")) {
                this.jTextFieldTelefone2.setText("");
            }
        }
    }//GEN-LAST:event_jTextFieldTelefone2MouseEntered

    private void jTextFieldTelefone2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldTelefone2MouseExited
        if (evt != null) {
            if (this.jTextFieldTelefone2.getText().isEmpty()) {
                this.jTextFieldTelefone2.setText("Telefone 2");
            }
        }
    }//GEN-LAST:event_jTextFieldTelefone2MouseExited

    private void jTextFieldEndereco1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldEndereco1MouseEntered
        if (evt != null) {
            if (this.jTextFieldEndereco1.getText().equals("Endereco 1")) {
                this.jTextFieldEndereco1.setText("");
            }
        }
    }//GEN-LAST:event_jTextFieldEndereco1MouseEntered

    private void jTextFieldEndereco1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldEndereco1MouseExited
        if (evt != null) {
            if (this.jTextFieldEndereco1.getText().isEmpty()) {
                this.jTextFieldEndereco1.setText("Endereco 1");
            }
        }
    }//GEN-LAST:event_jTextFieldEndereco1MouseExited

    private void jTextFieldEndereco2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldEndereco2MouseEntered
        if (evt != null) {
            if (this.jTextFieldEndereco2.getText().equals("Endereco 2")) {
                this.jTextFieldEndereco2.setText("");
            }
        }
    }//GEN-LAST:event_jTextFieldEndereco2MouseEntered

    private void jTextFieldEndereco2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldEndereco2MouseExited
        if (evt != null) {
            if (this.jTextFieldEndereco2.getText().isEmpty()) {
                this.jTextFieldEndereco2.setText("Endereco 2");
            }
        }
    }//GEN-LAST:event_jTextFieldEndereco2MouseExited

    private void jTextFieldEmailMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldEmailMouseEntered
        if (evt != null) {
            if (this.jTextFieldEmail.getText().equals("Email")) {
                this.jTextFieldEmail.setText("");
            }
        }
    }//GEN-LAST:event_jTextFieldEmailMouseEntered

    private void jTextFieldEmailMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldEmailMouseExited
        if (evt != null) {
            if (this.jTextFieldEmail.getText().isEmpty()) {
                this.jTextFieldEmail.setText("Email");
            }
        }
    }//GEN-LAST:event_jTextFieldEmailMouseExited

    private void jTextFieldPesquisarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldPesquisarMouseEntered
        if (evt != null) {
            if (this.jTextFieldPesquisar.getText().equals("Pesquisar")) {
                this.jTextFieldPesquisar.setText("");
            }
        }
    }//GEN-LAST:event_jTextFieldPesquisarMouseEntered

    private void jTextFieldPesquisarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldPesquisarMouseExited
        if (evt != null) {
            if (this.jTextFieldPesquisar.getText().isEmpty()) {
                this.jTextFieldPesquisar.setText("Pesquisar");
            }
        }
    }//GEN-LAST:event_jTextFieldPesquisarMouseExited

    private void jLabelSalvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSalvarMouseClicked
        if (evt != null) {
            try {
                if (camposVazios()) {
                    if (validarCampos()) {
                        salvarContacto();
                        limparCampos();
                        this.jTextFieldNome.requestFocus();
                        jTableContacto.setModel(tabelaContacto());
                        jTextFieldPesquisar.setText("Pesquisar");
                    }
                }
            } catch (SQLException e) {
                System.out.println(e);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(AgendaContacto.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jLabelSalvarMouseClicked

    private void jLabelExcluirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelExcluirMouseClicked
        if (evt != null) {
            if (jTableContacto.getSelectedRow() != -1) {
                try {
                    int id = (int) jTableContacto.getValueAt(jTableContacto.getSelectedRow(), 0);
                    db.excluirContacto(id);
                    jTableContacto.setModel(tabelaContacto());
                    limparCampos();
                    jTextFieldPesquisar.setText("Pesquisar");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um registro da tabela", "Selecao", 2);
            }
        }
    }//GEN-LAST:event_jLabelExcluirMouseClicked

    private void jLabelPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPrintMouseClicked
        if (evt != null) {
            if (jTableContacto.getSelectedRow() != -1) {
                int id = (int) jTableContacto.getValueAt(jTableContacto.getSelectedRow(), 0);
                Map par = new HashMap();
                try {
                    par.put("id", id);
                    URL file = this.getClass().getResource("/Report/contacto_id.jasper");
                    JasperReport jr = (JasperReport) JRLoader.loadObject(file);
                    JasperPrint jp = JasperFillManager.fillReport(jr, par, con);
                    JasperViewer v = new JasperViewer(jp, false);
                    v.setVisible(true);
                    limparCampos();
                } catch (JRException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                try {
                    URL file = this.getClass().getResource("/Report/contacto.jasper");
                    JasperReport jr = (JasperReport) JRLoader.loadObject(file);
                    JasperPrint jp = JasperFillManager.fillReport(jr, null, con);
                    JasperViewer v = new JasperViewer(jp, false);
                    v.setVisible(true);
                    limparCampos();
                } catch (JRException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            try {
                jTableContacto.setModel(tabelaContacto());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }//GEN-LAST:event_jLabelPrintMouseClicked

    private void jTextFieldPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPesquisarKeyReleased
        if (evt != null) {
            try {
                jTableContacto.setModel(tabelaContactoPesquisa());
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_jTextFieldPesquisarKeyReleased

    private void jLabelEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelEditarMouseClicked
        if (evt != null) {
            if (jTableContacto.getSelectedRow() != -1) {
                if (camposVazios()) {
                    if (validarCampos()) {
                        try {
                            int id = (int) jTableContacto.getValueAt(jTableContacto.getSelectedRow(), 0);
                            contacto.setID(id);
                            contacto.setNome(this.jTextFieldNome.getText());
                            contacto.setApelido(this.jTextFieldApelido.getText());
                            contacto.setTelefone1(this.jTextFieldTelefone1.getText());
                            contacto.setTelefone2(this.jTextFieldTelefone2.getText());
                            contacto.setEndereco1(this.jTextFieldEndereco1.getText());
                            contacto.setEndereco2(this.jTextFieldEndereco2.getText());
                            contacto.setEmail(this.jTextFieldEmail.getText());

                            contacto.setCategoria(this.jComboBoxCategoria.getSelectedItem().toString());
                            if (jRadioButtonFeminino.isSelected()) {
                                contacto.setGenero(jRadioButtonFeminino.getText());
                            }
                            if (jRadioButtonMasculino.isSelected()) {
                                contacto.setGenero(jRadioButtonMasculino.getText());
                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String addDate = dateFormat.format(jDateChooserDataNascimento.getDate());
                            contacto.setDataNascimento(addDate);
                            contacto.setFileInputStream(fis);
                            contacto.setTamanho(n);
                            db.editarContacto(contacto);
                            limparCampos();
                            jTableContacto.setModel(tabelaContacto());

                        } catch (SQLException ex) {
                            Logger.getLogger(AgendaContacto.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um registo da tabela!");
            }
        }
    }//GEN-LAST:event_jLabelEditarMouseClicked


    private void jLabelImagemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelImagemMouseClicked
        if (evt != null) {
            JFileChooser file = new JFileChooser();
            file.setDialogTitle("Selecionar arquivo");
            file.setFileFilter(new FileNameExtensionFilter("Arquivo de inagens (*.PNG, *.JPG, *.JPEG)", "png", "jpg", "jpeg"));

            int load = file.showOpenDialog(this);
            if (load == JFileChooser.APPROVE_OPTION) {
                try {
                    fis = new FileInputStream(file.getSelectedFile());
                    n = (int) file.getSelectedFile().length();
                    Image foto = ImageIO.read(file.getSelectedFile()).getScaledInstance(jLabelImagem.getWidth(),
                            jLabelImagem.getHeight(), Image.SCALE_SMOOTH);
                    jLabelImagem.setIcon(new ImageIcon(foto));
                    jLabelImagem.updateUI();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao carregar a imagem " + e);
                }
            }
        }
    }//GEN-LAST:event_jLabelImagemMouseClicked

    private void jTableContactoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableContactoMouseClicked
        if (evt != null) {
            try {
                dadosContacto();
                jTextFieldPesquisar.setText("Pesquisar");
            } catch (IOException ex) {
                Logger.getLogger(AgendaContacto.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_jTableContactoMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        if (evt != null) {
            try {
                limparCampos();
                jTableContacto.setModel(tabelaContacto());

            } catch (SQLException ex) {
                Logger.getLogger(AgendaContacto.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabelLog_OffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelLog_OffMouseClicked
        try {
            new Login().setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error ao fazer logoff "+ex);
        }
        this.dispose();
    }//GEN-LAST:event_jLabelLog_OffMouseClicked

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new AgendaContacto().setVisible(true);

            } catch (SQLException ex) {
                Logger.getLogger(AgendaContacto.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBoxCategoria;
    private com.toedter.calendar.JDateChooser jDateChooserDataNascimento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelEditar;
    private javax.swing.JLabel jLabelExcluir;
    private javax.swing.JLabel jLabelImagem;
    private javax.swing.JLabel jLabelLog_Off;
    private javax.swing.JLabel jLabelPrint;
    private javax.swing.JLabel jLabelSalvar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelDTNascimento;
    private javax.swing.JPanel jPanelFoto;
    private javax.swing.JPanel jPanelGenero;
    private javax.swing.JRadioButton jRadioButtonFeminino;
    private javax.swing.JRadioButton jRadioButtonMasculino;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableContacto;
    private javax.swing.JTextField jTextFieldApelido;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldEndereco1;
    private javax.swing.JTextField jTextFieldEndereco2;
    private javax.swing.JTextField jTextFieldNome;
    private javax.swing.JTextField jTextFieldPesquisar;
    private javax.swing.JTextField jTextFieldTelefone1;
    private javax.swing.JTextField jTextFieldTelefone2;
    // End of variables declaration//GEN-END:variables
}
