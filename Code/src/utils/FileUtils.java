package utils;

import java.util.Arrays;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils
{
   private static boolean write = false;
   private static boolean read = false;
   private static Formatter saida = null;
   private static Scanner entrada = null;
   
   public static File getCanonicalPath(String nomeArq) throws Exception{
         return (new File(nomeArq));
   }
   
	public static void showFile(File words) {
		try (Stream<String> stream = Files.lines(Paths.get(words.getPath()))) {
	        stream.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File openWrite(String nomeArq) {
      close();
      	File file = null;
		try{
			 file = getCanonicalPath(nomeArq);
			 nomeArq = file.getCanonicalPath().replace('\\','/');;
		   saida = new Formatter(nomeArq);
         write = true;
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return file;
   }

	public static void openRead(String nomeArq) {
      close();

		try{
         //nomeArq = getCanonicalPath(nomeArq);
         entrada = new Scanner(new File(nomeArq));
         read = true;
		}  catch (Exception e) {}
   }

	public static void close() {
      if(write == true){
    	  if(saida!=null)
    		  saida.close();
         write = false;
      }
      if(read == true){
         entrada.close();
         read = false;
      }
	}

   public static void print(int x){
      if(write == true){
		   saida.format( "%d", x);
      }
   }

   public static void print(double x){
      if(write == true){
	   	saida.format( "%f", x);
      }
   }

   public static void print(String x){
      if(write == true){
   		saida.format( "%s", x);
      }
   }

   public static void print(boolean x){
      if(write == true){
		   saida.format( "%s", ((x) ? "true" : "false"));
      }
   }

   public static void print(char x){
      if(write == true){
	   	saida.format( "%c", x);
      }
   }

   public static void print(String[] x) {
	      if(write == true){
			   saida.format( "%s", Arrays.toString(x));
	      }
}
   
   public static void println(int x){
      if(write == true){
   		saida.format( "%d\n", x);
      }
   }

   public static void println(double x){
      if(write == true){
		   saida.format( "%f\n", x);
      }
   }

   public static void println(String x){
      if(write == true){
	   	saida.format( "%s\n", x);
      }
   }

   public static void println(boolean x){
      if(write == true){
   		saida.format( "%s\n", ((x) ? "true" : "false"));
      }
   }

   public static void println(char x){
      if(write == true){
		   saida.format( "%c\n", x);
      }
   }
   
   public static void println(String[] x) {
	      if(write == true && saida != null){
	    	  if(x != null){
	    		  String output = Arrays.toString(x);
	    		  if(output !=null){
	    			  try{
		    			  saida.format( "%s\n", output);
	    			  }catch(FormatterClosedException e){
	    				  System.out.println("IO Error: "+ e.getLocalizedMessage());
	    			  }catch (Exception e) {
	    				  System.out.println("Unknown IO Error: "+ e.getLocalizedMessage());
					}
	    		}
	    	  }else
	    		 System.out.println("Array nulo, just keep going!");
	      }
   }

   public static int readInt(){
      int resp = -1;
		try{
         resp = entrada.nextInt();
		}  catch (Exception e) {}
      return resp;
   }

   public static char readChar(){
      char resp = ' ';
		try{
         resp = entrada.next().charAt(0);
		}  catch (Exception e) {}
      return resp;
   }

   public static double readDouble(){
      double resp = -1;
		try{
         resp = entrada.nextDouble();
		}  catch (Exception e) {}
      return resp;
   }

   public static String readString(){
      String resp = "";
		try{
         resp = entrada.next();
		}  catch (Exception e) {}
      return resp;
	}

   public static boolean readBoolean(){
      boolean resp = false;
		try{
         resp = (entrada.next().equals("true")) ? true : false;
		}  catch (Exception e) {}
      return resp;
	}

   public static String readLine(){
      String resp = "";
		try{
         resp = entrada.nextLine();
		}  catch (Exception e) {}
      return resp;
	}

   public static String readAll(){
      String resp = "";
      while (hasNext() == true) {
         resp += (entrada.nextLine() + '\n');
      }
      return resp.substring(0, resp.length()-1);
   }

   public static boolean hasNext(){
      return entrada.hasNext();
   }
}