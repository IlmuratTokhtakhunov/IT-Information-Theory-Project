import java.util.*;
import java.math.*;
import java.io.*;
/* Created by Tokhtakhunov Il'murat, student of 3 course in IT University(Almaty), specialty: CSSE
 * This is another variant of Information theory project, it uses Java language and Shannon-Fano algorithm for binary code creating*/
class nado{
	char c1;
	float prob;
	int coun;
	String code;
	public nado(char c1){
		this.c1 = c1;
		this.coun = 1;
		this.code = "";
	}
	public String toString() {
		return c1+" - "+prob+" : "+code;
	}
}
class syn{
	String code;
	int mplace;
	public syn(String c, int m) {
		code = c;
		mplace = m;
	}
}
public class Farhod {
	static ArrayList<nado> dict = new ArrayList<nado>();
	static ArrayList<syn> syndict = new ArrayList<syn>();
	//this is realization of shannon - fano algorithm, using recursion
	static void Shan(int b, int e) {
		if(e==b) {
			return;
		}
		float bsum=0;
		float nsum=100;
		int d=-1;
		for(int i=b;i<=e;i++) {
			bsum = bsum+dict.get(i).prob;
			float esum=0;
			for(int j=i+1;j<=e;j++) {
				esum = esum + dict.get(j).prob;
			}
			if(Math.abs(bsum-esum)<nsum) {
				d = i;
				nsum = Math.abs(bsum-esum);
			}
		}
		for(int i=b;i<=d;i++) {
			dict.get(i).code = dict.get(i).code+'1';
		}
		for(int i=d+1;i<=e;i++) {
			dict.get(i).code = dict.get(i).code+'0';
		}
		Shan(b, d);
		Shan(d+1, e);	
	}
	public static void main(String args[]) {
		
		// coping from text.txt to src string
		String src = "";
		try(FileInputStream fin=new FileInputStream("C:\\Users\\Ильмурат\\eclipse-workspace\\IlmuratHotBoy\\src\\Text.txt"))
        {
            int i=-1;
            while((i=fin.read())!=-1){
            	src = src + (char)i;
            }   
        }
        catch(IOException ex){
              
            System.out.println(ex.getMessage());
        } 
		src = src.toUpperCase();
		
		//There is filling dict ArrayList<nado> with characters of text and their probability
		for(int i=0;i<src.length();i++) {
			boolean zero = true;
			char d = src.charAt(i);
			for(int j=0;j<dict.size();j++) {
				if(dict.get(j).c1==d) {
					zero = false;
					dict.get(j).coun++;
				}
			}
			if(zero) {
				nado s = new nado(d);
				dict.add(s);
			}	
		}
		//There we output the created dictionary to console
		System.out.println("PART 1:");
		for(int i=0;i<dict.size();i++) {
			dict.get(i).prob = (float)dict.get(i).coun/src.length();
			System.out.println(dict.get(i));
		}
		
		//Part2
		//There Bubble sort according probability
		for(int i=0;i<dict.size();i++) {
			for(int j=1;j<dict.size();j++) {
				if(dict.get(j).prob>dict.get(j-1).prob) {
					nado temp = dict.get(j);
					dict.set(j, dict.get(j-1));
					dict.set(j-1, temp);
				}
			}
		}
		Shan(0, dict.size()-1);
		System.out.println("\n\nPART 2:\nDictionary:");
		for(int i=0;i<dict.size();i++) {
			System.out.println(dict.get(i));
		}
		//There we replace every character in text with their binary code according shannon - fano
		String src2 = "";
		for(int i=0;i<src.length();i++) {
			for(int j=0;j<dict.size();j++) {
				if(dict.get(j).c1==src.charAt(i))
					src2 = src2+dict.get(j).code;
			}
		}
		System.out.println("\nEncoded String(binary sequence):\n"+src2);
		
		
		//Part3
		String src3 = "";
		String buf="";
		for(int i=0;i<src2.length();i++) {
			buf = buf+src2.charAt(i);
			for(int j=0;j<dict.size();j++) {
				if(buf.compareTo(dict.get(j).code)==0) {
					src3 = src3+dict.get(j).c1;
					buf = "";
				}
			}
		}
		System.out.println("\n\nPART 3:\nOriginal text:\n"+src3);
		
		String src4 = "";
		String src2orig = src2;
		int temp = 4-src2.length()%4;
		if(src2.length()%4!=0) {
			for(int i=0;i<temp;i++) {
				src2 = src2+"0";
			}
		}
	
		for(int i=0;i<src2.length();i++) {
			src4 = src4+src2.charAt(i);
			if((i+1)%4==0) {
				src4 = src4 + (char)(((((int)src2.charAt(i-3))-48^((int)src2.charAt(i-2)-48)^((int)src2.charAt(i-1))-48)+48));
				src4 = src4 + (char)(((((int)src2.charAt(i-2))-48)^(((int)src2.charAt(i-1))-48)^(((int)src2.charAt(i))-48))+48);
				src4 = src4 + (char)(((((int)src2.charAt(i-3))-48)^(((int)src2.charAt(i-2))-48)^(((int)src2.charAt(i))-48))+48);
			}
		}
		System.out.println("\n\nPART 4:\nBinary sequence encoded with Hamming code(7, 4):");
		System.out.println(src4.substring(0,3000));
		System.out.println(src4.substring(3000));
		
		//PART 5 
		String src5 = src4;
		for(int i=0;i<src4.length();i+=7) {
			int z = i+(int)(Math.random()*7);
			if(src4.charAt(z)=='1')
				src5 = src5.substring(0, z)+"0"+src5.substring(z+1);
			else
				src5 = src5.substring(0, z)+"1"+src5.substring(z+1);
		}
		System.out.println("\n\nPART 5:\nBinary sequence encoded with Hamming code(7, 4) and with one mistake per block(7):");
		System.out.println(src5.substring(0,3000));
		System.out.println(src5.substring(3000));
		
		//PART 6
		String src6 = src5;
		//Filling ArrayList<syn> syndict with created syndrome with their code and mistake position
		syndict.add(new syn("001", 7));
		syndict.add(new syn("010", 6));
		syndict.add(new syn("011", 4));
		syndict.add(new syn("100", 5));
		syndict.add(new syn("101", 1));
		syndict.add(new syn("110", 3));
		syndict.add(new syn("111", 2));
		for(int i=0;i<src5.length();i+=7) {
			String synz = ""+(char)((((int)src5.charAt(i)-48)^((int)src5.charAt(i+1)-48)^((int)src5.charAt(i+2)-48)^((int)src5.charAt(i+4)-48))+48)+""+(char)((((int)src5.charAt(i+1)-48)^((int)src5.charAt(i+2)-48)^((int)src5.charAt(i+3)-48)^((int)src5.charAt(i+5)-48))+48)+""+(char)((((int)src5.charAt(i)-48)^((int)src5.charAt(i+1)-48)^((int)src5.charAt(i+3)-48)^((int)src5.charAt(i+6)-48))+48);
			//System.out.println("syn: "+synz);
			for(int j=0;j<syndict.size();j++) {
				if(synz.compareTo(syndict.get(j).code)==0) {
					int z = i+syndict.get(j).mplace-1;
					if(src5.charAt(z)=='1')
						src6 = src6.substring(0, z)+"0"+src6.substring(z+1);
					else
						src6 = src6.substring(0, z)+"1"+src6.substring(z+1);
				}
			}
			
		}
		String srcfin = "";
		for(int i=0;i<src6.length();i+=7) {
			srcfin = srcfin+src6.substring(i, i+4);
		}
		srcfin = srcfin.substring(0, srcfin.length()-temp);
		System.out.println("\n\nPART 6:\nEncoded string(binary sequence):\n"+srcfin);
		System.out.println("Compare function if 0 then strings from part 2 and part 6 are equal, otherwise are not equal: "+src2orig.compareTo(srcfin));
		
	}
}
