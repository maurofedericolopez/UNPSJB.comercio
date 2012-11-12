package comercio.vistas;

import comercio.controladores.ProductosController;
import comercio.modelo.Marca;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Mauro Federico Lopez
 */
public class NuevoProductoUI extends javax.swing.JDialog {

    private ProductosController controlador;

    /**
     * Creates new form NuevoProductoUI
     */
    public NuevoProductoUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        controlador = new ProductosController();
        campoPrecio.setValue(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelNuevoProducto = new javax.swing.JPanel();
        etiquetaCodigo = new javax.swing.JLabel();
        campoCodigo = new javax.swing.JTextField();
        etiquetaDescripcion = new javax.swing.JLabel();
        campoDescripcion = new javax.swing.JTextField();
        etiquetaPrecio = new javax.swing.JLabel();
        campoPrecio = new javax.swing.JFormattedTextField();
        etiquetaMarca = new javax.swing.JLabel();
        campoMarca = new javax.swing.JComboBox();
        etiquetaOrigen = new javax.swing.JLabel();
        campoOrigen = new javax.swing.JComboBox();
        etiquetaUnidad = new javax.swing.JLabel();
        campoUnidad = new javax.swing.JComboBox();
        etiquetaCategoria = new javax.swing.JLabel();
        campoCategoria = new javax.swing.JComboBox();
        botonGuardar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nuevo Producto");

        panelNuevoProducto.setMaximumSize(new java.awt.Dimension(280, 280));
        panelNuevoProducto.setMinimumSize(new java.awt.Dimension(280, 280));
        panelNuevoProducto.setPreferredSize(new java.awt.Dimension(280, 280));
        panelNuevoProducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        etiquetaCodigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        etiquetaCodigo.setText("Código");
        panelNuevoProducto.add(etiquetaCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, -1));
        panelNuevoProducto.add(campoCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 130, -1));

        etiquetaDescripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        etiquetaDescripcion.setText("Descripción");
        panelNuevoProducto.add(etiquetaDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, -1));
        panelNuevoProducto.add(campoDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 130, -1));

        etiquetaPrecio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        etiquetaPrecio.setText("Precio unitario");
        panelNuevoProducto.add(etiquetaPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, -1));

        campoPrecio.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat())));
        panelNuevoProducto.add(campoPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 130, -1));

        etiquetaMarca.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        etiquetaMarca.setText("Marca");
        panelNuevoProducto.add(etiquetaMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, -1));

        campoMarca.setModel(new comercio.vistas.modelos.MarcaComboBoxModel());
        panelNuevoProducto.add(campoMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 130, -1));

        etiquetaOrigen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        etiquetaOrigen.setText("Origen");
        panelNuevoProducto.add(etiquetaOrigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, -1));

        campoOrigen.setModel(new comercio.vistas.modelos.OrigenComboBoxModel());
        panelNuevoProducto.add(campoOrigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 130, -1));

        etiquetaUnidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        etiquetaUnidad.setText("Unidad");
        panelNuevoProducto.add(etiquetaUnidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 90, -1));

        campoUnidad.setModel(new comercio.vistas.modelos.UnidadComboBoxModel());
        panelNuevoProducto.add(campoUnidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 130, -1));

        etiquetaCategoria.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        etiquetaCategoria.setText("Categoría");
        panelNuevoProducto.add(etiquetaCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 90, -1));

        campoCategoria.setModel(new comercio.vistas.modelos.CategoriaComboBoxModel());
        panelNuevoProducto.add(campoCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 130, -1));

        botonGuardar.setText("Guardar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });
        panelNuevoProducto.add(botonGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, -1, -1));

        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });
        panelNuevoProducto.add(botonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, -1, -1));

        getContentPane().add(panelNuevoProducto, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        try {
            controlador.registrarNuevoProducto(campoCodigo.getText(), campoDescripcion.getText(), campoPrecio.getValue(), campoMarca.getSelectedItem(), campoOrigen.getSelectedItem(), campoUnidad.getSelectedItem(), campoCategoria.getSelectedItem());
            this.setVisible(false);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonGuardarActionPerformed

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        campoCodigo.setText("");
        campoDescripcion.setText("");
        campoPrecio.setValue(0);
        campoMarca.setSelectedItem(null);
        campoMarca.updateUI();
        campoOrigen.setSelectedItem(null);
        campoOrigen.updateUI();
        campoUnidad.setSelectedItem(null);
        campoUnidad.updateUI();
        campoCategoria.setSelectedItem(null);
        campoCategoria.updateUI();
    }//GEN-LAST:event_botonCancelarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NuevoProductoUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NuevoProductoUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NuevoProductoUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NuevoProductoUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                NuevoProductoUI dialog = new NuevoProductoUI(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JComboBox campoCategoria;
    private javax.swing.JTextField campoCodigo;
    private javax.swing.JTextField campoDescripcion;
    private javax.swing.JComboBox campoMarca;
    private javax.swing.JComboBox campoOrigen;
    private javax.swing.JFormattedTextField campoPrecio;
    private javax.swing.JComboBox campoUnidad;
    private javax.swing.JLabel etiquetaCategoria;
    private javax.swing.JLabel etiquetaCodigo;
    private javax.swing.JLabel etiquetaDescripcion;
    private javax.swing.JLabel etiquetaMarca;
    private javax.swing.JLabel etiquetaOrigen;
    private javax.swing.JLabel etiquetaPrecio;
    private javax.swing.JLabel etiquetaUnidad;
    private javax.swing.JPanel panelNuevoProducto;
    // End of variables declaration//GEN-END:variables
}