import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyBoard implements KeyListener {

	private final Cena cena;
	private final InicioCena inicioCena;
	private final FimDeCena fimDeCena;
	private final Renderer renderer;
	private float movement = 0.05f;
	;
	public boolean stop = false;

	// Construtor para a Cena
	public KeyBoard(Cena cena) {
		this.cena = cena;
		this.fimDeCena = null;
		this.inicioCena = null;
		this.renderer = new Renderer();
	}

	public KeyBoard(FimDeCena fimDeCena) {
		this.cena = null;
		this.inicioCena = null;
		this.fimDeCena = fimDeCena;
		this.renderer = new Renderer();
	}

	// Construtor para a InicioCena
	public KeyBoard(InicioCena inicioCena) {
		this.cena = null;
		this.fimDeCena = null;
		this.inicioCena = inicioCena;
		this.renderer = null;
	}

	// Construtor para Cena e InicioCena e fim de cena
	public KeyBoard(Cena cena, InicioCena inicioCena, FimDeCena fimDeCena, Renderer renderer) {
		this.cena = cena;
		this.fimDeCena = fimDeCena;
		this.inicioCena = inicioCena;
		this.renderer = renderer;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Key pressed: " + e.getKeyCode());

		if (cena != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_S: //83
					cena.fimDeCena();
					break;
				case KeyEvent.VK_P: //80
					this.stop = !stop;
					cena.setSphereSpeed();

					if (stop) {
						this.movement = 0f;
					} else {
						this.movement = 0.05f;
					}
					break;
				case KeyEvent.VK_RIGHT: //151
					if (cena.pxPositivo <= 0.95) {
						cena.pxPositivo += movement;
						cena.pxNegativo += movement;
					}
					break;
				case KeyEvent.VK_LEFT: //149
					if (cena.pxNegativo >= -0.95) {
						cena.pxPositivo -= movement;
						cena.pxNegativo -= movement;
					}
					break;
			}
		}

		if (inicioCena != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_1: //49
					if (inicioCena.menu == 1) {
						renderer.switchToCena(); // Muda a tela para Cena
					}
					break;
				case KeyEvent.VK_2: //50
					inicioCena.menu = 2;
					break;
				case KeyEvent.VK_3: //51
					inicioCena.menu = 3;
					break;
				case KeyEvent.VK_4: //52
					inicioCena.menu = 4;
					break;
				case KeyEvent.VK_5: //53
					inicioCena.menu = 1;
					break;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) renderer.toggleFullscreen(); // Troca entre fullscreen e janela
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
