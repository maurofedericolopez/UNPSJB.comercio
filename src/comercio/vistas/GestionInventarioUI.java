package comercio.vistas;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JComponent;

/**
 *
 * @author Mauro Federico Lopez
 */
public class GestionInventarioUI extends javax.swing.JFrame {

    /**
     * Creates new form GestionInventarioUI
     */
    public GestionInventarioUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barraMenu = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        menuAdministracion = new javax.swing.JMenu();
        itemMenuAdministrarProductos = new javax.swing.JMenuItem();
        itemMenuAdministrarCategorias = new javax.swing.JMenuItem();
        itemMenuAdministrarMarcas = new javax.swing.JMenuItem();
        itemMenuAdministrarOrigen = new javax.swing.JMenuItem();
        itemMenuAdministrarUnidad = new javax.swing.JMenuItem();
        menuOperaciones = new javax.swing.JMenu();
        itemMenuImportarLotes = new javax.swing.JMenuItem();
        itemMenuTransferencia = new javax.swing.JMenuItem();
        itemMenuEgreso = new javax.swing.JMenuItem();
        itemMenuCorregirInventarioManualmente = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión de inventario");
        setIconImage(getIconImage());
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(950, 550));
        setMinimumSize(new java.awt.Dimension(950, 550));
        setPreferredSize(new java.awt.Dimension(950, 550));
        setResizable(false);

        menuArchivo.setText("Archivo");
        barraMenu.add(menuArchivo);

        menuAdministracion.setText("Administración");

        itemMenuAdministrarProductos.setText("Administrar Productos");
        itemMenuAdministrarProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAdministrarProductosActionPerformed(evt);
            }
        });
        menuAdministracion.add(itemMenuAdministrarProductos);

        itemMenuAdministrarCategorias.setText("Administrar Categorias");
        itemMenuAdministrarCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAdministrarCategoriasActionPerformed(evt);
            }
        });
        menuAdministracion.add(itemMenuAdministrarCategorias);

        itemMenuAdministrarMarcas.setText("Administrar Marcas");
        itemMenuAdministrarMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAdministrarMarcasActionPerformed(evt);
            }
        });
        menuAdministracion.add(itemMenuAdministrarMarcas);

        itemMenuAdministrarOrigen.setText("Administrar Origenes");
        itemMenuAdministrarOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAdministrarOrigenActionPerformed(evt);
            }
        });
        menuAdministracion.add(itemMenuAdministrarOrigen);

        itemMenuAdministrarUnidad.setText("Administrar Unidades");
        itemMenuAdministrarUnidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAdministrarUnidadActionPerformed(evt);
            }
        });
        menuAdministracion.add(itemMenuAdministrarUnidad);

        barraMenu.add(menuAdministracion);

        menuOperaciones.setText("Operaciones");

        itemMenuImportarLotes.setText("Importar lotes de productos");
        itemMenuImportarLotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuImportarLotesActionPerformed(evt);
            }
        });
        menuOperaciones.add(itemMenuImportarLotes);

        itemMenuTransferencia.setText("Realizar transferencias de productos");
        itemMenuTransferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuTransferenciaActionPerformed(evt);
            }
        });
        menuOperaciones.add(itemMenuTransferencia);

        itemMenuEgreso.setText("Egresar lotes de productos");
        itemMenuEgreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuEgresoActionPerformed(evt);
            }
        });
        menuOperaciones.add(itemMenuEgreso);

        itemMenuCorregirInventarioManualmente.setText("Corregir Inventario Manualmente");
        itemMenuCorregirInventarioManualmente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuCorregirInventarioManualmenteActionPerformed(evt);
            }
        });
        menuOperaciones.add(itemMenuCorregirInventarioManualmente);

        barraMenu.add(menuOperaciones);

        setJMenuBar(barraMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemMenuAdministrarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAdministrarProductosActionPerformed
        agregarComponente(new AdministrarProductosUI());
    }//GEN-LAST:event_itemMenuAdministrarProductosActionPerformed

    private void itemMenuAdministrarCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAdministrarCategoriasActionPerformed
        agregarComponente(new AdministrarCategoriasUI());
    }//GEN-LAST:event_itemMenuAdministrarCategoriasActionPerformed

    private void itemMenuAdministrarMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAdministrarMarcasActionPerformed
        agregarComponente(new AdministrarMarcasUI());
    }//GEN-LAST:event_itemMenuAdministrarMarcasActionPerformed

    private void itemMenuImportarLotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuImportarLotesActionPerformed
        agregarComponente(new ImportarUI());
    }//GEN-LAST:event_itemMenuImportarLotesActionPerformed

    private void itemMenuTransferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuTransferenciaActionPerformed
        agregarComponente(new TransferenciaUI());
    }//GEN-LAST:event_itemMenuTransferenciaActionPerformed

    private void itemMenuEgresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuEgresoActionPerformed
        agregarComponente(new EgresoUI());
    }//GEN-LAST:event_itemMenuEgresoActionPerformed

    private void itemMenuCorregirInventarioManualmenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuCorregirInventarioManualmenteActionPerformed
        agregarComponente(new CorregirInventarioUI());
    }//GEN-LAST:event_itemMenuCorregirInventarioManualmenteActionPerformed

    private void itemMenuAdministrarUnidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAdministrarUnidadActionPerformed
        agregarComponente(new AdministrarUnidadesUI());
    }//GEN-LAST:event_itemMenuAdministrarUnidadActionPerformed

    private void itemMenuAdministrarOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAdministrarOrigenActionPerformed
        agregarComponente(new AdministrarOrigenesUI());
    }//GEN-LAST:event_itemMenuAdministrarOrigenActionPerformed

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
            java.util.logging.Logger.getLogger(GestionInventarioUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestionInventarioUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestionInventarioUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionInventarioUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new GestionInventarioUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JMenuItem itemMenuAdministrarCategorias;
    private javax.swing.JMenuItem itemMenuAdministrarMarcas;
    private javax.swing.JMenuItem itemMenuAdministrarOrigen;
    private javax.swing.JMenuItem itemMenuAdministrarProductos;
    private javax.swing.JMenuItem itemMenuAdministrarUnidad;
    private javax.swing.JMenuItem itemMenuCorregirInventarioManualmente;
    private javax.swing.JMenuItem itemMenuEgreso;
    private javax.swing.JMenuItem itemMenuImportarLotes;
    private javax.swing.JMenuItem itemMenuTransferencia;
    private javax.swing.JMenu menuAdministracion;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuOperaciones;
    // End of variables declaration//GEN-END:variables

    private void agregarComponente(JComponent componente) {
        getContentPane().removeAll();
        add(componente, BorderLayout.CENTER);
        componente.setVisible(true);
        componente.updateUI();
        pack();
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("imagenes/ecommerce.png"));
        return retValue;
    }

}
