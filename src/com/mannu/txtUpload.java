package com.mannu;

import java.awt.TextField;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DBConnn.DbConn;


public class txtUpload extends Thread{
	Connection con;
    PreparedStatement ps;
    PreparedStatement ps1;
    ResultSet rs;
    String update;
    
	public void run(){
		try{
		    
	        con=DbConn.conn();
	        JFileChooser chooser=new JFileChooser();
	        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        chooser.showOpenDialog(null);
	        File txtFolder=chooser.getSelectedFile();
	        File[] txtFiles=txtFolder.listFiles();
	        for(File txtFile:txtFiles)
	        {
	                
	        if(txtFile.getName().contains(".txt"))
	        {
	        FileInputStream input = new FileInputStream(txtFile.getAbsolutePath());
	        BufferedReader reader=new BufferedReader(new InputStreamReader(input));
	        
	        String line=reader.readLine();
	        while(line!=null)
	        {
	        
	        	String data[];
	        	int k=0;
	            data=line.split("");
	            for(int i=0;i<data.length;i++){
	                if(data[i].equals("^")){
	                    k++;
	                }

	            }
	            System.out.println("Count: "+k);

	            String[] args=line.split("\\^");
	         // System.out.println("args:"+args.length);
	            if(args.length==8)
	            {
	            	Date ud=new SimpleDateFormat("ddMMyyyy").parse(args[4]);
	            	SimpleDateFormat dff1=new SimpleDateFormat("yyyy-MM-dd");
	            	
	            	update=dff1.format(ud);
	            	
	            }else if(args.length!=8){
	            	System.out.println("Arg Lenth: "+args.length);
	            	
	            	if (k>80) {
	            		System.out.println(args[0]+","+args[67]+","+txtFile.getName());
		            	PreparedStatement ps10=con.prepareStatement("select * from textfiles where ackno='"+args[67]+"' and filename='"+txtFile.getName()+"' and sno='"+args[0]+"';;");
		            	ResultSet rs10=ps10.executeQuery();
		            	if (rs10.next()) {
							System.out.println("Data Already Insert");
						} else {
							
			                ps=con.prepareStatement("insert into textfiles values('"+args[0]+"','"+args[67]+"','"+txtFile.getName()+"','"+update+"')");
			                ps.execute();   
						}
					} else {
						
						System.out.println(args[0]+","+args[56]+","+txtFile.getName());
		            	PreparedStatement ps10=con.prepareStatement("select * from textfiles where ackno='"+args[56]+"' and filename='"+txtFile.getName()+"' and sno='"+args[0]+"';");
		            	ResultSet rs10=ps10.executeQuery();
		            	if (rs10.next()) {
							System.out.println("Data Already Insert");
						} else {
			                ps=con.prepareStatement("insert into textfiles values('"+args[0]+"','"+args[56]+"','"+txtFile.getName()+"','"+update+"')");
			                ps.execute();   
						}
					}
	            }
	            line=reader.readLine();      
	        }
	        input.close();
	        reader.close();
	        File nefil=new File(txtFolder+"\\importdone\\");
	        System.out.println("ffffff: "+nefil);
	        if (!nefil.exists()) {
				nefil.mkdir();
			}
	        txtFile.renameTo(new File(nefil+"//"+txtFile.getName()));
	        
	        System.out.println("File Data: "+txtFile.getAbsolutePath());
	        
	        } else {
	        	System.out.println("Other Format File");
	        } 
	        }
	        JOptionPane.showMessageDialog(null, "Completed", "PanOutfileUpload", JOptionPane.INFORMATION_MESSAGE);
	        con.close();
	    }catch(Exception e)
	    {
	    
	        JOptionPane.showMessageDialog(null, "Error:"+e, "PanOutfileUpload", JOptionPane.ERROR_MESSAGE);
	        
	    }
	}
}
