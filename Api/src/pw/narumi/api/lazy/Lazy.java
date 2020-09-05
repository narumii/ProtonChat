package pw.narumi.api.lazy;

public abstract class Lazy<T> {
    private T toLoad;

    public T get() {
        if (this.toLoad == null)
            this.toLoad = load();

        return this.toLoad;
    }

    public abstract T load();
}
