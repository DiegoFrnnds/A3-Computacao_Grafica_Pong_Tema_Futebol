import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class Objetos {

	public void CriarPlanoDeFundo(GL2 gl) {
		gl.glPushMatrix();

		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(-1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(-1.0f, -1.0f);
		gl.glEnd();

		gl.glPopMatrix();
	}

	public void CriarGradeDePontuacao(GL2 gl) {
		gl.glPushMatrix();

		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(-1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(1.0f, 0.8f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(-1.0f, 0.8f);
		gl.glEnd();

		gl.glPopMatrix();
	}

	public void CriarBolinha(GLU glu, GL2 gl, float sphereX, float sphereY, float sphereRadius, float rotationAngle) {
		gl.glPushMatrix();

		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(quad, true); // Habilita textura
		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);

		gl.glTranslatef(sphereX, sphereY, 0f); // Define a posição da esfera

		// Adiciona a rotação em graus ao redor do eixo x y
		gl.glRotatef(rotationAngle, 1.0f, 1.0f, 0.0f);

		glu.gluSphere(quad, sphereRadius, 15, 15);
		glu.gluDeleteQuadric(quad);

		gl.glPopMatrix();
	}

	public void CriarVidas(GLU glu, GL2 gl) {
		gl.glPushMatrix();

		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(quad, true);
		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);
		glu.gluSphere(quad, 0.03f, 15, 15);
		glu.gluDeleteQuadric(quad);

		gl.glPopMatrix();
	}

	public void CriarObstaculo(GL2 gl) {
		gl.glPushMatrix();

		gl.glBegin(GL2.GL_POLYGON);
			gl.glTexCoord2f(0.00f, 1.00f); gl.glVertex2f(0.30f, 0.40f);
			gl.glTexCoord2f(0.00f, 0.00f); gl.glVertex2f(0.30f, 0.00f);
			gl.glTexCoord2f(0.25f, 0.00f); gl.glVertex2f(0.24f, 0.00f);
			gl.glTexCoord2f(0.25f, 0.75f); gl.glVertex2f(0.24f, 0.30f);
			gl.glTexCoord2f(0.50f, 0.75f); gl.glVertex2f(0.0f, 0.30f);
			gl.glTexCoord2f(0.50f, 1.00f); gl.glVertex2f(0.0f, 0.40f);
		gl.glEnd();

		gl.glBegin(GL2.GL_POLYGON);
			gl.glTexCoord2f(1.00f, 1.00f); gl.glVertex2f(-0.30f, 0.40f);
			gl.glTexCoord2f(1.00f, 0.00f); gl.glVertex2f(-0.30f, 0.00f);
			gl.glTexCoord2f(0.75f, 0.00f); gl.glVertex2f(-0.24f, 0.00f);
			gl.glTexCoord2f(0.75f, 0.75f); gl.glVertex2f(-0.24f, 0.30f);
			gl.glTexCoord2f(0.50f, 0.75f); gl.glVertex2f(0.0f, 0.30f);
			gl.glTexCoord2f(0.50f, 1.00f); gl.glVertex2f(0.0f, 0.40f);
		gl.glEnd();

		gl.glPopMatrix();
	}

}
