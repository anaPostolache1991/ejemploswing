package ejemploswing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Contacts extends TreeMap<String, String> {
	static SortedMap<String,String>agenda=new TreeMap<>();
	public String exec(String cmd) {
		String result = null;
		
		Scanner s = new Scanner(cmd);
		int estado = 0;
		String token;
		String nombre = null;
		while (estado != 5) {
			switch (estado) {
			case 0:
				try {
					token = s.skip("buscar|\\p{L}+(\\s+\\p{L}+)*").match().group();
					if (token.equals("buscar"))
						estado = 2;
					else {
						nombre = token;
						estado = 1;
					}
				} catch (NoSuchElementException e) {
					result = "Se esperaba 'buscar' o 'fin' o un nombre";
					estado = 5;
				}
				break;
			case 1:
				try {
					s.skip("-");
					estado = 3;
				}catch (NoSuchElementException e) {
					result = "Se esperaba '-'";
					estado = 5;
				}
				break;
			case 2:
				try {
					s.skip(":");
					estado = 4;
				}catch (NoSuchElementException e) {
					result = "Se esperaba ':'";
					estado = 5;
				}
				break;
			case 3:
				try {
					token = s.skip("\\d{9}").match().group();
					agenda.put(nombre, token);
					estado = 5;
				}catch (NoSuchElementException e) {
					result = "Se esperaba un teléfono";
					estado = 5;
				}
				break;
			case 4:
				try {
					token = s.skip("\\p{L}+(\\s+\\p{L}+)*").match().group();
					String telefono = agenda.get(token);
					if (telefono != null)
						result = token + " -> " + telefono;
					else
						result = token + " no se encuentra en la agenda";
					estado = 5;
				} catch (NoSuchElementException e) {
					result = "Se esperaba un nombre";
					estado = 5;
				}
				break;
			}
		}
		
		return result;
	}
	
	public void load(String ruta) {
		File f=new File(ruta);
		try {
			FileReader fr=new FileReader(f);
			BufferedReader br=new BufferedReader(fr);
			String linea;
			while ((linea=br.readLine())!=null) {
				String [] datos=linea.split("-");
				agenda.put(datos[0], datos[1]);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error al leer el fichero");
		} catch (IOException e) {
			System.out.println("No existe el fichero, inserte datos en la agenda y guárdelos");
		} 
		
	}
	

	public void save(String ruta) {
		FileWriter fw;
		try {
			fw = new FileWriter(ruta);
			BufferedWriter bw= new BufferedWriter(fw);
			PrintWriter pw=new PrintWriter(bw);
			
			Set<String> conjuntoClaves=agenda.keySet();
			Iterator<String> it=conjuntoClaves.iterator();
			
			while (it.hasNext()) {
				String clave=(String) it.next();
				String tf= agenda.get(clave);
				pw.println(clave+"-"+tf);
			}
			pw.close();
		} catch (IOException e) {
			System.out.println("Error al buscar el fichero para escribir");
		}
		
	}

	}
	

