package madstodolist.model;

public class Tarea {
    private String titulo;
    private Usuario usuario;

    public Tarea(Usuario usuario, String titulo) {
        this.usuario = usuario;
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
