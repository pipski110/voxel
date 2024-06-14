package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an extension of the Camera class with additional functionality to handle 3D
 * camera inputs. It has a constructor that takes fov, aspect, zNear, and zFar
 * parameters to calculate the projection matrix, and an adjustToViewport method to
 * set the viewport size. The processInput method processes keyboard and mouse input
 * to rotate and move the camera.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Calculates a matrix that represents the projection of a 3D scene onto a 2D image
	 * plane, based on provided camera data.
	 * 
	 * @param data 3D camera parameters for which the projection matrix should be calculated.
	 * 
	 * @returns a Matrix4f object containing the camera's projection matrix.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts the camera's projection and view matrices to fit within the viewport
	 * specified by the user. It also sets the viewport size and positions the camera at
	 * the origin.
	 * 
	 * @param width 2D viewport width for which the projection and view matrices are
	 * calculated and then set as the GL_VIEWPORT value.
	 * 
	 * @param height 2D viewport size, which is used to calculate the appropriate projection
	 * and view matrices for the 3D scene.
	 */
	@Override
	public void adjustToViewport(int width, int height) {
		((CameraStruct3D) this.values).aspect = (float) width / height;
		calculateProjectionMatrix(values);
		try {
			calculateViewMatrix();
		} catch (NullPointerException e) {
		}
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Is a custom implementation of the Camera Struct for a 3D environment. It contains
	 * four instance fields: fov, aspect, zNear, and zFar, which are used to calculate
	 * the perspective projection matrix in the calculateProjectionMatrix method. The
	 * class also includes a getAsMatrix4 method that returns the perspective projection
	 * matrix as a Matrix4f object.
	 */
	protected class CameraStruct3D extends CameraStruct {

		public float fov, aspect, zNear, zFar;

		public CameraStruct3D(float fov, float aspect, float zNear, float zFar) {
			this.fov = fov;
			this.aspect = aspect;
			this.zNear = zNear;
			this.zFar = zFar;
		}

		/**
		 * Initializes a matrix representing a perspective projection with field of view
		 * (fov), aspect ratio, near and far distances.
		 * 
		 * @returns a matrix representation of a perspective projection, initialized with the
		 * specified field of view, aspect ratio, near and far distances.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Processes mouse input and keyboard input to move a 3D object based on user inputs.
	 * 
	 * @param dt time step of the animation, which determines the amount of movement the
	 * object undergoes during each frame.
	 * 
	 * @param speed 3D movement speed of the object being controlled by the code, and it
	 * is multiplied by the time elapsed (represented by `dt`) to determine the total
	 * distance traveled during the frame.
	 * 
	 * @param sens sensitivity of the mouse input, which determines how much the character
	 * will move based on the distance of the mouse cursor from the center of the screen.
	 */
	public void processInput(float dt, float speed, float sens) {

		float dx = Mouse.getDX();
		float dy = Mouse.getDY();
		float roty = (float)Math.toRadians(dx * sens);
		getTransform().rotate(new Vector3f(0, 1, 0), (float) roty);
		getTransform().rotate(getTransform().getRot().getRight(), (float) -Math.toRadians(dy * sens));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			speed *= 10;
		
		float movAmt = speed * dt;

		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			move(getTransform().getRot().getForward(), movAmt);
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			move(getTransform().getRot().getForward(), -movAmt);
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			move(getTransform().getRot().getLeft(), movAmt);
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			move(getTransform().getRot().getRight(), movAmt);
		
	}

	/**
	 * Moves an entity by a specified amount along a given direction, using the transform's
	 * `setPos()` method.
	 * 
	 * @param dir 3D direction in which the object should be moved, with the movement
	 * amount being applied to its position.
	 * 
	 * @param amt amount of movement along the specified direction, which is added to the
	 * current position of the transform.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
