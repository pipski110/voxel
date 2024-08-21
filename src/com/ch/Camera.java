package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents an abstract camera model for managing and transforming graphical data
 * in three-dimensional space. It combines projection and view matrices to create a
 * view-projection matrix, allowing for efficient rendering of scenes from different
 * perspectives. This class also provides methods for calculating specific matrices
 * and adjusting the camera's position and orientation based on its transform.
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
	 * Calculates and returns a matrix representing the combined view and projection
	 * transformations, if necessary, considering whether a view matrix has changed or
	 * not. It ensures consistency by recalculating the view matrix when changes occur.
	 * The calculated result is stored for subsequent use.
	 *
	 * @returns a `Matrix4f` object.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Generates a view matrix by combining camera rotation and translation. It first
	 * extracts rotation and translation from the provided transform, then conjugates the
	 * rotation to obtain the inverse rotation matrix. The result is multiplied with the
	 * projection matrix to produce the final view matrix.
	 *
	 * @returns a composite view-projection matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Creates a translation matrix for a camera based on its position. The camera's
	 * position is obtained by transforming and negating a given vector. The resulting
	 * values are then used to initialize a new translation matrix.
	 *
	 * @returns a 4x4 transformation matrix representing translation.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns an instance of a `Transform` class, which represents the transformation
	 * to be applied on a graphics object or other visual elements. This function does
	 * not modify any state and simply provides read-only access to the stored transform.
	 *
	 * @returns an instance of `Transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a blueprint for other classes to inherit from
	 * and implement. It provides a single abstract method getAsMatrix4() which returns
	 * a Matrix4f object.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
