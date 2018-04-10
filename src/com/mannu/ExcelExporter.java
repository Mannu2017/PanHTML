package com.mannu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import DBConnn.DbConn;

public class ExcelExporter extends Thread{
	JTable tab;
	String pa;
	Connection conn=new DbConn().conn();

	public ExcelExporter(JTable table, String path)throws IOException{
		TableModel model = table.getModel();
        FileWriter out = new FileWriter(path);
        String groupExport = "";
        for (int i = 0; i < (model.getColumnCount()); i++) {//* disable export from TableHeaders
            groupExport = String.valueOf(model.getColumnName(i));
            out.write(String.valueOf(groupExport) + "\t");
        }
        out.write("\n");
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < (model.getColumnCount()); j++) {
                if (model.getValueAt(i, j) == null) {
                    out.write("null" + "\t");
                } else {
                    groupExport = String.valueOf(model.getValueAt(i, j).toString());
                    out.write(String.valueOf(groupExport).toString() + "\t");
                }
            }
            out.write("\n");
        }
        out.close();
        JOptionPane.showMessageDialog(null, "Export Completed");
	}
}
