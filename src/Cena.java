import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import textura.Textura;

import java.awt.Color;
import java.awt.Font;

public class Cena implements GLEventListener {

	//Tamanho da Tela e configurações do GL
	// Preenchimento
	public int mode = GL2.GL_FILL;
	public GLU glu;
	private float xMin, xMax, yMin, yMax, zMin, zMax;

	//Renderer para troca de tela e TextRenderer para uso de Textos
	private Renderer renderer;
	private TextRenderer textRenderer;


	//Define a Esfera (bolinha), suas posições e velocidade
	private float sphereRadius = 0.04f;
	private float sphereSpeed = 0.012f;
	private float sphereX = 0f, sphereY = 0f;
	public float sphereSpeedX = sphereSpeed, sphereSpeedY = sphereSpeed;
	public float sphereSpeedAuxX;
	public float sphereSpeedAuxY;
	public float rotationAngle = 1.0f;


	//Controle de tamanho e posição da Raquete
	public float pxPositivo = 0.15f, pxNegativo = -0.15f;
	private float pyBaseRaquete = -0.9f, pyCimaRaquete = -0.8f;


	//Criação de Objeto Obstáculo e Grade de pontuação
	private Objetos objetos;


	//Pontuação e Vidas
	public int vidas = 5;
	public int pontos = 0;



	//ATENÇÃO, IMPORTANTE !!!!!!!
	//CONFIGURAR O PATH (CAMINHO/DIRETÓRIO/PASTA) DAS TEXTURAS CORRETAMENTE, NO EXEMPLO ABAIXO
	// FOI UTILIZADO O PATH ABSOLUTO DO ARQUIVO, COM A IDE (INTELLIJ) BASTA CLICAR COM O BOTÃO DIREITO NO ARQUIVO
	// CLICAR EM "Copy path/refence" E CLICAR EM "Copy Absolute path" e MUDAR A FINAL STRING CORRETAMENTE
	//Controle e configuração de Texturas
	private Textura textura;
	private final String planoDeFundo = "src/imagens/grama.png";
	private final String placarDePontuacao = "src/imagens/pontuacao.png";
	private final String raquete = "src/imagens/raquete.png";
	private final String bolinhaEvidas = "src/imagens/bola.png";
	private final String gradeGol = "src/imagens/GradeGol.png";


	// Construtor da cena
	public Cena(Renderer renderer) {
		this.renderer = renderer;
		this.objetos = new Objetos();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		//obtem o contexto Opengl
		GL2 gl = drawable.getGL().getGL2();
		//define a cor da janela (R, G, G, alpha)
		gl.glClearColor(1, 1, 1, 1);
		//limpa a janela com a cor especificada
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glLoadIdentity(); //lê a matriz identidade


		// Configura o plano de fundo
		textura.gerarTextura(gl, planoDeFundo, 0);
		objetos.CriarPlanoDeFundo(gl);
		textura.desabilitarTextura(gl, 0);


		//Pontuação
		textura.gerarTextura(gl, placarDePontuacao, 0);
		objetos.CriarGradeDePontuacao(gl);
		textura.desabilitarTextura(gl, 0);


		// Raquete
		textura.gerarTextura(gl, raquete, 0);
		gl.glColor3f(0, 0, 1);
		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(pxPositivo, pyCimaRaquete);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(pxPositivo, pyBaseRaquete);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(pxNegativo, pyBaseRaquete);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(pxNegativo, pyCimaRaquete);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);


		// Movimentação da Esfera
		// Move a esfera
		sphereX += sphereSpeedX;
		sphereY += sphereSpeedY;

		// Desenha a esfera/bolinha na posição especificada
		rotationAngle += 0.5f;
		textura.gerarTextura(gl, bolinhaEvidas, 0);
		objetos.CriarBolinha(glu, gl, sphereX, sphereY, sphereRadius, rotationAngle);
		textura.desabilitarTextura(gl, 0);


		// Verifica as bordas da tela e inverte a direção de objeto se necessário
		//Bateu na parede
		if (sphereX + sphereRadius >= 1.0f || sphereX - sphereRadius <= -1.0f) {
			sphereSpeedX = -sphereSpeedX; // Inverte a direção horizontal
		}
		//Bateu no "teto"
		if (sphereY + sphereRadius >= 0.8f) {
			sphereSpeedY = -sphereSpeedY; // Inverte a direção vertical
		}


