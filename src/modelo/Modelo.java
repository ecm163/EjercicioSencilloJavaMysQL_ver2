/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import vista.Vista;
import controlador.Controlador;
import static vista.Vista.grupoRadioBotones;
//import static vista.Vista.radioBotonEdad;
//import static vista.Vista.radioBotonId;
//import static vista.Vista.radioBotonNombre;

/**
 *
 * @author Emilio
 */
public class Modelo {

    Connection cc;
    Connection cn = Conexion();
    Vista view = new Vista();
    public static String atributo = "Id";
    public static String valor = "";

//    public Modelo(String atributo) {
//        
//        this.valor = atributo;
//        
//    }
       

    public Connection Conexion() {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            cc = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema", "root", "");
            System.out.println("Hecha la conexión con éxito.");

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
        return cc;

    }

    public void mostrartabla(String valor) {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Edad");
        modelo.addColumn("Correo");

        view.tabla_datos.setModel(modelo);

        //String sql = "SELECT * FROM usuario";
        String sql ="";
        if(valor.equals("")){
            sql = "SELECT * FROM usuario";
        }
        else{
            sql = "SELECT * FROM usuario WHERE " + atributo + " = '" + valor +"'";
        }
        
        String datos[] = new String[5];
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {

                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                modelo.addRow(datos);

            }
            view.tabla_datos.setModel(modelo);
            
            if(modelo.getRowCount()==0){
                JOptionPane.showMessageDialog(null, "La búsqueda no ha tenido éxito.");
            }

        } catch (SQLException ex) {
            System.out.println("Se ha producido una excepción en la función mostrartabla()");
        }

    }

    public void limpiar() {
        view.txt_nombre.setText("");
        view.txt_apellidos.setText("");
        view.txt_edad.setText("");
        view.txt_correo.setText("");
        view.txt_buscar.setText("");
    }

    public void Guardar() {
        
        try {

            PreparedStatement pps = cn.prepareStatement("INSERT INTO usuario(Nombre, Apellidos, Edad, Correo) VALUES(?, ?, ?, ?)");
            pps.setString(1, view.txt_nombre.getText().trim());
            pps.setString(2, view.txt_apellidos.getText().trim());
            pps.setString(3, view.txt_edad.getText().trim());
            pps.setString(4, view.txt_correo.getText().trim());
            pps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Datos guardados.");
            mostrartabla("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error.");
        }
        
    }
    
    public void Modificar(){
        
        int fila = view.tabla_datos.getSelectedRow();
        if(fila >= 0){
            
            view.txt_buscar.setText(view.tabla_datos.getValueAt(fila, 0).toString());
            view.txt_nombre.setText(view.tabla_datos.getValueAt(fila, 1).toString());
            view.txt_apellidos.setText(view.tabla_datos.getValueAt(fila, 2).toString());
            view.txt_edad.setText(view.tabla_datos.getValueAt(fila, 3).toString());
            view.txt_correo.setText(view.tabla_datos.getValueAt(fila, 4).toString());
        }
        else{
            JOptionPane.showMessageDialog(null, "Fila no seleccionada.");
        }
        
    }
    
    public void Atualizar(){
        
        try {
            PreparedStatement pps = cn.prepareStatement("UPDATE usuario SET Nombre = '"+view.txt_nombre.getText()+
                    "' ,Apellidos = '"+view.txt_apellidos.getText()+"' ,Edad = '"+view.txt_edad.getText()+
                    "' ,Correo ='"+view.txt_correo.getText()+"' WHERE Id = "+view.txt_buscar.getText()+"");
            pps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Datos Actualizados.");
            limpiar();
            mostrartabla("");
            
        } catch (SQLException ex) {
            System.out.println("Error en Actualizar()");
        }
        
    }
    
    public void Eliminar(){
        
        int fila = view.tabla_datos.getSelectedRow();
        String valor = view.tabla_datos.getValueAt(fila, 0).toString();
        if(fila >= 0){
            
            try {
                PreparedStatement pps = cn.prepareStatement("DELETE FROM usuario WHERE Id = '"+valor+"'");
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Dato eliminado.");
                mostrartabla("");
            } catch (SQLException ex) {
                System.out.println("Error en Eliminar()");
            }
            
        }
        else{
            
            JOptionPane.showMessageDialog(null, "La tabla está vacía.");
            
        }
        
    }
    //El método Buscar será diferente a la versión anterior hecha con radio-buttons.
    public void Buscar(){
        
        atributo = view.combo.getSelectedItem().toString();
        mostrartabla(view.txt_buscar.getText());

    }
    
    public void agregarItem(){
        
        view.combo.addItem("Id");
        view.combo.addItem("Nombre");
        view.combo.addItem("Apellidos");
        view.combo.addItem("Edad");
        view.combo.addItem("Correo");
        
    }

}
