package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

public class SelectEvent<T> {

	private final T oldObject;

	private final T newObject;

	public SelectEvent(T oldObject, T newObject) {
		super();
		this.oldObject = oldObject;
		this.newObject = newObject;
	}

	public T getOldObject() {
		return oldObject;
	}

	public T getNewObject() {
		return newObject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newObject == null) ? 0 : newObject.hashCode());
		result = prime * result + ((oldObject == null) ? 0 : oldObject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SelectEvent<?> other = (SelectEvent<?>) obj;
		if (newObject == null) {
			if (other.newObject != null) {
				return false;
			}
		}
		else if (!newObject.equals(other.newObject)) {
			return false;
		}
		if (oldObject == null) {
			if (other.oldObject != null) {
				return false;
			}
		}
		else if (!oldObject.equals(other.oldObject)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SelectEvent [oldObject=" + oldObject + ", newObject=" + newObject + "]";
	}

}
