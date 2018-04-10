package com.mannu;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jdesktop.swingx.JXDatePicker;



import DBConnn.DbConn;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class MainPage extends JFrame{
	private JTable view;
	JXDatePicker fdate,tdate;
	DefaultTableModel model;
	Connection con=DbConn.conn();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainPage mp=new MainPage();
				mp.setVisible(true);
			}
		});
	}
	
	public MainPage() {
		setSize(588,383);
		setTitle("HTML Upload");
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "File Upload", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(59, 11, 451, 50);
		panel.setLayout(null);
		getContentPane().add(panel);
		
		JButton btnTextUpload = new JButton("Text Upload");
		btnTextUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtUpload hu=new txtUpload();
				hu.start();
			}
		});
		btnTextUpload.setBounds(45, 16, 126, 23);
		panel.add(btnTextUpload);
		
		JButton btnHtmlUpload = new JButton("HTML Upload");
		btnHtmlUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HTMLupload hup=new HTMLupload();
				hup.start();
			}
		});
		btnHtmlUpload.setBounds(274, 16, 126, 23);
		panel.add(btnHtmlUpload);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Report", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 64, 562, 279);
		panel_1.setLayout(null);
		getContentPane().add(panel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 46, 542, 192);
		panel_1.add(scrollPane);
		
		view = new JTable(){
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
		           int rendererWidth = component.getPreferredSize().width;
		           TableColumn tableColumn = getColumnModel().getColumn(column);
		           tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		view.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			Object column []={"Slno" , "Line_No" , "Lot_No" , "File_Name" , "Field_Value" , "Responce_Code" , "Respdesc" , "Upload_Date" , "Ack_No"};
			model=new DefaultTableModel();
			model.setColumnIdentifiers(column);
			view.setModel(model);
			
		scrollPane.setViewportView(view);
		
		JLabel lblFromDate = new JLabel("From Date :");
		lblFromDate.setBounds(41, 20, 69, 14);
		panel_1.add(lblFromDate);
		
		fdate = new JXDatePicker();
		fdate.setBounds(108, 16, 120, 22);
		DateFormat d1=new SimpleDateFormat("yyyy-MM-dd");
		fdate.setFormats(d1);
		fdate.setDate(new java.util.Date());
		
		panel_1.add(fdate);
		
		tdate = new JXDatePicker();
		tdate.setBounds(290, 16, 120, 22);
		DateFormat d2=new SimpleDateFormat("yyyy-MM-dd");
		tdate.setFormats(d2);
		tdate.setDate(new java.util.Date());
		panel_1.add(tdate);
		
		JLabel lblToDate = new JLabel("To Date :");
		lblToDate.setBounds(238, 20, 69, 14);
		panel_1.add(lblToDate);
		
		JButton btnShow = new JButton("Show");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date fdat=fdate.getDate();
				java.util.Date tdat=tdate.getDate();
				String ffdat=df.format(fdat);
				String ttdat=df.format(tdat);
				System.out.println("From Date: "+ffdat+"    To Date: "+ttdat);
				
				try {
					//PreparedStatement ps10=con.prepareStatement("SELECT [lineno],fieldname,fieldvalue,responcecode,respdesc,upldate,newack,SUBSTRING(txtname,0,7) AS lotno FROM panfilevalidation WHERE upldate BETWEEN Convert(varchar(30),?,102) AND Convert(varchar(30),?,102) AND respdesc NOT LIKE '%Record inserted succesfully%' AND respdesc NOT LIKE 'Acknowledgement number already exists in table' AND respdesc NOT LIKE 'Record inserted into database successfully and is kept on hold at NSDL.' AND respdesc NOT LIKE 'WARNING: Applicant Last name contains Private or Limited when status of applicant is not Company.'");
					PreparedStatement ps10=con.prepareStatement("SELECT DISTINCT [lineno],fieldname,fieldvalue,responcecode,respdesc,upldate,newack,SUBSTRING(txtname,0,7) AS lotno FROM panfilevalidation WHERE upldate BETWEEN '"+ffdat+"' AND '"+ttdat+"' AND newack!=' ' AND respdesc NOT LIKE '%Record inserted succesfully%' AND respdesc NOT LIKE 'Acknowledgement number already exists in table' AND respdesc NOT LIKE 'Record inserted into database successfully and is kept on hold at NSDL.' AND respdesc NOT LIKE 'WARNING: Applicant Last name contains Private or Limited when status of applicant is not Company.';");
					
//					ps10.setString(1, ffdat);
//				      ps10.setString(2, ttdat);
				      System.out.println("SELECT DISTINCT [lineno],fieldname,fieldvalue,responcecode,respdesc,upldate,newack,SUBSTRING(txtname,0,7) AS lotno FROM panfilevalidation WHERE upldate BETWEEN '"+ffdat+"' AND '"+ttdat+"' AND newack!=' ' AND respdesc NOT LIKE '%Record inserted succesfully%' AND respdesc NOT LIKE 'Acknowledgement number already exists in table' AND respdesc NOT LIKE 'Record inserted into database successfully and is kept on hold at NSDL.' AND respdesc NOT LIKE 'WARNING: Applicant Last name contains Private or Limited when status of applicant is not Company.';");
					ResultSet rs10=ps10.executeQuery();
					int a=0;
					Object[] row=new Object[10];
					while (rs10.next()) {
						a=++a;
						row[0]=a;
						row[1]=rs10.getInt(1);
						row[2]=rs10.getInt(8);
						row[3]=rs10.getString(2);
						row[4]=rs10.getString(3);
						row[5]=rs10.getString(4);
						row[6]=rs10.getString(5);
						row[7]=rs10.getString(6);
						row[8]="'"+rs10.getString(7);
						model.addRow(row);
						
					}
				} catch (Exception e2) {
					System.out.println("Error: "+e2);
				}
			}
		});
		btnShow.setBounds(420, 15, 89, 23);
		panel_1.add(btnShow);
		
		JButton btnExport = new JButton("Export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser=new JFileChooser();
				int retrive=chooser.showSaveDialog(btnExport);
				String path2=chooser.getSelectedFile().getPath();
				String path=path2+".xls";
				if (retrive==JFileChooser.APPROVE_OPTION) {
				
				}
				try {
					ExcelExporter exporter=new ExcelExporter(view,path);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
			}
		});
		btnExport.setBounds(164, 245, 89, 23);
		panel_1.add(btnExport);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setBounds(310, 245, 89, 23);
		panel_1.add(btnExit);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
}