		//Interação Bolinha e Raquete
		//Passou da raquete
		if (sphereY - sphereRadius <= -1.0f) {
			vidas -= 1;
			sphereY = -0f;
			sphereX = -0f;
			sphereSpeedX = (Math.random() > 0.5) ? sphereSpeedX : -sphereSpeedX;
			sphereSpeedY = -sphereSpeedY;

		}

		// Verifica colisão da bolinha com a requete
		if (sphereX + sphereRadius >= pxNegativo && sphereX - sphereRadius <= pxPositivo &&
				sphereY + sphereRadius >= pyBaseRaquete && sphereY - sphereRadius <= pyCimaRaquete) {

			// Antes de mover a esfera, calcule a distância até a borda da raquete
			float distanceToRaqueteY = Math.abs(sphereY - pyCimaRaquete) - sphereRadius;
			float distanceToRaqueteXNegativo = Math.abs(sphereX - pxNegativo) - sphereRadius;
			float distanceToRaqueteXPositivo = Math.abs(sphereX - pxPositivo) - sphereRadius;

			// Se houver interpenetração, ajuste a posição da esfera
			if (distanceToRaqueteY < 0) {
				sphereY -= distanceToRaqueteY; // Ajusta a posição para evitar interpenetração
			} else if (distanceToRaqueteXNegativo <= 0) {
				sphereX -= distanceToRaqueteXNegativo;
			} else if (distanceToRaqueteXPositivo <= 0) {
				sphereX -= distanceToRaqueteXPositivo;
			}

			// Inverte a direção vertical
			sphereSpeedY = -sphereSpeedY;

			// Gera uma direção horizontal aleatória
			sphereSpeedX = (Math.random() > 0.5) ? sphereSpeedX : -sphereSpeedX;

			if (sphereY >= -0.9f) {
				pontos += 50;
			}
			if (pontos >= 200) {
				addSphereSpeed();
			}
		}

		//Fase2
		//Inicia a fase 2, desenha objeto obstáculo e verifica colisão com objeto
		if (pontos >= 200) {
			textura.gerarTextura(gl, gradeGol, 0);
			objetos.CriarObstaculo(gl);
			textura.desabilitarTextura(gl, 0);

			// Verifica colisão da esfera com o objeto
			if ((sphereX + sphereRadius >= -0.30f && sphereX - sphereRadius <= 0.30f &&
					sphereY + sphereRadius >= 0.30f && sphereY - sphereRadius <= 0.40f) ||
					(sphereX + sphereRadius >= -0.30f && sphereX - sphereRadius <= -0.24f &&
							sphereY + sphereRadius >= 0.00f && sphereY - sphereRadius <= 0.40f) ||
					(sphereX + sphereRadius >= 0.24f && sphereX - sphereRadius <= 0.30f &&
							sphereY + sphereRadius >= 0.00f && sphereY - sphereRadius <= 0.40f)

			) {

				// Antes de mover a esfera, calcule a distância até a borda da raquete
				float distanceToCimaGrade1 = Math.abs(sphereY - 0.40f) - sphereRadius;
				float distanceToCimaGrade2 = Math.abs(sphereY - 0.30f) - sphereRadius;
				float distanceToCimaGrade3 = Math.abs(sphereY - 0.00f) - sphereRadius;
				float distanceToGradeEsquerda1 = Math.abs(sphereX - (-0.30f)) - sphereRadius;
				float distanceToGradeEsquerda2 = Math.abs(sphereX - (-0.24f)) - sphereRadius;
				float distanceToGradeDireita1 = Math.abs(sphereX - 0.24f) - sphereRadius;
				float distanceToGradeDireita2 = Math.abs(sphereX - 0.30f) - sphereRadius;


				// Se houver interpenetração, ajuste a posição da esfera
				if (distanceToCimaGrade1 < 0 || distanceToCimaGrade2 < 0 || distanceToCimaGrade3 < 0) {
					sphereSpeedY = -sphereSpeedY;
				} else if (distanceToGradeDireita1 < 0) {
					sphereX -= distanceToGradeDireita1;
					sphereSpeedX = -sphereSpeedX;
				} else if (distanceToGradeDireita2 < 0) {
					sphereX -= distanceToGradeDireita2;
					sphereSpeedX = -sphereSpeedX;
				} else if (distanceToGradeEsquerda1 < 0) {
					sphereX -= distanceToGradeEsquerda1;
					sphereSpeedX = -sphereSpeedX;
				} else if (distanceToGradeEsquerda2 < 0) {
					sphereX -= distanceToGradeEsquerda2;
					sphereSpeedX = -sphereSpeedX;
				}
			}
		}

