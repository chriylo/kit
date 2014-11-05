package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

 public class Params {
	
	 public int test;
	 public String pathToTemplates;
	 public String pathToTemplateBarcodes;
	 public String pathToReads;
	 public String pathToTries;
	 public String pathToTrieFile;
	 public String pathToGeneIndicesFile;
	 public String outputFileName;
	 public String barcodeFileName;
	 public int k;
	 public String pathToReferenceDir;
	 public String pathToTemplateBarcodesDir;
	 public int c;
	 
    public Params(){
    	test = 0;
    	pathToReads = "";
    	pathToTries = "";
    	pathToTrieFile = "";
    	pathToGeneIndicesFile = "";
    	pathToTemplates = "";
    	outputFileName = "output.barcod";
    	barcodeFileName = "";
    	pathToTemplateBarcodes = "";
    	k = 50;    	
    	pathToReferenceDir = "";
    	pathToTemplateBarcodesDir = "";
    	c = 5;
    }
    
}
