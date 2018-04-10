package com.mannu;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import DBConnn.DbConn;




public class HTMLupload extends Thread{
	Connection con;
    PreparedStatement ps;
    PreparedStatement ps1;
    ResultSet rs;
	
	public void run(){
		try{
		    
	        Connection con=DbConn.conn();
	        JFileChooser chooser=new JFileChooser();
	        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        if (chooser.showOpenDialog(chooser)==JFileChooser.APPROVE_OPTION) {
	        	File htmlFolder=chooser.getSelectedFile();
		        File[] htmlFiles=htmlFolder.listFiles();
		        
		        String uploaddate="",filename="",txtname="";
		        String newack=""; String nwupdate=""; String htmldate="";
		        for(int p = 0; p < htmlFiles.length; p++)
		        {
		        	if (htmlFiles[p].getName().endsWith(".html")) {
		         System.out.println("Mannu");   
		        File input =new File(htmlFiles[p].getPath());
		        System.out.println(input);
		        Document doc = Jsoup.parse(input, "UTF-8");
		       
		        Elements trs=doc.select("TR");
		       
		        Elements centers=doc.select("CENTER");
		      
		        uploaddate=centers.get(1).text();
		        
		        String[] cou=centers.get(4).toString().split("No of Records:");
		        cou=cou[1].split(">");
		       String[] val=cou[1].toString().split("<");
		        
		        System.out.println("DD "+val[0]);
		      
		        String ddq=uploaddate.replaceAll("PAN File Validation Utility Version 2.11 Date : ", "");
		        DateFormat dff=new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT+05:30' yyyy");
		        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		        
		        htmldate=df.format(dff.parse(ddq));
		        
		        System.out.println("Data Record: "+htmldate);
		        
		        filename=centers.get(2).text().toString().replace("Input File Name: ", "");
		        txtname=filename.split("\\_")[1];
		        
		        System.out.println("Table Rows:"+trs.size());
		        for(int i=1;i<trs.size();i++)
		        {
		        newack="";
		        Elements tds=trs.get(i).select("TD");
		        PreparedStatement pps=con.prepareStatement("select uploddate from textfiles where sno='"+tds.get(0).text()+"' and filename='"+txtname+"'");
		        ResultSet rss=pps.executeQuery();
		        if (rss.next()) {
					nwupdate=rss.getString(1);
				}
		        pps.close();
		        rss.close();
		        
		        if(!tds.get(1).text().equalsIgnoreCase("Acknowledgemnt No."))
		        {
		        
		        ps1=con.prepareStatement("select ackno,uploddate from textfiles where sno='"+tds.get(0).text()+"' and filename='"+txtname+"'");
		        rs=ps1.executeQuery();
		        if(rs.next())
		        {
		            newack=rs.getString(1);
		            
		        } else {
		        	newack=rs.getString(1);
		        }
		        
		        ps1.close();
		        rs.close();
		        }
		        System.out.println("File Name: "+input.getName());
		        
		        if (tds.get(4).text().equals("Record inserted succesfully")) {
					
		        	PreparedStatement ppsd=con.prepareStatement("delete from panfilevalidation where SUBSTRING(fieldvalue,10,15)='060219701367103' or fieldvalue='060219701367103' and respdesc!='Record inserted succesfully'");
		        	
				} else {

				}
		        
		        
		        if(tds.get(2).text().length()>300)
		        {
		        	if (tds.get(4).text().equals("File header details already present in table - duplicate entry of file")) {
		        		 ps=con.prepareStatement("insert into panfilevalidation values('"+tds.get(0).text()+"','"+tds.get(1).text()+"','"+tds.get(1).text()+"','"+tds.get(3).text()+"','"+tds.get(4).text().replace("'", "")+"','"+filename+"','"+uploaddate+"','"+txtname+"','"+htmldate+"','0','"+htmldate+"','"+val[0]+"')");
		        		 ps.execute();
		        		 ps.close();
		        		 System.out.println("New Error");
					} else {
						 ps=con.prepareStatement("insert into panfilevalidation values('"+tds.get(0).text()+"','"+tds.get(1).text()+"','"+tds.get(1).text()+"','"+tds.get(3).text()+"','"+tds.get(4).text().replace("'", "")+"','"+filename+"','"+uploaddate+"','"+txtname+"','"+nwupdate+"','"+newack+"','"+htmldate+"','"+val[0]+"')");
					        System.out.println("Mannu 1st Query insert ");
					        ps.execute();
					        ps.close();
					}
		        } else{
		        	if (tds.get(4).text().equals("File header details already present in table - duplicate entry of file")) {
						ps=con.prepareStatement("insert into panfilevalidation values('"+tds.get(0).text()+"','"+tds.get(1).text()+"','"+tds.get(2).text().replace("'", "")+"','"+tds.get(3).text()+"','"+tds.get(4).text().replace("'", "")+"','"+filename+"','"+uploaddate+"','"+txtname+"','"+htmldate+"','0','"+htmldate+"','"+val[0]+"')");
				        System.out.println("New Error"); 
				        ps.execute();
				        ps.close();
		        		
					} else {
						ps=con.prepareStatement("insert into panfilevalidation values('"+tds.get(0).text()+"','"+tds.get(1).text()+"','"+tds.get(2).text().replace("'", "")+"','"+tds.get(3).text()+"','"+tds.get(4).text().replace("'", "")+"','"+filename+"','"+uploaddate+"','"+txtname+"','"+nwupdate+"','"+newack+"','"+htmldate+"','"+val[0]+"')");
				        System.out.println("Mannu 2nd Query insert "); 
				        ps.execute();
				        ps.close();
				        
					}
		        }
		        
		        }
		        
		        
		        File uploaddone=new File(htmlFolder+"//UploadDone");
		        if (!uploaddone.exists()) {
		        	uploaddone.mkdir();
				}
		        input.renameTo(new File(uploaddone+"//"+input.getName()));
		        	 }else {
				        	System.out.println("Other Format File");
				        }
		        
		        }
		        JOptionPane.showMessageDialog(null, "Completed", "PanOutfileUpload", JOptionPane.INFORMATION_MESSAGE);
		        con.close();
		        
		       
				
			} else {
				System.out.println("Path Not Selected");
			}
	        
	        
	    }catch(Exception e)
	    {
	    	
	    //System.out.println("Faild");
	    System.out.println("Error: "+e);
	        JOptionPane.showMessageDialog(null, "Error:"+e, "PanOutfileUpload", JOptionPane.ERROR_MESSAGE);
	        
	    }
        
	}
}
