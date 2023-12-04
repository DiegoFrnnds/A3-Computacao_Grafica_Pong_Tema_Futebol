import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Color;
import java.awt.Font;

import textura.Textura;

public class InicioCena implements GLEventListener {

	//Tamanho da Tela e configurações do GL
	// Preenchimento
	public int mode = GL2.GL_FILL;
	private float xMin, xMax, yMin, yMax, zMin, zMax;
	public GLU glu;

	// Largura e altura da tela
	private int screenWidth;
	private int screenHeight;
	public int menu = 1;


	//ATENÇÃO, IMPORTANTE !!!!!!!
	//CONFIGURAR O PATH (CAMINHO/DIRETÓRIO/PASTA) DAS TEXTURAS CORRETAMENTE, NO EXEMPLO ABAIXO
	// FOI UTILIZADO O PATH ABSOLUTO DO ARQUIVO, COM A IDE (INTELLIJ) BASTA CLICAR COM O BOTÃO DIREITO NO ARQUIVO
	// CLICAR EM "Copy path/refence" E CLICAR EM "Copy Absolute path" e MUDAR A FINAL STRING CORRETAMENTE
	//Controle e configuração de Texturas
	private Textura textura;
	private final String texturaPlanoDeFundo = "src/imagens/grama.png";
	private final String texturaMenu = "src/imagens/menu.png";

	//Variaveis para Texto e Título
	private TextRenderer textRendererTitulo;
	private TextRenderer textRendererTexto;

	// Construtor da Cena de Início
	public InicioCena(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// obtem o contexto Opengl
		GL2 gl = drawable.getGL().getGL2();
		// define a cor da janela (R, G, G, alpha)
		gl.glClearColor(1, 1, 1, 1);
		// limpa a janela com a cor especificada
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glLoadIdentity(); // lê a matriz identidade

		// Configura o plano de fundo
		textura.gerarTextura(gl, texturaPlanoDeFundo, 0);
		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(-1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(-1.0f, -1.0f);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

		// Desenha o Menu
		textura.gerarTextura(gl, texturaMenu, 0);
		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(-0.7f, 0.7f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(0.7f, 0.7f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(0.7f, -0.7f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(-0.7f, -0.7f);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

		desenhaTitulo(gl, 0, 680, Color.WHITE, "Pong Game - tema esportivo");

		switch (menu) {
			case 1:
				//Menu Principal
				desenhaTexto(gl, "[1] Jogar\n[2] Regras\n[3] Comandos\n[4] Autores");
				break;
			case 2:
				//Regras
				final String regras = "Regras do Jogo:\n" +
						"Uma bola de futebol está se movimentando num campo!\n" +
						"-Você começa com 5 vidas e 0 pontos, a cada vida perdida a bola\n" +
						"irá voltar para sua posição original no centro da tela.\n" +
						"-Seu Objetivo é controlar um jogador (bastão) e impedir que bola \"caia\"\n" +
						"Cada vez que o jogador rebater a bola, ele irá ganhar pontos.\n" +
						"-Ao atingir 200 pontos, uma espécie de Trave/Gol irá surgir na tela e a\n" +
						"bola irá gradualmente ficar cada vez mais rápida.\n" +
						"-O jogo termina quando todas as vidas acabarem\n" +
						"OU\n" +
						"quando o jogador decidir encerrar o jogo" +
						".\n" +
						"[5]Voltar\n";

				desenhaTexto(gl, regras);
				break;
			case 3:
				//Comandos
				final String comandos = "Comandos:\n" +
						"ESC --  Deixa o jogo em FullScreen ou Modo Janela\n" +
						"Seta para Direita  (->) --  Move o jogador para Direita\n" +
						"Seta para Esquerda (<-) --  Move o jogador para Esquerda\n" +
						"Tecla \"P\" --  Pausa o Jogo\n" +
						"Tecla \"S\" --  Finaliza o Jogo\n" +
						"[5]Voltar";

				desenhaTexto(gl, comandos);
				break;
			case 4:
				//Autores do Jogo
				final String autores = "Jogo feito por:\n" +
						"Daniel Ikeda Kuniyoshi, RA: 125111347030\n" +
						"Diego F Martinez, RA: 12522193520\n" +
						"Felipe da Silva Bagnato, 125111372069\n" +
						"Rafael H G Soares, RA: 125111374176\n" +
						"[5]Voltar";

				desenhaTexto(gl, autores);
				break;
		}

		gl.glFlush();
	}

	public void desenhaTitulo(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		// Obtém a largura do texto
		int textWidth = (int) textRendererTitulo.getBounds(frase).getWidth();

		// Calcula a posição x para centralizar o texto
		xPosicao = (screenWidth - textWidth) / 2;

		// Inicia o desenho do texto
		textRendererTitulo.beginRendering(screenWidth, screenHeight);
		textRendererTitulo.setColor(cor);
		textRendererTitulo.draw(frase, xPosicao, yPosicao);
		textRendererTitulo.endRendering();

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, mode);
	}

	public void desenhaTexto(GL2 gl, String texto) {
		// Configurações do texto
		textRendererTexto.setColor(Color.BLACK);
		textRendererTexto.beginRendering(screenWidth, screenHeight);

		// Quebra o texto em linhas usando "\n"
		String[] linhas = texto.split("\n");

		// Calcula a altura total do texto
		float alturaTotal = 0;
		for (String linha : linhas) {
			alturaTotal += textRendererTexto.getBounds(linha).getHeight();
		}

		// Inicia o desenho do texto
		float y = screenHeight / 2 + alturaTotal / 2; // Posiciona no meio verticalmente
		for (String linha : linhas) {
			textRendererTexto.draw(linha, (int) ((screenWidth - textRendererTexto.getBounds(linha).getWidth()) / 2.0), (int) y);
			y -= textRendererTexto.getBounds(linha).getHeight(); // Move para a próxima linha
		}

		// Finaliza o desenho do texto
		textRendererTexto.endRendering();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		glu = new GLU();
		xMin = yMin = zMin = -1;
		xMax = yMax = zMax = 1;

		textRendererTitulo = new TextRenderer(new Font("Arial", Font.BOLD, 30));
		textRendererTexto = new TextRenderer(new Font("Arial", Font.ITALIC, 25));

		textura = new Textura(1);
		textura.setFiltro(GL2.GL_LINEAR);
		textura.setModo(GL2.GL_DECAL);
		textura.setWrap(GL2.GL_REPEAT);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// obtem o contexto grafico Opengl
		GL2 gl = drawable.getGL().getGL2();

		// evita a divisão por zero
		if (height == 0)
			height = 1;
		// calcula a proporção da janela (aspect ratio) da nova janela
		float aspect = (float) width / height;

		// seta o viewport para abranger a janela inteira
		gl.glViewport(0, 0, width, height);

		// ativa a matriz de projeção
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity(); // lê a matriz identidade

		// ativa a matriz de modelagem
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); // lê a matriz identidade
		System.out.println("Reshape: " + width + ", " + height);
	}

}
