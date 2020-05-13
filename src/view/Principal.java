package view;

import javax.swing.JOptionPane;

import controller.RedesController;

public class Principal {

	public static void main(String[] args) {
		RedesController rc = new RedesController();
		int escolha = -1;
		String opcao = "";
		String menu = "Escolha:\n"
				+ "1 - Listar adaptadores de rede que possuem IPv4\n"
				+ "2 - Média de tempo de PING para www.google.com.br\n";
		while(escolha != 0) {
			opcao = JOptionPane.showInputDialog(null, menu);
			if(opcao.equals("1") || opcao.equals("2")) {
				escolha = Integer.parseInt(opcao);
			} else {
				escolha = 0;
			}
			switch(escolha) {
			case 1:
				rc.ip(rc.getOS()); break;
			case 2:
				rc.ping(rc.getOS()); break;
			}
	    }
	}
}
