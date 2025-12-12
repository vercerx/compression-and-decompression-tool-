public class ArrayList<T> {

    private T[] data;
    private int size;

    public ArrayList() {
        data = (T[]) new Object[10];
        size = 0;
    }

    public void add(T value) {
        ensureCapacity();
        data[size++] = value;
    }

    public T get(int index) {
        checkIndex(index);
        return data[index];
    }

    public void set(int index, T value) {
        checkIndex(index);
        data[index] = value;
    }

    public void remove(T value) {
        int index = indexOf(value);
        if (index == -1) return;

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        size--;
    }

    public void clear() {
        data = (T[]) new Object[10];
        size = 0;
    }

    private int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            T[] newData = (T[]) new Object[data.length * 2];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
