package Controller;

import Model.Contacto;
import java.awt.*;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.*;

public final class ContactoDB {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public ContactoDB() throws SQLException {
        con = getConexao();
    }

    public Connection getConexao() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/"
                    + "agendacontacto", "root", "");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return null;
    }

    public List ListarContacto() {
        ArrayList<Contacto> lista = new ArrayList<>();
        String sql = "SELECT * FROM contacto";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Contacto c = new Contacto();
                c.setID(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setApelido(rs.getString("apelido"));
                c.setTelefone1(rs.getString("telefone1"));
                c.setTelefone2(rs.getString("telefone2"));
                c.setEndereco1(rs.getString("endereco1"));
                c.setEndereco2(rs.getString("endereco2"));
                c.setEmail(rs.getString("email"));
                c.setCategoria(rs.getString("categoria"));
                c.setGenero(rs.getString("genero"));
                c.setDataNascimento(rs.getString(
                        "data_nascimento"));
                // c.setFoto(rs.getString("foto"));
                lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return lista;
    }

    public ArrayList<Contacto> pesquisarContacto(String nome) {
        ArrayList<Contacto> dd = new ArrayList<>();
        String sql = "SELECT * FROM contacto where nome LIKE '%" + nome + "%'";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Contacto c = new Contacto();
                c.setID(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setApelido(rs.getString("apelido"));
                c.setTelefone1(rs.getString("telefone1"));
                c.setTelefone2(rs.getString("telefone2"));
                c.setEndereco1(rs.getString("endereco1"));
                c.setEndereco2(rs.getString("endereco2"));
                c.setEmail(rs.getString("email"));
                c.setCategoria(rs.getString("categoria"));
                c.setGenero(rs.getString("genero"));
                c.setDataNascimento(rs.getString(
                        "data_nascimento"));
                //c.setFoto(rs.getString("foto"));
                dd.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Pesquisa nao encontrada " + e);
        }
        return dd;
    }

    public void editarContacto(Contacto c) {
        String sql = "UPDATE contacto SET nome=?, apelido=?, telefone1=?, "
                + "telefone2=?, endereco1=?, endereco2=?, "
                + "email=?, categoria=?, genero=?, data_nascimento=?, foto=? "
                + "WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(12, c.getID());
            ps.setString(1, c.getNome());
            ps.setString(2, c.getApelido());
            ps.setString(3, c.getTelefone1());
            ps.setString(4, c.getTelefone2());
            ps.setString(5, c.getEndereco1());
            ps.setString(6, c.getEndereco2());
            ps.setString(7, c.getEmail());
            ps.setString(8, c.getCategoria());
            ps.setString(9, c.getGenero());
            ps.setString(10, c.getDataNascimento());
            ps.setBlob(11, c.getFoto(), c.getTamanho());
            ps.execute();
            ps.close();
            JOptionPane.showMessageDialog(null,
                    "Contacto foi actualizado com sucesso");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Contacto nao actualizado " + e);
        }
    }

    public void excluirContacto(int id) {
        String sql = "DELETE FROM contacto WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            ps.close();
            JOptionPane.showMessageDialog(null,
                    "Contacto excluido com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Contacto nao excluido " + e);
        }
    }


    public void salvar(Contacto c) throws FileNotFoundException {
        String sql = "INSERT INTO contacto (nome, apelido, telefone1, "
                + "telefone2, endereco1, endereco2, email, categoria, genero, "
                + "data_nascimento, foto) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getApelido());
            ps.setString(3, c.getTelefone1());
            ps.setString(4, c.getTelefone2());
            ps.setString(5, c.getEndereco1());
            ps.setString(6, c.getEndereco2());
            ps.setString(7, c.getEmail());
            ps.setString(8, c.getCategoria());
            ps.setString(9, c.getGenero());
            ps.setString(10, c.getDataNascimento());
            ps.setBlob(11, c.getFoto(), c.getTamanho());
            ps.execute();
            ps.close();
            JOptionPane.showMessageDialog(null,
                    "Contacto salvo");
        } catch (SQLException x) {
            JOptionPane.showMessageDialog(null,
                    "Contacto nao salvo " + x);
        }
    }
    /*public static void main(String[] args) {
        try {
            ContactoDB c = new ContactoDB();
            c.getConexao();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error "+e, null, 0);
        }
        
    }
    */
}
