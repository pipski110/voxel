package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract class that represents a camera in a 3D environment. It provides
 * methods for calculating matrices related to the camera's position and orientation,
 * such as view projection and translation. The class also defines an interface for
 * calculating a projection matrix and adjusting the camera to a specific viewport size.
 */
public abstract class Camera {

	protected Matrix4f projection;
	protected Matrix4f viewProjectionMat4;
	protected CameraStruct values;
	protected Transform transform;

	protected Camera(Matrix4f projection) {
		this.projection = projection;
		transform = new Transform();
	}

	/**
	 * Calculates a combined view and projection matrix if necessary, based on a specified
	 * transformation and returns it. The calculation is performed only when the previous
	 * result is null or the transformation has changed.
	 *
	 * @returns a Matrix4f representing the combined view and projection transformation.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix by multiplying the camera's rotation and translation
	 * matrices with the projection matrix, resulting in a transformed matrix that defines
	 * the camera's viewpoint. The output is then assigned to the `viewProjectionMat4`.
	 *
	 * @returns a transformed Matrix4f representing the view of a camera.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Computes a translation matrix from a given position vector. It multiplies the
	 * current transformed position by -1 to invert it, then initializes a new Matrix4f
	 * object with the resulting x, y, and z values as the translation parameters.
	 *
	 * @returns a 4x4 translation matrix representing the inverse of the camera position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns the current value of the `transform` variable. It provides read-only access
	 * to the transformation data without modifying it. The returned object is a snapshot
	 * of the current state of the transformation.
	 *
	 * @returns an object of type `Transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a base for other camera-related data structures.
	 * It defines an abstract method getAsMatrix4() that returns a Matrix4f object. This
	 * suggests that the CameraStruct class is used to encapsulate various types of
	 * camera-specific data that can be converted into a matrix representation.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
