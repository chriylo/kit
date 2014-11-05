package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

 public class Params3 {
	 
	 public String sample; 
	
	 public int[] c;
	 public String b;
	 public String i;
	 public String t;
	 public String g;
	 public String o;
	 public String d;
	 
	 public String r1;
	 public String r2;
	 public String r;
	 public int k;
	 
	 
	 public String cn;
	 
	 
    public Params3(){
    	sample = "sample";
    }
    
   public void setCopyNumber(int[] c) {
	   this.c = c;
   }
   
   public void setSample(String sample) {
	   this.sample = sample;
   }
   
   public void setTrie(String t) {
	   this.t = t;
   }
   
   public void setGenes(String g) {
	   this.g = g;
   }
   
   public void setCopyNumbers(String cn) {
	   this.cn = cn;
   }
   
   public void setOutputDir(String o) {
	   this.o = o;
   }
   
   public void setReadsDir(String d) {
	   this.d = d;
   }
   
   public void setk(int k) {
	   this.k = k;
   }
   
   public void setBam(String b) {
	   this.b = b;
   }
   
   public void setIndex(String i) {
	   this.i = i;
   }
   
   public void setRead1(String f1) {
	   this.r1 = f1;
   }
   
   public void setRead2(String f2) {
	   this.r2 = f2;
   }
   
   public void setRead(String f) {
	   this.r = f;
   }
    
}
