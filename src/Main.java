import java.util.LinkedList;
import java.util.Scanner;
import java.text.DecimalFormat;

public class Main {
	public static class Cisterna {
		public double alturaBase;
		public double alturaTopo;
		public double largura;
		public double profundidade;
		
		public Cisterna(double alturaBase, double alturaTopo, double largura, double profundidade) {
			this.alturaBase = alturaBase;
			this.alturaTopo = alturaTopo;
			this.largura = largura;
			this.profundidade = profundidade;
		}
	}
	
	public static String arredondar(double numero) {
		DecimalFormat df = new DecimalFormat("#.##");
		String resp = String.valueOf(Double.parseDouble(df.format(numero).replace(",", ".")));
		if(resp.indexOf(".")+2 >= resp.length()) {
			resp += "0";
		}
		return resp;
	}
	public static void inicializarPatamares(double[] patamares) {
		for(int i=0; i<patamares.length; i++) {
			patamares[i] = -1;
		}
	}
	public static void ordenarVetor(double[] patamares) {
		double aux;
		for(int i=0; i<patamares.length; i++) {
			for(int j=i; j<patamares.length; j++) {
				if(patamares[j] < patamares[i]) {
					aux = patamares[i];
					patamares[i] = patamares[j];
					patamares[j] = aux;
				}
			}
		}
	}
	public static void tirarRepetidos(double[] vet, double[] semRep) {
		int posSemRep = 0;
		for(int i=0; i<vet.length-1; i++) {
			if(vet[i] != vet[i+1]) {
				semRep[posSemRep] = vet[i];
				posSemRep++;
				semRep[posSemRep] = vet[i+1];
			}
		}
		if(posSemRep==0) { //Todos sao repetidos
			semRep[0] = vet[0];
		}
	}
	public static void main(String[] args) {
		int nroCisternas;
		double volumeInjetado;
		LinkedList<Cisterna> cisternas;
		
		Scanner sc = new Scanner(System.in);
		int casos = Integer.parseInt(sc.next());
			
		double alturaBase;
		double alturaTopo;
		double largura;
		double profundidade;
		
		double alturaBaseMaisBaixa;
		double alturaTopoMaisAlto;
		double areaAcumuladaAtual;
		double volumeAcumuladoAtual;
		
		int indexPatamarAtual;
		double[] patamares;
		double[] patamaresSemRep;
		
		double alturaAcumulada;
		
		for(int i=0; i<casos; i++) {
			nroCisternas = Integer.parseInt(sc.next());
			cisternas = new LinkedList<Cisterna>();
			
			indexPatamarAtual = 0;
			patamares = new double[nroCisternas*2];
			patamaresSemRep = new double[nroCisternas*2];
			
			alturaBaseMaisBaixa = Double.MAX_VALUE;
			alturaTopoMaisAlto = 0;
			
			for(int j=0; j<nroCisternas; j++) {
				alturaBase = Double.parseDouble(sc.next());
				alturaTopo = alturaBase + Double.parseDouble(sc.next());
				largura = Double.parseDouble(sc.next());
				profundidade = Double.parseDouble(sc.next());
				
				Cisterna nova = new Cisterna(alturaBase, alturaTopo, largura, profundidade);
				cisternas.add(nova);
			
				patamares[indexPatamarAtual] = alturaBase;
				indexPatamarAtual++;
				patamares[indexPatamarAtual] = alturaTopo;
				indexPatamarAtual++;
			
				if(alturaBase < alturaBaseMaisBaixa) alturaBaseMaisBaixa = alturaBase;
				if(alturaTopo > alturaTopoMaisAlto) alturaTopoMaisAlto = alturaTopo;
			}
			volumeInjetado = Double.parseDouble(sc.next());
			
			
			if(volumeInjetado <= 0) {
				System.out.println("0.00");
			}
			else if(nroCisternas <= 0) {
				System.out.println("OVERFLOW");
			}
			else {
				ordenarVetor(patamares);
				inicializarPatamares(patamaresSemRep);
				tirarRepetidos(patamares, patamaresSemRep);
			
				alturaAcumulada = alturaBaseMaisBaixa;
				
				if(patamaresSemRep[0] == alturaTopoMaisAlto) { //Unico patamar
					System.out.println("OVERFLOW");
					if(i != casos-1) System.out.println();
					continue;
				}
				
				for(int j=0; j<patamaresSemRep.length-1; j++) {
					areaAcumuladaAtual = 0;
					for(Cisterna cisAtual : cisternas) {
						if(patamaresSemRep[j] >= cisAtual.alturaBase && patamaresSemRep[j+1] <= cisAtual.alturaTopo) {
							areaAcumuladaAtual += (cisAtual.largura * cisAtual.profundidade);
						}
					}
					volumeAcumuladoAtual = areaAcumuladaAtual * (patamaresSemRep[j+1] - patamaresSemRep[j]);
					
					if(patamaresSemRep[j+1] == alturaTopoMaisAlto) { //Ultimo patamar
						if(volumeAcumuladoAtual < volumeInjetado) System.out.println("OVERFLOW");
						else {
							alturaAcumulada += volumeInjetado/areaAcumuladaAtual;
							System.out.println(arredondar(alturaAcumulada));
						}
						break;
					}
					else {
						if(volumeAcumuladoAtual < volumeInjetado) {
							volumeInjetado = volumeInjetado - volumeAcumuladoAtual;
							alturaAcumulada += patamaresSemRep[j+1] - patamaresSemRep[j];
						}
						else {
							alturaAcumulada += volumeInjetado/areaAcumuladaAtual;
							System.out.println(arredondar(alturaAcumulada));
							break;
						}
					}
				}
			}
			if(i != casos-1) System.out.println();
		}
		sc.close();
	}
}
