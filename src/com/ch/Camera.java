package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract class that provides a basic structure for a camera system in a 3D
 * graphics environment. It maintains matrices for projection and view projection
 * transformations. The class allows for customization through abstract methods for
 * calculating the projection matrix and adjusting to the viewport dimensions.
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
	 * Returns a matrix that combines the current view and projection transformations.
	 * If the view projection matrix is null or if the transformation has changed, it
	 * recalculates the view matrix before returning the combined result.
	 *
	 * @returns a Matrix4f object representing the combined view and projection matrices.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Generates a view matrix by multiplying three matrices: the conjugate of the camera's
	 * rotation, the translation matrix, and the projection matrix. The result is then
	 * stored in the `viewProjectionMat4`. This combines the camera's transformation with
	 * the projection transformation.
	 *
	 * @returns a Matrix4f representing the combined view and projection transformations.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Calculates a translation matrix based on the position of a camera, represented by
	 * the `transform.getTransformedPos()` method. The calculated position is negated and
	 * used to initialize a new `Matrix4f` object with a translation transformation.
	 *
	 * @returns a 4x4 matrix representing a translation transformation.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Retrieves a reference to the `transform` object and returns it as a result. The
	 * returned value is accessible for further processing or modification. This getter
	 * method provides read-only access to the internal state of the object.
	 *
	 * @returns an instance of a `Transform` class.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class used to define a structure for camera-related data. It provides
	 * an abstract method getAsMatrix4() which returns a Matrix4f object representing the
	 * camera's data.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
