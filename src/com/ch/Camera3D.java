package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Implements a camera system for a 3D game. It handles perspective projection, adjusts
 * to screen dimensions, and processes user input (mouse and keyboard) to rotate and
 * move the camera. The class also supports zooming with the Shift key.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Calculates and returns a projection matrix from a given `CameraStruct`. It takes
	 * a camera object as input, retrieves its internal matrix representation using
	 * `getAsMatrix4`, and assigns it to an instance variable named `projection`.
	 *
	 * @param data 4x4 transformation matrix that is used to create the projection matrix
	 * by assigning it to the `projection` variable.
	 *
	 * @returns a `Matrix4f` object.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts a camera's aspect ratio and projection matrix based on the given viewport
	 * dimensions. It also attempts to calculate the view matrix but catches any
	 * NullPointerExceptions that may occur. Finally, it sets the OpenGL viewport to the
	 * specified width and height.
	 *
	 * @param width 2D window's width, which is used to calculate the aspect ratio of the
	 * camera and set the viewport dimensions.
	 *
	 * @param height height of the viewport and is used to calculate the aspect ratio of
	 * the camera.
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
	 * Represents a camera struct with perspective projection capabilities. It encapsulates
	 * four floating-point values: field of view, aspect ratio, near clipping plane, and
	 * far clipping plane. The class provides an initialization method and a method to
	 * retrieve the corresponding matrix4f for perspective projection.
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
		 * Returns a matrix representing a perspective projection with the specified field-of-view
		 * (fov), aspect ratio, near clipping plane distance (zNear), and far clipping plane
		 * distance (zFar). The returned matrix is initialized using the provided parameters.
		 *
		 * @returns a new perspective matrix.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Rotates a 3D object based on mouse movement and translates it based on keyboard
	 * input. The rotation is proportional to the sensitivity and time since last frame,
	 * while translation is based on the speed and direction of movement.
	 *
	 * @param dt time interval since the last frame, used to calculate the movement amount
	 * based on the provided speed.
	 *
	 * @param speed movement speed of the object, which is multiplied by the delta time
	 * (`dt`) to calculate the actual movement amount (`movAmt`).
	 *
	 * @param sens sensitivity of mouse movement, which is used to calculate the rotation
	 * angles based on the mouse movement deltas `dx` and `dy`.
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
	 * Adds a vector to an object's current position by multiplying the given direction
	 * vector by a specified amount and then adding it to the current position. This
	 * operation is performed on the object's transformation, updating its position accordingly.
	 *
	 * @param dir 3D direction vector that is multiplied by the specified amount (`amt`)
	 * to determine the movement offset.
	 *
	 * @param amt amount by which the vector `dir` is multiplied to determine the
	 * displacement of the object's position.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