		//Desenho das Vidas
		// Desenha o objeto de vidas com base no número atual de vidas
		float posicaoVerticalVidas = 0.9f; //Posição X em relação a tela
		float posicaoHorizontalVidas = 0.9f; //Posição Y em relação a tela
		float espacoEntreVidas = 0.1f;

		for (int i = 0; i < vidas; i++) {
			gl.glPushMatrix();
			textura.gerarTextura(gl, bolinhaEvidas, 0);
			gl.glTranslatef(posicaoHorizontalVidas - i * espacoEntreVidas, posicaoVerticalVidas, 0);
			objetos.CriarVidas(glu, gl);
			textura.desabilitarTextura(gl, 0);
			gl.glPopMatrix();
		}

		// Verifica se a vida chegou a zero
		if (vidas <= 0) {
			fimDeCena();
		}

		int posicaoTextoPontuacaoHorizontal = 100;
		int posicaoTextoPontuacaoVertical = Renderer.getWindowHeight() - (Renderer.getWindowHeight() / 18);

		desenhaTexto(gl, posicaoTextoPontuacaoHorizontal, posicaoTextoPontuacaoVertical, Color.BLACK, "Pontos: " + pontos);

		gl.glFlush();
	}

	//Controle de Bolinha, velocidade e pause
	//Aumenta a Velocidade da bolinha
	public void addSphereSpeed() {
		if (this.sphereSpeedY <= 0.02f || this.sphereSpeedX <= 0.02f) {
			this.sphereSpeed += 0.002f;
			this.sphereSpeedY += 0.002f;
			this.sphereSpeedX += 0.002f;
		}
	}

	//Função para controle da velocidade da bolinha ao usar o "Stop"
	public void setSphereSpeed() {
		if (this.sphereSpeedY != 0 || this.sphereSpeedX != 0) {
			this.sphereSpeedAuxY = this.sphereSpeedY;
			this.sphereSpeedAuxX = this.sphereSpeedX;
			this.sphereSpeedY = 0.0f;
			this.sphereSpeedX = 0.0f;
		} else {
			this.sphereSpeedY = this.sphereSpeedAuxY;
			this.sphereSpeedX = this.sphereSpeedAuxX;
		}
	}

	public void desenhaTexto(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		//Retorna a largura e altura da janela
		textRenderer.beginRendering(Renderer.getWindowWidth(), Renderer.getWindowHeight());
		textRenderer.setColor(cor);
		textRenderer.draw(frase, xPosicao, yPosicao);
		textRenderer.endRendering();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, mode);
	}

	public void fimDeCena() {
		renderer.switchToFimDeCena(this.pontos);
		renderer.window.addGLEventListener(renderer.fimDeCena);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
//        GL2 gl = drawable.getGL().getGL2();

		glu = new GLU();
		xMin = yMin = zMin = -1;
		xMax = yMax = zMax = 1;

		textRenderer = new TextRenderer(new Font("Comic Sans MS Negrito", Font.BOLD, 15));
		//Habilita o buffer de profundidade
//        gl.glEnable(GL2.GL_DEPTH_TEST);

		textura = new Textura(1);
		textura.setFiltro(GL2.GL_LINEAR);
		textura.setModo(GL2.GL_DECAL);
		textura.setWrap(GL2.GL_REPEAT);
//        planoDeFundoTextura.gerarTextura(gl, "C:\\Users\\rafae\\OneDrive\\Área de Trabalho\\Teste-switchCase\\OpenGl-Game\\src\\grama.jpg", 0);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		//obtem o contexto grafico Opengl
		GL2 gl = drawable.getGL().getGL2();

		//evita a divisão por zero
		if (height == 0) height = 1;
		//calcula a proporção da janela (aspect ratio) da nova janela
		float aspect = (float) width / height;

		//seta o viewport para abranger a janela inteira
		gl.glViewport(0, 0, width, height);

		//ativa a matriz de projeção
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity(); //lê a matriz identidade

		//ativa a matriz de modelagem
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); //lê a matriz identidade
		System.out.println("Reshape: " + width + ", " + height);

		//Define a Proporção de tela
		float unitsTall = Renderer.getWindowHeight() / Renderer.getWindowWidth() / Renderer.unitsWide;

		if (width >= height)
			gl.glOrtho(-Renderer.unitsWide / 2, Renderer.unitsWide / 2,
					-unitsTall / 2, unitsTall / 2, -1, 1);
		else
			gl.glOrtho(xMin, xMax, yMin / aspect, yMax / aspect, zMin, zMax);
	}

}
