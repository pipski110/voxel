package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents an abstract camera object that provides a framework for managing
 * projection and view matrices, as well as transformations.
 * It relies on subclasses to implement specific camera behaviors and settings through
 * abstract methods.
 * This class enables the calculation of combined view-projection matrices based on
 * camera transformations and projection data.
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
	 * Caches or recalculates a combined view and projection matrix based on changes to
	 * the object's transform, returning it when requested. The cached result is reused
	 * if the underlying data has not changed since its last calculation.
	 *
	 * @returns a Matrix4f object representing the combined view and projection transformation.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix, combining camera rotation and translation with an existing
	 * projection matrix. The result is stored in `viewProjectionMat4`. The function
	 * involves matrix multiplication operations to obtain the combined transformation.
	 *
	 * @returns a matrix representing a view-projection transformation.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Creates a translation matrix based on the negative of an object's position,
	 * transformed by its parent transform. The result is a Matrix4f that can be used to
	 * translate space without rotating or scaling it. A new Matrix4f instance is returned
	 * with its translation component set accordingly.
	 *
	 * @returns a translation matrix representing the camera's position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns a reference to an object of type `Transform`, which is stored in the
	 * variable `transform`. The returned object allows access to its properties and
	 * methods, enabling modification or querying of its attributes as needed.
	 *
	 * @returns an object of type `Transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a container for camera-related data. It
	 * encapsulates the necessary information to represent various types of cameras, and
	 * its instances can be converted to a Matrix4f object. The class is intended to be
	 * subclassed by specific camera implementations.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
