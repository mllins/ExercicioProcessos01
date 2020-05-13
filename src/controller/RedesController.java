package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RedesController {

	private String[] saida;
	
	public RedesController() {
		this.saida=null;
	}
	
	public String[] getSaida() {
		return(this.saida);
	}
	
	public String getOS() {
		return(System.getProperty("os.name"));
	}
	
	public String getOSVersion() {
		return(System.getProperty("os.version"));
	}
	
	private String filtrarAdaptadores(String[] lista, String tipo) {
		// Prepara Array para quando detectar um tipo
		String[] partes = null;
		// Prepara saida com StringBuffer (mais rápido)
		StringBuffer ips=new StringBuffer();
		// Percorre a lista com saída de comando
		for(String linha : lista) {
			// Checa se é adaptador
			if(linha.indexOf("Adaptador")>=0) {
				// Adiciona o adaptador
				if(ips.length()>0) {
					ips.append(";");
				}
				ips.append(linha);
			} else if (linha.indexOf(tipo)>=0){ // Checa se é de um tipo
				// Adiciona o tipo ao adaptador
				partes = linha.split(": ");
				ips.append(" - "+tipo+": ");
				ips.append(partes[1]);
			}
		}
		return(ips.toString());
	}
	
	private String[] filtrarIPs(String[] lista, String tipo) {
		// Recebe todos os adaptadores obtidos de uma lista de dados
		String adaptadores = this.filtrarAdaptadores(lista,tipo);
		// Separa a lista de adaptadores em nova lista 
		String novalista[] = adaptadores.split(";");
		// Prepara saída como StringBuffer
		StringBuffer ips = new StringBuffer();
		// Percorre a lista atrás de um tipo
		for(String linha : novalista) {
			// Checa se o adaptador tem o tipo escolhido
			if(linha.indexOf(tipo)>=0) {
				// Adiciona o tipo
				if(ips.length()>0) {
					ips.append(";");
				}
				ips.append(linha);
			}
		}
		return(ips.toString().split(";"));
	}
	
	private String[] executaComando(String comando) {
		// Executa um comando de terminal (seja Windows ou Linux)
		String[] saida = null;
		try {
			// Executa o processo relacionado com o comando dado.
			Process p = Runtime.getRuntime().exec(comando);
			// Recebe uma sequência de bits enviadas pelo processo.
			InputStream is = p.getInputStream();
			// Faz a leitura dos bits recebidos
			InputStreamReader isr = new InputStreamReader(is);
			// Converte a leitura de Stream e armazena em um Buffer
			BufferedReader b = new BufferedReader(isr);
			// Retira uma String do Buffer
			String linha = b.readLine();
			StringBuffer sb = new StringBuffer();
			while(linha != null) {
				if(sb.length() >= 0) {
					sb.append("\n");  // Adiciona final de linha
				}
				sb.append(linha);     // Adiciona linha
				linha = b.readLine(); // Faz nova leitura
			}
			saida = sb.toString().split("\n"); // Gera saída em Array
		} catch (IOException e) {
			System.err.println(e);
		}
		return(saida);
	}
	
	public void exibeSaida() {
		if(this.saida != null) {
			for(String linha : this.saida) {
				System.out.println(linha);
			}			
		}
	}
	
	private String[] filtraTempos(String[] lista) {
		StringBuffer sb = new StringBuffer();
		String[] partes = null;
		for(String linha : lista) {
			if(linha.indexOf("tempo") >= 0) {
				partes = linha.split("tempo=");
				if(sb.length() > 0) {
					sb.append(";");
				}
				sb.append(partes[1]);
			}
		}
		partes = sb.toString().split(";");
		return(partes);
	}
	
	private double calculaMedia(String[] tempos) {
		int i = 0;
		String valorNumerico = "";
		double soma = 0;
		for(String tempo : tempos) {
			valorNumerico = tempo.replace("ms", "");
			soma += Double.parseDouble(valorNumerico);
			i += 1;
		}
		return(soma / i);
	}
	
	public void ip(String os) {
		String comando = "";
		if(os.indexOf("Windows") >= 0) {
			comando = "ipconfig";
		} else if(os.indexOf("Linux") >= 0) {
			comando = "ifconfig -a";
		}
		String[] saida = this.executaComando(comando);
		if(saida != null) {
			this.saida = this.filtrarIPs(saida,"IPv4");
			this.exibeSaida();
		}
	}
	
	public void ping(String os) {
		String local = "www.google.com.br";
		this.ping(os, local);
	}
	
	
	public void ping(String os, String local) {
		String comando = "";
		if(os.indexOf("Windows") >= 0) {
			comando = "ping -n 10 " + local;
		} else if(os.indexOf("Linux") >= 0) {
			comando = "ping -c 10 " + local;
		}
		String[] saida = this.executaComando(comando);
		if(saida != null) {
			this.saida = this.filtraTempos(saida);
			System.out.println(this.calculaMedia(this.saida) + "ms");
		}
	}
}
