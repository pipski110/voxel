package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract base class for managing camera transformations in a 3D environment.
 * It maintains matrices for projection and view projections, as well as a transformation
 * object to store the camera's position and rotation.
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
	 * Returns a matrix representing the combined view and projection transformations.
	 * If no cached result is available or if the transformation has changed, it calculates
	 * the view matrix using the `calculateViewMatrix` method before returning the combined
	 * view-projection matrix.
	 *
	 * @returns a `Matrix4f` object representing the view-projection matrix.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix by combining camera rotation and translation matrices.
	 * It takes the conjugate of the camera's rotated transform, creates a translation
	 * matrix from the camera's position, and then multiplies these two matrices with the
	 * projection matrix to produce the final view matrix.
	 *
	 * @returns a Matrix4f representing the view matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Calculates a translation matrix based on a given camera position, which is obtained
	 * by multiplying the transformed position of an object with -1. It then initializes
	 * a new Matrix4f object with the resulting x, y, and z coordinates to represent a
	 * translation transformation.
	 *
	 * @returns a translation matrix with specified x, y, z coordinates.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Retrieves and returns an instance of a `Transform` object. This object is stored
	 * as a class variable or field named `transform`. The function does not perform any
	 * operations, but rather provides read-only access to the existing transform object.
	 *
	 * @returns an instance of `Transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a structure for camera-related data. It provides
	 * a base template for concrete implementations to inherit from and extend its
	 * functionality. This class defines a single abstract method getAsMatrix4, which
	 * allows derived classes to convert their internal state into a Matrix4f object.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
