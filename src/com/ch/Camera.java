package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents an abstraction of a camera system. It encapsulates various transformations
 * and matrices for rendering scenes in 3D space. The class is designed to handle
 * camera positioning, rotation, and projection calculations.
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
	 * Calculates a view-projection matrix when necessary and returns it. It checks if
	 * the `viewProjectionMat4` is null or if the transformation has changed, and if so,
	 * calls the `calculateViewMatrix` method to recalculate the matrix.
	 *
	 * @returns a Matrix4f object, either newly calculated or retrieved from memory.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Computes a view matrix by multiplying a camera translation matrix with a conjugated
	 * and rotated transform, then with a projection matrix. The result is returned as a
	 * new matrix.
	 *
	 * @returns a combined view-projection matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Generates a translation matrix based on the camera's position, which is calculated
	 * by multiplying the original position by -1. The resulting matrix is initialized
	 * with the camera's X, Y, and Z coordinates.
	 *
	 * @returns a translation matrix initialized with negative camera position coordinates.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns a reference to an object of type `Transform`. The returned object is not
	 * modified by this function, and its state remains unchanged. This function provides
	 * access to the existing `transform` object without performing any calculations or
	 * modifications.
	 *
	 * @returns a `Transform` object that represents the transformation state.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that provides an interface for objects to implement. It defines
	 * two abstract methods: getAsMatrix4, which returns a Matrix4f object, and no further
	 * information about its purpose or use cases can be determined from the given code.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
