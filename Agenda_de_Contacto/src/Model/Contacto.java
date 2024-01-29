package Model;

import java.io.FileInputStream;

public class Contacto {
    String nome, apelido, telefone1, telefone2, endereco1, endereco2, email,
            categoria, genero, data_nascimento;
    FileInputStream file;
    int id, tamanho;
    
    public int getTamanho(){
        return tamanho;
    }
    public void setTamanho(int n){
        tamanho=n;
    }
    public FileInputStream getFoto(){
        return file;
    }
    public void setFileInputStream(FileInputStream f){
        file=f;
    }
    public void setID(int id) {
        this.id = id;
    }
    public int getID() {
        return this.id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return this.nome;
    }
    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
    public String getApelido() {
        return this.apelido;
    }
    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }
    public String getTelefone1() {
        return this.telefone1;
    }
    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }
    public String getTelefone2() {
        return this.telefone2;
    }
    public void setEndereco1(String endereco1) {
        this.endereco1 = endereco1;
    }
    public String getEndereco1() {
        return this.endereco1;
    }
    public void setEndereco2(String endereco2) {
        this.endereco2 = endereco2;
    }
    public String getEndereco2() {
        return this.endereco2;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getCategoria() {
        return this.categoria;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public String getGenero() {
        return this.genero;
    }
    public void setDataNascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }
    public String getDataNascimento() {
        return this.data_nascimento;
    }


}
