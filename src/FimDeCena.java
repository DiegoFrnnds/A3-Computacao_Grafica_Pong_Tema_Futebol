import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import textura.Textura;

import java.awt.*;

public class FimDeCena implements GLEventListener {

	// Preenchimento
	public int mode = GL2.GL_FILL;
	private float xMin, xMax, yMin, yMax, zMin, zMax;

	GLU glu;

	// Largura e altura da tela
	private int screenWidth;
	private int screenHeight;


	//ATENÇÃO, IMPORTANTE !!!!!!!
	//CONFIGURAR O PATH (CAMINHO/DIRETÓRIO/PASTA) DAS TEXTURAS CORRETAMENTE, NO EXEMPLO ABAIXO
	// FOI UTILIZADO O PATH ABSOLUTO DO ARQUIVO, COM A IDE (INTELLIJ) BASTA CLICAR COM O BOTÃO DIREITO NO ARQUIVO
	// CLICAR EM "Copy path/refence" E CLICAR EM "Copy Absolute path" e MUDAR A FINAL STRING CORRETAMENTE
	//Controle e configuração de Texturas
	private Textura textura;
	private final String texturaPlanoDeFundo = "src/imagens/grama.png";
	private final String texturaMenu = "src/imagens/menu.png";

	//Variaveis de Texturas
	private TextRenderer textRendererTitulo;
	private TextRenderer textRendererTexto;

	//Variavel de pontuação do jogador
	public int pontos;

	// Construtor que recebe largura e altura da tela
	public FimDeCena(int screenWidth, int screenHeight) {
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

		// Desenha Menu
		textura = new Textura(1);
		textura.setFiltro(GL2.GL_LINEAR);
		textura.setModo(GL2.GL_DECAL);
		textura.setWrap(GL2.GL_REPEAT);

		textura.gerarTextura(gl, texturaMenu, 0);
		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(-0.7f, 0.7f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(0.7f, 0.7f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(0.7f, -0.7f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(-0.7f, -0.7f);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

		desenhaTitulo(gl, 0, 680, Color.WHITE, "Fim de Jogo");
		desenhaTexto(gl, "Pontuação: " + pontos);

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

		textRendererTitulo = new TextRenderer(new Font("Comic Sans MS Negrito", Font.BOLD, 30));
		textRendererTexto = new TextRenderer(new Font("Comic Sans MS", Font.ITALIC, 30));

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
